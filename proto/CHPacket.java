/**
Author: Aaron Magnaye,	Antonette Porca, Joshua Panganiban
Subject: CMSC 137 Protobuf Milestone
Description: A class that acts as a wrapper for the Proto-generated class ChatPacket
**/

import proto.*;

public class CHPacket{

	private TcpPacketProtos.TcpPacket.ChatPacket chp;

	public CHPacket(Player player){
		this.chp = TcpPacketProtos.TcpPacket.ChatPacket.newBuilder()
											.setType(TcpPacketProtos.TcpPacket.PacketType.forNumber(2))
											.setPlayer(player)
											.build();
	}

	public TcpPacketProtos.TcpPacket.ChatPacket getPacket(){
		return this.chp;
	}

	public void self(){
		System.out.println(this.chp);
	}

	public byte[] serialize(){
		return this.chp.toByteArray();
	}

	public TcpPacketProtos.TcpPacket.ChatPacket deserialize(byte[] b){

		// returns null if there was an error

		TcpPacketProtos.TcpPacket.ChatPacket n = null;		
		try{

			n = TcpPacketProtos.TcpPacket.ChatPacket.parseFrom(b);


		}catch(Exception e){
			System.out.println(e);
		}
		return n;
	}

}