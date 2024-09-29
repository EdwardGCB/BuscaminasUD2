package com.ud.aplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ud.aplication.Enums.EnumDificultad
import com.ud.aplication.Enums.EnumEstado
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
    val banderas = remember { mutableIntStateOf(0) }
    banderas.value = Tablero.minas
    val timer = remember { mutableIntStateOf(0) }
    val refresh = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TopInfo(timer, Tablero.minas, banderas)
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                },
                modifier = Modifier.padding(top = 6.dp)
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
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
            if (refresh.value) {
                tablero = Tablero.inicializarTablero(dificultad.value)
                MinesBoard(tablero, refresh)
            } else MinesBoard(tablero = Tablero.inicializarTablero(dificultad.value), refresh)
        }
    }
}

@Composable
fun TopInfo(timer: MutableState<Int>, minas: Int, banderas: MutableState<Int>) {
    val imageReloj = R.drawable.reloj
    val imageMinies = R.drawable.bomb
    val imageFlag = R.drawable.bandera
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(painterResource(imageMinies), contentDescription = "Minas")
        Text(minas.toString())
        Spacer(modifier = Modifier.width(20.dp))
        Image(painterResource(imageReloj), contentDescription = "Reloj")
        Text(timer.value.toString())
        Spacer(modifier = Modifier.width(20.dp))
        Image(painterResource(imageFlag), contentDescription = "Banderas")
        Text(banderas.value.toString())

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
        modifier = Modifier.width(160.dp),
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
                        Tablero.librerarEspacioMemoria()
                    }
                )
            }
        }
    }

}

@Composable
fun MinesBoard(tablero: List<List<Casilla>>, refresh: MutableState<Boolean>) {
    for (f in tablero.indices) {
        Row {
            for (c in 0 until tablero[0].size) {
                when (tablero[f][c].estado) {
                    EnumEstado.VISIBLE.toString() -> MiniesBox(tablero[f][c])
                    EnumEstado.OCULTA.toString() -> MiniesButton(f, c, tablero[f][c], refresh)
                }
            }
        }
    }
}

@Composable
fun MiniesButton(f: Int, c: Int, casilla: Casilla, refresh: MutableState<Boolean>) {
    if (casilla.estado == EnumEstado.MINA_MARCADA.toString()) {
        ImageButton(R.drawable.bandera)
    }else {
        Button(
            onClick = {
                /*
                * Cuando clickeo el boton tengo tres posibilidades:
                * 1. si el valor es cero entonces muestro los adyacentes
                * 2. si el valor es una mina = 100 entonces muestro todas las minas y acabo el juego
                * 3. si el valor es cualquier otro numero, solo lo muestro
                * */
                when (casilla.valor) {
                    0 -> Tablero.mostrarCerosAdyacentes(f, c)
                    100 -> Tablero.mostrarMinas()
                    else -> casilla.estado = EnumEstado.VISIBLE.toString()
                }
                refresh.value = !refresh.value
            },
            modifier = Modifier
                .padding(4.dp)
                .size(40.dp),
            shape = RoundedCornerShape(0.dp)
        ) {

        }
    }
}

@Composable
fun MiniesBox(casilla: Casilla) {
    if(casilla.valor == 100){
        ImageButton(R.drawable.explosion)
    }else{
        Box(
            modifier = Modifier
                .padding(4.dp)
                .size(40.dp)
                .border(2.dp, Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if(casilla.valor == 0)"" else casilla.valor.toString(),
                fontSize = 16.sp,
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ImageButton(imageId: Int) {

    // Crea un botón con la imagen
    Image(
        painter = painterResource(imageId),
        contentDescription = null, // Puedes agregar una descripción aquí
        modifier = Modifier
            .padding(4.dp)
            .size(40.dp)
            .clickable { /* Acción al hacer clic en la imagen */ },
    )
}