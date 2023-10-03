package com.example.customviewdemo

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
class PaintingViewModel (private val repository: PaintingRepository) : ViewModel() {
    val currentPainting: LiveData<PaintingData> = repository.currentPainting

    val allPaintings: LiveData<List<PaintingData>> = repository.allPaintings

//    fun checkPaintings(painting: Bitmap){
//        repository.checkPaintings(painting)
//    }
    fun saveImage(bitmap: Bitmap, filename: String) {
    //designed for ease of view author, so take string and bitmap^^^
    //repository actually stores it, builds an entity object that goes into the database and calls a dao method
        viewModelScope.launch{
//            repository.addPainting(bitmap, filename)
        }
    }

}

// This factory class allows us to define custom constructors for the view model
class PaintingViewModelFactory(private val repository: PaintingRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaintingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PaintingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
