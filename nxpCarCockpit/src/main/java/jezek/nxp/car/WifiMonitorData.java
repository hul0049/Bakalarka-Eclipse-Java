package jezek.nxp.car;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

import jezek.nxp.car.Tfc.andata_chnl_enum;

public class WifiMonitorData {
	public static final int STRUCT_LENGTH = 142;
	public static final int NUM_LINE_SCAN = 128; // Number of camera pixels
	private boolean missing;
	private long timestamp; // number of sample
	private int[] image; // line camera image
	private int servo; // -1000/1000
	private int[] pwm; // -1000/1000
	private int[] fb; // 0-1000
	private int[] dc; // distance 
	private int speed; // speed of car
	private boolean selected;
	private ResponseData responseData;
		
	public WifiMonitorData() {
		super();
	}

	public WifiMonitorData(long timestamp) {
		this.timestamp = timestamp;
		missing = true;
	}

	public WifiMonitorData(byte[] data) {
		this(data, 0);
	}

	public WifiMonitorData(byte[] data, int offset) {
		image = new int[NUM_LINE_SCAN];
		pwm = new int[2];
		fb = new int[2];
		dc = new int[2];
		missing = false;
		//ByteBuffer dataBuffer = ByteBuffer.wrap(data, offset, STRUCT_LENGTH);
		ByteBuffer dataBuffer = ByteBuffer.wrap(data);
		dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
		timestamp = BinaryUtils.toUnsigned(dataBuffer.getInt());
		for (int i = 0; i < image.length; i++) {
			this.image[i] = BinaryUtils.toUnsigned(dataBuffer.getShort())/16;
		}
		servo = dataBuffer.getShort();
		pwm[0] = dataBuffer.getShort();
		pwm[1] = dataBuffer.getShort();
		fb[0] = dataBuffer.getShort();
		fb[1] = dataBuffer.getShort();
		this.dc[0] = dataBuffer.getShort();
		this.dc[1] = dataBuffer.getShort();
		this.speed = dataBuffer.getShort();

	}

	public byte[] writeToArray(byte[] buf) {
		ByteBuffer settingBuffer = ByteBuffer.wrap(buf, 0, buf.length);
		settingBuffer.order(ByteOrder.LITTLE_ENDIAN);
		settingBuffer.putInt(BinaryUtils.toInt(BinaryUtils.toBytes(timestamp), 4));
		for (int i = 0; i < image.length; i++) {
			settingBuffer.put(BinaryUtils.toBytes(image[i]), 3, 1);
		}
		settingBuffer.putShort(BinaryUtils.toShort(BinaryUtils.toBytes(servo), 2));
		settingBuffer.putShort(BinaryUtils.toShort(BinaryUtils.toBytes(pwm[0]), 2));
		settingBuffer.putShort(BinaryUtils.toShort(BinaryUtils.toBytes(pwm[1]), 2));
		settingBuffer.putShort(BinaryUtils.toShort(BinaryUtils.toBytes(fb[0]), 2));
		settingBuffer.putShort(BinaryUtils.toShort(BinaryUtils.toBytes(fb[1]), 2));
		if(dc == null)
		{dc = new int[2];
		}
		settingBuffer.putShort(BinaryUtils.toShort(BinaryUtils.toBytes(dc[0]), 2));
		settingBuffer.putShort(BinaryUtils.toShort(BinaryUtils.toBytes(dc[1]), 2));
		settingBuffer.putShort(BinaryUtils.toShort(BinaryUtils.toBytes(speed), 2));
		//settingBuffer.putShort(BinaryUtils.toShort(BinaryUtils.toBytes(leftIndex), 2));
		//settingBuffer.putShort(BinaryUtils.toShort(BinaryUtils.toBytes(rightIndex), 2));
		return buf;
	}

	public boolean isMissing() {
		return missing;
	}

	public void setMissing(boolean missing) {
		this.missing = missing;
	}

	@XmlElementWrapper(name = "camerraImage")
	@XmlElement(name = "pixel")
	public int[] getImage() {
		return image;
	}

	public void setImage(int[] image) {
		this.image = image;
	}

	public int getServo() {
		return servo;
	}

	public void setServo(int servo) {
		this.servo = servo;
	}

	public int[] getPwm() {
		return pwm;
	}

	public void setPwm(int[] pwm) {
		this.pwm = pwm;
	}

	public int[] getFb() {
		return fb;
	}

	public void setFb(int[] fb) {
		this.fb = fb;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(fb);
		result = prime * result + Arrays.hashCode(image);
		result = prime * result + (missing ? 1231 : 1237);
		result = prime * result + Arrays.hashCode(pwm);
		result = prime * result + servo;
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WifiMonitorData other = (WifiMonitorData) obj;
		if (!Arrays.equals(fb, other.fb))
			return false;
		if (!Arrays.equals(image, other.image))
			return false;
		if (missing != other.missing)
			return false;
		if (!Arrays.equals(pwm, other.pwm))
			return false;
		if (servo != other.servo)
			return false;
		if (dc != other.dc)
			return false;
		if (speed != other.speed)
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WifiMonitorData [missing=" + missing + ", timestamp=" + timestamp + ", image=" + Arrays.toString(image)
				+ ", servo=" + servo + ", pwm=" + Arrays.toString(pwm) + ", fb=" + Arrays.toString(fb) +
				"dc = " + Arrays.toString(dc) + "speed = " + speed;
	}

	@XmlTransient
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public ResponseData getResponseData() {
		return responseData;
	}

	public void setResponseData(ResponseData responseData) {
		this.responseData = responseData;
	}

}
