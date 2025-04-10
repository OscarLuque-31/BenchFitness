package com.oscar.benchfitness.services

import android.content.Context
import android.provider.Settings.Global.getString
import androidx.compose.ui.platform.LocalContext
import com.oscar.benchfitness.R
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object RetrofitClient {
    // URL Base de mi API
    private const val BASE_URL = "https://api-benchfitness-production.up.railway.app/"
    // Contexto de la app
    private lateinit var appContext: Context

    fun init(context: Context){
        appContext = context.applicationContext
    }

    val apiService: ApiService by lazy {
        val apiKey = appContext.getString(R.string.API_KEY)

        // Crea un cliente con el que añade la api key en el header de la llamada
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request: Request = chain.request().newBuilder()
                    .addHeader("x-api-key", apiKey)
                    .build()
                chain.proceed(request)
            }
            .build()

        // Realiza la llamada añadiendo al cliente
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

}
