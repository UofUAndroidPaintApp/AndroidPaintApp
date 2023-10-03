package com.example.customviewdemo

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}

//Defines a SQLITE table, basically
@Entity(tableName="paintings")
data class PaintingData(
    var timestamp: Date,
    var filename: String,
    var bitmap: ByteArray
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0 // integer primary key for the DB
}

