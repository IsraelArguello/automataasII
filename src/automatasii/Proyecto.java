package automatasii;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Proyecto {

    private int numLin, iGlobal, numConsec;
    private String palabrasReserv[], aritmeticos[], logicos[], relacionales[], especiales[], token;
    public ArrayList<String> tablaToken, tablaErrores;
    private char palabra[];

    public Proyecto() {
        numLin = 0;
        numConsec = 1;
        palabrasReserv = new String[]{"program", "begin", "end", "input", "output", "integer", "real", "char",
            "string", "if", "else", "then", "while", "do", "repeat", "until", "var", "procedure", "call"};
        tablaToken = new ArrayList<String>();
        tablaErrores = new ArrayList<String>();
        aritmeticos = new String[]{"-", "+", "/", "*", "%"};
        relacionales = new String[]{"<", ">", "<=", ">=", "==", "!="};
        logicos = new String[]{"||", "&&", "!"};
        especiales = new String[]{"(", ")", ";", ",", "=", "[", "]", "{", "}"};
        iGlobal = 0;
        token = "";

    }

    public void leerArchivo() throws IOException {
        tablaToken.add("\tPalabra\t\tN° Token\tPosicion en tabla\tN° Linea");
        tablaErrores.add("\tN° Consecutivo\t\tError\t\tN° Linea");
        File file = new File("./entrada1.txt");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String cadena = "";
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
        iGlobal = 0;
        palabra = palab.toCharArray();
        while (iGlobal < palabra.length) {
            if (palabra[iGlobal] != ' ') {
                if (!esIdentificador()) {
                    if (!esReservado()) {
                        if (!esNumero()) {

                        }
                    }
                }
            } else {
                iGlobal++;
            }
        }

    }

    private boolean esNumero() {

        if (palabra[iGlobal] == '+' || palabra[iGlobal] == '-') {
            if (!(palabra[iGlobal - 1] <= '0' && palabra[iGlobal - 1] <= '9') || palabra[iGlobal - 1] <= ' ') {
                token+=palabra[iGlobal];
                iGlobal++;
                if (cicloNumero(false)) {
                        return true;
                }
            }
        } else if (palabra[iGlobal] <= '0' || palabra[iGlobal - 1] <= '9') {
            if (cicloNumero(false)) {
                return true;
            }
        } else if (palabra[iGlobal] == '.') {
            if (cicloNumero(true)) {
                return true;
            }
        }

        return false;
    }

    private boolean esReservado() {
        if (iGlobal < palabra.length) {
            if (palabra[iGlobal] >= 'a' && palabra[iGlobal] <= 'z') {
                token += palabra[iGlobal];
                iGlobal++;
                while (true) {
                    if (iGlobal < palabra.length) {
                        if ((palabra[iGlobal] >= 'a' && palabra[iGlobal] <= 'z')) {
                            token += palabra[iGlobal];
                            iGlobal++;
                        } else {
                            for (int i = 0; i < palabrasReserv.length; i++) {
                                if (token.equals(palabrasReserv[i])) {
                                    tablaToken.add("\t" + token + "\t\t" + (i + 501) + "\t\t\t-1\t\t\t" + numLin);
                                    token = "";
                                    return true;

                                }
                            }
                            error();
                        }
                    } else {
                        for (int i = 0; i < palabrasReserv.length; i++) {
                            if (token.equals(palabrasReserv[i])) {
                                tablaToken.add("\t" + token + "\t\t" + (i + 501) + "\t\t\t-1\t\t\t" + numLin);
                                token = "";
                                return true;
                            }
                        }
                        error();
                    }
                }
            }
        }
        return false;
    }

    private boolean esIdentificador() {

        if (iGlobal < palabra.length) {
            if (palabra[iGlobal] == '#') {
                token += "#";
                iGlobal++;
                if (palabra[iGlobal] >= 'a' && palabra[iGlobal] <= 'z') {
                    token += palabra[iGlobal];
                    iGlobal++;
                    while (true) {
                        if (iGlobal < palabra.length) {
                            if ((palabra[iGlobal] >= 'a' && palabra[iGlobal] <= 'z') || palabra[iGlobal] >= '_'
                                    || (palabra[iGlobal] >= '0' && palabra[iGlobal] <= '9')) {
                                token += palabra[iGlobal];
                                iGlobal++;
                            } else {
                                tablaToken.add("\t" + token + "\t100\t\t\t-2\t\t\t" + numLin);
                                token = "";
                                return true;
                            }
                        } else {
                            tablaToken.add("\t" + token + "\t100\t\t\t-2\t\t\t" + numLin);
                            token = "";
                            return true;

                        }
                    }

                } else {
                    error();
                }
            } else {
                return false;
            }
        }
        return false;

    }

    private void error() {
        String error = "";
        for (int i = 0; i < palabra.length; i++) {
            error += palabra[i];
        }

        tablaErrores.add(numConsec + "\t" + error + "\t\t" + numLin);

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
    }

    private boolean cicloNumero(boolean esReal) {

     

        while (true) {
            if (iGlobal < palabra.length) {
                if (palabra[iGlobal] >= '0' && palabra[iGlobal] <= '9') {
                    token += palabra[iGlobal];
                     iGlobal++;
                } else if (palabra[iGlobal] == '.') {
                    if (esReal) {
                        error();
                        return false;
                    }
                    esReal = true;
                    token += palabra[iGlobal];
                    iGlobal++;
                    while (true) {
                        if (palabra[iGlobal] >= '0' || palabra[iGlobal] <= '9') {
                            token += palabra[iGlobal];
                            iGlobal++;

                        } else {
                            tablaToken.add("\t\t" + token + "\t900\t\t\t-1\t\t\t" + numLin);
                            token="";
                            return true;
                        }
                    }

                } else {
                    tablaToken.add("\t\t" + token + "\t800\t\t\t-1\t\t\t" + numLin);
                     token="";
                    return true;
                }
            }
        }
    }

}
