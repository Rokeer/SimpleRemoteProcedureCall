import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PortMapper {

	private int port = 0;;
	private ServerSocket mServerSocket;
	private String filename = "";
	// thread pool
	private ExecutorService mExecutorService;
	private Hashtable<String, ArrayList<String>> mapper = new Hashtable<String, ArrayList<String>>();

	public PortMapper(String filename, int port) {
		this.filename = filename;
		this.port = port;
	}

	public void start() {
		try {
			// Create the server
			mServerSocket = new ServerSocket(port);
			// create a thread pool
			mExecutorService = Executors.newCachedThreadPool();
			System.out.println("Port Mapper: Created!");

			saveToFile();
			System.out.println("Port Mapper: Store Mapper information to " + filename);

			// Start listening for connections. The program waits until some
			// client connects to the socket.
			System.out.println("Port Mapper: Start listening on port " + port + ".");
			mExecutorService.execute(new PortMapperChecker(mapper));
			while (true) {
				// Wait for incoming connections
				Socket socket = mServerSocket.accept();
				System.out.println("Prot Mapper: New people is comming in.");
				// open a client thread
				mExecutorService.execute(new PortMapperThread(socket, mapper));
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

	private void saveToFile() throws UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();
		System.out.println("Port Mapper: IP Address: " + addr.getHostAddress());
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(filename));
			out.write(addr.getHostAddress() + ":" + port);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// String path = "/afs/cs.pitt.edu/usr0/colinzhang/public/Prj1HaoranZhang/";
		String path = "";
		String filename = path + "portmapper.txt";
		int port = 15222;
		PortMapper pm = new PortMapper(filename, port);
		pm.start();
	}

}
