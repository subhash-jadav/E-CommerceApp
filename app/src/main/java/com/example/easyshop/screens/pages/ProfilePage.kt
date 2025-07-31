package com.example.easyshop.screens.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easyshop.AppUtil
import com.example.easyshop.GlobalNavigation
import com.example.easyshop.GlobalNavigation.navController
import com.example.easyshop.R
import com.example.easyshop.models.OrderModel
import com.example.easyshop.models.ProductModel
import com.example.easyshop.models.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

@Composable
fun ProfilePage(modifier: Modifier = Modifier) {
    var userModel by remember { mutableStateOf(UserModel()) }
    var userAddress by remember { mutableStateOf(userModel.userAddress) }
    var context = LocalContext.current
    LaunchedEffect(Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result.toObject(UserModel::class.java)
                    if (result != null) {
                        userModel = result
                        userAddress = result.userAddress
                    }
                }
            }

    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Your Profile", style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Image(
            painter = painterResource(R.drawable.profile), contentDescription = "profile_icon",
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = userModel.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Address", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = userAddress,
            onValueChange = { userAddress = it },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if (userAddress.isNotEmpty())
                    Firebase.firestore.collection("users")
                        .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                        .update("userAddress", userAddress)
                        .addOnCompleteListener {
                            AppUtil.showToast(context, "Address Updated")
                        }
                else
                    AppUtil.showToast(context, "Address cannot be empty")
            })
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Email", fontSize = 22.sp, fontWeight = FontWeight.Medium)
        Text(text = userModel.email, fontSize = 18.sp, fontWeight = FontWeight.Normal)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Number of Item in cart", fontSize = 22.sp, fontWeight = FontWeight.Medium)
        Text(text = userModel.cartItems.size.toString(), fontSize = 18.sp, fontWeight = FontWeight.Normal)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "My Orders", fontSize = 22.sp, fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth().clickable {
                GlobalNavigation.navController.navigate("orders")
            }.padding(vertical = 12.dp)
            )

        TextButton(onClick = {
            FirebaseAuth.getInstance().signOut()
            GlobalNavigation.navController.popBackStack()
            navController.navigate("auth")
        },
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
            ) {
            Text(text = "Sign Out", fontSize = 22.sp, fontWeight = FontWeight.Medium)
        }

    }
}