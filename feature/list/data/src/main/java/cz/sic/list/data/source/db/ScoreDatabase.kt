package cz.sic.list.data.source.db

import androidx.room.Database
import androidx.room.RoomDatabase
import cz.sic.list.data.source.db.model.ScoreEntity

@Database(version = 3, entities = [ScoreEntity::class])
abstract class ScoreDatabase: RoomDatabase() {
    abstract fun scoreDao(): ScoreDao
}