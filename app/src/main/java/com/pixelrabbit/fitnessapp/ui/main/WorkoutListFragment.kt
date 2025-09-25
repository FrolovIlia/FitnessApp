package com.pixelrabbit.fitnessapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixelrabbit.fitnessapp.data.model.Workout
import com.pixelrabbit.fitnessapp.databinding.FragmentWorkoutListBinding
import com.pixelrabbit.fitnessapp.utils.UiState
import android.content.Context
import android.view.inputmethod.InputMethodManager

class WorkoutListFragment : Fragment() {

    private var _binding: FragmentWorkoutListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkoutViewModel by viewModels()
    private lateinit var adapter: WorkoutAdapter

    interface WorkoutSelectionListener {
        fun onWorkoutSelected(workoutId: Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = WorkoutAdapter { workout -> openDetail(workout) }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        setupSearch()
        setupFilter()

        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
            binding.searchView.requestFocus()
            val searchEditText = binding.searchView.findViewById<android.widget.EditText>(
                androidx.appcompat.R.id.search_src_text
            )
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT)
        }

        viewModel.workouts.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is UiState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is UiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(state.data)
                }
                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
                is UiState.Empty -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(emptyList())
                }
            }
        })

        viewModel.loadWorkouts()
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchByTitle(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchByTitle(newText.orEmpty())
                return true
            }
        })
    }

    private fun setupFilter() {
        val spinner: Spinner = binding.typeFilter
        val items = arrayOf("Все", "Тренировка", "Эфир", "Комплекс")
        val adapterSpinner = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterSpinner

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.filterByType(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun openDetail(workout: Workout) {
        (activity as? WorkoutSelectionListener)?.onWorkoutSelected(workout.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
