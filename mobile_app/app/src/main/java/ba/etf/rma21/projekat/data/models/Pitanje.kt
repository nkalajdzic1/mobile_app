package ba.etf.rma21.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Pitanje(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "Pitanjeid") @SerializedName("id") var PitanjeId: Int,
    @ColumnInfo(name = "naziv") @SerializedName("naziv") var naziv: String,
    @ColumnInfo(name = "tekstPitanja") @SerializedName("tekstPitanja") var tekstPitanja: String,
    @ColumnInfo(name = "opcije") @SerializedName("opcije") var opcije: String,
    @ColumnInfo(name = "tacan") @SerializedName("tacan") var tacan: Int,
    @ColumnInfo(name = "KvizId") @SerializedName("KvizId") var KvizId: Int?
)