package com.example.customviewdemo

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.customviewdemo.network.ServerApplication
import com.example.customviewdemo.network.ServerRepository
import com.example.customviewdemo.network.ServerViewModel
import com.example.customviewdemo.network.ServerViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import com.example.customviewdemo.network.ServerService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.client.request.*
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.statement.*
import io.ktor.http.*


class SavePaintingFragment : Fragment() {

    val vm: PaintingViewModel by activityViewModels { PaintingViewModelFactory((requireActivity().application as PaintingApplication).paintingRepository) }

    // TODO declare a serverViewModel and work on sending  images to the server
    val serverViewModel: ServerViewModel by activityViewModels { ServerViewModelFactory( (requireActivity().application as ServerApplication).serverRepository) }

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

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun SavePaintingCompose(vm: PaintingViewModel, context: Context) {

        val editIcon = painterResource(id = R.drawable.ic_launcher_edit)
        val deleteIcon: Painter = painterResource(id = R.drawable.ic_launcher_delete_icon)

        val allPics by vm.allPics.observeAsState()
        val list = allPics ?: listOf()


        // Ktor stuffss


        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            // content padding
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            content = {
                items(list.size) { index ->
                    val file = File(context?.filesDir, list[index].filename).readBytes()
                    val bitmap = BitmapFactory.decodeByteArray(file, 0, file.size)
                    val bmp_Copy: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                    val imageBitmap = bitmap.asImageBitmap()
                    val cardmodifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            //This is where we'd add listener to open saved painting
                            //findNavController().navigate(R.id.action_savePaintingFragment_to_drawFragment)
                        }

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
                                bitmap = imageBitmap,
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
                                saveImage(bitmap, list[index].filename + ".png")

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
                                        navigateToDrawFragment(list[index].filename, bmp_Copy)
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

                                    saveImage(bitmap, list[index].filename + ".png")


                                    val filepath: String =
                                        context.filesDir.toString() + "/" + list[index].filename + ".png"

                                    lifecycleScope.launch {
                                        Log.i("serverPost", "inside post button clickable")
                                        val response: HttpResponse = client.post("http://10.0.2.2:8080/paint/create") {
                                            setBody(
                                                MultiPartFormDataContent(
                                                formData {
                                                    append("image", File(filepath).readBytes(), Headers.build {
                                                        append(HttpHeaders.ContentType, "image/png")
                                                        append(HttpHeaders.ContentDisposition, "filename=" + list[index].filename+".png")
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
                                    //Text(text="Server", fontsize=10.sp)
                                    Text(text = "Upload", fontSize = 10.sp)

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

    fun navigateToDrawFragment(filename: String, bitmap: Bitmap) {
        Log.e("Button", "mu button thing works")
        val drawFragment = DrawFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerViewID, drawFragment, "load_tag")
        transaction.addToBackStack(null)
        vm.loadPainting(bitmap, filename)
        transaction.commit()
    }

    fun saveImage(bitmap: Bitmap, fileName: String) {
        lifecycleScope.launch {
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
            context?.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                it?.write(bos.toByteArray())
            }
            withContext(Dispatchers.IO) {
                bos.close()
            }
        }
    }

    fun startFileShareIntent(filePath: String) { // pass the file path where the actual file is located.
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type =
                "*/*"  // "*/*" will accepts all types of files, if you want specific then change it on your need.
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            putExtra(
                Intent.EXTRA_SUBJECT,
                "Beautiful art I made"
            )
            putExtra(
                Intent.EXTRA_TEXT,
                "Check it out"
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




