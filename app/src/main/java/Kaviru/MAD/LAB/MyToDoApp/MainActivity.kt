package Kaviru.MAD.LAB.MyToDoApp

import java.util.UUID
import android.app.Dialog
import java.util.Date
import android.os.Bundle
import kotlinx.coroutines.launch
import android.text.Editable
import kotlinx.coroutines.flow.collectLatest
import android.text.TextWatcher
import kotlinx.coroutines.Dispatchers
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import android.view.inputmethod.EditorInfo
import com.google.android.material.textfield.TextInputLayout
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.core.view.ViewCompat
import Kaviru.MAD.LAB.MyToDoApp.viewmodels.TaskViewModel
import androidx.lifecycle.MutableLiveData
import Kaviru.MAD.LAB.MyToDoApp.utils.validateEditText
import androidx.lifecycle.ViewModelProvider
import Kaviru.MAD.LAB.MyToDoApp.utils.setupDialog
import androidx.recyclerview.widget.LinearLayoutManager
import Kaviru.MAD.LAB.MyToDoApp.utils.longToastShow
import androidx.recyclerview.widget.RecyclerView
import Kaviru.MAD.LAB.MyToDoApp.utils.hideKeyBoard
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import Kaviru.MAD.LAB.MyToDoApp.utils.clearEditText
import com.coding.meet.todo_app.R
import Kaviru.MAD.LAB.MyToDoApp.utils.StatusResult.Updated
import Kaviru.MAD.LAB.MyToDoApp.adapters.TaskRVVBListAdapter
import Kaviru.MAD.LAB.MyToDoApp.utils.StatusResult.Deleted
import com.coding.meet.todo_app.databinding.ActivityMainBinding
import Kaviru.MAD.LAB.MyToDoApp.utils.StatusResult.Added
import Kaviru.MAD.LAB.MyToDoApp.models.Task
import Kaviru.MAD.LAB.MyToDoApp.utils.StatusResult
import Kaviru.MAD.LAB.MyToDoApp.utils.Status


class MainActivity : AppCompatActivity() {

    private val pMainBindings: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val pAddTaskDialogs: Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.add_task_dialog)
        }
    }

    private val pUpdateTaskDialogs: Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.update_task_dialog)
        }
    }

    private val pLoadingDialogs: Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.loading_dialog)
        }
    }

    private val pTaskViewModels: TaskViewModel by lazy {
        ViewModelProvider(this)[TaskViewModel::class.java]
    }

    private val pIsListMutableLiveDatas = MutableLiveData<Boolean>().apply {
        postValue(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(pMainBindings.root)


        val pAddCloseImgs = pAddTaskDialogs.findViewById<ImageView>(R.id.closeImg)
        pAddCloseImgs.setOnClickListener { pAddTaskDialogs.dismiss() }

        val pAddETTitles = pAddTaskDialogs.findViewById<TextInputEditText>(R.id.edTaskTitle)
        val pAddETTitleLs = pAddTaskDialogs.findViewById<TextInputLayout>(R.id.edTaskTitleL)

        pAddETTitles.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, pP1: Int, pP2: Int, pP3: Int) {}
            override fun onTextChanged(p0: CharSequence?, pP1: Int, pP2: Int, pP3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(pAddETTitles, pAddETTitleLs)
            }

        })

        val pAddETDescs = pAddTaskDialogs.findViewById<TextInputEditText>(R.id.edTaskDesc)
        val pAddETDescLs = pAddTaskDialogs.findViewById<TextInputLayout>(R.id.edTaskDescL)

        pAddETDescs.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, pP1: Int, pP2: Int, pP3: Int) {}
            override fun onTextChanged(p0: CharSequence?, pP1: Int,  pP2: Int, pP3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(pAddETDescs, pAddETDescLs)
            }
        })

        pMainBindings.addTaskFABtn.setOnClickListener {
            clearEditText(pAddETTitles, pAddETTitleLs)
            clearEditText(pAddETDescs, pAddETDescLs)
            pAddTaskDialogs.show()
        }
        val pSaveTaskBtns = pAddTaskDialogs.findViewById<Button>(R.id.saveTaskBtn)
        pSaveTaskBtns.setOnClickListener {
            if (validateEditText(pAddETTitles, pAddETTitleLs)
                && validateEditText(pAddETDescs, pAddETDescLs)
            ) {

                val pNewTasks = Task(
                    UUID.randomUUID().toString(),
                    pAddETTitles.text.toString().trim(),
                    pAddETDescs.text.toString().trim(),
                    Date()
                )
                hideKeyBoard(it)
                pAddTaskDialogs.dismiss()
                pTaskViewModels.pInsertTasks(pNewTasks)
            }
        }



        val pUpdateETTitles = pUpdateTaskDialogs.findViewById<TextInputEditText>(R.id.edTaskTitle)
        val pUpdateETTitleLs = pUpdateTaskDialogs.findViewById<TextInputLayout>(R.id.edTaskTitleL)

        pUpdateETTitles.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, pP1: Int, pP2: Int, pP3: Int) {}
            override fun onTextChanged(p0: CharSequence?, pP1: Int, pP2: Int, pP3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(pUpdateETTitles, pUpdateETTitleLs)
            }

        })

        val pUpdateETDescs = pUpdateTaskDialogs.findViewById<TextInputEditText>(R.id.edTaskDesc)
        val pUpdateETDescLs = pUpdateTaskDialogs.findViewById<TextInputLayout>(R.id.edTaskDescL)

        pUpdateETDescs.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, pP1: Int, pP2: Int, pP3: Int) {}
            override fun onTextChanged(p0: CharSequence?, pP1: Int, pP2: Int, pP3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(pUpdateETDescs, pUpdateETDescLs)
            }
        })

        val pUpdateCloseImgs = pUpdateTaskDialogs.findViewById<ImageView>(R.id.closeImg)
        pUpdateCloseImgs.setOnClickListener { pUpdateTaskDialogs.dismiss() }

        val pUpdateTaskBtns = pUpdateTaskDialogs.findViewById<Button>(R.id.updateTaskBtn)


        pIsListMutableLiveDatas.observe(this){
            if (it){
                pMainBindings.taskRV.layoutManager = LinearLayoutManager(
                    this,LinearLayoutManager.VERTICAL,false
                )
                pMainBindings.listOrGridImg.setImageResource(R.drawable.ics_views_modules)
            }else{
                pMainBindings.taskRV.layoutManager = StaggeredGridLayoutManager(
                    2,LinearLayoutManager.VERTICAL
                )
                pMainBindings.listOrGridImg.setImageResource(R.drawable.ics_views_lists)
            }
        }

        pMainBindings.listOrGridImg.setOnClickListener {
            pIsListMutableLiveDatas.postValue(!pIsListMutableLiveDatas.value!!)
        }

        val pTaskRVVBListAdapters = TaskRVVBListAdapter(pIsListMutableLiveDatas ) { type, position, task ->
            if (type == "delete") {
                pTaskViewModels

                    .pDeleteTaskUsingIds(task.id)


                pRestoreDeletedTasks(task)
            } else if (type == "update") {
                pUpdateETTitles.setText(task.title)
                pUpdateETDescs.setText(task.description)
                pUpdateTaskBtns.setOnClickListener {
                    if (validateEditText(pUpdateETTitles, pUpdateETTitleLs)
                        && validateEditText(pUpdateETDescs, pUpdateETDescLs)
                    ) {
                        val pUpdateTasks = Task(
                            task.id,
                            pUpdateETTitles.text.toString().trim(),
                            pUpdateETDescs.text.toString().trim(),

                            Date()
                        )
                        hideKeyBoard(it)
                        pUpdateTaskDialogs.dismiss()
                        pTaskViewModels
                            .pUpdateTasks(pUpdateTasks)

                    }
                }
                pUpdateTaskDialogs.show()
            }
        }
        pMainBindings.taskRV.adapter = pTaskRVVBListAdapters
        ViewCompat.setNestedScrollingEnabled(pMainBindings.taskRV,false)
        pTaskRVVBListAdapters.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(pPositionStart: Int, pItemCount: Int) {
                super.onItemRangeInserted(pPositionStart, pItemCount)
//                mainBinding.taskRV.smoothScrollToPosition(positionStart)
                pMainBindings.nestedScrollView.smoothScrollTo(0,pPositionStart)
            }
        })
        pCallGetTaskLists(pTaskRVVBListAdapters)
        pCallSortByLiveDatas()
        pStatusCallbacks()

        pCallSearchs()

    }

    private fun pRestoreDeletedTasks(deletedTask : Task){
        val pSnackBars = Snackbar.make(
            pMainBindings.root, "Deleted '${deletedTask.title}'",
            Snackbar.LENGTH_LONG
        )
        pSnackBars.setAction("Undo"){
            pTaskViewModels.pInsertTasks(deletedTask)
        }
        pSnackBars.show()
    }

    private fun pCallSearchs() {
        pMainBindings.edSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, pP1: Int, pP2: Int, pP3: Int) {}

            override fun onTextChanged(p0: CharSequence?, pP1: Int, pP2: Int, pP3: Int) {}

            override fun afterTextChanged(query: Editable) {
                if (query.toString().isNotEmpty()){
                    pTaskViewModels.pSearchTaskLists(query.toString())
                }else{
                    pCallSortByLiveDatas()
                }
            }
        })

        pMainBindings.edSearch.setOnEditorActionListener{ v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                hideKeyBoard(v)
                return@setOnEditorActionListener true
            }
            false
        }

        pCallSortByDialogs()
    }
    private fun pCallSortByLiveDatas(){
        pTaskViewModels.pSortByLiveDatas.observe(this){
            pTaskViewModels.pGetTaskLists(it.second,it.first)
        }
    }

    private fun pCallSortByDialogs() {
        var checkedItem = 0   // 2 is default item set
        val items = arrayOf("Title Ascending", "Title Descending","Date Ascending","Date Descending")

        pMainBindings.sortImg.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Sort By")
                .setPositiveButton("Ok") { _, _ ->
                    when (checkedItem) {
                        0 -> {
                            pTaskViewModels.pSetSortBys(Pair("title",true))
                        }
                        1 -> {
                            pTaskViewModels.pSetSortBys(Pair("title",false))
                        }
                        2 -> {
                            pTaskViewModels.pSetSortBys(Pair("date",true))
                        }
                        else -> {
                            pTaskViewModels.pSetSortBys(Pair("date",false))
                        }
                    }
                }
                .setSingleChoiceItems(items, checkedItem) { _, selectedItemIndex ->
                    checkedItem = selectedItemIndex
                }
                .setCancelable(false)
                .show()
        }
    }

    private fun pStatusCallbacks() {
        pTaskViewModels
            .pStatusLiveDatas
            .observe(this) {
                when (it.pStatus) {
                    Status.LOADING -> {
                        pLoadingDialogs.show()
                    }

                    Status.SUCCESS -> {
                        pLoadingDialogs.dismiss()
                        when (it.pData as StatusResult) {
                            Added -> {
                                Log.d("StatusResult", "Added")
                            }

                            Deleted -> {
                                Log.d("StatusResult", "Deleted")

                            }

                            Updated -> {
                                Log.d("StatusResult", "Updated")

                            }
                        }
                        it.pMessage?.let { it1 -> longToastShow(it1) }
                    }

                    Status.ERROR -> {
                        pLoadingDialogs.dismiss()
                        it.pMessage?.let { it1 -> longToastShow(it1) }
                    }
                }
            }
    }

    private fun pCallGetTaskLists(taskRecyclerViewAdapter: TaskRVVBListAdapter) {

        CoroutineScope(Dispatchers.Main).launch {
            pTaskViewModels
                .pTaskStateFlows
                .collectLatest {
                    Log.d("status", it.pStatus.toString())

                    when (it.pStatus) {
                        Status.LOADING -> {
                            pLoadingDialogs.show()
                        }

                        Status.SUCCESS -> {
                            pLoadingDialogs.dismiss()
                            it.pData?.collect { taskList ->
                                taskRecyclerViewAdapter.submitList(taskList)
                            }
                        }

                        Status.ERROR -> {
                            pLoadingDialogs.dismiss()
                            it.pMessage?.let { it1 -> longToastShow(it1) }
                        }
                    }

                }
        }
    }
}