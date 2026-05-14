package com.example.hastakala

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hastakala.ui.theme.HastaKalaTheme
import com.example.hastakala.ui.theme.ArtisanSand
import com.example.hastakala.ui.theme.BrandBrown
import com.example.hastakala.ui.theme.ArtisanTan
import com.example.hastakala.ui.theme.BrandRed
import com.example.hastakala.ui.theme.SplashScreen
import com.example.hastakala.ui.theme.AuthScreen
import com.example.hastakala.ui.theme.DashboardScreen
import com.example.hastakala.ui.theme.InventoryScreen
import com.example.hastakala.ui.theme.SaleScreen
import com.example.hastakala.ui.theme.AnalyticsScreen
import com.example.hastakala.ui.theme.IncomeLogScreen
import com.example.hastakala.viewmodel.MainViewModel
import com.example.hastakala.Product
import com.example.hastakala.Sale
import com.example.hastakala.CartItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HastaKalaTheme {
                HastaKalaApp()
            }
        }
    }
}

@Composable
fun HastaKalaApp(viewModel: MainViewModel = viewModel()) {
    // Observed State from Database
    val products by viewModel.products.collectAsState()
    val sales by viewModel.sales.collectAsState()

    var cart by remember { mutableStateOf(emptyList<CartItem>()) }
    var currentScreen by remember { mutableStateOf("splash") }
    var preselectedProduct by remember { mutableStateOf<Product?>(null) }

    // Seed initial data if database is empty
    LaunchedEffect(products, currentScreen) {
        if (products.isEmpty() && currentScreen == "login") {
            val initialProducts = listOf(
                Product(name = "Terracotta Diya Set", price = 350.0, stock = 45, colors = listOf("Natural", "Painted")),
                Product(name = "Blue Pottery Vase", price = 1800.0, stock = 12, colors = listOf("Azure", "Cobalt")),
                Product(name = "Wooden Block Print", price = 650.0, stock = 25, colors = listOf("Teak", "Walnut")),
                Product(name = "Bamboo Basket Pair", price = 450.0, stock = 30, colors = listOf("Natural")),
                Product(name = "Pashmina Shawl", price = 4500.0, stock = 5, colors = listOf("Cream", "Beige", "Lavender")),
                Product(name = "Channapatna Toys", price = 750.0, stock = 18, colors = listOf("Multi-color")),
                Product(name = "Brass Laxmi Idol", price = 2200.0, stock = 8, colors = listOf("Antique Brass", "Polished")),
                Product(name = "Kalamkari Stole", price = 1200.0, stock = 15, colors = listOf("Maroon", "Indigo")),
                Product(name = "Dokra Showpiece", price = 3200.0, stock = 4, colors = listOf("Bronze")),
                Product(name = "Madhubani Art Box", price = 950.0, stock = 22, colors = listOf("Hand-painted"))
            )
            initialProducts.forEach { viewModel.addProduct(it) }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ArtisanSand
    ) {
        when (currentScreen) {
            "splash" -> SplashScreen { currentScreen = "login" }

            "login" -> AuthScreen(
                isLogin = true,
                onSuccess = { currentScreen = "dashboard" },
                onToggle = { currentScreen = "register" }
            )

            "register" -> AuthScreen(
                isLogin = false,
                onSuccess = { currentScreen = "login" },
                onToggle = { currentScreen = "login" }
            )

            else -> {
                Scaffold(
                    bottomBar = {
                        AppBottomBar(
                            currentScreen = currentScreen,
                            onNavigate = { screen -> currentScreen = screen }
                        )
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues).background(ArtisanSand).fillMaxSize()) {
                        when (currentScreen) {
                            "dashboard" -> DashboardScreen(
                                sales = sales,
                                products = products,
                                onNavigate = { screen -> currentScreen = screen }
                            )

                            "products" -> InventoryScreen(
                                products = products,
                                onAddToCart = { product ->
                                    preselectedProduct = product
                                    currentScreen = "sale"
                                },
                                onAddProduct = { newProduct ->
                                    viewModel.addProduct(newProduct)
                                },
                                onUpdateStock = { productId, newStock ->
                                    viewModel.updateStock(productId, newStock)
                                }
                            )

                            "sale" -> SaleScreen(
                                products = products,
                                initialProduct = preselectedProduct,
                                cart = cart,
                                onAddToCart = { newItem: CartItem ->
                                    cart = cart.toMutableList().apply { add(newItem) }
                                    preselectedProduct = null
                                },
                                onUpdateCartItem = { index: Int, updatedItem: CartItem ->
                                    cart = cart.toMutableList().apply {
                                        this[index] = updatedItem
                                    }
                                },
                                onRemoveFromCart = { index: Int ->
                                    cart = cart.filterIndexed { i, _ -> i != index }
                                },
                                onComplete = { completedSales: List<Sale> ->
                                    viewModel.completeSale(completedSales)
                                    cart = emptyList()
                                    preselectedProduct = null
                                    currentScreen = "dashboard"
                                },
                                onClose = {
                                    preselectedProduct = null
                                    currentScreen = "dashboard"
                                }
                            )

                            "stats" -> AnalyticsScreen(sales)

                            "income" -> IncomeLogScreen(sales)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppBottomBar(currentScreen: String, onNavigate: (String) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(80.dp)
    ) {
        val items = listOf(
            Triple("dashboard", Icons.Default.Home, "HOME"),
            Triple("income", Icons.Default.DateRange, "INCOME"),
            Triple("sale", Icons.Default.Add, ""),
            Triple("stats", Icons.Default.Star, "STATS"),
            Triple("products", Icons.AutoMirrored.Filled.List, "PRODUCT")
        )

        items.forEach { (route, icon, label) ->
            val isSelected = currentScreen == route

            if (route == "sale") {
                // Centered Add Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        onClick = { onNavigate(route) },
                        color = BrandBrown,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.size(56.dp),
                        shadowElevation = 4.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(icon, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(28.dp))
                        }
                    }
                }
            } else {
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onNavigate(route) },
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = label,
                            fontSize = 9.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandBrown,
                        unselectedIconColor = ArtisanTan,
                        selectedTextColor = BrandBrown,
                        unselectedTextColor = ArtisanTan,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}
