package agentes.apriori;

import java.util.ArrayList;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Profesor Bastar
 */
public class ReglaAsociacion {

    private ArrayList<Integer> izquierda;
    private ArrayList<Integer> derecha;
    private double confianza;
    private String iz;
    private String de;

    public ReglaAsociacion() {
        izquierda = new ArrayList<>();
        derecha = new ArrayList<>();
        confianza = 0;
    }

    public void setConfianza(double confianza) {
        this.confianza = confianza;
    }

    public void setIzquierda(Set<Integer> elementos) {
        for (Integer elemento : elementos) {
            putI(elemento);
        }
    }

    public void setDerecha(Set<Integer> elementos) {
        for (Integer elemento : elementos) {
            putD(elemento);
        }
    }

    public double getConfianza() {
        return confianza;
    }

    public void putI(int valor) {
        izquierda.add(valor);
        calculaIz();
    }

    public void putD(int valor) {
        derecha.add(valor);
        calculaDe();
    }

    public ArrayList<Integer> getIzquierda() {
        return izquierda;
    }

    public ArrayList<Integer> getDerecha() {
        return derecha;
    }

    private void calculaIz() {
        String salida = ",";
        for (Integer integer : izquierda) {
            salida = salida + integer + ",";
        }
        salida = salida.substring(0, salida.length() - 1) + "";
        iz = salida;
    }

    private void calculaDe() {
        String salida = "";
        for (Integer integer : derecha) {
            salida = salida + integer + ",";
        }
        salida = salida.substring(0, salida.length() - 1) + "";
        de = salida;
    }

    @Override
    public String toString() {
        String salida = "";
        String s[]=de.split(",");
        for(String d:s)salida+="transaccion("+d+iz + ").\n";
        return salida;
    }

    public String getIz() {
        return iz;
    }

    public String getDe() {
        return de;
    }

}
