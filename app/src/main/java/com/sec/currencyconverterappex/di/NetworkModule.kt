package com.sec.currencyconverterappex.di

import com.sec.currencyconverterappex.data.AppRepositoryImpl
import com.sec.currencyconverterappex.data.remote.ApiService
import com.sec.currencyconverterappex.data.remote.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level= HttpLoggingInterceptor.Level.BODY
        }
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            this.readTimeout(60, TimeUnit.SECONDS)
            this.connectTimeout(60, TimeUnit.SECONDS)
            this.addInterceptor(httpLoggingInterceptor)
        }.build()
    }



    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient):Retrofit{
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideAppApi(retrofit: Retrofit):ApiService{
        return retrofit.create(ApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideAppRepository(apiService: ApiService): AppRepositoryImpl {
        return AppRepositoryImpl(apiService)
    }
}