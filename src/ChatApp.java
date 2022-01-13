
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class ChatApp {



    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name for the Group chat: ");
        String userName = scanner.nextLine();
        Socket socket = new Socket("localhost", 9099);
        Client client = new Client(socket, userName);
        client.listenForMessage();
        client.sendMessage();
    }
}


