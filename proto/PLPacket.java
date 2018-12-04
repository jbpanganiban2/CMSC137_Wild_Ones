/**
Author: Aaron Magnaye,	Antonette Porca, Joshua Panganiban
Subject: CMSC 137 Protobuf Milestone
Description: A class that acts as a wrapper for the Proto-generated class PlayerListPacket
**/

import proto.*;

public class PLPacket{

	private TcpPacketProtos.TcpPacket.PlayerListPacket packet;
	private Player p;

	public PLPacket(){
		this.packet = TcpPacketProtos.TcpPacket.PlayerListPacket.newBuilder()
					.setType(TcpPacketProtos.TcpPacket.PacketType.forNumber(4))
					.build();
		this.p = null;
	}

	public PLPacket(byte[] b){                                           // receiving constructor
          TcpPacketProtos.TcpPacket.PlayerListPacket n = null;    

          try{
               n = TcpPacketProtos.TcpPacket.PlayerListPacket.parseFrom(b);
          }catch(Exception e){
               System.out.println(e);
          }
          this.packet = n;
     }

	public TcpPacketProtos.TcpPacket.PlayerListPacket getPacket(){
		return this.packet;
	}

	public void self(){
		System.out.println(this.packet);
	}

	public Player getPlayerAt(int index){
		return new Player(this.packet.getPlayerList(index));
	}

	public Player[] getPlayerList(){
		int count = this.packet.getPlayerListCount();
		System.out.println("countofplayers: "+count);
		Player[] p = new Player[count];

		for(int i = 0; i < count; i++){
			p[i] = this.getPlayerAt(i);
			// p[i].self();
		}

		return p;
	}

	public int getPlayerCount(){
		return this.packet.getPlayerListCount();
	}

	public byte[] serialize(){
		return this.packet.toByteArray();
	}
}