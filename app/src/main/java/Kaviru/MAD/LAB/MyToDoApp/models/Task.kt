package Kaviru.MAD.LAB.MyToDoApp.models

import java.util.Date
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity()
data class Task(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "taskId")
    val id: String,
    @ColumnInfo(name = "taskTitle")
    val title: String,
    val description: String,
    val date: Date,
)
