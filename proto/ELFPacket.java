/**
Author: Aaron Magnaye,	Antonette Porca, Joshua Panganiban
Subject: CMSC 137 Protobuf Milestone
Description: A class that acts as a wrapper for the Proto-generated class ErrLfullPacket
**/

import proto.*;

public class ELFPacket{

	private TcpPacketProtos.TcpPacket.ErrLfullPacket elfp;

	public ELFPacket(){
		this.elfp = TcpPacketProtos.TcpPacket.ErrLfullPacket.newBuilder()
											.setType(TcpPacketProtos.TcpPacket.PacketType.forNumber(2))
											.build();
	}

	public TcpPacketProtos.TcpPacket.ErrLfullPacket getPacket(){
		return this.elfp;
	}

	public void self(){
		System.out.println(this.elfp);
	}

	public byte[] serialize(){
		return this.elfp.toByteArray();
	}

	public TcpPacketProtos.TcpPacket.ErrLfullPacket deserialize(byte[] b){

		// returns null if there was an error

		TcpPacketProtos.TcpPacket.ErrLfullPacket n = null;		
		try{

			n = TcpPacketProtos.TcpPacket.ErrLfullPacket.parseFrom(b);


		}catch(Exception e){
			System.out.println(e);
		}
		return n;
	}

}