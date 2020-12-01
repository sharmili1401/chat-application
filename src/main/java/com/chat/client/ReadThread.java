package com.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * This thread is responsible for reading server's input and printing it to the
 * console. It runs in an infinite loop until the client disconnects from the
 * server.
 */
public class ReadThread extends Thread {
	private BufferedReader reader;
	private final Socket socket;
	private final ChatClient client;

	public ReadThread(final Socket socket, final ChatClient client) {
		this.socket = socket;
		this.client = client;

		try {
			final InputStream input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));
		} catch (final IOException ex) {
			System.out.println("Error getting input stream: " + ex.getMessage());
//			ex.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				final String response = reader.readLine();
				if (response == null) {
					System.out.println("ChatServer is stopped..");
					System.exit(0);
				}
				if (!response.trim().isEmpty())
					System.out.println(response);

				// prints the username after displaying the server's message
//				if (client.getUserName() != null) {
//					System.out.print("[" + client.getUserName() + "]: ");
//				}
			} catch (final IOException ex) {
				System.out.println("Bye " + client.getUserName() + "!!");
//				System.out.println("Error reading from server: " + ex.getMessage());
//				ex.printStackTrace();
				break;
			}
		}
	}
}