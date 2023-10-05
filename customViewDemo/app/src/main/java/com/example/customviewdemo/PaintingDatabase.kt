package com.example.customviewdemo

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow

@Database(entities= [PaintingData::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PaintingDatabase : RoomDatabase() {

    abstract fun paintingDao(): PaintingDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PaintingDatabase? = null

        fun getDatabase(context: Context): PaintingDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PaintingDatabase::class.java,
                    "painting_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

@Dao
interface PaintingDAO {

    @Insert
    suspend fun addPaintingData(data: PaintingData)

    @Query("SELECT * from paintings ORDER BY timestamp DESC")
    fun allPaintings() : Flow<List<PaintingData>>

    @Query("SELECT * from paintings ORDER BY timestamp DESC LIMIT 1")
    fun latestPainting() : Flow<PaintingData>

}

//@Dao
//interface ImageDAO {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun addImageData(data: ImageData)
//
//    @Update
//    suspend fun updateImageData(data: ImageData)
//
//    @Query("SELECT * from images ORDER BY timestamp DESC")
//    fun allImages() : Flow<List<ImageData>>
//}

