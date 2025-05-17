import android.content.Context
import org.json.JSONArray
import java.io.File
import java.util.Timer
import java.util.TimerTask

class FileNotebook(private val context: Context) {

    private val notes = mutableListOf<Note>()

    fun getNotes(): List<Note> = notes.toList()

    fun addNote(note: Note) {
        notes.add(note)
    }

    fun removeNoteByUid(uid: String) {
        notes.removeAll { it.uid == uid }
    }

    fun saveToFile(filename: String = "notebook.json") {
        val file = File(context.filesDir, filename)
        val jsonArray = JSONArray()
        notes.forEach { jsonArray.put(it.toJson()) }
        file.writeText(jsonArray.toString())
    }

    fun loadFromFile(filename: String = "notebook.json") {
        val file = File(context.filesDir, filename)
        if (!file.exists()) return
        val content = file.readText()
        val jsonArray = JSONArray(content)
        notes.clear()
        for (i in 0 until jsonArray.length()) {
            Note.parse(jsonArray.getJSONObject(i))?.let { notes.add(it) }
        }
    }

    fun scheduleNoteDeletion(uid: String, delayMillis: Long = 60000) {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                removeNoteByUid(uid)
            }
        }, delayMillis)
    }
}