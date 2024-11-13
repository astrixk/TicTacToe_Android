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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Home"){
        composable("Home") { HomePage(navController) }
        composable("options"){ GameOptionsPage(navController) }
        composable("threeXGrid"){ ThreeXGrid(navController) }
        composable("eightXGrid"){ EightXGrid(navController) }
    }
}

@Preview
@Composable
fun MyAppPreview(modifier:Modifier=Modifier){
    MyApp()
}



@Composable
fun GameOptionsPage(navController: NavController, modifier: Modifier=Modifier){
    var selectedGrid by rememberSaveable { mutableStateOf<GridOption?>(null) }
    LaunchedEffect(Unit){
        selectedGrid = null
    }
    Surface (modifier,color = Color.DarkGray) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.fillMaxWidth()
                .padding(top=16.dp),
                horizontalArrangement = Arrangement.Start) {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(text = "Back")
                }
            }
            when (selectedGrid) {
                null -> {
                    GameOptions(onGridSelected = { grid ->
                        selectedGrid = grid
                        when (grid) {
                            GridOption.THREE_X_THREE -> navController.navigate("threeXGrid")
                            GridOption.EIGHT_X_EIGHT -> navController.navigate("eightXGrid")
                        }
                    })
                }

                else -> {}
            }

        }
    }
}

@Composable
fun EightXGrid(navController: NavController, modifier: Modifier=Modifier) {
    var board by remember { mutableStateOf(List(8){ MutableList(8){""} })}
    var currentPlayer by remember {mutableStateOf("X")}
    var winner by remember { mutableStateOf<String?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = winner?.let{"$it Wins!!"}?: "Player $currentPlayer's turn",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        board.forEachIndexed {rowIndex, row ->
            Row{
                row.forEachIndexed {colIndex, cell ->
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .background(Color.LightGray)
                            .padding(8.dp)
                            .clickable(enabled = cell.isEmpty() && winner == null) {
                                board = board
                                    .toMutableList()
                                    .apply {
                                        this[rowIndex][colIndex] = currentPlayer
                                    }
                                winner = checkWinner(board, 4)
                                currentPlayer = if (currentPlayer == "X") "O" else "X"
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = cell,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                board = List(8) {MutableList(8){""} }
                currentPlayer = "X"
                winner = null
            }
        ){
            Text(text = "Reset Game")
        }
        Button(
            onClick={ navController.popBackStack() }
        ){
            Text(
                text = "Back"
            )
        }
    }
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
fun ThreeXGrid(navController:NavController,modifier:Modifier = Modifier){
   var board by remember {mutableStateOf(List(3) {MutableList(3){""} })}
    var currentPlayer by remember {mutableStateOf("X")}
    var winner by remember { mutableStateOf<String?>(null)}
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.DarkGray)
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
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.LightGray)
                            .padding(8.dp)
                            .clickable(enabled = cell.isEmpty() && winner == null) {
                                board = board
                                    .toMutableList()
                                    .apply {
                                        this[rowIndex][colIndex] = currentPlayer
                                    }
                                winner = checkWinner(board, 3)
                                currentPlayer = if (currentPlayer == "X") "O" else "X"
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
        Button(onClick = {navController.popBackStack()}) {
            Text(text = "Back")
        }
    }
}


fun checkWinner(board: List<List<String>>, winLength : Int): String? {
    val size = board.size
    for(row in board){
        for(i in 0..size - winLength) {
            val sequence = row.subList(i, i + winLength)
            if (sequence.all { it == "X" }) return "X"
            if (sequence.all { it == "O" }) return "O"
        }
    }
    for(col in 0 until size){
        for( i in 0..size - winLength){
            val sequence = board.subList(i, i+winLength).map{it[col]}
            if (sequence.all { it == "X" }) return "X"
            if (sequence.all { it == "O" }) return "O"
        }
    }
    for (row in 0..size - winLength){
        for (col in 0..size - winLength){
            val sequence = (0 until winLength).map{board[row + it][col + it]}
            if (sequence.all { it == "X" }) return "X"
            if (sequence.all { it == "O" }) return "O"
        }
    }
    for (row in winLength-1 until size){
        for (col in 0..size - winLength){
            val sequence = (0 until winLength).map{board[row - it][col + it]}
            if (sequence.all { it == "X" }) return "X"
            if (sequence.all { it == "O" }) return "O"
        }
    }
    return null
}



@Composable
fun HomePage(navController: NavController,modifier:Modifier=Modifier,
             ){
    val buttonSize = Modifier.size(200.dp, 60.dp)
    Column(modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){

        ElevatedButton(modifier = buttonSize,
            onClick = {navController.navigate("options")}) {
            Text(
                text = "Start",
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        ElevatedButton(modifier = buttonSize.padding(top=6.dp),
            onClick = {}) {
            Text(
                text = "Setting",
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        ElevatedButton(modifier = buttonSize.padding(top=6.dp),
            onClick = {}) {
            Text(
                text = "Exit",
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}




//@Preview(showBackground = true)
//@Composable
//fun GameOptionsPreview(){
//    GameOptionsPage()
//}