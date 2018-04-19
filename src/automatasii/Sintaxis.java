/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatasii;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author L55-C5211R
 */
public class Sintaxis {

    private int avanza, numLinea;
    private String arregloTokens[], arregloCadenas[];;

    public Sintaxis() {
        avanza = 0;
        numLinea = 0;
        arregloTokens = new String[1000];
    }

    public void leerArchivo() {
        String cadena = "";
        FileReader fr = null;
        try {
            File file = new File("./tablaSintaxis.txt");
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            int i = 0;
            while (br.ready()) {
                cadena = br.readLine();
                if (!cadena.equals("")) {
                    arregloCadenas = cadena.split("\\$");
                    arregloTokens[i] = arregloCadenas[0];
                    i++;
                }
              

            }
            prog(arregloTokens[0]);
            br.close();
        } catch (IOException e) {
            System.out.println("Archivo no encontrado");
        }

    }

    private void prog(String cadena) {
        if (cadena.equals("program")) {
            avanza++;
            if (arregloTokens[avanza].contains("#")) {
                avanza++;
                declararVar();
                //metodo metodos
                if (arregloTokens[avanza].equals("{")) {
                    avanza++;
                    //metodo estatutos
                    if (arregloTokens[avanza].equals("}")) {
                        avanza++;
                    } else {
                        error("Error: se esperaba una }");
                    }
                }
            }
        }
    }

    private void declararVar() {
        if (tipo()) {
            avanza++;
            do{
            if (arregloTokens[avanza].contains("#")) {
                avanza++;
                if (arregloTokens[avanza].contains("[")) {
                    do {
                        avanza++;
                        //Metodo constante
                    } while (arregloTokens[avanza].equals(","));
                    if (arregloTokens[avanza].contains("]")) {
                        avanza++;
                        if (arregloTokens[avanza].contains(";")) {
                            avanza++;
                        } else {
                            error("Se esperaba ;");
                        }
                    }else{
                        error("Se esperaba ]");
                    }
                }else{
                    if(arregloTokens[avanza].contains(";")){
                        avanza++;
                    }
                }
            }
            }while(arregloTokens[avanza].equals(","));
        }
    }

    private boolean tipo() {
        if (arregloTokens[avanza].equals("integer")) {
            avanza++;
            return true;
        } else if (arregloTokens[avanza].equals("real")) {
            avanza++;
            return true;
        } else if (arregloTokens[avanza].equals("string")) {
            avanza++;
            return true;
        }
        return false;
    }

    private void error(String error) {
        System.out.println(error + "\nLinea:" + arregloCadenas[3]);
    }

}
