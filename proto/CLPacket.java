/**
Author: Aaron Magnaye,	Antonette Porca, Joshua Panganiban
Subject: CMSC 137 Protobuf Milestone
Description: A class that acts as a wrapper for the Proto-generated class CreateLobbyPacket
**/

import proto.TcpPacketProtos.*;

import java.net.*;
import java.io.*;
import java.util.Scanner;


public class CLPacket{

	private TcpPacket.CreateLobbyPacket packet;

	public CLPacket(int maxPlayers){
		this.packet = TcpPacket.CreateLobbyPacket.newBuilder()
					.setType(TcpPacket.PacketType.forNumber(2))
					.setMaxPlayers(maxPlayers)
					.build();
	}

	public CLPacket(byte[] b){

		// transforms a byte stream to a CLPacket
		
		TcpPacket.CreateLobbyPacket n = null;		
		try{
			n = TcpPacket.CreateLobbyPacket.parseFrom(b);
		}catch(Exception e){
			System.out.println(e);
		}
		this.packet = n;
	}

	public TcpPacket.CreateLobbyPacket getPacket(){
		return this.packet;
	}

	public String getLobbyId(){
		return this.packet.getLobbyId();
	}

	public void self(){
		System.out.println(this.packet);
	}

	public byte[] serialize(){

		return this.packet.toByteArray();
	}
}






	