package com.example.customviewdemo
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import java.io.File

class SavePaintingFragment : Fragment() {

    val vm: PaintingViewModel by activityViewModels { PaintingViewModelFactory((requireActivity().application as PaintingApplication).paintingRepository) }

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

        val allPics by vm.allPics.observeAsState()
        val list = allPics ?: listOf()
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
                            navigateToDrawFragment(list[index].filename, bmp_Copy)
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
                                    .size(200.dp)
                                    .fillMaxWidth()
                            )
                            Text(
                                text = list[index].filename,
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp,
                                color = Color(0xFFFFFFFF),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
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

        vm.updateBitmap(bitmap)

        transaction.commit()
    }


}



