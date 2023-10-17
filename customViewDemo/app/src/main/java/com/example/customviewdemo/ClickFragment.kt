package com.example.customviewdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.customviewdemo.databinding.FragmentClickBinding


//The ClickFragment stores the click me button on the welcome screen which maps to the draw fragment
//via a click listener and navigation control.
class ClickFragment : Fragment() {

    //The onCreateView() method manages the navigation from the ClickFragment welcome screen to the
    //DrawFragment where the actually painting takes place.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentClickBinding.inflate(inflater, container, false)

        binding.startPainting.setOnClickListener {
            val viewModel: PaintingViewModel by activityViewModels { PaintingViewModelFactory((requireActivity().application as PaintingApplication).paintingRepository) }
            viewModel.clearBitmap()
            findNavController().navigate(R.id.action_clickFragment_to_drawFragment)
        }
        binding.savedPaintings.setOnClickListener {
            findNavController().navigate(R.id.action_clickFragment_to_savePaintingFragment)
        }
        return binding.root
    }

}