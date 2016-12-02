/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;


import static agentes.apriori.Main.main;
import jade.core.behaviours.Behaviour;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BIGZENER
 */
public class Recomendar extends Behaviour{
    private ServerSocket server;
    private int port=2550;//recomendador
    ObjectInputStream reader;
    
    public Recomendar() {
        try {
            System.out.println("abriendo socket");
            server=new ServerSocket(port);
            Socket client=server.accept();
            reader=new ObjectInputStream(client.getInputStream());
            System.out.println("socket abierto");
        } catch (IOException ex) {
            try {
                Socket client=server.accept();
                reader=new ObjectInputStream(client.getInputStream());
//            Logger.getLogger(Comportamiento.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex1) {
                Logger.getLogger(Recomendar.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
        }
    }
    
    @Override
    public void action() {
        try {
            Object entrada;
            entrada=reader.readObject();
            System.out.println(entrada);
            String s[]=entrada.toString().split(",");
            double min=Double.parseDouble(s[0]),con=Double.parseDouble(s[1]);
            main(min ,con);
        } catch (IOException ex) {
            Logger.getLogger(Comportamiento.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Comportamiento.class.getName()).log(Level.SEVERE, null, ex);
        }
        //soporte
        //confianza
    }

    @Override
    public boolean done() {
        return false;
    }
    
}
