import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

public class PortMapperThread implements Runnable {
	private Socket mSocket;
	private BufferedReader mBufferedReader;
	private PrintWriter mPrintWriter;
	private String mStrMSG;
	private Hashtable<String, ArrayList<String>> mapper;
	
	public PortMapperThread (Socket socket, Hashtable<String, ArrayList<String>> mapper) throws IOException {
		this.mSocket = socket;
		this.mapper = mapper;
		mBufferedReader = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
	}
	
	@Override
	public void run() {
		try {
			while (((mStrMSG = mBufferedReader.readLine()) != null)) {
				mStrMSG = mStrMSG.trim();
				String[] msgs = mStrMSG.split("#");
				if (msgs[0].equals("s")){
					// handle server request
					System.out.println("Port Mapper: This is a Server.");
					
					String[] services = msgs[1].split(",");
					ArrayList<String> servers;
					String s = "Port Mapper: This server provides ";
					for (int i = 0; i < services.length; i++) {
						s = s + services[i] + " ";
						if (mapper.containsKey(services[i])) {
							servers = mapper.get(services[i]);
							servers.add(msgs[2]);
						} else {
							servers = new ArrayList<String>();
							servers.add(msgs[2]);
							mapper.put(services[i], servers);
						}
					}
					s = s + "on " + msgs[2];
					System.out.println(s);
				} else if (msgs[0].equals("c")) {
					// handle client request
					System.out.println("Port Mapper: This is a Client.");
					if (mapper.containsKey(msgs[1])){
						// there is server provide this service
						mPrintWriter = new PrintWriter(
								mSocket.getOutputStream(), true);
						mPrintWriter.println(mapper.get(msgs[1]).get(0));
						System.out.println("Port Mapper: Return server address: " + mapper.get(msgs[1]).get(0));
					} else {
						// there is no server provide this service
						mPrintWriter = new PrintWriter(
								mSocket.getOutputStream(), true);
						mPrintWriter.println("0");
						System.out.println("Port Mapper: There is no server provide this service, return 0");
					}
					mPrintWriter.close();
				} else {
					System.out.println("Port Mapper: Bad request!");
				}
				// jump out of the loop, disconnect this socket.
				mSocket.close();
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
