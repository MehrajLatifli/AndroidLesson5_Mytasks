import android.content.Context
import com.example.mytasks.Models.Todo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SharedPreferences(context: Context) {

    companion object {
        private const val PREF_NAME = "todo_prefs"
        private const val TODO_LIST_KEY = "todo_list"

        fun saveArrayListToSharedPreferences(context: Context, arrayList: ArrayList<Todo>) {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val gson = Gson()
            val json = gson.toJson(arrayList)
            editor.putString(TODO_LIST_KEY, json)
            editor.apply()
        }


        fun getArrayListFromSharedPreferences(context: Context): ArrayList<Todo> {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString(TODO_LIST_KEY, null)
            val type: Type = object : TypeToken<ArrayList<Todo>>() {}.type
            return gson.fromJson(json, type) ?: ArrayList()
        }
    }
}
