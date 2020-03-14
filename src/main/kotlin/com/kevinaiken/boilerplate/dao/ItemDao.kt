package com.kevinaiken.boilerplate.dao

import com.kevinaiken.boilerplate.createLogger
import com.kevinaiken.boilerplate.model.Item
import com.kevinaiken.boilerplate.model.ItemMapper
import com.kevinaiken.boilerplate.model.NewItem
import com.kevinaiken.boilerplate.model.UpdatedItemFields
import org.intellij.lang.annotations.Language
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ItemDao(@Autowired var conn: JdbcTemplate) {
    private val logger = createLogger()

    fun createItem(newItem: NewItem) {
        @Language("PostgreSQL") val query = "INSERT INTO item (name, description) VALUES (?, ?)"
        conn.update(query, newItem.name, newItem.description)
    }

    fun getItem(itemName: String): Item {
        @Language("PostgreSQL") val query = "SELECT name, description FROM item WHERE name = ?"
        return conn.queryForObject(query, ItemMapper(), itemName)!!
    }

    fun getItems(): List<Item> {
        @Language("PostgreSQL") val query = "SELECT name, description FROM item"
        return conn.query(query, ItemMapper())
    }

    fun updateItem(itemName: String, updatedFields: UpdatedItemFields) {
        @Language("PostgreSQL") val query = "UPDATE item SET description = ? WHERE name = ?"
        conn.update(query, updatedFields.description, itemName)
    }

    fun deleteItem(itemName: String) {
        @Language("PostgreSQL") val query = "DELETE FROM item where name = ?"
        conn.update(query, itemName)
    }
}