import android.content.Context
import org.json.JSONArray
import org.json.JSONException
import java.io.File

class FileNotebook(private val context: Context) {
    private val notes = mutableListOf<Note>()
    private val notesFile = File(context.filesDir, "notes.json")

    fun addNote(note: Note) {
        notes.add(note)
        saveToFile()
    }

    fun getNotes(): List<Note> = notes.toList()

    fun loadFromFile() {
        if (!notesFile.exists()) return
        
        try {
            val json = notesFile.readText()
            val jsonArray = JSONArray(json)
            notes.clear()
            
            for (i in 0 until jsonArray.length()) {
                val note = Note.parse(jsonArray.getJSONObject(i))
                notes.add(note)
            }
        } catch (e: JSONException) {
            println("Error loading notes: ${e.message}")
        }
    }

    private fun saveToFile() {
        val jsonArray = JSONArray().apply {
            notes.forEach { put(it.json) }
        }
        notesFile.writeText(jsonArray.toString())
    }
}
