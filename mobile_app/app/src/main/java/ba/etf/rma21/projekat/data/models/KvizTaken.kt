package ba.etf.rma21.projekat.data.models

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class KvizTaken(
    @PrimaryKey @SerializedName("id") var id: Int,
    @SerializedName("osvojeniBodovi") var osvojeniBodovi: Int,
    @SerializedName("datumRada") var datumRada: String,
    @SerializedName("student") var student: String,
    @SerializedName("KvizId") var KvizId: Int
)