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
			System.out.println("dito error no");
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

	public void showMessage(){
		Player p = new Player(this.packet.getPlayer());
		System.out.println("\n"+p.getName()+": "+this.packet.getMessage());
	}

	public byte[] serialize(){
		return this.packet.toByteArray();
	}


}