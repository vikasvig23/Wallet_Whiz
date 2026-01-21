package com.example.expensestracker.data

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensestracker.R
import com.example.expensestracker.navigation.ui.theme.FillTertiary
import com.example.expensestracker.navigation.ui.theme.Shapes
import com.example.expensestracker.screens.ui.theme.DarculaCard
import com.example.expensestracker.screens.ui.theme.DarculaMuted
import com.example.expensestracker.screens.ui.theme.DarculaText
import com.example.expensestracker.utils.Utility.backborder


@Composable
fun RecurrenceTrigger(
    label: String?,
    placeholder: String = "Select",
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isPlaceholder = label.isNullOrBlank() || label == placeholder

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = DarculaCard,
        modifier = modifier,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            (if (isPlaceholder) placeholder else label)?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isPlaceholder)
                        DarculaMuted      // 👈 placeholder color
                    else
                        DarculaText
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = if (isPlaceholder) DarculaMuted else DarculaText
            )
        }
    }
}


@Preview
@Composable
fun Previewtrig(){
    RecurrenceTrigger("this week", onClick = {})
}