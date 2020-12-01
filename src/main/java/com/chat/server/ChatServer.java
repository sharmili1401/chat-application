package com.chat.server;

import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * This is the chat server program. Press Ctrl + C to terminate the program.
 */
public class ChatServer {
	private final int port;
	private final Set<String> userNames = new HashSet<>();
	private final Set<UserThread> userThreads = new HashSet<>();

	public ChatServer(final int port) {
		this.port = port;
	}

	public static FileWriter fileWriter;

	public void execute() throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(port)) {

//			System.out.println("Chat Server is listening on port " + port);
			fileWriter = new FileWriter("messages.txt");

			while (true) {
				final Socket socket = serverSocket.accept();
//				System.out.println("New user connected");

				final UserThread newUser = new UserThread(socket, this);
				userThreads.add(newUser);
				newUser.start();

			}

		} catch (final IOException ex) {
			System.out.println("Error in the server: " + ex.getMessage());
//			ex.printStackTrace();
		} finally {
			fileWriter.close();
		}
	}

	public static void main(final String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Proper Syntax to run the ChatServer: java ChatServer <port-number>");
			System.exit(0);
		}

		final int port = Integer.parseInt(args[0]);

		final ChatServer server = new ChatServer(port);
		server.execute();
	}

	/**
	 * Delivers a message from one user to others (broadcasting)
	 */
	void broadcast(final String message, final UserThread excludeUser, final MessageTypes messageTypes) {
		for (final UserThread aUser : userThreads) {
			if (aUser != excludeUser) {
				aUser.sendMessage(messageTypes.getValue() + message);
			}
			// Send out message to the same user
			if (aUser == excludeUser && messageTypes.equals(MessageTypes.IN)) {
				aUser.sendMessage(MessageTypes.OUT.getValue() + message.replaceFirst("<=", "=>"));
			}
		}
	}

	/**
	 * Stores userName of the newly connected client.
	 */
	void addUserName(final String userName) {
		userNames.add(userName);
		System.out.println(String.format("User '%s' is connected..", userName));
	}

	/**
	 * When a client is disconnected, removes the associated userName and UserThread
	 */
	void removeUser(final String userName, final UserThread aUser) {
		final boolean removed = userNames.remove(userName);
		if (removed) {
			userThreads.remove(aUser);
			System.out.println(String.format("User '%s' is gone..", userName));
		}
	}

	Set<String> getUserNames() {
		return userNames;
	}

	/**
	 * Returns true if there are other users connected (not count the currently
	 * connected user)
	 */
	boolean hasUsers() {
		return !userNames.isEmpty();
	}
}