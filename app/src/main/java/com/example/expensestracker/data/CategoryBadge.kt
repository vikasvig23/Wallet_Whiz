package com.example.expensestracker.data

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.expensestracker.db_model.Category
import com.example.expensestracker.ui.theme.Shapes
import com.example.expensestracker.utils.Utility

@Composable
fun CategoryBadge(category: Category, modifier: Modifier = Modifier) {
    val badgeColor = category.toColor()

    Surface(
        shape = Shapes.large,
        color = badgeColor.copy(alpha = 0.25f),
        modifier = modifier,
    ) {
        Text(
            category.name,
            color = badgeColor,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            fontFamily = Utility.Poppins,
        )
    }
}
