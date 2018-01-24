/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my_server_app;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 *
 * @author delcracnk
 */
public class Read extends Thread{
    String client_name;
    BufferedReader client_input;
    PrintWriter client_out;
    
    Read(BufferedReader incoming_inp, PrintWriter incoming_out) {
            this.client_input = incoming_inp;
            this.client_out = incoming_out;
        }
    
    public void run() {
        String str_tmp;
        String password;
        try {
            this.client_name = this.client_input.readLine();
            password = this.client_input.readLine();
        }
        catch(Exception e) {
            System.err.println("ERRORR!!!!" + e.getMessage());
        }
    }
}
