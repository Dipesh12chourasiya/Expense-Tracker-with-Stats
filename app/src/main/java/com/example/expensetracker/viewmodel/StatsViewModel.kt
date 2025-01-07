package com.example.expensetracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.DeleteColumn
import com.example.expensetracker.Utils
import com.example.expensetracker.data.ExpenseDataBase
import com.example.expensetracker.data.dao.ExpenseDao
import com.example.expensetracker.data.model.ExpenseEntity
import com.example.expensetracker.data.model.ExpenseSummary
import com.github.mikephil.charting.data.Entry

class StatsViewModel(val dao: ExpenseDao) : ViewModel() {
    val entities = dao.getAllExpenseByDate()
    val topEntries = dao.getTopExpenses()

    suspend fun deleteExpense(expenseEntity: ExpenseEntity) {
        dao.deleteExpense(expenseEntity)
    }

    fun getEntriesForChart(entries: List<ExpenseSummary>): List<Entry>{
        val list = mutableListOf<Entry>()
        for(entry in entries){
            val formattedDate = Utils.getMilliFromDate(entry.date)
            list.add(Entry(formattedDate.toFloat(), entry.total_amount.toFloat()))
        }
        return list
    }
}

class StatsViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StatsViewModel::class.java)){
            val dao = ExpenseDataBase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return StatsViewModel(dao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}