package com.kevinaiken.boilerplate.controller

import com.kevinaiken.boilerplate.dao.ItemDao
import com.kevinaiken.boilerplate.model.Item
import com.kevinaiken.boilerplate.model.NewItem
import com.kevinaiken.boilerplate.model.UpdatedItemFields
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/item")
class ItemController @Autowired
constructor(val itemDao: ItemDao) {
    @PostMapping
    fun createItem(@RequestBody item: NewItem): Item {
        itemDao.createItem(item)
        return itemDao.getItem(item.name)
    }

    @GetMapping
    fun getItems(): List<Item> {
        return itemDao.getItems()
    }

    @GetMapping("/{itemName}")
    fun getItem(@PathVariable(value = "itemName") itemName: String): Item {
        return itemDao.getItem(itemName)
    }

    @PutMapping("{itemName}")
    fun updateItem(@PathVariable itemName: String, @RequestBody itemUpdateFields: UpdatedItemFields): Item {
        itemDao.updateItem(itemName, itemUpdateFields)
        return itemDao.getItem(itemName)
    }

    @DeleteMapping("/{itemName}")
    fun deleteItem(@PathVariable(value = "itemName") itemName: String) {
        itemDao.deleteItem(itemName)
    }
}
