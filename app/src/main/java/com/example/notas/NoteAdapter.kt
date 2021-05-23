package com.example.notas

import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class NoteAdapter(
    private val holder: cardsHolder,
    private var noteList: List<NoteData>,
    var prefs: SharedPreferences?
)
    :RecyclerView.Adapter<NoteAdapter.ListItemHolder>(), Filterable{
    private var noteListFiltered = ArrayList<NoteData>(noteList)
    private var searchByTitle = true
    private var searchByDescription = false

    private var TAMMANHO_PREVIW_DESCRICAO = 50 

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ListItemHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        if (prefs != null) {
            itemView.findViewById<TextView>(R.id.ListItemTitle).textSize = prefs!!.getInt("tamanho_titulo_lista", 24).toFloat()
            itemView.findViewById<TextView>(R.id.ListItemDescription).textSize = prefs!!.getInt("tamanho_descricao_lista", 14).toFloat()
            searchByTitle = prefs!!.getBoolean("search_title", true)
            searchByDescription = prefs!!.getBoolean("search_description", false)
        }

        return ListItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val note = noteListFiltered[position]
        holder.title.text = note.title
        if(note.description!!.length <= TAMMANHO_PREVIW_DESCRICAO && !TextUtils.isEmpty(note.description!!))
            holder.description.text = note.description!!
        else if(!TextUtils.isEmpty(note.description!!))
            holder.description.text = note.description!!.substring(0, TAMMANHO_PREVIW_DESCRICAO)

    }

    fun update(notes:ArrayList<NoteData>){
         noteList = notes
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        if(noteListFiltered != null)
            return noteListFiltered.size
        return  -1
    }

    override fun getFilter(): Filter {
        return filter
    }

    private val filter = object :Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filtered = ArrayList<NoteData>()
            var contains = false
            if(TextUtils.isEmpty(constraint)) filtered.addAll(noteList)
            else{
                val pattern = constraint.toString().toLowerCase().trim()
                noteList.forEach {
                    if(searchByTitle){
                        if(it.title?.toLowerCase()!!.contains(pattern)) contains = true
                    }
                    if(searchByDescription){
                        if(it.description?.toLowerCase()!!.contains(pattern)) contains = true
                    }
                    if(contains) filtered.add(it)
                    contains = false
                }
            }
            val results = FilterResults()
            results.values = filtered
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            noteListFiltered.clear()
            if (results != null) noteListFiltered.addAll(results.values as ArrayList<NoteData>)
            notifyDataSetChanged()
        }



    }

    inner class ListItemHolder(view: View):
            RecyclerView.ViewHolder(view), View.OnClickListener{

        internal var title = view.findViewById<View>(R.id.ListItemTitle) as TextView
        internal var description = view.findViewById<View>(R.id.ListItemDescription) as TextView

        init {
            view.isClickable = true
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val note = cardsHolder.notesManager.getNote(adapterPosition)
            val bundle = Bundle()
            bundle.putSerializable("note", note)
            holder.GoToNote(bundle)
        }


    }






}
