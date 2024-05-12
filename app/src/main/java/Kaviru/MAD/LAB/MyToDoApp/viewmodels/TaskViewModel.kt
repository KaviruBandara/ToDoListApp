package Kaviru.MAD.LAB.MyToDoApp.viewmodels

import Kaviru.MAD.LAB.MyToDoApp.repository.TaskRepository
import android.app.Application
import Kaviru.MAD.LAB.MyToDoApp.models.Task
import androidx.lifecycle.AndroidViewModel

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    val pSortByLiveDatas get() =  pTaskRepositorys.pSortByLiveDatas
    private val pTaskRepositorys = TaskRepository(application)
    val pStatusLiveDatas get() =  pTaskRepositorys.pStatusLiveDatas
    val pTaskStateFlows get() =  pTaskRepositorys.pTaskStateFlows

    fun pGetTaskLists(isAsc : Boolean, sortByName:String) {
        pTaskRepositorys.pGetTaskLists(isAsc, sortByName)
    }

    fun pSearchTaskLists(query: String){
        pTaskRepositorys.pSearchTaskLists(query)
    }

    fun pUpdateTasks(task: Task) {
        pTaskRepositorys.pUpdateTasks(task)
    }

    fun deleteTask(task: Task) {
        pTaskRepositorys.pDeleteTasks(task)
    }

    fun pSetSortBys(sort:Pair<String,Boolean>){
        pTaskRepositorys.pSetSortBys(sort)
    }



    fun pInsertTasks(task: Task){
        pTaskRepositorys.pInsertTasks(task)
    }



    fun pDeleteTaskUsingIds(taskId: String){
        pTaskRepositorys.pDeleteTaskUsingIds(taskId)
    }



    fun pUpdateTaskPaticularFields(taskId: String, title:String, description:String) {
        pTaskRepositorys.pUpdateTaskPaticularFields(taskId, title, description)
    }

}