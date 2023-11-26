package com.Apic.apic.data


import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo WHERE date")
    fun getAll(): Flow<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Todo)

    @Delete
    suspend fun delete(entity: Todo)

    @Query("SELECT * FROM todo WHERE date = :selectedDate ORDER BY date DESC")
    fun getByDate(selectedDate: Long): List<Todo>

    @Query("SELECT * FROM todo WHERE date = :selectedDate ORDER BY date DESC")
    fun getTodosByDate(selectedDate: Long): Flow<List<Todo>>
}