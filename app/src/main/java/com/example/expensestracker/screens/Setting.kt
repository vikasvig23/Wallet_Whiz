package com.example.expensestracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensestracker.db
import com.example.expensestracker.db_model.Category
import com.example.expensestracker.db_model.Expense
import com.example.expensestracker.navigation.AppRouter
import com.example.expensestracker.navigation.Screen
import com.example.expensestracker.ui.theme.DividerColor
import com.example.expensestracker.ui.theme.Shapes
import com.google.firebase.auth.FirebaseAuth
import io.realm.kotlin.ext.query
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable 
fun Setting(navController:NavController) {
  //  val navController= rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    var deleteConfirmationShowing by remember {
        mutableStateOf(false)
    }

    val eraseAllData: () -> Unit = {
        coroutineScope.launch {
            db.write {
                val expenses = this.query<Expense>().find()
                val categories = this.query<Category>().find()

                delete(expenses)
                delete(categories)

                deleteConfirmationShowing = false
            }
        }
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.LightGray,
                    titleContentColor = Color.White,
                ), title = {
                    Text("Setting")
                })
        }
    ) {
        innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(Shapes.large)
                        .background(color = Color.LightGray)
                        .fillMaxWidth()
                ) {
                    TableRow(
                        label = "Categories",
                        hasArrow = true,
                                modifier = Modifier.clickable {
                            navController.navigate("setting/categories")
                        })
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(horizontal = 16.dp), thickness = 1.dp, color = DividerColor
                    )
                    TableRow(
                        label = "Select Currency",
                        hasArrow = true,
                        modifier = Modifier.clickable {
                            navController.navigate("setting/currency")
                        })
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(start = 16.dp), thickness = 1.dp, color = DividerColor
                    )
                    TableRow(
                        label = "Erase all data",
                        isDestructive = true,
                        modifier = Modifier.clickable {
                            deleteConfirmationShowing = true
                        })

                    if(deleteConfirmationShowing){
                        AlertDialog(
                            onDismissRequest = { deleteConfirmationShowing = false },
                            title={ Text(text = "Are you Sure?")},
                            text={ Text(text = "This action cannot be undone.")},
                            confirmButton = {
                                TextButton(onClick = eraseAllData) {
                                    Text("Delete everything")

                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { deleteConfirmationShowing=false}) {
                                    
                                    Text(text = "Cancel")
                                }
                            })
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(start = 16.dp), thickness = 1.dp, color = DividerColor
                    )
                    TableRow(
                        label = "LogOut",
                        //isDestructive = true,
                        modifier = Modifier.clickable {
                            FirebaseAuth.getInstance().signOut()
                            AppRouter.navigateTo(Screen.LoginScreen)
                        })



                }
            }

    }
}




@Preview
@Composable
fun SettingPreview(){
    Setting(navController = rememberNavController())
}
