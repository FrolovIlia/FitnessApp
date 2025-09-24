package com.pixelrabbit.fitnessapp.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.pixelrabbit.fitnessapp.databinding.FragmentWorkoutDetailBinding

class WorkoutDetailFragment : Fragment() {

    private var _binding: FragmentWorkoutDetailBinding? = null
    private val binding get() = _binding!!

    private var player: ExoPlayer? = null

    private val testVideoUrl =
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

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
        binding.progressBar.visibility = View.VISIBLE
        setupPlayer(testVideoUrl)
    }

    private fun setupPlayer(videoUrl: String) {
        Log.d("WorkoutDetail", "Start setup player with URL: $videoUrl")
        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player

        // Включаем контролы
        binding.playerView.useController = true
        binding.playerView.showController()

        val mediaItem = MediaItem.fromUri(videoUrl)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()

        binding.progressBar.visibility = View.GONE
        Log.d("WorkoutDetail", "Player prepared and playing")
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
