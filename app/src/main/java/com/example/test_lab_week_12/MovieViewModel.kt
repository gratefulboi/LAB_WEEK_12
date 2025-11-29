package com.example.test_lab_week_12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    init {
        fetchPopularMovies()
    }

    // define StateFlow untuk menggantikan LiveData
    // StateFlow itu observable Flow yang mengeluarkan state updates ke collectors
    // MutableStateFlow adalah StateFlow yang bisa dichange valuenya

    private val _popularMovies = MutableStateFlow(
        emptyList<Movie>()
    )

    val popularMovies: StateFlow<List<Movie>> = _popularMovies

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    // fetch movie dari API
    private fun fetchPopularMovies() {
        // Jalankan coroutine di viewModelScope
        // Dispatcher.io berarti coroutine akan berjalan di shared pool threads
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.fetchMovies().catch {
                // catch terminal operator yang catch exceptions dari Flow
                _error.value = "An exception occured: ${it.message}"
            }.collect {
                // collect terminal operator yang collect value dari flow
                _popularMovies.value = it
            }
        }
    }
}