package com.example.customviewdemo

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
class PaintingViewModel (private val repository: PaintingRepository) : ViewModel() {

    private val _bitmap: MutableLiveData<Bitmap??> = MutableLiveData(Bitmap.createBitmap(1440, 2990, Bitmap.Config.ARGB_8888))
    var bitmap = _bitmap as LiveData<Bitmap>
    private val _penColor: MutableLiveData<Color> = MutableLiveData(Color.valueOf(1f, 1f, 0f))
    val penColor = _penColor as LiveData<Color>
    val allPics: LiveData<List<PaintingData>> = repository.allPics
    var _paintingName  : MutableLiveData<String> = MutableLiveData("")
    val paintingName = _paintingName as LiveData<String>

    fun saveImage(pName: String) {

    Log.d("saveImage", "inside save image")
    //designed for ease of view author, so take string and bitmap^^^
    //repository actually stores it, builds an entity object that goes into the database and calls a dao method
        viewModelScope.launch{
            repository.addPainting(pName)
            Log.d("saveImage", "after somethign?")
        }
    }

    fun loadPainting(bitmap: Bitmap, filename: String) {
        Log.d("updateBitmap", "inside update bitmap")
        _bitmap.value = bitmap
        _paintingName.value = filename
    }


    public fun clearBitmap() {
        _paintingName.value = ""
        _bitmap.value?.eraseColor(Color.WHITE)
    }

    fun removePainting(filename: String) {
        viewModelScope.launch {
            repository.removePainting(filename)
        }
    }

    suspend fun getKtorFiles(): List<Map<String, String>> {
        Log.i("getKtorFiles", "inside getKtorFiles")
        val client = HttpClient(CIO)

        val httpResponse: HttpResponse = client.get("http://10.0.2.2:8080/paint")
        val response = httpResponse.bodyAsText()
        val jsons = Json.parseToJsonElement(response).jsonArray

//        Log.i("jsons", jsons.toString())

        val result = jsons.map { jsonObject ->
            val bitmap = jsonObject.jsonObject["imagePath"]?.jsonPrimitive?.content ?: ""
            val userId = jsonObject.jsonObject["UserId"]?.jsonPrimitive?.content ?: ""
            mapOf("bitmap" to bitmap, "userId" to userId)
        }
        client.close()

        Log.i("jsons", result.toString())

        return result
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








