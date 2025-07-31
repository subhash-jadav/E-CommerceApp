package com.example.easyshop.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.easyshop.screens.pages.CartPage
import com.example.easyshop.screens.pages.FavPage
import com.example.easyshop.screens.pages.HomePage
import com.example.easyshop.screens.pages.ProfilePage
import com.example.easyshop.viewModels.AuthViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, navController: NavHostController,
    authViewModel: AuthViewModel
) {
    var itemList = listOf(
        navItems("Home", Icons.Default.Home),
        navItems("Favourite", Icons.Default.Favorite),
        navItems("Cart", Icons.Default.ShoppingCart),
        navItems("Profile", Icons.Default.Person)
    )
    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    Scaffold(
        bottomBar = {
            NavigationBar {
                itemList.forEachIndexed { index, navItems ->
                    NavigationBarItem(
                        selected = index == selectedIndex,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = { Icon(navItems.icon, contentDescription = navItems.label) },
                        label = { Text(navItems.label) }
                    )

                }
            }
        }
    ) {
        ContentScreen(modifier = Modifier.padding(it),selectedIndex)
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier,selectedIndex : Int) {
when(selectedIndex){
    0 -> HomePage(modifier)
    1 -> FavPage(modifier)
    2 -> CartPage(modifier)
    3 -> ProfilePage(modifier)
}
}

data class navItems(
    var label: String,
    var icon: ImageVector
)