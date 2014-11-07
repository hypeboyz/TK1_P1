package ClientBuild;

import javax.swing.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import impl.IGameClient;
import impl.IGameServer;

public class GameClient implements IGameClient{
	private final static int width = 640, height = 640;
	private static IGameServer ServerStub;
	private GameClient()
	{
		super();
	}

	// The following methods are client interfaces
	public void receiveFlyHunted(String playerName, long newPoints) throws RemoteException
	{
		System.out.printf("Name: %s\nScore: %l\n", playerName, newPoints);
	}
	
	public void receiveFlyPosition(int x, int y) throws RemoteException
	{
		System.out.printf("x: %d\ny: %d\n", x, y);
	}

	private static void createAndShowGUI()
	{
		//Create and set up the window.
		JFrame frame = new JFrame("Fly Hunting");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(width, height);
		JLabel label = new JLabel("Fly Hunting");
		frame.getContentPane().add(label);

		//Display the window and block resizability
		frame.setVisible(true);
		frame.setResizable(false);
	}

	public static void main(String[] args)
	{
		try {
			IGameClient obj = new GameClient();
			// Export the server interface object
			IGameClient stub = (IGameClient) UnicastRemoteObject.exportObject(obj, 0);

			Registry registry = LocateRegistry.getRegistry(null);
			registry.rebind("IGameClient", stub);
			ServerStub = (IGameServer)registry.lookup("IGameServer");

			ServerStub.login("Chen", stub);
			ServerStub.logout("Chen");
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
