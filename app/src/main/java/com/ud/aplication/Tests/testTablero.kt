package com.ud.aplication.Tests
import com.ud.aplication.Enums.EnumDificultad
import com.ud.aplication.Enums.EnumEstado
import com.ud.aplication.Logic.Casilla
import org.junit.Test

import org.junit.Assert.*
import com.ud.aplication.Logic.Tablero


class testTablero {

    @Test
    fun testInicializarTableroConDificultadEasy() {
        val resultado = Tablero.inicializarTablero(EnumDificultad.EASY.toString())

        val esperado = List(8) { List(8) { Casilla() } }

        assertEquals(esperado.size, resultado.size)
    }

    @Test
    fun testInicializarTableroConDificultadMedium() {
        val resultado = Tablero.inicializarTablero(EnumDificultad.MEDIUM.toString())

        val esperado = List(12) { List(8) { Casilla() } }

        assertEquals(esperado.size, resultado.size)
    }

    @Test
    fun testInicializarTableroConDificultadHard() {
        val resultado = Tablero.inicializarTablero(EnumDificultad.HARD.toString())

        val esperado = List(15) { List(8) { Casilla() } }

        assertEquals(esperado.size, resultado.size)
    }

    @Test
    fun testAgregarMinas() {
        Tablero.inicializarTablero(EnumDificultad.EASY.toString())

        val numeroMinas = 10
        Tablero.agregarMinas(numeroMinas)

        var minasContadas = (0 - 10)
        for (fila in Tablero.tablero) {
            for (casilla in fila) {
                if (casilla.valor == 100) {
                    minasContadas++
                }
            }
        }

        assertEquals(numeroMinas, minasContadas)
    }

    @Test
    fun testMostrarCerosAdyacentes() {
        Tablero.inicializarTablero(EnumDificultad.EASY.toString())
        Tablero.tablero[0][0].valor = 0
        Tablero.tablero[0][1].valor = 100

        Tablero.mostrarCerosAdyacentes(0, 1)

        assertEquals(EnumEstado.VISIBLE.toString(), Tablero.tablero[0][1].estado)
    }

    @Test
    fun testMarcarMina() {
        Tablero.inicializarTablero(EnumDificultad.EASY.toString())
        Tablero.marcarMina(0, 0)
        assertEquals(EnumEstado.MINA_MARCADA.toString(), Tablero.tablero[0][0].estado)
    }

}