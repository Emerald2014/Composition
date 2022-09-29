package ru.kudesnik.composition.domain.usecases

import ru.kudesnik.composition.domain.entity.GameSettings
import ru.kudesnik.composition.domain.entity.Level
import ru.kudesnik.composition.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}