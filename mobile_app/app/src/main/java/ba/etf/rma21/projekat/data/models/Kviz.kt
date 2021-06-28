package ba.etf.rma21.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Kviz(
    @PrimaryKey @SerializedName("id") var id: Int,
    @ColumnInfo(name = "naziv") @SerializedName("naziv") var naziv: String,
    @ColumnInfo(name = "datumPocetka") @SerializedName("datumPocetak") var datumPocetka: String,
    @ColumnInfo(name = "datumKraj") @SerializedName("datumKraj") var datumKraj: String?,
    @ColumnInfo(name = "trajanje") @SerializedName("trajanje") var trajanje: Int,
    @ColumnInfo(name = "predan") @SerializedName("predan") var predan: Boolean?
)