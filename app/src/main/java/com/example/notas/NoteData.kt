package com.example.notas

import java.io.Serializable
import org.json.JSONObject

class NoteData: Serializable{
    var title: String? = null
    var description: String? = null
    var idea: Boolean = false
    var todo: Boolean = false
    var important: Boolean = false

    private val JSON_TITLE = "title"
    private val JSON_DESCRIPTION = "description"

    constructor(j:JSONObject){
        title = j.getString("title")
        description = j.getString("description")

    }
    constructor(){}

    fun parseToJson(): JSONObject{
        val j = JSONObject()
        j.put(JSON_TITLE, title)
        j.put(JSON_DESCRIPTION, description)
        return j
    }



}