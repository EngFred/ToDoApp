package com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases

import com.engineerfred.kotlin.todoapp.feature_todo.domain.repository.PreferencesRepository
import javax.inject.Inject

class ChangeAppThemeUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke( isLightTheme: Boolean ) = preferencesRepository.saveTheme(isLightTheme)
}