package com.example.customviewdemo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import com.example.customviewdemo.databinding.FragmentDrawBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID

//The DrawFragment contains all the pen buttons for shape, size and color. Each button has a listener
//attached to it which updates values in the onTouchEvent() method in the view.
class DrawFragment : Fragment() {

    val viewModel: PaintingViewModel by activityViewModels { PaintingViewModelFactory((requireActivity().application as PaintingApplication).paintingRepository) }

    //Phase 3 - This x and y will be used to store collected gravity values sensor is collecting
    private var x = mutableFloatStateOf(0.0f)
    private var y = mutableFloatStateOf(0.0f)

    //The onCreateView() method stores all the pen button listeners.
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        val binding = FragmentDrawBinding.inflate(inflater)

        //May need to add one line here

        viewModel.bitmap.observe(viewLifecycleOwner) {
            binding.customView.setBitMap(it)
        }
        //Blue button listener
        binding.blueButton.setOnClickListener {
            //change paint in Custom View to blue
            binding.customView.setColor(-16776961)
            Log.d("blueTest", "inside blue click listener")
        }
        //Green button listener
        binding.greenButton.setOnClickListener {
            binding.customView.setColor(-16711936)
        }
        //Red button listener
        binding.redButton.setOnClickListener {
            binding.customView.setColor(-65536)
        }

        //Round pen button listener
        binding.roundButton.setOnClickListener {
            binding.customView.shape = CustomView.Shape.circle
        }

        //Square pen button listener
        binding.squareButton.setOnClickListener {
            binding.customView.shape = CustomView.Shape.rectangle
        }

        //Increase pen size button listener
        binding.increaseSizeButton.setOnClickListener {
            binding.customView.offset += 10F
        }

        //Decrease pen size button listener
        binding.decreaseSizeButton.setOnClickListener {
            if (binding.customView.offset < 15F) {
                binding.customView.offset = 15F
            } else {
                binding.customView.offset -= 10F
            }
        }

        //Save painting button listener
        binding.savePaintingButton.setOnClickListener {
            var isInDatabase = false
            var pName = ""
            if (viewModel.paintingName.value == "") {
                pName = UUID.randomUUID().toString()
            } else {
                pName = viewModel.paintingName.value.toString()
                isInDatabase = true
            }

            Log.d("DrawFragment", "inside save button")

            //Call a method that saves current painting
            val stream = ByteArrayOutputStream()
            viewModel.bitmap.value?.compress(Bitmap.CompressFormat.PNG, 100, stream)
            context?.openFileOutput(pName, Context.MODE_PRIVATE).use {
                it?.write(stream.toByteArray())
            }

            runBlocking {
                withContext(Dispatchers.IO) {
                    stream.close()
                }
            }
            if (!isInDatabase) {
                viewModel.saveImage(pName)
            }
            Log.d("DrawFragment", "after saveImage")

        }

        //Phase 3 - Marble sensor mode click listener
        binding.gravityModeButton.setOnClickListener {
            Log.d("DrawFragement", "About to call initialize sensor.")
            binding.customView.initializeSensor()
            binding.customView.getGravData().asLiveData().observe(viewLifecycleOwner){

                if (it[0] > 0) {
                    x.floatValue -= 5

                }
                if (it[0] < 0) {
                    x.floatValue += 5
                }

                if (it[1] > 0) {
                    y.floatValue += 5
                }
                if (it[1] < 0) {
                    y.floatValue -= 5
                }
                if (y.floatValue < 0)
                    y.floatValue = 0f
                if (x.floatValue < 0)
                    x.floatValue = 0f
                if (y.floatValue > 2670)
                    y.floatValue = 2670f
                if (x.floatValue > 1374)
                    x.floatValue = 1374f

                binding.customView.moveMarble(x.floatValue, y.floatValue)
            }
        }
        return binding.root
    }
}