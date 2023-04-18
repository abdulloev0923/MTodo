package com.example.myapplicationtodo.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationtodo.R
import com.example.myapplicationtodo.adapter.TodoAdapter
import com.example.myapplicationtodo.data.Todo
import com.example.myapplicationtodo.data.TodoDatabase
import com.example.myapplicationtodo.databinding.ActivityMainBinding
import com.example.myapplicationtodo.swipe.Swipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var mAdapter: TodoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.add.setOnClickListener {
            val intent = Intent(this@MainActivity, AddActivity::class.java)
            startActivity(intent)
        }








        lifecycleScope.launch {
            val todoList = TodoDatabase(this@MainActivity).getTodoDao().getAllTodo()


            mAdapter = TodoAdapter()
            binding.recyclerview.apply {
                layoutManager = GridLayoutManager(this@MainActivity, 2)
                adapter = mAdapter.apply {
                    setAdapter(todoList)

                    mAdapter?.setOnActionEditListener {
                        val intent = Intent(this@MainActivity, UpdateActivity::class.java)
                        intent.putExtra("Data", it)
                        startActivity(intent)
                    }






                }
            }






        }
        val swipeToDeleteCallBack = object : Swipe() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                lifecycleScope.launch(Dispatchers.Main) {
                    val position = viewHolder.adapterPosition
                    val list = TodoDatabase(this@MainActivity).getTodoDao().getAllTodo().toMutableList()

                    TodoDatabase(this@MainActivity).getTodoDao()
                        .deleteTodo(list[position])
                    list.removeAt(position)
                    binding.recyclerview.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                    setAdapter(list)




                }
            }


        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(binding.recyclerview)

//        binding.searchview.setOnQueryTextListener(
//            object :SearchView.OnQueryTextListener{
//                override fun onQueryTextSubmit(p0: String?): Boolean {
//                    if(p0 != null){
//                        searchDatabase(p0)
//                    }
//                    return true
//                }
//
//                override fun onQueryTextChange(p0: String?): Boolean {
//                    if(p0 != null){
//                        searchDatabase(p0)
//                    }
//                    return true
//                }
//            }
//        )







    }




    fun setAdapter(list: List<Todo>) {
        mAdapter?.setData(list)
    }

  


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val search = menu.findItem(R.id.search_view)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.queryHint = "Search..."
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {

                return true

            }

            override fun onQueryTextChange(p0: String?): Boolean {

                p0?.let {
                    searchDatabase(it)
                    Log.e("onQueryTextSubmit", "onQueryTextSubmit: $it")
                }

                return true

            }
        })
       return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.high ->{
                lifecycleScope.launch {
                    val list =
                        TodoDatabase(this@MainActivity).getTodoDao().getAllTodo().toMutableList()
                    list.sortBy {
                        it.priority
                    }
                    setAdapter(list)
                }
            }
            R.id.medium ->{
                lifecycleScope.launch {
                    val list =
                        TodoDatabase(this@MainActivity).getTodoDao().getAllTodo().toMutableList()
                    list.sortBy {
                        it.priority.and(1)
                    }

                    setAdapter(list)
                }
            }
            R.id.low ->{
                lifecycleScope.launch {
                    val list =
                        TodoDatabase(this@MainActivity).getTodoDao().getAllTodo().toMutableList()


                    val listComparator = Comparator{p1:Todo , p2:Todo -> p2.priority - p1.priority}
                    list.sortWith(listComparator)

                    setAdapter(list)
                }
            }


            R.id.DeleteAll -> {
                val builder = AlertDialog.Builder(this)

                builder.setMessage("Are you sure you want to delete all?")

                builder.setNegativeButton("No") { p0, p1 ->
                    p0.dismiss()
                }
                builder.setPositiveButton("Yes") { p0, p1 ->
                    lifecycleScope.launch {
                        TodoDatabase(this@MainActivity).getTodoDao().deleteAllTodo()
                        val list = TodoDatabase(this@MainActivity).getTodoDao().getAllTodo()
                        setAdapter(list)
                    }
                    p0.dismiss()
                }

                val dialog = builder.create()
                dialog.show()
            }


            R.id.sort_date_new ->{
                lifecycleScope.launch() {
                    val list = TodoDatabase(this@MainActivity).getTodoDao().getAllTodo().toTypedArray()



                }
            }
            R.id.sort_date_starie ->{
                lifecycleScope.launch {
                    val list =
                        TodoDatabase(this@MainActivity).getTodoDao().getAllTodo().toMutableList()
                    list.sortBy {
                        it.date
                    }

                    setAdapter(list)
                }
            }

        }

        return super.onOptionsItemSelected(item)
    }


    private fun searchDatabase(query:String) {
        lifecycleScope.launch {
            val searchView = "%$query%"
            val list = TodoDatabase(this@MainActivity).getTodoDao().search(searchView)
            list.let {
                mAdapter?.setData(it)
            }


        }
    }


}
