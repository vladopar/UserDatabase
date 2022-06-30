package com.example.userdatabase.viewmodel

import androidx.lifecycle.*
import com.example.userdatabase.data.UserDao
import com.example.userdatabase.model.User
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class UserViewModel(
    private val userDao: UserDao
): ViewModel() {

    val allUsers: LiveData<List<User>> = userDao.getUsers().asLiveData()

    fun getUser(id: Int): LiveData<User> {
        return userDao.getUser(id).asLiveData()
    }

    fun addUser(
        firstName: String,
        lastName: String
    ) {
        val user = User(
            firstName = firstName,
            lastName = lastName
        )
        viewModelScope.launch {
            userDao.insert(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userDao.delete(user)
        }
    }

    fun updateUser(
        id: Int,
        firstName: String,
        lastName: String
    ) {
        val user = User(
            id = id,
            firstName = firstName,
            lastName = lastName
        )
        viewModelScope.launch {
            userDao.update(user)
        }
    }

    fun isValidEntry(firstname: String, lastName: String): Boolean {
        return firstname.isNotBlank() && lastName.isNotBlank()
    }
}

class UserViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}