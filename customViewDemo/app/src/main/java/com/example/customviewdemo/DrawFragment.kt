package com.example.customviewdemo

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.customviewdemo.databinding.FragmentClickBinding
import com.example.customviewdemo.databinding.FragmentDrawBinding


class DrawFragment : Fragment() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        val binding = FragmentDrawBinding.inflate(inflater)

        val viewModel : SimpleViewModel by activityViewModels()

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
            //binding.customView.setShape(Paint.Cap.ROUND)
            binding.customView.shape = CustomView.Shape.circle
        }

        //Sqaure pen button listener
        binding.squareButton.setOnClickListener {
            binding.customView.shape = CustomView.Shape.rectangle

            //binding.customView.setShape(Paint.Cap.SQUARE)
        }

        return binding.root

    }
}