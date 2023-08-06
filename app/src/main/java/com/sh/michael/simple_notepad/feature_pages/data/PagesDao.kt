package com.sh.michael.simple_notepad.feature_pages.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sh.michael.simple_notepad.feature_pages.data.model.RoomPage
import kotlinx.coroutines.flow.Flow

const val PAGES_TABLE_NAME = "pages_table"

@Dao
interface PagesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: RoomPage)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg notes: RoomPage)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notes: List<RoomPage>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(note: RoomPage)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(vararg notes: RoomPage)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(notes: List<RoomPage>)

    @Delete
    suspend fun delete(note: RoomPage)

    @Delete
    suspend fun delete(vararg notes: RoomPage)

    @Delete
    suspend fun delete(notes: List<RoomPage>)

    @Query("DELETE FROM $PAGES_TABLE_NAME")
    suspend fun deleteAll()

    @Query("DELETE FROM $PAGES_TABLE_NAME WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM $PAGES_TABLE_NAME WHERE fileId = :id")
    suspend fun deleteByFileId(id: String)

    @Query("SELECT * FROM $PAGES_TABLE_NAME")
    fun getAll(): Flow<List<RoomPage>>

    @Query(
        "SELECT * FROM $PAGES_TABLE_NAME " +
            "WHERE id LIKE (:id) " +
            "LIMIT 1"
    )
    suspend fun getPageById(id: String): RoomPage?

    @Query(
        "SELECT * FROM $PAGES_TABLE_NAME " +
            "WHERE fileId LIKE (:id) " +
            "LIMIT 1"
    )
    suspend fun getPageByFileId(id: String): RoomPage?

    @Query(
        "SELECT * FROM $PAGES_TABLE_NAME " +
            "WHERE fileId LIKE (:id) " +
            "LIMIT 1"
    )
    fun observePageOfFile(id: String): Flow<RoomPage?>

    @Query(
        "SELECT * FROM $PAGES_TABLE_NAME " +
            "WHERE id LIKE (:id) " +
            "LIMIT 1"
    )
    fun observePage(id: String): Flow<RoomPage?>
}