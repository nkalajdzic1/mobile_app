package ba.etf.rma21.projekat.view

import android.content.Context
import android.os.Build
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma21.projekat.MainActivity
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.*
import ba.etf.rma21.projekat.viewmodel.OdgovorViewModel
import ba.etf.rma21.projekat.viewmodel.PitanjeKvizViewModel
import ba.etf.rma21.projekat.viewmodel.PredmetIGrupaViewModel
import ba.etf.rma21.projekat.viewmodel.TakeKvizViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

class KvizListAdapter(
    private var kvizovi: List<Kviz>,
    private var mainActivity: MainActivity,
    private var odabraniKviz: (odabrKviz:Kviz) -> Unit) : RecyclerView.Adapter<KvizListAdapter.KvizViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KvizViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.activity_kviz, parent, false)
        return KvizViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: KvizViewHolder, position: Int) {
        holder.textNaslov.text = ""

        PredmetIGrupaViewModel().dajGrupeZaKviz(kvizovi[position].id, holder, onSuccess = ::onSuccess, onError = ::onError)

        val trajanje = kvizovi[position].trajanje.toString() + " min"

        TakeKvizViewModel().dajKvizoveZaStudenta(kvizovi[position], holder, onSuccess = ::daLiJeRadjen, onError = ::onErrorr)

        holder.imeKviza.text = kvizovi[position].naziv
        holder.trajanje.text = trajanje

        holder.itemView.setOnClickListener{
            odabraniKviz(kvizovi[position])
        }
    }

    override fun getItemCount(): Int { return kvizovi.size }

    fun update(lista: List<Kviz>) {
        kvizovi = lista
        notifyDataSetChanged()
    }

    inner class KvizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textNaslov: TextView = itemView.findViewById(R.id.textViewNaslov)
        val imgBoja: ImageView = itemView.findViewById(R.id.imageViewKrug)
        val imeKviza: TextView = itemView.findViewById(R.id.ImeKviza)
        val datumPocetka: TextView = itemView.findViewById(R.id.DatumPocetka)
        val trajanje: TextView = itemView.findViewById(R.id.Trajanje)
        val bodovi: TextView = itemView.findViewById(R.id.Bodovi)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatirajDatum(datum: LocalDate?) : String {
        if(datum == null) return ""
        var mjesec = ""
        if(datum.month.value < 10) mjesec += "0" + (datum.month.value).toString()
        else mjesec += datum.month + 1
        return datum.dayOfMonth.toString() + "." + mjesec + "." + datum.year
    }

    fun onSuccess(holder: KvizViewHolder, grupe: List<Grupa>) {

        if(grupe.isNotEmpty())
           PredmetIGrupaViewModel().dajPredmetPoId(grupe[0].PredmetId, holder, onSuccess = ::postaviPredmet, onError = ::onError)

        for(i in 1 until grupe.size) {

            var imaVec = false
            for(j in grupe.indices) {
                if(i != j && grupe[j].PredmetId == grupe[i].PredmetId) {
                    imaVec = true
                    break
                }
            }

            if (!imaVec)
                PredmetIGrupaViewModel().dajPredmetPoId(grupe[i].PredmetId, holder, onSuccess = ::postaviPredmet, onError = ::onError)
        }

    }

    fun postaviPredmet(holder: KvizListAdapter.KvizViewHolder, predmet: Predmet) {
        if(!holder.textNaslov.text.toString().contains(predmet.naziv))
           holder.textNaslov.text = holder.textNaslov.text.toString() + predmet.naziv + " "
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun daLiJeRadjen(kviz: Kviz, holder: KvizViewHolder, kvizTakens: List<KvizTaken>) {

        GlobalScope.launch(Dispatchers.Main) {
            val boja = FragmentKvizovi.dajStatus(kviz, kvizTakens)

            val datumKraja = LocalDate.parse(kviz.datumPocetka)

            if(boja == "plava") {
                holder.datumPocetka.text = formatirajDatum(LocalDate.parse(kvizTakens[0].datumRada))
                val bodovi = kvizTakens.find { kt -> kt.KvizId == kviz.id }?.osvojeniBodovi
                holder.bodovi.text = bodovi.toString().plus("%")
            } else {

                if(boja == "zelena")
                    holder.datumPocetka.text = formatirajDatum(if(kviz.datumKraj.isNullOrEmpty() || kviz.datumKraj.equals( "null")) LocalDate.parse(kviz.datumPocetka) else LocalDate.parse(kviz.datumKraj))
                else if(boja == "zuta")
                    holder.datumPocetka.text = formatirajDatum(LocalDate.parse(kviz.datumPocetka))
                else if(boja == "crvena")
                    holder.datumPocetka.text = formatirajDatum(if(kviz.datumKraj.isNullOrEmpty() || kviz.datumKraj.equals("null")) datumKraja else LocalDate.parse(kviz.datumKraj))

                holder.bodovi.text = ""

            }

            val imgContext: Context = holder.imgBoja.context
            holder.imgBoja.setImageResource(
                imgContext.resources.getIdentifier(
                    boja,
                    "drawable",
                    imgContext.packageName
                )
            )
        }

    }

    private fun onError() {
        val toast = Toast.makeText(mainActivity.kvizoviFragment.context, "Error", Toast.LENGTH_SHORT)
        toast.show()
    }

    private fun onErrorr() {
        val toast = Toast.makeText(mainActivity.kvizoviFragment.context, "drugi", Toast.LENGTH_SHORT)
        toast.show()
    }
}