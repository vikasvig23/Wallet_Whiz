package com.example.expensestracker.screens

import android.annotation.SuppressLint
import android.provider.ContactsContract.Profile
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensestracker.navigation.ui.theme.TopAppBarBackground
import com.example.expensestracker.screens.ui.theme.DarculaBg
import com.example.expensestracker.screens.ui.theme.DarculaCard
import com.example.expensestracker.screens.ui.theme.DarculaMuted
import com.example.expensestracker.screens.ui.theme.DarculaText
import com.example.expensestracker.utils.Poppins
import com.example.expensestracker.utils.Utility.backborder

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Profile(  navController: NavController){


    Scaffold(
        containerColor = DarculaBg,
        topBar = {

            MediumTopAppBar(
                colors = topAppBarColors(
                    containerColor = DarculaCard,
                    titleContentColor = DarculaText,
                ),

                navigationIcon = {
                    Surface(
                        onClick = navController::popBackStack,
                        color = DarculaCard

                    ) {
                        Row(modifier = Modifier.padding(vertical = 10.dp)) {
                            Icon(
                                Icons.AutoMirrored.Rounded.KeyboardArrowLeft, contentDescription = "Settings",
                            )
                            Text("Back", style = TextStyle(fontSize = 15.sp, fontFamily = Poppins, color = DarculaMuted ))
                        }
                    }

                },


                title={
                    Text("Profile")
                }
            )
        },

    ) {

    }
}

@Preview
@Composable
fun PreviewProf(){
    Profile(navController= rememberNavController(

    ))
}