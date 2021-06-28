package ba.etf.rma21.projekat.view

import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.MainActivity
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.models.Odgovor
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.AccountRepository
import ba.etf.rma21.projekat.viewmodel.OdgovorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class FragmentPitanje(private val pitanje: Pitanje, private val odgovor: Odgovor?, var frag: FragmentPokusaj, var redniBroj: Int): Fragment() {
    private lateinit var postavkaPitanja: TextView
    lateinit var listaOdgovora: ListView
    var status: String = ""
    var kvizTaken: KvizTaken? = KvizTaken(0, 0, "", "", 0)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.pitanje_fragment, container, false)

        postavkaPitanja = view.findViewById(R.id.tekstPitanja)
        listaOdgovora = view.findViewById(R.id.odgovoriLista)

        postavkaPitanja.text = pitanje.tekstPitanja
        listaOdgovora.adapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, pitanje.opcije.split(","))

        if(odgovor != null) postaviListuNeklikabilnu()

        listaOdgovora.addOnLayoutChangeListener(object: View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                if(odgovor != null) {
                   if(odgovor.odgovoreno == pitanje.tacan) {
                      (listaOdgovora.get(odgovor.odgovoreno) as TextView).setTextColor(resources.getColor(R.color.tacno))
                   } else {
                       if(odgovor.odgovoreno == -1) {
                           (listaOdgovora.get(pitanje.tacan) as TextView).setTextColor(resources.getColor(R.color.tacno))
                           listaOdgovora.get(pitanje.tacan).setBackgroundColor(resources.getColor(R.color.netacno))
                       } else {
                           (listaOdgovora.get(pitanje.tacan) as TextView).setTextColor(resources.getColor(R.color.tacno))
                           listaOdgovora.get(odgovor.odgovoreno).setBackgroundColor(resources.getColor(R.color.netacno))
                       }
                   }
                   postaviListuNeklikabilnu()
                }

                if(status == "crvena") postaviListuNeklikabilnu()
            }
        })

        listaOdgovora.setOnItemClickListener { parent, v, position, id ->

            GlobalScope.launch(Dispatchers.Main) {

                val currentDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                kvizTaken?.let { OdgovorViewModel().postaviOdgovor(it.id, pitanje.PitanjeId, position) }
                AccountRepository.updateLastUpdate(currentDate)

                if((listaOdgovora.get(position) as TextView).text == pitanje.opcije.split(",")[pitanje.tacan]) {

                   (listaOdgovora.get(position) as TextView).setTextColor(resources.getColor(R.color.tacno))
                    frag.postaviBojuMenuItema(frag.navigationView.menu.get(Integer.parseInt(frag.redniBroj) - 1), R.color.tacno)

                } else {

                    (listaOdgovora.get(pitanje.tacan) as TextView).setTextColor(resources.getColor(R.color.tacno))
                    listaOdgovora.get(position).setBackgroundColor(resources.getColor(R.color.netacno))
                    frag.postaviBojuMenuItema(frag.navigationView.menu.get(Integer.parseInt(frag.redniBroj) - 1), R.color.netacno)

                }

                postaviListuNeklikabilnu()
            }


        }

        return view
    }

    fun postaviListuNeklikabilnu() {
        listaOdgovora.onItemClickListener = null
    }
}