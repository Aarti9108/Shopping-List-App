package com.example.shoppinglistapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



data class ShoppingItem(val id: Int,
                        var name:String,
                        var quantity:Int,
                        var isEditing: Boolean = false)


@Composable
fun ShoppingListApp() {

    //Shopping list state that updates the UI when items change
    val sItems by remember { mutableStateOf(listOf<ShoppingItem>()) }

    var showDialog by remember { mutableStateOf(false) }

    var itemName by remember { mutableStateOf("") }

    var itemQuantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        ) {
            Text("Add Items to the list")
        }

        // A scrollable vertical list that fills the whole screen
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            items(sItems) {

            }

        }


        if (showDialog) {
            AlertDialog(onDismissRequest = { showDialog = false },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Button(onClick = {}) {
                        Text("Add")
                    }
                    Button(onClick = {showDialog = false}) {
                        Text("Cancel")
                    }
                }
            },
            title = { Text("Add Shopping Items") },
            text = {
                Column {
                        OutlinedTextField(
                            value= itemName,
                            onValueChange = { itemName = it },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                            )
                        OutlinedTextField(
                            value= itemName,
                            onValueChange = { itemQuantity = it },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )


                }
            }
            )
        }

    }
}