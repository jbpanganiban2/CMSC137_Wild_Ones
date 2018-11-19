
import proto.*;

public class Player{
	
	private PlayerProtos.Player p;

	public Player(String name){
		this.p = PlayerProtos.Player.newBuilder()
						.setName(name)
						.build();
	}

	public PlayerProtos.Player getPlayer(){
		return this.p;
	}

	public void self(){
		System.out.println(this.p);
	}

	public byte[] serialize(){
		return this.p.toByteArray();
	}

}