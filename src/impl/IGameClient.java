package impl;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameClient extends Remote{
	public void receiveFlyHunted(String playerName, long newPoints) throws RemoteException;
	public void receiveFlyPosition(int x, int y) throws RemoteException;
}
