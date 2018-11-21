/**
Author: Aaron Magnaye,	Antonette Porca, Joshua Panganiban
Subject: CMSC 137 Protobuf Milestone
Description: A class that acts as a wrapper for the Proto-generated class DisconnectPacket
**/

import proto.*;

public class DCPacket{

	private TcpPacketProtos.TcpPacket.DisconnectPacket dcp;

	public DCPacket(Player player){
		this.dcp = TcpPacketProtos.TcpPacket.DisconnectPacket.newBuilder()
											.setType(TcpPacketProtos.TcpPacket.PacketType.forNumber(2))
											.setPlayer(player.getPlayer())
											.build();
	}

	public TcpPacketProtos.TcpPacket.DisconnectPacket getPacket(){
		return this.dcp;
	}

	public void self(){
		System.out.println(this.dcp);
	}

	public byte[] serialize(){
		return this.dcp.toByteArray();
	}

	public TcpPacketProtos.TcpPacket.DisconnectPacket deserialize(byte[] b){

		// returns null if there was an error

		TcpPacketProtos.TcpPacket.DisconnectPacket n = null;		
		try{

			n = TcpPacketProtos.TcpPacket.DisconnectPacket.parseFrom(b);


		}catch(Exception e){
			System.out.println(e);
		}
		return n;
	}

}