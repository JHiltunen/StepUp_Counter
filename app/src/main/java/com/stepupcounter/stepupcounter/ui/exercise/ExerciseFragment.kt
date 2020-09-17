package com.stepupcounter.stepupcounter.ui.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.stepupcounter.stepupcounter.R

class ExerciseFragment : Fragment() {

    private lateinit var exerciseViewModel: ExerciseViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        exerciseViewModel =
                ViewModelProvider(this).get(ExerciseViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_exercise, container, false)
        val textView: TextView = root.findViewById(R.id.text_exercise)
        exerciseViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }
}