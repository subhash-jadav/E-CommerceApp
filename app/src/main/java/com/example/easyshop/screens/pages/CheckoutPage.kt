package com.example.easyshop.screens.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.easyshop.AppUtil
import com.example.easyshop.models.ProductModel
import com.example.easyshop.models.UserModel
import com.example.easyshop.viewModels.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun CheckoutPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val userModel = remember { mutableStateOf(UserModel()) }
    val productList = remember { mutableStateListOf<ProductModel>() }
    val subTotal = remember { mutableStateOf(0f) }
    val platformTax = remember { mutableStateOf(0f) }
    val total = remember { mutableStateOf(0f) }
    val discount = remember { mutableStateOf(0f) }

    fun calculateAndAssign() {
        productList.forEach {
            if (it.actualPrice.isNotEmpty()) {
                val qty = userModel.value.cartItems[it.id] ?: 0
                subTotal.value += it.actualPrice.toFloat() * qty
            }
        }
        discount.value = subTotal.value * (AppUtil.getDiscount()) / 100;
        platformTax.value = subTotal.value * (AppUtil.getPlatformTax()) / 100;
        total.value = "%.2f".format(subTotal.value + platformTax.value - discount.value).toFloat()
    }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    var result = it.result.toObject(UserModel::class.java)
                    if (result != null) {
                        userModel.value = result
                        Firebase.firestore.collection("data").document("stock")
                            .collection("products")
                            .whereIn("id", result.cartItems.keys.toList())
                            .get()
                            .addOnSuccessListener { task ->
                                val resultProducts = task.toObjects(ProductModel::class.java)
                                productList.clear()
                                productList.addAll(resultProducts)
                                calculateAndAssign()


                            }
                    }

                }
            }
    }

    Column(
        modifier = Modifier
            .padding(top = 38.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize()
    ) {

        Text(text = "CheckOut", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Deliver to :", fontWeight = FontWeight.SemiBold, fontSize = 22.sp)
        Text(text = userModel.value.userAddress, fontWeight = FontWeight.Thin)
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(18.dp))
        RowCheckOutItems(title = "Subtotal", value = "₹" + subTotal.value)
        Spacer(modifier = Modifier.height(18.dp))
        RowCheckOutItems(title = "Platform Tax(+)", value = "₹" + platformTax.value)
        Spacer(modifier = Modifier.height(18.dp))
        RowCheckOutItems(title = "Discount(-)", value = "₹" + discount.value)
        Spacer(modifier = Modifier.height(18.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "to pay",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = total.value.toString(),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
//        RowCheckOutItems(title = "Total", value = "$" + total.value)
        Spacer(modifier = Modifier.height(18.dp))
//        HorizontalDivider()
//        Spacer(modifier = Modifier.height(18.dp))
        Button(onClick = {AppUtil.startPayment(total.value)}, modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()) {
            Text(text = "Pay now")
        }


    }
}

@Composable
fun RowCheckOutItems(title: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
//        Spacer(modifier=Modifier.weight(1f))
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold)

    }

}