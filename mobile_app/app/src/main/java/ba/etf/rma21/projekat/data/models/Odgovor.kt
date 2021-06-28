package ba.etf.rma21.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class Odgovor(
    @PrimaryKey(autoGenerate = true) @SerializedName("id") var id: Int,
    @ColumnInfo(name = "odgovoreno") @SerializedName("odgovoreno") var odgovoreno: Int,
    @ColumnInfo(name = "KvizTakenId") @SerializedName("KvizTakenId") var KvizTakenId: Int,
    @ColumnInfo(name = "PitanjeId") @SerializedName("PitanjeId") var PitanjeId: Int
)