package com.example.customviewdemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.customviewdemo.network.ServerApplication
import com.example.customviewdemo.network.ServerViewModel
import com.example.customviewdemo.network.ServerViewModelFactory
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.onDownload
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readBytes
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.util.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.io.ByteArrayOutputStream
import java.io.File

@Serializable
data class SharedBitmapData(val bitmap: String, val userID: String)

class SaveToKtorFragment : Fragment() {

    val vm: PaintingViewModel by activityViewModels { PaintingViewModelFactory((requireActivity().application as PaintingApplication).paintingRepository) }

    //Declare a serverViewModel and work on sending  images to the server
    val serverViewModel: ServerViewModel by activityViewModels { ServerViewModelFactory((requireActivity().application as ServerApplication).serverRepository) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    SavePaintingCompose(vm, context)
                }
            }
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun SavePaintingCompose(vm: PaintingViewModel, context: Context) {
        val editIcon = painterResource(id = R.drawable.ic_launcher_edit)
        val deleteIcon: Painter = painterResource(id = R.drawable.ic_launcher_delete_icon)
        val allPics by vm.allPics.observeAsState()
        val list = allPics ?: listOf()
        val client = HttpClient()

        //var pics by remember {mutableStateOf<Array<receivedData>?>(null)}
        var test = emptyArray<receivedData>()
        var allPaths by remember { mutableStateOf<List<String>>(emptyList()) }
        var bitmaps = mutableListOf<Bitmap>()
        //var bitmaps = arrayOf<Bitmap>()
        //todo change runblocking to the other thing
        runBlocking {

            val httpResponse: String = client.get("http://10.0.2.2:8080/paint").bodyAsText()
            val gson = Gson()
            val drawingDataList = gson.fromJson(httpResponse, Array<receivedData>::class.java)
            var allPaintFiles = emptyArray<File>().toMutableList()
            //Log.i("saveToKTor", gson.toJson(drawingDataList[0]))
        }

        lifecycleScope.launch {
            //Get all the filenames for paintings stored in server
            val allEntries = vm.getKtorFiles()
            //Go through each filename and actually request the file
            for (entry in allEntries) {
                val path = entry["bitmap"]
                val client = HttpClient(CIO)
                val response: HttpResponse =
                    client.get("http://10.0.2.2:8080/paint/" + path + "/getImage")
                if (response.status.isSuccess()) {
                    val bytes = response.readBytes()
                    val bitmapResponse = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    bitmaps.add(bitmapResponse)
                    Log.i("bitmaps", bitmaps.size.toString())
                        // bitmaps.add(bitmapResponse.copy(Bitmap.Config.ARGB_8888, true))
                    Log.i("succeed?", "inside success")
                } else {
                    Log.i("succeed?", "DIDN'T SUCCEED")
                }

            }
            client.close()
        }
        //Log.i("bitmaps", bitmaps.size.toString())
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            // content padding
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            content =
            {
                items(bitmaps.size) { index ->
                    val imageBitmap = bitmaps[index]


                    Card(
                        onClick = {
                            // navigateToDrawFragment(list[index].filename, bmp_Copy)
                            Log.d("filename", list[index].filename)
                        },
                        backgroundColor = Color.LightGray,
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        elevation = 8.dp,
                    ) {
                        Column {
                            Image(
                                bitmap = imageBitmap.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(150.dp)
                                    .fillMaxWidth()
                            )
                            Text(
                                text = list[index].timestamp.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(5.dp)
                            )

                            OutlinedButton(onClick = {

                                // /data/data/com.example.customviewdemo/files/319000ce-a644-472d-b152-57cbe9030cbb

                                //get bitmap

                                //convert bitmap to a file

                                val filepath: String =
                                    context.filesDir.toString() + "/" + list[index].filename + ".png"

                                // TODO share the filepath on the server
                                startFileShareIntent(filepath)


                            }) {
                                Text("Share")
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {

                                IconButton(
                                    onClick = {
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        painter = editIcon,
                                        contentDescription = "Edit Icon"
                                    )
                                }

                                Button(onClick = {
                                    val client = HttpClient(CIO)


                                    val filepath: String =
                                        context.filesDir.toString() + "/" + list[index].filename + ".png"

                                    lifecycleScope.launch {
                                        Log.i("serverPost", "inside post button clickable")
                                        val response: HttpResponse =
                                            client.post("http://10.0.2.2:8080/paint/create") {
                                                setBody(
                                                    MultiPartFormDataContent(
                                                        formData {
                                                            append(
                                                                "image",
                                                                File(filepath).readBytes(),
                                                                Headers.build {
                                                                    append(
                                                                        HttpHeaders.ContentType,
                                                                        "image/png"
                                                                    )
                                                                    append(
                                                                        HttpHeaders.ContentDisposition,
                                                                        "filename=" + list[index].filename + ".png"
                                                                    )
                                                                })
                                                        },
                                                        boundary = "WebAppBoundary"
                                                    )
                                                )
                                                onUpload { bytesSentTotal, contentLength ->
                                                    println("Sent $bytesSentTotal bytes from $contentLength")
                                                }
                                            }
                                    }
                                    Log.i("serverPost", "we're AFTER?? the posting maybe it worked")


                                }) {
                                    Text(text = "Share to Server")

                                }

                                IconButton(
                                    onClick = {
                                        vm.removePainting(list[index].filename)
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        painter = deleteIcon,
                                        contentDescription = "Delete Icon"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }

//    fun navigateToDrawFragment(filename: String, bitmap: Bitmap) {
//        Log.e("Button", "mu button thing works")
//        val drawFragment = DrawFragment()
//        val transaction = requireActivity().supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.fragmentContainerViewID, drawFragment, "load_tag")
//        transaction.addToBackStack(null)
//        vm.loadPainting(bitmap, filename)
//        transaction.commit()
//    }


    fun startFileShareIntent(filePath: String) { // pass the file path where the actual file is located.
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type =
                "*/*"  // "*/*" will accepts all types of files, if you want specific then change it on your need.
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            putExtra(
                Intent.EXTRA_SUBJECT,
                "Sharing file from the AppName"
            )
            putExtra(
                Intent.EXTRA_TEXT,
                "Sharing file from the AppName with some description"
            )
            val fileURI = FileProvider.getUriForFile(
                requireContext(), requireContext().packageName + ".provider",
                File(filePath)
            )
            putExtra(Intent.EXTRA_STREAM, fileURI)
        }
        startActivity(shareIntent)
    }

}