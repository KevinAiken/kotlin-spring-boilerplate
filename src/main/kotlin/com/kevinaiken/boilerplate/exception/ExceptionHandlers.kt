package com.kevinaiken.boilerplate.exception

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.kevinaiken.boilerplate.createLogger
import org.postgresql.util.PSQLException
import org.slf4j.Logger
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.jdbc.BadSqlGrammarException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.Instant

data class ErrorResponse(
        val timestamp: Instant = Instant.now(),
        val status: Int,
        val error: String,
        val fieldErrors: Map<String, String>? = null,
        val message: String,
        val path: String
)

enum class ErrorKind {
    /** typical client errors */
    User,
    /** potential malicious client */
    Security,
    /** potential internal bug */
    Internal
}

private fun categorizeException(ex: Throwable) =
        when (ex) {
            // 400
            is BadRequestException ->
                Pair(ErrorKind.User, HttpStatus.BAD_REQUEST)
            // 401
            is InvalidLoginException, is UnauthorizedException ->
                Pair(ErrorKind.Security, HttpStatus.UNAUTHORIZED)
            // 403
            is ForbiddenRequestException ->
                Pair(ErrorKind.Security, HttpStatus.FORBIDDEN)
            // 404
            is EmptyResultDataAccessException, is NotFoundException ->
                Pair(ErrorKind.User, HttpStatus.NOT_FOUND)
            // 409
            is ConflictException ->
                Pair(ErrorKind.User, HttpStatus.CONFLICT)
            // 500
            is NullPointerException, is IllegalArgumentException,
            is IllegalStateException, is DataIntegrityViolationException,
            is BadSqlGrammarException, is PSQLException ->
                Pair(ErrorKind.Internal, HttpStatus.INTERNAL_SERVER_ERROR)
            else -> Pair(ErrorKind.Internal, HttpStatus.INTERNAL_SERVER_ERROR)
        }

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    private val exceptionHandlerLogger = createLogger()

    override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val cause = ex.cause
        if (cause is MissingKotlinParameterException) {
            val paramName = cause.parameter.name
            if (paramName != null) {
                return handleHttpException(
                        ErrorKind.User, ex, request, HttpStatus.BAD_REQUEST,
                        mapOf(paramName to "can't be missing"))
            }
        }
        return handleHttpException(
                ErrorKind.User, ex, request, HttpStatus.BAD_REQUEST)
    }

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val fieldErrors = ex.bindingResult.allErrors.associate { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.getDefaultMessage() ?: "Invalid field"

            fieldName to errorMessage
        }
        return handleHttpException(
                ErrorKind.User, ex, request, HttpStatus.BAD_REQUEST, fieldErrors)
    }

    @ExceptionHandler
    protected fun handleException(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        val (errorKind, statusCode) = categorizeException(ex)
        return handleHttpException(errorKind, ex, request, statusCode)
    }

    fun handleHttpException(
            errorKind: ErrorKind,
            ex: Exception,
            request: WebRequest,
            httpStatus: HttpStatus,
            fieldErrors: Map<String, String>? = null
    ): ResponseEntity<Any> {
        getLoggingForError(exceptionHandlerLogger, errorKind)(
                "Error $errorKind on ${(request as ServletWebRequest).httpMethod}: " +
                        "request to ${request.request.requestURI} " +
                        "${httpStatus.value()}'d", ex)

        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_JSON

        val bodyOfResponse = ErrorResponse(
                status = httpStatus.value(),
                error = httpStatus.reasonPhrase,
                message = ex.localizedMessage ?: ex.message ?: "Unknown",
                path = request.request.requestURI,
                fieldErrors = fieldErrors)

        return handleExceptionInternal(ex, bodyOfResponse, httpHeaders, httpStatus, request)
    }
}

private fun getLoggingForError(
        logger: Logger,
        errorKind: ErrorKind
): (String, Throwable) -> Unit = when (errorKind) {
    ErrorKind.User -> logger::info
    ErrorKind.Security -> logger::error
    ErrorKind.Internal -> logger::error
}
