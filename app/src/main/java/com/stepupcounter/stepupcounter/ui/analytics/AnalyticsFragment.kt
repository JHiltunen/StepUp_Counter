package com.stepupcounter.stepupcounter.ui.analytics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.stepupcounter.stepupcounter.R

class AnalyticsFragment : Fragment() {

    private lateinit var analyticsViewModel: AnalyticsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        analyticsViewModel =
                ViewModelProvider(this).get(AnalyticsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_analytics, container, false)
        val textView: TextView = root.findViewById(R.id.text_analytics)
        analyticsViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }
}