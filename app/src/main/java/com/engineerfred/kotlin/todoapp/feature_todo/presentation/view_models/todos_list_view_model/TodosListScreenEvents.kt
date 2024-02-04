package com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model

import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo

sealed class TodosListEvents {
    data class DeleteTodoClicked( val todo: Todo ) : TodosListEvents()
    data class UndoTodoDeleteClicked( val todo: Todo ) : TodosListEvents()
    data class TodosSortClicked( val todosOrder: TodosOrder ) : TodosListEvents()
    data class TodoCompletedClicked( val todo: Todo ) : TodosListEvents()
    data class TodoPrioritized(val todo: Todo ) : TodosListEvents()
    data object OnThemeChanged: TodosListEvents()
    data object RetryClicked: TodosListEvents()
}

sealed class TodosSortingDirection {
    data object AtoZ: TodosSortingDirection()
    data object ZtoA: TodosSortingDirection()
}

sealed class TodosOrder( val sortingDirection: TodosSortingDirection, val showPrioritized: Boolean ) {

    class Title(sortingDirection: TodosSortingDirection, showPrioritized: Boolean ) : TodosOrder(sortingDirection, showPrioritized)
    class Time(sortingDirection: TodosSortingDirection, showPrioritized: Boolean ) : TodosOrder(sortingDirection, showPrioritized)
    class Completed(sortingDirection: TodosSortingDirection, showPrioritized: Boolean ) : TodosOrder(sortingDirection, showPrioritized)

    fun update(sortingDirection: TodosSortingDirection, showPrioritized: Boolean ) : TodosOrder {
        return when(this) {
            is Title -> Title(sortingDirection, showPrioritized)
            is Time -> Time(sortingDirection, showPrioritized)
            is Completed -> Completed(sortingDirection, showPrioritized)
        }
    }

}