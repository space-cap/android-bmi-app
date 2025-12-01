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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ezlevup.bmicalculator.ui.theme.BMICalculatorTheme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BMICalculatorTheme {
                val navController = rememberNavController()
                // ViewModel 인스턴스를 생성합니다. NavHost 내에서 공유됩니다.
                val viewModel: BmiViewModel = viewModel()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(navController = navController, viewModel = viewModel)
                    }
                    composable("result") { // 경로에서 BMI 인자 제거
                        // ViewModel에서 직접 BMI 값을 가져오므로 인자가 필요 없음
                        ResultScreen(navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }
}

// UI 상태와 비즈니스 로직을 담당하는 ViewModel
class BmiViewModel : ViewModel() {
    // 키 입력값을 저장하는 상태
    var height by mutableStateOf("")
        private set // 외부에서는 값을 변경할 수 없도록 설정

    // 몸무게 입력값을 저장하는 상태
    var weight by mutableStateOf("")
        private set

    // 계산된 BMI 결과를 저장하는 상태
    private val _bmi = mutableStateOf(0.0)
    val bmi: State<Double> = _bmi

    // 키 입력값이 변경될 때 호출되는 함수
    fun onHeightChange(newHeight: String) {
        height = newHeight
    }

    // 몸무게 입력값이 변경될 때 호출되는 함수
    fun onWeightChange(newWeight: String) {
        weight = newWeight
    }

    // BMI를 계산하는 함수
    fun calculateBmi() {
        val h = height.toDoubleOrNull()
        val w = weight.toDoubleOrNull()
        if (h != null && w != null && h > 0 && w > 0) {
            _bmi.value = w / (h / 100).pow(2.0)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: BmiViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "비만도 계산기") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.height,
                onValueChange = { viewModel.onHeightChange(it) },
                label = { Text(text = "키") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            OutlinedTextField(
                value = viewModel.weight,
                onValueChange = { viewModel.onWeightChange(it) },
                label = { Text(text = "몸무게") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.calculateBmi()
                    navController.navigate("result") // 인자 없이 경로로만 이동
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
fun ResultScreen(navController: NavController, viewModel: BmiViewModel) {
    // ViewModel에서 직접 BMI 값 가져오기
    val bmi by viewModel.bmi
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
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
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
        HomeScreen(navController = rememberNavController(), viewModel = viewModel())
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    BMICalculatorTheme {
        val viewModel: BmiViewModel = viewModel()
        viewModel.onHeightChange("170")
        viewModel.onWeightChange("70")
        viewModel.calculateBmi()
        ResultScreen(navController = rememberNavController(), viewModel = viewModel)
    }
}
