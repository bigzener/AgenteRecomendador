/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archivos;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Profesor Bastar
 */
public class EscrituraArchivos {

    private String nombreArchivo;
    private File archivo;
    private PrintWriter wr;
    public EscrituraArchivos(String ar) {
        try {
            nombreArchivo = ar;
            archivo = new File(nombreArchivo);
            abreArchivo();
        } catch (IOException ex) {
            System.out.println("Error = " + ex);
        }
    }

    public void abreArchivo() throws IOException {
        FileWriter w = new FileWriter(archivo);
        BufferedWriter bw = new BufferedWriter(w);
        wr = new PrintWriter(bw);
        
    }

    public void agregaLinea(String linea) {
        wr.append(linea);
        wr.println();
    }

    public void cierraArchivo(){
        wr.close();
    }
    public void abrir() {
        try {
            Desktop.getDesktop().open(new File(nombreArchivo));
        } catch (IOException ex) {
            System.out.println("ex = " + ex);
        }
    }

}
