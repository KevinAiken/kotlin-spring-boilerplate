package com.kevinaiken.boilerplate.model

import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

data class Item(
    val name: String,
    val description: String
)

data class NewItem(
    val name: String,
    val description: String
)

data class UpdatedItemFields(
    val description: String
)

class ItemMapper : RowMapper<Item> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Item {
        return Item(
                name = rs.getString("name"),
                description = rs.getString("description")
        )
    }
}
