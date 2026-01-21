package com.example.expensestracker.screens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults.containerColor
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface

import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensestracker.Components.BottomNavGraph
import com.example.expensestracker.R
import com.example.expensestracker.navigation.BottomBarScreen
import com.example.expensestracker.navigation.Screen
import com.example.expensestracker.navigation.ui.theme.TopAppBarBackground
import com.example.expensestracker.navigation.ui.theme.blue
import com.example.expensestracker.screens.ui.theme.DarculaBg
import com.example.expensestracker.utils.Utility
import com.example.expensestracker.utils.Utility.backborder
import java.util.Locale

//@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@Composable

fun HomeScreen() {
    val context = LocalContext.current
    val navController = rememberNavController()

    Scaffold(
      //  topBar = { TopBar() },
        containerColor = DarculaBg,
        bottomBar = { BottomBar(navController = navController) }
    ) { innerPadding ->
        BottomNavGraph(context, navController = navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(){
   Scaffold(
       topBar =
       {

               CenterAlignedTopAppBar(

                   colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                       containerColor = Color.White,

                   ),
                   title = {
                       Image(
                           painter = painterResource(id = R.drawable.wallethome), // your drawable here
                           contentDescription = "App Logo",

                       )
                   },
//                   navigationIcon = {
//                       IconButton(onClick = { /* do something */ }) {
//                           Icon(
//                               imageVector = Icons.AutoMirrored.Filled.List,
//                               contentDescription = "Localized description",
//                               modifier = Modifier.size(50.dp
//                               )
//                           )
//                       }
//                   },
               )

       }
   ){

   }
}
@Composable
fun ReliveTopNavigation() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Home / Summary
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Summary",
                tint = Color.Black
            )
            Text(
                text = "Summary",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Black
            )
        }

        // Stats
        Icon(
            painter = painterResource(id = R.drawable.upload), // replace with your drawable
            contentDescription = "Stats",
            tint = Color.Black
        )

        // Challenges / Trophy
        Icon(
            painter = painterResource(id = R.drawable.bar_chart), // replace with your drawable
            contentDescription = "Challenges",
            tint = Color.Black
        )

        // Profile / People
        Icon(
            painter = painterResource(id = R.drawable.add), // replace with your drawable
            contentDescription = "Profile",
            tint = Color.Black
        )
    }
}



@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Expense,
        BottomBarScreen.Reports,
        BottomBarScreen.Add,
        BottomBarScreen.Setting
    )

    var showBottomBar by rememberSaveable { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    showBottomBar = when (navBackStackEntry?.destination?.route) {
        "setting/categories" -> false
        else -> true
    }

    if (showBottomBar) {
        NavigationBar(
            modifier = Modifier
                .padding(start = 18.dp, end = 18.dp,  bottom = 10.dp) // space around bar
                .height(65.dp)
                .clip(RoundedCornerShape(50.dp)) // round corners
                .backborder(shape = RoundedCornerShape(50.dp)), // background after clip
            containerColor = Color.Transparent, // avoid overriding background
            contentColor = Color.White,
            tonalElevation = 4.dp // small shadow/elevation
        ) {
            Row(
                modifier = Modifier
                   // .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                screens.forEach { screen ->
                    AddItem(
                        screen = screen,
                        currentDestination = currentDestination,
                        navController = navController
                    )
                }
            }
        }

    }
}


@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    val contentColor =
        if (selected)   Color(0xFFE0D7FF)  else Color(0xFF9E9E9E)
    val background =
        if (selected) Color(0xFF6E5FD9).copy(alpha = 0.18f) else Color.Transparent


    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(CircleShape)
            .background(background)
            .clickable(onClick = {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            })
    ) {

        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(id = if (selected) screen.icon_focused else screen.icon),
                contentDescription = "icon",
                tint = contentColor
            )
            AnimatedVisibility(visible = selected) {
                Text(
                    text = screen.title,
                    fontFamily = Utility.Poppins,
                    color = contentColor
                )
            }

        }
    }



 /*
    var showBottomBar by rememberSaveable { mutableStateOf(true) }
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    showBottomBar = when (backStackEntry?.destination?.route) {
        "setting/categories" -> false
        else -> true
    }


  Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = TopAppBarBackground) {
                    NavigationBarItem(
                        selected = backStackEntry?.destination?.route == "expenses",
                        onClick = { navController.navigate("expenses") },
                        label = {
                            Text("Expenses")
                        },
                        icon = {
                            Icon(
                                painterResource(id = R.drawable.upload),
                                contentDescription = "Upload"
                            )
                        }
                    )
                    NavigationBarItem(
                        selected = backStackEntry?.destination?.route == "reports",
                        onClick = { navController.navigate("reports") },
                        label = {
                            Text("Reports")
                        },
                        icon = {
                            Icon(
                                painterResource(id = R.drawable.bar_chart),
                                contentDescription = "Reports"
                            )
                        }
                    )
                    NavigationBarItem(
                        selected = backStackEntry?.destination?.route == "add",
                        onClick = { navController.navigate("add") },
                        label = {
                            Text("Add")
                        },
                        icon = {
                            Icon(
                                painterResource(id = R.drawable.add),
                                contentDescription = "Add"
                            )
                        }
                    )
                    NavigationBarItem(
                        selected = backStackEntry?.destination?.route?.startsWith("setting")
                            ?: false,
                        onClick = { navController.navigate("setting") },
                        label = {
                            Text("Settings")
                        },
                        icon = {
                            Icon(
                                painterResource(id = R.drawable.settings_outlined),
                                contentDescription = "Settings"
                            )
                        }
                    )
                }
            }
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "expenses"
            ) {
                composable("expenses") {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        Expenses(navController)
                    }
                }
                composable("reports") {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        Report()
                    }
                }
                composable("add") {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        AddExpenses(navController)
                    }
                }
                composable("setting") {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        Setting(navController)
                    }
                }
                composable("setting/categories") {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        categories(navController)
                    }
                }
            }
        }
    )

  */
}
//@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun HomeScreenPreview(){

   HomeScreen()
}
