package network;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import game.Game;
import game.entities.PlayerMP;
import game.network.packets.Packet;
import game.network.packets.Packet.PacketTypes;
import game.network.packets.Packet00Login;
import game.network.packets.Packet01Disconnect;
import game.network.packets.Packet02Move;

public class GameServer extends Thread {
	private DatagramSocket socket;
	private Game game;
	private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();
	
	public GameServer(Game game) {
		this.game = game;
		try {
			this.socket = new DatagramSocket(1331);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
//			String message = new String(packet.getData());
//			if(message.trim().equalsIgnoreCase("ping")) {
//				System.out.println("Client ["+packet.getAddress().getHostAddress()+":"+packet.getPort()+"]-> " + message);
//				sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
//				
//
//			}
		}
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(message.substring(0,2));

		Packet packet = null;
		switch (type) {
		default:
		case INVALID:
			break;
		case LOGIN:
			packet = new Packet00Login(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet00Login) packet).getUsername() + " has connected...");
			PlayerMP player = new PlayerMP (game.currentLevel, 100, 100, ((Packet00Login) packet).getUsername(), address, port);
			this.addConnection(player, (Packet00Login) packet);
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet01Disconnect) packet).getUsername() + " has left...");
			this.removeConnection((Packet01Disconnect) packet);
			break;
		case MOVE:
			packet = new Packet02Move(data);
			System.out.println(((Packet02Move)packet).getUsername() + " has moved to " + ((Packet02Move)packet).getX() + ", " + ((Packet02Move)packet).getY());
			this.handleMove((Packet02Move)packet);
		}
	}

	

	

	public void addConnection(PlayerMP player, Packet00Login packet) {

		boolean alreadyConnected = false;
		for (PlayerMP p : this.connectedPlayers) {
			if(player.getUsername().equalsIgnoreCase(p.getUsername())) {
				if(p.ipAddress == null) {
					p.ipAddress = player.ipAddress;
				}
				if(p.port == -1) {
					p.port = player.port;
				}
				alreadyConnected = true;
			} else {
				sendData(packet.getData(),p.ipAddress, p.port);
				
				packet = new Packet00Login(p.getUsername());
                sendData(packet.getData(), player.ipAddress, player.port);
			}
		}
		if (!alreadyConnected) {

            this.connectedPlayers.add(player);
        }


	}
	
	public void removeConnection(Packet01Disconnect packet) {
		PlayerMP player = getPlayerMP(packet.getUsername());
		this.connectedPlayers.remove(getPlayerMPIndex(packet.getUsername()));
		packet.writeData(this);
	}
	
	public PlayerMP getPlayerMP(String username) {
		for(PlayerMP player : this.connectedPlayers) {
			if(player.getUsername().equals(username)) {
				return player;
			}
		}
		return null;
	}
	
	public int getPlayerMPIndex(String username) {
		int index = 0;
		for(PlayerMP player : this.connectedPlayers) {
			if(player.getUsername().equals(username)) {
				break;
			}
			index++;
		}
		return index;
	}
	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			this.socket.send(packet);
			//System.out.println("server packet sent to " + ipAddress +":"+port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDataToAllClients(byte[] data) {
		for (PlayerMP p : connectedPlayers) {
			sendData(data, p.ipAddress, p.port);
		}
	}
	
	private void handleMove(Packet02Move packet) {
		if(getPlayerMP(packet.getUsername()) != null) {
			int index = getPlayerMPIndex(packet.getUsername());
			this.connectedPlayers.get(index).x = packet.getX();
			this.connectedPlayers.get(index).y = packet.getY();
			packet.writeData(this);
		}
	}
}
