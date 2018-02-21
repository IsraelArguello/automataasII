package automatasii;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Proyecto {

    private int numLin, iGlobal, numConsec;
    private String palabrasReserv[], reserv, aritmeticos[], logicos[], relacionales[], especiales[], identificador;
    private ArrayList<String> tablaToken, tablaErrores;
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
        identificador = "";
        reserv = "";

    }

    public void leerArchivo() throws IOException {
        tablaToken.add("\tPalabra\t\tN° Token\tPosicion en tabla\tN° Linea");
        tablaErrores.add("\tN° Consecutivo\t\tError\t\tN° Linea");
        File file = new File("/automatasii/files/entrada.txt");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String cadena = "";
        while (br.ready()) {
            numLin++;
            cadena = br.readLine();
            System.out.println(cadena);
            String spli[] = cadena.split(" ");
            if (!cadena.equals("")) {
                for (int i = 0; i < spli.length; i++) {
                    estadoCero(spli[i].toLowerCase());

                }
            }

        }

        //impFile();
        br.close();
    }

    private void estadoCero(String palab) {
        iGlobal = 0;
        palabra = palab.toCharArray();
        while (iGlobal < palabra.length) {
            if (!esIdentificador()) {
                if (!esReservado()) {

                }
            }
        }

    }

    private boolean esReservado() {
        if (iGlobal < palabra.length) {
            if (palabra[iGlobal] >= 'a' && palabra[iGlobal] <= 'z') {
                reserv += palabra[iGlobal];
                iGlobal++;
                while (true) {
                    if (iGlobal < palabra.length) {
                        if ((palabra[iGlobal] >= 'a' && palabra[iGlobal] <= 'z')) {
                            reserv += palabra[iGlobal];
                            iGlobal++;
                        } else {
                            for (int i = 0; i >= (palabrasReserv.length); i++) {
                                if (reserv.equals(palabrasReserv[i])) {
                                    tablaToken.add("\t" + reserv + "\t1" + (i + 501) + "\t\t\t-2\t\t\t" + numLin);
                                    reserv = "";
                                    return true;
                                }
                            }
                            error();
                        }
                    } else {
                        for (int i = 0; i >= (palabrasReserv.length); i++) {
                            if (reserv.equals(palabrasReserv[i])) {
                                tablaToken.add("\t" + reserv + "\t1" + (i + 501) + "\t\t\t-2\t\t\t" + numLin);
                                reserv = "";
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
                identificador += "#";
                iGlobal++;
                if (palabra[iGlobal] >= 'a' && palabra[iGlobal] <= 'z') {
                    identificador += palabra[iGlobal];
                    iGlobal++;
                    while (true) {
                        if (iGlobal < palabra.length) {
                            if ((palabra[iGlobal] >= 'a' && palabra[iGlobal] <= 'z') || palabra[iGlobal] >= '_'
                                    || (palabra[iGlobal] >= '0' && palabra[iGlobal] <= '9')) {
                                identificador += palabra[iGlobal];
                                iGlobal++;
                            } else {
                                tablaToken.add("\t" + identificador + "\t100\t\t\t-2\t\t\t" + numLin);
                                identificador = "";
                                return true;
                            }
                        } else {
                            tablaToken.add("\t" + identificador + "\t100\t\t\t-2\t\t\t" + numLin);
                            identificador = "";
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

    private void siIdentificador() {

    }

    private void esReservada() {

        for (int i = 0; i < tablaToken.size(); i++) {
            System.out.println(tablaToken.get(i));
        }

    }

    private void esDelimitador() {
        if (iGlobal < palabra.length) {
            if (palabra[iGlobal] == '-' || palabra[iGlobal] == '+' || palabra[iGlobal] == '/'
                    || palabra[iGlobal] == '*' || palabra[iGlobal] == '%' || palabra[iGlobal] == '#') {

                esIdentificador();
            }
        }
    }

    private void esAritmetico() {
        // TODO Auto-generated method stub

    }

    private void esNumero(String palabra) {
        // TODO Auto-generated method stub

    }

    public static void main(String[] a) {
        try {
            new Proyecto().leerArchivo();
        } catch (IOException e) {
            System.out.println("Archivo no encontrado");
        }
    }

}
