package com.sh.michael.simple_notepad.feature_notes.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sh.michael.simple_notepad.feature_notes.data.NOTES_TABLE_NAME
import com.sh.michael.simple_notepad.feature_notes.domain.model.BackgroundColor
import com.sh.michael.simple_notepad.feature_notes.domain.model.IStickyNote

@Entity(tableName = NOTES_TABLE_NAME)
data class RoomStickyNote(

    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String = "",

    @ColumnInfo(name = "priority")
    override val priority: Int,

    @ColumnInfo(name = "background")
    override val backgroundColor: BackgroundColor,

    @ColumnInfo(name = "text")
    override val noteText: String,
) : IStickyNote

















