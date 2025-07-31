package com.example.easyshop.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.easyshop.GlobalNavigation
import com.example.easyshop.models.CategoryModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun CategoriesView(modifier: Modifier = Modifier) {
    var categoryList = remember {
        mutableStateOf<List<CategoryModel>>(emptyList())
    }
    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data").document("stock").collection("categories").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    var resultList = it.result.documents.mapNotNull { doc ->
                        doc.toObject(CategoryModel::class.java)
                    }
                    categoryList.value = resultList
                } else {
                    it.exception?.let {
                        it.printStackTrace()
                    }
                }
            }
    }
    LazyRow (
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ){
        items(categoryList.value) { item ->
            CategoryItem(item)
        }
    }
}

@Composable
fun CategoryItem(category: CategoryModel) {

   Card (
       modifier = Modifier.size(100.dp).clickable {
           GlobalNavigation.navController.navigate("category-products/"+category.id)

       },
       shape = RoundedCornerShape(12.dp),
       elevation = CardDefaults.cardElevation(4.dp),
       colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)

   ){  Column(
       horizontalAlignment = Alignment.CenterHorizontally,
       verticalArrangement = Arrangement.Center,
       modifier = Modifier.fillMaxSize()
   ) {
       AsyncImage(
           model = category.imageUrl, contentDescription = category.name,
           modifier = Modifier.size(70.dp).padding(top = 6.dp)
       )
       Spacer(modifier = Modifier.size(8.dp))
       Text(text = category.name)

   } }
}
