package com.example.hastakala.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hastakala.Product
import com.example.hastakala.ui.theme.*

@Composable
fun InventoryScreen(
    products: List<Product>,
    onAddToCart: (Product) -> Unit,
    onAddProduct: (Product) -> Unit,
    onUpdateStock: (String, Int) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Stock & Inventory", color = BrandBrown, fontWeight = FontWeight.Black, fontSize = 24.sp)
                    Text("MANAGE YOUR CRAFTS", color = ArtisanTan, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                }
                IconButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Add, "Add Product", tint = BrandBrown)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (products.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No inventory available", color = ArtisanTan)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(products) { product ->
                        ProductCard(product, onAddToCart, onUpdateStock)
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddProductDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = {
                onAddProduct(it)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AddProductDialog(onDismiss: () -> Unit, onConfirm: (Product) -> Unit) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var colors by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Product", fontWeight = FontWeight.Black, color = BrandBrown) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price (₹)") })
                OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Initial Stock") })
                OutlinedTextField(value = colors, onValueChange = { colors = it }, label = { Text("Colors (comma separated)") })
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && price.toDoubleOrNull() != null) {
                        onConfirm(
                            Product(
                                name = name,
                                price = price.toDouble(),
                                stock = stock.toIntOrNull() ?: 0,
                                colors = colors.split(",").map { it.trim() }.filter { it.isNotBlank() }
                            )
                        )
                    }
                }
            ) {
                Text("ADD", fontWeight = FontWeight.Bold, color = BrandBrown)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("CANCEL", color = ArtisanTan) }
        }
    )
}

@Composable
fun ProductCard(
    product: Product,
    onAddToCart: (Product) -> Unit,
    onUpdateStock: (String, Int) -> Unit
) {
    val isLowStock = product.stock <= 10

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(ArtisanSand, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(product.icon, fontSize = 28.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(product.name, fontWeight = FontWeight.Black, color = BrandBrown)
                    Text("₹${product.price.toInt()}", color = BrandRed, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                if (product.stock == 0) {
                    Box(
                        modifier = Modifier
                            .background(BrandRed, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("OUT OF STOCK", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
                    }
                } else if (isLowStock) {
                    Box(
                        modifier = Modifier
                            .background(BrandRed.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("LOW STOCK", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${product.stock} units", color = ArtisanTan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { onUpdateStock(product.id, product.stock + 1) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Default.AddCircle, null, tint = ArtisanTan, modifier = Modifier.size(20.dp))
                    }
                }
                Button(
                    onClick = { onAddToCart(product) },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBrown),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    modifier = Modifier.height(36.dp),
                    enabled = product.stock > 0
                ) {
                    Text(if (product.stock > 0) "ADD TO CART" else "OUT OF STOCK", fontSize = 10.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}
