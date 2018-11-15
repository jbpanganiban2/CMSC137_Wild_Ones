import proto.*;

public class Main{
	public static void main(String[] args) {
		PlayerProtos.Player p = PlayerProtos.Player.newBuilder()
							.setName("p")
							.setId("1")
							.build();
		System.out.println(p.getName());
	}
}