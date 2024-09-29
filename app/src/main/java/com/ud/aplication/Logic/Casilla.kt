package com.ud.aplication.Logic

import com.ud.aplication.Enums.EnumEstado

class Casilla (
    /*
     * para reconocer que la casilla contiene una mina su valor sera uno dificil
     * de alcanzar siendo este = 100
     * */
    var valor: Int =0,
    /*
     * El estado va a alternar entre 4 estados
     * descritos en el EnumEstado:
     * 1. OCULTA
     * 2. VISIBLE
     * 3. MINA_MARCADA
     * 4. MINA_REVENTADA
     * */
    var estado: String = EnumEstado.OCULTA.toString()
) {
}