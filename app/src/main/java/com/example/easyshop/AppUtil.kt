package com.example.easyshop

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.example.easyshop.models.OrderModel
import com.example.easyshop.models.UserModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.razorpay.Checkout
import org.json.JSONObject
import java.util.UUID

object AppUtil {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun addToCart(productId: String, context: Context) {
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
//                val user = it.result.toObject(UserModel::class.java)
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[productId] ?: 0
                val updatedQuantity = currentQuantity + 1

                val updateCart = mapOf("cartItems.$productId" to updatedQuantity)
                userDoc.update(updateCart)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            showToast(context, "Added to cart")
                        } else {
                            showToast(context, "Failed to add to cart")
                        }
                    }


            }
        }
    }

    fun removeToCart(productId: String, context: Context, removeAll: Boolean = false) {
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
//                val user = it.result.toObject(UserModel::class.java)
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[productId] ?: 0
                val updatedQuantity = currentQuantity - 1

                val updateCart =
                    if (updatedQuantity <= 0 || removeAll)
                        mapOf("cartItems.$productId" to FieldValue.delete())
                    else
                        mapOf("cartItems.$productId" to updatedQuantity)

                userDoc.update(updateCart)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            showToast(context, "Removed from cart")
                        } else {
                            showToast(context, "Failed to remove from cart")
                        }
                    }
            }
        }
    }

    fun getPlatformTax(): Float {
        return 5.0f
    }

    fun getDiscount(): Float {
        return 10.0f
    }

    fun razorpayApiKey(): String {
        return "rzp_test_dEOrCLbFVlTCoT"
    }

    fun startPayment(amount: Float) {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_dEOrCLbFVlTCoT")
        val options = JSONObject()
        options.put("name", "EasyShop")
        options.put("description", "Reference No. #123456")
        options.put("amount", (amount * 100))
        options.put("currency", "INR")

        checkout.open(GlobalNavigation.navController.context as Activity, options)

    }

    fun clearCartAndAddtoOrders() {
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val order = OrderModel(
                    id = "ORD_"+UUID.randomUUID().toString().replace("-", "").take(10).uppercase(),
                    userId = FirebaseAuth.getInstance().currentUser?.uid!!,
                    items = currentCart,
                    address = it.result.get("userAddress") as String,
                    status = "Ordered",
                    date = Timestamp.now()
                )
                Firebase.firestore.collection("orders").document(order.id).set(order)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            userDoc.update("cartItems", FieldValue.delete())

                        }
                    }
            }
        }
    }
}