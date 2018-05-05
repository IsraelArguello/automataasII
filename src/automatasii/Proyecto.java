package automatasii;

import java.io.IOException;



public class Proyecto {
  public static void main(String[] a) {
        Lexico lexico = new Lexico();
        Sintaxis sintaxis = new Sintaxis();
       
        try {
            lexico.leerArchivo();
            lexico.impArchivo();
            sintaxis.leerArchivo();
            sintaxis.impArchivo();
            
        } catch (IOException e) {
            System.out.println("Archivo no encontrado");
        }
      
    }
}