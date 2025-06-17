package com.example.getitdone.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_list")
data class TaskList(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_list_id")
    val id: Int,

    val name: String,


    )
