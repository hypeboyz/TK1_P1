package impl;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameClient extends Remote{
	public void recieveFlyHunted(String playerName, int newPoints) throws RemoteException;
	public void recieveFlyPosition(int x, int y) throws RemoteException;
}
