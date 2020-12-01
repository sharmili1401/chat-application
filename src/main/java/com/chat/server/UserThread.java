package com.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This thread handles connection for each connected client, so the server can
 * handle multiple clients at the same time.
 */
public class UserThread extends Thread {
	private final Socket socket;
	private final ChatServer server;
	private PrintWriter writer;

	public UserThread(final Socket socket, final ChatServer server) {
		this.socket = socket;
		this.server = server;
	}

	@Override
	public void run() {
		try {
			final InputStream input = socket.getInputStream();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(input));

			final OutputStream output = socket.getOutputStream();
			writer = new PrintWriter(output, true);

			printUsers();
			printHelp();

			final String userName = reader.readLine();
			server.addUserName(userName);

			String serverMessage = String.format("User '%s' is connected..\n", userName);
			ChatServer.fileWriter.write(serverMessage);
			ChatServer.fileWriter.flush();
			server.broadcast("\r" + serverMessage, this, MessageTypes.INFO);

			String clientMessage;

			do {
				clientMessage = reader.readLine();
				if (!clientMessage.equals("/exit") && !clientMessage.equals("/help")
						&& clientMessage.contains("/send")) {
					serverMessage = userName + " <= "
							+ clientMessage.substring(clientMessage.indexOf("/send") + 5).trim();
					ChatServer.fileWriter.write(userName + " : "
							+ clientMessage.substring(clientMessage.indexOf("/send") + 5).trim() + "\n");
					ChatServer.fileWriter.flush();
					server.broadcast(serverMessage, this, MessageTypes.IN);
				} else if (!clientMessage.equals("/exit")) {
					printHelp();
				}
			} while (!clientMessage.equals("/exit"));

			server.removeUser(userName, this);
			socket.close();

			serverMessage = String.format("User '%s' is gone..\n", userName);
			ChatServer.fileWriter.write(serverMessage);
			ChatServer.fileWriter.flush();
			server.broadcast(serverMessage, this, MessageTypes.INFO);

		} catch (final IOException ex) {
			System.out.println("Error in UserThread: " + ex.getMessage());
//			ex.printStackTrace();
		}
	}

	/**
	 * Sends a list of online users to the newly connected user.
	 */
	void printUsers() {
		if (server.hasUsers()) {
			writer.print("\rConnected users till now: " + server.getUserNames());
		} else {
			writer.print("\rNo other users connected yet");
		}
	}

	/**
	 * Send help related information to the users
	 */
	void printHelp() {
		writer.println("\nCommand overview:");
		writer.println("	/help - This help page");
		writer.println("	/exit - Exit from application");
		writer.println("	/send <message> - Send message for other participants");
	}

	/**
	 * Sends a message to the client.
	 */
	void sendMessage(final String message) {
		writer.println("\r" + message);
	}
}