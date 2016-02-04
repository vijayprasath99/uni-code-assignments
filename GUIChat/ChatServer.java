
package GUIChat;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

	//getting user list
	static Set<String> userlist = new HashSet();

	//main function
	public static void main(String[] args) {
		
		// Array list to create handler 
		ArrayList<ChatHandler> AllHandlers = new ArrayList<ChatHandler>();

		try {
			ServerSocket s = new ServerSocket(5050);

			// infinitely runs handler
			for (;;) {
				Socket incoming = s.accept();
				new ChatHandler(incoming, AllHandlers).start();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// function to create and add user to user list
	static String printlist() {

		String list = "";
		for (String i : userlist) {
			list = list + i + "\n";
		}
		return list;
	}

}

class ChatHandler extends Thread {

	public ChatHandler(Socket i, ArrayList<ChatHandler> h) {
		incoming = i;
		handlers = h;
		handlers.add(this);

		try {
			in = new ObjectInputStream(incoming.getInputStream());
			out = new ObjectOutputStream(incoming.getOutputStream());
		} catch (IOException ioe) {
			System.out.println("Could not create streams.");
		}
	}

	public synchronized void broadcast() throws Exception {

		ChatHandler left = null;
		for (ChatHandler handler : handlers) {
			ChatMessage cm = new ChatMessage();
			// Getting broadcast messages to handle userlist, disconnect 
			String x = new String(myObject.getMessage());
			String y = new String("Send_List");
			String z = new String("Disconnect_me");
			
			if (x.equals(y)) {
				cm.setName("Users Currently Online : ");
				cm.setMessage(ChatServer.printlist());
				handler.out.writeObject(cm);
				System.out.println("Sent User List .. ");
			}
			
			else if (x.equals(z)) {
				cm.setName(myObject.getName());
				ChatServer.userlist.remove(myObject.getName());
				cm.setMessage("-has disconnected-");
				handler.out.writeObject(cm);
				System.out.println("Disconnected user");
			}
			 
			else {
				cm.setName(myObject.getName());
				cm.setMessage(myObject.getMessage());
				handler.out.writeObject(cm);
				System.out.println("Writing to handler outputstream: " + cm.getName() + " : " + cm.getMessage());
				ChatServer.userlist.add(myObject.getName());
			}
		}

		handlers.remove(left);
		
		if (myObject.getMessage().equals("bye")) { //  client wants to leave
			done = true;
			handlers.remove(this);
			System.out.println("Removed handler. Number of handlers: " + handlers.size());
		}
		System.out.println("Number of handlers: " + handlers.size());
	}

	
	public void run() {
		try {
			while (!done) {
				myObject = (ChatMessage) in.readObject();
				System.out.println("Message read: " + "\"" + myObject.getMessage() + "\"" + " from " + "\"" + myObject.getName() + "\"");
				broadcast();
			}
		} catch (IOException e) {
			if (e.getMessage().equals("Connection reset")) {
				System.out.println("A client terminated its connection.");
			} else {
				System.out.println("Problem receiving: " + e.getMessage());
			}
		} catch (Exception cnfe) {
			System.out.println(cnfe.getMessage());
		} finally {
			handlers.remove(this);
		}
	}

	
	ChatMessage myObject = null;
	private Socket incoming;
	
	boolean done = false;
	ArrayList<ChatHandler> handlers;
	
	ObjectOutputStream out;
	ObjectInputStream in;
}