package com.example.notas

import android.content.Context
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONTokener
import java.io.*
import java.util.ArrayList

class JSONSerializer(private val filename: String, private val context: Context) {
        @Throws(IOException::class, JSONException::class)
        fun save(notes: List<NoteData>) {
            val jArray = JSONArray()
            for (n in notes)
                jArray.put(n.parseToJson())
            var writer: Writer? = null
            try {
                val out = context.openFileOutput(filename,
                    Context.MODE_PRIVATE)
                writer = OutputStreamWriter(out)
                writer.write(jArray.toString())
            } finally {
                if (writer != null) {
                    writer.close()
                }
            }
        }

        @Throws(IOException::class, JSONException::class)
        fun load(): ArrayList<NoteData> {
            val noteList = ArrayList<NoteData>()
            var reader: BufferedReader? = null

            try {
                val `in` = context.openFileInput(filename)
                reader = BufferedReader(InputStreamReader(`in`))
                val jsonString = StringBuilder()
                for (line in reader.readLine()) {
                    jsonString.append(line)
                }

                val jArray = JSONTokener(jsonString.toString()).nextValue() as JSONArray

                for (i in 0 until jArray.length()) {
                    noteList.add(NoteData(jArray.getJSONObject(i)))
                }
            } catch (e: FileNotFoundException) {
            } finally {
                reader!!.close()
            }
            return noteList
        }


    }