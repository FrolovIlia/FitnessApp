package com.pixelrabbit.fitnessapp.ui.detail

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

        val workoutId = intent.getIntExtra("workout_id", -1)
        if (workoutId == -1) {
            Toast.makeText(this, "Ошибка: нет ID тренировки", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewModel.video.observe(this) { state ->
            when (state) {
                is UiState.Loading -> binding.progressBar.visibility = android.view.View.VISIBLE
                is UiState.Success -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    setupPlayer(state.data.link)
                }
                is UiState.Error -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    Toast.makeText(this, "Ошибка загрузки видео: ${state.message}", Toast.LENGTH_SHORT).show()
                }
                is UiState.Empty -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    Toast.makeText(this, "Видео не найдено", Toast.LENGTH_SHORT).show()
                }
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
