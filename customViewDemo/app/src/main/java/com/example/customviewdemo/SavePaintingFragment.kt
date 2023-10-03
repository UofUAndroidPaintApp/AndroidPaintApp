package com.example.customviewdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SavePaintingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //We want to display all saved paintings in chronological order
        //We should be able to select any of the paintings -> that action takes us back to painting
        //screen, the saved bitmap should be displayed and we can continue editing it

        return inflater.inflate(R.layout.fragment_save_painting, container, false)
    }

}