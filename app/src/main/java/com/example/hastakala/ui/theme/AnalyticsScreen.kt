package com.example.hastakala.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hastakala.Sale
import com.example.hastakala.ui.theme.*

@Composable
fun AnalyticsScreen(sales: List<Sale>) {
    val totalRevenue = sales.sumOf { it.price * it.quantity }
    val averageSale = if (sales.isNotEmpty()) totalRevenue / sales.size else 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Stats & Insights", fontWeight = FontWeight.Black, fontSize = 28.sp, color = BrandBrown)
        Spacer(modifier = Modifier.height(24.dp))

        // Sales Trend Chart
        Text("Weekly Sales Trend", fontWeight = FontWeight.Bold, color = BrandBrown, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(16.dp))
        SalesChart(sales)

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AnalyticsStatCard(
                title = "Total Orders",
                value = "${sales.size}",
                modifier = Modifier.weight(1f)
            )
            AnalyticsStatCard(
                title = "Revenue",
                value = "₹${totalRevenue.toInt()}",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnalyticsStatCard(
            title = "Average Order Value",
            value = "₹${averageSale.toInt()}",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text("Top Performing Products", fontWeight = FontWeight.Bold, color = BrandBrown, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(12.dp))

        if (sales.isEmpty()) {
            Text("No sales data available.", color = ArtisanTan, fontSize = 14.sp)
        } else {
            val topProducts = sales.groupBy { it.productName }
                .mapValues { it.value.sumOf { s -> s.quantity } }
                .toList()
                .sortedByDescending { it.second }
                .take(3)

            topProducts.forEach { (name, count) ->
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(name, color = BrandBrown, fontWeight = FontWeight.Medium)
                        Text("$count", fontWeight = FontWeight.Black, color = Color(0xFFCC5555))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun SalesChart(sales: List<Sale>) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(180.dp),
        color = Color.White,
        shape = RoundedCornerShape(24.dp)
    ) {
        val trendData = if (sales.isEmpty()) {
            listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f)
        } else {
            // Group by date and take last 7 distinct dates or pad if necessary
            val grouped = sales.groupBy { it.date }
                .mapValues { it.value.sumOf { s -> s.price * s.quantity }.toFloat() }
                .toList()
                .sortedBy { it.first }
                .takeLast(7)

            val values = grouped.map { it.second }
            if (values.size < 7) {
                List(7 - values.size) { 0f } + values
            } else {
                values
            }
        }

        Canvas(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            val width = size.width
            val height = size.height
            val barWidth = 30.dp.toPx()
            val spaceBetween = (width - (barWidth * trendData.size)) / (trendData.size - 1)
            val maxVal = trendData.maxOrNull()?.takeIf { it > 0 } ?: 1000f

            trendData.forEachIndexed { index, value ->
                val barHeight = (value / maxVal) * height
                drawRect(
                    color = if (index == trendData.size - 1) Color(0xFFE48E59) else BrandBrown.copy(alpha = 0.2f),
                    topLeft = Offset(index * (barWidth + spaceBetween), height - barHeight),
                    size = Size(barWidth, barHeight)
                )
            }
        }
    }
}

@Composable
fun AnalyticsStatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(title, color = ArtisanTan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Black, color = BrandBrown)
        }
    }
}
