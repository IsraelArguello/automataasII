package automatasii;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Proyecto {

    private int numLin, iGlobal, numConsec, idError;
    private String palabrasReserv[], token;
    private char aritmeticos[], especiales[];
    public ArrayList<String> tablaToken, tablaErrores;
    private char palabra[];
    private String cadena;
    private BufferedReader br;

    public Proyecto() {
        numLin = 0;
        numConsec = 1;
        palabrasReserv = new String[]{"program", "begin", "end", "input", "output", "integer", "real", "char",
            "string", "if", "else", "then", "while", "do", "repeat", "until", "var", "procedure", "call"};
        tablaToken = new ArrayList<String>();
        tablaErrores = new ArrayList<String>();
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

        //impFile();
        br.close();
    }

    private void estadoCero(String palab) {
        token="";
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
                                                error();
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

    private boolean esLogico() {
        token="";
        if (iGlobal < palabra.length) {
            if (palabra[iGlobal] == '&' || palabra[iGlobal] == '|' || palabra[iGlobal] == '!') {
                if (palabra[iGlobal] == '&' && iGlobal < (palabra.length - 1) && palabra[iGlobal + 1] == '&') {
                    token += palabra[iGlobal];
                    token += palabra[iGlobal + 1];
                    iGlobal += 2;
                    tablaToken.add("\t" + token + "\t\t\t401\t\t\t\t\t-1\t\t\t\t" + numLin);
                    token = "";
                    return true;
                }
                if (palabra[iGlobal] == '|' && iGlobal < (palabra.length - 1) && palabra[iGlobal + 1] == '|') {
                    token += palabra[iGlobal];
                    token += palabra[iGlobal + 1];
                    iGlobal += 2;
                    tablaToken.add("\t" + token + "\t\t\t402\t\t\t\t\t-1\t\t\t\t" + numLin);
                    token = "";
                    return true;
                }
                if (palabra[iGlobal] == '!' && iGlobal < (palabra.length - 1) && palabra[iGlobal + 1] != '=') {
                    token += palabra[iGlobal];
                    iGlobal++;
                    tablaToken.add("\t" + token + "\t\t\t403\t\t\t\t\t-1\t\t\t\t" + numLin);
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
            token = "";
            return true;
        }

    }

    private boolean esComentario() {
int iGlobaltemp=iGlobal;
token="";
        if (palabra[iGlobal] == '/') {
            token += palabra[iGlobal];
            iGlobal++;
            if (iGlobal<palabra.length && palabra[iGlobal] == '/') {
                token += palabra[iGlobal];
                iGlobal++;
                while (true) {
                    if (iGlobal < palabra.length) {
                        if (palabra[iGlobal] == '/') {
                            token += palabra[iGlobal];
                            iGlobal++;
                            if (iGlobal<palabra.length && palabra[iGlobal] == '/') {
                                token += palabra[iGlobal];
                                tablaToken.add("\t" + token + "\t\t\t700\t\t\t\t\t-1\t\t\t\t" + numLin);
                                iGlobal++;
                                token = "";
                                return true;
                            } else {
                                iGlobal=iGlobaltemp;
                                return false;
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
token="";
        if (palabra[iGlobal] == '+' || palabra[iGlobal] == '-') {
            if (iGlobal==0 || (!(palabra[iGlobal - 1] >= '0' && palabra[iGlobal - 1] <= '9') || palabra[iGlobal - 1] == ' ')) {
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
token="";
        if (palabra[iGlobal] == '<') {
            token += palabra[iGlobal];
            iGlobal++;
            if (iGlobal < palabra.length && palabra[iGlobal] == '=') {
                token += palabra[iGlobal];
                iGlobal++;
                tablaToken.add("\t" + token + "\t\t\t" + 303 + "\t\t\t\t\t-1\t\t\t\t" + numLin);
                token = "";
                return true;
            } else {
                tablaToken.add("\t" + token + "\t\t\t" + 301 + "\t\t\t\t\t-1\t\t\t\t" + numLin);
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
                iGlobal++;
                token = "";
                return true;
            } else {
                tablaToken.add("\t" + token + "\t\t\t" + 302 + "\t\t\t\t\t-1\t\t\t\t" + numLin);
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
                iGlobal++;
                return true;
            }
        }

        return false;
    }

    private boolean revisionAritmeticos(char signo) {
        for (int i = 0; i < aritmeticos.length; i++) {
            if (signo == aritmeticos[i]) {
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
token="";
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
                                token = "";
                                return true;
                            }
                        } else {
                            tablaToken.add("\t" + token + "\t\t\t100\t\t\t-2\t\t\t" + numLin);
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
                if (palabra[iGlobal]==especiales[i]) {
                    tablaToken.add("\t" + palabra[iGlobal] + "\t\t\t" + (i + 601) + "\t\t\t\t\t-1\t\t\t\t" + numLin);
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

    public static void main(String[] a) {
        Proyecto p = new Proyecto();
        try {
            p.leerArchivo();
        } catch (IOException e) {
            System.out.println("Archivo no encontrado");
        }
        for (int i = 0; i < p.tablaToken.size(); i++) {
            System.out.println(p.tablaToken.get(i));
        }
        System.out.println("\n\n\n");
        for (int i = 0; i < p.tablaErrores.size(); i++) {
            System.out.println(p.tablaErrores.get(i));
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
                        if (iGlobal<palabra.length && palabra[iGlobal] >= '0' && palabra[iGlobal] <= '9') {
                            token += palabra[iGlobal];
                            iGlobal++;

                        } else {
                            tablaToken.add("\t" + token + "\t\t\t900\t\t\t\t\t-1\t\t\t\t" + numLin);
                            token = "";
                            return true;
                        }
                    }

                } else {
                    tablaToken.add("\t" + token + "\t\t\t800\t\t\t\t\t-1\t\t\t\t" + numLin);
                    token = "";
                    return true;
                }
            } else {
                tablaToken.add("\t" + token + "\t\t\t800\t\t\t\t\t-1\t\t\t\t" + numLin);
                token = "";
                return true;
            }

        }
    }

}
