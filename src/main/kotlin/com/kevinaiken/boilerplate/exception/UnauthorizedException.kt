package com.kevinaiken.boilerplate.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class UnauthorizedException : RuntimeException {
    constructor() : super()
    constructor(msg: String) : super(msg)
    constructor(msg: String, exc: Throwable) : super(msg, exc)
    constructor(exc: Throwable) : super(exc)
}
