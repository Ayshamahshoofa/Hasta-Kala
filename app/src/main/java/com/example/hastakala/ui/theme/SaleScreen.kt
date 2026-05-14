package com.example.hastakala.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hastakala.Product
import com.example.hastakala.Sale
import com.example.hastakala.CartItem
import com.example.hastakala.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward

@Composable
fun SaleScreen(
    products: List<Product>,
    initialProduct: Product?,
    cart: List<CartItem>,
    onAddToCart: (CartItem) -> Unit,
    onUpdateCartItem: (Int, CartItem) -> Unit,
    onRemoveFromCart: (Int) -> Unit,
    onComplete: (List<Sale>) -> Unit,
    onClose: () -> Unit
) {
    var selectedProduct by remember { mutableStateOf(initialProduct) }
    var selectedColor by remember { mutableStateOf<String?>(null) }
    var quantity by remember { mutableIntStateOf(1) }

    // Reset selection when initialProduct changes
    LaunchedEffect(initialProduct) {
        selectedProduct = initialProduct
        selectedColor = null
        quantity = 1
    }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Quick Bill", fontWeight = FontWeight.Black, fontSize = 26.sp, color = BrandBrown)
            IconButton(onClick = onClose) { Icon(Icons.Default.Close, null, tint = BrandBrown) }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.weight(1f)) {
            Column {
                if (selectedProduct == null) {
                    // Product Selection
                    Text("SELECT PRODUCT", color = ArtisanTan, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(products) { p ->
                            val isOutOfStock = p.stock <= 0
                            ListItem(
                                headlineContent = {
                                    Text(
                                        p.name,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isOutOfStock) ArtisanTan else BrandBrown
                                    )
                                },
                                supportingContent = {
                                    Text(
                                        if (isOutOfStock) "OUT OF STOCK" else "₹${p.price.toInt()} - ${p.stock} units",
                                        color = if (isOutOfStock) BrandRed else ArtisanTan
                                    )
                                },
                                leadingContent = {
                                    Surface(
                                        color = ArtisanSand,
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(text = p.icon, fontSize = 24.sp, modifier = Modifier.alpha(if (isOutOfStock) 0.5f else 1f))
                                        }
                                    }
                                },
                                modifier = Modifier.clickable(enabled = !isOutOfStock) {
                                    selectedProduct = p
                                    selectedColor = null
                                    quantity = 1
                                }
                            )
                        }
                    }
                } else {
                    // Variant & Quantity Selection
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { selectedProduct = null }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = BrandBrown)
                        }
                        Text(selectedProduct!!.name, fontWeight = FontWeight.Black, color = BrandBrown, fontSize = 18.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("CHOOSE COLOR", color = ArtisanTan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        selectedProduct!!.colors.forEach { color ->
                            FilterChip(
                                selected = selectedColor == color,
                                onClick = { selectedColor = color },
                                label = { Text(color) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandBrown,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("QUANTITY", color = ArtisanTan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            enabled = quantity > 1
                        ) {
                            Text("-", fontWeight = FontWeight.Black, fontSize = 24.sp, color = BrandBrown)
                        }

                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.width(60.dp).height(40.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ArtisanSand)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(quantity.toString(), fontWeight = FontWeight.Bold, color = BrandBrown)
                            }
                        }

                        IconButton(
                            onClick = { if (quantity < selectedProduct!!.stock) quantity++ },
                            enabled = quantity < selectedProduct!!.stock
                        ) {
                            Icon(Icons.Default.Add, null, tint = BrandBrown)
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Max: ${selectedProduct!!.stock}", color = ArtisanTan, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            if (selectedColor != null) {
                                onAddToCart(CartItem(selectedProduct!!, quantity, selectedColor!!))
                                selectedProduct = null
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        enabled = selectedColor != null,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBrown),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("ADD TO BILL", fontWeight = FontWeight.Black)
                    }
                }

                if (cart.isNotEmpty() && selectedProduct == null) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("CURRENT BILL", color = ArtisanTan, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        itemsIndexed(cart) { index, item ->
                            CartItemRow(
                                item = item,
                                onUpdateQuantity = { newQty ->
                                    if (newQty > 0 && newQty <= item.product.stock) {
                                        onUpdateCartItem(index, item.copy(quantity = newQty))
                                    }
                                },
                                onRemove = { onRemoveFromCart(index) }
                            )
                        }
                    }
                }
            }
        }

        // Checkout Button
        if (cart.isNotEmpty() && selectedProduct == null) {
            val total = cart.sumOf { it.product.price * it.quantity }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = BrandBrown,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("TOTAL AMOUNT", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("₹${total.toInt()}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
                    }

                    Button(
                        onClick = {
                            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val completedSales = cart.map { item ->
                                Sale(
                                    productName = item.product.name,
                                    productId = item.product.id,
                                    color = item.selectedColor,
                                    price = item.product.price,
                                    quantity = item.quantity,
                                    date = date
                                )
                            }
                            onComplete(completedSales)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("CHECKOUT", color = BrandBrown, fontWeight = FontWeight.Black)
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = BrandBrown, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onUpdateQuantity: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(ArtisanSand, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(item.product.icon, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.product.name, fontWeight = FontWeight.Bold, color = BrandBrown, fontSize = 14.sp)
                Text("${item.selectedColor} • ₹${item.product.price.toInt()}/ea", color = ArtisanTan, fontSize = 11.sp)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onUpdateQuantity(item.quantity - 1) }, modifier = Modifier.size(24.dp)) {
                    Text("-", fontWeight = FontWeight.Black, fontSize = 18.sp, color = ArtisanTan)
                }
                Text(item.quantity.toString(), fontWeight = FontWeight.Bold, color = BrandBrown, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 4.dp))
                IconButton(onClick = { onUpdateQuantity(item.quantity + 1) }, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Add, null, tint = ArtisanTan, modifier = Modifier.size(16.dp))
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, null, tint = BrandRed, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}
