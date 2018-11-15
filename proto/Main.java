import proto.*;
// import TcpPacketProtos.*;

public class Main{
	public static void main(String[] args) {
		// PlayerProtos.Player p = PlayerProtos.Player.newBuilder()
		// 					.setName("p")
		// 					.setId("1")
		// 					.build();

		// byte[] b = p.toByteArray();
		// System.out.println(b);
		// System.out.println(p.getName());

		// TcpPacketProtos.TcpPacket tcp = TcpPacketProtos.TcpPacket.newBuilder()
		// 		.setType(TcpPacketProtos.TcpPacket.PacketType.forNumber(2))
		// 		.build();

		// System.out.println(tcp);

		TcpPacketProtos.TcpPacket.CreateLobbyPacket c = TcpPacketProtos.TcpPacket.CreateLobbyPacket.newBuilder()
											.setType(TcpPacketProtos.TcpPacket.PacketType.forNumber(2))
											.setMaxPlayers(4)
											.build();
		System.out.println(c);
	}
}