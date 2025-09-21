package cz.sic.data.source

import kotlinx.coroutines.flow.Flow

interface BaseSource<T> {
    suspend fun getData(): List<T>

    suspend fun save(score: T)

    suspend fun delete(id: Long)

    fun observe(): Flow<List<T>>

    suspend fun deleteAll()

    suspend fun getItem(id: Long): T?
}