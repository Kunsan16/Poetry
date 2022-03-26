package com.kunsan.poetry.page

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.*
import com.kunsan.poetry.PoetryRepository
import com.kunsan.poetry.data.PoetryResponse
import kotlinx.coroutines.flow.*
import java.io.IOException

class MainViewModel(private val dataStore: DataStore<Preferences>) : ViewModel() {


    private object PreferencesKeys {
        val USER_TOKEN = stringPreferencesKey("user_token")
    }

    var token = MutableLiveData<String>()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()


    fun openDialog(){
        _showDialog.value = true
    }

    fun onDialogConfirm(){
        onDialogDismiss()
    }

    fun onDialogDismiss() {
        _showDialog.value = false
    }

    /**
     * 检查本地token是否存在
     */
    suspend fun findLocalToken() {
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val localToken = preferences[PreferencesKeys.USER_TOKEN] ?: ""
                if (localToken.isEmpty()) {
                    obtainToken()
                } else {
                    token.postValue(localToken)
                }
            }.collect {

            }
    }

    /**
     * 获取token并缓存本地
     * token永久有效
     */
    private suspend fun obtainToken() = PoetryRepository.token()
        .collect {
            saveToken(it.data)
        }


    /**
     * 请求诗词数据
     */
    val poetry: LiveData<PoetryResponse> = token.switchMap {
        PoetryRepository.poetry(it).asLiveData()
    }


    private suspend fun saveToken(value: String) {
        dataStore.edit { mutablePreferences ->
            token.postValue(value)
            mutablePreferences[PreferencesKeys.USER_TOKEN] = value
        }
    }

}