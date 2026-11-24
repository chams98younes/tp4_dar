package etape3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UDPClient {

    private static final int BUFFER_SIZE = 4096;

    private static void send(DatagramSocket socket, InetAddress server, String message) throws IOException {
        byte[] data = message.getBytes(StandardCharsets.UTF_8);
        socket.send(new DatagramPacket(data, data.length, server, UDPServer.PORT));
    }

    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "localhost";
        final InetAddress serverAddress = InetAddress.getByName(host);

        Scanner scanner = new Scanner(System.in, "UTF-8");
        System.out.print("Entrez votre nom d'utilisateur : ");
        final String username = scanner.nextLine().trim();

        final DatagramSocket socket = new DatagramSocket();

        // Thread de reception : affiche les messages diffuses par le serveur
        Thread receiver = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[BUFFER_SIZE];
                try {
                    while (true) {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        System.out.println(new String(packet.getData(), 0, packet.getLength(),
                                StandardCharsets.UTF_8));
                    }
                } catch (SocketException e) {
                    // Socket ferme : fin normale du client
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        receiver.setDaemon(true);
        receiver.start();

        // Premier message : permet au serveur d'enregistrer ce client
        send(socket, serverAddress, "[" + username + "] : a rejoint le chat");
        System.out.println("Connecte au serveur " + host + ":" + UDPServer.PORT
                + " (taper 'quit' pour sortir)");

        // Thread principal : envoi des messages saisis au clavier
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("quit")) {
                send(socket, serverAddress, "[" + username + "]" + UDPServer.QUIT_MARK);
                break;
            }
            send(socket, serverAddress, "[" + username + "] : " + line);
        }

        socket.close();
        System.out.println("Client ferme.");
    }
}
