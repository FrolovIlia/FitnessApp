package com.pixelrabbit.fitnessapp.ui.detail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.pixelrabbit.fitnessapp.databinding.FragmentWorkoutDetailBinding
import com.pixelrabbit.fitnessapp.utils.UiState

class WorkoutDetailFragment : Fragment() {

    private var _binding: FragmentWorkoutDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VideoPlayerViewModel by viewModels()
    private var player: ExoPlayer? = null

    companion object {
        private const val ARG_WORKOUT_ID = "workout_id"

        fun newInstance(workoutId: Int) = WorkoutDetailFragment().apply {
            arguments = Bundle().apply { putInt(ARG_WORKOUT_ID, workoutId) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val workoutId = arguments?.getInt(ARG_WORKOUT_ID) ?: -1
        if (workoutId == -1) {
            Toast.makeText(requireContext(), "Ошибка: нет ID тренировки", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.video.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is UiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    setupPlayer(state.data.link)
                }
                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Ошибка загрузки видео: ${state.message}", Toast.LENGTH_SHORT).show()
                }
                is UiState.Empty -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Видео не найдено", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.loadVideo(workoutId)
    }

    private fun setupPlayer(videoUrl: String) {
        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player
        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
