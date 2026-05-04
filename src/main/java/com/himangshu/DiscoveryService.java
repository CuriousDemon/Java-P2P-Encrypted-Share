package com.himangshu;

import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class DiscoveryService {
    private static final int DISCOVERY_PORT = 8888;
    private static final String DISCOVERY_MESSAGE = "SECURE_DROP_PEER";
    private static final Set<String> discoveredPeers = new HashSet<>();

    // 1. Start shouting "I am here!"
    public static void startBroadcasting() {
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket()) {
                socket.setBroadcast(true);
                byte[] buffer = DISCOVERY_MESSAGE.getBytes();

                while (true) {
                    DatagramPacket packet = new DatagramPacket(
                            buffer, buffer.length,
                            InetAddress.getByName("255.255.255.255"), DISCOVERY_PORT
                    );
                    socket.send(packet);
                    Thread.sleep(5000); // Shout every 5 seconds
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // 2. Listen for others shouting
    public static void startListening() {
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(DISCOVERY_PORT)) {
                byte[] buffer = new byte[1024];
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String message = new String(packet.getData(), 0, packet.getLength());
                    if (message.equals(DISCOVERY_MESSAGE)) {
                        String peerIp = packet.getAddress().getHostAddress();
                        if (discoveredPeers.add(peerIp)) {
                            System.out.println("[DISCOVERY] Found new peer: " + peerIp);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static Set<String> getDiscoveredPeers() {
        return discoveredPeers;
    }
}
