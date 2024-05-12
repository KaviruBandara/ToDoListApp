package Kaviru.MAD.LAB.MyToDoApp.converters

import java.util.Date
import androidx.room.TypeConverter

class TypeConverter {

    @TypeConverter
    fun pFromTimestamps(value:Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun pDateToTimestamps(date:Date): Long {
        return date.time
    }
}