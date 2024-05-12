package Kaviru.MAD.LAB.MyToDoApp.adapters

import java.util.Locale
import android.view.LayoutInflater
import java.text.SimpleDateFormat
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import Kaviru.MAD.LAB.MyToDoApp.models.Task
import androidx.lifecycle.MutableLiveData
import com.coding.meet.todo_app.databinding.ViewTaskListLayoutBinding
import androidx.recyclerview.widget.DiffUtil
import com.coding.meet.todo_app.databinding.ViewTaskGridLayoutBinding
import androidx.recyclerview.widget.ListAdapter

class TaskRVVBListAdapter(
    private val pIsLists: MutableLiveData<Boolean>,
    private val pDeleteUpdateCallbacks: (type: String, position: Int, task: Task) -> Unit,
) :
    ListAdapter<Task,RecyclerView.ViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(pOldItem: Task, pNewItem: Task): Boolean {
            return pOldItem.id == pNewItem.id
        }

        override fun areContentsTheSame(pOldItem: Task, pNewItem: Task): Boolean {
            return pOldItem == pNewItem
        }

    }

    class ListTaskViewHolder(private val viewTaskListLayoutBinding: ViewTaskListLayoutBinding) :
        RecyclerView.ViewHolder(viewTaskListLayoutBinding.root) {

        fun bind(
            pTasks: Task,
            pDeleteUpdateCallbacks: (type: String, position: Int, task: Task) -> Unit,
        ) {
            viewTaskListLayoutBinding.titleTxt.text = pTasks.title
            viewTaskListLayoutBinding.descrTxt.text = pTasks.description

            val pDateFormats = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a", Locale.getDefault())

            viewTaskListLayoutBinding.dateTxt.text = pDateFormats.format(pTasks.date)

            viewTaskListLayoutBinding.deleteImg.setOnClickListener {
                if (adapterPosition != -1) {
                    pDeleteUpdateCallbacks("delete", adapterPosition, pTasks)
                }
            }
            viewTaskListLayoutBinding.editImg.setOnClickListener {
                if (adapterPosition != -1) {
                    pDeleteUpdateCallbacks("update", adapterPosition, pTasks)
                }
            }
        }
    }




    override fun onBindViewHolder(pHolder: RecyclerView.ViewHolder, position: Int) {
        val task = getItem(position)

        if (pIsLists.value!!){
            (pHolder as ListTaskViewHolder).bind(task,pDeleteUpdateCallbacks)
        }else{
            (pHolder as GridTaskViewHolder).bind(task,pDeleteUpdateCallbacks)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return if (viewType == 1){  // Grid_Item
            GridTaskViewHolder(
                ViewTaskGridLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }else{  // List_Item
            ListTaskViewHolder(
                ViewTaskListLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }



    override fun getItemViewType(position: Int): Int {
        return if (pIsLists.value!!){
            0 // List_Item
        }else{
            1 // Grid_Item
        }
    }

    class GridTaskViewHolder(private val viewTaskGridLayoutBinding: ViewTaskGridLayoutBinding) :
        RecyclerView.ViewHolder(viewTaskGridLayoutBinding.root) {

        fun bind(
            pTasks: Task,
            pDeleteUpdateCallbacks: (type: String, position: Int, task: Task) -> Unit,
        ) {
            viewTaskGridLayoutBinding.titleTxt.text = pTasks.title
            viewTaskGridLayoutBinding.descrTxt.text = pTasks.description

            val pDateFormats = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a", Locale.getDefault())

            viewTaskGridLayoutBinding.dateTxt.text = pDateFormats.format(pTasks.date)

            viewTaskGridLayoutBinding.deleteImg.setOnClickListener {
                if (adapterPosition != -1) {
                    pDeleteUpdateCallbacks("delete", adapterPosition, pTasks)
                }
            }
            viewTaskGridLayoutBinding.editImg.setOnClickListener {
                if (adapterPosition != -1) {
                    pDeleteUpdateCallbacks("update", adapterPosition, pTasks)
                }
            }
        }
    }

}