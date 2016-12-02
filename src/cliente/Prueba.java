/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

/**
 *
 * @author BIGZENER
 */
public class Prueba {
    public static void main(String arg[]) throws InterruptedException{
        Cliente c =new Cliente("localhost",2551);
        while(true){
            System.out.println("pregunta");
            c.send("180722");
            Thread.sleep(10000);
        }
    }
}
