package com.engineerfred.kotlin.todoapp.core.util

sealed class Resource<out K> {
    data class Success<out K>( val result: K ) : Resource<K>()
    data class Failure( val errorMessage: String ) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
    data object Undefined : Resource<Nothing>()
}