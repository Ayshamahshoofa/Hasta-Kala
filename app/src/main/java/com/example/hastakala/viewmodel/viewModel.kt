package com.example.hastakala.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hastakala.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = HastaKalaDatabase.getDatabase(application).dao()

    val products: StateFlow<List<Product>> = dao.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val sales: StateFlow<List<Sale>> = dao.getAllSales()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addProduct(product: Product) {
        viewModelScope.launch {
            dao.insertProduct(product)
        }
    }

    fun updateStock(productId: String, newStock: Int) {
        viewModelScope.launch {
            dao.updateStock(productId, newStock)
        }
    }

    fun completeSale(newSales: List<Sale>) {
        viewModelScope.launch {
            newSales.forEach { sale ->
                dao.insertSale(sale)
                val product = products.value.find { it.id == sale.productId }
                if (product != null) {
                    dao.updateStock(product.id, product.stock - sale.quantity)
                }
            }
        }
    }

    fun seedDatabaseIfNeeded() {
        viewModelScope.launch {
            // Check if products are already present in a more robust way
            // For this app, we'll just check if the list is empty once it's loaded
            // But since stateIn starts with emptyList, we might need a better check
            // or just rely on a manual seed trigger if needed.
        }
    }
}
