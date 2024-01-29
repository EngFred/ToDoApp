package com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases

import com.engineerfred.kotlin.todoapp.feature_todo.domain.repository.TodosRepository
import javax.inject.Inject

class GetAllTodosUseCase @Inject constructor(
    private val todosRepository: TodosRepository
) {
    operator fun invoke() = todosRepository.getAllTodosFromLocalCache()
}