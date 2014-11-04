package impl;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameServer extends Remote{
	public void login(String playerName, IGameClient client) throws RemoteException;
	public void logout(String playerName) throws RemoteException;
	public void huntFly(String playerName) throws RemoteException;
}
