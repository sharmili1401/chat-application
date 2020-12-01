# How Simple chat application works:
1. Creates a server to connect between all the clients using a Thread for each user.
2. Creates a client for user interaction and start two threads ReadThread and WriteThread.


# How to run the application from terminal:
1. Go to folder ~/target/classes
2. Run server using command: 'java com.chat.server.ChatServer <port-number>'
3. Run each client using command: 'java com.chat.client.ChatClient <host> <port-number>'
4. 'messages.txt' file will be generated in the project.

## Challenging part:
Implementing the separation of terminal into user interaction. I tried using \r command but it's not working as expected in MacOS.