package com.chat.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This is the chat client program. Type '/exit' to terminate the program.
 */
public class ChatClient {
	private final String hostname;
	private final int port;
	private String userName;

	public ChatClient(final String hostname, final int port) {
		this.hostname = hostname;
		this.port = port;
	}

	public void execute() {
		try {
			final Socket socket = new Socket(hostname, port);
//			System.out.println("Connected to the chat server");
			new ReadThread(socket, this).start();
			new WriteThread(socket, this).start();

		} catch (final UnknownHostException ex) {
			ex.getMessage();
//			System.out.println("Server not found: " + ex.getMessage());
		} catch (final IOException ex) {
			ex.getMessage();
//			System.out.println("I/O Error: " + ex.getMessage());
		}

	}

	void setUserName(final String userName) {
		this.userName = userName;
	}

	String getUserName() {
		return userName;
	}

	public static void main(final String[] args) {
		if (args.length < 2) {
			System.out.println("Proper Syntax to run the ChatClient: java ChatClient <port-name> <port-number>");
			return;
		}
		final String hostname = args[0];
		final int port = Integer.parseInt(args[1]);

		final ChatClient client = new ChatClient(hostname, port);
		client.execute();
	}
}