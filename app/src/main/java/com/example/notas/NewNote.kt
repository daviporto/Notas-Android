package com.example.notas

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notas.databinding.FragmentNewNoteBinding


class NewNote(): Fragment() {
    private var _binding: FragmentNewNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewNoteBinding.inflate(inflater, container, false)

        binding.btnPronto.setOnClickListener{
            val title = binding.txtTitle.text.toString()
            val description = binding.txtDescription.text.toString()
            val note = NoteData()
            note.title = title
            note.description = description
            val bundle = bundleOf("description" to description, "title" to title)
            findNavController().navigate(R.id.action_newNote_to_cardsHolder, bundle)
        }
        binding.btnCancelar.setOnClickListener{
            findNavController().popBackStack()
        }

        val prefs = activity?.getSharedPreferences("Notes", Context.MODE_PRIVATE)
        if(prefs != null){
            binding.txtTitle.textSize = prefs.getInt("tamanho_titulo_nota", 24).toFloat()
            binding.txtDescription.textSize = prefs.getInt("tamanho_descricao_nota", 18).toFloat()
        }
        var description = arguments?.getString("description")
        if(!TextUtils.isEmpty(description))
            binding.txtDescription.setText(description)

        return binding.root
    }



    fun getRoot(): View{
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun setDescription(it: String) {
        binding.txtDescription.setText(it)
    }

}