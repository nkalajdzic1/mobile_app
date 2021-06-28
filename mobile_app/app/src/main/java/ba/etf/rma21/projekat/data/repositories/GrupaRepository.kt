package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import ba.etf.rma21.projekat.data.models.Grupa

class GrupaRepository {

    companion object {

        private lateinit var context: Context

        fun setContext(_context: Context){
            context=_context
        }

        fun getGroupsByPredmet(nazivPredmeta: String): List<Grupa> {
            return listOf()
        }

        fun getAll() : List<Grupa> { return listOf() }

    }

}