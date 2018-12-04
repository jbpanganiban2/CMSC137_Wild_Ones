
import proto.*;

public class ConnectPacket{

	private TcpPacketProtos.TcpPacket.ConnectPacket packet;

	public ConnectPacket(Player p, String lobby_id){                          // creating constructor
		this.packet = TcpPacketProtos.TcpPacket.ConnectPacket.newBuilder()
					.setType(TcpPacketProtos.TcpPacket.PacketType.forNumber(1))
                         .setPlayer(p.getPlayer())
                         .setLobbyId(lobby_id)
					.build();
	}

     public ConnectPacket(byte[] b){                                           // receiving constructor
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

     public void showMessage(Player user){
          Player p = new Player(this.packet.getPlayer());
          if(p.getName().equals(user.getName()))return;
          System.out.println("\n"+p.getName()+" connected to lobby.");
     }

     public String getPlayerName(){
          return (new Player(this.packet.getPlayer())).getName();
     }

	public void self(){
		System.out.println(this.packet);
	}

	public byte[] serialize(){
		return this.packet.toByteArray();
	}

}