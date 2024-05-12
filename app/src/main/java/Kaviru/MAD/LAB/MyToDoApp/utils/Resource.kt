package Kaviru.MAD.LAB.MyToDoApp.utils

sealed class Resource<T>(val pStatus: Status, val pData: T? = null, val pMessage : String? = null) {

    class Loading<T> : Resource<T>(Status.LOADING)
    class Error<T>(pMessage: String, pData: T? = null) : Resource<T>(Status.ERROR,pData,pMessage)
    class Success<T>(pMessage: String, pData: T?= null) : Resource<T>(Status.SUCCESS,pData,pMessage)
}