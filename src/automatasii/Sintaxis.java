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
import java.util.Arrays;

/**
 *
 * @author L55-C5211R
 */
public class Sintaxis {

    private int tipo;
    private int avanza, ambito;
    private final ArrayList<String[]> arregloTokens;
    private ArrayList<String> tablaSimbolos, tablaSimbolosPesos, tablaDirecciones, tablaDireccionesPesos;
    private String arregloCadenas[];
    private final String tipos[], opAritmeticos[], opRelacionales[], opLogicos[];

    public Sintaxis() {
        avanza = 0;
        tipo = 0;
        ambito = 0;
        arregloTokens = new ArrayList<>();
        tablaSimbolos = new ArrayList<>();
        tablaSimbolosPesos = new ArrayList<>();
        tablaDirecciones = new ArrayList<>();
        tablaDireccionesPesos = new ArrayList<>();
        tipos = new String[]{"integer", "real", "string"};
        opAritmeticos = new String[]{"+", "-", "*", "/", "%"};
        opRelacionales = new String[]{"<", "<=", ">", ">=", "!=", "=="};
        opLogicos = new String[]{"&&", "||", "!"};
    }

    public void leerArchivo() {
        tablaSimbolos.add("Id\tToken\tDim1\tDim2\tAmbito");
        tablaDirecciones.add("Id\tToken\t#Linea\tVCI");
        String cadena = "";
        FileReader fr = null;
        try {
            File file = new File("./tablaSintaxis.txt");
            fr = new FileReader(file);
            try (BufferedReader br = new BufferedReader(fr)) {
                while (br.ready()) {
                    cadena = br.readLine();
                    if (!cadena.equals("")) {
                        arregloCadenas = cadena.split("\\$");
                        arregloTokens.add(arregloCadenas);
                    }
                }
                prog(arregloTokens.get(0)[0]);
            }
        } catch (IOException e) {
            System.out.println("Archivo no encontrado");
        }

    }

    private void prog(String cadena) {
        if (cadena.equals("program")) {
            avanza++;
            if (arregloTokens.get(avanza)[1].equals("100")) {
                arregloTokens.get(avanza)[1] = "105";
                arregloTokens.get(avanza)[2] = "0";
                tablaDirecciones.add(arregloTokens.get(avanza)[0] + "\t" + arregloTokens.get(avanza)[1] + "\t"
                        + arregloTokens.get(avanza)[3] + "\t" + ambito);
                tablaDireccionesPesos.add(arregloTokens.get(avanza)[0] + "$" + arregloTokens.get(avanza)[1] + "$"
                        + arregloTokens.get(avanza)[3] + "$" + ambito);
                avanza++;
                declararVar();
                metodo();
                if (arregloTokens.get(avanza)[0].equals("{")) {
                    avanza++;
                    estatuto();
                    if (arregloTokens.get(avanza)[0].equals("}")) {
                        avanza++;
                    } else {
                        error("Error: se esperaba una }");
                    }
                } else {
                    error("Error: se esperaba una {");
                }
            }
        }
    }

    private void declararVar() {
        String dims[] = new String[]{"0", "0"};
        String tokenTem = "";
        int i = 0, indexTok = 0;
        do {
            dims = new String[]{"0", "0"};
            if (tipo()) {
                avanza++;
                do {
                    if (arregloTokens.get(avanza)[0].equals(",")) {
                        avanza++;
                    }
                    if (arregloTokens.get(avanza)[1].equals("100")) {
                        tokenTem = arregloTokens.get(avanza)[0];
                        indexTok = avanza;
                        avanza++;
                        if (arregloTokens.get(avanza)[0].contains("[")) {
                            do {
                                avanza++;
                                if (arregloTokens.get(avanza)[1].equals("800")) {
                                    dims[i] = arregloTokens.get(avanza)[0];
                                    avanza++;
                                    i++;
                                } else {
                                    error("Se esperaba una constante entera");
                                }
                            } while (arregloTokens.get(avanza)[0].equals(","));
                            if (arregloTokens.get(avanza)[0].contains("]")) {
                                avanza++;
                                if (arregloTokens.get(avanza)[0].contains(";")) {
                                    avanza++;
                                    tablaSimbolos.add(tokenTem + "\t" + tipo + "\t0\t" + dims[0] + "\t" + dims[1] + "\t"
                                            + ambito);
                                    tablaSimbolosPesos.add(
                                            tokenTem + "$" + tipo + "$0$" + dims[0] + "$" + dims[1] + "$" + ambito);
                                    arregloTokens.get(indexTok)[2] = tablaSimbolos.size() - 1 + "";
                                    arregloTokens.get(indexTok)[1] = tipo + "";
                                } else {
                                    error("Se esperaba ;");
                                }
                            } else {
                                error("Se esperaba ]");
                            }
                        } else if (arregloTokens.get(avanza)[0].contains(";")) {
                            avanza++;
                            tablaSimbolos
                                    .add(tokenTem + "\t" + tipo + "\t0\t" + dims[0] + "\t" + dims[1] + "\t" + ambito);
                            tablaSimbolosPesos
                                    .add(tokenTem + "$" + tipo + "$0$" + dims[0] + "$" + dims[1] + "$" + ambito);
                            arregloTokens.get(indexTok)[2] = tablaSimbolos.size() - 1 + "";
                            arregloTokens.get(indexTok)[1] = tipo + "";

                        }
                    } else {
                        error("Se esperaba una variable");
                    }
                } while (arregloTokens.get(avanza)[0].equals(","));
            }
        } while (tipo());
    }

    private void metodo() {
        String dims[] = new String[]{"0", "0"};
        do {
            if (arregloTokens.get(avanza)[0].equals("procedure")) {
                avanza++;
                ambito++;
                tablaDirecciones
                        .add(arregloTokens.get(avanza)[0] + "\t105\t" + arregloTokens.get(avanza)[3] + "\t" + ambito);
                tablaDireccionesPesos
                        .add(arregloTokens.get(avanza)[0] + "$105$" + arregloTokens.get(avanza)[3] + "$" + ambito);
                arregloTokens.get(avanza)[2] = tablaDirecciones.size() - 1 + "";
                arregloTokens.get(avanza)[1] = "105";
                if (arregloTokens.get(avanza)[1].equals("105")) {
                    avanza++;
                    if (arregloTokens.get(avanza)[0].equals("(")) {
                        do {
                            avanza++;
                            if (tipo()) {
                                avanza++;
                                if (arregloTokens.get(avanza)[1].equals("100")) {
                                    tablaSimbolos.add(arregloTokens.get(avanza)[0] + "\t" + tipo + "\t0\t" + dims[0] + "\t" + dims[1] + "\t"
                                            + ambito);
                                    tablaSimbolosPesos.add(
                                            arregloTokens.get(avanza)[0] + "$" + tipo + "$0$" + dims[0] + "$" + dims[1] + "$" + ambito);
                                    arregloTokens.get(avanza)[2] = tablaSimbolos.size() - 1 + "";
                                    arregloTokens.get(avanza)[1] = tipo + "";
                                    avanza++;
                                } else {
                                    error("Se esperaba un identificador");
                                }
                            }
                        } while (avanza < arregloTokens.size() - 1 && arregloTokens.get(avanza)[0].equals(","));
                        if (arregloTokens.get(avanza)[0].equals(")")) {
                            avanza++;
                            declararVar();
                            if (arregloTokens.get(avanza)[0].equals("{")) {
                                avanza++;
                                estatuto();
                                if (arregloTokens.get(avanza)[0].equals("}")) {
                                    avanza++;
                                } else {
                                    error("Error: se esperaba una }");
                                }
                            } else {
                                error("Error: se esperaba una {");
                            }
                        } else {
                            error("Error: se esperaba una )");
                        }

                    } else {
                        error("Error: se esperaba una (");
                    }

                } else {
                    error("Error: se esperaba un identificador");
                }

            } else {
                error("Error: se esperaba un palabra reservada \"procedure\"");
            }

        } while (avanza < arregloTokens.size() - 1 && arregloTokens.get(avanza)[0].equals("procedure"));
    }

    private void idArreglo() {
        if (arregloTokens.get(avanza)[1].equals("100")) {
            if (modifTabToken(arregloTokens.get(avanza)[0])) {
                avanza++;
                if (arregloTokens.get(avanza)[0].equals("[")) {
                    do {
                        avanza++;
                        modifTabToken(arregloTokens.get(avanza)[0]);
                        if (arregloTokens.get(avanza)[1].equals("101") || arregloTokens.get(avanza)[1].equals("800")) {
                            avanza++;
                        } else {
                            error("Se esperaba una o más variable entera");
                        }
                    } while (arregloTokens.get(avanza)[0].equals(","));

                    if (arregloTokens.get(avanza)[0].equals("]")) {
                        avanza++;
                    } else {
                        error("Se esperaba ]");
                    }
                }
            }
        } else {
            error("Se esperaba un identificador");
        }
    }

    public void asigna() {
        idArreglo();
        if (arregloTokens.get(avanza)[0].equals("=")) {
            avanza++;
            exp_Arit();
            if (arregloTokens.get(avanza)[0].equals(";")) {
                avanza++;
            } else {
                error("Se esperaba ;");
            }
        } else {
            error("Se esperaba =");
        }
    }

    private void exp_Arit() {

        while (arregloTokens.get(avanza)[0].equals("(")) {
            avanza++;
        }
        if (constante()) {
            avanza++;
        } else {
            idArreglo();
        }
        while (arregloTokens.get(avanza)[0].equals(")")) {
            avanza++;
        }

        if (aritmeticos()) {
            avanza++;
            exp_Arit();
        }

    }

    private void estatuto() {
        do {
            if (arregloTokens.get(avanza)[1].equals("100")) {
                asigna();
            } else if (arregloTokens.get(avanza)[0].equals("input")) {
                avanza++;
                leer();
            } else if (arregloTokens.get(avanza)[0].equals("output")) {
                avanza++;
                escribir();
            } else if (arregloTokens.get(avanza)[0].equals("if")) {
                avanza++;
                si();
            } else if (arregloTokens.get(avanza)[0].equals("repeat")) {
                avanza++;
                repetir();
            } else if (arregloTokens.get(avanza)[0].equals("while")) {
                avanza++;
                mientras();
            } else if (arregloTokens.get(avanza)[0].equals("call")) {
                avanza++;
                ejecutar();
            } else {
                error("Error: Se esperaba un estatuto");
            }
        } while (arregloTokens.get(avanza)[0].equals("call") || arregloTokens.get(avanza)[0].equals("while")
                || arregloTokens.get(avanza)[0].equals("repeat") || arregloTokens.get(avanza)[0].equals("if")
                || arregloTokens.get(avanza)[0].equals("output") || arregloTokens.get(avanza)[0].equals("input")
                || arregloTokens.get(avanza)[1].contains("100"));
    }

    private boolean tipo() {

        for (int i = 0; i < tipos.length; i++) {
            if (arregloTokens.get(avanza)[0].equals(tipos[i])) {
                tipo = 101 + i;
                return true;
            }
        }
        return false;
    }

    private boolean aritmeticos() {
        for (String aritmetico : opAritmeticos) {
            if (arregloTokens.get(avanza)[0].equals(aritmetico)) {
                return true;
            }
        }
        return false;
    }

    private boolean relacionales() {
        for (String relacional : opRelacionales) {
            if (arregloTokens.get(avanza)[0].equals(relacional)) {
                return true;
            }
        }
        return false;
    }

    private boolean logicos() {
        for (String logico : opLogicos) {
            if (arregloTokens.get(avanza)[0].equals(logico)) {
                return true;
            }
        }
        return false;
    }

    private boolean constante() {
        return arregloTokens.get(avanza)[1].equals("800") || arregloTokens.get(avanza)[1].equals("1000")
                || arregloTokens.get(avanza)[1].equals("900");
    }

    private void error(String error) {
        System.out.println(error + "\nLinea:" + arregloTokens.get(avanza)[3]);
        System.exit(0);
    }

    private void leer() {
        if (arregloTokens.get(avanza)[0].equals("input")) {
            avanza++;
            if (arregloTokens.get(avanza)[0].equals("(")) {
                do {
                    avanza++;
                    idArreglo();
                } while (arregloTokens.get(avanza)[0].equals(","));
                if (arregloTokens.get(avanza)[0].equals(")")) {
                    avanza++;
                    if (arregloTokens.get(avanza)[0].equals(";")) {
                        avanza++;
                    } else {
                        error("Se esperaba un ;");
                    }
                } else {
                    error("Se esperaba un )");
                }
            } else {
                error("Se esperaba un (");
            }
        } else {
            error("Se esperaba un input");
        }
    }

    private void escribir() {
        if (arregloTokens.get(avanza)[0].equals("output")) {
            avanza++;
            if (arregloTokens.get(avanza)[0].equals("(")) {
                do {
                    avanza++;
                    if (arregloTokens.get(avanza)[0].equals(",")) {
                        avanza++;
                    }
                    if (arregloTokens.get(avanza)[1].equals("100")) {
                        idArreglo();
                    } else if (arregloTokens.get(avanza)[1].equals("800") || arregloTokens.get(avanza)[1].equals("1000")
                            || arregloTokens.get(avanza)[1].equals("900")) {
                        constante();
                    } else {
                        error("Se esperaba una constante");
                    }

                } while (arregloTokens.get(avanza)[0].equals(","));
                if (arregloTokens.get(avanza)[0].equals(")")) {
                    avanza++;
                    if (arregloTokens.get(avanza)[0].equals(";")) {
                        avanza++;
                    } else {
                        error("Se esperaba un ;");
                    }
                } else {
                    error("Se esperaba un )");
                }
            } else {
                error("Se esperaba un (");
            }
        } else {
            error("Se esperaba un input");
        }
    }

    private void si() {
        if (arregloTokens.get(avanza)[0].equals("if")) {
            avanza++;
            condicion();
            if (arregloTokens.get(avanza)[0].equals("then")) {
                avanza++;
                if (arregloTokens.get(avanza)[0].equals("{")) {
                    avanza++;
                    estatuto();
                    if (arregloTokens.get(avanza)[0].equals("else")) {
                        avanza++;
                        estatuto();
                    }
                    if (arregloTokens.get(avanza)[0].equals("}")) {
                        avanza++;
                        if (arregloTokens.get(avanza)[0].equals(";")) {
                            avanza++;
                        } else {
                            error("Se esperaba un ;");
                        }
                    } else {
                        error("Se esperaba un }");
                    }
                } else {
                    error("Se esperaba un }");
                }
            } else {
                error("Se esperana then");
            }
        }
    }

    private void repetir() {
        if (arregloTokens.get(avanza)[0].equals("repeat")) {
            avanza++;
            if (arregloTokens.get(avanza)[0].equals("{")) {
                avanza++;
                estatuto();
                if (arregloTokens.get(avanza)[0].equals("}")) {
                    avanza++;
                    if (arregloTokens.get(avanza)[0].equals("until")) {
                        avanza++;
                    } else {
                        error("Falta token until");
                    }
                    condicion();
                    if (arregloTokens.get(avanza)[0].equals(";")) {
                        avanza++;
                    } else {
                        error("Se esperaba un ;");
                    }
                } else {
                    error("Se esperaba un }");
                }
            } else {
                error("Se esperaba un {");
            }
        } else {
            error("Se esperaba el estatuto repetir");
        }
    }

    private void mientras() {
        if (arregloTokens.get(avanza)[0].equals("while")) {
            avanza++;
            condicion();
            if (arregloTokens.get(avanza)[0].equals("{")) {
                avanza++;
                estatuto();
                if (arregloTokens.get(avanza)[0].equals("}")) {
                    avanza++;
                    if (arregloTokens.get(avanza)[0].equals(";")) {
                        avanza++;
                    } else {
                        error("Se esperaba un ;");
                    }
                } else {
                    error("Se esperaba un }");
                }
            } else {
                error("Se esperana un {");
            }
        } else {
            error("Se esperaba el estatuto mientras");
        }
    }

    private void ejecutar() {
        if (arregloTokens.get(avanza)[0].equals("call")) {
            avanza++;
            if (arregloTokens.get(avanza)[1].equals("100")) {
                if (modifTabTokenDir(arregloTokens.get(avanza)[0])) {
                    avanza++;
                    if (arregloTokens.get(avanza)[0].equals("(")) {
                        do {
                            avanza++;
                            if (arregloTokens.get(avanza)[1].equals("100")) {
                                if (modifTabToken(arregloTokens.get(avanza)[0])) {
                                    avanza++;
                                }
                            } else {
                                error("Se esperaba un identificador");
                            }
                        } while (arregloTokens.get(avanza)[0].equals(","));
                        if (arregloTokens.get(avanza)[0].equals(")")) {
                            avanza++;
                        } else {
                            error("Se esperaba un )");
                        }
                    }
                    if (arregloTokens.get(avanza)[0].equals(";")) {
                        avanza++;
                    } else {
                        error("Se esperaba un ;");
                    }
                }
            } else {
                error("Se esperaba un identificador de metodo");
            }
        } else {
            error("Se esperaba el estatuto llamar");
        }
    }

    private void condicion() {

        while (arregloTokens.get(avanza)[0].equals("[")) {
            avanza++;
        }
        if (arregloTokens.get(avanza)[0].equals("!")) {
            avanza++;
        }
        exp_Arit();
        if (relacionales()) {
            avanza++;
        } else {
            error("se esperaba un operador relacional");
        }
        exp_Arit();
        while (arregloTokens.get(avanza)[0].equals("]")) {
            avanza++;
        }
        if (logicos()) {
            avanza++;
            condicion();

        }

    }

    private boolean modifTabToken(String nombreTok) {
        for (int tok = tablaSimbolosPesos.size() - 1; tok >= 0; tok--) {
            String separa[] = tablaSimbolosPesos.get(tok).split("\\$");
            if (nombreTok.equals(separa[0])) {
                arregloTokens.get(avanza)[1] = separa[1];
                arregloTokens.get(avanza)[2] = tok + 1 + "";
                return true;
            }
        }
        error("Variable no declarada en este ambito");
        return false;
    }

    private boolean modifTabTokenDir(String nombreTok) {
        for (int tok = tablaDireccionesPesos.size() - 1; tok >= 1; tok--) {
            String separa[] = tablaDireccionesPesos.get(tok).split("\\$");
            if (nombreTok.equals(separa[0])) {
                arregloTokens.get(avanza)[1] = separa[1];
                arregloTokens.get(avanza)[2] = tok + 1 + "";
                return true;
            }
        }
        error("Variable no declarada en este ambito");
        return false;
    }

    public void impArchivo() {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("./tablaTokens.txt");
            pw = new PrintWriter(fichero);

            for (int i = 0; i < arregloTokens.size(); i++) {
                if (i == 0) {
                    pw.println("\t Token\t Num. Token\t Posición tabla\t Num. Linea");
                } else {
                    pw.println("\t" + arregloTokens.get(i)[0] + "\t\t" + arregloTokens.get(i)[1] + "\t\t"
                            + arregloTokens.get(i)[2] + "\t\t" + arregloTokens.get(i)[3]);
                }
            }

            fichero.close();

            fichero = new FileWriter("./tablaDirecciones.txt");
            pw = new PrintWriter(fichero);

            for (int i = 0; i < tablaDirecciones.size(); i++) {
                pw.println(tablaDirecciones.get(i));
            }

            fichero.close();

            fichero = new FileWriter("./tablaSimbolos.txt");
            pw = new PrintWriter(fichero);

            for (int i = 0; i < tablaSimbolos.size(); i++) {
                pw.println(tablaSimbolos.get(i));
            }

            fichero.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
