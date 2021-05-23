package com.example.notas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notas.R.menu
import com.example.notas.databinding.FragmentNewNoteBinding
import com.example.notas.databinding.FragmentNoteBinding


class Note: Fragment() {
    private lateinit var _note:NoteData
    private  var _binding:FragmentNoteBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        _note = arguments?.getSerializable("note") as NoteData
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        _binding = FragmentNoteBinding.inflate(inflater, container, false)

        binding.txtTitle.setText(_note.title)
        binding.txtDescription.setText(_note.description)
        val prefs = activity?.getSharedPreferences("Notes", Context.MODE_PRIVATE)
        if(prefs != null){
            binding.txtTitle.textSize = prefs.getInt("tamanho_titulo_nota", 24).toFloat()
            binding.txtDescription.textSize = prefs.getInt("tamanho_descricao_nota", 18).toFloat()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_notes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.mt_back-> {
                findNavController().popBackStack()
                true
            }
            R.id.mt_done->{
                _note.title = binding.txtTitle.text.toString()
                _note.description = binding.txtDescription.text.toString()
                true
            }
            R.id.mt_delete->{
                cardsHolder.notesManager.deleNote(_note)
                findNavController().popBackStack()
                true
            }

            R.id.mt_share ->{
                val text = binding.txtTitle.text.toString() + "\n" + binding.txtDescription.text.toString()
                val sendIntent:Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, text)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(sendIntent, null))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}