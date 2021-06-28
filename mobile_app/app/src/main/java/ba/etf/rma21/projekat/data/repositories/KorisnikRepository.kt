package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Predmet

class KorisnikRepository() {

    /*companion object {

    private var grupe: ArrayList<Grupa>
    private var predmeti: ArrayList<Predmet>
    private var kvizovi: ArrayList<Kviz>

    init {
        this.grupe = grupe().filter { grupa -> grupa.naziv == "OOI1" || grupa.naziv == "NRPR1" || grupa.naziv == "TP2" } as ArrayList<Grupa>
        this.predmeti = predmeti().filter { predmet -> predmet.naziv == "OOI" || predmet.naziv == "NRPR" || predmet.naziv == "TP" } as ArrayList<Predmet>
        this.kvizovi = kvizovi().filter { kviz -> kviz.naziv == "Kviz 7" || kviz.naziv == "Kviz 8" || kviz.naziv == "Kviz 9" } as ArrayList<Kviz>
    }

    fun dodajPredmetIGrupu(predmet: Predmet?, grupa: Grupa?) {
        if(predmet == null || grupa == null) return
        grupe.add(grupa)
        predmeti.add(predmet)
        val noviK = kvizovi().filter { k -> k.nazivPredmeta == predmet.naziv && k.nazivGrupe == grupa.naziv }
        this.kvizovi.addAll(noviK)
    }

        fun dajGrupe() : ArrayList<Grupa> { return this.grupe }

        fun dajPredmete() : ArrayList<Predmet> { return this.predmeti }

        fun dajKvizove() : ArrayList<Kviz> { return this.kvizovi }

    }*/
}