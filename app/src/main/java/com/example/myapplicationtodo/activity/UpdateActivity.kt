package com.example.myapplicationtodo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.myapplicationtodo.R
import com.example.myapplicationtodo.adapter.TodoAdapter
import com.example.myapplicationtodo.data.Todo
import com.example.myapplicationtodo.data.TodoDatabase
import com.example.myapplicationtodo.databinding.ActivityUpdateBinding
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UpdateActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateBinding
    private var todo: Todo? = null
    private var mAdapter: TodoAdapter? = null
    lateinit var spinner_update: AutoCompleteTextView
    lateinit var priority_update:Array<String>
    private var my_priority_update:Int = 0
    private var priority_text_update:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todo = intent.getSerializableExtra("Data") as Todo?

        binding.titleUpdate.setText(todo?.title.toString())
        binding.descriptionUpdate.setText(todo?.description.toString())
        if (todo?.priority == 1){
            priority_update = arrayOf("High priority", "Medium priority", "Low priority")
        }else if(todo?.priority == 2){
            priority_update = arrayOf("Medium priority", "High priority", "Low priority")
        }else if(todo?.priority == 3){
            priority_update = arrayOf("Low priority", "High priority", "Medium priority")
        }else{
            priority_update = arrayOf("High priority", "Medium priority", "Low priority")
        }

        spinner_update = binding.spinnerUpdate
        val my_adapters_spiiner = ArrayAdapter(this, android.R.layout.simple_spinner_item, priority_update)

        my_adapters_spiiner.setDropDownViewResource(

            android.R.layout.simple_dropdown_item_1line
        )

        spinner_update.setAdapter(my_adapters_spiiner)

        spinner_update.onItemClickListener = AdapterView.OnItemClickListener{parent, arg1, pos,id ->

                priority_text_update = (parent?.getItemAtPosition(pos) as CharSequence?).toString()

                if (priority_text_update == "High priority"){
                    my_priority_update = 1
                }
                else if (priority_text_update == "Medium priority"){
                    my_priority_update = 2
                }
                else if (priority_text_update == "Low priority"){
                    my_priority_update = 3
                }
                else{
                    Toast.makeText(this, "Пожалуйста выберите статус заметки", Toast.LENGTH_SHORT).show()
                }

        }







    }


    private fun updateUser() {
        val title = binding.titleUpdate.text.toString()
        val description = binding.descriptionUpdate.text.toString()
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd.MM. HH:mm")
        val date = current.format(formatter)

        lifecycleScope.launch {
            if (my_priority_update == 0) {
                Toast.makeText(
                    this@UpdateActivity,
                    "Пожалуйста выберите статус заметки",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val todos = Todo(title, description, priority = my_priority_update, date = date)

                todos.id = todo?.id ?: 0
                TodoDatabase(this@UpdateActivity).getTodoDao().updateTodo(todos)

                val intent = Intent(this@UpdateActivity, MainActivity::class.java)
                startActivity(intent)

                Toast.makeText(this@UpdateActivity, "Successful updated", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun setAdapter(list:List<Todo>){
        mAdapter?.setData(list)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.update_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.update_action -> {
                updateUser()
            }
            R.id.back_main -> {
                val intent = Intent(this@UpdateActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }


        return super.onOptionsItemSelected(item)
    }



}