package com.pkt.emarkit.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import android.content.Context
import android.util.Log
import com.pkt.emarkit.utils.preferences.PrefsSessionManagement
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClientObj(context: Context) {

//        var BASE_URL = "https://dealer-website.primarykeytech.in/dynamic/api/api/"
//        var BASE_URL = "https://logistic.aitechiez.com/api/api/"
        var BASE_URL = "https://uat.patanjaliudaipur.com/logistic/api"


    var okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(addHttpLoggingInterceptor())
        .addInterceptor{ chain ->
            val original = chain.request()
            var session: PrefsSessionManagement = PrefsSessionManagement(context)
            val requestBuilder = original.newBuilder()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Authorization","Bearer "+session.getAccessToken())
                .method(original.method(),original.body())

            val request = requestBuilder.build()
            chain.proceed(request)

        }
        .build()


    val instance : ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        retrofit.create(ApiService::class.java)
    }

    private fun addHttpLoggingInterceptor(): Interceptor {
      val logRequestResponseInterceptor = HttpLoggingInterceptor()
        logRequestResponseInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return logRequestResponseInterceptor
    }
}