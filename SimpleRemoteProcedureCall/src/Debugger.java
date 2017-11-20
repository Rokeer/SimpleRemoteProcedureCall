import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Debugger {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// connect to server when the app run
		try {
			// connect to server
			Socket mSocket = new Socket("150.212.41.123", 15222);
			// open input & output stream
			BufferedReader mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
			PrintWriter mPrintWriter = new PrintWriter(mSocket.getOutputStream(), true);

//			mPrintWriter.flush();
//			mPrintWriter.println("s#3@1,1@1#127.0.0.1,27015");
			mPrintWriter.flush();
			mPrintWriter.println("c#3@1");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
