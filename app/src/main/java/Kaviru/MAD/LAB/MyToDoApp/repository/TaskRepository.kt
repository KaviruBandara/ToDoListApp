package Kaviru.MAD.LAB.MyToDoApp.repository

import kotlinx.coroutines.launch
import android.app.Application
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import Kaviru.MAD.LAB.MyToDoApp.database.TaskDatabase
import kotlinx.coroutines.delay
import Kaviru.MAD.LAB.MyToDoApp.utils.Resource.Success
import Kaviru.MAD.LAB.MyToDoApp.models.Task
import kotlinx.coroutines.Dispatchers
import Kaviru.MAD.LAB.MyToDoApp.utils.Resource
import kotlinx.coroutines.CoroutineScope
import Kaviru.MAD.LAB.MyToDoApp.utils.Resource.Error
import Kaviru.MAD.LAB.MyToDoApp.utils.StatusResult
import Kaviru.MAD.LAB.MyToDoApp.utils.Resource.Loading



class TaskRepository(application: Application) {


    private val _pSortByLiveDatas = MutableLiveData<Pair<String,Boolean>>().apply {
        postValue(Pair("title",true))
    }
    val pSortByLiveDatas: LiveData<Pair<String,Boolean>>
        get() = _pSortByLiveDatas



    private val pTaskDaos = TaskDatabase.getInstance(application).pTaskDaos




    private val _pStatusLiveDatas = MutableLiveData<Resource<StatusResult>>()
    val pStatusLiveDatas: LiveData<Resource<StatusResult>>
        get() = _pStatusLiveDatas



    private val _pTaskStateFlows = MutableStateFlow<Resource<Flow<List<Task>>>>(Loading())
    val pTaskStateFlows: StateFlow<Resource<Flow<List<Task>>>>
        get() = _pTaskStateFlows




    private fun pHandleResults(result: Int, message: String, statusResult: StatusResult) {
        if (result != -1) {
            _pStatusLiveDatas.postValue(Success(message, statusResult))
        } else {
            _pStatusLiveDatas.postValue(Error("Something Went Wrong", statusResult))
        }
    }



    fun pGetTaskLists(isAsc : Boolean, sortByName:String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _pTaskStateFlows.emit(Loading())
                delay(500)
                val result = if (sortByName == "title"){
                    pTaskDaos.getTaskListSortByTaskTitle(isAsc)
                }else{
                    pTaskDaos.getTaskListSortByTaskDate(isAsc)
                }
                _pTaskStateFlows.emit(Success("loading", result))
            } catch (e: Exception) {
                _pTaskStateFlows.emit(Error(e.message.toString()))
            }
        }
    }

    fun pUpdateTaskPaticularFields(taskId: String, title: String, description: String) {
        try {
            _pStatusLiveDatas.postValue(Loading())
            CoroutineScope(Dispatchers.IO).launch {
                val result = pTaskDaos.updateTaskPaticularField(taskId, title, description)
                pHandleResults(result, "Updated Task Successfully", StatusResult.Updated)

            }
        } catch (e: Exception) {
            _pStatusLiveDatas.postValue(Error(e.message.toString()))
        }
    }

    fun pInsertTasks(task: Task) {
        try {
            _pStatusLiveDatas.postValue(Loading())
            CoroutineScope(Dispatchers.IO).launch {
                val result = pTaskDaos.insertTask(task)
                pHandleResults(result.toInt(), "Inserted Task Successfully", StatusResult.Added)
            }
        } catch (e: Exception) {
            _pStatusLiveDatas.postValue(Error(e.message.toString()))
        }
    }


    fun pDeleteTaskUsingIds(taskId: String) {
        try {
            _pStatusLiveDatas.postValue(Loading())
            CoroutineScope(Dispatchers.IO).launch {
                val result = pTaskDaos.deleteTaskUsingId(taskId)
                pHandleResults(result, "Deleted Task Successfully", StatusResult.Deleted)

            }
        } catch (e: Exception) {
            _pStatusLiveDatas.postValue(Error(e.message.toString()))
        }
    }

    fun pDeleteTasks(task: Task) {
        try {
            _pStatusLiveDatas.postValue(Loading())
            CoroutineScope(Dispatchers.IO).launch {
                val result = pTaskDaos.deleteTask(task)
                pHandleResults(result, "Deleted Task Successfully", StatusResult.Deleted)

            }
        } catch (e: Exception) {
            _pStatusLiveDatas.postValue(Error(e.message.toString()))
        }
    }

    fun pUpdateTasks(task: Task) {
        try {
            _pStatusLiveDatas.postValue(Loading())
            CoroutineScope(Dispatchers.IO).launch {
                val result = pTaskDaos.updateTask(task)
                pHandleResults(result, "Updated Task Successfully", StatusResult.Updated)

            }
        } catch (e: Exception) {
            _pStatusLiveDatas.postValue(Error(e.message.toString()))
        }
    }

    fun pSetSortBys(sort:Pair<String,Boolean>){
        _pSortByLiveDatas.postValue(sort)
    }

    fun pSearchTaskLists(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _pTaskStateFlows.emit(Loading())
                val result = pTaskDaos.searchTaskList("%${query}%")
                _pTaskStateFlows.emit(Success("loading", result))
            } catch (e: Exception) {
                _pTaskStateFlows.emit(Error(e.message.toString()))
            }
        }
    }
}