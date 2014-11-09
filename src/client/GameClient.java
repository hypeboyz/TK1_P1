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
	// Better keep the palyer name
	private static String PlayerName;
	private GameClient()
	{
		super();
	}

	// The following methods are client interfaces
	public void receiveFlyHunted(String playerName, long newPoints) throws RemoteException
	{
		System.out.printf("Name: %s\tScore: %d\n", playerName, newPoints);
	}
	
	public void receiveFlyPosition(int x, int y) throws RemoteException
	{
		System.out.printf("Name: %s\tx: %d\ty: %d\n", PlayerName, x, y);
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
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new SecurityManager());

		try {
			IGameClient obj = new GameClient();
			// Export the server interface object
			IGameClient stub = (IGameClient) UnicastRemoteObject.exportObject(obj, 0);

			Registry registry = LocateRegistry.getRegistry(null);
			registry.rebind("IGameClient", stub);
			ServerStub = (IGameServer)registry.lookup("IGameServer");

			// Following codes are used to test remote calls
			// Remove them once the synchronization is done
			PlayerName = args[0];
			ServerStub.login(args[0], stub);
			for (int i = 0; i < 10; i++) {
				Thread.sleep(800);
				ServerStub.huntFly(args[0]);
			}

			ServerStub.logout(args[0]);
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
