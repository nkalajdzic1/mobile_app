package ba.etf.rma21.projekat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Predmet

class FragmentPoruka(val objekt1: Any, val objekt2: Any?): Fragment() {
    private lateinit var poruka: TextView

    companion object {
        fun newInstance(o1: Any, o2: Any?): FragmentPoruka = FragmentPoruka(o1, o2)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.poruka_fragment, container, false)

        poruka = view.findViewById(R.id.tvPoruka)

        if(objekt1 is Grupa && objekt2 is Predmet)
          poruka.text = "Uspješno ste upisani u grupu ${objekt1.naziv} predmeta ${objekt2.naziv}!"
        else if(objekt1 is Kviz && objekt2 is Int)
           poruka.text = "Završili ste kviz ${objekt1.naziv} sa tačnosti ${objekt2}%"

        return view
    }

}

