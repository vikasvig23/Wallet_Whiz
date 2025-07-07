package com.example.expensestracker

import com.example.expensestracker.db_model.Category
import com.example.expensestracker.db_model.Expense
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

val config = RealmConfiguration.create(schema = setOf(Expense::class, Category::class))
val db: Realm = Realm.open(config)