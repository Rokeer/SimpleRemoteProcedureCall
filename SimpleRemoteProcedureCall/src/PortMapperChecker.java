import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

public class PortMapperChecker implements Runnable {
	Hashtable<String, ArrayList<String>> mapper = null;

	public PortMapperChecker(Hashtable<String, ArrayList<String>> mapper) {
		this.mapper = mapper;
	}

	@Override
	public void run() {
		try {
			while (true){
				Thread.sleep(60000);
				BufferedReader br = null;
				PrintWriter pw = null;
				for (String key : mapper.keySet()) {
					ArrayList<String> al = mapper.get(key);
					key = key.replace("@", ",");
					for (int i = 0; i < al.size(); i++) {
						String server = al.get(i);
						System.out.println("Port Mapper: Checking server availability: " + server);
						String[] tmp = server.split(":");
						int port = Integer.parseInt(tmp[1]);
						server = tmp[0];
						Socket mSocket = null;
						try {
							mSocket = new Socket(server, port);
							br = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
							pw = new PrintWriter(mSocket.getOutputStream(), true);
							pw.println("0," + key + ",0");
							String msg = "";
							while (((msg = br.readLine()) != null)) {
								msg = msg.trim();
								System.out.println("Port Mapper: Receive mssage from server: " + msg);
								if (!msg.equals("1,0,1")) {
									System.out.println("Port Mapper: This server has problem, remove it from servers list");
									al.remove(i);
									i = i - 1;
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
							al.remove(i);
							i = i - 1;
						}
					}

				}
			}
			


		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
