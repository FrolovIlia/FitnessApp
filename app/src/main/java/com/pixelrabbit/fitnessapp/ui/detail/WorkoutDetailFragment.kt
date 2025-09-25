package com.pixelrabbit.fitnessapp.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.pixelrabbit.fitnessapp.data.model.VideoWorkout
import com.pixelrabbit.fitnessapp.data.model.Workout
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
    private var workout: Workout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workoutId = arguments?.getInt("workout_id") ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            workout = viewModel.getWorkoutById(workoutId)
            workout?.let {
                binding.workoutTitle.text = it.title
                binding.workoutDescription.text = it.description ?: "Описание отсутствует"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.video.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorText.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.visibility = View.GONE
                        setupVideo(state.data)
                    }
                    is UiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.visibility = View.VISIBLE
                        Log.e("WorkoutDetail", "Ошибка загрузки видео: ${state.message}")
                    }
                    is UiState.Empty -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorText.visibility = View.VISIBLE
                        binding.errorText.text = "Видео отсутствует"
                    }
                }
            }
        }

        viewModel.loadVideo(workoutId)
    }

    private fun setupVideo(video: VideoWorkout) {
        val fixedVideo = video.copy(
            link = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        )

        binding.videoDuration.text = "Длительность: ${fixedVideo.duration}"

        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player
        binding.playerView.useController = true
        binding.playerView.showController()

        val mediaItem = MediaItem.fromUri(fixedVideo.link.toUri())
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()

        Log.d("WorkoutDetail", "Video URL: ${fixedVideo.link}")
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
        Log.d("WorkoutDetail", "Player released")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
