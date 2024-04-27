package com.example.mytasks.Activities

import SharedPreferences
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mytasks.Models.Todo
import com.example.mytasks.databinding.ActivityCreateBinding

class CreateActivity : AppCompatActivity() {

    lateinit var binding: ActivityCreateBinding
    private lateinit var arrayList: ArrayList<Todo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

            arrayList = arrayListOf()

        binding.button1.setOnClickListener {
            val title = binding.editTextText.text?.toString()
            val text = binding.editTextTextMultiLine.text?.toString()

            if (title!!.isNotBlank() && text!!.isNotBlank()) {

                val newTodo = Todo(title, text)

                if (arrayList.size == 0) {
                    arrayList = SharedPreferences.getArrayListFromSharedPreferences(this)
                }

                newTodo?.let { it1 ->

                    arrayList.add(it1)

                    SharedPreferences.saveArrayListToSharedPreferences(this, arrayList)

                    Log.i("list", arrayList.toString())

                }

                val intent = Intent(this@CreateActivity, ViewActivity::class.java)
                intent.putExtra("arrayList", arrayList)
                startActivity(intent)
            }
            else{

                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("Information")
                alertDialogBuilder.setMessage("None of the places where Title and Text should be written can be empty.\n")
                alertDialogBuilder.setCancelable(false)

                alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }


                val alertDialog = alertDialogBuilder.create()


                alertDialog.show()




            }
        }
    }


}
