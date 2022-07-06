package com.example.userdatabase.data

import androidx.room.*
import com.example.userdatabase.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * from user_database")
    fun getUsers(): Flow<List<User>>

    @Query("SELECT * from user_database WHERE id = :id")
    fun getUser(id: Int): Flow<User>

    @Query("DELETE FROM user_database")
    suspend fun deleteAll()
}