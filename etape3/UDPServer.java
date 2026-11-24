package etape3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UDPServer {

    public static final int PORT = 1234;
    /** Fin de message envoyee par un client qui quitte le chat. */
    public static final String QUIT_MARK = " : /quit";
    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(new InetSocketAddress(PORT));
        // Liste des adresses (IP + port) des clients connus
        List<SocketAddress> clients = new ArrayList<SocketAddress>();
        System.out.println("Serveur de chat UDP en ecoute sur le port " + PORT + " ...");

        byte[] buffer = new byte[BUFFER_SIZE];
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            SocketAddress sender = packet.getSocketAddress();
            String message = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);

            // 3a. Enregistrer l'expediteur s'il n'est pas deja dans la liste
            if (!clients.contains(sender)) {
                clients.add(sender);
                System.out.println("Nouveau client enregistre : " + sender
                        + " (" + clients.size() + " client(s))");
            }

            System.out.println("Message recu de " + sender + " -> " + message);

            // Un client qui quitte est retire de la liste, les autres sont prevenus
            if (message.endsWith(QUIT_MARK)) {
                clients.remove(sender);
                message = message.substring(0, message.length() - QUIT_MARK.length()) + " a quitte le chat";
                System.out.println("Client retire : " + sender + " (" + clients.size() + " client(s))");
            }

            // 3b. Diffuser le message a tous les autres clients
            byte[] data = message.getBytes(StandardCharsets.UTF_8);
            for (SocketAddress client : clients) {
                if (!client.equals(sender)) {
                    socket.send(new DatagramPacket(data, data.length, client));
                }
            }
        }
    }
}
