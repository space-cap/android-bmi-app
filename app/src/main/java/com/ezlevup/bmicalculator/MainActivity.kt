package com.ezlevup.bmicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ezlevup.bmicalculator.ui.theme.BMICalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BMICalculatorTheme {
                // 내비게이션 컨트롤러 생성 및 기억
                val navController = rememberNavController()
                // NavHost: 내비게이션 그래프를 정의하고, 화면 전환을 관리
                NavHost(navController = navController, startDestination = "home") {
                    // "home" 경로에 대한 화면 정의
                    composable("home") {
                        HomeScreen(navController = navController)
                    }
                    // "result/{bmi}" 경로에 대한 화면 정의. {bmi}는 전달받을 인자
                    composable(
                        "result/{bmi}",
                        // "bmi" 인자의 타입을 FloatType으로 지정
                        arguments = listOf(navArgument("bmi") { type = NavType.FloatType })
                    ) {
                        // arguments에서 "bmi" 값을 추출. 안전하게 Float로 받고 Double로 변환
                        val bmi = it.arguments?.getFloat("bmi")?.toDouble() ?: 0.0
                        ResultScreen(navController = navController, bmi = bmi)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var height by rememberSaveable { mutableStateOf("") }
    var weight by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "비만도 계산기") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = { Text(text = "키") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text(text = "몸무게") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val h = height.toDoubleOrNull()
                    val w = weight.toDoubleOrNull()
                    if (h != null && w != null && h > 0 && w > 0) {
                        val bmiResult = w / (h / 100 * h / 100)
                        // "result" 경로로 이동하면서 계산된 bmiResult 값을 전달
                        navController.navigate("result/$bmiResult")
                    }
                },
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(text = "계산")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(navController: NavController, bmi: Double) {
    val resultText: String
    val resultColor: Color

    when {
        bmi < 18.5 -> {
            resultText = "저체중"
            resultColor = Color.Blue
        }
        bmi < 23.0 -> {
            resultText = "정상"
            resultColor = Color(0xFF00C853)
        }
        bmi < 25.0 -> {
            resultText = "과체중"
            resultColor = Color.Yellow
        }
        else -> {
            resultText = "비만"
            resultColor = Color.Red
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "비만도 계산 결과") },
                navigationIcon = {
                    // 뒤로가기 버튼 클릭 시 이전 화면으로 이동
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(resultText, fontSize = 30.sp, color = resultColor)
            Spacer(modifier = Modifier.height(50.dp))
            Image(
                painter = painterResource(id = R.drawable.baseline_sentiment_very_dissatisfied_24),
                contentDescription = resultText,
                modifier = Modifier.size(100.dp),
                colorFilter = ColorFilter.tint(resultColor)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BMICalculatorTheme {
        HomeScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    BMICalculatorTheme {
        ResultScreen(navController = rememberNavController(), bmi = 24.5)
    }
}
