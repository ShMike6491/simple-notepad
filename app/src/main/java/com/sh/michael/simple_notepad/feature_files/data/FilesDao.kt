package com.sh.michael.simple_notepad.feature_files.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sh.michael.simple_notepad.feature_files.data.model.RoomFile
import kotlinx.coroutines.flow.Flow

const val FILES_TABLE_NAME = "files_table"

@Dao
interface FilesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: RoomFile)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg notes: RoomFile)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notes: List<RoomFile>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(note: RoomFile)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(vararg notes: RoomFile)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(notes: List<RoomFile>)

    @Delete
    suspend fun delete(note: RoomFile)

    @Delete
    suspend fun delete(vararg notes: RoomFile)

    @Delete
    suspend fun delete(notes: List<RoomFile>)

    @Query("DELETE FROM $FILES_TABLE_NAME")
    suspend fun deleteAll()

    @Query("DELETE FROM $FILES_TABLE_NAME WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query(
        "SELECT * FROM $FILES_TABLE_NAME " +
            "WHERE id LIKE (:id) " +
            "LIMIT 1"
    )
    suspend fun getFileById(id: String): RoomFile?

    @Query("SELECT * FROM $FILES_TABLE_NAME")
    fun getAll(): Flow<List<RoomFile>>

    @Query("SELECT * FROM $FILES_TABLE_NAME ORDER BY priority DESC")
    fun getAllByPriority(): Flow<List<RoomFile>>

    @Query("SELECT MAX(priority) FROM $FILES_TABLE_NAME")
    fun getHighestPriority(): Int?
}