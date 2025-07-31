package com.example.easyshop

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.easyshop.ui.theme.EasyShopTheme
import com.example.easyshop.viewModels.AuthViewModel
import com.razorpay.PaymentResultListener

class MainActivity : ComponentActivity(), PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var authViewModel = AuthViewModel()
        setContent {
            EasyShopTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(Modifier.padding(innerPadding), authViewModel)
                }
            }
        }
    }

    override fun onPaymentSuccess(p0: String?) {

        AppUtil.clearCartAndAddtoOrders()
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Payment Successful")
        builder.setMessage("Your order has been placed successfully")
        builder.setPositiveButton("OK") { dialog, which ->
            val navController = GlobalNavigation.navController
            navController.popBackStack()
            navController.navigate("home")
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        AppUtil.showToast(this, "Payment Failed")
    }
}

