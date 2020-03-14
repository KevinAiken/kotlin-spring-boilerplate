package com.kevinaiken.boilerplate.controller

import com.kevinaiken.boilerplate.createLogger
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/hello")
class HelloWorldController {

    private val logger = createLogger()

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getHello(): String {
        logger.info("Hello world triggered")
        return "{\"response\": \"Hello, World\"}"
    }
}