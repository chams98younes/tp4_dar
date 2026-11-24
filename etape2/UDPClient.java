package etape2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UDPClient {

    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "localhost";
        InetAddress serverAddress = InetAddress.getByName(host);

        Scanner scanner = new Scanner(System.in, "UTF-8");
        System.out.print("Entrez votre nom d'utilisateur : ");
        String username = scanner.nextLine().trim();

        DatagramSocket socket = new DatagramSocket();
        System.out.println("Connecte au serveur " + host + ":" + UDPServer.PORT
                + " (taper 'quit' pour sortir)");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("quit")) {
                break;
            }
            String message = "[" + username + "] : " + line;
            byte[] data = message.getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(data, data.length, serverAddress, UDPServer.PORT));
        }

        socket.close();
        System.out.println("Client ferme.");
    }
}
