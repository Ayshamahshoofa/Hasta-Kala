package com.example.hastakala

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HastaKalaDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Query("UPDATE products SET stock = :newStock WHERE id = :productId")
    suspend fun updateStock(productId: String, newStock: Int)

    @Query("SELECT * FROM sales ORDER BY date DESC")
    fun getAllSales(): Flow<List<Sale>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: Sale)

    @Transaction
    suspend fun recordSale(sales: List<Sale>) {
        sales.forEach { sale ->
            insertSale(sale)
            // Note: In a real app, you'd fetch the product first to ensure stock is available
            // but for simplicity we'll just execute the update
            // We use the product update logic in the ViewModel usually
        }
    }
}

@Database(entities = [Product::class, Sale::class], version = 2)
@TypeConverters(Converters::class)
abstract class HastaKalaDatabase : RoomDatabase() {
    abstract fun dao(): HastaKalaDao

    companion object {
        @Volatile
        private var INSTANCE: HastaKalaDatabase? = null

        fun getDatabase(context: Context): HastaKalaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HastaKalaDatabase::class.java,
                    "hastakala_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
