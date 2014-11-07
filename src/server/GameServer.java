package ServerBuild;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.lang.Integer;
import java.util.Random;
import impl.IGameClient;
import impl.IGameServer;

public class GameServer implements IGameServer, IGameClient {
	private static final int DefaultPlayers = 2;
	private static int PlayerNum = 0;
	private static Player PlayerList[];
	private static int PosX, PosY;

	private GameServer()
	{
		super();
	}

	public int findNextFreeSlot()
	{
		for (int i = 0; i < PlayerNum; i++)
			if (PlayerList[i].getName() == null)
				return i;
		// Cannot find FreeSlot
		return -1;
	}

	public int findClientIndex(String name)
	{
		for (int i = 0; i < PlayerNum; i++)
			if (PlayerList[i].getName() == name)
				return i;
		// Cannot spot the Client
		return -1;
	}

	// The following methods are server interfaces
	public synchronized void login(String playerName, IGameClient client) throws RemoteException
	{
		int n;

		n = findNextFreeSlot();
		if (n < 0) 
			throw new RemoteException();

		PlayerList[n].setName(playerName);
		PlayerList[n].setClient(client);
	}

	public synchronized void logout(String playerName) throws RemoteException
	{
		int n;

		n = findClientIndex(playerName);
		if (n < 0)
			throw new RemoteException();

		PlayerList[n].setName(null);
		PlayerList[n].setScore(0);
		PlayerList[n].setClient(null);
	}

	public synchronized void huntFly(String playerName) throws RemoteException
	{
		int n;

		n = findClientIndex(playerName);
		if (n < 0)
			throw new RemoteException();

		PlayerList[n].setScore(PlayerList[n].getScore() + 10L);
	}

	// The following methods are client interfaces
	public void recieveFlyHunted(String playerName, long newPoints)
	{
		int n;

		n = findClientIndex(playerName);
		if (n < 0)
			return ;

		newPoints = PlayerList[n].getScore();
	}

	public void recieveFlyPosition(int x, int y)
	{
		x = PosX;
		y = PosY;
	}

	public static void main(String args[])
	{
		int n;
		Random randomGenerator = new Random();

		PosX = randomGenerator.nextInt(640 - 32);
		PosY = randomGenerator.nextInt(640 - 32);

		n = (args.length < 1) ? DefaultPlayers : Integer.parseInt(args[0]);
		PlayerList = new Player[n];
		PlayerNum = n;

		try {
			IGameServer obj = new GameServer();
			// Export the object
			IGameServer stub = (IGameServer) UnicastRemoteObject.exportObject(obj, 0);

			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("IGameServer", stub);

			System.err.println("Server ready");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}
}
