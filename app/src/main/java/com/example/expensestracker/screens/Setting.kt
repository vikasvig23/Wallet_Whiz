package com.example.expensestracker.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensestracker.R

import com.example.expensestracker.db_model.Category

import com.example.expensestracker.navigation.AppRouter
import com.example.expensestracker.navigation.Screen
import com.example.expensestracker.navigation.ui.theme.DividerColor
import com.example.expensestracker.navigation.ui.theme.Shapes
import com.example.expensestracker.screens.ui.NeoPopButton
import com.example.expensestracker.screens.ui.theme.DarculaBg
import com.example.expensestracker.screens.ui.theme.DarculaCard
import com.example.expensestracker.utils.PrefDataStore
import com.example.expensestracker.utils.Utility
import com.example.expensestracker.utils.Utility.backborder
import com.google.firebase.auth.FirebaseAuth
import io.realm.kotlin.ext.query
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

@Composable
fun Setting(navController: NavController) {

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    var deleteConfirmationShowing by remember {
        mutableStateOf(false)
    }

    val eraseAllData: () -> Unit = {
//        coroutineScope.launch {
//            db.write {
//                val expenses = this.query<Expense>().find()
//                val categories = this.query<Category>().find()
//
//                delete(expenses)
//                delete(categories)
//
//                deleteConfirmationShowing = false
//            }
//        }
    }
    val items = listOf(
        "Profile" to R.drawable.profile,
        "Categories"  to R.drawable.category,
        "Currency" to R.drawable.category,
        "Erase Data" to R.drawable.category,
        "Logout" to R.drawable.logot,

    )

    Scaffold(
        containerColor = DarculaBg
    ) { innerPadding ->


    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(18.dp)
            .fillMaxSize()
           ,

        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(items.size) { index ->
            val (title, icon) = items[index]

            NeoPopButton(
                text = title,
                iconRes = icon,
                textSize = 18.sp,
                fontFamily = Utility.Poppins,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                when (title) {
                    "Profile" -> {}
                    "Categories" -> navController.navigate("setting/categories")
                    "Currency" -> navController.navigate("setting/currency")
                    "Erase Data" -> { deleteConfirmationShowing = true}
                    "Logout" ->{coroutineScope.launch { PrefDataStore.clearEmail(context) }
                               coroutineScope.launch { PrefDataStore.clearAll(context) }
                            FirebaseAuth.getInstance().signOut()
                            AppRouter.navigateTo(Screen.LoginScreen)}
                }
            }
        }
        }
    }

    if (deleteConfirmationShowing) {
        AlertDialog(
            onDismissRequest = { deleteConfirmationShowing = false },
            title = { Text("Are you Sure?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = eraseAllData) {
                    Text("Delete everything")
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteConfirmationShowing = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}


// Handle navigation + actions
private fun onSettingClick(label: String, navController: NavController) {
    when (label) {
        //"Profile" -> navController.navigate("setting/profile")
        "Categories" -> navController.navigate("setting/categories")
        "Currency" -> navController.navigate("setting/currency")
        "Erase Data" -> { /* Show popup */ }
        "Logout" -> {
            AppRouter.navigateTo(Screen.LoginScreen)}
    }
}


@Preview @Composable fun SettingPreview(){ Setting(navController = rememberNavController()) }
