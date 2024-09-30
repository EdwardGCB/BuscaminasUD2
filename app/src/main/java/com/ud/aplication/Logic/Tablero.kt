package com.ud.aplication.Logic

import com.ud.aplication.Enums.EnumDificultad
import com.ud.aplication.Enums.EnumEstado
import kotlin.random.Random

object Tablero {
    internal var tablero: MutableList<MutableList<Casilla>> = mutableListOf()
    var minas: Int = 0

    fun inicializarTablero(dificultad: String?): List<List<Casilla>>{
        if(tablero.isEmpty()){
            var filas =0
            var columnas =0
            when(dificultad){
                /*
                * niveles:
                * Facil = Tablero 8x8 - 10 Minas
                * Mediano = Tablero 12x8 - 20 Minas
                * Dificil = Tablero 15x8 - 35 Minas
                *
                * */
                EnumDificultad.EASY.toString() -> {
                    filas = 8
                    columnas = 8
                    minas = 10
                }
                EnumDificultad.MEDIUM.toString() -> {
                    filas = 12
                    columnas = 8
                    minas = 20
                }
                EnumDificultad.HARD.toString() -> {
                    filas = 15
                    columnas = 8
                    minas = 35
                }
            }
            for (i in 0 until filas){
                val filasCasillas = mutableListOf<Casilla>()
                for (j in 0 until columnas){
                    filasCasillas.add(Casilla())
                }
                tablero.add(filasCasillas)
            }
            agregarMinas(minas)
        }
        return tablero
    }

    /*
    * El parametro f = fila y c = columna hacen referencia al
    * boton que realizo un evento onclick
    * */
    fun mostrarCerosAdyacentes(f: Int,c: Int){
        tablero[f][c].estado = EnumEstado.VISIBLE.toString()
        for (ft in (f-1)..((f+1))){
            for (ct in (c-1)..((c+1))){
                /*
                * Realizamos una validacion donde se tienen que
                * cumplir tres condiciones:
                * 1. fila y la columna temporal debe existir en el tablero
                * 2. la fila temporal o la columna tempotal deben ser
                * diferentes a la fila y la columna que se selecciono
                * 3. la cosilla tiene que estar oculta
                * 4. que no sea una mina
                * */
                if(((ft>=0) && (ct>=0) && (ft< tablero.size) && (ct<tablero[0].size)) && ( (ft!=f) || (ct!=c))){
                    if(tablero[ft][ct].valor != 100){
                        if(tablero[ft][ct].estado != EnumEstado.VISIBLE.toString()){
                            /*
                            * Si cumple con las tres condiciones entonces hago la casilla visible
                            * */
                            tablero[ft][ct].estado = EnumEstado.VISIBLE.toString()

                            /*
                            * si cumple con la condicion de que el valor de la casilla
                            * es 0 entonces repito el proceso pero con la posicion actual
                            * */

                            if (tablero[ft][ct].valor == 0){
                                mostrarCerosAdyacentes(ft,ct)
                            }
                        }
                    }
                }
            }
        }
    }

    /*
    * Funcion para cuando el evento onClick se realizo en una mina
    * */
    fun mostrarMinas(): Boolean{
        for(i in 0 until tablero.size){
            for (j in 0 until tablero[0].size){
                if (tablero[i][j].valor == 100){
                    tablero[i][j].estado = EnumEstado.VISIBLE.toString()

                    //tablero[i][j].estado = EnumEstado.MINA_REVENTADA.toString()
                }
            }
        }
        return true;
    }

    fun marcarMina(f: Int, c: Int){
        tablero[f][c].estado = EnumEstado.MINA_MARCADA.toString()
    }

    internal fun agregarMinas(minas: Int){
        var contador = 0
        while(contador < minas){
            val filaTemp = Random.nextInt(0,tablero.size)
            val columnaTemp = Random.nextInt(0,tablero[0].size)
            if (tablero[filaTemp][columnaTemp].valor != 100){
                tablero[filaTemp][columnaTemp].valor = 100
                for (ft2 in (filaTemp - 1)..(filaTemp+1)) {
                    for (ct2 in (columnaTemp-1)..(columnaTemp+1)){
                        if (ft2 >= 0 && ft2 < tablero.size && ct2 >= 0 && ct2 < tablero[0].size) {
                            if (tablero[ft2][ct2].valor != 100) {
                                tablero[ft2][ct2].valor++
                            }

                        }
                    }
                }
                contador++
            }
        }
    }

    fun librerarEspacioMemoria(){
        tablero= mutableListOf()
        minas = 0
    }
}