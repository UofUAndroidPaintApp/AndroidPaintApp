package com.example.customviewdemo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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


    //The onCreateView() method stores all the pen button listeners.
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        val binding = FragmentDrawBinding.inflate(inflater)

        viewModel.bitmap.observe(viewLifecycleOwner) {
            binding.customView.setBitMap(it)
        }
        //Blue button listener
        binding.blueButton.setOnClickListener {
            //change paint in Custom View to blue
            binding.customView.setColor(-16776961)
            Log.d("blueTest", "inside blue click listener")
        }
        //Purple button listener
        binding.greenButton.setOnClickListener {
            binding.customView.setColor(-16711936)
        }
        //Green button listener
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
        return binding.root

    }
}