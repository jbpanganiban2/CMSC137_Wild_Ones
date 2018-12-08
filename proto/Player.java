import proto.*;

public class Player{
	
	private PlayerProtos.Player p;
	private int pID;
	private static int pNum;

	public Player(String name){
		this.p = PlayerProtos.Player.newBuilder()
						.setName(name)
						.build();
		this.pID = pNum++;
	}

	public Player(){
		this.p = PlayerProtos.Player.newBuilder()
						.setName("host")
						.build();
	}

	public Player(PlayerProtos.Player p){
		this.p = p;
	}

	public Player(byte[] b){
		PlayerProtos.Player n = null;		

		try{

			n = PlayerProtos.Player.parseFrom(b);

		}catch(Exception e){
			System.out.println(e);
		}
		this.p = n;
	}

	public PlayerProtos.Player getPlayer(){
		return this.p;
	}

	public String getName(){
		return this.p.getName();
	}

	public String getID(){
		return Integer.toString(this.pID);
	}

	public void self(){
		System.out.println(this.p);
	}

	public byte[] serialize(){
		return this.p.toByteArray();
	}

}

// TODO
// remove start button in Lobby of Client