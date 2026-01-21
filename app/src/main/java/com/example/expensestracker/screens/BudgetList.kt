package com.example.expensestracker.screens

import android.inputmethodservice.Keyboard.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.expensestracker.screens.ui.theme.DarculaText
import com.example.expensestracker.utils.Utility

@Composable
fun BudgetList() {

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(10.dp),

    ){

        RowList("Your Budget","2000")
        HorizontalDivider(modifier = Modifier.padding(top = 16.dp, bottom =  16.dp))

        RowList("Expenses","2000")
        HorizontalDivider(modifier = Modifier.padding(top = 16.dp, bottom =  16.dp))

        RowList("Saving","2000")

    }
}

@Composable
fun RowList(name1:String,name2:String){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = name1,
            fontFamily = Utility.Poppins,
            style = MaterialTheme.typography.headlineMedium,
            color = DarculaText
        )
        Text(
            text = name2,
            fontFamily = Utility.Poppins,
            style =  MaterialTheme.typography.headlineMedium,
            color =DarculaText
        )

    }
}