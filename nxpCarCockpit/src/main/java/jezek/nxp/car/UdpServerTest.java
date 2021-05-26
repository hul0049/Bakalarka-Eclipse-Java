package jezek.nxp.car;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jezek.nxp.car.Tfc.andata_chnl_enum;
import jezek.nxp.car.Tfc.s_data;

public class UdpServerTest {
		
	public static void main(String args[]) throws Exception {
		boolean end = false;
		try (DatagramSocket clientSocket = new DatagramSocket(4444)) {
			byte[] receiveData = new byte[2048];
			while (!end) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				//WifiMonitorData data = new WifiMonitorData(receivePacket.getData());
				//String modifiedSentence = new String(receivePacket.getData());
				MyData data2 = new MyData();
				data2.setData(receivePacket.getData());
				//System.out.println("FROM SERVER:" + modifiedSentence);
				System.out.println("FROM SERVER:" + data2.toString());
				//System.out.println("Ahoj");
			}
		}
	}
	

}
class MyData {
	private	long timestamp; // number of sample
	private int[] image = new int[128];// line camera image
	private int servo; // -1000/1000
	private int[] pwm = new int[2]; // -1000/1000
	private int[] fb = new int[2]; // 0-1000
	private int[] dc = new int[2]; // distance 
	private int speed; // speed of car
	
	private static final Logger logger = LogManager.getLogger(Tfc.class);
	private List<ImageBuffer> imageBuffers = new ArrayList<>();
	
	public void setData(byte[] data) {
		ByteBuffer dataBuffer = ByteBuffer.wrap(data);
		dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
		long oldTimestamp = this.timestamp;
		this.timestamp = BinaryUtils.toUnsigned(dataBuffer.getInt());
		for (int i = 0; i < this.image.length; i++) {
			this.image[i] = BinaryUtils.toUnsigned(dataBuffer.getShort());
		}
		this.servo = dataBuffer.getShort();
		this.pwm[0]= dataBuffer.getShort();
		this.pwm[1]= dataBuffer.getShort();
		this.fb[0]= dataBuffer.getShort();
		this.fb[1]= dataBuffer.getShort();
		this.dc[0]= dataBuffer.getShort();
		this.dc[1]= dataBuffer.getShort();
		this.speed= dataBuffer.getShort();

		if (this.timestamp - oldTimestamp > 1) {
			logger.info("Lost frames " + (this.timestamp - oldTimestamp - 1));
		}
		imageBuffers.forEach(imageBuffer -> imageBuffer.addRow(this.image));
		//m_data_ready = 1;
	}
	
	@Override
	public String toString() {
		return "MyData [timestamp=" + timestamp + ", servo=" + servo + ", pwm1= " + pwm[0] + ", pwm2= " + pwm[1]
				+ ", fb1=" + fb[0]  + ", fb2=" + fb[1] + ", dc1=" + dc[0]  + ", dc2=" + dc[1] 
				+", Speed=" + speed + "image=" + Arrays.toString(image) + "]";
	}

}