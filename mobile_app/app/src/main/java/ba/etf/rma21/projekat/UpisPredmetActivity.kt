package ba.etf.rma21.projekat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.repositories.KorisnikRepository

import ba.etf.rma21.projekat.viewmodel.GrupaViewModel
import ba.etf.rma21.projekat.viewmodel.KorisnikViewModel
import ba.etf.rma21.projekat.viewmodel.KvizViewModel
import ba.etf.rma21.projekat.viewmodel.PredmetViewModel

class UpisPredmetActivity() : AppCompatActivity() {
    private lateinit var spinnerGodina: Spinner
    private lateinit var spinnerPredmet: Spinner
    private lateinit var spinnerGrupa: Spinner
    private lateinit var dugmeUpisi: Button
    private var grupaViewModel = GrupaViewModel()
    private var kvizViewModel = KvizViewModel()
    private var korisnikViewModel = KorisnikViewModel()
    private var predmetViewModel = PredmetViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upis_predmeta_fragment)

        spinnerGodina = findViewById(R.id.odabirGodina)
        spinnerPredmet = findViewById(R.id.odabirPredmet)
        spinnerGrupa = findViewById(R.id.odabirGrupa)
        dugmeUpisi = findViewById(R.id.dodajPredmetDugme)

        ArrayAdapter.createFromResource(this, R.array.spinnerGodine, android.R.layout.simple_spinner_item)
            .also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGodina.adapter = adapter
        }

        /*spinnerGodina.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val listaNeupisanihPredmeta = predmetViewModel.dajSve().filter{ p -> !korisnikViewModel.dajPredmete().contains(p) }
                postaviPredmete(listaNeupisanihPredmeta.filter { p -> p.godina == position })

                if(spinnerGodina.selectedItem.toString() != "/" && spinnerPredmet.selectedItem.toString() != "/"
                    && spinnerGrupa.selectedItem.toString() != "/") {
                    dugmeUpisi.isEnabled = true
                    dugmeUpisi.isClickable = true
                } else {
                    dugmeUpisi.isEnabled = false
                    dugmeUpisi.isClickable = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerPredmet.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val listaGrupa = grupaViewModel.dajSveGrupe().filter{ g -> g.naziv.contains(spinnerPredmet.selectedItem.toString())
                        && g.nazivPredmeta == spinnerPredmet.selectedItem }
                postaviGrupe(listaGrupa)

                if(spinnerGodina.selectedItem.toString() != "/" && spinnerPredmet.selectedItem.toString() != "/"
                    && spinnerGrupa.selectedItem.toString() != "/") {
                    dugmeUpisi.isEnabled = true
                    dugmeUpisi.isClickable = true
                } else {
                    dugmeUpisi.isEnabled = false
                    dugmeUpisi.isClickable = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerGrupa.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(spinnerGodina.selectedItem.toString() != "/" && spinnerPredmet.selectedItem.toString() != "/"
                    && spinnerGrupa.selectedItem.toString() != "/") {
                    dugmeUpisi.isEnabled = true
                    dugmeUpisi.isClickable = true
                } else {
                    dugmeUpisi.isEnabled = false
                    dugmeUpisi.isClickable = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        dugmeUpisi.setOnClickListener{
            if(spinnerGodina.selectedItem.toString() != "/" && spinnerPredmet.selectedItem.toString() != "/"
            && spinnerGrupa.selectedItem.toString() != "/") {

                val odabraniP = predmetViewModel.dajSve().find { p -> p.naziv == spinnerPredmet.selectedItem }
                val odabraniG = grupaViewModel.dajSveGrupe().find { p -> p.naziv == spinnerGrupa.selectedItem }

                korisnikViewModel.dodajPredmetIGrupu(odabraniP, odabraniG)

                finish()
            }
        }*/

    }

    private fun postaviGrupe(listaGrupa: List<Grupa>?) {
        if(listaGrupa == null) return
        val listaNazivaGrupa: List<String> = listaGrupa.map { p -> p.naziv }
        val spinnerGrupeAdapter: ArrayAdapter<String> = ArrayAdapter(this,
            R.layout.support_simple_spinner_dropdown_item)
        spinnerGrupeAdapter.add("/")
        spinnerGrupeAdapter.addAll(listaNazivaGrupa)
        spinnerGrupeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGrupa.adapter = spinnerGrupeAdapter
    }

    private fun postaviPredmete(listaPredmeta: List<Predmet>?) {
        if(listaPredmeta == null) return

        val listaNazivaPredmeta: List<String> = listaPredmeta.map { p -> p.naziv }

        val spinnerPredmetiAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item)
        spinnerPredmetiAdapter.add("/")
        spinnerPredmetiAdapter.addAll(listaNazivaPredmeta)
        spinnerPredmetiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPredmet.adapter = spinnerPredmetiAdapter
    }


}