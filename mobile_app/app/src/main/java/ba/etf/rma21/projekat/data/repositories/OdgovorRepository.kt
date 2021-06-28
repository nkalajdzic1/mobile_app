package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.models.Odgovor
import ba.etf.rma21.projekat.data.models.OdgovorDTO
import ba.etf.rma21.projekat.objects.ApiConfig
import ba.etf.rma21.projekat.viewmodel.OdgovorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OdgovorRepository {

    companion object {

        private lateinit var context: Context

        fun setContext(_context: Context){
            context=_context
        }

        suspend fun getOdgovoriKviz(idKviza:Int): List<Odgovor>? {
            return withContext(Dispatchers.IO) {
                var response = ApiConfig.retrofit.GetOdgovori(idKviza)
                return@withContext response.body()
            }
        }

        suspend fun postaviOdgovor(idKvizTaken:Int,idPitanje:Int,odgovor:Int):Int {
            return withContext(Dispatchers.IO) {
                val kvizTakens = TakeKvizRepository.getPocetiKvizovi()
                val kvizTaken = kvizTakens?.find { kt -> kt.id == idKvizTaken }
                val pitanja = kvizTaken?.let { PitanjeKvizRepository.getPitanjaApi(it.KvizId) }
                var bodovi: Int = kvizTaken?.osvojeniBodovi!!
                if(pitanja?.find { p -> p.id == idPitanje }?.tacan == odgovor) bodovi += 100 / pitanja.size
                val odgovorDTO = OdgovorDTO(odgovor, idPitanje, bodovi)
                val response = ApiConfig.retrofit.AddAnswer(idKvizTaken,odgovorDTO)
                val odg = response.body()
                if(odg != null) {
                    val kt = TakeKvizRepository.getPocetiKvizovi()?.find { kt -> kt.id == odg.KvizTakenId }
                    if(kt != null) return@withContext kt.osvojeniBodovi
                    return@withContext -1
                }
                return@withContext -1
            }
        }

        suspend fun getAllForKvizTaken(KvizTakenId: Int): List<Odgovor> {
            return AppDatabase.getInstance(context).OdgovorDao().getAllForKvizTaken(KvizTakenId)
        }

        suspend fun postaviOdgovorKviz(KvizTakenId: Int, PitanjeId: Int, odgovoreno: Int): Int {
            val kvizTakens = TakeKvizRepository.getPocetiKvizovi()
            val kvizTaken = kvizTakens?.find { kt -> kt.id == KvizTakenId }

            val sviOdgovori = AppDatabase.getInstance(context).OdgovorDao().getAllForKvizTaken(KvizTakenId)
            val svaPitanja = kvizTaken?.let { PitanjeKvizRepository.getPitanjaApi(it.KvizId) }

            var vecPostoji = false

            for(o in sviOdgovori)
                if(o.KvizTakenId == KvizTakenId && o.PitanjeId == PitanjeId) {
                    vecPostoji = true
                    break
                }

            if(!vecPostoji)
               AppDatabase.getInstance(context).OdgovorDao().postaviOdgovor(odgovoreno, KvizTakenId, PitanjeId)

            val sviNoviOdgovori = AppDatabase.getInstance(context).OdgovorDao().getAllForKvizTaken(KvizTakenId)

            var brTacnih: Int = 0

            sviNoviOdgovori.forEach { o ->
                val p = svaPitanja?.find { p -> p.id == o.PitanjeId }
                if(p?.tacan == o.odgovoreno) brTacnih += 1
            }

            if(svaPitanja == null) return 0

            return (brTacnih.toDouble()/svaPitanja.size.toDouble() * 100).toInt()
        }

        suspend fun deleteAll() {
            AppDatabase.getInstance(context).OdgovorDao().deleteAll()
        }

        suspend fun predajOdgovore(idKviz: Int) {
            val odgovori = getAllForKvizTaken(idKviz)
            val kt = TakeKvizRepository.getPocetiKvizovi()?.find { k -> k.id == idKviz }
            val listaPitanja = kt?.let { PitanjeKvizRepository.getAllForKviz(it.KvizId) }

            if (listaPitanja != null) {
                for(i in listaPitanja.indices) {
                    val odg = odgovori.find { o -> o.PitanjeId == listaPitanja[i].PitanjeId }
                    if(odg == null)
                        postaviOdgovorKviz(idKviz, listaPitanja[i].PitanjeId, -1)
                }
            }

            val listaZaDodati = nadjiOdgovoreKojiFale(kt)
            dodajOdgovore(listaZaDodati)

        }

        suspend fun nadjiOdgovoreKojiFale(kvizTaken: KvizTaken?): List<Odgovor>? {
            val odgovoriSaApi = kvizTaken?.let { OdgovorViewModel().getOdgovori(it.id) }
            val odgovoriZaDodati = kvizTaken?.let { OdgovorViewModel().getAllForKvizTaken(it.id) }
            return odgovoriZaDodati?.filter { o -> odgovoriSaApi?.find { osa -> osa.PitanjeId == o.PitanjeId && osa.KvizTakenId == o.KvizTakenId } == null }
        }

        suspend fun dodajOdgovore(odgovori: List<Odgovor>?) {
            odgovori?.forEach { o ->
                postaviOdgovor(o.KvizTakenId, o.PitanjeId, o.odgovoreno)
            }
        }

    }

}