import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerStub {
	String Prog_Name = "1";
	String Prog_Version = "2";
	int port = 0;
	String filename = "";
	private ServerSocket mServerSocket;
	// thread pool
	private ExecutorService mExecutorService;
	private Hashtable<String, String> cache = new Hashtable<String, String>();

	public ServerStub(String filename, int port) {
		this.filename = filename;
		this.port = port;
	}

	public void start() {
		System.out.println("Server: Created!");
		connectToPortMapper();

		try {
			// Create the server
			mServerSocket = new ServerSocket(port);
			// create a thread pool
			mExecutorService = Executors.newCachedThreadPool();
			System.out.println("Server: Start server for services!");

			// Start listening for connections. The program waits until some
			// client connects to the socket.
			System.out.println("Server: Start listening on port " + port + ".");

			while (true) {
				// Wait for incoming connections
				Socket socket = mServerSocket.accept();
				System.out.println("Prot Mapper: New client is comming in.");
				// open a client thread
				mExecutorService.execute(new ServerThread(socket, cache));
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (mServerSocket != null) {
				try {
					// Close the server
					mServerSocket.close();
					System.out.println("Port Mapper: Closed!");
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("resource")
	private void connectToPortMapper() {
		try {
			// This will reference one line at a time
			String line = null;
			String server = "";
			int port = 0;
			try {
				// FileReader reads text files in the default encoding.
				FileReader fileReader = new FileReader(filename);

				// Always wrap FileReader in BufferedReader.
				BufferedReader bufferedReader = new BufferedReader(fileReader);

				while ((line = bufferedReader.readLine()) != null) {
					line = line.trim();
					String[] serverInfo = line.split(":");
					server = serverInfo[0];
					port = Integer.parseInt(serverInfo[1]);
					break;
				}
				// Always close files.
				bufferedReader.close();
			} catch (FileNotFoundException ex) {
				System.out.println("Unable to open file '" + filename + "'");
			} catch (IOException ex) {
				System.out.println("Error reading file '" + filename + "'");
				// Or we could just do this:
				// ex.printStackTrace();
			}

			System.out.println("Server: Port Mapper Server is " + server + ":" + port);
			// connect to server
			Socket mSocket = new Socket(server, port);
			System.out.println("Server: Connect to Port Mapper");
			// open input & output stream
			PrintWriter mPrintWriter = new PrintWriter(mSocket.getOutputStream(), true);

			InetAddress addr = InetAddress.getLocalHost();

			mPrintWriter.flush();
			mPrintWriter.println("s#" + Prog_Name + "@" + Prog_Version + "#" + addr.getHostAddress() + ":" + this.port);
			System.out.println("Server: Send message to Port Mapper: " + "s#" + Prog_Name + "@" + Prog_Version + "#"
					+ addr.getHostAddress() + ":" + this.port);
			System.out.println("Server: Bind to Port Mapper");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// String path = "/afs/cs.pitt.edu/usr0/colinzhang/public/";
		String path = "";
		String filename = path + "portmapper.txt";
		int port = 15223;
		ServerStub ss = new ServerStub(filename, port);
		ss.start();
	}

}