package ba.etf.rma21.projekat.viewmodel

import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.repositories.KorisnikRepository

class KorisnikViewModel {

    private var korisnikRepositoryKvizovi = listOf<Kviz>()
    private var korisnikRepositoryGrupe = listOf<Grupa>()
    private var korisnikRepositoryPredmeti = listOf<Predmet>()

    fun dajPredmete() : List<Predmet> { return korisnikRepositoryPredmeti }

    fun dajGrupe() : List<Grupa> { return korisnikRepositoryGrupe }

    fun dajKvizove() : List<Kviz> { return korisnikRepositoryKvizovi }

    fun dodajPredmetIGrupu(predmet: Predmet?, grupa: Grupa?) { //KorisnikRepository.dodajPredmetIGrupu(predmet, grupa)
         }

    fun postaviKvizove(listaK: List<Kviz>) {
       // korisnikRepositoryKvizovi.clear()
        //korisnikRepositoryKvizovi.addAll(listaK)
    }

}