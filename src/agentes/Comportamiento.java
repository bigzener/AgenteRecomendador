/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import jade.core.behaviours.Behaviour;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpl7.Query;

/**
 *
 * @author BIGZENER
 */
public class Comportamiento extends Behaviour {

    private ServerSocket server;
    private int port=2550;
    
    
    private static void loadDLL(String location) {
        try {
            File dll = new File(location);
            System.load(dll.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void inid(){
        String path="";
        loadDLL(path+"libwinpthread-1.dll");
        loadDLL(path+"libgcc_s_sjlj-1.dll");
        loadDLL(path+"libgmp-10.dll");
        loadDLL(path+"libswipl.dll");    
        loadDLL(path+"json.dll");
        loadDLL(path+"jpl.dll");
        Query.hasSolution("use_module(library(jpl))"); // only because we call e.g. jpl_pl_syntax/1 below
	String t1 = "consult('family.pl')";
	System.out.println(t1 + " " + (Query.hasSolution(t1) ? "succeeded" : "failed"));
	
        
        
        try {
            server=new ServerSocket(port);
            
        } catch (IOException ex) {
            Logger.getLogger(Comportamiento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Comportamiento() {
        try {
            inid();
            Socket client=server.accept();
            reader=new ObjectInputStream(client.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Comportamiento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    ObjectInputStream reader;
    @Override
    public void action() {
        try {
            Object entrada;
            entrada=reader.readObject();
            System.out.println(entrada);
        } catch (IOException ex) {
            Logger.getLogger(Comportamiento.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Comportamiento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean done() {
        //ystem.out.println("done");
        return false;
    }
    
}
