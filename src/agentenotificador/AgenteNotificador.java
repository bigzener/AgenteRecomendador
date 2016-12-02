/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentenotificador;

import jade.core.Agent;

/**
 *
 * @author BIGZENER
 */
public class AgenteNotificador extends Agent {

    @Override
    protected void setup() {
        System.out.println("cargando notificador");
        addBehaviour(new Notificador());
    }
/**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
