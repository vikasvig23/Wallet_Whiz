package com.example.expensestracker.data

import androidx.compose.ui.graphics.Color
import com.example.expensestracker.db_model.Category
import com.example.expensestracker.db_model.ExpensesFb

import com.example.expensestracker.db_model.Recurrence


import io.github.serpro69.kfaker.Faker
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

val faker = Faker()

val mockCategories = listOf(
    Category(
        "Bills",
        Color(
            faker.random.nextInt(0, 255),
            faker.random.nextInt(0, 255),
            faker.random.nextInt(0, 255)
        ).toString()
    ),
    Category(
        "Subscriptions", Color(
            faker.random.nextInt(0, 255),
            faker.random.nextInt(0, 255),
            faker.random.nextInt(0, 255)
        ).toString()
    ),
    Category(
        "Take out", Color(
            faker.random.nextInt(0, 255),
            faker.random.nextInt(0, 255),
            faker.random.nextInt(0, 255)
        ).toString()
    ),
    Category(
        "Hobbies", Color(
            faker.random.nextInt(0, 255),
            faker.random.nextInt(0, 255),
            faker.random.nextInt(0, 255)
        ).toString()
    ),
)

//val mockExpenses: List<ExpensesFb> = List(30) {
//    ExpensesFb(
//        faker.random.nextInt(min = 1, max = 999).toDouble() + faker.random.nextDouble(),
//        faker.random.randomValue(
//            listOf(
//                Recurrence.None,
//                Recurrence.Daily,
//                Recurrence.Monthly,
//                Recurrence.Weekly,
//                Recurrence.Yearly
//            )
//        ),
//        LocalDateTime.now().minus(
//            faker.random.nextInt(min = 300, max = 345600).toLong(),
//            ChronoUnit.SECONDS
//        ),
//        "", // time
//        faker.australia.animals(), // note
//        faker.random.randomValue(mockCategories) // category
//    )
//}
