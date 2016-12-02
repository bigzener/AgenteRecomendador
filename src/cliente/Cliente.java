/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BIGZENER
 */
public class Cliente {
    private int port=2550;
    private Socket client;
    private String ip="localhost";
    private ObjectOutputStream write;
    private ObjectInputStream reader;
    
    public Cliente(String ip,int puerto) {
        this.ip=ip;
        this.port=puerto;
        init();
    }
    private void init(){
        try {
            client=new Socket();
            client.connect(new InetSocketAddress(InetAddress.getByName(ip).getHostAddress(),port));
            write=new ObjectOutputStream(client.getOutputStream());     
            reader=new ObjectInputStream(client.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Cliente() {
        init();
    }    
    public Object get(){
        try {
            Object entrada;
            entrada=reader.readObject();
            return entrada;
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public void send(Object o){
        try {
            write.writeObject(o);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
