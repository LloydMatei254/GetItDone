package com.example.getitdone.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.getitdone.data.model.TaskList
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskListDao {

    @Insert
    suspend fun createTaskList(list: TaskList)

    @Query("SELECT * FROM task_list")
    fun getTaskLists(): Flow<List<TaskList>>

    @Update
    suspend fun updateTaskList(task: TaskList)

    @Delete
    suspend fun deleteTaskList(task: TaskList)


}