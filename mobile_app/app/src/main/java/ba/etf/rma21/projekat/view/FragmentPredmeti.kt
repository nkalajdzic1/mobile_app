package ba.etf.rma21.projekat.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.MainActivity
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.repositories.AccountRepository
import ba.etf.rma21.projekat.data.repositories.DBRepository
import ba.etf.rma21.projekat.viewmodel.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FragmentPredmeti(var i1: Int, var i2: Int, var i3: Int): Fragment() {

    companion object {
        fun newInstance(iz1: Int, iz2: Int, iz3: Int): FragmentPredmeti = FragmentPredmeti(iz1, iz2, iz3)
    }

    private lateinit var spinnerGodina: Spinner
    private lateinit var spinnerPredmet: Spinner
    private lateinit var spinnerGrupa: Spinner
    private lateinit var dugmeUpisi: Button
    var mainActivity: MainActivity = MainActivity()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view =  inflater.inflate(R.layout.upis_predmeta_fragment, container, false)
        spinnerGodina = view.findViewById(R.id.odabirGodina)
        spinnerPredmet = view.findViewById(R.id.odabirPredmet)
        spinnerGrupa = view.findViewById(R.id.odabirGrupa)
        dugmeUpisi = view.findViewById(R.id.dodajPredmetDugme)

            ArrayAdapter.createFromResource(activity!!, R.array.spinnerGodine, android.R.layout.simple_spinner_item)
                .also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerGodina.adapter = adapter
                }

        initSpinnerGodina(i1, i2, i3)

        dugmeUpisi.setOnClickListener {
            if(spinnerGodina.selectedItem.toString() != "/" && spinnerPredmet.selectedItem.toString() != "/"
                && spinnerGrupa.selectedItem.toString() != "/") {

                    GlobalScope.launch(Dispatchers.Main) {
                        val odabraniP = PredmetIGrupaViewModel().dajPredmete()?.find { p -> p.naziv == spinnerPredmet.selectedItem }
                        val odabraniG = PredmetIGrupaViewModel().pomDajGrupe()?.find { p -> p.naziv == spinnerGrupa.selectedItem && p.PredmetId == odabraniP?.id }

                        if(odabraniG != null && odabraniP != null) {
                            val mess = PredmetIGrupaViewModel().upisiStudentaUGrupu(odabraniG.id)?.message
                            if (mess?.isNotEmpty() == true) {
                                i1 = 0
                                i2 = 0
                                i3 = 0
                                spinnerGodina.setSelection(0)
                                spinnerPredmet.setSelection(0)
                                spinnerGrupa.setSelection(0)
                                val fragmentPoruka = FragmentPoruka.newInstance(odabraniG, odabraniP)
                                val transaction = fragmentManager!!.beginTransaction()
                                transaction.replace(R.id.container, fragmentPoruka)
                                transaction.addToBackStack(null)
                                transaction.commit()
                            }
                            DBRepository.updateNow()
                        }

                }
            }
        }

        return view;
    }

    override fun onResume() {
        super.onResume()
        initSpinnerGodina(i1,i2,i3)
    }

    override fun onPause() {
        super.onPause()
        i1 = spinnerGodina.selectedItemPosition
        i2 = spinnerPredmet.selectedItemPosition
        i3 = spinnerGrupa.selectedItemPosition

    }

    private fun initSpinnerGodina(ii1: Int, ii2: Int, ii3: Int) {

        spinnerGodina.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                GlobalScope.launch(Dispatchers.Main) {
                    var predmeti = PredmetIGrupaViewModel().dajPredmete()?.filter { p -> p.godina == position }
                    val grupe = PredmetIGrupaViewModel().getAllMyGrupe()

                    predmeti = predmeti?.filter { p -> grupe.find { g -> g.PredmetId == p.id } == null }

                    postaviPredmete(predmeti)

                    if(position == 0 && spinnerPredmet.selectedItem.toString() != "/"
                        && spinnerGrupa.selectedItem.toString() != "/") {
                        dugmeUpisi.isEnabled = true
                        dugmeUpisi.isClickable = true
                    } else {
                        dugmeUpisi.isEnabled = false
                        dugmeUpisi.isClickable = false
                    }

                    if(i1 != position) {
                        i1 = position
                        innitSpinnnerPredmet(0, 0)
                    }
                    else innitSpinnnerPredmet(ii2, ii3)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun innitSpinnnerPredmet(iii2: Int, iii3: Int) {

        spinnerPredmet.setSelection(iii2)

        spinnerPredmet.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                GlobalScope.launch(Dispatchers.Main) {

                    val predmet = PredmetIGrupaViewModel().dajPredmete()?.find { p -> p.naziv.contains(spinnerPredmet.selectedItem.toString()) }
                    val grupe  = predmet?.let { PredmetIGrupaViewModel().pomDajGrupeZaPredmet(it.id) }

                    postaviGrupe(grupe)

                    if(spinnerGodina.selectedItem.toString() != "/" && spinnerPredmet.selectedItem.toString() != "/"
                        && spinnerGrupa.selectedItem.toString() != "/") {
                        dugmeUpisi.isEnabled = true
                        dugmeUpisi.isClickable = true
                    } else {
                        dugmeUpisi.isEnabled = false
                        dugmeUpisi.isClickable = false
                    }
                    if(i2 != position) {
                        i2 = position
                        innitSpinnerGrupa(0)
                    }
                    else innitSpinnerGrupa(iii3)

                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun innitSpinnerGrupa(iiii3: Int) {

        spinnerGrupa.setSelection(iiii3)

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
    }

    private fun postaviGrupe(listaGrupa: List<Grupa>?) {
        if(listaGrupa == null) {
            val spinnerGrupeAdapter: ArrayAdapter<String> = ArrayAdapter(activity!!,
                R.layout.support_simple_spinner_dropdown_item)
            spinnerGrupeAdapter.add("/")
            spinnerGrupeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGrupa.adapter = spinnerGrupeAdapter
            return
        }
        val listaNazivaGrupa: List<String> = listaGrupa.map { p -> p.naziv }
        val spinnerGrupeAdapter: ArrayAdapter<String> = ArrayAdapter(activity!!, R.layout.support_simple_spinner_dropdown_item)
        spinnerGrupeAdapter.add("/")
        spinnerGrupeAdapter.addAll(listaNazivaGrupa)
        spinnerGrupeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGrupa.adapter = spinnerGrupeAdapter
    }

    private fun postaviPredmete(listaPredmeta: List<Predmet>?) {
        if(listaPredmeta == null) {
            val spinnerPredmetiAdapter: ArrayAdapter<String> = ArrayAdapter(activity!!, R.layout.support_simple_spinner_dropdown_item)
            spinnerPredmetiAdapter.add("/")
            spinnerPredmetiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerPredmet.adapter = spinnerPredmetiAdapter
            return
        }
        val listaNazivaPredmeta: List<String> = listaPredmeta.map { p -> p.naziv }
        val spinnerPredmetiAdapter: ArrayAdapter<String> = ArrayAdapter(activity!!, R.layout.support_simple_spinner_dropdown_item)
        spinnerPredmetiAdapter.add("/")
        spinnerPredmetiAdapter.addAll(listaNazivaPredmeta)
        spinnerPredmetiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPredmet.adapter = spinnerPredmetiAdapter
    }

}