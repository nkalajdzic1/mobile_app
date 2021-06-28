package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.objects.ApiConfig.retrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    var baseURL = "https://rma21-etf.herokuapp.com"

        fun postaviBaseURL(baseUrl:String):Unit {
            this.baseURL = baseUrl
        }

        fun dajBaseUrl(): String { return baseURL }


}