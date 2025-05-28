import android.graphics.Color
import org.json.JSONException
import org.json.JSONObject
import java.util.UUID

enum class Importance {
    LOW, NORMAL, HIGH
}

data class Note(
    val uid: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val color: Int = Color.WHITE,
    val importance: Importance = Importance.NORMAL,
    val selfDestructTime: Long? = null
) {
    companion object {
        @Throws(JSONException::class)
        fun parse(json: JSONObject): Note {
            return Note(
                uid = json.optString("uid", UUID.randomUUID().toString()),
                title = json.getString("title"),
                content = json.getString("content"),
                color = json.optInt("color", Color.WHITE),
                importance = json.optString("importance")?.let { Importance.valueOf(it) } 
                    ?: Importance.NORMAL,
                selfDestructTime = json.optLong("selfDestructTime").takeIf { it > 0 }
            )
        }
    }

    val json: JSONObject
        get() = JSONObject().apply {
            put("uid", uid)
            put("title", title)
            put("content", content)
            if (color != Color.WHITE) put("color", color)
            if (importance != Importance.NORMAL) put("importance", importance.name)
            selfDestructTime?.let { put("selfDestructTime", it) }
        }
}
