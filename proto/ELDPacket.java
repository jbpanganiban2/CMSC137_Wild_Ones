/**
Author: Aaron Magnaye,	Antonette Porca, Joshua Panganiban
Subject: CMSC 137 Protobuf Milestone
Description: A class that acts as a wrapper for the Proto-generated class ErrLdnePacket
**/

import proto.*;

public class ELDPacket{

	private TcpPacketProtos.TcpPacket.ErrLdnePacket eldp;

	public ELDPacket(){
		this.eldp = TcpPacketProtos.TcpPacket.ErrLdnePacket.newBuilder()
											.setType(TcpPacketProtos.TcpPacket.PacketType.forNumber(2))
											.build();
	}

	public TcpPacketProtos.TcpPacket.ErrLdnePacket getPacket(){
		return this.eldp;
	}

	public void self(){
		System.out.println(this.eldp);
	}

	public byte[] serialize(){
		return this.eldp.toByteArray();
	}

	public TcpPacketProtos.TcpPacket.ErrLdnePacket deserialize(byte[] b){

		// returns null if there was an error

		TcpPacketProtos.TcpPacket.ErrLdnePacket n = null;		
		try{

			n = TcpPacketProtos.TcpPacket.ErrLdnePacket.parseFrom(b);


		}catch(Exception e){
			System.out.println(e);
		}
		return n;
	}

}