package com.example.jcasd.data.local

// step 13 create a ui state data class, rename it to your liking
data class UiState(
    val shows: List<ShowEntity> = emptyList(),
    val title: String = "",
    val ott: String = "",
    val priority: Int = 0,
    val isDialogVisible: Boolean = false,
)
