package com.example.shoppinglistapp

// Compose imports for layout, UI elements, and state handling
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Data class for each shopping item with optional editing state
data class ShoppingItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false // used to toggle edit mode
)

@Composable
fun ShoppingListApp() {
    // Holds the list of shopping items
    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) }

    // Controls visibility of the add item dialog
    var showDialog by remember { mutableStateOf(false) }

    // Temporary inputs for new item name and quantity
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Button to show the dialog to add a new item
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Items to the list")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Displaying the list of shopping items
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(sItems) { item ->
                // If item is in editing mode, show editor
                if (item.isEditing) {
                    ShoppingItemEditor(item = item, onEditComplete = { editedName, editedQuantity ->
                        // Update the edited item and turn off edit mode
                        sItems = sItems.map {
                            if (it.id == item.id) it.copy(name = editedName, quantity = editedQuantity, isEditing = false)
                            else it.copy(isEditing = false)
                        }
                    })
                } else {
                    // Display item in read mode with edit and delete options
                    ShoppingListItem(
                        item = item,
                        onEditClick = {
                            // Set only this item to edit mode
                            sItems = sItems.map { it.copy(isEditing = it.id == item.id) }
                        },
                        onDeleteClick = {
                            // Remove item from list
                            sItems = sItems - item
                        }
                    )
                }
            }
        }

        // AlertDialog for adding a new item
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Add button logic
                        Button(onClick = {
                            if (itemName.isNotBlank() && itemQuantity.isNotBlank()) {
                                val newItem = ShoppingItem(
                                    id = sItems.size + 1, // Unique ID based on size
                                    name = itemName,
                                    quantity = itemQuantity.toIntOrNull() ?: 1
                                )
                                sItems = sItems + newItem
                                showDialog = false
                                itemName = ""
                                itemQuantity = ""
                            }
                        }) {
                            Text("Add")
                        }
                        // Cancel button to dismiss dialog
                        Button(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                },
                title = { Text("Add Shopping Item") },
                text = {
                    Column {
                        // Text input for item name
                        OutlinedTextField(
                            value = itemName,
                            onValueChange = { itemName = it },
                            singleLine = true,
                            label = { Text("Item Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                        // Text input for quantity
                        OutlinedTextField(
                            value = itemQuantity,
                            onValueChange = { itemQuantity = it },
                            singleLine = true,
                            label = { Text("Quantity") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun ShoppingItemEditor(item: ShoppingItem, onEditComplete: (String, Int) -> Unit) {
    // Local state to hold edited name and quantity
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            // Editable name field
            BasicTextField(
                value = editedName,
                onValueChange = { editedName = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )

            // Editable quantity field
            BasicTextField(
                value = editedQuantity,
                onValueChange = { editedQuantity = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
        }

        // Button to save changes
        Button(onClick = {
            onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
        }) {
            Text("Save")
        }
    }
}

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0xFF018786)), // Teal-colored border
                shape = RoundedCornerShape(20) // Rounded corners
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            // Display item name
            Text(text = item.name, modifier = Modifier.padding(4.dp))
            // Display item quantity
            Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(4.dp))
        }

        Row(modifier = Modifier.padding(4.dp)) {
            // Edit icon button
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }

            // Delete icon button
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
