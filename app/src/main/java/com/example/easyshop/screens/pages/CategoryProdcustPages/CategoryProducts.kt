package com.example.easyshop.screens.pages.CategoryProdcustPages

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.easyshop.models.CategoryModel
import com.example.easyshop.models.ProductModel
import com.example.easyshop.screens.components.ProductItemView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import androidx.compose.material3.Text as Text

@Composable
fun CategoryProductsPage(
    modifier: Modifier = Modifier,
    categoryID: String

) {
    var productsList = remember {
        mutableStateOf<List<ProductModel>>(emptyList())
    }
    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data").document("stock").collection("products")
            .whereEqualTo("category", categoryID).get()

            .addOnCompleteListener {
                if (it.isSuccessful) {
                    var resultList = it.result.documents.mapNotNull { doc ->
                        doc.toObject(ProductModel::class.java)
                    }
                    productsList.value = resultList
                } else {
                    it.exception?.let {
                        it.printStackTrace()
                    }
                }
            }
    }
    LazyColumn() {
        items(productsList.value.chunked(2)) { rowItems ->
            Row {
                rowItems.forEach {
                    ProductItemView(it, modifier = Modifier.weight(1f))
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))

                }
            }
        }
    }

}