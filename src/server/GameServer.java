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

public class GameServer implements IGameServer {
	private static final int DefaultPlayers = 2;
	private static IGameClient ClientStub = null;
	private static int PlayerNum = 0;
	private static Player PlayerList[];
	private static int PosX, PosY;
	private volatile static int FlyLock;

	private GameServer()
	{
		super();
	}

	public int findNextFreeSlot()
	{
		for (int i = 0; i < PlayerNum; i++) {
			if (PlayerList[i] == null)
				return i;
			if (PlayerList[i].getName() == null)
				return i;
		}
		// Cannot find FreeSlot
		return -1;
	}

	public int findClientIndex(String name) 
	{
		for (int i = 0; i < PlayerNum; i++) {
			if (PlayerList[i] == null)
				continue;
			if (PlayerList[i].getName().equals(name))
				return i;
		}
		// Cannot spot the Client
		return -1;
	}

	public int getNextPos()
	{
		Random randomGenerator = new Random();

		return randomGenerator.nextInt(640 - 32);
	}

	// The following methods are server interfaces
	public synchronized void login(String playerName, IGameClient client) throws RemoteException
	{
		int n;

		n = findNextFreeSlot();
		if (n < 0) 
			throw new RemoteException();

		try {
			// Cases that the first client try to login
			if (ClientStub == null) {
				Registry registry = LocateRegistry.getRegistry(null);
				ClientStub = (IGameClient)registry.lookup("IGameClient");
			}
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}

		PlayerList[n] = new Player(playerName, 0);
		PlayerList[n].setClient(client);
	}

	public synchronized void logout(String playerName) throws RemoteException
	{
		int n;

		n = findClientIndex(playerName);
		if (n < 0)
			throw new RemoteException();

		PlayerList[n] = null;
	}

	public synchronized void huntFly(String playerName) throws RemoteException
	{
		int n;
		if (FlyLock != 0) {
			FlyLock = 0;
			n = findClientIndex(playerName);
			if (n < 0)
				throw new RemoteException();

			PlayerList[n].setScore(PlayerList[n].getScore() + 10L);
			ClientStub.receiveFlyHunted(playerName, PlayerList[n].getScore());
			PosX = getNextPos();
			PosY = getNextPos();
			ClientStub.receiveFlyPosition(PosX, PosY);
		} else {
			ClientStub.receiveFlyPosition(PosX, PosY);
			FlyLock++;
		}
	}

	public static void main(String args[])
	{
		int n;

		n = (args.length < 1) ? DefaultPlayers : Integer.parseInt(args[0]);
		Random randomGenerator = new Random();

		PosX = randomGenerator.nextInt(640 - 32);
		PosY = randomGenerator.nextInt(640 - 32);

		PlayerList = new Player[n];
		PlayerNum = n;

		FlyLock = 1;

		if (System.getSecurityManager() == null)
			System.setSecurityManager(new SecurityManager());

		try {
			IGameServer obj = new GameServer();
			// Export the server interface object
			IGameServer stub = (IGameServer) UnicastRemoteObject.exportObject(obj, 0);

			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.getRegistry(null);
			registry.rebind("IGameServer", stub);

			System.out.println("Server ready");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}
}
