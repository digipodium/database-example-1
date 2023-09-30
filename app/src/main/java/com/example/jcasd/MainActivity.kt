@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.jcasd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.jcasd.data.local.AppDatabase
import com.example.jcasd.data.local.AppEvent
import com.example.jcasd.data.local.UiState
import com.example.jcasd.ui.AppViewModel
import com.example.jcasd.ui.theme.JetpackComposeAppSimpleDatabaseTheme

class MainActivity : ComponentActivity() {

    //    step 14 create a db variable
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app.db"
        ).build()
    }

    //    step 15 create a view model variable
    private val viewModel by viewModels<AppViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AppViewModel(db.dao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeAppSimpleDatabaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val state = viewModel.state.collectAsState()
                    WatchListScreen(
                        state = state.value,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }
    }
}

@Composable
fun WatchListScreen(
    state: UiState,
    onEvent: (AppEvent) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(AppEvent.ShowDialog) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add New Entry"
                )
            }
        }
    ) { padding ->
        if (state.isDialogVisible) {
            AddEntryDialog(
                state = state,
                onEvent = onEvent
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn {
                items(state.shows) {
                    ShowItem(state, onEvent)
                }
            }
        }
    }
}

@Composable
fun ShowItem(state: UiState, onEvent: (AppEvent) -> Unit) {
    Row {
        Text(text = state.title)
    }
}

@Composable
fun AddEntryDialog(
    state: UiState,
    onEvent: (AppEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { onEvent(AppEvent.DismissDialog) },
        confirmButton = {
            IconButton(onClick = { onEvent(AppEvent.SaveShow) }) {
                Icon(imageVector = Icons.Default.Done, contentDescription = null)
            }
        },
        title = {
            Text(text = "Add New Entry")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = state.title,
                    onValueChange = { onEvent(AppEvent.SetTitle(it)) },
                    placeholder = { Text(text = "Title") }
                )
                OutlinedTextField(
                    value = state.ott,
                    onValueChange = { onEvent(AppEvent.SetOtt(it)) },
                    placeholder = { Text(text = "Title") }
                )
                OutlinedTextField(
                    value = state.priority.toString(),
                    onValueChange = {
                        if (it.isNotEmpty()) {
                            onEvent(
                                AppEvent.SetPriority(
                                    it.toInt()
                                )
                            )
                        }
                    },
                    placeholder = { Text(text = "Title") }
                )
            }
        }
    )
}

