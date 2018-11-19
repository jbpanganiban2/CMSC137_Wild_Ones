/**
Author: Aaron Magnaye,	Antonette Porca, Joshua Panganiban
Subject: CMSC 137 Protobuf Milestone
Description: A class that acts as a wrapper for the Proto-generated class ErrPacket
**/

import proto.*;

public class ERPacket{

	private TcpPacketProtos.TcpPacket.ErrPacket erp;

	public ERPacket(){
		this.erp = TcpPacketProtos.TcpPacket.ErrPacket.newBuilder()
											.setType(TcpPacketProtos.TcpPacket.PacketType.forNumber(2))
											.build();
	}

	public TcpPacketProtos.TcpPacket.ErrPacket getPacket(){
		return this.erp;
	}

	public void self(){
		System.out.println(this.erp);
	}

	public byte[] serialize(){
		return this.erp.toByteArray();
	}

	public TcpPacketProtos.TcpPacket.ErrPacket deserialize(byte[] b){

		// returns null if there was an error

		TcpPacketProtos.TcpPacket.ErrPacket n = null;		
		try{

			n = TcpPacketProtos.TcpPacket.ErrPacket.parseFrom(b);


		}catch(Exception e){
			System.out.println(e);
		}
		return n;
	}

}