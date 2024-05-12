package Kaviru.MAD.LAB.MyToDoApp.database

import Kaviru.MAD.LAB.MyToDoApp.models.Task
import android.content.Context
import Kaviru.MAD.LAB.MyToDoApp.dao.TaskDao
import androidx.room.Database
import Kaviru.MAD.LAB.MyToDoApp.converters.TypeConverter
import androidx.room.Room
import androidx.room.TypeConverters
import androidx.room.RoomDatabase

@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class TaskDatabase : RoomDatabase() {

    abstract val pTaskDaos : TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null
        fun getInstance(context: Context): TaskDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_db"
                ).build().also {
                    INSTANCE = it
                }
            }

        }
    }

}