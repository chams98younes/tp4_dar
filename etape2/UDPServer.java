package etape2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class UDPServer {

    public static final int PORT = 1234;
    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) throws IOException {
        // Liaison du socket a l'adresse locale (toutes les interfaces) sur le port 1234
        DatagramSocket socket = new DatagramSocket(new InetSocketAddress(PORT));
        System.out.println("Serveur UDP en ecoute sur le port " + PORT + " ...");

        byte[] buffer = new byte[BUFFER_SIZE];
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet); // appel bloquant : attend un datagramme

            String message = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
            System.out.println("Message recu de " + packet.getAddress().getHostAddress()
                    + ":" + packet.getPort() + " -> " + message);
        }
    }
}
