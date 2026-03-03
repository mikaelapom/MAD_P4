package com.mikaelap.myapplication


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class TrivialQuestionRepository(private val TrivialQuestionDao: TrivialQuestionDao) {

    val allQuestions: LiveData<List<TrivialQuestion>> = TrivialQuestionDao.getAllQuestions()
    val searchResults = MutableLiveData<List<TrivialQuestion>>()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertQuestion(newquestion: TrivialQuestion) {
        coroutineScope.launch(Dispatchers.IO) {
            TrivialQuestionDao.insertQuestion(newquestion)
        }
    }

    /*
    fun deleteCourse(id: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            trivialQuestionDao.deleteQuestion(id)
        }
    }


    fun smartSearch(courseName: String?, creditHour: String?, letterGrade: String?) {
        coroutineScope.launch(Dispatchers.Main) {
            val results = when {
                !courseName.isNullOrBlank() -> asyncFindByName("%$courseName%").await()
                !creditHour.isNullOrBlank() -> asyncFindByCreditHour(creditHour.toInt()).await()
                !letterGrade.isNullOrBlank() -> asyncFindByLetterGrade(letterGrade.uppercase()).await()
                else -> emptyList()
            }
            searchResults.value = results
        }
    }

     */

    private fun asyncFindByName(name: String): Deferred<List<TrivialQuestion>> =
        coroutineScope.async(Dispatchers.IO) { TrivialQuestionDao.findQuestionByName(name) }

}