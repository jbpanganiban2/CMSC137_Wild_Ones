import proto.*;

public class Player{
	
	private PlayerProtos.Player p;
	private int pID;
	// private static int pNum;

	public Player(String name){
		this.p = PlayerProtos.Player.newBuilder()
						.setName(name)
						.build();
		// this.pID = Player.pNum++;
		this.pID = 0;
	}

	public Player(){
		this.p = PlayerProtos.Player.newBuilder()
						.setName("host")
						.build();
		// this.pID = Player.pNum++;
	}

	public Player(PlayerProtos.Player p, int index){
		this.p = p;
		this.pID = index;
		// this.pID = Player.pNum++;
	}

	public Player(PlayerProtos.Player p){
		this.p = p;
		// this.pID = index;
		// this.pID = Player.pNum++;
	}


	public Player(byte[] b){
		PlayerProtos.Player n = null;		

		try{

			n = PlayerProtos.Player.parseFrom(b);

		}catch(Exception e){
			System.out.println(e);
		}
		this.p = n;
		// this.pID = Player.pNum++;
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