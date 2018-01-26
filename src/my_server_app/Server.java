/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my_server_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author delcracnk
 */
public class Server extends Thread {
    private static final int PORT = 7520;
    private  ServerSocket listener_server = null;
    private  ArrayList<PrintWriter> printWriter_list_clients;
    private  ArrayList<String> name_list_clients;
    BufferedReader input_socket;
    PrintWriter output_socket;
    String name;
    
    Server() throws IOException {
        this.listener_server = new ServerSocket(PORT);
        this.name_list_clients = new ArrayList<String>();
        this.printWriter_list_clients = new ArrayList<PrintWriter>();        
    }
    
    
    public void run() {
        System.out.println("Server is Listening on port : " + this.listener_server.getLocalPort());
        while (true) {
            try {
                 /*Here the program flow stop until a client is accepted*/
                 Socket client_listener = this.listener_server.accept();
                 
                 /*From here the program flow continues*/
                 System.out.println("The client is connected to :" + client_listener.getRemoteSocketAddress());
                 /*Getting the IO from Server socket*/
                 this.input_socket = new BufferedReader(new InputStreamReader(client_listener.getInputStream()));
                 this.output_socket = new PrintWriter(client_listener.getOutputStream(), true);
                 /*Starting a new Thread for a new Client*/
                 new Read(this.input_socket, this.output_socket).start();
                 
            }
            catch(Exception e){
                System.err.println("Error in server!!!! " + e.getMessage());
            }
        }                       
    }
    
    
    
    public void broadcast(String message, PrintWriter  out) {
        
    } 
    /*###############################<INNER CLASS>############################*/
    
    public class Read extends Thread {
        String client_name;
        String password;
        BufferedReader client_input;
        PrintWriter client_out;
    
        Read(BufferedReader incoming_inp, PrintWriter incoming_out) {
                this.client_input = incoming_inp;
                this.client_out = incoming_out;
            }

        public void run() {           
            try {
                this.client_name = this.client_input.readLine();//Getting the user Name that comes from Client
                this.password = this.client_input.readLine(); //Getting the user password that comes from client.
                /*Sending a confirmation message*/
                this.client_out.println("added");
                printWriter_list_clients.add(client_out);//Adding the output_stream of this client SOCKET.
                name_list_clients.add(client_name); //Adding the client name recenlty added.
                System.out.println(client_name + " joined");//Printing in terminal 
                /*Sending the list of clients to this client*/
            }
            catch(Exception e) {
                System.err.println("ERRORR!!!!" + e.getMessage());
            }
        }
    }    
}
