/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatasii;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author L55-C5211R
 */
public class Lexico {
    
    private int numLin, iGlobal, idError;
    private final String palabrasReserv[];
    private String token;
    private final char aritmeticos[], especiales[];
    public ArrayList<String> tablaToken, tablaErrores,tablaSintaxis;
    private char palabra[];
    private String cadena;
    private BufferedReader br;

    public Lexico() {
        numLin = 0;
        palabrasReserv = new String[]{"program", "begin", "end", "input", "output", "integer", "real", "char",
            "string", "if", "else", "then", "while", "do", "repeat", "until", "var", "procedure", "call"};
        tablaToken = new ArrayList<String>();
        tablaErrores = new ArrayList<String>();
        tablaSintaxis = new ArrayList<>();
        aritmeticos = new char[]{'-', '+', '/', '*', '%'};
        especiales = new char[]{'(', ')', ';', ',', '=', '[', ']', '{', '}'};
        iGlobal = 0;
        token = "";
        cadena = "";
        idError = 1;

    }

    public void leerArchivo() throws IOException {
        tablaToken.add("\tPalabra\t\t\tN° Token\t\t\tPosicion en tabla\t\t\tN° Linea");
        tablaErrores.add("\tN° Consecutivo\t\tError\t\tN° Linea");
        File file = new File("./entrada1.txt");
        FileReader fr = new FileReader(file);
        br = new BufferedReader(fr);
        while (br.ready()) {
            numLin++;
            cadena = br.readLine();
            System.out.println(cadena);

            if (!cadena.equals("")) {
                estadoCero(cadena);
            }

        }
          for (int i = 0; i < tablaToken.size(); i++) {
            System.out.println(tablaToken.get(i));
        }
        System.out.println("\n\n\n");
        for (int i = 0; i < tablaErrores.size(); i++) {
            System.out.println(tablaErrores.get(i));
        }

        
        br.close();
    }

    private void estadoCero(String palab) {
        token = "";
        iGlobal = 0;
        palabra = palab.toLowerCase().toCharArray();
        while (iGlobal < palabra.length) {
            if (palabra[iGlobal] != ' ') {
                if (!esComentario()) {
                    if (!esIdentificador()) {
                        if (!esReservado()) {
                            if (!esNumero()) {
                                if (!esAritmetico()) {
                                    if (!esRelacional()) {
                                        if (!esLogico()) {
                                            if (!esCaractEsp()) {
                                                if (!esConstante()) {
                                                    error();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                iGlobal++;
            }
        }
    }

    private boolean esConstante() {
        if (palabra[iGlobal] == '"') {
            token += palabra[iGlobal];
            iGlobal++;
            while (true) {
                if (palabra[iGlobal] == '"') {
                    token += palabra[iGlobal];
                    iGlobal++;
                    tablaToken.add("\t" + token + "\t\t\t1000\t\t\t\t\t-1\t\t\t\t" + numLin);
                    tablaSintaxis.add(token+"$1000$-1$"+numLin);
                    token = "";
                    return true;
                } else {
                    token += palabra[iGlobal];
                    iGlobal++;
                }
            }
        }
        return false;
    }

    private boolean esLogico() {
        token = "";
        if (iGlobal < palabra.length) {
            if (palabra[iGlobal] == '&' || palabra[iGlobal] == '|' || palabra[iGlobal] == '!') {
                if (palabra[iGlobal] == '&' && iGlobal < (palabra.length - 1) && palabra[iGlobal + 1] == '&') {
                    token += palabra[iGlobal];
                    token += palabra[iGlobal + 1];
                    iGlobal += 2;
                    tablaToken.add("\t" + token + "\t\t\t401\t\t\t\t\t-1\t\t\t\t" + numLin);
                    tablaSintaxis.add(token+"$401$-1$"+numLin);
                    token = "";
                    return true;
                }
                if (palabra[iGlobal] == '|' && iGlobal < (palabra.length - 1) && palabra[iGlobal + 1] == '|') {
                    token += palabra[iGlobal];
                    token += palabra[iGlobal + 1];
                    iGlobal += 2;
                    tablaToken.add("\t" + token + "\t\t\t402\t\t\t\t\t-1\t\t\t\t" + numLin);
                    tablaSintaxis.add(token+"$402$-1$"+numLin);
                    token = "";
                    return true;
                }
                if (palabra[iGlobal] == '!' && iGlobal < (palabra.length - 1) && palabra[iGlobal + 1] != '=') {
                    token += palabra[iGlobal];
                    iGlobal++;
                    tablaToken.add("\t" + token + "\t\t\t403\t\t\t\t\t-1\t\t\t\t" + numLin);
                    tablaSintaxis.add(token+"$403$-1$"+numLin);
                    token = "";
                    return true;
                }
                return false;
            }

            return false;
        } else {
            token += palabra[iGlobal];
            iGlobal++;
            tablaToken.add("\t" + token + "\t\t\t403\t\t\t\t\t-1\t\t\t\t" + numLin);
            tablaSintaxis.add(token+"$403$-1$"+numLin);
            token = "";
            return true;
        }

    }

    private boolean esComentario() {
        
        token = "";
        if (palabra[iGlobal] == '/') {
            token += palabra[iGlobal];
            iGlobal++;
            if (iGlobal < palabra.length && palabra[iGlobal] == '/') {
                token += palabra[iGlobal];
                iGlobal++;
                while (true) {
                    if (iGlobal < palabra.length) {
                        if (palabra[iGlobal] == '/') {
                            token += palabra[iGlobal];
                            iGlobal++;
                          if (palabra[iGlobal-2] != '/') {
                            if (iGlobal < palabra.length) {
                                if (palabra[iGlobal] == '/') {
                                    token += palabra[iGlobal];
                                    tablaToken.add("\t" + token + "\t\t\t700\t\t\t\t\t-1\t\t\t\t" + numLin);
                                    tablaSintaxis.add(token+"$700$-1$"+numLin);
                                    iGlobal++;
                                    token = "";
                                    return true;
                                } else {
                                    token += palabra[iGlobal];
                                    iGlobal++;
                                }
                            } else {
                                tablaErrores.add("\t" + idError + "\t\t\t" + token + "\t\t\t" + numLin);
                                return true;
                            }
                        }else{
                             
                              token += palabra[iGlobal];
                              tablaErrores.add("\t" + idError + "\t\t\t" + token + "\t\t\t" + numLin);
                                return true;
                        }
                        } else {
                            token += palabra[iGlobal];
                            iGlobal++;
                        }

                    } else {
                        try {
                            if (br.ready()) {
                                numLin++;
                                palabra = br.readLine().toLowerCase().toCharArray();
                                iGlobal = 0;
                                token += ' ';

                            } else {
                                tablaErrores.add("\t" + idError + "\t\t\t" + token + "\t\t\t" + numLin);
                                return true;
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }

            } else {
                iGlobal--;
                return false;
            }
        }
        return false;
    }

    private boolean esNumero() {
        token = "";
        if (palabra[iGlobal] == '+' || palabra[iGlobal] == '-') {
            if (iGlobal == 0 || (!(palabra[iGlobal - 1] >= '0' && palabra[iGlobal - 1] <= '9') || palabra[iGlobal - 1] == ' ')) {
                token += palabra[iGlobal];
                iGlobal++;
                if (cicloNumero(false)) {
                    return true;
                } else if (palabra[iGlobal] == '.') {
                    if (cicloNumero(true)) {
                        return true;
                    }

                }
            }
        } else if (palabra[iGlobal] >= '0' && palabra[iGlobal] <= '9') {
            if (cicloNumero(false)) {
                return true;
            }
        } else if (palabra[iGlobal] == '.') {
            if (cicloNumero(false)) {
                return true;
            }
        }

        return false;
    }

    private boolean esRelacional() {
        token = "";
        if (palabra[iGlobal] == '<') {
            token += palabra[iGlobal];
            iGlobal++;
            if (iGlobal < palabra.length && palabra[iGlobal] == '=') {
                token += palabra[iGlobal];
                iGlobal++;
                tablaToken.add("\t" + token + "\t\t\t" + 303 + "\t\t\t\t\t-1\t\t\t\t" + numLin);
                tablaSintaxis.add(token+"$303$-1$"+numLin);
                token = "";
                return true;
            } else {
                tablaToken.add("\t" + token + "\t\t\t" + 301 + "\t\t\t\t\t-1\t\t\t\t" + numLin);
                tablaSintaxis.add(token+"$301$-1$"+numLin);
                token = "";
                return true;
            }
        }
        if (palabra[iGlobal] == '>') {
            token += palabra[iGlobal];
            iGlobal++;
            if (iGlobal < palabra.length && palabra[iGlobal] == '=') {
                token += palabra[iGlobal];
                tablaToken.add("\t" + token + "\t\t\t" + 304 + "\t\t\t\t\t-1\t\t\t\t" + numLin);
                tablaSintaxis.add(token+"$304$-1$"+numLin);
                iGlobal++;
                token = "";
                return true;
            } else {
                tablaToken.add("\t" + token + "\t\t\t" + 302 + "\t\t\t\t\t-1\t\t\t\t" + numLin);
                tablaSintaxis.add(token+"$302$-1$"+numLin);
                iGlobal++;
                token = "";
                return true;
            }
        }
        if (palabra[iGlobal] == '=') {
            token += palabra[iGlobal];
            iGlobal++;
            if (iGlobal < palabra.length && palabra[iGlobal] == '=') {
                token += palabra[iGlobal];
                tablaToken.add("\t" + token + "\t\t\t" + 305 + "\t\t\t\t\t-1\t\t\t\t" + numLin);
                tablaSintaxis.add(token+"$305$-1$"+numLin);
                iGlobal++;
                token = "";
                return true;
            } else {
                iGlobal--;
                return false;
            }
        }
        if (palabra[iGlobal] == '!') {
            token += palabra[iGlobal];
            iGlobal++;
            if (iGlobal < palabra.length && palabra[iGlobal] == '=') {
                token += palabra[iGlobal];
                tablaToken.add("\t" + token + "\t\t\t" + 306 + "\t\t\t\t\t-1\t\t\t\t" + numLin);
                tablaSintaxis.add(token+"$306$-1$"+numLin);
                iGlobal++;
                token = "";
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    private boolean esAritmetico() {

        for (int i = 0; i < aritmeticos.length; i++) {
            if (palabra[iGlobal] == aritmeticos[i]) {
                tablaToken.add("\t" + palabra[iGlobal] + "\t\t\t" + (i + 201) + "\t\t\t\t\t-1\t\t\t\t" + numLin);
                tablaSintaxis.add(palabra[iGlobal]+"$"+(i + 201) +"$-1$"+numLin);
                iGlobal++;
                return true;
            }
        }

        return false;
    }

    private boolean esReservado() {
        if (iGlobal < palabra.length) {
            if ((palabra[iGlobal] >= 'a' && palabra[iGlobal] <= 'z')) {
                token += palabra[iGlobal];
                iGlobal++;
                while (true) {
                    if (iGlobal < palabra.length) {
                        if ((palabra[iGlobal] >= 'a' && palabra[iGlobal] <= 'z') || (palabra[iGlobal] >= '0' && palabra[iGlobal] <= '9')) {
                            token += palabra[iGlobal];
                            iGlobal++;
                        } else {
                            for (int i = 0; i < palabrasReserv.length; i++) {
                                if (token.equals(palabrasReserv[i])) {
                                    tablaToken.add("\t" + token + "\t\t\t" + (i + 501) + "\t\t\t\t\t-1\t\t\t\t" + numLin);
                                    tablaSintaxis.add(token+"$"+(i + 501) +"$-1$"+numLin);
                                    token = "";
                                    return true;

                                }
                            }
                            tablaErrores.add("\t" + idError + "\t\t\t" + token + "\t\t\t" + numLin);
                            idError++;
                            token = "";
                            return true;
                        }
                    } else {
                        for (int i = 0; i < palabrasReserv.length; i++) {
                            if (token.equals(palabrasReserv[i])) {
                                tablaToken.add("\t" + token + "\t\t\t" + (i + 501) + "\t\t\t\t\t-1\t\t\t\t" + numLin);
                                tablaSintaxis.add(token+"$"+(i + 501) +"$-1$"+numLin);
                                token = "";
                                return true;
                            }
                        }
                        tablaErrores.add("\t" + idError + "\t\t\t" + token + "\t\t\t" + numLin);
                        idError++;
                        token = "";
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean esIdentificador() {
        token = "";
        if (iGlobal < palabra.length) {
            if (palabra[iGlobal] == '#') {
                token += "#";
                iGlobal++;
                if (iGlobal < palabra.length && palabra[iGlobal] >= 'a' && palabra[iGlobal] <= 'z') {
                    token += palabra[iGlobal];
                    iGlobal++;
                    while (true) {
                        if (iGlobal < palabra.length) {
                            if ((palabra[iGlobal] >= 'a' && palabra[iGlobal] <= 'z') || palabra[iGlobal] >= '_'
                                    || (palabra[iGlobal] >= '0' && palabra[iGlobal] <= '9')) {
                                token += palabra[iGlobal];
                                iGlobal++;
                            } else {
                                tablaToken.add("\t" + token + "\t\t\t100\t\t\t-2\t\t\t" + numLin);
                                tablaSintaxis.add(token+"$100$-2$"+numLin);
                                token = "";
                                return true;
                            }
                        } else {
                            tablaToken.add("\t" + token + "\t\t\t100\t\t\t-2\t\t\t" + numLin);
                             tablaSintaxis.add(token+"$100$-2$"+numLin);
                            token = "";
                            return true;

                        }
                    }

                } else {
                    iGlobal--;
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;

    }

    private boolean esCaractEsp() {
        token = "";
        if (iGlobal < palabra.length) {
            for (int i = 0; i < especiales.length; i++) {
                if (palabra[iGlobal] == especiales[i]) {
                    tablaToken.add("\t" + palabra[iGlobal] + "\t\t\t" + (i + 601) + "\t\t\t\t\t-1\t\t\t\t" + numLin);
                     tablaSintaxis.add(palabra[iGlobal]+"$"+ (i + 601) +"$-1$"+numLin);
                    iGlobal++;
                    return true;
                }
            }

            return false;
        }
        return false;
    }

    private void error() {
        token = "";
        if (palabra[iGlobal] == '#') {
            token += palabra[iGlobal];
            iGlobal++;
            while (true) {
                if (iGlobal < palabra.length) {
                    if ((palabra[iGlobal] >= 'a' && palabra[iGlobal] <= 'z') || palabra[iGlobal] >= '_'
                            || (palabra[iGlobal] >= '0' && palabra[iGlobal] <= '9')) {
                        token += palabra[iGlobal];
                        iGlobal++;
                    } else {
                        tablaErrores.add("\t" + idError + "\t\t\t" + token + "\t\t\t" + numLin);
                        idError++;
                        token = "";
                        break;

                    }
                } else {
                    tablaErrores.add("\t" + idError + "\t\t\t" + token + "\t\t\t" + numLin);
                    idError++;
                    token = "";
                    break;

                }
            }
        } else {
            token += palabra[iGlobal];
            iGlobal++;
            tablaErrores.add("\t" + idError + "\t\t\t" + token + "\t\t\t" + numLin);
            idError++;
            token = "";
        }

    }

     private boolean cicloNumero(boolean esReal) {

        while (true) {
            if (iGlobal < palabra.length) {
                if (palabra[iGlobal] >= '0' && palabra[iGlobal] <= '9') {
                    token += palabra[iGlobal];
                    iGlobal++;
                } else if (palabra[iGlobal] == '.') {
                    if (esReal) {
                        return false;
                    }
                    esReal = true;
                    token += palabra[iGlobal];
                    iGlobal++;
                    while (true) {
                        if (iGlobal < palabra.length && palabra[iGlobal] >= '0' && palabra[iGlobal] <= '9') {
                            token += palabra[iGlobal];
                            iGlobal++;

                        } else {
                            tablaToken.add("\t" + token + "\t\t\t900\t\t\t\t\t-1\t\t\t\t" + numLin);
                            tablaSintaxis.add(token+"$900$-1$"+numLin);
                            token = "";
                            return true;
                        }
                    }

                } else {
                    tablaToken.add("\t" + token + "\t\t\t800\t\t\t\t\t-1\t\t\t\t" + numLin);
                    tablaSintaxis.add(token+"$800$-1$"+numLin);
                    token = "";
                    return true;
                }
            } else {
                tablaToken.add("\t" + token + "\t\t\t800\t\t\t\t\t-1\t\t\t\t" + numLin);
                tablaSintaxis.add(token+"$800$-1$"+numLin);
                token = "";
                return true;
            }

        }
    }
    
    public void impArchivo()
    {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("./tablaTokens.txt");
            pw = new PrintWriter(fichero);

            for (int i = 0; i < tablaToken.size(); i++)
                pw.println(tablaToken.get(i));
            
               fichero.close();
                          
            fichero = new FileWriter("./tablaErrores.txt");
            pw = new PrintWriter(fichero);

            for (int i = 0; i < tablaErrores.size(); i++)
                pw.println(tablaErrores.get(i));
            
           fichero.close();
            
             fichero = new FileWriter("./tablaSintaxis.txt");
            pw = new PrintWriter(fichero);

            for (int i = 0; i < tablaSintaxis.size(); i++)
                pw.println(tablaSintaxis.get(i));
            
             fichero.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
