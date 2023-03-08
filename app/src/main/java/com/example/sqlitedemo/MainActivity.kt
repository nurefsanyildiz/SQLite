package com.sqlitedemo

import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_update.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAdd.setOnClickListener { view ->
            addRecord(view)
        }
        setupListofDataIntoRecyclerView()
    }

    private fun setupListofDataIntoRecyclerView() {
        if (getItemsList().size > 0) {
            rvItemsList.visibility = View.VISIBLE
            tvNoRecordsAvailable.visibility = View.GONE
            rvItemsList.layoutManager = LinearLayoutManager(this)
            val itemAdapter = ItemAdapter(this, getItemsList())
            rvItemsList.adapter = itemAdapter
        } else {
            rvItemsList.visibility = View.GONE
            tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    private fun getItemsList(): ArrayList<EmpModelClass> {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        return databaseHandler.viewEmployee()
    }

    fun addRecord(view: View) {
        val name = etName.text.toString()
        val email = etEmailId.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if (!name.isEmpty() && !email.isEmpty()) {
            val status = databaseHandler.addEmployee(EmpModelClass(0, name, email))
            if (status > -1) {
                Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
                etName.text.clear()
                etEmailId.text.clear()
                setupListofDataIntoRecyclerView()
            }
        } else {
            Toast.makeText(applicationContext, "Name or Email cannot be blank", Toast.LENGTH_LONG).show()
        }
    }

    fun updateRecordDialog(empModelClass: EmpModelClass) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        updateDialog.setContentView(R.layout.dialog_update)
        updateDialog.etUpdateName.setText(empModelClass.name)
        updateDialog.etUpdateEmailId.setText(empModelClass.email)

        updateDialog.tvUpdate.setOnClickListener(View.OnClickListener {
            val name = updateDialog.etUpdateName.text.toString()
            val email = updateDialog.etUpdateEmailId.text.toString()
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            if (name.isNotEmpty() && email.isNotEmpty()) {
                val status = databaseHandler.updateEmployee(EmpModelClass(empModelClass.id, name, email))
                if (status > -1) {
                    Toast.makeText(applicationContext, "Record Updated.", Toast.LENGTH_LONG).show()
                    setupListofDataIntoRecyclerView()
                    updateDialog.dismiss()
                }
            } else {
                Toast.makeText(applicationContext, "Name or Email cannot be blank", Toast.LENGTH_LONG).show()
            }
        })
        updateDialog.tvCancel.setOnClickListener(View.OnClickListener {
            updateDialog.dismiss()
        })
        updateDialog.show()
    }

    fun deleteRecordAlertDialog(empModelClass: EmpModelClass) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setMessage("Are you sure you wants to delete ${empModelClass.name}.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, which ->
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            val status = databaseHandler.deleteEmployee(EmpModelClass(empModelClass.id, "", ""))
            if (status > -1) {
                Toast.makeText(applicationContext, "Record deleted successfully.", Toast.LENGTH_LONG).show()
                setupListofDataIntoRecyclerView()
            }
            dialogInterface.dismiss()
        }

        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}

