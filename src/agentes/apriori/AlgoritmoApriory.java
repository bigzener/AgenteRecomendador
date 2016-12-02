package agentes.apriori;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class AlgoritmoApriory {

    public static ArrayList<ReglaAsociacion> generaFinalesReglas(ArrayList<ReglaAsociacion> reglas) {
        ArrayList<ReglaAsociacion> salida = new ArrayList<>();
        for (int i = 0; i < reglas.size(); i++) {
            salida.add(reglas.get(i));
            for (int j = i + 1; j < reglas.size(); j++) {
                if (reglas.get(i).getIz().equalsIgnoreCase(reglas.get(j).getIz())
                        && reglas.get(i).getConfianza() == reglas.get(j).getConfianza()) {
                    for (int k = 0; k < reglas.get(j).getIzquierda().size(); k++) {
                        salida.get(i).putD(reglas.get(j).getDerecha().get(k));
                    }
                }
            }
        }
        return salida;
    }

    //Listo, no mover, es la estructura padre
    public static ArrayList<ReglaAsociacion> generaReglasDeAsociacion(int matriz[][], double minsup, double minconf) {

        ArrayList<String> itemsets = calculaItemsets(matriz, minsup);
        ArrayList<ReglaAsociacion> reglas = null;

        if (!itemsets.get(0).isEmpty()) {
            reglas = calculaAsociaciones(itemsets, minconf);
        }
        return generaFinalesReglas(reglas);
        //return reglas;
    }

    //Funcional
    private static ArrayList<String> calculaItemsets(int matriz[][], double minsup) {

        ArrayList<String> estructuraInicial = new ArrayList<>();

        int[] frecuencias = calculaFrecuenciasUno(matriz);

        double[] soporte = calculaSoporteUno(frecuencias, matriz.length);

        String estructura = conjuntoSoporteUno(soporte, frecuencias, minsup);

        estructuraInicial.add(estructura);
        //Busco los elementos para las combinaciones
        if (!estructura.isEmpty()) {
            int[] bases = obtenerBasesDadaPrimerEstructura(estructura);
            //Bases si tiene hasta el 3
            if (bases.length > 0) {
                int numeroCombinaciones = 2;
                ArrayList<List<String>> combinaciones = combina(bases, numeroCombinaciones);
                while (!combinaciones.isEmpty()) {
                    //Analiza las combinaciones
                    for (List combi : combinaciones) {
                        double x = esMayorAlSoporte(combi, matriz, minsup);

                        if (x > 0) {
                            estructuraInicial.add(creaEstructura(combi, x));
                        }
                    }
                    numeroCombinaciones++;
                    combinaciones = combina(bases, numeroCombinaciones);
                }
            }
        }
        return estructuraInicial;
    }

    //Funcional
    private static int[] calculaFrecuenciasUno(int matriz[][]) {
        int columnas = matriz[0].length;
        int frecuencias[] = new int[columnas];

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < columnas; j++) {
                frecuencias[j] = frecuencias[j] + matriz[i][j];
            }
        }
        return frecuencias;
    }

    //Funcional
    private static double[] calculaSoporteUno(int frecuencias[], double numTransacciones) {
        double soporte[] = new double[frecuencias.length];

        for (int i = 0; i < soporte.length; i++) {
            soporte[i] = frecuencias[i] / numTransacciones;
        }
        return soporte;
    }

    //Funcional
    private static String conjuntoSoporteUno(double soporte[], int frec[], double minSup) {
        String ides = "";
        String frecuencias = "";
        String salida = "";
        for (int i = 0; i < soporte.length; i++) {
            if (soporte[i] >= minSup) {
                ides = ides + i + ",";
                frecuencias = frecuencias + frec[i] + ",";
            }
        }
        if (!ides.isEmpty()) {

            ides = ides.substring(0, ides.length() - 1);
            frecuencias = frecuencias.substring(0, frecuencias.length() - 1);
            salida = ides + "*" + frecuencias;
        }

        return salida;
    }

    //Hay que hacerlo, es el que se recombina todo
    private static ArrayList<ReglaAsociacion> calculaAsociaciones(ArrayList<String> itemsets, double minconf) {
        ArrayList<ReglaAsociacion> asociaciones = new ArrayList<>();

        TreeMap<String, Double> candidatos = convierteItemsATreeMap(itemsets);

        //imprimeCandidatos(candidatos);
        for (Map.Entry<String, Double> entry : candidatos.entrySet()) {
            String regla = entry.getKey();
            double frecu = entry.getValue();
            if (regla.length() >= 3) {
                //Buscar todas las combinaciones tomando n que va desde 1 hasta tam-1

                String itemsDeRegla[] = regla.split("-");
                int tam = itemsDeRegla.length;
                ArrayList<List<String>> combinaciones = new ArrayList();
                for (int i = 1; i < tam; i++) {
                    combinaciones = combinaciones(itemsDeRegla, i);
                }
                for (List combinacione : combinaciones) {
                    int dum[] = new int[combinacione.size()];
                    for (int i = 0; i < dum.length; i++) {
                        dum[i] = Integer.parseInt(combinacione.get(i).toString());
                    }
                    String reglaProbar = convierteArrayToString(dum);
                    double valorRela = candidatos.get(reglaProbar);
                    double miConfianza = frecu / valorRela;
                    if (miConfianza >= minconf) {

                        Set<Integer> padre = convierteAConjunto(regla);
                        Set<Integer> izq = convierteAConjunto(reglaProbar);

                        Set<Integer> der = new HashSet<Integer>();
                        der.addAll(padre);
                        der.removeAll(izq);

                        //imprimeSemiReglas(izq, der,regla);
                        ReglaAsociacion rul = new ReglaAsociacion();
                        rul.setDerecha(der);
                        rul.setIzquierda(izq);
                        rul.setConfianza(miConfianza);
                        asociaciones.add(rul);
                    }
                }
            }
        }

        return asociaciones;
    }

    private static String convierteArrayToString(int array[]) {
        if (array.length == 1) {
            return "" + array[0];
        } else {
            Arrays.sort(array);
            String sal = "";
            for (int i = array.length - 1; i >= 1; i--) {
                sal = sal + array[i] + "-";
            }
            sal = sal + array[0];
            return sal;
        }
    }

    //Funcional
    private static void imprimeCandidatos(TreeMap<String, Double> tm) {

        for (Map.Entry<String, Double> entry : tm.entrySet()) {
            System.out.println("Item: " + entry.getKey() + " Frec: " + entry.getValue());
        }
    }

    //Funcional
    private static int[] obtenerBasesDadaPrimerEstructura(String estructura) {
        String iz = estructura.substring(0, estructura.indexOf("*"));
        String arr[] = iz.split(",");
        int salida[] = new int[arr.length];
        for (int i = 0; i < salida.length; i++) {
            salida[i] = Integer.parseInt(arr[i]);
        }
        return salida;
    }

    //Funcional
    private static ArrayList<List<String>> combina(int[] bases, int n) {
        ArrayList<List<String>> salida = new ArrayList();
        if (n <= bases.length) {
            salida = combinaciones(bases, n);
        }
        return salida;
    }

    //Funcional
    private static ArrayList<List<String>> combinaciones(int n[], int tam) {
        ArrayList na = new ArrayList();
        for (int i = 0; i < n.length; i++) {
            na.add("" + n[i]);
        }
        IteradorCombinacion it = new IteradorCombinacion(na, tam);
        Iterator s = it.iterator();
        ArrayList<List<String>> l2 = new ArrayList();

        while (s.hasNext()) {
            List<String> listares = (List<String>) s.next();
            l2.add(listares);
        }
        return l2;
    }

    private static ArrayList<List<String>> combinaciones(String n[], int tam) {
        ArrayList na = new ArrayList();
        for (int i = 0; i < n.length; i++) {
            na.add("" + n[i]);
        }
        IteradorCombinacion it = new IteradorCombinacion(na, tam);
        Iterator s = it.iterator();
        ArrayList<List<String>> l2 = new ArrayList();

        while (s.hasNext()) {
            List<String> listares = (List<String>) s.next();
            l2.add(listares);
        }
        return l2;
    }

    //Funcional
    private static double esMayorAlSoporte(List combi, int[][] matriz, double minsup) {
        Object aux[] = combi.toArray();

        int items[] = new int[aux.length];
        for (int i = 0; i < aux.length; i++) {
            items[i] = Integer.parseInt(aux[i].toString());
        }
        double c = 0;
        for (int i = 0; i < matriz.length; i++) {
            boolean band = true;
            for (int j = 0; j < items.length; j++) {
                if (matriz[i][items[j]] == 0) {
                    band = false;
                }
            }
            if (band == true) {
                c++;
            }
        }
        double sopo = c / matriz.length;
        if (sopo >= minsup) {
            return c;
        } else {
            return 0;
        }
    }

    //Funcional
    private static String creaEstructura(List combi, double x) {
        String sal = "";
        for (Object object : combi) {
            sal = sal + object.toString() + "-";
        }
        sal = sal.substring(0, sal.length() - 1);
        sal = sal + "*" + x;
        return sal;
    }

    //Funcional
    private static TreeMap<String, Double> convierteItemsATreeMap(ArrayList<String> itemsets) {
        TreeMap<String, Double> candidatos = new TreeMap<String, Double>();

        String iz = itemsets.get(0).substring(0, itemsets.get(0).indexOf("*"));
        String de = itemsets.get(0).substring(itemsets.get(0).indexOf("*") + 1);
        String item[] = iz.split(",");
        String frec[] = de.split(",");

        for (int i = 0; i < item.length; i++) {
            candidatos.put(item[i], Double.parseDouble(frec[i]));
        }

        for (int i = 1; i < itemsets.size(); i++) {
            iz = itemsets.get(i).substring(0, itemsets.get(i).indexOf("*"));
            de = itemsets.get(i).substring(itemsets.get(i).indexOf("*") + 1);

            candidatos.put(iz, Double.parseDouble(de));
        }
        return candidatos;
    }

    private static Set<Integer> convierteAConjunto(String regla) {
        Set<Integer> conjunto = new HashSet<>();
        String rul[] = regla.split("-");
        for (int i = 0; i < rul.length; i++) {
            conjunto.add(Integer.parseInt(rul[i]));
        }
        return conjunto;
    }

    private static void imprimeSemiReglas(Set<Integer> izq, Set<Integer> der, String regla) {

        //ReglaProbar va a izquierda
        //Padre - regla a Probar va a derecha
        System.out.println("Padre " + regla);
        System.out.print("Derecha");
        for (Integer integer : der) {
            System.out.print(integer + " ");
        }
        System.out.println("");
        System.out.print("Izquierda");
        for (Integer integer : izq) {
            System.out.print(integer + " ");
        }
        System.out.println("");
    }

    public static ArrayList<ReglaAsociacion> convierteReglasAID(ArrayList<ReglaAsociacion> reglas, int[] ides) {

        TreeMap<Integer, Integer> mapa = new TreeMap<Integer, Integer>();
        for (int i = 1; i <= ides.length; i++) {
            mapa.put(i, ides[i - 1]);
        }
        ArrayList<ReglaAsociacion> salida = new ArrayList<>();
        for (ReglaAsociacion regla : reglas) {
            ReglaAsociacion nuevaRegla = new ReglaAsociacion();
            for (int i = 0; i < regla.getIzquierda().size(); i++) {
                int vieja = regla.getIzquierda().get(i);
                int nueva = mapa.get(vieja);
                nuevaRegla.putI(nueva);
            }

            for (int i = 0; i < regla.getDerecha().size(); i++) {
                int vieja = regla.getDerecha().get(i);
                int nueva = mapa.get(vieja);
                nuevaRegla.putD(nueva);
            }
            nuevaRegla.setConfianza(regla.getConfianza());
            salida.add(nuevaRegla);
        }
        return salida;
    }

}
