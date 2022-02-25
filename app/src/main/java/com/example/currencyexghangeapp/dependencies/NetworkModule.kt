package com.example.currencyexghangeapp.dependencies

import android.content.Context
import android.net.ConnectivityManager
import com.example.currencyexghangeapp.BuildConfig
import com.example.currencyexghangeapp.repository.api.CurrencyExchangeApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.internal.tls.OkHostnameVerifier
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object {
        private const val REQUEST_TIMEOUT_SECONDS = 30L
    }

    @Singleton
    @Provides
    fun provideHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpBuilder = OkHttpClient.Builder()
            .connectTimeout(REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            okHttpBuilder.addInterceptor(loggingInterceptor)
        }

        return okHttpBuilder.build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BuildConfig.BASE_API_URL).build()

    @Singleton
    @Provides
    fun provideCurrencyExchangeApi(retrofit: Retrofit) : CurrencyExchangeApi = retrofit.create(CurrencyExchangeApi::class.java)
}
