package ba.etf.rma21.projekat.data.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class OdgovorDTO(
    @SerializedName("odgovor") var odgovoreno: Int,
    @SerializedName("pitanje") var pitanje: Int,
    @SerializedName("bodovi") var bodovi: Int
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(odgovoreno)
        parcel.writeInt(pitanje)
        parcel.writeInt(bodovi)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OdgovorDTO> {
        override fun createFromParcel(parcel: Parcel): OdgovorDTO {
            return OdgovorDTO(parcel)
        }

        override fun newArray(size: Int): Array<OdgovorDTO?> {
            return arrayOfNulls(size)
        }
    }
}