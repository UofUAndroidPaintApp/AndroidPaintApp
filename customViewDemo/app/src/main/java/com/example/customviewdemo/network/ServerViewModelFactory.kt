package com.example.customviewdemo.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Ensures we only have one view model ??
//class ServerViewModelFactory(private val repository: ServerRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(ServerViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return ServerViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
