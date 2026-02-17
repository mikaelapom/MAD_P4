package com.maxli.coursegpa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.livedata.observeAsState
import com.maxli.coursegpa.MyApplicationTheme


val TimesNewRoman = FontFamily(
    Font(R.font.times, FontWeight.Normal),
    Font(R.font.times, FontWeight.Bold),
    Font(R.font.times, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.times, FontWeight.Bold, FontStyle.Italic)
)

var questionNumber by mutableIntStateOf(1)
var QuestionsAnswered: Int = 0
var QuestionsCorrect: Int = 0
var QuestionsWrong: Int = 0 //Might not need -Diana

//class Question(val question: String, val correctAnswer: String, val wrong1: String, val wrong2: String, val wrong3: String, var answered: Boolean) { }

/*
val TestQuestion1 = Question("Test Question",
    "Test Answer",
    "Test Wrong",
    "Test Wrong",
    "Test Wrong",
    false)

val TestQuestion2 = Question("Test Question2",
    "Test Answer2",
    "Test Wrong2",
    "Test Wrong2",
    "Test Wrong2",
    false)

var questionsList = listOf(TestQuestion1, TestQuestion2)

 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[MainViewModel::class.java]

        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TriviaScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

    }
}


@Composable
fun TriviaScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val questions by viewModel.allQuestions.observeAsState(emptyList())
    var questionIndex by remember { mutableIntStateOf(0) }

    if (questions.isEmpty()) {
        Text("No questions in database")
        return
    }

    val current = questions[questionIndex]

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopBanner()
        SecondBanner()

        QuestionBox(current, questionIndex)

        Spacer(modifier = Modifier.height(8.dp))

        AnswerButtons(current) {
            questionIndex = (questionIndex + 1) % questions.size
        }

        BackForwardReset(
            currentIndex = questionIndex,
            total = questions.size,
            onBack = {
                if (questionIndex > 0) questionIndex--
            },
            onNext = {
                if (questionIndex < questions.size - 1) questionIndex++
            },
            onReset = {
                questionIndex = 0
            }
        )

    }
}



@Composable
fun AnswerButtons(question: TrivialQuestion, onAnswered: () -> Unit) {
    AnswerRow("A", question.choiceA, onAnswered)
    AnswerRow("B", question.choiceB, onAnswered)
    AnswerRow("C", question.choiceC, onAnswered)
    AnswerRow("D", question.choiceD, onAnswered)
}

@Composable
fun QuestionBox(question: TrivialQuestion, index: Int) {
    Text(
        text = "Question ${index + 1}: ${question.questionName}",
        fontSize = 24.sp
    )
}

@Composable
fun AnswerRow(label: String, text: String, onAnswered: () -> Unit) {
    Row {
        Button(onClick = onAnswered) {
            Text(label)
        }
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
fun SecondBanner() {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color(0xFFFBE475))
            , contentAlignment = Alignment.Center
        )
        {

            Text(text = "Trivia Questionaire",
                style = TextStyle(
                    fontFamily = TimesNewRoman,
                    fontSize = 30.sp,
                    color = Color(0xFF1A2C57)
                ))
        }
    }
}
@Composable
fun TopBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color(0xFFFBE475)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.smith),
                contentDescription = "Smith College Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 8.dp)
            )

            Text(
                text = "Smith College",
                style = TextStyle(
                    fontFamily = TimesNewRoman,
                    fontSize = 35.sp,
                    color = Color(0xFF1A2C57)
                )
            )
        }

    }
}

@Composable
fun BackForwardReset(
    currentIndex: Int,
    total: Int,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onReset: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF97CDEC),
                contentColor = Color(0xFF1A2C57)
            ),
            shape = CircleShape,
            modifier = Modifier.size(100.dp),
            enabled = currentIndex > 0
        ) {
            Text("Back", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = onReset,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF97CDEC),
                contentColor = Color(0xFF1A2C57)
            ),
            shape = CircleShape,
            modifier = Modifier.size(100.dp)
        ) {
            Text("Reset", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF97CDEC),
                contentColor = Color(0xFF1A2C57)
            ),
            shape = CircleShape,
            modifier = Modifier.size(100.dp),
            enabled = currentIndex < total - 1
        ) {
            Text("Next", fontSize = 20.sp)
        }
    }
}


/*
@Composable
fun AnswerOne() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                questionNumber++
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF97CDEC),
                contentColor = Color(0xFF1A2C57)
            ),
            shape = CircleShape,
            modifier = Modifier.size(50.dp)
        ) {
            Text(
                text = "A", //Didnt use times for font bcs ugly - Diana
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 25.sp,
                    color = Color(0xFF1A2C57)
                )
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "${questionsList[questionNumber - 1].correctAnswer}",
            fontFamily = TimesNewRoman,
            style = TextStyle(
                fontSize = 25.sp,
                color = Color(0xFF1A2C57)
            )
        )
    }
}

@Composable
fun AnswerTwo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                questionNumber++
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF97CDEC),
                contentColor = Color(0xFF1A2C57)
            ),
            shape = CircleShape,
            modifier = Modifier.size(50.dp)
        ) {
            Text(
                text = "B",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 25.sp,
                    color = Color(0xFF1A2C57)
                )
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "${questionsList[questionNumber - 1].wrong1}",
            style = TextStyle(
                fontFamily = TimesNewRoman,
                fontSize = 25.sp,
                color = Color(0xFF1A2C57)
            )
        )
    }
}

@Composable
fun AnswerThree() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                questionNumber++
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF97CDEC),
                contentColor = Color(0xFF1A2C57)
            ),
            shape = CircleShape,
            modifier = Modifier.size(50.dp)
        ) {
            Text(
                text = "C",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 25.sp,
                    color = Color(0xFF1A2C57)
                )
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "${questionsList[questionNumber - 1].wrong2}",
            style = TextStyle(
                fontFamily = TimesNewRoman,
                fontSize = 25.sp,
                color = Color(0xFF1A2C57)
            )
        )
    }
}

@Composable
fun AnswerFour() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                questionNumber++
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF97CDEC),
                contentColor = Color(0xFF1A2C57)
            ),
            shape = CircleShape,
            modifier = Modifier.size(50.dp)
        ) {
            Text(
                text = "D",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 25.sp,
                    color = Color(0xFF1A2C57)
                )
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "${questionsList[questionNumber - 1].wrong3}",
            style = TextStyle(
                fontFamily = TimesNewRoman,
                fontSize = 25.sp,
                color = Color(0xFF1A2C57)
            )
        )
    }
}

@Composable
fun backForwardReset() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                questionNumber++
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF97CDEC),
                contentColor = Color(0xFF1A2C57)
            ),
            shape = CircleShape,
            modifier = Modifier.size(100.dp)
        ) {
            Text(
                text = "Back",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 20.sp,
                    color = Color(0xFF1A2C57)
                )
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = {
                questionNumber++
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF97CDEC),
                contentColor = Color(0xFF1A2C57)
            ),
            shape = CircleShape,
            modifier = Modifier.size(100.dp)
        ) {
            Text(
                text = "Reset",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 20.sp,
                    color = Color(0xFF1A2C57)
                )
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = {
                questionNumber++
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF97CDEC),
                contentColor = Color(0xFF1A2C57)
            ),
            shape = CircleShape,
            modifier = Modifier.size(100.dp)
        ) {
            Text(
                text = "Next",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 20.sp,
                    color = Color(0xFF1A2C57)
                )
            )
        }

    }
}


 */