package ba.etf.rma21.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class Account(
    @PrimaryKey(autoGenerate = true) @SerializedName("id") var id: Int,
    @ColumnInfo(name = "acHash") @SerializedName("acHash") var acHash: String,
    @ColumnInfo(name = "lastUpdate") @SerializedName("acHash") var lastUpdate: String
)