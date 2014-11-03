package ServerBuild;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import impl.IGameClient;
import impl.IGameServer;

public class GameServer implements IGameServer {
	GameServer()
	{
		super();
	}
	public void login (String playerName, IGameClient client)
	{
	}
	public void logout(String playerName)
	{
	}
	public void huntFly(String playerName)
	{
	}
	public static void main(String args[])
	{
		try {
			IGameServer obj = new GameServer();
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
