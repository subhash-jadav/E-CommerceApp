package com.example.easyshop.screens.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun BannerView(modifier: Modifier = Modifier.fillMaxWidth()) {
    var bannerList by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("banners")
            .get()
            .addOnSuccessListener { snapshot ->
                val urls = snapshot.documents.mapNotNull { it.getString("url") }
                Log.d("BannerView", "Fetched banner URLs: $urls")
                bannerList = urls
            }
            .addOnFailureListener { e ->
                Log.e("BannerView", "Error fetching banners", e)
            }
    }



//    AsyncImage(model = if(bannerList.size>0) bannerList[0] else "", contentDescription = "banner")
    Column(modifier = Modifier.fillMaxWidth()) {
        val pagerState = rememberPagerState(0) { bannerList.size }
        HorizontalPager(state = pagerState  , modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = bannerList[it],
                contentDescription = "banner",
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .height(150.dp),
//                    .width(),
                contentScale = ContentScale.Crop,

            )


        }
        Spacer(modifier.height(12.dp))
        DotsIndicator(
            dotCount = bannerList.size,
            modifier = Modifier,
            type = ShiftIndicatorType(DotGraphic(color = Color.White, size = 6.dp) ),
            pagerState = pagerState
        )
    }

}