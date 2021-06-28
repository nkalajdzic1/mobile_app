package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import ba.etf.rma21.projekat.MainActivity
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.*
import ba.etf.rma21.projekat.objects.ApiConfig
import ba.etf.rma21.projekat.viewmodel.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DBRepository {

    companion object{

        private lateinit var context:Context

        fun setContext(_context: Context){
            context=_context
        }

        @RequiresApi(Build.VERSION_CODES.O)
        suspend fun updateNow(): Boolean {
            return withContext(Dispatchers.IO) {

                val accountRow = AppDatabase.getInstance(context).AccountDao().getAccountRow()

                if(accountRow != null) {
                    val response = ApiConfig.retrofit.LastUpdate(AccountRepository.getHash(), accountRow.lastUpdate)

                    if(response.body() != null) {
                       if(response.body()!!.changed) {
                          val currentDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                          AccountRepository.updateLastUpdate(currentDate)
                          updateBase()
                       }
                       return@withContext response.body()!!.changed
                    }

                    return@withContext false

                } else {
                    val currentDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                    AccountRepository.insertAccountRow(AccountRepository.getHash(), currentDate)
                    updateBase()

                }

                return@withContext true
            }
        }

        private fun updateBase() {
            azurirajKvizove()
            azurirajPredmete()
            azurirajGrupe()
            azurirajPitanja()
            azurirajOdgovore()
        }

        private fun azurirajKvizove() {
            GlobalScope.launch(Dispatchers.Main) {
                val mojiKvizoviApi = KvizViewModel().getMyKvizesApi()
                val kvizTakens = TakeKvizViewModel().pomDajKvizoveZaStudenta()

                AppDatabase.getInstance(context).KvizDao().deleteKvizes()

                mojiKvizoviApi.forEach{
                    var predan = 0
                    val radjen = kvizTakens?.find { kt -> kt.KvizId == it.id }
                    if(radjen != null) {
                        val odgovori: List<Odgovor>? = OdgovorViewModel().getOdgovori(radjen.id)
                        val pitanja: List<PitanjeAPI>? = PitanjeKvizViewModel().getPitanjaApi(it.id)
                        if (odgovori != null && pitanja != null)
                            if(odgovori.size == pitanja.size) predan = 1
                    }
                    AppDatabase.getInstance(context).KvizDao().insertKviz(it.id, it.naziv, it.datumPocetka, it.datumKraj.toString(), it.trajanje, predan)
                }
            }
        }

        private fun azurirajPredmete() {
            GlobalScope.launch(Dispatchers.Main) {
                val upisaneGrupe = PredmetIGrupaViewModel().pomDajGrupeZaStudenta()
                val predmeti = PredmetIGrupaViewModel().dajPredmete()?.filter { p -> upisaneGrupe?.find { g -> g.PredmetId == p.id } != null }

                AppDatabase.getInstance(context).PredmetDao().deleteAll()
                predmeti?.forEach { AppDatabase.getInstance(context).PredmetDao().insertPredmet(it.id, it.naziv, it.godina) }
            }
        }

        private fun azurirajGrupe() {
            GlobalScope.launch(Dispatchers.Main) {
                val upisaneGrupe = PredmetIGrupaViewModel().pomDajGrupeZaStudenta()

                AppDatabase.getInstance(context).GrupaDao().deleteAll()
                upisaneGrupe?.forEach { AppDatabase.getInstance(context).GrupaDao().insertGrupa(it.id, it.naziv, it.PredmetId) }
            }
        }

        private fun azurirajPitanja() {
            GlobalScope.launch(Dispatchers.Main) {
                val mojiKvizovi = KvizViewModel().getMyKvizesApi()

                var lista : MutableList<Pitanje>? = mutableListOf()

                mojiKvizovi.forEach { k ->
                    val pitanja: List<Pitanje>? = PitanjeKvizViewModel().getPitanjaApi(k.id)
                        ?.map { p -> Pitanje(1, p.id, p.naziv, p.tekstPitanja, p.opcije.joinToString(","), p.tacan, k.id) }?.distinctBy { it }

                    if(pitanja != null && lista != null) lista.addAll(pitanja)
                }

                lista?.distinctBy { Pair(it.KvizId, it.PitanjeId) }

                AppDatabase.getInstance(context).PitanjeDao().deleteAll()
                lista?.forEach { p ->
                    p.KvizId?.let {
                        AppDatabase.getInstance(context).PitanjeDao().insertPitanje(p.PitanjeId, p.naziv, p.tekstPitanja, p.opcije, p.tacan, it)
                    }
                }


            }
        }

        private fun azurirajOdgovore() {
            GlobalScope.launch(Dispatchers.Main) {
                val kvizTakens = TakeKvizViewModel().pomDajKvizoveZaStudenta()

                AppDatabase.getInstance(context).OdgovorDao().deleteAll()
                kvizTakens?.forEach { kt ->
                    val odgovori = OdgovorViewModel().getOdgovori(kt.id)
                    odgovori?.forEach { o ->
                        AppDatabase.getInstance(context).OdgovorDao().postaviOdgovor(o.KvizTakenId, o.PitanjeId, o.odgovoreno)
                    }
                }
            }

        }

    }

}
