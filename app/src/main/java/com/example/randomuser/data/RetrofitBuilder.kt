package com.example.randomuser.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {
    companion object {
        private var retrofit_api: Retrofit? = null
        //val baseSecureHost = "https://api.randomuser.me"

        val baseSecureHost = "https://mobile.ofertia.com/api/v2/"


        fun getRetrofitApi(): Retrofit? {
            if (retrofit_api == null) initRetrofitApi()

            return retrofit_api
        }


        private fun initRetrofitApi() {
            retrofit_api = Retrofit.Builder()
                .baseUrl(baseSecureHost)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}