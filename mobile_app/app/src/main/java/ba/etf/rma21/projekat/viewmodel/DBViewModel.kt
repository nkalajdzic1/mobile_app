package ba.etf.rma21.projekat.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import ba.etf.rma21.projekat.data.repositories.DBRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DBViewModel {

    val scope = CoroutineScope(Job() + Dispatchers.Main)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateNow(): Boolean {
        return DBRepository.updateNow()
    }
}