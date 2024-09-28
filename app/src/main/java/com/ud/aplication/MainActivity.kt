package com.ud.aplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ud.aplication.Enums.EnumDificultad
import com.ud.aplication.Logic.Casilla
import com.ud.aplication.Logic.Tablero


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ventana()
        }
    }
}

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ventana() {
    val dificultad = remember { mutableStateOf(EnumDificultad.MEDIUM.toString()) }
    var tablero = Tablero.inicializarTablero(dificultad.value)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Buscaminas")
                }

            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                Desplegable(dificultad)
            }
        }
    ) { innerPadding ->
        Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
            MinesBoard(tablero)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Desplegable(seleccion: MutableState<String>) {
    val context = LocalContext.current
    val dificultades = arrayOf("EASY", "MEDIUM", "HARD")
    val desplegar = remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = desplegar.value,
        onExpandedChange = {
            desplegar.value = !desplegar.value
        }
    ) {
        TextField(
            value = seleccion.value,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = desplegar.value)
            },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = desplegar.value,
            onDismissRequest = { desplegar.value = false }
        ) {
            dificultades.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        seleccion.value = item
                        desplegar.value = false
                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

}

@Composable
fun MinesBoard (tablero: MutableList<MutableList<Casilla>>){
    for(f in 0 until tablero.size){
        Row {
            for(c in 0 until tablero[0].size){
                MiniesButton(f,c,tablero[f][c])
            }
        }
    }
}

@Composable
fun MiniesButton(f: Int, c: Int, casilla: Casilla){
    val clicked = remember{ mutableStateOf(false) }
    Button(
        onClick = {
            clicked.value = true },
            modifier = Modifier
                .padding(4.dp)
                .size(40.dp)

        ) {
        Text(
            text = casilla.valor.toString(),
            modifier = Modifier.fillMaxSize()
        )
    }
}
