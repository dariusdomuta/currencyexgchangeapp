package com.example.currencyexghangeapp.repository.api

import android.content.Context
import android.net.ConnectivityManager
import com.example.currencyexghangeapp.R
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.net.SocketTimeoutException
import java.util.ArrayList
import javax.inject.Inject

interface ErrorApiHandler {
    fun parseApiErrors(throwable: Throwable): List<ApiError>
}

class ErrorApiHandlerImpl @Inject constructor(@ApplicationContext val context: Context, private val connectivityManager: ConnectivityManager) : ErrorApiHandler {

    override fun parseApiErrors(throwable: Throwable): List<ApiError> {
        Timber.w(throwable)

        if (throwable is com.jakewharton.retrofit2.adapter.rxjava2.HttpException || throwable is retrofit2.HttpException) {
            val response = if (throwable is retrofit2.HttpException) {
                throwable.response()
            } else {
                (throwable as com.jakewharton.retrofit2.adapter.rxjava2.HttpException).response()
            }

            if (response?.code() == UNAUTHORIZED_CODE) {
                return listOf(ApiError(UNAUTHORIZED_CODE.toString(), context.getString(R.string.general_error_message)))
            }

            val apiErrors: MutableList<ApiError> = ArrayList()
            try {
                val listType = object : TypeToken<ArrayList<String>>() {}.type
                val errorBodyAsString = response?.errorBody()?.string()
                val errorBodyAsJson = JsonParser().parse(errorBodyAsString).asJsonObject
                if (errorBodyAsJson.has(ERROR_CODE_KEY)) {
                    val errorJson = errorBodyAsJson.get(ERROR_CODE_KEY)
                    if (errorJson.isJsonArray) {
                        val errors = Gson().fromJson<List<String>>(errorJson, listType)
                        errors.mapTo(apiErrors) { createApiError(it) }
                    } else {
                        apiErrors.add(createApiError(Gson().fromJson(errorJson, String::class.java)))
                    }
                }
            } catch (e: Exception) {
                apiErrors.add(createApiError())
                Timber.w(e)
            }

        } else if (throwable is SocketTimeoutException) {
            return listOf(ApiError(DEFAULT_ERROR_CODE, context.getString(R.string.request_timeout_message_error)))
        }
        return listOf(ApiError(DEFAULT_ERROR_CODE, context.getString(R.string.general_error_message)))
    }

    private fun isNetworkConnected(): Boolean {
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo?.isConnected ?: false
    }

    private fun createApiError(errorCode: String = DEFAULT_ERROR_CODE): ApiError {
        return if (isNetworkConnected()) {
            ApiError(errorCode, context.getString(R.string.general_error_message))
        } else {
            ApiError(errorCode, context.getString(R.string.no_internet_error))
        }
    }

    companion object {
        private const val DEFAULT_ERROR_CODE = "-1"
        private const val ERROR_CODE_KEY = "code"
        private const val UNAUTHORIZED_CODE = 401
    }
}

data class ApiError(
    val errorCode: String,
    val errorMessage: String
)