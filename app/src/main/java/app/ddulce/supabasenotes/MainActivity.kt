package app.ddulce.supabasenotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.ddulce.supabasenotes.ui.theme.SupabaseNotesTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SupabaseNotesTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NotesList()
                }
            }
        }
    }
}

@Composable
fun NotesList() {
    val notes = remember { mutableStateListOf<Note>() }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val res = supabase.from("notes").select().decodeList<Note>()
            notes.addAll(res)}
    }
    Column {
        LazyColumn {
            items(notes, key = { it.id }) { note ->
                ListItem(headlineContent = { Text(note.body) })
            }
        }
        var newNote by remember { mutableStateOf("") }
        val composableScope = rememberCoroutineScope()
        Row {
            OutlinedTextField(
                value = newNote,
                onValueChange = { newNote = it },
                label = { Text("New note") }
            )
            Button(
                onClick = {
                    if (newNote.isBlank()) {
                        return@Button
                    }
                    composableScope.launch(Dispatchers.IO) {
                        val note = supabase.from("notes").insert(mapOf("body" to newNote)) {
                            select()
                            single()
                        }.decodeAs<Note>()
                        notes.add(note)
                        newNote = ""
                    }
                }
            ) {
                Text("Add")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesListPreview() {
    SupabaseNotesTheme {
        NotesList()
    }
}