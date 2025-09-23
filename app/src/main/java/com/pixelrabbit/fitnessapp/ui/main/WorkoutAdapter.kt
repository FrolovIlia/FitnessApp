package com.pixelrabbit.fitnessapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pixelrabbit.fitnessapp.R
import com.pixelrabbit.fitnessapp.data.model.Workout

class WorkoutAdapter(
    private val onItemClick: (Workout) -> Unit
) : ListAdapter<Workout, WorkoutAdapter.WorkoutViewHolder>(WorkoutDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.workoutTitle)
        private val durationText: TextView = itemView.findViewById(R.id.workoutDuration)
        private val typeText: TextView = itemView.findViewById(R.id.workoutType)

        fun bind(workout: Workout) {
            titleText.text = workout.title
            durationText.text = workout.duration
            typeText.text = when (workout.type) {
                1 -> "Тренировка"
                2 -> "Эфир"
                3 -> "Комплекс"
                else -> "Неизвестно"
            }

            itemView.setOnClickListener {
                onItemClick(workout)
            }
        }
    }

    class WorkoutDiffCallback : DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Workout, newItem: Workout) = oldItem == newItem
    }
}
