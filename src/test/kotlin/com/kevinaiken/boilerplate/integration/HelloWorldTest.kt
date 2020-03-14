package com.kevinaiken.boilerplate.integration

import com.kevinaiken.boilerplate.get
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.flywaydb.test.annotation.FlywayTest
import org.junit.Test
import org.junit.runner.RunWith
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FlywayTest
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
class HelloWorldTest {
    @LocalServerPort
    val serverPort: Int = 0
    val baseUrl: String by lazy { "http://localhost:$serverPort/api" }

    @Test
    fun getHelloWorldTest() {
        JSONAssert.assertEquals(
                "{\"response\": \"Hello, World\"}",
                get("$baseUrl/hello").body,
                JSONCompareMode.LENIENT
        )
    }
}