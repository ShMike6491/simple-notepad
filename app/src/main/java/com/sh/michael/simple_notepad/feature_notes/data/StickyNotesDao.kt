package com.sh.michael.simple_notepad.feature_notes.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sh.michael.simple_notepad.feature_notes.data.model.RoomStickyNote
import kotlinx.coroutines.flow.Flow

const val NOTES_TABLE_NAME = "sticky_notes_table"

@Dao
interface StickyNotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: RoomStickyNote)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg notes: RoomStickyNote)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notes: List<RoomStickyNote>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(note: RoomStickyNote)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(vararg notes: RoomStickyNote)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(notes: List<RoomStickyNote>)

    @Delete
    suspend fun delete(note: RoomStickyNote)

    @Delete
    suspend fun delete(vararg notes: RoomStickyNote)

    @Delete
    suspend fun delete(notes: List<RoomStickyNote>)

    @Query("DELETE FROM $NOTES_TABLE_NAME")
    suspend fun deleteAll()

    @Query("DELETE FROM $NOTES_TABLE_NAME WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM $NOTES_TABLE_NAME")
    fun getAll(): Flow<List<RoomStickyNote>>

    @Query("SELECT * FROM $NOTES_TABLE_NAME ORDER BY priority ASC")
    fun getAllByPriority(): Flow<List<RoomStickyNote>>

    @Query(
        "SELECT * FROM $NOTES_TABLE_NAME " +
            "WHERE id LIKE (:id) " +
            "LIMIT 1"
    )
    suspend fun getNoteById(id: String): RoomStickyNote?
}