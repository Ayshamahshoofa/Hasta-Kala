package com.example.hastakala.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hastakala.Product
import com.example.hastakala.Sale
import com.example.hastakala.ui.theme.*

@Composable
fun DashboardScreen(
    sales: List<Sale>,
    products: List<Product>,
    onNavigate: (String) -> Unit
) {
    val totalIncome = sales.sumOf { it.price * it.quantity }
    val totalItemsSold = sales.sumOf { it.quantity }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Header (Simplified)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(BrandBrown)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hasta-Kala",
                    color = ArtisanSand,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "ARTISAN ANALYTICS",
                    color = ArtisanSand.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp
                )
            }
        }

        Column(modifier = Modifier.padding(24.dp)) {
            // Top Row Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val decimalFormat = java.text.DecimalFormat("#,###")
                val topProduct = if (sales.isNotEmpty()) {
                    sales.groupBy { it.productName }
                        .maxByOrNull { it.value.sumOf { s -> s.quantity } }?.key
                } else null

                val topProductIcon = products.find { it.name == topProduct }?.icon ?: "✨"

                DashboardStatCard(
                    title = "REVENUE",
                    value = "₹${decimalFormat.format(totalIncome.toLong())}",
                    icon = "₹",
                    modifier = Modifier.weight(1f)
                )

                DashboardStatCard(
                    title = "BESTSELLER",
                    value = topProduct?.take(10)?.plus(if (topProduct.length > 10) ".." else "") ?: "N/A",
                    icon = topProductIcon,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Items Sold Stat (Full Width)
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = ArtisanSand,
                        shape = CircleShape,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("🛍️", fontSize = 16.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("ITEMS SOLD", color = ArtisanTan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("$totalItemsSold Products", color = BrandBrown, fontSize = 16.sp, fontWeight = FontWeight.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stock Alerts
            val lowStockProducts = products.filter { it.stock < 10 }
            if (lowStockProducts.isNotEmpty()) {
                Surface(
                    color = BrandRed.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BrandRed.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = BrandRed,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "LOW STOCK ALERTS",
                                    color = BrandRed,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        lowStockProducts.forEach { product ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = product.name,
                                        color = BrandBrown,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${product.stock} items left",
                                        color = BrandRed,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Button(
                                    onClick = { onNavigate("products") },
                                    colors = ButtonDefaults.buttonColors(containerColor = BrandRed),
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text("RESTOCK", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            if (product != lowStockProducts.last()) {
                                HorizontalDivider(color = BrandRed.copy(alpha = 0.1f))
                            }
                        }
                    }
                }
            }

            // Product Distribution Card
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PRODUCT DISTRIBUTION",
                            color = BrandBrown,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                        Icon(
                            Icons.Default.Share,
                            contentDescription = null,
                            tint = ArtisanTan,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (sales.isEmpty()) {
                            Text("No sales data yet", color = ArtisanTan, fontSize = 12.sp)
                        } else {
                            val totalSalesQty = sales.sumOf { it.quantity }.toFloat()
                            val groupedSales = sales.groupBy { it.productName }
                            val distribution = groupedSales.map { it.value.sumOf { s -> s.quantity }.toFloat() / totalSalesQty }
                            val productNames = groupedSales.keys.toList()

                            val chartColors = listOf(
                                Color(0xFF6B705C),
                                Color(0xFFA5A58D),
                                Color(0xFFB7B7A4),
                                Color(0xFFD4A373),
                                Color(0xFFE9C46A),
                                Color(0xFFE48E59),
                                BrandRed
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                DonutChart(
                                    data = distribution,
                                    colors = chartColors
                                )

                                Spacer(modifier = Modifier.width(24.dp))

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    productNames.take(4).forEachIndexed { index, name ->
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(10.dp)
                                                    .background(chartColors.getOrElse(index) { Color.Gray }, CircleShape)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = name,
                                                color = BrandBrown,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recent Activity Section
            Text(
                text = "RECENT ACTIVITY",
                color = ArtisanTan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (sales.isEmpty()) {
                Surface(
                    color = ArtisanSand.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("No recent sales recorded", color = ArtisanTan, fontSize = 12.sp)
                    }
                }
            } else {
                sales.take(3).forEach { sale ->
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val product = products.find { it.name == sale.productName }
                            Surface(
                                color = ArtisanSand,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(product?.icon ?: "✨", fontSize = 20.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(sale.productName, color = BrandBrown, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("${sale.quantity} units • ${sale.color}", color = ArtisanTan, fontSize = 11.sp)
                            }
                            Text(
                                "+₹${(sale.price * sale.quantity).toInt()}",
                                color = Color(0xFF6B705C),
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardStatCard(title: String, value: String, icon: String, modifier: Modifier = Modifier) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = ArtisanSand,
                shape = CircleShape,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = icon, fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                color = ArtisanTan,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = BrandBrown,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
fun DonutChart(data: List<Float>, colors: List<Color>) {
    Canvas(modifier = Modifier.size(160.dp)) {
        var startAngle = -90f
        val strokeWidth = 32.dp.toPx()

        data.forEachIndexed { index, sweepPercent ->
            val sweepAngle = sweepPercent * 360f
            drawArc(
                color = colors.getOrElse(index) { Color.LightGray },
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
            )
            startAngle += sweepAngle
        }
    }
}
