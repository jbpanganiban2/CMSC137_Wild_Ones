/**
Author: Aaron Magnaye,	Antonette Porca, Joshua Panganiban
Subject: CMSC 137 Protobuf Milestone
Description: A class that acts as a wrapper for the Proto-generated class ChatPacket
**/

import proto.TcpPacketProtos.*;

public class CHPacket{

	private TcpPacket.ChatPacket packet;

	public CHPacket(Player player,String message){
		this.packet = TcpPacket.ChatPacket.newBuilder()
						.setType(TcpPacket.PacketType.forNumber(3))
						.setPlayer(player.getPlayer())
						.setMessage(message)
						.build();
	}

	public CHPacket(byte[] b){
		TcpPacket.ChatPacket n = null;		
		try{

			n = TcpPacket.ChatPacket.parseFrom(b);

		}catch(Exception e){
			System.out.println(e);
		}
		this.packet = n;
	}

	public TcpPacket.ChatPacket getPacket(){
		return this.packet;
	}

	public void self(){
		System.out.println(this.packet);
	}

	public static boolean ashost(String mess){
		return (mess.contains("!ALERT!"))?true:false;
	}

	public void showMessage(Player user){
		Player p = new Player(this.packet.getPlayer());
		if(p.getName().equals(user.getName()))							// does not show if the user connected to the lobby
			return;					
		else if(ashost(this.packet.getMessage()))
			System.out.println(this.packet.getMessage());				// does not show the name of the sender if the sender is a host
		else
			System.out.println(p.getName()+": "+this.packet.getMessage());	// normal show of chat
	}

	public String getMessage(){
		return this.packet.getMessage();
	}

	public void addMessageToBox(String message, Chat chat){
		Player p = new Player(this.packet.getPlayer());
		String username = p.getName();
		chat.addMessageToBox(username, message);
	}

	public byte[] serialize(){
		return this.packet.toByteArray();
	}


}