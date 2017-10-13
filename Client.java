import java.net.*;
import java.io.*;
import java.util.*;

/*
 * ISTE-121.02 Lab 07
 * The Client for the Client/Server chat program
 */
public class Client  {

	// declaring the Input and output streams
	private ObjectInputStream inputStream;		// to read from the socket
	private ObjectOutputStream outputStream;		// to write on the socket
	private Socket socket;

	// To use the GUI
   private SignomiGameGUI gGui;
	
	// declaring the server, the port, and the username
	private String server, username;
	private int port;

	/*
	 *  @param server: the server address
	 *  @param port: the port number
	 *  @param username: the username
	 */
	Client(String server, int port, String username) {
		this(server, port, username, null); // calls the common constructor with the GUI set to null
	}

	/*
	 * Constructor call
	 */
	Client(String server, int port, String username, SignomiGameGUI gGui) {
		this.server = server;
		this.port = port;
		this.username = username;
      this.gGui = gGui;
	}
	
	/*
	 * To start the dialog
	 */
	public boolean start() {
		// try to connect to the server
		try {
			socket = new Socket(server, port);
		} 
      //Error connecting to the server
		catch(Exception ec) {
			display("Error connecting to server:" + ec);
			return false;
		}
		
		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		display(msg);
	
		/* Creating both Data Stream */
		try
		{
			inputStream  = new ObjectInputStream(socket.getInputStream());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException eIO) {
			display("Exception creating new Input/output Streams: " + eIO);
			return false;
		}

		// creates the Thread to listen from the server 
		new ListenFromServer().start();
		// Send username to the server
		try
		{
			outputStream.writeObject(username);
		}
		catch (IOException eIO) {
			display("Exception doing login : " + eIO);
			disconnect();
			return false;
		}
		// success 
		return true;
	}

	/*
	 * To send a message to the GUI
	 */
	private void display(String msg) {
			gGui.append(msg + "\n");		// append to the ClientGUI JTextArea (or whatever)
	}
	
	/*
	 * To send a message to the server
	 */
	void sendMessage(ChatMessage msg) {
		try {
			outputStream.writeObject(msg);
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
		}
	}

	/*
	 * Error condition
	 */
	void disconnect() {
		try { 
			if(inputStream != null) inputStream.close();
		}
		catch(Exception e) {} 
		try {
			if(outputStream != null) outputStream.close();
		}
		catch(Exception e) {} 
        try{
			if(socket != null) socket.close();
		}
		catch(Exception e) {} 
		
		// inform the GUI
		if(gGui != null)
			gGui.connectionFailed();
			
	}

	/*
	 * This class waits for the message from the server and append them to the JTextArea
	 */
	class ListenFromServer extends Thread {

		public void run() {
			while(true) {
				try {
					String msg = (String) inputStream.readObject();
					gGui.append(msg);
				}
				catch(IOException e) {
					display("Server has closed the connection: " + e);
					if(gGui != null) 
						gGui.connectionFailed();
					break;
				}
				catch(ClassNotFoundException e2) {
				}
			}
		}
	}
}
