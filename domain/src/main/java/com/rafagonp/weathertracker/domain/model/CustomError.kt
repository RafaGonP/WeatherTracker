package com.rafagonp.weathertracker.domain.model

sealed interface CustomError {

    data object Connectivity : CustomError

    data class Unknown(val message: String? = null) : CustomError
    companion object {
        const val ERROR_CODE_SERVICE_NOT_AVAILABLE = 503
    }

    sealed interface ServerError : CustomError {
        val message: String
        val errorCode: String

        data class Unauthorized(
            override val message: String,
            override val errorCode: String
        ) : ServerError {
            companion object {
                internal const val ERROR_CODE = 401
            }
        }

        data class NotFound(
            override val message: String,
            override val errorCode: String
        ) : ServerError {
            companion object {
                internal const val ERROR_CODE = 404
            }
        }

        data class ServiceUnavailable(
            override val message: String,
            override val errorCode: String
        ) : ServerError {
            companion object {
                const val ERROR_CODE = 503
            }
        }

        data class UnprocessableEntity(
            override val message: String,
            override val errorCode: String
        ) : ServerError {
            companion object {
                const val ERROR_CODE = 422
            }
        }

        data class UnknownError(
            override val message: String,
            override val errorCode: String
        ) : ServerError

        companion object {
            fun fromResponse(code: Int, message: String, errorCode: String): CustomError =
                when (code) {
                    Unauthorized.ERROR_CODE -> Unauthorized(message, errorCode)
                    NotFound.ERROR_CODE -> NotFound(message, errorCode)
                    ServiceUnavailable.ERROR_CODE -> ServiceUnavailable(message, errorCode)
                    UnprocessableEntity.ERROR_CODE -> UnprocessableEntity(message, errorCode)
                    else -> UnknownError(message, errorCode)
                }
        }
    }
}