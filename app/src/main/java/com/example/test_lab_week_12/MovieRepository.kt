package com.example.test_lab_week_12

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.test_lab_week_12.api.MovieService
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn


class MovieRepository(private val movieService: MovieService) {
    private val apiKey = "56730abb6537da79713ce74cc21399b9"

    // LiveData yang berisi list movies
    private val movieLiveData = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = movieLiveData

    // LiveData yang berisi error message
    private val errorLiveData = MutableLiveData<String>()
    val error: LiveData<String>
        get() = errorLiveData

    // fetch movies from API
    // function ini return Flow dari movie objects
    fun fetchMovies(): Flow<List<Movie>> {
       return flow {
           // emit list dari popular movies dari API
           emit(movieService.getPopularMovies(apiKey).results)
           // Pake dispatcher.io untuk menjalankan coroutine ini di sharedpool of threads
       }.flowOn(Dispatchers.IO)
    }
}