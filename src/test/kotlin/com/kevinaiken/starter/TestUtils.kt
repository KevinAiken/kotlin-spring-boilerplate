package com.kevinaiken.starter

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

fun get(url: String, token: String? = null): ResponseEntity<String> {
    val restTemplate = RestTemplate()
    val headers = HttpHeaders()
    headers.set("Authorization", "Bearer $token")
    return restTemplate.exchange(url, HttpMethod.GET, HttpEntity<Any>(headers), String::class.java)
}

fun put(url: String, token: String? = null, body: Any? = null): ResponseEntity<String> {
    val restTemplate = RestTemplate()
    val headers = HttpHeaders()
    headers.set("Authorization", "Bearer $token")
    return restTemplate.exchange(url, HttpMethod.PUT, HttpEntity<Any>(body, headers), String::class.java)
}

fun post(url: String, token: String? = null, body: Any? = null): ResponseEntity<String> {
    val restTemplate = RestTemplate()
    val headers = HttpHeaders()
    headers.set("Authorization", "Bearer $token")
    return restTemplate.exchange(url, HttpMethod.POST, HttpEntity<Any>(body, headers), String::class.java)
}
