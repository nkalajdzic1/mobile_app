package ba.etf.rma21.projekat.view

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma21.projekat.MainActivity
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.dao.KvizDAO
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.models.Odgovor
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.viewmodel.KvizViewModel
import ba.etf.rma21.projekat.viewmodel.OdgovorViewModel
import ba.etf.rma21.projekat.viewmodel.PitanjeKvizViewModel
import ba.etf.rma21.projekat.viewmodel.TakeKvizViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

class FragmentKvizovi : Fragment() {
    private lateinit var spinner: Spinner
    lateinit var kvizovi: RecyclerView
    lateinit var listAdapter: KvizListAdapter
    var mainActivity: MainActivity = MainActivity()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view =  inflater.inflate(R.layout.pregled_kvizova_fragment, container, false)

        spinner = view.findViewById(R.id.filterKvizova)
        kvizovi = view.findViewById(R.id.listaKvizova)

        ArrayAdapter.createFromResource(
            activity!!,
            R.array.spinnerVrijednosti,
            android.R.layout.simple_spinner_item
        ).also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        kvizovi.layoutManager = GridLayoutManager(activity!!, 2)
        listAdapter = KvizListAdapter(listOf(), mainActivity) { kviz -> otvoriKviz(kviz)}
        kvizovi.adapter = listAdapter
        GlobalScope.launch(Dispatchers.Main) {
            context?.let { AppDatabase.getInstance(it).KvizDao().getMyKvizes() }?.let {
                listAdapter.update(
                    it
                )
            }
        }

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(spinner.selectedItem == "Svi kvizovi")
                    mainActivity.kvizViewModel.dajSve(onSuccess = ::onSuccess, onError = ::onError)
                else if(spinner.selectedItem == "Urađeni kvizovi")
                    mainActivity.kvizViewModel.dajZavrsene(onSuccess = ::onSuccess)
                 else if(spinner.selectedItem == "Svi moji kvizovi")
                    mainActivity.kvizViewModel.dajMojeKvizove(onSuccess = ::onSuccess)
                 else if(spinner.selectedItem == "Budući kvizovi")
                    mainActivity.kvizViewModel.dajBuduce(onSuccess = ::onSuccess)
                 else if(spinner.selectedItem == "Prošli kvizovi")
                    mainActivity.kvizViewModel.dajNeuradjene(onSuccess = ::onSuccess)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinner.setSelection(0)

        return view
    }

    fun onSuccess(kvizovi:List<Kviz>) {
        listAdapter.update(kvizovi)
    }

    fun onError() {
        val toast = Toast.makeText(context, "Ne postoje nikakvi kvizovi", Toast.LENGTH_SHORT)
        toast.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun otvoriKviz(kviz: Kviz) {
        if(LocalDate.parse(kviz.datumPocetka) > LocalDate.now()) return
        GlobalScope.launch(Dispatchers.Main) {
            if(KvizViewModel().getMyKvizes().contains(kviz)) {
                (activity as MainActivity).zamijeniBottomNavigation()
                val pitanja = PitanjeKvizViewModel().getPitanja(kviz.id)
                val kvizTakens = TakeKvizViewModel().pomDajKvizoveZaStudenta()
                var kvizTaken = kvizTakens?.find { kt -> kt.KvizId == kviz.id }
                val odgovori = kvizTaken?.let { OdgovorViewModel().getAllForKvizTaken(it.id) }
                val status = kvizTakens?.let { dajStatus(kviz, it) }
                if(kvizTaken == null && status == "zelena") {
                    TakeKvizViewModel().pomPokusajKviz(kviz.id)
                    kvizTaken = TakeKvizViewModel().pomDajKvizoveZaStudenta()?.find { kt -> kt.KvizId == kviz.id }
                }
                val fragmentPokusaj = status?.let { FragmentPokusaj(pitanja, odgovori, kvizTaken, kviz, it) }
                if (fragmentPokusaj != null)
                   (activity as MainActivity).openFragment(fragmentPokusaj)
            }
        }
    }


    companion object {
        fun newInstance(): FragmentKvizovi = FragmentKvizovi()

        @RequiresApi(Build.VERSION_CODES.O)
        suspend fun dajStatus(kviz: Kviz, kvizTakens: List<KvizTaken>): String {

            val radjen = kvizTakens.find { kt -> kt.KvizId == kviz.id }

            if(radjen != null) {

                val odgovori: List<Odgovor>? = OdgovorViewModel().getOdgovori(radjen.id)
                val pitanja: List<Pitanje>? = PitanjeKvizViewModel().getPitanja(kviz.id)
                if (odgovori != null && pitanja != null)
                    if(odgovori.size == pitanja.size) return "plava"

                return "zelena"

            }
            else {
                if(kviz.datumKraj.isNullOrEmpty() || kviz.datumKraj.equals("null"))
                    return "zelena"
                else {
                    if (LocalDate.parse(kviz.datumPocetka) <= LocalDate.now() && LocalDate.parse(kviz.datumKraj) > LocalDate.now())
                        return "zelena"
                    else if (LocalDate.parse(kviz.datumPocetka) > LocalDate.now())
                        return "zuta"
                    else if (LocalDate.parse(kviz.datumKraj) <= LocalDate.now())
                        return "crvena"
                }

            }

            return ""

        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        kvizovi.layoutManager = GridLayoutManager(activity!!, 2)
        listAdapter = KvizListAdapter(listOf(), mainActivity) { kviz -> otvoriKviz(kviz) }
        kvizovi.adapter = listAdapter
        mainActivity.kvizViewModel.dajMojeKvizove(onSuccess = ::onSuccess)
        spinner.setSelection(0)
        this.requireView().isFocusableInTouchMode = true
        this.requireView().requestFocus()
        this.requireView().setOnKeyListener{ _, tipka, _ ->
            tipka == KeyEvent.KEYCODE_BACK
        }
    }

}