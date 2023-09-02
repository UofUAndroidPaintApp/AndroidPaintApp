package com.example.customviewdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.customviewdemo.databinding.FragmentDrawBinding


class DrawFragment : Fragment() {

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
////        outState.putBundle("color", )
//        Log.d("stateTest", "onsaveinstancestate happened")
//    }



    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentDrawBinding.inflate(inflater)

//         add the unchristened
        val viewModel : SimpleViewModel by activityViewModels()

        viewModel.bitmap.observe(viewLifecycleOwner) {
            binding.customView.setBitMap(it)
        }


//        binding.customView.setOnTouchListener{
//            v: View,
//                event:MotionEvent
//                    ->
//            val x = event.x.toInt()
//            val y = event.y.toInt()
//            Log.d("touch", "x is...$x and y is...$y")
//
//            binding.customView.drawCircle(viewModel.color.value!!)
//            true
//        }
        return binding.root

    }
}