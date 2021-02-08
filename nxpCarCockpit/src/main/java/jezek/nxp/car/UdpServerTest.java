package jezek.nxp.car;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServerTest {
	public static void main(String args[]) throws Exception {
		boolean end = false;
		try (DatagramSocket clientSocket = new DatagramSocket(4444)) {
			byte[] receiveData = new byte[2048];
			while (!end) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				String modifiedSentence = new String(receivePacket.getData());
				System.out.println("FROM SERVER:" + modifiedSentence);
			}
		}
	}
}
