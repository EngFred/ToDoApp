package com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model

import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo

sealed class TodosListEvents {
    data class DeleteTodoClicked( val todo: Todo ) : TodosListEvents()
    data class UndoTodoDeleteClicked( val todo: Todo ) : TodosListEvents()
    data class TodosSortClicked( val todosOrder: TodosOrder ) : TodosListEvents()
    data class TodoCompletedClicked( val todo: Todo ) : TodosListEvents()
    data class TodoArchived( val todo: Todo ) : TodosListEvents()
}

sealed class TodosSortingDirection {
    data object AtoZ: TodosSortingDirection()
    data object ZtoA: TodosSortingDirection()
}

sealed class TodosOrder( val sortingDirection: TodosSortingDirection, val showAchieved: Boolean ) {

    class Title( sortingDirection: TodosSortingDirection, showAchieved: Boolean ) : TodosOrder(sortingDirection, showAchieved)
    class Time( sortingDirection: TodosSortingDirection, showAchieved: Boolean ) : TodosOrder(sortingDirection, showAchieved)
    class Completed( sortingDirection: TodosSortingDirection, showAchieved: Boolean ) : TodosOrder(sortingDirection, showAchieved)

    fun update( sortingDirection: TodosSortingDirection, showAchieved: Boolean ) : TodosOrder {
        return when(this) {
            is Title -> Title(sortingDirection, showAchieved)
            is Time -> Time(sortingDirection, showAchieved)
            is Completed -> Completed(sortingDirection, showAchieved)
        }
    }

}