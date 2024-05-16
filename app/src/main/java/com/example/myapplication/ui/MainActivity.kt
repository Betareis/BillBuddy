package com.example.myapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.addTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    /*private lateinit var transactionList: ListView
    private lateinit var addTransactions: FloatingActionButton
    private lateinit var transactions: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var bottomBar: MaterialToolbar*/

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Groups())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){

                R.id.groups -> replaceFragment(Groups())
                R.id.profile -> replaceFragment(Profile())
                R.id.more -> replaceFragment(More())

            }
            true

        }

        /*transactionList = findViewById(R.id.transactionList)
        addTransaction = findViewById(R.id.addTransactionButton)
        transactions = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, transactions)
        //bottomBar = findViewById(R.id.bottomBar)

        transactionList.adapter = adapter

        addTransaction.setOnClickListener {
            Intent intent = Intent(this, addTransaction.class)
            startActivity(intent)
        }
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
        }*/

    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}