package com.example.easyshop.screens.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easyshop.screens.components.BannerView
import com.example.easyshop.screens.components.CategoriesView
import com.example.easyshop.screens.components.HeaderView

@Composable
fun HomePage(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 42.dp, start = 16.dp, end = 16.dp)
    ) {
        HeaderView()
        Spacer(modifier = Modifier.height(12.dp))
        BannerView()
        Spacer(modifier = Modifier.height(12.dp))
        Text("Categories" , style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        ))
        Spacer(modifier = Modifier.height(12.dp))
        CategoriesView()


    }
}