package com.ud.aplication.Logic;

public class Tablero {
    private static Casilla[][] tablero;

    private Tablero(){

    }

    public static Casilla[][] getTablero(int f, int c, int m){
        if(tablero == null){
            for (int i=0; i<f;i++) {
                for (int j = 0; j < c; j++) {
                    Casilla[][] tablero = new Casilla[i][j];
                }
            }
            generarMinas(f, c, m);
        }
        return tablero;
    }

    public static void generarMinas(int f, int c, int m){
        for(int contador =0; contador < m;){
            int ft = (int) (Math.random() * f);
            int ct = (int) (Math.random() * c);
            if (tablero[ft][ct].getValor() != 100){ //Que la mina no se repita
                tablero[ft][ct].setValor(100);
                //Cuando la mina es generada le resto la cantidad de minas que voy a generar
                //Si lo hiciera en el while() ocurriria que podria generar menos minas de las que solicita el nivel.
                contador++;
                for(int ft2=(ft-1);ft2<=(ft+1);ft++){
                    /*
                    * Se crean nuevas columnas temporales en este caso ct2 y ft2
                    * debido a que debo recorrer el alrededor de la mina para
                    * agregar un valor
                    *
                    * */
                    for (int ct2=(ct-1);ct<=(ct+1);ct++){
                        if ((ft2>=0)&&(ct2>=0)&&(ft2<ft)&&(ct2<ct)){
                            if (tablero[ft2][ct2].getValor() != 100){
                                tablero[ft2][ct2].setValor(tablero[ft2][ct2].getValor()+1);
                            }
                        }
                    }
                }
            }
        }
    }

}
