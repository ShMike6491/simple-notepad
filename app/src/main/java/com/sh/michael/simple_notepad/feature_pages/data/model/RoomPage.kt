package com.sh.michael.simple_notepad.feature_pages.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sh.michael.simple_notepad.feature_pages.data.PAGES_TABLE_NAME
import com.sh.michael.simple_notepad.feature_pages.domain.model.IPage

@Entity(tableName = PAGES_TABLE_NAME)
data class RoomPage (

    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,

    @ColumnInfo(name = "fileId")
    override val fileId: String,

    @ColumnInfo(name = "text")
    override val pageText: String?
) : IPage