package com.example.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var transactionList: ListView
    private lateinit var addTransaction: FloatingActionButton
    private lateinit var transactions: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transactionList = findViewById(R.id.transactionList)
        addTransaction = findViewById(R.id.addTransactionButton)
        transactions = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, transactions)

        transactionList.adapter = adapter

        addTransaction.setOnClickListener{
            var builder = AlertDialog.Builder(this)
            builder.setTitle("Add transaction")

            var input = EditText(this)
            input.hint = "Add name"
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            builder.setPositiveButton("Add") { _, _ ->
                transactions.add(input.text.toString())
                adapter.notifyDataSetChanged()
            }

            builder.setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(applicationContext, "Cancelled", Toast.LENGTH_SHORT).show()
            }

            builder.show()
        }


    }
}