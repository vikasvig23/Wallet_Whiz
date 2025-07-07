package com.example.expensestracker.data

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

//import com.example.expensestracker.navigation.CurrencyScreen



@Composable
fun CurrencyCard(
  //  onCurrencySelected:  (String) -> Unit,

    country: String, currencyCode: String, onClick: () -> Unit
) {

    val text = remember {
        mutableStateOf("")
    }
    //val state by vm.uiState.collectAsState()
 //   val currency = viewModel.currencyCode.collectAsState()
    val isCurrencySelected = remember { mutableStateOf(false) }
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(false) }
//    var selectedItem by remember { mutableStateOf<String?>(null) }
    Card(
        modifier = Modifier
            // The space between each card and the other
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                // AppRouter.navigateTo(Screen.HomeScreen)
                onClick()
                //isCurrencySelected.value = true
            }
            //   navController.navigate(route = "Expenses/$currencyCode")
            ,
        //onClick = { if(expanded){AppRouter.navigateTo(Screen.HomeScreen)}},
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(15.dp),
        colors =  CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically)
        {
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = country,
                    fontSize = 22.sp,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(0.6f)
                        ,
                  //  color = MaterialTheme.colorScheme.surface
                )
                Text(
                    text = currencyCode,
                    fontSize = 22.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    color = Color.Black,


                )
            }
         
        }


    }
    if (isCurrencySelected.value) {
      //  Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // viewModel.saveData(text.value)
                Toast.makeText(context, "currency $currencyCode updated", Toast.LENGTH_SHORT).show()
                // Perform any additional logic here, like updating the selected currency in the database
                isCurrencySelected.value = false
            },
          //  modifier = Modifier.align()
        ) {
            Text("Set $currencyCode")
        }
    }

}


/*
@Composable
fun ShowToast(message: String) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.show()
        onDispose {
            toast.cancel()
        }
    }
}

 */


/*@Composable
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

 */

/**private fun navigateToExpenseScreen(navController: NavController, currencyCode: String) {
    navController.navigate("Expenses/$currencyCode")
}
 **/





