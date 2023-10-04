package com.example.customviewdemo

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
class PaintingViewModel (private val repository: PaintingRepository) : ViewModel() {

    private val _bitmap: MutableLiveData<Bitmap> = MutableLiveData(Bitmap.createBitmap(1440, 2990, Bitmap.Config.ARGB_8888))
    var bitmap = _bitmap as LiveData<Bitmap>
    private val _penColor: MutableLiveData<Color> = MutableLiveData(Color.valueOf(1f, 1f, 0f))
    val penColor = _penColor as LiveData<Color>
    val paintingName = ""
    val allPics: LiveData<List<PaintingData>> = repository.allPics

//    val currentPainting: LiveData<PaintingData> = repository.currentPainting
//
//    val allPaintings: LiveData<List<PaintingData>> = repository.allPaintings

//    fun checkPaintings(painting: Bitmap){
//        repository.checkPaintings(painting)
//    }
    fun saveImage(pName: String) {

    Log.d("saveImage", "inside save image")
    //designed for ease of view author, so take string and bitmap^^^
    //repository actually stores it, builds an entity object that goes into the database and calls a dao method
        viewModelScope.launch{
            repository.addPainting(pName)
            Log.d("saveImage", "after somethign?")
        }
    }

    fun updateBitmap(bitmap: Bitmap) {
        Log.d("updateBitmap", "inside update bitmap")
        _bitmap.value = bitmap
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
