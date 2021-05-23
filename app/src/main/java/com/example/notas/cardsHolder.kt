package com.example.notas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notas.databinding.FragmentCardsHolderBinding


class cardsHolder : Fragment() {
    private var _binding: FragmentCardsHolderBinding? = null
    private val binding get() = _binding!!
    private var adapter: NoteAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var title = arguments?.getString("title")
        var description = arguments?.getString("description")
        setHasOptionsMenu(true)


        if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(description)){
            var nota = NoteData()
            title.let { nota.title = title }
            description.let { nota.description = description }
            addNote(nota)
        }

        initializeSerializer(requireContext().applicationContext)

        when (activity?.intent?.action) {
            Intent.ACTION_SEND -> {
                if ("text/plain" == activity?.intent?.type){
                    activity?.intent?.getStringExtra(Intent.EXTRA_TEXT)?.let {
                        val bundle = bundleOf("description" to  it)
                        activity?.intent = null
                        findNavController().navigate(R.id.action_cardsHolder_to_newNote, bundle)
                    }
                }
            }
        }
    }

    fun GoToNote(b:Bundle){
        findNavController().navigate(R.id.action_cardsHolder_to_note, b)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCardsHolderBinding.inflate(inflater, container, false)

        binding.btnAdd.setOnClickListener(){
            findNavController().navigate(R.id.action_cardsHolder_to_newNote)
        }


//        if(!atatched){binding.recicler.layoutManager = layoutManager
//            atatched = true
//        }
        adapter = NoteAdapter(this, noteList, getActivity()?.getSharedPreferences("Notes", Context.MODE_PRIVATE))
        binding.recicler.layoutManager =  LinearLayoutManager(context)
        binding.recicler.itemAnimator = DefaultItemAnimator()
        binding.recicler.adapter = adapter
        adapter?.update(noteList)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val prefs = activity?.getSharedPreferences("Notes", Context.MODE_PRIVATE)
        if (prefs != null) {
            val dividers = prefs.getBoolean("show_dividers", true)
            if (dividers)
                binding.recicler.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
            else {
                if (  binding.recicler.itemDecorationCount > 0) binding.recicler.removeItemDecorationAt(0)
            }
            adapter = NoteAdapter(this, noteList, getActivity()?.getSharedPreferences("Notes", Context.MODE_PRIVATE))
            binding.recicler.layoutManager =  LinearLayoutManager(context)
            binding.recicler.itemAnimator = DefaultItemAnimator()
            binding.recicler.adapter = adapter


        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val searchFilter = menu.findItem(R.id.app_bar_search)
        val searchView = searchFilter.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter?.filter?.filter(newText)
                return false
            }

        })


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
//                val intent = Intent(activity?.application, SettingsActivity::class.java)
//                startActivity(intent)
                findNavController().navigate(R.id.action_cardsHolder_to_settingsActivity22)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object notesManager{
        public var noteList: ArrayList<NoteData> = ArrayList()
        var serializer:JSONSerializer? = null
        fun addNote(note: NoteData){
            noteList.add(note)
        }

    fun initializeSerializer(context:Context){
        serializer?.let { return }
        serializer = JSONSerializer("save.json", context)
        try {
            noteList = serializer!!.load()
        } catch (e: Exception) {
            noteList = ArrayList()
            Log.e("Error loading notes: ", "", e)
        }
    }

    fun saveNotes() {
        try {
            serializer!!.save(this.noteList!!)
        } catch (e: Exception) {
            Log.e("Error Saving Notes", "", e)
        }
    }

    fun deleNote(note:NoteData){
        noteList.remove(note)
    }

    fun getNote(position:Int):NoteData{
        return noteList[position]
    }

}

}