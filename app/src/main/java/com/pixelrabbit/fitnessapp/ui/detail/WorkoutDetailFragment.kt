package com.pixelrabbit.fitnessapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.pixelrabbit.fitnessapp.databinding.FragmentWorkoutDetailBinding
import com.pixelrabbit.fitnessapp.utils.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WorkoutDetailFragment : Fragment() {

    private var _binding: FragmentWorkoutDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VideoPlayerViewModel by viewModels()
    private var player: ExoPlayer? = null
    private var workoutId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workoutId = arguments?.getInt("workout_id") ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.video.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        setupPlayer(state.data.link)
                        binding.tvWorkoutTitle.text = state.data.id.toString() // Можно заменить на реальный title
                        binding.tvWorkoutDuration.text = state.data.duration
                    }
                    is UiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvWorkoutTitle.text = "Ошибка загрузки видео"
                    }
                    is UiState.Empty -> binding.progressBar.visibility = View.GONE
                }
            }
        }

        viewModel.loadVideo(workoutId)
    }

    private fun setupPlayer(videoUrl: String) {
        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player
        binding.playerView.useController = true

        val mediaItem = MediaItem.fromUri(videoUrl)
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
