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
    
    
    public void send_message(String from_user, String message, String recipient_user, PrintWriter client_out) {
        if(recipient_user.substring(1).compareTo("All") != 0) {
            int index_user_recipient = this.name_list_clients.indexOf(recipient_user.substring(1));
            this.printWriter_list_clients.get(index_user_recipient).println(from_user + ":" + message);
        }
        else { 
            for(PrintWriter pr_writter_At : this.printWriter_list_clients) {
                if(pr_writter_At != client_out)
                    pr_writter_At.println(from_user + ":" + message);
            }
        }
    }
    
    
    public void broadcast_names() {                
        for(PrintWriter pr_wr_client : this.printWriter_list_clients) {         
           for(String name_client : this.name_list_clients) {
               pr_wr_client.println("$"+ name_client);               
           }
        }
    } 
    
    
    public void broadcast_kick_outname(String kick_out_name, PrintWriter pr_writter_client_kicked) {
        for(PrintWriter pr_wr : this.printWriter_list_clients) {
            if(pr_wr != pr_writter_client_kicked)
                pr_wr.println("%" + kick_out_name);
        }
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
                    
                String message; 
                
                this.client_name = this.client_input.readLine();      //Getting the user Name that comes from Client
                this.password = this.client_input.readLine();        //Getting the user password that comes from client.                
                this.client_out.println("added");                   //Sending a confirmation message
                printWriter_list_clients.add(client_out);          //Adding the output_stream of this client SOCKET.
                name_list_clients.add(client_name);               //Adding the client name recenlty added.                
                broadcast_names();                               //Sending the list of clients to this client
                System.out.println(client_name + " joined");    //Printing in terminal 
                
                /*--------------------------------------------------------------------------------------------------------------*/
                //Starting with the main loop for read and send messages*/         
                /*--------------------------------------------------------------------------------------------------------------*/
                message = this.client_input.readLine();
                
                String[] message_splitted = message.split(":");
                
                while(true) {                    
                    if (message.indexOf("~<N!_/_!D>~") != -1 && message.indexOf(" left") != -1 && message.indexOf(":") == -1) //If the string contains this then we can say that the client is offline.
                            break;     
                    send_message(message_splitted[0], message_splitted[1], message_splitted[2], this.client_out);   
                    message = this.client_input.readLine();
                    if(message.contains(":")) {
                        message_splitted = message.split(":");                    
                        System.out.println("Sending message from" + message_splitted[0] +  "To :" + message_splitted[2]);
                    }
                }                              
                /*--------------------------------------------------------------------------------------------------------------*/
                //End loop recieve and send messages
                /*--------------------------------------------------------------------------------------------------------------*/
                System.out.println(this.client_name + "left.. :("); 
                
                int get_kicked_index = name_list_clients.indexOf(this.client_name);
                
                name_list_clients.remove(get_kicked_index);
                printWriter_list_clients.remove(get_kicked_index);
                broadcast_names();
            }
            catch(Exception e) {
                System.err.println("ERRORR!!!!" + e.getMessage());
              
            }
        }
    }    
}
