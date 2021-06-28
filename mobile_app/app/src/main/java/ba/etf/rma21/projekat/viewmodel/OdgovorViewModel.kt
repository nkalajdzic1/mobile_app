package ba.etf.rma21.projekat.viewmodel

import ba.etf.rma21.projekat.data.models.Odgovor
import ba.etf.rma21.projekat.data.repositories.OdgovorRepository

class OdgovorViewModel {

    suspend fun getOdgovori(idKviza: Int): List<Odgovor>? {
        return OdgovorRepository.getOdgovoriKviz(idKviza)
    }

    suspend fun addOdgovor(idKvizTaken:Int,idPitanje:Int,odgovor:Int): Int {
        return OdgovorRepository.postaviOdgovor(idKvizTaken, idPitanje, odgovor)
    }

    suspend fun getAllForKvizTaken(KvizTakenId: Int): List<Odgovor> {
        return OdgovorRepository.getAllForKvizTaken(KvizTakenId)
    }

    suspend fun postaviOdgovor(KvizTakenId: Int, PitanjeId: Int, odgovoreno: Int): Int {
        return OdgovorRepository.postaviOdgovorKviz(KvizTakenId, PitanjeId, odgovoreno)
    }

    suspend fun deleteAll() {
        OdgovorRepository.deleteAll()
    }

    suspend fun predajOdgovore(idKviz: Int) {
        OdgovorRepository.predajOdgovore(idKviz)
    }
}