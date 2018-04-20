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
    private final ArrayList<String[]> arregloTokens;
    private ArrayList<String> tablaSimbolos, tablaDirecciones;
    private String arregloCadenas[];
    private final String tipos[], opAritmeticos[], opRelacionales[];

    public Sintaxis() {
        avanza = 0;
        numLinea = 0;
        arregloTokens = new ArrayList<>();
        tablaSimbolos = new ArrayList<>();
        tablaDirecciones = new ArrayList<>();
        tipos = new String[]{"integer", "real", "string"};
        opAritmeticos = new String[]{"+", "-", "*", "/", "%"};
        opRelacionales = new String[]{"<", "<=", ">", ">=", "!=", "=="};
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
                        numLinea++;
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
                avanza++;
                declararVar();
                metodo();
                if (arregloTokens.get(avanza)[0].equals("{")) {
                    avanza++;
                    //metodo estatutos
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
        do {
            if (tipo()) {
                avanza++;
                do {
                    if (arregloTokens.get(avanza)[1].equals("100")) {
                        avanza++;
                        if (arregloTokens.get(avanza)[0].contains("[")) {
                            do {
                                avanza++;
                                if(arregloTokens.get(avanza)[1].equals("800")){
                                    avanza++;
                                }else{
                                    error("Se esperaba una constante entera");
                                }
                            } while (arregloTokens.get(avanza)[0].equals(","));
                            if (arregloTokens.get(avanza)[0].contains("]")) {
                                avanza++;
                                if (arregloTokens.get(avanza)[0].contains(";")) {
                                    avanza++;
                                } else {
                                    error("Se esperaba ;");
                                }
                            } else {
                                error("Se esperaba ]");
                            }
                        } else if (arregloTokens.get(avanza)[0].contains(";")) {
                            avanza++;
                        }
                    }
                } while (arregloTokens.get(avanza)[0].equals(","));
            }
        } while (tipo());
    }

    private void metodo() {
        do {
            if (arregloTokens.get(avanza)[0].equals("procedure")) {
                avanza++;
                if (arregloTokens.get(avanza)[0].contains("#")) {
                    avanza++;
                    if (arregloTokens.get(avanza)[0].equals("(")) {
                        avanza++;
                        do {
                            if (tipo()) {
                                avanza++;
                                if (arregloTokens.get(avanza)[0].contains("#")) {
                                    avanza++;
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
                                    error("Error: se esperaba un identificador");
                                }
                            }
                        } while (arregloTokens.get(avanza)[0].equals(","));
                    } else {
                        error("Error: se esperaba una (");
                    }
                } else {
                    error("Error: se esperaba un identificador");
                }
            }
        } while (arregloTokens.get(avanza)[0].equals("procedure"));
    }

    private void estatuto() {
        do {
            if (arregloTokens.get(avanza)[0].contains("#")) {
                avanza++;
                //asigna();
            } else if (arregloTokens.get(avanza)[0].equals("input")) {
                avanza++;
                //leer();
            } else if (arregloTokens.get(avanza)[0].equals("output")) {
                avanza++;
                //escribir();
            } else if (arregloTokens.get(avanza)[0].equals("if")) {
                avanza++;
                //si();
            } else if (arregloTokens.get(avanza)[0].equals("repeat")) {
                avanza++;
                //repetir();
            } else if (arregloTokens.get(avanza)[0].equals("while")) {
                avanza++;
                //mientras();
            } else if (arregloTokens.get(avanza)[0].equals("call")) {
                avanza++;
                //ejecutar();
            } else {
                error("Error: Se esperaba un estatuto");
            }
        } while (arregloTokens.get(avanza)[0].equals("call") || arregloTokens.get(avanza)[0].equals("while")
                || arregloTokens.get(avanza)[0].equals("repeat") || arregloTokens.get(avanza)[0].equals("if")
                || arregloTokens.get(avanza)[0].equals("output") || arregloTokens.get(avanza)[0].equals("input")
                || arregloTokens.get(avanza)[1].contains("100"));
    }

    private boolean tipo() {
        for (String tipo : tipos) {
            if (arregloTokens.get(avanza)[0].equals(tipo)) {
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

    private boolean constante() {
        return arregloTokens.get(avanza)[1].equals("800") || arregloTokens.get(avanza)[1].equals("1000")
                || arregloTokens.get(avanza)[1].equals("900");
    }

    private void error(String error) {
        System.out.println(error + "\nLinea:" + arregloTokens.get(avanza)[3]);
        System.exit(0);
    }

}
