
import proto.*;

public class ConnectPacket{

	private TcpPacketProtos.TcpPacket.ConnectPacket cp;

	public ConnectPacket(Player p){
		this.cp = TcpPacketProtos.TcpPacket.ConnectPacket.newBuilder()
							.setType(TcpPacketProtos.TcpPacket.PacketType.forNumber(1))
							.setUpdate(TcpPacketProtos.TcpPacket.ConnectPacket.Update.forNumber(0))
							.build();
	}

	public TcpPacketProtos.TcpPacket.ConnectPacket getPacket(){
		return this.cp;
	}

	public void self(){
		System.out.println(this.cp);
	}

	public byte[] serialize(){
		return this.cp.toByteArray();
	}

}