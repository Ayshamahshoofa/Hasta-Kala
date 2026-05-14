package com.example.hastakala

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.UUID

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val icon: String = "👜",
    val price: Double,
    var stock: Int,
    val colors: List<String>
)

@Entity(tableName = "sales")
data class Sale(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val productName: String,
    val productId: String,
    val color: String,
    val price: Double,
    val quantity: Int = 1,
    val date: String
)

data class CartItem(
    val product: Product,
    var quantity: Int,
    var selectedColor: String
)

class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(",").filter { it.isNotBlank() }
    }

    @TypeConverter
    fun listToString(list: List<String>): String {
        return list.joinToString(",")
    }
}
