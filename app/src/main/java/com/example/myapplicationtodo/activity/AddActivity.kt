package com.example.myapplicationtodo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.myapplicationtodo.R
import com.example.myapplicationtodo.data.Todo
import com.example.myapplicationtodo.data.TodoDatabase
import com.example.myapplicationtodo.databinding.ActivityAddBinding
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    lateinit var spinner: AutoCompleteTextView
    lateinit var priority:Array<String>
    private var my_priority:Int = 0
    private var priority_text:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        priority = arrayOf("High priority", "Medium priority", "Low priority")
        spinner = binding.spinner
        val my_adapters_spiiner = ArrayAdapter(this, android.R.layout.simple_spinner_item, priority)

        my_adapters_spiiner.setDropDownViewResource(

            android.R.layout.simple_dropdown_item_1line
        )

        spinner.setAdapter(my_adapters_spiiner)

        spinner.onItemClickListener = OnItemClickListener {parent, arg1, pos, id ->

            priority_text = (parent?.getItemAtPosition(pos) as CharSequence?).toString()
            if (priority_text == "High priority") {
                my_priority = 1
            } else if (priority_text == "Medium priority") {
                my_priority = 2
            } else if (priority_text == "Low priority") {
                my_priority = 3
            } else {

            }
        }




    }




    private fun addUser() {


        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text.toString()

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd.MM HH:mm")
        val date = current.format(formatter)

        lifecycleScope.launch {
            if (my_priority == 0) {
                Toast.makeText(
                    this@AddActivity,
                    "Пожалуйста выберите статус заметки",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val todo = Todo(
                    title = title, description = description, priority = my_priority, date = date
                )

                TodoDatabase(this@AddActivity).getTodoDao().addTodo(todo)


                val intent = Intent(this@AddActivity, MainActivity::class.java)
                startActivity(intent)

                Toast.makeText(this@AddActivity, "Successful added", Toast.LENGTH_SHORT).show()
            }

        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.save_action ->{
                addUser()
            }
            R.id.back ->{
                val intent = Intent(this@AddActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }


}
