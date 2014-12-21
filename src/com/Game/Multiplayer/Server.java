package com.Game.Multiplayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private ServerSocket server;
    private ArrayList<Handler> clients;
    private Thread greeter;
    private boolean interrupted;
    private boolean started;
    private int source_id_next;

    public void start(int port) {
        if (started) {
            System.out.println("Server is already running");
            return;
        }
        System.out.println("Starting Server ...");
        try {
            server = new ServerSocket(port);
        } catch (Exception e) {
            System.out.println("Error binding port " + Integer.toString(port));
            System.out.println("Aborting");
            return;
        }
        clients = new ArrayList<Handler>();
        greeter = new Thread(new Greeter());
        greeter.start();
    }

    public void close() {
        interrupted = true;
    }

    class Greeter implements Runnable {

        public void run() {
            System.out.println("Greeter thread is running ...");
            while(!interrupted) {
                try {
                    Socket socket = server.accept();
                    System.out.println("Incoming request ...");
                    Client client = new Client(new DataInputStream(socket.getInputStream()), new DataOutputStream(socket.getOutputStream()), source_id_next++);
                    Handler handler = new Handler(client);
                    clients.add(handler);
                } catch (Exception e) {
                    System.err.println("Exception @ Greeter");
                }
            }
        }
    }

    class Handler implements Runnable {

        Client client;
        Thread thread;
        ArrayList<Packet> packets_to_send;

        Handler(Client client) {
            this.client = client;
            this.packets_to_send = new ArrayList<Packet>();
            this.thread = new Thread(this);
            this.thread.start();
        }

        public void run() {
            while(!interrupted) {
                try {
                    if (client.dis.available() == 0)
                        Thread.sleep(1);
                    else {
                        Packet packet = receivePacket();
                        broadcastPacket(packet);
                    }
                    synchronized (this) {
                        for (Packet packet : packets_to_send) {
                            sendPacket(packet);
                        }
                        packets_to_send.clear();
                    }
                } catch (Exception e) {

                }
            }
        }

        Packet receivePacket() {
            try {
                int size = client.dis.readInt();
                byte data[] = new byte[size];
                client.dis.readFully(data);
                Packet packet = new Packet(size, data, source_id_next);
                return packet;
            } catch (Exception e) {
                System.out.println("Error receiving packet ...");
            }
            return null;
        }

        void sendPacket(Packet packet) {
            try {
                client.dos.writeInt(packet.size);
                client.dos.write(packet.data);
            } catch (Exception e) {
                System.out.println("Error sending packet");
            }

        }

        void broadcastPacket(Packet packet) {
            for (Handler handler : clients) {
                if (this.client.source_id != handler.client.source_id) {
                    handler.addPacket(packet);
                }

            }
        }

        void addPacket(Packet packet) {
            synchronized (this) {
                packets_to_send.add(packet);
            }
        }
    }

    class Client {

        Client(DataInputStream dis, DataOutputStream dos, int source_id) {
            this.dis = dis;
            this.dos = dos;
            this.source_id = source_id;
        }

        DataInputStream dis;
        DataOutputStream dos;
        int source_id;

    }

    class Packet {

        Packet(int size, byte data[], int source_id) {
            this.size = size;
            this.data = data;
            this.source_id = source_id;
        }

        int source_id;
        int size;
        byte data[];
    }
}
