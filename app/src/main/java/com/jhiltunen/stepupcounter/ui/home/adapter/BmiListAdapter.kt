package com.jhiltunen.stepupcounter.ui.home.adapter

import android.R
import android.icu.text.DecimalFormat
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jhiltunen.stepupcounter.data.models.BodyMassIndex


class BmiListAdapter : RecyclerView.Adapter<BmiListAdapter.MyViewHolder>() {

    var bodyMassIndexes: List<BodyMassIndex> = emptyList<BodyMassIndex>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textViewTitle: TextView
        val textViewDescription: TextView

        init {
            textViewTitle = itemView.findViewById(R.id.text1)
            textViewDescription = itemView.findViewById(R.id.text2)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BmiListAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.simple_list_item_2, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bmi: BodyMassIndex = bodyMassIndexes[position]
        holder.textViewTitle.text = DecimalFormat("##.##").format(bmi.bodyMassIndex)
        holder.textViewDescription.text = SimpleDateFormat("dd.MM.yyyy").format(SimpleDateFormat("yyyy-MM-dd").parse(bmi.date))
    }

    override fun getItemCount(): Int {
        return bodyMassIndexes.size
    }

    fun setData(bodyMassIndexes: List<BodyMassIndex>) {
        this.bodyMassIndexes = bodyMassIndexes
        notifyDataSetChanged()
    }
}