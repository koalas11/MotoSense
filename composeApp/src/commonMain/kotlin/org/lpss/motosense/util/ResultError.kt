package org.lpss.motosense.util

/**
 * Error types for the repository
 */
sealed interface ResultError {
    val message: String

    data class UnknownError(
        override val message: String
    ) : ResultError
}

/**
 * Exception thrown by the repository
 */
internal class ResultException(
    val error: ResultError
) : Exception()
