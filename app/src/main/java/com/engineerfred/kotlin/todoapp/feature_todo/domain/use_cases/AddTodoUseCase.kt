package com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases

import com.engineerfred.kotlin.todoapp.feature_todo.data.local.TodoEntity
import com.engineerfred.kotlin.todoapp.feature_todo.domain.repository.TasksRepository
import javax.inject.Inject

class AddTodoUseCase @Inject constructor(
    private val tasksRepository: TasksRepository
) {
    fun invoke( todoEntity: TodoEntity ) = tasksRepository.addTodo(todoEntity)
}