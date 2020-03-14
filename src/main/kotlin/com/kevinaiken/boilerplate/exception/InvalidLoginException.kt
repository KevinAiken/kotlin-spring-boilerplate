package com.kevinaiken.boilerplate.exception

class InvalidLoginException : RuntimeException {
    constructor() : super("Invalid username or password")
    constructor(msg: String) : super(msg)
    constructor(msg: String, exc: Throwable) : super(msg, exc)
    constructor(exc: Throwable) : super(exc)
}
