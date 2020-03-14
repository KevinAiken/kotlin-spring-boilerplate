package com.kevinaiken.boilerplate.integration

import com.kevinaiken.boilerplate.delete
import com.kevinaiken.boilerplate.get
import com.kevinaiken.boilerplate.model.Item
import com.kevinaiken.boilerplate.model.UpdatedItemFields
import com.kevinaiken.boilerplate.post
import com.kevinaiken.boilerplate.put
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.flywaydb.test.annotation.FlywayTest
import org.junit.Test
import org.junit.runner.RunWith
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.client.HttpClientErrorException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FlywayTest
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
class ItemTest {
    @LocalServerPort
    val serverPort: Int = 0
    val baseUrl: String by lazy { "http://localhost:$serverPort/api/item" }

    @Test
    fun itemSmokeTest() {
        // create one item
        JSONAssert.assertEquals(
                "{\"name\":\"apple\",\"description\":\"a fruit\"}",
                post("http://localhost:$serverPort/api/item", body = Item("apple", "a fruit")).body,
                JSONCompareMode.LENIENT
        )

        // create another
        JSONAssert.assertEquals(
                "{\"name\":\"onion\",\"description\":\"a vegetable\"}",
                post(baseUrl, body = Item("onion", "a vegetable")).body,
                JSONCompareMode.LENIENT
        )

        // get an item
        JSONAssert.assertEquals(
                "{\"name\":\"apple\",\"description\":\"a fruit\"}",
                get("$baseUrl/apple").body,
                JSONCompareMode.LENIENT
        )

        // get both items
        JSONAssert.assertEquals(
                "[{\"name\":\"apple\",\"description\":\"a fruit\"}," +
                        " {\"name\":\"onion\",\"description\":\"a vegetable\"}]",
                get(baseUrl).body,
                JSONCompareMode.LENIENT
        )

        // update an item
        JSONAssert.assertEquals(
                "{\"name\":\"onion\",\"description\":\"a vegetable, sometimes known as the bulb onion\"}",
                put(
                        "$baseUrl/onion",
                        body = UpdatedItemFields("a vegetable, sometimes known as the bulb onion")
                ).body,
                JSONCompareMode.LENIENT
        )

        // delete an item
        assertEquals(delete("$baseUrl/apple").statusCodeValue, 200)

        // confirm it's deleted
        assertFailsWith<HttpClientErrorException.NotFound>(block = { get("$baseUrl/apple") })
    }
}