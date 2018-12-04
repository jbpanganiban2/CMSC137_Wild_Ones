/**
Author: Aaron Magnaye,	Antonette Porca, Joshua Panganiban
Subject: CMSC 137 Protobuf Milestone
Description: A class that acts as a wrapper for the Proto-generated class DisconnectPacket
**/

import proto.*;

public class DCPacket{

	private TcpPacketProtos.TcpPacket.DisconnectPacket packet;

	public DCPacket(Player player){
		this.packet = TcpPacketProtos.TcpPacket.DisconnectPacket.newBuilder()
					.setType(TcpPacketProtos.TcpPacket.PacketType.forNumber(0))
					.setUpdate(TcpPacketProtos.TcpPacket.DisconnectPacket.Update.forNumber(0))
					.setPlayer(player.getPlayer())
					.build();
	}

	public DCPacket(byte[] b){                                           // receiving constructor
          TcpPacketProtos.TcpPacket.DisconnectPacket n = null;    

          try{
               n = TcpPacketProtos.TcpPacket.DisconnectPacket.parseFrom(b);
          }catch(Exception e){
               System.out.println(e);
          }
          this.packet = n;

          // System.out.println(this.packet);
     }

     public String getPlayerName(){
     	return (this.packet.getPlayer().getName());
     }

	public TcpPacketProtos.TcpPacket.DisconnectPacket getPacket(){
		return this.packet;
	}

	public void self(){
		System.out.println(this.packet);
	}

	public byte[] serialize(){
		return this.packet.toByteArray();
	}

}