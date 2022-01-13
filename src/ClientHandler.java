import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); //Create multiple clients

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;


    public ClientHandler(Socket socket) throws IOException {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientUserName + " has enter the chat!");
        }catch (IOException e){
            closeEverything(socket, bufferedWriter ,bufferedReader );
        }

    }

    @Override
    public void run() {
        String messageFromClient;

        while(socket.isConnected()){
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);

            }catch(IOException e){
                closeEverything(socket, bufferedWriter, bufferedReader);
                break;

            }


        }
    }

    //Send message from client to every other client in group-chat
    public void broadcastMessage(String messageToSend){
        for(ClientHandler clientHandler : clientHandlers){
            try {

                //Sending Client doesn't have to receive its own message
                if(!clientHandler.clientUserName.equals(clientUserName)){
                    clientHandler.bufferedWriter.write(messageToSend); //Sending message is placed in BufferArray to ease sending
                    clientHandler.bufferedWriter.newLine(); //end of message
                    clientHandler.bufferedWriter.flush(); //clear the buffer for reuse
                }
            }catch(IOException e){
                closeEverything(socket, bufferedWriter, bufferedReader); // In case error shutdown the broadcast
            }
        }


    }

    public void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        removeClientHandler();
        try {
            if(socket !=null){
                socket.close();
            }
            if(bufferedWriter !=null){
                bufferedWriter.close();
            }
            if(bufferedReader !=null){
                bufferedReader.close();
            }

        }catch (IOException e){
            e.printStackTrace(); //print error location(Class name, line number)

        }
    }

    //Removing client from chat
    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUserName + "has left the chat!");
    }


}
