package com.pixelrabbit.fitnessapp.ui.detail

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.pixelrabbit.fitnessapp.databinding.FragmentWorkoutDetailBinding
import com.pixelrabbit.fitnessapp.utils.UiState

class WorkoutDetailActivity : AppCompatActivity() {

    private lateinit var binding: FragmentWorkoutDetailBinding
    private val viewModel: VideoPlayerViewModel by viewModels()
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentWorkoutDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val workoutId = intent.getIntExtra("workout_id", 0)

        viewModel.video.observe(this) { state ->
            when(state) {
                is UiState.Loading -> {
                    binding.progressBar.visibility = android.view.View.VISIBLE
                }
                is UiState.Success -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    setupPlayer(state.data.link)
                }
                is UiState.Error -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    // Можно показать Toast
                }
                else -> {}
            }
        }

        viewModel.loadVideo(workoutId)
    }

    private fun setupPlayer(videoUrl: String) {
        player = ExoPlayer.Builder(this).build()
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
}
