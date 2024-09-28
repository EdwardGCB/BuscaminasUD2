package com.ud.aplication.Logic;

public class Casilla {
    /*
     * para reconocer que la casilla contiene una mina su valor sera uno dificil
     * de alcanzar siendo este = 100
     * */
    private int valor=0;
    /*
     * El estado va a alternar entre 4 estados
     * 1. oculta
     * 2. visible
     * 3. mina marcada
     * 4. mina reventada
     *
     * */
    private String estado="oculta";


    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public String getEstado(){
        return estado;
    }
}
