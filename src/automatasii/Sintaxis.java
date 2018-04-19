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
import java.util.ArrayList;

/**
 *
 * @author L55-C5211R
 */
public class Sintaxis {

    private int avanza, numLinea;
    private ArrayList<String> arregloTokens;
    private String arregloCadenas[];
    
    public Sintaxis() {
        avanza = 0;
        numLinea = 0;
        arregloTokens = new ArrayList<String>();
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
                    arregloTokens.add(arregloCadenas[0]);
                    i++;
                }
              

            }
            prog(arregloTokens.get(0));
            br.close();
        } catch (IOException e) {
            System.out.println("Archivo no encontrado");
        }

    }

    private void prog(String cadena) {
        if (cadena.equals("program")) {
            avanza++;
            if (arregloTokens.get(avanza).contains("#")) {
                avanza++;
                declararVar();
                //metodo metodos
                if (arregloTokens.get(avanza).equals("{")) {
                    avanza++;
                    //metodo estatutos
                    if (arregloTokens.get(avanza).equals("}")) {
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
            if (arregloTokens.get(avanza).contains("#")) {
                avanza++;
                if (arregloTokens.get(avanza).contains("[")) {
                    do {
                        avanza++;
                        //Metodo constante
                    } while (arregloTokens.get(avanza).equals(","));
                    if (arregloTokens.get(avanza).contains("]")) {
                        avanza++;
                        if (arregloTokens.get(avanza).contains(";")) {
                            avanza++;
                        } else {
                            error("Se esperaba ;");
                        }
                    }else{
                        error("Se esperaba ]");
                    }
                }else{
                    if(arregloTokens.get(avanza).contains(";")){
                        avanza++;
                    }
                }
            }
            }while(arregloTokens.get(avanza).equals(","));
        }
    }

    private boolean tipo() {
        if (arregloTokens.get(avanza).equals("integer")) {
            avanza++;
            return true;
        } else if (arregloTokens.get(avanza).equals("real")) {
            avanza++;
            return true;
        } else if (arregloTokens.get(avanza).equals("string")) {
            avanza++;
            return true;
        }
        return false;
    }

    private void error(String error) {
        System.out.println(error + "\nLinea:" + arregloCadenas[3]);
    }

}
