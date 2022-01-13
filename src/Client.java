import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;

    public Client(Socket socket, String userName) {

        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader =  new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.userName = userName;
        }catch (IOException e){
            closeEverything(socket, bufferedWriter, bufferedReader );
        }

    }

    public void sendMessage(){
        try {
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(userName + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

        }catch (IOException e){
            closeEverything(socket, bufferedWriter, bufferedReader );
        }
    }

    //client receive message from Group Chat
    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;

                while (socket.isConnected()){
                    try {
                        messageFromGroupChat = bufferedReader.readLine();
                        System.out.println(messageFromGroupChat);
                    }catch (IOException e){

                        closeEverything(socket, bufferedWriter, bufferedReader );
                    }
                }

            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader){
        try {
            if(socket != null){
                socket.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(bufferedReader != null){
                bufferedReader.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
