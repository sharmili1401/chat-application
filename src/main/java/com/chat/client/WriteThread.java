package com.chat.client;

import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This thread is responsible for reading user's input and send it to the
 * server. It runs in an infinite loop until the user types '/exit' to quit.
 */
public class WriteThread extends Thread {
	private PrintWriter writer;
	private final Socket socket;
	private final ChatClient client;

	public WriteThread(final Socket socket, final ChatClient client) {
		this.socket = socket;
		this.client = client;

		try {
			final OutputStream output = socket.getOutputStream();
			writer = new PrintWriter(output, true);
		} catch (final IOException ex) {
			System.out.println("Error getting output stream: " + ex.getMessage());
//			ex.printStackTrace();
		}
	}

	@Override
	public void run() {
		final Console console = System.console();
		final String userName = console.readLine("\nEnter your user name: ");
		client.setUserName(userName);
		writer.println(userName);

		String text;

		do {
//			text = console.readLine("[" + userName + "]: ");
//			text = console.readLine(">");
			text = console.readLine("");
			if (text != null && !text.trim().isEmpty())
				writer.println(text);

		} while (!text.equals("/exit"));

		try {
			socket.close();
		} catch (final IOException ex) {
//			System.out.println("Error writing to server: " + ex.getMessage());
		}
	}
}