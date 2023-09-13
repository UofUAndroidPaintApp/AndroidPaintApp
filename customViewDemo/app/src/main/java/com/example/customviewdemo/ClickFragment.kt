package com.example.customviewdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.customviewdemo.databinding.FragmentClickBinding


class ClickFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentClickBinding.inflate(inflater, container, false)
        val viewModel : SimpleViewModel by activityViewModels()

        binding.clickMe.setOnClickListener {
            //viewModel.pickColor()
            //call makeBitMap, give width and height from this context
//            buttonFunction()
            findNavController().navigate(R.id.action_clickFragment_to_drawFragment)
        }

        return binding.root
    }


}