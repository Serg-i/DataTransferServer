package main;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable  {
	private static final int DEFAULT_PORT = 2154;
	private static Thread serverThread;
	int port;
	String directory;
	ServerSocket serverSocket = null;
    private static Server instance = null;
    
	Server(){
		this.port = DEFAULT_PORT;
		this.directory =""; 
	}
	Server(int port,String directory) {
		this.port = port;
		this.directory =directory; 
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static synchronized Server getServer(int port,String directory){
		  if (instance == null) {
	            instance = new Server(port,directory);
	        }
	        return instance;
	}
	public static synchronized Server getServer(){
		  if (instance == null) {
	            instance = new Server(DEFAULT_PORT,"");
	        }
	        return instance;
	}
	public void start(){
		Window.log("Starting server... \n");
		serverThread = new Thread(instance);
		Window.log(" server started \n");
		serverThread.start();
	}
	public void stop(){
		serverThread.interrupt();//TODO: add some logic for stopping the server
	}
	private void connectHandler() {
		try {
			while (true) {
				Window.log("Wait connect...\n");
				Socket soket = serverSocket.accept();
				InputStream in = soket.getInputStream();
				DataInputStream din = new DataInputStream(in);

				long fileSize = din.readLong(); // получаем размер файла
				String fileName = din.readUTF(); // прием имени файла
				Window.log("Имя файла: " + fileName + "\n");
				Window.log("Размер файла: " + fileSize + " байт\n");

				byte[] buffer = new byte[64 * 1024];
				FileOutputStream outF = new FileOutputStream(directory+File.separator+fileName);
				int count, total = 0;

				while ((count = din.read(buffer)) != -1) {
					total += count;
					outF.write(buffer, 0, count);

					if (total == fileSize) {
						break;
					}
				}
				outF.flush();
				outF.close();
				Window.log("Файл принят\n---------------------------------\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
			Window.log("can't create server at port " + port);
		}
	}
	@Override
	public void run() {
			connectHandler();	
	}


}
