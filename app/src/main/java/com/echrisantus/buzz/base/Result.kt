package com.echrisantus.buzz.base

import org.xml.sax.ErrorHandler

sealed class Result<out T : Any>

class Success<out T : Any>(val data: T) : Result<T>()

class Error(
    val exception: Throwable,
    val message: String = exception.message ?: "ErrorHandler.UNKNOWN_ERROR"
) : Result<Nothing>()

class Progress(val isLoading: Boolean) : Result<Nothing>()