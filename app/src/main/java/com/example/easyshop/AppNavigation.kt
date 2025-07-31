package com.example.easyshop

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.easyshop.screens.AuthScreen
import com.example.easyshop.screens.HomeScreen
import com.example.easyshop.screens.LogInScreen
import com.example.easyshop.screens.SignUpScreen
import com.example.easyshop.screens.components.MyOrders
import com.example.easyshop.screens.pages.CategoryProdcustPages.CategoryProductsPage
import com.example.easyshop.screens.pages.CategoryProdcustPages.CategoryProductsPage
import com.example.easyshop.screens.pages.CheckoutPage
import com.example.easyshop.screens.pages.ProductDetailsPage
import com.example.easyshop.viewModels.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    GlobalNavigation.navController = navController
    var isLoggedin = Firebase.auth.currentUser != null
    var firstPage = if (isLoggedin) "home" else "auth"

    NavHost(navController = navController, startDestination = firstPage) {
        composable("auth") {
            AuthScreen(modifier, navController = navController)
        }
        composable("login") {
            LogInScreen(
                navController = navController, authViewModel = authViewModel
            )
        }
        composable("signup") {
            SignUpScreen(
                navController = navController, authViewModel = authViewModel
            )
        }
        composable("home") {
            HomeScreen(
                navController = navController, authViewModel = authViewModel
            )
        }
        composable("orders") {
            MyOrders()
        }
        composable("category-products/{categoryId}") {
            var categoryId = it.arguments?.getString("categoryId")
            CategoryProductsPage(
                modifier, categoryId ?: ""
            )
        }
        composable("product-details/{productId}") {

            var productId = it.arguments?.getString("productId")
            Log.d("ProductDetails", "productId: $productId")
            ProductDetailsPage(
                modifier, productId ?: ""
            )
        }
        composable("checkout") {
            CheckoutPage(
                navController = navController, authViewModel = authViewModel
            )
        }

    }
}

object GlobalNavigation {
    lateinit var navController: NavHostController
}