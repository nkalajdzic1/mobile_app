package ba.etf.rma21.projekat.view

import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import android.widget.FrameLayout
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
import ba.etf.rma21.projekat.viewmodel.KvizViewModel
import ba.etf.rma21.projekat.viewmodel.OdgovorViewModel
import ba.etf.rma21.projekat.viewmodel.PitanjeKvizViewModel
import ba.etf.rma21.projekat.viewmodel.TakeKvizViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class FragmentPokusaj(private val listaPitanja: List<Pitanje>,
                      var listaOdgovora: List<Odgovor>?,
                      private val kvizTaken: KvizTaken?,
                      private var kvizFragmenta: Kviz,
                      private val status: String): Fragment() {
    lateinit var navigationView: NavigationView
    private lateinit var frameLayout: FrameLayout
    var fragmentKvizovi: FragmentKvizovi = FragmentKvizovi()
    var redniBroj: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    private val bottomNavigationListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.zaustaviKviz -> {
                GlobalScope.launch(Dispatchers.Main) {
                    provjeriOdgovore()
                    (activity as MainActivity).vratiBottomNavigation()
                    (activity as MainActivity).openFragment(fragmentKvizovi)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.predajKviz -> {
                (activity as MainActivity).vratiBottomNavigation()
                GlobalScope.launch(Dispatchers.Main) {
                    predajSveOdgovore()
                    val fragmentPoruka = FragmentPoruka.newInstance(kvizFragmenta, dajBodove())
                    (activity as MainActivity).otvoriUFramePitanju(fragmentPoruka)
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun provjeriOdgovore() {
        val odg = kvizTaken?.let { OdgovorViewModel().getAllForKvizTaken(it.id) }
        val pitanja = kvizTaken?.let { PitanjeKvizViewModel().getPitanja(it.KvizId) }
        if(odg?.size == pitanja?.size) predajSveOdgovore()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun predajSveOdgovore() {
        if (kvizTaken != null) {
            OdgovorViewModel().predajOdgovore(kvizTaken.id)
        }

        val currentDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        kvizTaken?.let { KvizViewModel().updateKvizPredan(it.KvizId) }
        AccountRepository.updateLastUpdate(currentDate)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun dajBodove(): Int {
        var bodovi = 0

        val tempKt = TakeKvizViewModel().pomDajKvizoveZaStudenta()?.find{ tk -> tk.id == kvizTaken?.id }
        if(tempKt != null)
           bodovi += tempKt.osvojeniBodovi

        return bodovi
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.pokusaj_fragment, container, false)

        navigationView = view.findViewById(R.id.navigacijaPitanja)

        frameLayout = view.findViewById(R.id.framePitanje)
        (activity as MainActivity).bottomNavigation.setOnNavigationItemSelectedListener(bottomNavigationListener)

        var ii = 1
        listaPitanja.forEach{ lp ->
            navigationView.menu.add(0, ii-1, 0, ii.toString())
            ii++
        }

        println(kvizTaken?.id)

        if(status == "crvena" || status == "plava") navigationView.menu.add("Rezultat")

        navigationView.setNavigationItemSelectedListener { item: MenuItem ->
            GlobalScope.launch(Dispatchers.Main) {

                redniBroj = item.title.toString()
                if(redniBroj == "Rezultat" ) {
                   (activity as MainActivity).vratiBottomNavigation()
                     val fragmentPoruka =  FragmentPoruka.newInstance(kvizFragmenta,
                         kvizTaken?.osvojeniBodovi
                     )
                    (activity as MainActivity).openFragment(fragmentPoruka)
                } else {
                    val pitanje = listaPitanja.get(Integer.parseInt(redniBroj) - 1)
                    val lodg = TakeKvizViewModel().pomDajKvizoveZaStudenta()?.find{ tk -> tk.KvizId == kvizFragmenta.id }
                            ?.let { OdgovorViewModel().getAllForKvizTaken(it.id) }
                    val odg = lodg?.find { o -> o.PitanjeId == pitanje.PitanjeId }
                    val fragmentPitanje = FragmentPitanje(pitanje, odg, this@FragmentPokusaj, Integer.parseInt(redniBroj) - 1)
                    fragmentPitanje.status = this@FragmentPokusaj.status
                    fragmentPitanje.kvizTaken = this@FragmentPokusaj.kvizTaken

                    val transaction = fragmentManager!!.beginTransaction()
                    transaction.replace(R.id.framePitanje, fragmentPitanje)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }

            }

            true
        }


        if(listaOdgovora != null) {
            for(i in listaPitanja.indices) {

                val odgovor = listaOdgovora!!.find { o -> o.PitanjeId == listaPitanja[i].PitanjeId }

                if(odgovor != null) {
                    if(odgovor.odgovoreno  == listaPitanja[i].tacan)
                        postaviBojuMenuItema(navigationView.menu.get(i), R.color.tacno)
                    else
                        postaviBojuMenuItema(navigationView.menu.get(i), R.color.netacno)
                }

            }
        }


        return view
    }

    fun postaviBojuMenuItema(menuItem: MenuItem, @ColorRes color: Int) {
        val spanString = SpannableString(menuItem.title.toString())
        spanString.setSpan(ForegroundColorSpan(resources.getColor(color)), 0, spanString.length, 0)
        menuItem.title = spanString
    }

    override fun onResume() {
        super.onResume()
        this.requireView().isFocusableInTouchMode = true
        this.requireView().requestFocus()
        this.requireView().setOnKeyListener { _, tipka, _ ->
            tipka == KeyEvent.KEYCODE_BACK
        }
    }
}