/**
Author: Aaron Magnaye,	Antonette Porca, Joshua Panganiban
Subject: CMSC 137 Protobuf Milestone
Description: A class that acts as a wrapper for the Proto-generated class ConnectPacket
**/

import proto.*;

public class COPacket{

	private TcpPacketProtos.TcpPacket.ConnectPacket cop;

	public COPacket(Player player, String lobbyid){
		this.cop = TcpPacketProtos.TcpPacket.ConnectPacket.newBuilder()
											.setType(TcpPacketProtos.TcpPacket.PacketType.forNumber(2))
											.setPlayer(player.getPlayer())
											.setLobbyId(lobbyid)
											.build();
	}

	public TcpPacketProtos.TcpPacket.ConnectPacket getPacket(){
		return this.cop;
	}

	public void self(){
		System.out.println(this.cop);
	}

	public byte[] serialize(){
		return this.cop.toByteArray();
	}

	public TcpPacketProtos.TcpPacket.ConnectPacket deserialize(byte[] b){

		// returns null if there was an error

		TcpPacketProtos.TcpPacket.ConnectPacket n = null;		
		try{

			n = TcpPacketProtos.TcpPacket.ConnectPacket.parseFrom(b);


		}catch(Exception e){
			System.out.println(e);
		}
		return n;
	}

}