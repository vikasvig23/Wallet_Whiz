package com.example.expensestracker.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.expensestracker.R
import com.example.expensestracker.ui.theme.Destructive
import com.example.expensestracker.ui.theme.TextPrimary



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableRow(
  //  label: String?=null,
    modifier: Modifier = Modifier,
    label: String?=null,
    hasArrow: Boolean = false,
    isDestructive: Boolean = false,
  //  titleContentColor: Color,
  //  textColor: Color,
   // colors: TopAppBarColors,
  //  onClicked: () -> Unit,
    detailContent: (@Composable RowScope.() -> Unit)? = null,
    content: (@Composable RowScope.() -> Unit)? = null
           // logoutButtonClicked: () -> Unit
) {
    val textColor = if (isDestructive) Destructive else TextPrimary

    Row(
        modifier = modifier.fillMaxWidth(),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

    ) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
        if (content != null) {
            content()
        }

        if (hasArrow) {
            Icon(
                painterResource(id = R.drawable.chevron_right),
                contentDescription = "Right arrow",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
        }
        if (detailContent != null) {
            detailContent()
        }
    }
}


