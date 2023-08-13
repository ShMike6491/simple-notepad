package com.sh.michael.simple_notepad.feature_files.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sh.michael.simple_notepad.feature_files.data.FILES_TABLE_NAME
import com.sh.michael.simple_notepad.feature_files.domain.model.IFile

@Entity(tableName = FILES_TABLE_NAME)
data class RoomFile(

    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String = "",

    @ColumnInfo(name = "priority")
    override val priority: Int,

    @ColumnInfo(name = "title")
    override val title: String,

    @ColumnInfo(name = "isPinned")
    override val isPinned: Boolean
) : IFile
