package ServerBuild;

import impl.IGameClient;

public class Player {
	private String PlayerName;
	private long Score;
	private IGameClient ClientInstance;

	//Constructors 
	public Player()
	{
		super();
		PlayerName = null;
		Score = 0L;
		ClientInstance = null;
	}

	public Player(String name, long score)
	{
		super();
		PlayerName = name;
		Score = score;
		ClientInstance = null;
	}

	public String getName()
	{
		return this.PlayerName;
	}

	public long getScore()
	{
		return this.Score;
	}

	public void setName(String name)
	{
		PlayerName = name;
	}

	public void setScore(long score)
	{
		Score = score;
	}

	public IGameClient getClient()
	{
		return ClientInstance;
	}

	public void setClient(IGameClient client)
	{
		ClientInstance = client;
	}
}
