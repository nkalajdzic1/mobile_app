package ba.etf.rma21.projekat

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.repositories.*
import ba.etf.rma21.projekat.view.FragmentKvizovi
import ba.etf.rma21.projekat.view.FragmentPredmeti
import ba.etf.rma21.projekat.viewmodel.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity() : AppCompatActivity() {

    lateinit var bottomNavigation: BottomNavigationView
    private val br: BroadcastReceiver = ConnectivityBroadcastReceiver()
    private val filter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
    private var izbor1: Int = 0
    private var izbor2: Int = 0
    private var izbor3: Int = 0
    var kvizViewModel: KvizViewModel = KvizViewModel()
    lateinit var dodavanjeGrupeFragment: FragmentPredmeti
    lateinit var kvizoviFragment: FragmentKvizovi
    lateinit var appDao: AppDatabase

    val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.kvizovi -> {
                openFragment(kvizoviFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.predmeti -> {
                openFragment(dodavanjeGrupeFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appDao = AppDatabase.getInstance(this.applicationContext)

        AccountRepository.setContext(this.applicationContext)
        KvizRepository.setContext(this.applicationContext)
        OdgovorRepository.setContext(this.applicationContext)
        PitanjeKvizRepository.setContext(this.applicationContext)
        PredmetIGrupaRepository.setContext(this.applicationContext)
        TakeKvizRepository.setContext(this.applicationContext)
        DBRepository.setContext(this.applicationContext)

        val _intent: Intent = this.intent

        GlobalScope.launch(Dispatchers.Main) {
            DBRepository.updateNow()
            _intent.getStringExtra("payload")?.let { AccountRepository.postaviHash(it) }
        }

            dodavanjeGrupeFragment = FragmentPredmeti.newInstance(izbor1, izbor2, izbor3)
            dodavanjeGrupeFragment.mainActivity = this
            kvizoviFragment = FragmentKvizovi.newInstance()
            kvizoviFragment.mainActivity = this
            bottomNavigation = findViewById(R.id.bottomNav)
            bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
            bottomNavigation.selectedItemId = R.id.kvizovi

            openFragment(kvizoviFragment)


    }

    fun otvoriUFramePitanju(fragment: Fragment) {
        val transaction =  supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun zamijeniBottomNavigation() {
        bottomNavigation.menu.clear()
        bottomNavigation.inflateMenu(R.menu.navigationkviza)
    }

    fun vratiBottomNavigation() {
        bottomNavigation.menu.clear()
        bottomNavigation.inflateMenu(R.menu.navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigation.selectedItemId = R.id.kvizovi
    }


}

