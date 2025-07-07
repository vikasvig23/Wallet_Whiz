package com.example.expensestracker.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

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
import com.example.expensestracker.ui.theme.TopAppBarBackground

//@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable

fun HomeScreen(){
   /* Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(28.dp)

    ){

        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(0.dp, 20.dp)) {
            HeadingTextComponents(value = "Home")

            ButtonComponent(value = stringResource(R.string.logout), onButtonClicked = {
                loginViewModel.logout()
            },
                isEnabled = true
            )


        }
        } */

 /*   val navController=rememberNavController()

    Scaffold(
            bottomBar={ BottomBar(navController=navController)}

    ) {

        BottomNavGraph(navController = navController)

    }

}*/






    val navController=rememberNavController()


  //  homeViewModel.getUserData()

    Scaffold(
        bottomBar={ BottomBar(navController=navController)}

    ) {


        BottomNavGraph(navController = navController)
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

     showBottomBar=when(navBackStackEntry?.destination?.route){
        "setting/categories"->false
         else ->true
    }
    NavigationBar {
        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
                .background(Color.Transparent)
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



@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    val contentColor =
        if (selected) Color.White else Color.Black
    val background =
        if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else Color.Transparent

/*
    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        //unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }

    )*/
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
