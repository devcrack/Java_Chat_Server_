<<<<<<< HEAD
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
        if(recipient_user.compareTo("$All") != 0) {
            int index_user_recipient = this.name_list_clients.indexOf(recipient_user);
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
        
    
    /**
     * @param kick_out_name name of the user that has be kicked out from the server
     * @param pr_writter_client_kicked printwriter of the user that has be kicked out from the server.
     */
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
                broadcast_kick_outname(client_name, client_out);
                
                int get_kicked_index = name_list_clients.indexOf(this.client_name);
                
                name_list_clients.remove(get_kicked_index);
                printWriter_list_clients.remove(get_kicked_index);                
            }
            catch(Exception e) {
                System.err.println("ERRORR!!!!" + e.getMessage());
              
            }
        }
    }    
}
=======
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
    
    
    /*Sending all to the client recently added...............................................................*/
//    public void broadcast_users_names(String message, PrintWriter  out) {
    public void broadcast_users_names(PrintWriter pr_client, String this_client_name) {
        boolean append_name = true;
        
        for(PrintWriter pw_client:  this.printWriter_list_clients) {
            if(pr_client == pw_client)
                append_name = false;
            for(String str_client_name : this.name_list_clients) {                
                if(!append_name && !this_client_name.equals(str_client_name)) {
                    pw_client.println("$" + str_client_name);
                    System.out.println(str_client_name + " is in list");//Printing in terminal 
                }
                
//                    pw_client.println("$" + str_client_name);
//                    System.out.println(str_client_name + " is in list");//Printing in terminal 
//                }
//                else 
//                    if(!this_client_name.equals(str_client_name)) {
//                        pw_client.println("$" + str_client_name);
//                        System.out.println(str_client_name + " is in list");//Printing in terminal 
//                    }
            }
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
                this.client_name = this.client_input.readLine();//Getting the user Name that comes from Client
                this.password = this.client_input.readLine(); //Getting the user password that comes from client.
                /*Sending a confirmation message*/
                this.client_out.println("added");
                printWriter_list_clients.add(client_out);//Adding the output_stream of this client SOCKET.
                name_list_clients.add(client_name); //Adding the client name recenlty added.
                System.out.println(client_name + " joined");//Printing in terminal 
                /*Sending the list of clients to this client*/
                broadcast_users_names(this.client_out, client_name);
                
            }
            catch(Exception e) {
                System.err.println("ERRORR!!!!" + e.getMessage());
            }
        }
    }    
}
>>>>>>> 9aca76d2412168e4ec0637d132ca6c9b36be3d0b
