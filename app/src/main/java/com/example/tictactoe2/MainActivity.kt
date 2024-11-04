package com.example.tictactoe2

import android.os.Bundle
import android.webkit.WebSettings.TextSize
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tictactoe2.ui.theme.TicTacToe2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToe2Theme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
@Composable
fun MyApp(modifier:Modifier=Modifier){
    var shouldShowStartPage by rememberSaveable{ mutableStateOf(true)}
    var shouldShowSettingPage by rememberSaveable{ mutableStateOf(true)}
    Surface(modifier,
        color = Color.DarkGray){
        if(shouldShowStartPage) {
            HomePage(
                onStartClicked = {shouldShowStartPage=false})
        }
        else if(shouldShowSettingPage){

        }
    }
}

@Preview
@Composable
fun MyAppPreview(modifier:Modifier=Modifier){
    MyApp()
}



@Composable
fun HomePage(onStartClicked:() -> Unit,modifier:Modifier=Modifier,
             ){
    val buttonSize = Modifier.size(200.dp, 60.dp)
    Column(modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){

        ElevatedButton(modifier = buttonSize,
            onClick = onStartClicked) {
            Text(
                text = "Start",
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        ElevatedButton(modifier = buttonSize.padding(top=6.dp),
            onClick = onStartClicked) {
            Text(
                text = "Setting",
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        ElevatedButton(modifier = buttonSize.padding(top=6.dp),
            onClick = onStartClicked) {
            Text(
                text = "Exit",
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TicTacToe2Theme {
        Greeting("Android")
    }
}