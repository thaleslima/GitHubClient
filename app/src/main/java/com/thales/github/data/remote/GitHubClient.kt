package com.thales.github.data.remote

import android.content.Context
import android.util.Log

import com.thales.github.utilities.Constants
import com.thales.github.utilities.Utility

import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object GitHubClient {
    private val TAG = GitHubClient::class.java.name
    private val BASE_URL = Constants.GITHUB_API_URL
    private val CACHE_CONTROL = "Cache-Control"

    private var instance: GitHubApi? = null

    fun getClient(context: Context): GitHubApi? {
        if (instance == null) {
            val client = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getOkHttpClient(context))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            instance = client.create(GitHubApi::class.java)
        }

        return instance
    }

    private fun getOkHttpClient(context: Context): OkHttpClient {
        val okClientBuilder = OkHttpClient.Builder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        okClientBuilder.addInterceptor(httpLoggingInterceptor)
        okClientBuilder.addInterceptor(provideOfflineCacheInterceptor(context))
        okClientBuilder.addNetworkInterceptor(provideCacheInterceptor())
        okClientBuilder.cache(provideCache(context))
        return okClientBuilder.build()
    }


    private fun provideCache(context: Context): Cache? {
        var cache: Cache? = null
        try {
            cache = Cache(File(context.cacheDir, "http-cache"),
                    (10 * 1024 * 1024).toLong()) // 10 MB
        } catch (e: Exception) {
            Log.e(TAG, "Could not create Cache!")
        }

        return cache
    }

    private fun provideCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())

            // re-write response header to force use of cache
            val cacheControl = CacheControl.Builder()
                    .maxAge(1, TimeUnit.HOURS)
                    .build()

            response.newBuilder()
                    .header(CACHE_CONTROL, cacheControl.toString())
                    .build()
        }
    }

    private fun provideOfflineCacheInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()

            if (!Utility.isNetworkAvailable(context)) {
                val cacheControl = CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build()

                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build()
            }

            chain.proceed(request)
        }
    }

}