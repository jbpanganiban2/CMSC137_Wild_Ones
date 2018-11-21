
import proto.*;

public class ConnectPacket{

	private TcpPacketProtos.TcpPacket.ConnectPacket packet;

	public ConnectPacket(Player p, String lobby_id){
		this.packet = TcpPacketProtos.TcpPacket.ConnectPacket.newBuilder()
							.setType(TcpPacketProtos.TcpPacket.PacketType.forNumber(1))
              .setPlayer(p.getPlayer())
              .setLobbyId(lobby_id)
							.build();
	}

  public ConnectPacket(byte[] b){
    TcpPacketProtos.TcpPacket.ConnectPacket n = null;    

    try{

      n = TcpPacketProtos.TcpPacket.ConnectPacket.parseFrom(b);

    }catch(Exception e){
      System.out.println(e);
    }
    this.packet = n;
  }

	public TcpPacketProtos.TcpPacket.ConnectPacket getPacket(){
		return this.packet;
	}

	public void self(){
		System.out.println(this.packet);
	}

	public byte[] serialize(){
    // System.out.println("cp being serialized");
		return this.packet.toByteArray();
	}

}