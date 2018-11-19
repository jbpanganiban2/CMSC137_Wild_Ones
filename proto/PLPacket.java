/**
Author: Aaron Magnaye,	Antonette Porca, Joshua Panganiban
Subject: CMSC 137 Protobuf Milestone
Description: A class that acts as a wrapper for the Proto-generated class PlayerListPacket
**/

import proto.*;

public class PLPacket{

	private TcpPacketProtos.TcpPacket.PlayerListPacket plp;

	public PLPacket(Player playerList){
		this.plp = TcpPacketProtos.TcpPacket.PlayerListPacket.newBuilder()
											.setType(TcpPacketProtos.TcpPacket.PacketType.forNumber(2))
											.setPlayer(playerList)
											.build();
	}

	public TcpPacketProtos.TcpPacket.PlayerListPacket getPacket(){
		return this.plp;
	}

	public void self(){
		System.out.println(this.plp);
	}

	public byte[] serialize(){
		return this.plp.toByteArray();
	}

	public TcpPacketProtos.TcpPacket.PlayerListPacket deserialize(byte[] b){

		// returns null if there was an error

		TcpPacketProtos.TcpPacket.PlayerListPacket n = null;		
		try{

			n = TcpPacketProtos.TcpPacket.PlayerListPacket.parseFrom(b);


		}catch(Exception e){
			System.out.println(e);
		}
		return n;
	}

}