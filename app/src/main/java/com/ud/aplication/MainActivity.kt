package com.ud.aplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ud.aplication.Enums.EnumDificultad
import com.ud.aplication.Enums.EnumEstado
import com.ud.aplication.Logic.Casilla
import com.ud.aplication.Logic.Operaciones
import com.ud.aplication.Logic.Tablero
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ventana()
        }
    }
}

//@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ventana() {
    val isready = remember { mutableStateOf(false) }
    val time = remember { mutableIntStateOf(60) }
    val dificultad = remember { mutableStateOf(EnumDificultad.MEDIUM.toString()) }
    var tablero = Tablero.inicializarTablero(dificultad.value)
    val banderas = remember { mutableIntStateOf(0) }
    val mostrarMensajeFlotante1 = remember { mutableStateOf(true) }
    val mostrarMensajeFlotante2 = remember { mutableStateOf(false) }
    banderas.value = Tablero.minas
    val refresh = remember { mutableStateOf(false) }

    // Mensaje de bienvenida
    MensajeAlerta(mostrarMensajeFlotante1, "Buscaminas UD", "Bienvenido a buscaminas", isready, time, 1, refresh)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TopInfo(isready, time, Tablero.minas, banderas)
                },
                actions = {
                    IconButton(onClick = {
                        Tablero.librerarEspacioMemoria()
                        refresh.value = !refresh.value
                        time.value = 60 // Reinicia el tiempo a 60 segundos
                        isready.value = true // Marca el juego como listo para iniciar el timer
                        banderas.value = Tablero.minas
                    }) {
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
                MinesBoard(tablero, refresh, banderas, mostrarMensajeFlotante2, isready)
            } else {
                MinesBoard(tablero = Tablero.inicializarTablero(dificultad.value), refresh, banderas, mostrarMensajeFlotante2, isready)
            }
        }
    }

    // Mensaje de fin de juego
    MensajeAlerta(
        mostrarMensajeFlotante2,
        "puntuacion: {${Tablero.puntaje.toString()}}\n ¿Volver a intentar?",
        "Fin del juego",
        isready,
        time,
        0,
        refresh
    )
}

@Composable
fun TopInfo(
    isready: MutableState<Boolean>,
    time: MutableState<Int>,
    minas: Int,
    banderas: MutableState<Int>
) {

    val imageReloj = R.drawable.reloj
    val imageMinies = R.drawable.bomb
    val imageFlag = R.drawable.bandera

    // Cambia las claves de LaunchedEffect a solo 'isready.value'
    LaunchedEffect(key1 = isready.value) {
        if (isready.value) {
            while (time.value > 0 && isready.value) {
                delay(1000L)
                time.value--
            }
            // Opcional: Puedes manejar el tiempo agotado aquí si lo deseas
            if (time.value == 0) {
                // Lógica cuando el tiempo se agota
                // Por ejemplo, puedes mostrar un mensaje de fin del juego
            }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(painterResource(imageMinies), contentDescription = "Minas")
        Text(minas.toString())
        Spacer(modifier = Modifier.width(20.dp))
        Image(painterResource(imageReloj), contentDescription = "Reloj")
        Text(Operaciones().formatTime(time.value))
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
                        // Reinicia el tiempo y marca el juego como listo
                        // Puedes añadir aquí si lo deseas
                    }
                )
            }
        }
    }

}

@Composable
fun MinesBoard(
    tablero: List<List<Casilla>>,
    refresh: MutableState<Boolean>,
    banderas: MutableState<Int>,
    mostrarMensaje: MutableState<Boolean>,
    isready: MutableState<Boolean> // Añadido
) {
    for (f in tablero.indices) {
        Row {
            for (c in 0 until tablero[0].size) {
                when (tablero[f][c].estado) {
                    EnumEstado.VISIBLE.toString() -> MiniesBox(tablero[f][c])
                    EnumEstado.OCULTA.toString() -> MiniesButton(
                        onClick = {
                            when (tablero[f][c].valor) {
                                0 -> Tablero.mostrarCerosAdyacentes(f, c)
                                100 -> {
                                    Tablero.mostrarMinas()
                                    mostrarMensaje.value = true
                                    isready.value = false // Detiene el timer al perder
                                }
                                else -> tablero[f][c].estado = EnumEstado.VISIBLE.toString()
                            }
                            refresh.value = !refresh.value
                        },
                        onLongPress = {
                            Tablero.marcarCasilla(f, c)
                            refresh.value = !refresh.value
                            banderas.value--
                            if (banderas.value == 0) {
                                Tablero.mostrarMinas()
                                refresh.value = !refresh.value
                                mostrarMensaje.value = true
                                isready.value = false // Detiene el timer al ganar
                            }
                        },
                        bandera = false
                    )
                    EnumEstado.MINA_REVENTADA.toString() -> {
                        ImageButton(R.drawable.explosion)
                    }
                    EnumEstado.MINA_MARCADA.toString() -> MiniesButton(
                        onClick = { },
                        onLongPress = {
                            Tablero.desmarcarCasilla(f, c)
                            refresh.value = !refresh.value
                            banderas.value++
                        },
                        bandera = true
                    )
                }
            }
        }
    }
}

@Composable
fun MiniesButton(
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    bandera: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(35.dp)
            .background(Color.DarkGray)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        val job = coroutineScope.launch {
                            delay(1000L) // 1 segundo
                            onLongPress()
                        }
                        tryAwaitRelease()
                        job.cancel()
                    },
                    onTap = {
                        onClick()
                    }
                )
            }
    ) {
        if (bandera) {
            Image(painterResource(R.drawable.banderaw), contentDescription = "Banderas")
        }
    }
}

@Composable
fun MiniesBox(casilla: Casilla) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(35.dp)
            .border(2.dp, Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (casilla.valor == 0) "" else casilla.valor.toString(),
            fontSize = 16.sp,
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
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
            .size(35.dp)
            .clickable { /* Acción al hacer clic en la imagen */ },
    )
}

@Composable
fun MensajeAlerta(
    mostrarMensaje: MutableState<Boolean>,
    mensaje: String,
    titulo: String,
    isready: MutableState<Boolean>,
    time: MutableState<Int>,
    temp: Int,
    refresh: MutableState<Boolean>
) {
    Column(modifier = Modifier.padding(16.dp)) {
        if (mostrarMensaje.value) {
            AlertDialog(
                icon = {
                    Icon(Icons.Default.CheckCircle, "Correcto")
                },
                onDismissRequest = { mostrarMensaje.value = !mostrarMensaje.value },
                title = {
                    Text(
                        text = titulo
                    )
                },
                text = {
                    Text(
                        text = mensaje
                    )
                },
                dismissButton = {
                    if (temp == 0) { // Solo para fin de juego
                        Button(onClick = {
                            mostrarMensaje.value = !mostrarMensaje.value
                        }) {
                            Text(text = "Cancelar")
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        isready.value = true // Marca el juego como listo para iniciar el timer
                        time.value = 60 // Reinicia el tiempo a 60 segundos
                        Tablero.librerarEspacioMemoria()
                        refresh.value = !refresh.value
                        mostrarMensaje.value = !mostrarMensaje.value
                    }) {
                        Text(text = "Aceptar")
                    }
                }
            )
        }
    }
}
