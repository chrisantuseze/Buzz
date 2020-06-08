package com.echrisantus.buzz.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genre")
data class Genre(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,

    @ColumnInfo(name = "name")
    var name: String = ""
)