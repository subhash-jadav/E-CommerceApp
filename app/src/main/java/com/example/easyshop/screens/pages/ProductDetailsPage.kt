package com.example.easyshop.screens.pages

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.easyshop.AppUtil
import com.example.easyshop.models.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun ProductDetailsPage(modifier: Modifier = Modifier, productId: String) {
    var product by remember {
        mutableStateOf(ProductModel())
    }
    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data").document("stock").collection("products")
            .document(productId).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    var result = it.result.toObject(ProductModel::class.java)
                    if (result != null) {
                        product = result
                    }
                }

            }

    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = product.title, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Spacer(modifier.height(2.dp))
        Column() {
            val pagerState = rememberPagerState(0) { product.images.size }
            HorizontalPager(
                state = pagerState, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp)

            ) {
//                test 1
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .clip(RoundedCornerShape(16.dp)) // Apply rounded corners to the container
                        .background(Color.LightGray)     // Optional: helps visualize corners
                ) {
                    AsyncImage(
                        model = product.images[it],
                        contentDescription = "product_images",
                        contentScale = ContentScale.Fit,     // Keeps image's aspect ratio, no crop
                        modifier = Modifier
                            .fillMaxSize() // Fill the box to respect clipping
                    )
                }

//                test 1 end
//                AsyncImage(
//                    model = product.images[it],
//                    contentDescription = "product_images",
//                    modifier = Modifier
////                        .padding(horizontal = 6.dp)
//                        .clip(RoundedCornerShape(16.dp))
//                        .fillMaxWidth()
//                        .height(230.dp),
//
//                    contentScale = ContentScale.Inside,
//
//                )


            }
            Spacer(modifier.height(6.dp))
            DotsIndicator(
                dotCount = product.images.size,
                modifier = Modifier,
                type = ShiftIndicatorType(DotGraphic(color = Color.White, size = 6.dp)),
                pagerState = pagerState
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$" + product.price,
                style = TextStyle(textDecoration = TextDecoration.LineThrough, fontSize = 16.sp)
            )
            Spacer(modifier.width(8.dp))
            Text(
                text = "$" + product.actualPrice,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "fav"
            )

        }
        var context = LocalContext.current
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {AppUtil.addToCart(productId,context)}, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
        {
            Text(text = "Add to Cart", fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Product Description", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = product.description, fontWeight = FontWeight.Normal, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        if (product.otherDetails.isNotEmpty()) {
            Text(text = "Other Details", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)

            product.otherDetails.forEach { (key, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(text = key + " : ", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Normal)
                }
            }
        }

    }

}