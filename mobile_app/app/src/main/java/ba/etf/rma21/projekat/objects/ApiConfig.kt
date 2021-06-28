package ba.etf.rma21.projekat.objects

import ba.etf.rma21.projekat.data.repositories.Api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    var retrofit : Api = Retrofit.Builder()
        .baseUrl("https://rma21-etf.herokuapp.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)

}