/**
Author: Aaron Magnaye,	Antonette Porca, Joshua Panganiban
Subject: CMSC 137 Protobuf Milestone
Description: A class that acts as a wrapper for the Proto-generated class CreateLobbyPacket
**/

import proto.*;

public class CLPacket{

	private TcpPacketProtos.TcpPacket.CreateLobbyPacket clp;

	public CLPacket(int maxPlayers){
		this.clp = TcpPacketProtos.TcpPacket.CreateLobbyPacket.newBuilder()
											.setType(TcpPacketProtos.TcpPacket.PacketType.forNumber(2))
											.setMaxPlayers(maxPlayers)
											.build();
	}

	public CLPacket(byte[] b){
		TcpPacketProtos.TcpPacket.CreateLobbyPacket n = null;		
		try{

			n = TcpPacketProtos.TcpPacket.CreateLobbyPacket.parseFrom(b);


		}catch(Exception e){
			System.out.println(e);
		}
		this.clp = n;
	}

	public TcpPacketProtos.TcpPacket.CreateLobbyPacket getPacket(){
		return this.clp;
	}

	public void self(){
		System.out.println(this.clp);
	}

	public byte[] serialize(){
		return this.clp.toByteArray();
	}
}






	