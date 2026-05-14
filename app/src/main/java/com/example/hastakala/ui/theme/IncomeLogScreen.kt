package com.example.hastakala.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hastakala.Sale
import com.example.hastakala.ui.theme.*

@Composable
fun IncomeLogScreen(sales: List<Sale>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Income Log",
            color = BrandBrown,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (sales.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("No sales logged yet today.", color = ArtisanTan, fontSize = 14.sp)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(items = sales.reversed()) { sale ->
                    IncomeItem(sale)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Summary Footer
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total Income", color = ArtisanTan, fontWeight = FontWeight.Bold)
                Text("₹${sales.sumOf { it.price * it.quantity }.toInt()}", color = BrandBrown, fontSize = 20.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
fun IncomeItem(sale: Sale) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(ArtisanTan.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🧾", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(sale.productName, color = BrandBrown, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                    Text("${sale.quantity} units • ${sale.color} • ${sale.date}", color = ArtisanTan, fontSize = 11.sp)
                }
            }
            Text(
                "₹${(sale.price * sale.quantity).toInt()}",
                color = Color(0xFFCC5555),
                fontWeight = FontWeight.Black,
                fontSize = 16.sp
            )
        }
    }
}
