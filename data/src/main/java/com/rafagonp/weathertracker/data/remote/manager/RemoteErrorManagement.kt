package com.rafagonp.weathertracker.data.remote.manager

import android.util.Log
import arrow.core.Either
import com.rafagonp.weathertracker.domain.model.CustomError
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

internal object RemoteErrorManagement {

    private const val ERROR_JSON_TITLE = "error"
    private const val ERRORS_JSON_TITLE = "errors"
    private const val CODE_ERROR_JSON_TITLE = "error_codes"
    private const val TAG = "RemoteErrorManagement"

    suspend fun <T, R> safeCall(
        action: suspend () -> Response<T>,
        mapperToDomainLayer: (T) -> R,
    ): Either<CustomError, R> = handleResponse(action, onSuccess = { response, body, errorBody ->
        when {
            response.isSuccessful && body != null -> Either.Right(mapperToDomainLayer(body))
            else -> Either.Left(handleError(response, errorBody))
        }
    })

    private suspend fun <T, R> handleResponse(
        action: suspend () -> Response<T>,
        onSuccess: (Response<T>, T?, ResponseBody?) -> Either<CustomError, R>,
    ): Either<CustomError, R> = try {
        val response = action()
        val body = response.body()
        val errorBody = response.errorBody()

        onSuccess(response, body, errorBody)
    } catch (exception: Exception) {
        Either.Left(exception.toError())
    }


    private fun handleError(response: Response<*>, errorBody: ResponseBody?): CustomError =
        handleCode(
            response.code(),
            errorBody?.string() ?: ERROR_JSON_TITLE
        )

    fun getErrorMessage(errorBody: String): String = try {
        JSONObject(errorBody).getJSONArray(ERRORS_JSON_TITLE).get(0).toString()
    } catch (exception: JSONException) {
        Log.e("Error", exception.message.toString())
        try {
            JSONObject(errorBody).getString(ERROR_JSON_TITLE)
        } catch (exception: JSONException) {
            Log.e("Error", exception.message.toString())
            errorBody
        }
    } catch (exception: JSONException) {
        Log.e("Error", exception.message.toString())
        errorBody
    }

    fun getErrorCode(errorBody: String): String = try {
        JSONObject(errorBody).getJSONArray(CODE_ERROR_JSON_TITLE).get(0).toString()
    } catch (exception: JSONException) {
        Log.e("Error", exception.message.toString())
        errorBody
    }


    private fun Throwable.toError(): CustomError = when (this) {
        is IOException -> CustomError.Connectivity
        is HttpException -> CustomError.ServerError.fromResponse(code(), message(), message())
        else -> CustomError.Unknown(message ?: "Unexpected error. Please try again")
    }

    private fun handleCode(code: Int, errorBody: String): CustomError =
        CustomError.ServerError.fromResponse(
            code,
            getErrorMessage(errorBody),
            getErrorCode(errorBody)
        )
}

