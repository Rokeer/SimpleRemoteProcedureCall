import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Hashtable;

public class ServerThread implements Runnable, HeaderInterface {
	String Prog_Name = "$Prog_Name$";
	String Prog_Version = "$Prog_Version$";
	private Socket mSocket;
	private BufferedReader mBufferedReader;
	private PrintWriter mPrintWriter;
	private String mStrMSG;
	private Hashtable<String, String> cache;
	private Server s = new Server();

	public ServerThread(Socket socket, Hashtable<String, String> cache) throws IOException {
		this.mSocket = socket;
		mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
		this.cache = cache;
	}

	@Override
	public void run() {
		try {
			while (((mStrMSG = mBufferedReader.readLine()) != null)) {
				mStrMSG = mStrMSG.trim();
				String[] msgs = mStrMSG.split(",");
				System.out.println("Server: Start handle the request");
				if ((msgs[1] + msgs[2]).equals(Prog_Name + Prog_Version)) {
					if (cache.containsKey(msgs[0])) {
						// request in cache
						System.out.println("Server: This is a executed transaction, return result directly");
						mPrintWriter = new PrintWriter(mSocket.getOutputStream(), true);
						mPrintWriter.println(cache.get(msgs[0]));
					} else {
						// request not in cache, then execute it
						System.out.println("Server: This is a new transaction, start execution");
						int procedure = Integer.parseInt(msgs[3]);
						String result = "1," + msgs[0] + ",";
						switch (procedure) {
						case 0:
							System.out.println("Server: Procedure " + procedure + " is executing");
							result = result + "1";
						$cases$
						default:
							result = "0," + msgs[0];
							System.out.println("Server: Unknow procedure");
							break;
						}
						cache.put(msgs[0], result);
						mPrintWriter = new PrintWriter(mSocket.getOutputStream(), true);
						mPrintWriter.println(result);
						System.out.println("Server: Execution finish, return results");

					}
				} else {
					// there is no server provide this service
					mPrintWriter = new PrintWriter(mSocket.getOutputStream(), true);
					mPrintWriter.println("0");
					System.out.println("Server: There is no requested service provide in this service, return 0");
				}

				mPrintWriter.close();
				// jump out of the loop, disconnect this socket.
				mSocket.close();
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	$methods$

}