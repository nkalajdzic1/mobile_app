package ba.etf.rma21.projekat.data.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import ba.etf.rma21.projekat.data.models.*
import retrofit2.Response
import retrofit2.http.*
import java.time.LocalDate
import java.time.LocalDateTime

interface Api {

    @GET("/kviz")
    suspend fun GetKvizovi(@Query("api_key") apiKey: String = AccountRepository.getHash()): Response<List<Kviz>>

    @GET("/kviz/{id}/pitanja")
    suspend fun GetKvizoviById(@Path("id") id:Int, @Query("api_key") apiKey: String = AccountRepository.getHash()): Response<List<Pitanje>>

    @GET("/kviz/{id}/grupa")
    suspend fun GetGrupeByKviz(@Path("id") id:Int, @Query("api_key") apiKey: String = AccountRepository.getHash()): Response<List<Grupa>>

    @GET("/predmet/{id}")
    suspend fun GetPredmetById(@Path("id") id:Int, @Query("api_key") apiKey: String = AccountRepository.getHash()): Response<Predmet>

    @GET("/student/{id}/kviztaken")
    suspend fun GetKvizoviForStudent(@Path("id") id:String = AccountRepository.getHash(), @Query("api_key") apiKey: String = AccountRepository.getHash()): Response<List<KvizTaken>>

    @GET("/student/{id}/grupa")
    suspend fun GetGrupeByStudent(@Path("id") id:String = AccountRepository.getHash(), @Query("api_key") apiKey: String = AccountRepository.getHash()): Response<List<Grupa>>

    @GET("/grupa/{id}/kvizovi")
    suspend fun GetKvizoviByGroup(@Path("id") id:Int, @Query("api_key") apiKey: String = AccountRepository.getHash()): Response<List<Kviz>>

    @GET("/grupa")
    suspend fun GetGrupe(@Query("api_key") apiKey: String = AccountRepository.getHash()): Response<List<Grupa>>

    @GET("/kviz/{id}")
    suspend fun GetKvizById(@Path("id") id:Int, @Query("api_key") apiKey: String = AccountRepository.getHash()): Response<Kviz>

    @GET("/predmet")
    suspend fun GetPredmeti(@Query("api_key") apiKey: String = AccountRepository.getHash()): Response<List<Predmet>>

    @GET("/predmet/{id}/grupa")
    suspend fun GetGroupsByPredmetId(@Path("id") id:Int, @Query("api_key") apiKey: String = AccountRepository.getHash()): Response<List<Grupa>>

    @GET("/kviz/{id}/pitanja")
    suspend fun GetPitanjaByKvizId(@Path("id") id:Int, @Query("api_key") apiKey: String = AccountRepository.getHash()): Response<List<PitanjeAPI>>

    @POST("/grupa/{gid}/student/{id}")
    suspend fun AddStudentToGroup(@Path("gid") gid:Int, @Path("id") id:String = AccountRepository.getHash(), @Query("api_key") apiKey: String = AccountRepository.getHash()): Response<Message>

    @POST("/student/{id}/kviz/{kid}")
    suspend fun TakeKviz(@Path("kid") kid:Int, @Path("id") id:String = AccountRepository.getHash(), @Query("api_key") apiKey: String = AccountRepository.getHash()): Response<KvizTaken>

    @GET("/student/{id}/kviztaken/{ktid}/odgovori")
    suspend fun GetOdgovori(@Path("ktid") kid:Int, @Path("id") id:String = AccountRepository.getHash(), @Query("api_key") apiKey: String = AccountRepository.getHash()): Response<List<Odgovor>>

    @POST("/student/{id}/kviz/{kid}")
    suspend fun StartKviz(@Path("kid") kid:Int, @Path("id") id:String = AccountRepository.getHash(), @Query("api_key") apiKey: String = AccountRepository.getHash()): Response<KvizTaken>

    @POST("/student/{id}/kviztaken/{ktid}/odgovor")
    suspend fun AddAnswer(@Path("ktid") ktid:Int, @Body odgovor: OdgovorDTO, @Path("id") id:String = AccountRepository.getHash(), @Query("api_key") apiKey: String = AccountRepository.getHash()): Response<Odgovor>

    @RequiresApi(Build.VERSION_CODES.O)
    @GET("/account/{id}/lastUpdate")
    suspend fun LastUpdate(@Path("id") id:String = AccountRepository.getHash(), @Query("date") date: String, @Query("api_key") apiKey: String = AccountRepository.getHash()): Response<Changed>


}