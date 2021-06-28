package ba.etf.rma21.projekat

import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.viewmodel.GrupaViewModel
import ba.etf.rma21.projekat.viewmodel.KvizViewModel
import ba.etf.rma21.projekat.viewmodel.PredmetViewModel
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PredmetiKvizoviGrupeUnitTest {

   /* @Test
    fun sviKvizoviTest1() {
        assertEquals(KvizViewModel().dajSve().size, 10)
    }

    @Test
    fun sviKvizoviTest2() {
        for(i in 0..KvizViewModel().dajSve().size-2) {
            assertTrue(KvizViewModel().dajSve()[i].datumPocetka > KvizViewModel().dajSve()[i+1].datumPocetka)
        }
        //provjera da li je sortirano
    }

    @Test
    fun mojiKvizoviTest1() {
        assertEquals(KvizViewModel().dajMojeKvizove().size, 3)
    }

    @Test
    fun mojiKvizoviTest2() {
        val listaNazivaKvizova: List<String> = KvizViewModel().dajMojeKvizove().map { k -> k.nazivGrupe }
        assertTrue(listaNazivaKvizova.contains("NRPR1"))
        assertTrue(listaNazivaKvizova.contains("OOI1"))
        assertTrue(listaNazivaKvizova.contains("TP2"))
    }

    @Test
    fun neuradjeniKvizoviTest() {
        KvizViewModel().dajNeuradjene().forEach{ k ->
            assertEquals(k.datumRada, null)
            assertEquals(k.osvojeniBodovi, null)
            assertTrue(k.datumPocetka <= Date() && k.datumKraj <= Date())
        }
    }

    @Test
    fun buduciKvizoviTest() {
        KvizViewModel().dajBuduce().forEach{ k ->
            assertEquals(k.datumRada, null)
            assertEquals(k.osvojeniBodovi, null)
            assertTrue(k.datumPocetka > Date() && k.datumKraj > Date())
        }
    }

    @Test
    fun uradjeniKvizoviTest() {
        KvizViewModel().dajZavrsene().forEach{ k ->
            assertNotEquals(k.datumRada, null)
            assertNotEquals(k.osvojeniBodovi, null)
            assertTrue(k.datumPocetka <= Date() && k.datumKraj <= Date())
        }
    }

    @Test
    fun predmetiPoGodiniTest1() {
        assertEquals(PredmetViewModel().getPredmetsByGodina(1).size, 6)
    }

    @Test
    fun predmetiPoGodiniTest2() {
        PredmetViewModel().getPredmetsByGodina(3).forEach{ p ->
            assertTrue(p.godina == 3)
        }
        val lista_predmeta: List<Predmet> = listOf(Predmet("NRPR", 3), Predmet("OOI", 3))
        PredmetViewModel().getPredmetsByGodina(3).containsAll(lista_predmeta)
    }

    @Test
    fun grupePoPredmetuTest() {
        assertEquals(GrupaViewModel().dajGrupePoPredmetu("FWT").size, 2)
        assertEquals(GrupaViewModel().dajGrupePoPredmetu("RP").size, 2) // ne smije NRPR uzeti!
        assertEquals(GrupaViewModel().dajGrupePoPredmetu("").size, 0)
    }*/


}