/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author elder
 */
public class Server {
    /* 1 - Criar o servidor de conexões
     2 -Esperar o um pedido de conexão;
     Outro processo
     2.1 e criar uma nova conexão;
     3 - Criar streams de enechar socket de comunicação entre servidor/cliente
     4.2 - Fechar streams de entrada e saída
           trada e saída;
     4 - Tratar a conversação entre cliente e 
     servidor (tratar protocolo);
     4.1 - Fechar socket de comunicação entre servidor/cliente
     4.2 - Fechar streams de entrada e saída
           
     5 - voltar para o passo 2, até que finalize o programa;
     6 - fechar serverSocket
     */

    private ServerSocket serverSocket;
    /*- Criar o servidor de conexões*/

    private void criarServerSocket(int porta) throws IOException {
        serverSocket = new ServerSocket(porta);

    }
    /*2 -Esperar o um pedido de conexão;
     Outro processo*/

    private Socket esperaConexao() throws IOException {
        Socket socket = serverSocket.accept();
        return socket;
    }
    
    private void fechaSocket(Socket s) throws IOException
    {
        s.close();
    }
    
    private void enviaMsg( Object o, ObjectOutputStream out ) throws IOException
    {
        out.writeObject(o);
        out.flush();
    }

    private void trataConexao(Socket socket) throws IOException {
        // * Cliente ------SOCKET-----servidor
        //protocolo da aplicação
        /*
        4 - Tratar a conversação entre cliente e 
         servidor (tratar protocolo);
         */
           
        
        try {
            /* 3 - Criar streams de entrada e saída;*/
         
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            
            /*protocolo
                Cliente --> HELLO
                Server <---- HELLO WORLD!
            */
            /*4 - Tratar a conversação entre cliente e 
         servidor (tratar protocolo);*/
            System.out.println("Tratando...");
            String msg = input.readUTF();
            System.out.println("Mensagem recebida: " + msg);
            output.writeUTF("HELLO WORLD!");
            output.flush();//cambio do rádio amador
            
            //4.2 - Fechar streams de entrada e saída
            input.close();
            output.close();
        } catch (IOException e) {
            //tratamento de falhas
            System.out.println("Problema no tratamento da conexão com o cliente: "+socket.getInetAddress());
            System.out.println("Erro: " + e.getMessage());
            throw e;
        }finally{
            //final do tratamento do protocolo
            /*4.1 - Fechar socket de comunicação entre servidor/cliente*/
            fechaSocket(socket);
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Server server = new Server();
            System.out.println("Aguardando conexão...");
            server.criarServerSocket(5555);
            Socket socket = server.esperaConexao();//protocolo
            System.out.println("Cliente conectado.");
            server.trataConexao(socket);
            System.out.println("Cliente finalizado.");
            
        } catch (IOException e) {
            //trata exceção
            System.out.println("Erro no servidor: "+ e.getMessage());
        }
    }

}
