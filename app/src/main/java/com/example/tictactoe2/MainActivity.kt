package com.example.tictactoe2

import android.os.Bundle
import android.webkit.WebSettings.TextSize
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        else{
            GameOptionsPage(modifier)
        }
        if(shouldShowSettingPage){

        }
    }
}

@Preview
@Composable
fun MyAppPreview(modifier:Modifier=Modifier){
    MyApp()
}



@Composable
fun GameOptionsPage(modifier:Modifier=Modifier){
    var selectedGrid by rememberSaveable { mutableStateOf<GridOption?>(null) }
    Surface (modifier,color = Color.DarkGray) {
       when (selectedGrid){
           null -> {
               GameOptions(onGridSelected = {grid ->selectedGrid = grid})
           }
           GridOption.THREE_X_THREE -> ThreeXGrid(modifier)
           GridOption.EIGHT_X_EIGHT -> EightXGrid(modifier)
       }
    }
}

@Composable
fun EightXGrid(modifier: Modifier) {

}

enum class GridOption{
    THREE_X_THREE, EIGHT_X_EIGHT
}
@Composable
fun GameOptions(onGridSelected: (GridOption) -> Unit,modifier:Modifier=Modifier){
    val buttonSize = Modifier.size(200.dp,60.dp)

    Column(modifier=modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(
            text = "Please Select the Grid",
            fontSize = 33.sp,
            modifier = Modifier.padding(bottom = 90.dp),
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontStyle = FontStyle.Normal
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedButton(
                modifier = buttonSize,
                onClick = {onGridSelected(GridOption.THREE_X_THREE)}
            ) {
                Text(
                    text = "3 x 3",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            ElevatedButton(
                modifier = buttonSize.padding(top = 6.dp),
                onClick = {onGridSelected(GridOption.EIGHT_X_EIGHT)}
            ) {
                Text(
                    text = "8 x 8",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

@Composable
fun ThreeXGrid(modifier:Modifier = Modifier){
   var board by remember {mutableStateOf(List(3) {MutableList(3){""} })}
    var currentPlayer by remember {mutableStateOf("X")}
    var winner by remember { mutableStateOf<String?>(null)}
    Column(modifier = Modifier.fillMaxSize().background(Color.DarkGray)
        .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = winner?.let { "$it Wins!" } ?: "Player $currentPlayer's Turn",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        board.forEachIndexed {rowIndex, row ->
            Row{
                row.forEachIndexed {colIndex, cell ->
                    Box(
                        modifier = Modifier.size(100.dp)
                            .background(Color.LightGray)
                            .padding(8.dp)
                            .clickable(enabled = cell.isEmpty() && winner == null){
                                board = board.toMutableList().apply{
                                    this[rowIndex][colIndex] = currentPlayer
                                }
                                winner = checkWinner(board)
                                currentPlayer = if(currentPlayer=="X") "O" else "X"
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = cell,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                board = List(3) {MutableList(3){""} }
                currentPlayer = "X"
                winner = null
            }
        ){
            Text(text = "Reset Game")
        }
    }
}


fun checkWinner(board: List<List<String>>): String? {
    for(row in board){
        if(row[0].isNotEmpty() && row[0]==row[1] && row[1]==row[2]){ return row[0]}
    }

    for(col in 0 until 3){
        if(board[0][col].isNotEmpty() && board[0][col] == board[1][col] && board[1][col] == board[2][col]){return board[0][col]}
    }
    if (board[0][0].isNotEmpty() && board[0][0] == board[1][1] && board[1][1] == board[2][2]){ return board[0][0]}
    if (board[0][2].isNotEmpty() && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {return board[0][2]}

    return null
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




@Preview(showBackground = true)
@Composable
fun GameOptionsPreview(){
    GameOptionsPage()
}