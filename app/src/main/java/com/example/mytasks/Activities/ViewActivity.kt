package com.example.mytasks.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mytasks.Models.Todo
import com.example.mytasks.R
import com.example.mytasks.databinding.ActivityViewBinding
import java.util.ArrayList

class ViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityViewBinding
    private lateinit var todos: ArrayList<Todo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todos = intent.getSerializableExtra("arrayList") as? ArrayList<Todo> ?: ArrayList()

        if(todos.count()<=0)
        {
            todos=SharedPreferences.getArrayListFromSharedPreferences(this)
        }

        displayTodos(todos)

        setTrashIconClickListener()

        binding.button1.setOnClickListener {

            val intent = Intent(this@ViewActivity, MainActivity::class.java)
            startActivity(intent)

        }
    }

    private fun displayTodos(todos: ArrayList<Todo>) {

        binding.containerLayout.removeAllViews()

        for ((index, todo) in todos.withIndex()) {
            val customLayout = createCustomLayout(todo, index)
            binding.containerLayout.addView(customLayout)
        }
    }

    private fun createCustomLayout(todo: Todo, position: Int): RelativeLayout {
        val relativeLayout = RelativeLayout(this)
        relativeLayout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        relativeLayout.setBackgroundColor(Color.parseColor("#F44336"))

        val layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(20.dpToPx(), 10.dpToPx(), 20.dpToPx(), 0)
        relativeLayout.layoutParams = layoutParams

        val textView1 = createTextView("\n ${todo.title} \n\n", 10, 10)
        val textView2 = createTextView("\n ${todo.text} \n\n", 10, 75)

        relativeLayout.addView(textView1)
        relativeLayout.addView(textView2)

        val trashIcon = createTrashIcon(position)

        trashIcon.setOnClickListener {


            if (position >= 0 && position < todos.size) {
                val todoToRemove = todos[position]
                todos.remove(todoToRemove)
                SharedPreferences.saveArrayListToSharedPreferences(this, todos)

                todos=SharedPreferences.getArrayListFromSharedPreferences(this)

                displayTodos(todos)

            } else {
                Log.e("ViewActivity", "Invalid position: $position")
            }



        }

        val iconLayoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.ALIGN_PARENT_END)
            addRule(RelativeLayout.ALIGN_PARENT_TOP)
            topMargin = 10.dpToPx()
            rightMargin = 16.dpToPx()
        }

        relativeLayout.addView(trashIcon, iconLayoutParams)

        return relativeLayout
    }


    private fun setTrashIconClickListener() {
        for ((index, _) in todos.withIndex()) {
            val trashIcon = createTrashIcon(index)
            trashIcon.setOnClickListener {
                if (index >= 0 && index < todos.size) {
                    todos.removeAt(index)
                    SharedPreferences.saveArrayListToSharedPreferences(this, todos)
                    todos = SharedPreferences.getArrayListFromSharedPreferences(this)
                    displayTodos(todos)
                } else {
                    Log.e("ViewActivity", "Invalid position: $index")
                }
            }
        }
    }

    private fun createTrashIcon(position: Int): ImageView {
        val imageView = ImageView(this)
        val source = ImageDecoder.createSource(resources, R.drawable.delete)
        val drawableBitmap = ImageDecoder.decodeBitmap(source)
        val resizedBitmap = Bitmap.createScaledBitmap(drawableBitmap, 100, 100, true)
        imageView.setImageBitmap(resizedBitmap)
        imageView.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
        imageView.tag = position



        return imageView
    }







    private fun createTextView(text: String, marginLeft: Int, marginTop: Int): TextView {
        val textView = TextView(this)
        textView.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            leftMargin = marginLeft.dpToPx()
            topMargin = marginTop.dpToPx()
            bottomMargin = 20.dpToPx()
            rightMargin = 50.dpToPx()
        }
        textView.text = text
        textView.setTextColor(Color.WHITE)
        return textView
    }

    private fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density + 0.5f).toInt()
    }
}
