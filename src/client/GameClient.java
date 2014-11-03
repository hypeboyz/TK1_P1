package ClientBuild;

import javax.swing.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import impl.IGameServer;

public class GameClient {
	final static int width = 640, height = 640;
	private GameClient()
	{
		super();
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
			Registry registry = LocateRegistry.getRegistry(null);
			IGameServer stub = (IGameServer) registry.lookup("IGameServer");
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
