package com.maxli.coursegpa

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.AndroidViewModel

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val allQuestions: LiveData<List<TrivialQuestion>>
    private val repository: TrivialQuestionRepository
    val searchResults: MutableLiveData<List<TrivialQuestion>>

    init {
        val questionDb = TrivialQuestionRoomDatabase.getInstance(application)
        val dao = questionDb.TrivialQuestionDao()
        repository = TrivialQuestionRepository(dao)

        allQuestions = repository.allQuestions
        searchResults = repository.searchResults

        preloadQuestions()
    }

    private fun preloadQuestions() {
        seedQuestions.forEach { question ->
            repository.insertQuestion(question)
        }
    }

    fun insertQuestion(question: TrivialQuestion) {
        repository.insertQuestion(question)
    }
}
