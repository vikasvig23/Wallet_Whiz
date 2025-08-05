package com.example.expensestracker.data

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope


import com.example.expensestracker.db_model.Category
import com.example.expensestracker.utils.PrefDataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class CategoriesState(
    val newCategoryColor: Color = Color.White,
    val newCategoryName: String = "",
    val colorPickerShowing: Boolean = false,
    val categories: List<Category> = listOf()
)

class CategoriesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoriesViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CategoriesViewModel(context: Context) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoriesState())
    val uiState: StateFlow<CategoriesState> = _uiState.asStateFlow()

    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userEmail = auth.currentUser?.email?.replace(".", "_") ?: "unknown_user"

    init {
        viewModelScope.launch {
            fetchCategoriesFromFirebase(context)
        }

    }

    private suspend fun fetchCategoriesFromFirebase(context: Context) {
        val email = PrefDataStore.getEmail(context)
        if (email != null) {
            database.getReference("expenses").child(email).child("category")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val categoryList = mutableListOf<Category>()
                        for (catSnap in snapshot.children) {
                            try {
                                if (catSnap.value is Map<*, *>) {
                                    val categoryFb = catSnap.getValue(Category::class.java)
                                    categoryFb?.let { categoryList.add(it) }
                                } else {
                                    Log.e("Firebase", "Skipped invalid category: ${catSnap.value}")
                                }
                            } catch (e: Exception) {
                                Log.e("Firebase", "Invalid category data: ${e.message}")
                            }
                        }



                        _uiState.update { it.copy(categories = categoryList) }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error if needed
                    }
                })
        }
    }

    fun setNewCategoryColor(color: Color) {
        _uiState.update { it.copy(newCategoryColor = color) }
    }

    fun setNewCategoryName(name: String) {
        _uiState.update { it.copy(newCategoryName = name) }
    }

    fun showColorPicker() {
        _uiState.update { it.copy(colorPickerShowing = true) }
    }

    fun hideColorPicker() {
        _uiState.update { it.copy(colorPickerShowing = false) }
    }

    suspend fun createNewCategory(context: Context) {
        val email = PrefDataStore.getEmail(context)
        viewModelScope.launch(Dispatchers.IO) {
            val categoryName = _uiState.value.newCategoryName
            val color = _uiState.value.newCategoryColor
            val colorValue = "${color.red},${color.green},${color.blue}"
            val id = UUID.randomUUID().toString()

            val categoryFb = Category(
               // id = id,
                name = categoryName,
                colorValue = colorValue
            )

            if (email != null) {
                database.getReference("expenses")
                    .child(email)
                    .child("category")
                    .push()
                    .setValue(categoryFb)
            }

            _uiState.update { it.copy(
                newCategoryColor = Color.White,
                newCategoryName = ""
            )}
        }
    }

    fun deleteCategory(context: Context,category: Category) {

        viewModelScope.launch(Dispatchers.IO) {
            val email = PrefDataStore.getEmail(context)
            val colorValue =  category.colorValue


            // Find and delete category with matching name and colorValue
            if (email != null) {
                database.getReference("expenses").child(email).child("category")
                    .get().addOnSuccessListener { snapshot ->
                        for (child in snapshot.children) {
                            val fb = child.getValue(Category::class.java)
                            if (fb?.name == category.name && fb.colorValue == colorValue) {
                                child.ref.removeValue()
                                break
                            }
                        }
                    }
            }
        }
    }
}
