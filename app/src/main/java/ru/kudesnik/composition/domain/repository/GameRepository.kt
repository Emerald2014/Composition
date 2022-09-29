package ru.kudesnik.composition.domain.repository

import ru.kudesnik.composition.domain.entity.GameSettings
import ru.kudesnik.composition.domain.entity.Level
import ru.kudesnik.composition.domain.entity.Question

interface GameRepository {
    fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int
    ): Question

    fun getGameSettings(level: Level): GameSettings
}