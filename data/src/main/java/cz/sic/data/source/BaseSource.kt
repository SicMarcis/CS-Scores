package cz.sic.data.source

interface BaseSource<T> {
    suspend fun getData(): List<T>
}