package com.example.jcasd.data.local

// step 10 create an event interface, rename it to your liking
sealed interface AppEvent {
    object SaveShow : AppEvent
    data class DeleteShow(val show: ShowEntity) : AppEvent
    object ShowDialog : AppEvent
    object DismissDialog : AppEvent
    data class SetTitle(val title: String) : AppEvent
    data class SetOtt(val ott: String) : AppEvent
    data class SetPriority(val priority: Int) : AppEvent
}