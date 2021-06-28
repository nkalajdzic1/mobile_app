package ba.etf.rma21.projekat.data.models

import com.google.gson.annotations.SerializedName

data class PitanjeAPI(
    @SerializedName("id") var id: Int,
    @SerializedName("naziv") var naziv: String,
    @SerializedName("tekstPitanja") var tekstPitanja: String,
    @SerializedName("opcije") var opcije: List<String>,
    @SerializedName("tacan") var tacan: Int
)