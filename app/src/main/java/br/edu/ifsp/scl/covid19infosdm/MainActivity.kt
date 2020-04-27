package br.edu.ifsp.scl.covid19infosdm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import br.edu.ifsp.scl.covid19infosdm.viewmodel.Covid19ViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: Covid19ViewModel
    private lateinit var countryAdapter: ArrayAdapter<String>

    // preenchera o spinner de informacao / classe para os servicos que serao acessados:
    private enum class Information(val type: String){
        DAY_ONE("Day one"),
        BY_COUNTRY("By country")
    }

    // preenchera o spinner de status / classe para o status que sera buscado no servico:
    private enum class Status(val type: String){
        CONFIRMED("Confirmed"),
        RECOVERED("Recovered"),
        DEATHS("Deaths")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = Covid19ViewModel(this)

        countryAdapterInit()

        informationAdapterInit()

        statusAdapterInit()
    }



    fun onRetrieveClick(view: View){

    }

    private fun countryAdapterInit(){
        // populado pelo web service
        countryAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        spinnerPais.adapter = countryAdapter
        viewModel.fetchCountries().observe(
            this,
            Observer { countryList ->
                countryList.forEach { countryListItem ->
                    if (countryListItem.country.isNotEmpty()){countryAdapter.add(countryListItem.country)}
                }
            }
        )
    }

    private fun informationAdapterInit(){
        // populado pela enum class Information
        val informationList = arrayListOf<String>()
        Information.values().forEach { informationList.add(it.type) }

        spinnerInfo.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, informationList)
        spinnerInfo.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    Information.DAY_ONE.ordinal -> {
                        tvModoVisualizacao.visibility = View.GONE // modo texto
                        rgModoVisualizacao.visibility = View.GONE // modo grafico
                    }
                    Information.BY_COUNTRY.ordinal -> {
                        tvModoVisualizacao.visibility = View.VISIBLE
                        rgModoVisualizacao.visibility = View.VISIBLE
                    }
                }
            }

        }
    }

    private fun statusAdapterInit(){
        val statusList = arrayListOf<String>()
        Status.values().forEach { statusList.add(it.type) }

        spinnerStatus.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, statusList)
    }



}
