
package GUIChat;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

//import SimpleDraw.LineDrawer;
//import SimpleDraw.PositionRecorder;


public class Client extends Thread implements ActionListener {

	String history = "";
	ChatMessage myObject;
	boolean sendingdone = false, receivingdone = false;
	Scanner scan;
	Socket socketToServer;
	ObjectOutputStream myOutputStream;
	ObjectInputStream myInputStream;
	Frame f;
	TextArea ta;
	BufferedWriter fileWrite;
	Frame drawFrame;

	private JPanel northPanel = new JPanel();
	private JPanel DrawingPanel = new JPanel();
	private JButton connectBtn = new JButton();
	private JButton userlistBtn = new JButton();
	private JButton Draw = new JButton();
	private JButton messageHistoryBtn = new JButton("Message History");
	private JTextField nameField = new JTextField();
	private JTextField tf = new JTextField();
	private JTextField tfServer = new JTextField();
	private JTextField tfPort = new JTextField();
	public Client() {

		// ############## FRAME SETTINGS ##############

		f = new Frame();
		f.setSize(600, 550);
		f.setLayout( new BorderLayout() );
		northPanel.setLayout( new GridLayout( 1, 5 ) );
		f.add( northPanel, BorderLayout.NORTH );
		f.setTitle("Chat Client");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});

		// ############## CONNECT BUTTON ##############

		connectBtn = new JButton("Connect");
		northPanel.add( connectBtn );
		connectBtn.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						connectionBtnFunction();
					}
				}
				);

		// ################ USER LIST BUTTON #############

		userlistBtn = new JButton("Userlist");
		northPanel.add( userlistBtn );
		userlistBtn.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//ta.append("Users currently In chat : \n");
						getUserList();
					}
				}
				);

		// ############# MESSAGE HISTORY BUTTON   ############	

		messageHistoryBtn = new JButton("History");
		northPanel.add(messageHistoryBtn);
		messageHistoryBtn.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							writeToFile();
							//File dir = new File(".");
							//File fin = new File(dir.getCanonicalPath() + File.separator + "in.txt");
							//OpenHistory(fin);
						}
						catch(IOException eee){
						}
					}
				}
				);

		// ############# Drawing Button  #############

		Draw = new JButton("Draw");
		northPanel.add(Draw);
		Draw.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Drawing();
			}
		});
		userlistBtn.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Drawing();
					}
				}
				);

		// ######## TEXT FIELD FOR USER NAME  ########

		nameField = new JTextField("");
		northPanel.add(nameField);
		nameField.requestFocus();

		// ######### USER CHAT MESSAGE INPUT #########

		tf = new JTextField();
		tf.addActionListener(this);
		f.add(tf, BorderLayout.SOUTH);

		// ############# CHAT TEXT AREA ##############

		ta = new TextArea();
		f.add(ta, BorderLayout.CENTER);

		f.setVisible(true);
	}

	// ########### TO HANDLE TEXT INPUT ###########
	
	//for sending the chat message to server
	public void actionPerformed(ActionEvent ae) {
		myObject = new ChatMessage();
		//sets name for ChatMessage
		myObject.setName(nameField.getText());
		//gets the text from the chat text area and puts it in the ChatMessage 
		myObject.setMessage(tf.getText());
		//Resets the column for typing message again
		tf.setText("");
		try {
			myOutputStream.reset();
			//writes the object to output stream
			myOutputStream.writeObject(myObject);
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}

	}


	// ############ RUN METHOD ###################

	public void run()  {

		System.out.println("Listening for messages from server . . . ");

		try {
			//keeps checking for input from server 
			while (!receivingdone) {
				myObject = (ChatMessage) myInputStream.readObject();
				//to display users online
				if(myObject.getName().equals("Users Currently Online : ")){
					ta.append(myObject.getName() + " \n" + myObject.getMessage() + "\n");
				}
				else{
					//to display any other message
					ta.append(myObject.getName() + " : " + myObject.getMessage() + "\n");
					storeMessage(myObject.getName(), myObject.getMessage());
				}

				//userlist.add(myObject.getName());

			}
		} catch (IOException ioe) {
			System.out.println("IOE: " + ioe.getMessage());
		} catch (ClassNotFoundException cnf) {
			System.out.println(cnf.getMessage());
		}
	}

	void toConnect() throws Exception {
		//to establish socket connection
		nameField.setVisible(false);
		f.setTitle(nameField.getText());
		socketToServer = new Socket("127.0.0.1", 5050);
		myOutputStream = new ObjectOutputStream(socketToServer.getOutputStream());
		myInputStream = new ObjectInputStream(socketToServer.getInputStream()); 
		connected();
		start();
	}

	// ############### CONNECT / DISCONNECT BUTTON ##############
	void connectionBtnFunction() {
		try {
			if (connectBtn.getLabel() == "Connect") {

				toConnect();
				connectBtn.setLabel("Disconnect");

			} else if (connectBtn.getLabel() == "Disconnect") {

				disconnectSignal();
				connectBtn.setLabel("Connect");
				socketToServer.close();
				System.exit(0);

			}
		} catch (Exception ee) {
		}
	}
	// ############### TO DISPLAY CONNECTED ###################

	void connected(){
		myObject = new ChatMessage();
		myObject.setName(nameField.getText());
		myObject.setMessage("-Has Joined-");

		try {
			myOutputStream.reset();
			myOutputStream.writeObject(myObject);
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}

	// ################# TO GET USERLIST ####################

	void getUserList(){
		myObject = new ChatMessage();
		myObject.setName(nameField.getText());
		myObject.setMessage("Send_List");

		try {
			myOutputStream.reset();
			myOutputStream.writeObject(myObject);
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}

	// ################# TO DISCONNECT ######################

	void disconnectSignal(){
		myObject = new ChatMessage();
		myObject.setName(nameField.getText());
		myObject.setMessage("Disconnect_me");

		try {
			myOutputStream.reset();
			myOutputStream.writeObject(myObject);
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}

	}

	// ############# TO WRITE HISTORY IN TEXT FILE ##############

	void writeToFile() throws IOException{
		String fileName = f.getTitle() + ".txt";
		fileWrite = new BufferedWriter( new FileWriter(fileName));
		fileWrite.write(history);
		System.out.println(fileName + " File Writing Successful!!");
		System.out.println("Data in file :\n" + history);
		fileWrite.flush();
		File file = new File(fileName);
		java.awt.Desktop.getDesktop().open(file);

	}

	// ################# TO STORE HISTORY ####################

	void storeMessage(String name, String message){
		history = history + name + " : " + message + "\n";

	}


	// ################# DRAWING #######################

	void Drawing(){
		
		// to call simple draw
		
	}

	// #################### MAIN METHOD  ##################

	public static void main(String[] arg) {

		Client c = new Client();

	}
	public void newFrame(){
		drawFrame = new Frame();
		drawFrame .setTitle("Drawing");
		drawFrame .setSize(100, 100);
		drawFrame .setBackground(Color.white);
		drawFrame .setVisible(true);
	    //addMouseListener(new PositionRecorder());
	   // addMouseMotionListener(new LineDrawer());
	}

}