package ba.etf.rma21.projekat.viewmodel

import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.repositories.GrupaRepository

class GrupaViewModel {

    fun dajGrupePoPredmetu(nazivPredmeta: String): List<Grupa> {
        return GrupaRepository.getGroupsByPredmet(nazivPredmeta)
    }

    fun dajSveGrupe() : List<Grupa> { return GrupaRepository.getAll() }



}