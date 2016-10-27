import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Random;

public class ClientStub implements HeaderInterface {
	
	private String filename = "portmapper.txt";
	private String Prog_Name = "1";
	private String Prog_Version = "1";
	private String mStrMSG = "";
	private String server = "";
	private int port = 0;
	private Random r = new Random();
	private Hashtable<String, Integer> transactions = new Hashtable<String, Integer>();
	private String ip = "";
	public ClientStub() {
		String[] serverInfo = connectToPortMapper().split(":");
		server = serverInfo[0];
		port = Integer.parseInt(serverInfo[1]);
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ip = addr.getHostAddress();
		
	}
	
	private Socket connectToServer() {
		Socket mSocket = null;
		try {
			// Create the server
			mSocket = new Socket(server, port);
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return mSocket;
	}

	private String connectToPortMapper() {
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

			Socket mSocket = new Socket(server, port);
			
			PrintWriter mPrintWriter = new PrintWriter(mSocket.getOutputStream(), true);
			BufferedReader mBufferedReader = new BufferedReader(new InputStreamReader(
					mSocket.getInputStream()));

			

			mPrintWriter.flush();
			mPrintWriter.println("c#" + Prog_Name + "@" + Prog_Version);
			//System.out.println("Server: Send message to Port Mapper: " + "s#" + Prog_Name + "@" + Prog_Version + "#"
			//		+ addr.getHostAddress() + ":" + this.port);
			//System.out.println("Server: Bind to Port Mapper");
			while (((mStrMSG = mBufferedReader.readLine()) != null)) {
				mStrMSG = mStrMSG.trim();
				if (mStrMSG.equals("0")){
					
				} else {
					return mStrMSG;
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	@Override
	public int add(int a, int b) {
		int procedure = 1;
		String[] msgs = null;
		Object result = null;
		try {
			Socket mSocket = connectToServer();
			int transid = r.nextInt();
			while (transactions.containsKey(transid+ip)) {
				transid = r.nextInt();
			}
			transactions.put(transid+ip, 0);
			mStrMSG = transid + ip + "," + Prog_Name + "," + Prog_Version + "," + procedure + ",";
			mStrMSG = mStrMSG + ObjectUtil.toString(a) + ",";
			mStrMSG = mStrMSG + ObjectUtil.toString(b);
			PrintWriter mPrintWriter = new PrintWriter(mSocket.getOutputStream(), true);
			BufferedReader mBufferedReader = new BufferedReader(new InputStreamReader(
					mSocket.getInputStream()));
			mPrintWriter.flush();
			mPrintWriter.println(mStrMSG);
			
			while (((mStrMSG = mBufferedReader.readLine()) != null)) {
				//mStrMSG = mStrMSG.trim();
				msgs = mStrMSG.split(",");
				if (msgs[0].equals("0")) {
					throw new Exception();
				}
				
				if (transactions.containsKey(msgs[1])){
					transactions.remove(msgs[1]);
					result = ObjectUtil.fromString(msgs[2]);
				}
				
				break;
			}
			
			mSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (int) result;
	}

}
