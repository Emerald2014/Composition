package ru.kudesnik.composition.ui

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.kudesnik.composition.data.GameRepositoryImpl
import ru.kudesnik.composition.domain.entity.GameSettings
import ru.kudesnik.composition.domain.entity.Level
import ru.kudesnik.composition.domain.usecases.GenerateQuestionUseCase
import ru.kudesnik.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel : ViewModel() {

    private lateinit var gameSettings: GameSettings
    private lateinit var level: Level

    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private val _formattedtime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedtime

    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        this.gameSettings = getGameSettingsUseCase(level)
    }

    private fun startTimer() {
        val timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedtime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }

        }
    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes - SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun finishGame() {

    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }
}