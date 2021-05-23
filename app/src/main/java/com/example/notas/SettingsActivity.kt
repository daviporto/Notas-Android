package com.example.notas

import android.content.Context
import android.icu.number.IntegerWidth
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

import com.example.notas.databinding.SettingsActivityBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var bindings:SettingsActivityBinding
    private var show_dividers = true
    private var search_title = true
    private var search_description = false
    private var tamanho_titulo_nota = 24
    private var tamanho_descricao_nota = 18
    private var tamanho_titulo_lista = 24
    private var tamanho_descricao_lista = 14
    private val textSizes = intArrayOf(8,10,12,14,18,24,30,36)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(bindings.root)
    }

    override fun onResume() {
        super.onResume()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val prefs = getSharedPreferences("Notes", Context.MODE_PRIVATE)
        show_dividers = prefs.getBoolean("show_dividers", true)
        search_title = prefs.getBoolean("search_title", true)
        search_description = prefs.getBoolean("search_description", false)
        tamanho_titulo_nota = prefs.getInt("tamanho_titulo_nota", 24)
        tamanho_descricao_nota = prefs.getInt("tamanho_descricao_nota", 18)
        tamanho_titulo_lista = prefs.getInt("tamanho_titulo_lista", 24)
        tamanho_descricao_lista = prefs.getInt("tamanho_descricao_lista", 14)


        bindings.swtMostrarDivisor.isChecked = show_dividers
        bindings.swtMostrarDivisor.setOnCheckedChangeListener{ buttonView, isChecked ->
            show_dividers = isChecked }

        bindings.swtPesquisarTitulo.isChecked = search_title
        bindings.swtPesquisarTitulo.setOnCheckedChangeListener{ buttonView, isChecked ->
            search_title = isChecked }

        bindings.swtPesquisarDescricao.isChecked = search_description
        bindings.swtPesquisarDescricao.setOnCheckedChangeListener{ buttonView, isChecked ->
            search_description = isChecked }

        bindings.spnTamanhoDescricaoLista.setSelection(getPosition(tamanho_descricao_lista))
        bindings.spnTamanhoDescricaoLista.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tamanho_descricao_lista = Integer.valueOf(bindings.spnTamanhoDescricaoLista.selectedItem.toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        bindings.spnTamanhoDescricaoNota.setSelection(getPosition(tamanho_descricao_nota))
        bindings.spnTamanhoDescricaoNota.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tamanho_descricao_nota = Integer.valueOf(bindings.spnTamanhoDescricaoNota.selectedItem.toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        bindings.spnTamanhoTituloLista.setSelection(getPosition(tamanho_titulo_lista))
        bindings.spnTamanhoTituloLista.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tamanho_titulo_lista = Integer.valueOf(bindings.spnTamanhoTituloLista.selectedItem.toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        bindings.spnTamanhoTituloNota.setSelection(getPosition(tamanho_titulo_nota))
        bindings.spnTamanhoTituloNota.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                tamanho_titulo_nota =
                    Integer.valueOf(bindings.spnTamanhoTituloNota.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun getPosition(n:Int): Int{
        textSizes.forEachIndexed { index, i -> if(i == n) return index }
        return 3
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    override fun onPause() {
        super.onPause()
        val prefs = getSharedPreferences("Notes", Context.MODE_PRIVATE)
        val edit = prefs.edit()
        edit.putBoolean("show_dividers", show_dividers)
        edit.putBoolean("search_title", search_title)
        edit.putBoolean("search_description", search_description)
        edit.putInt("tamanho_titulo_nota", tamanho_titulo_nota)
        edit.putInt("tamanho_descricao_nota", tamanho_descricao_nota)
        edit.putInt("tamanho_titulo_lista", tamanho_titulo_lista)
        edit.putInt("tamanho_descricao_lista", tamanho_descricao_lista)
        edit.apply()
    }



}