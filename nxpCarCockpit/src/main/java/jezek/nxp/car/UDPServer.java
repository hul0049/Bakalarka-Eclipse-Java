package jezek.nxp.car;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UDPServer extends CommunicationWrapper implements Runnable {
	
	private static final Logger logger = LogManager.getLogger(UDPServer.class);
	
	private boolean end = false;
	private Random r = new Random();
	private long timestamp = 0;
	private boolean fakeMode = false;
	private WifiMonitorData oldFakeData;
	private WifiMonitorData directions;
	private int port;
	private DatagramSocket serverSocket;

	public UDPServer(int port, DataTransformer dataTransformer) {
		super(dataTransformer);
		this.port = port;
		initFakeData();
	}

	private void initFakeData() {
		oldFakeData = new WifiMonitorData(0);
		oldFakeData.setFb(new int[] { 0, 0 });
		oldFakeData.setPwm(new int[] { 0, 0 });
		int[] d = new int[128];
		for (int j = 0; j < 128; j++) {
			d[j] = r.nextInt(255);
		}
		oldFakeData.setImage(d);
		oldFakeData.setMissing(false);
		oldFakeData.setServo(0);

		directions = new WifiMonitorData(0);
		directions.setFb(new int[] { 1, -1 });
		directions.setPwm(new int[] { -1, 1 });
		d = new int[128];
		int direction = 1;
		int prevValue = 0;
		for (int j = 0; j < 128; j++) {
			d[j] = getNextValue(prevValue, direction, 10, 0, 255);
			direction = d[j] > prevValue ? 1 : -1;
			prevValue = d[j];
		}
		directions.setImage(d);
		directions.setMissing(false);
		directions.setServo(1);
	}

	@Override
	public void startRead() {
		end = false;
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		if (fakeMode) {
			byte[] buffer = new byte[142];
			while (!end) {
				WifiMonitorData data = fakeData();
				data.writeToArray(buffer);
				dataTransformer.processData(buffer);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		} else {
			try {
				connect();
				byte[] receiveData = new byte[dataTransformer.dataCount()];
				while (!end) {
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					serverSocket.receive(receivePacket);
					dataTransformer.processData(receiveData);
					if(record){
						recordStream.write(dataTransformer.getTempalte());
						recordStream.write(receiveData);
					}

				}
			} catch (IOException e) {
				logger.error("Packet receive error:", e);
			}
		}
	}

	public void stop() {
		end = true;
	}

	private WifiMonitorData fakeData() {
		if (r.nextInt(100) > 98) {
			timestamp += r.nextInt(20);
		}
		WifiMonitorData data = new WifiMonitorData(timestamp++);
		data.setFb(new int[] { getNextValue(oldFakeData.getFb()[0], directions.getFb()[0], 2, -1000, 1000),
				getNextValue(oldFakeData.getFb()[1], directions.getFb()[1], 1, -1000, 1000) });
		directions.getFb()[0] = data.getFb()[0] > oldFakeData.getFb()[0] ? 1 : -1;
		directions.getFb()[1] = data.getFb()[1] > oldFakeData.getFb()[1] ? 1 : -1;
		oldFakeData.getFb()[0] = data.getFb()[0];
		oldFakeData.getFb()[1] = data.getFb()[1];
		data.setPwm(new int[] { getNextValue(oldFakeData.getPwm()[0], directions.getPwm()[0], 2, -1000, 1000),
				getNextValue(oldFakeData.getPwm()[1], directions.getPwm()[1], 1, -1000, 1000) });
		directions.getPwm()[0] = data.getPwm()[0] >= oldFakeData.getPwm()[0] ? 1 : -1;
		directions.getPwm()[1] = data.getPwm()[1] >= oldFakeData.getPwm()[1] ? 1 : -1;
		oldFakeData.getPwm()[0] = data.getPwm()[0];
		oldFakeData.getPwm()[1] = data.getPwm()[1];
		int[] d = new int[128];
		int direction = directions.getImage()[0];
		int prevValue = oldFakeData.getImage()[0];
		for (int j = 0; j < 128; j++) {
			d[j] = getNextValue((prevValue + oldFakeData.getImage()[j]) / 2, direction, 10, 0, 255);
			direction = d[j] > (prevValue + oldFakeData.getImage()[j]) / 2 ? 1 : -1;
			prevValue = d[j];
		}
		for (int j = 0; j < 128; j++) {
			directions.getImage()[j] = d[j] > oldFakeData.getImage()[j] ? 1 : -1;
			oldFakeData.getImage()[j] = d[j];
		}
		data.setImage(d);
		data.setMissing(false);
		data.setServo(getNextValue(oldFakeData.getServo(), directions.getServo(), 2, -1000, 1000));
		directions.setServo(data.getServo() > oldFakeData.getServo() ? 1 : -1);
		oldFakeData.setServo(data.getServo());
		return data;
	}

	private int getNextValue(int prevValue, int prevDirection, int amount, int min, int max) {
		if (change(3)) {
			prevDirection -= prevDirection;
		}
		int value = prevValue + prevDirection * r.nextInt((max - min) / 100 * amount);
		if (value > max || value < min) {
			value = prevValue - prevDirection * r.nextInt((max - min) / 100 * amount);
		}
		return value;
	}

	private boolean change(int chance) {
		return r.nextInt(100) < chance;
	}

	private void test() {
		WifiMonitorData data = fakeData();
		data.setTimestamp(r.nextInt(Integer.MAX_VALUE));
		byte[] buf = data.writeToArray(new byte[WifiMonitorData.STRUCT_LENGTH]);
		WifiMonitorData data2 = new WifiMonitorData(buf);
		System.out.println("Data1:" + data);
		System.out.println("Data2:" + data2);
		System.out.println("equality:" + data.equals(data2));

	}

	public boolean isFakeMode() {
		return fakeMode;
	}

	public void setFakeMode(boolean fakeMode) {
		this.fakeMode = fakeMode;
	}

	public static void main(String[] args) {
		new UDPServer(0, null).test();
	}

	@Override
	public void connect() throws IOException {
		serverSocket = new DatagramSocket(port);
	}

	@Override
	public void close() throws IOException {
		end = true;
		if (serverSocket != null) {
			serverSocket.close();
		}

	}

	@Override
	public boolean isOpened() {
		if (serverSocket == null) {
			return false;
		}
		return !serverSocket.isClosed();
	}

	@Override
	protected int read(byte[] buffer, int offset, int size, int timeout) throws IOException {
		return 0;
	}

	@Override
	protected byte readByte(int timeout) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void sendData(byte[] data) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	protected int availableBytes() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void sendData(byte data) throws IOException {
		throw new UnsupportedOperationException();
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
