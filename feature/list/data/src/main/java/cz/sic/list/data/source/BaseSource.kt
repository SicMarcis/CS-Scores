package cz.sic.list.data.source

interface BaseSource<T> {
    suspend fun getData(): List<T>
}