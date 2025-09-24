package com.pixelrabbit.fitnessapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pixelrabbit.fitnessapp.data.model.Workout
import com.pixelrabbit.fitnessapp.data.model.typeName
import com.pixelrabbit.fitnessapp.databinding.ItemWorkoutBinding

class WorkoutAdapter(
    private val onClick: (Workout) -> Unit
) : ListAdapter<Workout, WorkoutAdapter.WorkoutViewHolder>(DiffCallback()) {

    class WorkoutViewHolder(private val binding: ItemWorkoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(workout: Workout, onClick: (Workout) -> Unit) {
            binding.title.text = workout.title
            binding.type.text = workout.typeName()
            binding.duration.text = workout.duration
            binding.description.text = workout.description ?: "Описание отсутствует"

            binding.root.setOnClickListener { onClick(workout) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = ItemWorkoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }

    class DiffCallback : DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Workout, newItem: Workout) = oldItem == newItem
    }
}
