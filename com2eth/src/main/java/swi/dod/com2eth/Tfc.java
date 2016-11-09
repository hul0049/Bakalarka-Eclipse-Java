package swi.dod.com2eth;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author David Ježek
 *
 */
public class Tfc {

	private static final Logger logger = LogManager.getLogger(Tfc.class);
	
	public static final int NUM_LINE_SCAN = 128; // Number of camera pixels

	public static final int PWM_MINMAX = 1000; // -/+ range for PWM

	public static final int SERVO_MINMAX = 1000; // -/+ range for servo position

	public static final int ADC_MAXVAL = 0x0FFF; // Maximum value of analog
													// sample from ADC
	public static final int ANDATA_MINMAX = 1000; // -/+ range for analog values
													// (e.g. potentiometers)

	public static final int SERVO_DEFAULT_CENTER = 1500; // Center position of
															// servo - pulse in
															// microseconds
	public static final int SERVO_DEFAULT_MAX_LR = 200; // Default -/+ range of
														// pulse width range
	public static final int SERVO_MAX_LR = 400; // Maximum -/+ allowed range of
												// pulse width

	public static final int PWM_DEFAULT_MAX = 200; // Default value for maximal
													// -/+ PWM duty cycle
	public static final int PWM_MAX = 500; // Maximal allowed -/+ PWM duty cycle

	// commands
	public static final int CMD_DATA = 1;
	public static final int CMD_SETTING = 2;
	public static final int CMD_CONTROL = 3;

	// protocol start and stop byte
	public static final byte STX = 0x2;
	public static final byte ETX = 0x3;

	private List<ImageBuffer> imageBuffers = new ArrayList<>();
	private boolean sendSetting;
	// protocol (length=1+1+2+N+1)
	// bytes 1 2 1 N 1
	// type uint_8 uint16_6 uint8_t N*uint8_t uint8_t
	// purpose STX length CMD_xx struct ETX

	// analog data order
	enum andata_chnl_enum {
		anPOT_1(0), anPOT_2(1), anFB_A(2), anFB_B(3), anBAT(4);
		int index;

		private andata_chnl_enum(int index) {
			this.index = index;
		}

		public int index() {
			return index;
		}
	};

	public static final int HEADER_LENGTH = 1+2+1+1;
	class s_protocol_empty {
		byte stx;
		short length;
		byte cmd;
		byte etx;
	};
	/**
	 * @author David Ježek
	 *
	 */
	public static final int DATA_LENGTH = 4+2*andata_chnl_enum.values().length+1+1+2*NUM_LINE_SCAN + 4;
	class s_data {
		long timestamp; // number of sample
		short[] adc = new short[andata_chnl_enum.values().length]; // ADC
																	// channels
		byte dip_sw; // dip switches status
		byte push_sw; // push buttons
		int[] image = new int[NUM_LINE_SCAN];// line camera image
		int padding; //aligment and reserve
		@Override
		public String toString() {
			return "s_data [timestamp=" + timestamp + ", adc=" + Arrays.toString(adc) + ", dip_sw=" + dip_sw
					+ ", push_sw=" + push_sw + ", image=" + Arrays.toString(image) + "]";
		} 
	}

	public static final int SETTING_LENGTH_DATA = 2*2 + 2*2 + 2 + 2;
	public static final int SETTING_LENGTH = HEADER_LENGTH + SETTING_LENGTH_DATA;
	private byte[] setting = new byte[SETTING_LENGTH];
	private ByteBuffer settingBuffer = ByteBuffer.wrap(setting, 0, SETTING_LENGTH);

	class s_setting {
		short[] servo_center = new short[2]; // servos calibration
		short[] servo_max_lr = new short[2]; // servos range
		short pwm_max; // maximal PWM for motors
		short padding;
		public void write(ByteBuffer buf){
			buf.order(ByteOrder.LITTLE_ENDIAN);
			buf.put(SETTING_HEADER);
			buf.putShort(servo_center[0]);
			buf.putShort(servo_center[1]);
			buf.putShort(servo_max_lr[0]);
			buf.putShort(servo_max_lr[1]);
			buf.putShort(pwm_max);
			buf.putShort(padding);
			buf.put(ETX);
		}
	};

	public static final int CONTROL_LENGTH_DATA = 1+1+1+ 1+ 2*2+2*2;
	public static final int CONTROL_LENGTH = HEADER_LENGTH + CONTROL_LENGTH_DATA;
	private byte[] control = new byte[CONTROL_LENGTH];
	private ByteBuffer controlBuffer = ByteBuffer.wrap(control, 0, control.length);
	class s_control {
		byte leds; // four leds - low nibble
		byte pwm_onoff; // enable motors <0,1>
		byte servo_onoff; // enable servo <0,1>
		byte padding;
		short pwm_a, pwm_b; // PWM of motors <-PWM_MINMAX, PWM_MINMAX>
		short[] servo_pos = new short[2]; // servos position <-SERVO_MAX_LR,
											// SERVO_MAX_LR>
		public void write(ByteBuffer buf){
			buf.order(ByteOrder.LITTLE_ENDIAN);
			buf.put(CONTROL_HEADER);
			buf.put(leds);
			buf.put(pwm_onoff);
			buf.put(servo_onoff);
			buf.put(padding);
			buf.putShort(pwm_a);
			buf.putShort(pwm_b);
			buf.putShort(servo_pos[0]);
			buf.putShort(servo_pos[1]);
			buf.put(ETX);
		}
	};
	public static final byte[] DATA_HEADER = new byte[] { (byte) 0x02, (byte) 0x19, (byte) 0x01, (byte) CMD_DATA };
	public static final byte[] CONTROL_HEADER = new byte[] { STX, (byte) CONTROL_LENGTH & 0xFF, (byte) (CONTROL_LENGTH>>8) & 0xFF, (byte) CMD_CONTROL };
	public static final byte[] SETTING_HEADER = new byte[] { STX, (byte) SETTING_LENGTH & 0xFF, (byte) (SETTING_LENGTH>>8) & 0xFF, (byte) CMD_SETTING};

	protected s_data m_data = new s_data();
	protected s_setting m_setting = new s_setting();
	protected s_control m_control = new s_control();

	private int m_data_ready;

	/**
	 *
	 */

	public Tfc() {
		controlBuffer.order(ByteOrder.LITTLE_ENDIAN);
		settingBuffer.order(ByteOrder.LITTLE_ENDIAN);
		BinaryUtils.copyInto(CONTROL_HEADER, control, 0);
		BinaryUtils.copyInto(SETTING_HEADER, setting, 0);
		InitAll();
	}

	public void InitAll() {
		// bzero( &m_control, sizeof( m_control ) );
		// m_setting.servo_center[ 0 ] = SERVO_DEFAULT_CENTER;
		// m_setting.servo_center[ 1 ] = SERVO_DEFAULT_CENTER;
		// m_setting.servo_max_lr[ 0 ] = SERVO_DEFAULT_MAX_LR;
		// m_setting.servo_max_lr[ 1 ] = SERVO_DEFAULT_MAX_LR;
		// m_setting.pwm_max = PWM_DEFAULT_MAX;

	}

	/****************************************************************************
	 * Digital Input and Output LEDs & Switches
	 */

	// set single LED
	public void setLED(int led, boolean val) {
		if (val)
			m_control.leds |= 1 << led;
		else
			m_control.leds &= ~(1 << led);

	}

	// set all LEDs
	public void setLEDs(byte leds) {
		m_control.leds = leds;
	}

	// set LED level in range <0-4>
	public void setBatteryLEDLevel(int bat_level) {

	}

	public int getDIPSwitch() {
		return m_data.dip_sw;
	}

	public int getPushButton(int channel) {
		if(channel == 0){
			return m_data.push_sw & 0x01;
		} else {
			return m_data.push_sw & 0x02;
		}
	}

	/****************************************************************************
	 * Analog Values
	 */

	// return value in range <-ANDATA_MINMAX,ANDATA_MINMAX>
	// result have to accept clockwise rotation of potentiometers!
	public int ReadPot_i(int Channel) {
		// ... m_data
		int val = Channel == 0 ? m_data.adc[andata_chnl_enum.anPOT_1.index()]
				: m_data.adc[andata_chnl_enum.anPOT_2.index()];
		return -(val * 2 * ANDATA_MINMAX / ADC_MAXVAL - ANDATA_MINMAX);

	}

	// return value in range <-1.0, 1.0>
	public float ReadPot_f(int Channel) {
		// ... TFC_ReadPot_i
		return ((float) ReadPot_i(Channel)) / ANDATA_MINMAX;

	}

	// return value from ADC
	public int ReadFB_i(int Channel) {
		// ... m_data.adc
		return Channel == 0 ? m_data.adc[andata_chnl_enum.anFB_A.index()] : m_data.adc[andata_chnl_enum.anFB_B.index()];

	}

	// return current in Amperes
	public float ReadFB_f(int Channel) {
		// ...TFC_ReadFB_i
		int val = ReadFB_i(Channel);
		// Power Supply 3.3V
		// Current FB=16mA ~ Output=6A
		// Resistor 220Ohm
		return ((float) val) / ADC_MAXVAL * 3.3f / 220.0f / 0.016f * 6;

	}

	// return value from ADC
	public int ReadBatteryVoltage_i() {
		// ... m_data.adc
		return m_data.adc[andata_chnl_enum.anBAT.index()];

	}

	// return voltage in Volts
	public float ReadBatteryVoltage_f() {
		// ...TFC_ReadBatteryVoltage_i
		int val = ReadBatteryVoltage_i();
		return ((float) val) / ADC_MAXVAL * 3.3f * 5.7f; // *
															// ((47000.0+10000.0)/10000.0);
	}

	/****************************************************************************
	 * Camera
	 */

	public int ImageReady(int channel) {
		// ...
		return m_data_ready;
	}

	public int[] getImage(int channel, int length) {
		if (length > NUM_LINE_SCAN)
			length = NUM_LINE_SCAN;
		int[] img = Arrays.copyOf(m_data.image, length);
		m_data_ready = 0;
		return img;
	}

	/****************************************************************************
	 * Servo and Motors
	 */

	// servo calibration - center is pulse width microseconds,
	// max is left/right pulse width
	public void setServoCalibration(int channel, short center, short max_lr) {
		m_setting.servo_center[channel] = center;
		m_setting.servo_max_lr[channel] = max_lr;

	}

	// set maximal PWM duty cycle
	public void setPWMMax(short max) {
		m_setting.pwm_max = max;

	}

	// enable/disable servo
	public void ServoOnOff(byte onoff) {
		m_control.servo_onoff = onoff;

	}

	// set servo position in range <-SERVO_MINMAX, SERVO_MINMAX>
	public void setServo_i(int channel, short position) {
		// ... m_control
		m_control.servo_pos[channel] = position;

	}

	// set servo position in range <-1.0, 1.0>
	public void setServo_f(int channel, float position) {
		// ... TFC_Servo_i
		setServo_i(channel, (short) (position * SERVO_MINMAX));
	}

	// enable/disable motors
	public void MotorPWMOnOff(byte onoff) {
		// ...m_control
		m_control.pwm_onoff = onoff;
	}

	// set PWM for both motors in range <-PWM_MINMAX, PWM_MINMAX>
	public void setMotorPWM_i(short pwm_a, short pwm_b) {
		// ... m_control
		m_control.pwm_a = pwm_a;
		m_control.pwm_b = pwm_b;

	}

	// set PWM for both motors in range <-1.0, 1.0>
	public void setMotorPWM_f(float pwm_a, float pwm_b) {
		// ...TFC_setMotorPWM_i
		setMotorPWM_i((short) (pwm_a * PWM_MINMAX), (short) (pwm_b * PWM_MINMAX));

	}

	public void setData(byte[] data) {
		ByteBuffer dataBuffer = ByteBuffer.wrap(data);
		dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
		long oldTimestamp =  m_data.timestamp;
		m_data.timestamp = BinaryUtils.toUnsigned(dataBuffer.getInt());
		m_data.adc[andata_chnl_enum.anPOT_1.index()] = dataBuffer.getShort();
		m_data.adc[andata_chnl_enum.anPOT_2.index()] = dataBuffer.getShort();
		m_data.adc[andata_chnl_enum.anFB_A.index()] = dataBuffer.getShort();
		m_data.adc[andata_chnl_enum.anFB_B.index()] = dataBuffer.getShort();
		m_data.adc[andata_chnl_enum.anBAT.index()] = dataBuffer.getShort();
		m_data.dip_sw = dataBuffer.get();
		m_data.push_sw = dataBuffer.get();
		for(int i=0; i<m_data.image.length; i++){
			m_data.image[i] = BinaryUtils.toUnsigned(dataBuffer.getShort());
		}
		if(m_data.timestamp - oldTimestamp > 1){
			logger.info("Lost frames " + (m_data.timestamp - oldTimestamp -1));
		}
		imageBuffers.forEach(imageBuffer -> imageBuffer.addRow(m_data.image));
		m_data_ready = 1;
	}

	public byte[] getSetting() {
		settingBuffer.position(0);
		m_setting.write(settingBuffer);
		sendSetting = false;
		return setting;
	}

	public byte[] getControl() {
		controlBuffer.position(0);
		m_control.write(controlBuffer);
		return control;
	}

	private static byte[][] sourceData = new byte[][]{
    	{70, -49, 2, 0, -7, 7, -76, 7, 1, 0, 1, 0, 18, 1, 0, 0, 124, 0, 123, 0, -128, 0, -121, 0, -127, 0, -121, 0, -126, 0, -120, 0, -125, 0, -121, 0, -124, 0, -118, 0, -124, 0, -120, 0, -123, 0, -118, 0, -122, 0, -119, 0, -122, 0, -118, 0, -123, 0, -118, 0, -124, 0, -118, 0, -122, 0, -117, 0, -122, 0, -121, 0, -124, 0, -121, 0, -123, 0, -119, 0, -124, 0, -119, 0, -123, 0, -118, 0, -122, 0, -120, 0, -123, 0, -119, 0, -121, 0, -117, 0, -122, 0, -119, 0, -124, 0, -118, 0, -121, 0, -118, 0, -122, 0, -118, 0, -122, 0, -117, 0, -121, 0, -118, 0, -120, 0, -116, 0, -118, 0, -114, 0, -118, 0, -114, 0, -118, 0, -112, 0, -116, 0, -111, 0, -114, 0, -110, 0, -113, 0, -108, 0, -114, 0, -109, 0, -111, 0, -108, 0, -111, 0, -109, 0, -111, 0, -105, 0, -110, 0, -106, 0, -111, 0, -105, 0, -108, 0, -105, 0, -110, 0, -107, 0, -110, 0, -106, 0, -111, 0, -109, 0, -111, 0, -108, 0, -114, 0, -111, 0, -114, 0, -109, 0, -115, 0, -110, 0, -115, 0, -111, 0, -116, 0, -113, 0, -118, 0, -114, 0, -119, 0, -115, 0, -119, 0, -115, 0, -119, 0, -117, 0, -119, 0, -117, 0, -120, 0, -118, 0, -120, 0, -117, 0, -121, 0, -115, 0, -122, 0, -118, 0, -122, 0, -116, 0, -121, 0, -118, 0, -123, 0, -118, 0, -122, 0, -119, 0, -123, 0, -118, 0, -7, 11, 0, 0, 3},
		{-35, -49, 2, 0, -7, 7, -76, 7, 1, 0, 1, 0, 18, 1, 0, 0, 120, 0, 120, 0, 127, 0, -121, 0, -126, 0, -121, 0, -128, 0, -122, 0, -126, 0, -122, 0, -126, 0, -121, 0, -124, 0, -120, 0, -122, 0, -119, 0, -125, 0, -121, 0, -122, 0, -119, 0, -124, 0, -120, 0, -124, 0, -121, 0, -123, 0, -119, 0, -124, 0, -118, 0, -124, 0, -119, 0, -124, 0, -119, 0, -124, 0, -119, 0, -124, 0, -117, 0, -123, 0, -119, 0, -124, 0, -120, 0, -122, 0, -119, 0, -123, 0, -119, 0, -122, 0, -118, 0, -122, 0, -118, 0, -123, 0, -120, 0, -123, 0, -116, 0, -123, 0, -118, 0, -119, 0, -115, 0, -119, 0, -118, 0, -119, 0, -115, 0, -117, 0, -113, 0, -117, 0, -112, 0, -118, 0, -111, 0, -116, 0, -111, 0, -115, 0, -110, 0, -113, 0, -111, 0, -112, 0, -110, 0, -113, 0, -107, 0, -114, 0, -110, 0, -114, 0, -108, 0, -112, 0, -106, 0, -113, 0, -109, 0, -113, 0, -109, 0, -114, 0, -109, 0, -114, 0, -112, 0, -116, 0, -108, 0, -119, 0, -111, 0, -115, 0, -112, 0, -118, 0, -113, 0, -118, 0, -114, 0, -118, 0, -116, 0, -119, 0, -117, 0, -121, 0, -116, 0, -122, 0, -117, 0, -120, 0, -116, 0, -123, 0, -115, 0, -121, 0, -117, 0, -120, 0, -118, 0, -121, 0, -118, 0, -124, 0, -117, 0, -121, 0, -119, 0, -122, 0, -119, 0, -122, 0, -119, 0, -122, 0, -118, 0, -7, 11, 0, 0, 3},
		{-34, -49, 2, 0, -8, 7, -76, 7, 1, 0, 1, 0, 18, 1, 0, 0, 123, 0, 124, 0, -127, 0, -122, 0, -126, 0, -122, 0, -127, 0, -122, 0, -123, 0, -119, 0, -125, 0, -120, 0, -125, 0, -119, 0, -123, 0, -119, 0, -124, 0, -121, 0, -125, 0, -119, 0, -121, 0, -117, 0, -122, 0, -117, 0, -123, 0, -120, 0, -124, 0, -121, 0, -125, 0, -119, 0, -121, 0, -126, 0, -124, 0, -121, 0, -122, 0, -117, 0, -124, 0, -119, 0, -123, 0, -118, 0, -121, 0, -117, 0, -123, 0, -117, 0, -123, 0, -119, 0, -122, 0, -118, 0, -122, 0, -120, 0, -120, 0, -121, 0, -121, 0, -116, 0, -119, 0, -115, 0, -119, 0, -112, 0, -119, 0, -112, 0, -117, 0, -112, 0, -116, 0, -112, 0, -114, 0, -110, 0, -112, 0, -109, 0, -114, 0, -107, 0, -113, 0, -109, 0, -111, 0, -107, 0, -112, 0, -107, 0, -110, 0, -107, 0, -111, 0, -106, 0, -110, 0, -106, 0, -109, 0, -107, 0, -110, 0, -108, 0, -113, 0, -107, 0, -112, 0, -108, 0, -114, 0, -109, 0, -115, 0, -110, 0, -114, 0, -111, 0, -114, 0, -112, 0, -114, 0, -112, 0, -116, 0, -113, 0, -119, 0, -115, 0, -119, 0, -116, 0, -119, 0, -116, 0, -122, 0, -116, 0, -119, 0, -117, 0, -117, 0, -115, 0, -121, 0, -119, 0, -121, 0, -117, 0, -121, 0, -116, 0, -122, 0, -118, 0, -121, 0, -119, 0, -121, 0, -121, 0, -121, 0, -116, 0, -7, 11, 0, 0, 3},
		{-33, -49, 2, 0, -7, 7, -77, 7, 1, 0, 1, 0, 19, 1, 0, 0, 123, 0, 124, 0, 126, 0, -123, 0, -127, 0, -123, 0, -127, 0, -121, 0, -127, 0, -121, 0, -125, 0, -120, 0, -125, 0, -120, 0, -125, 0, -117, 0, -126, 0, -119, 0, -125, 0, -119, 0, -125, 0, -118, 0, -124, 0, -120, 0, -124, 0, -121, 0, -123, 0, -120, 0, -123, 0, -120, 0, -124, 0, -118, 0, -124, 0, -120, 0, -124, 0, -118, 0, -124, 0, -120, 0, -123, 0, -120, 0, -123, 0, -119, 0, -123, 0, -119, 0, -124, 0, -121, 0, -122, 0, -120, 0, -122, 0, -119, 0, -124, 0, -119, 0, -123, 0, -119, 0, -123, 0, -116, 0, -120, 0, -115, 0, -120, 0, -118, 0, -118, 0, -109, 0, -117, 0, -111, 0, -115, 0, -112, 0, -116, 0, -109, 0, -114, 0, -111, 0, -115, 0, -108, 0, -114, 0, -109, 0, -114, 0, -110, 0, -112, 0, -109, 0, -113, 0, -109, 0, -113, 0, -109, 0, -114, 0, -109, 0, -113, 0, -110, 0, -114, 0, -110, 0, -114, 0, -112, 0, -115, 0, -111, 0, -116, 0, -112, 0, -116, 0, -111, 0, -118, 0, -114, 0, -116, 0, -114, 0, -119, 0, -115, 0, -120, 0, -117, 0, -120, 0, -116, 0, -122, 0, -116, 0, -121, 0, -118, 0, -119, 0, -118, 0, -123, 0, -118, 0, -122, 0, -120, 0, -122, 0, -118, 0, -124, 0, -117, 0, -121, 0, -120, 0, -121, 0, -120, 0, -122, 0, -119, 0, -122, 0, -121, 0, -7, 11, 0, 0, 3},
		{-32, -49, 2, 0, -8, 7, -76, 7, 1, 0, 1, 0, 19, 1, 0, 0, 125, 0, 124, 0, -128, 0, -122, 0, -124, 0, -119, 0, -126, 0, -121, 0, -126, 0, -119, 0, -124, 0, -121, 0, -126, 0, -120, 0, -125, 0, -117, 0, -126, 0, -120, 0, -122, 0, -118, 0, -122, 0, -118, 0, -123, 0, -118, 0, -124, 0, -119, 0, -124, 0, -118, 0, -122, 0, -119, 0, -122, 0, -119, 0, -122, 0, -118, 0, -121, 0, -117, 0, -119, 0, -121, 0, -126, 0, -117, 0, -123, 0, -118, 0, -123, 0, -116, 0, -124, 0, -118, 0, -122, 0, -116, 0, -122, 0, -119, 0, -123, 0, -117, 0, -121, 0, -118, 0, -119, 0, -116, 0, -118, 0, -115, 0, -118, 0, -113, 0, -118, 0, -111, 0, -115, 0, -110, 0, -115, 0, -110, 0, -114, 0, -109, 0, -119, 0, -107, 0, -111, 0, -106, 0, -112, 0, -107, 0, -111, 0, -106, 0, -110, 0, -106, 0, -111, 0, -107, 0, -112, 0, -105, 0, -110, 0, -104, 0, -110, 0, -107, 0, -111, 0, -107, 0, -112, 0, -107, 0, -111, 0, -109, 0, -115, 0, -114, 0, -117, 0, -108, 0, -116, 0, -114, 0, -117, 0, -113, 0, -117, 0, -115, 0, -117, 0, -115, 0, -119, 0, -115, 0, -121, 0, -117, 0, -121, 0, -115, 0, -119, 0, -116, 0, -121, 0, -116, 0, -120, 0, -112, 0, -121, 0, -118, 0, -123, 0, -116, 0, -121, 0, -115, 0, -122, 0, -117, 0, -122, 0, -115, 0, -122, 0, -119, 0, -7, 11, 0, 0, 3},
		{-31, -49, 2, 0, -7, 7, -76, 7, 1, 0, 1, 0, 19, 1, 0, 0, 122, 0, 122, 0, 125, 0, -123, 0, -127, 0, -122, 0, -128, 0, -123, 0, -124, 0, -121, 0, -125, 0, -119, 0, -126, 0, -122, 0, -127, 0, -118, 0, -122, 0, -119, 0, -122, 0, -118, 0, -124, 0, -119, 0, -125, 0, -119, 0, -124, 0, -120, 0, -123, 0, -119, 0, -125, 0, -118, 0, -123, 0, -118, 0, -123, 0, -119, 0, -123, 0, -117, 0, -124, 0, -120, 0, -124, 0, -120, 0, -124, 0, -117, 0, -123, 0, -118, 0, -122, 0, -118, 0, -121, 0, -121, 0, -121, 0, -119, 0, -122, 0, -116, 0, -121, 0, -117, 0, -121, 0, -117, 0, -120, 0, -114, 0, -118, 0, -114, 0, -119, 0, -114, 0, -118, 0, -114, 0, -118, 0, -113, 0, -115, 0, -111, 0, -115, 0, -108, 0, -114, 0, -110, 0, -112, 0, -107, 0, -113, 0, -107, 0, -111, 0, -109, 0, -112, 0, -109, 0, -111, 0, -107, 0, -113, 0, -109, 0, -113, 0, -109, 0, -112, 0, -110, 0, -115, 0, -112, 0, -114, 0, -111, 0, -115, 0, -110, 0, -119, 0, -113, 0, -118, 0, -112, 0, -118, 0, -114, 0, -118, 0, -114, 0, -118, 0, -115, 0, -118, 0, -115, 0, -121, 0, -117, 0, -120, 0, -118, 0, -121, 0, -114, 0, -122, 0, -116, 0, -122, 0, -119, 0, -121, 0, -119, 0, -122, 0, -118, 0, -121, 0, -119, 0, -121, 0, -120, 0, -122, 0, -119, 0, -122, 0, -120, 0, -7, 11, 0, 0, 3},
		{-30, -49, 2, 0, -7, 7, -76, 7, 1, 0, 1, 0, 19, 1, 0, 0, 123, 0, 123, 0, 127, 0, -121, 0, -127, 0, -121, 0, -127, 0, -121, 0, -125, 0, -119, 0, -124, 0, -119, 0, -124, 0, -120, 0, -124, 0, -118, 0, -126, 0, -119, 0, -121, 0, -116, 0, -123, 0, -117, 0, -127, 0, -117, 0, -122, 0, -117, 0, -124, 0, -119, 0, -123, 0, -119, 0, -124, 0, -115, 0, -122, 0, -119, 0, -125, 0, -116, 0, -122, 0, -116, 0, -120, 0, -120, 0, -123, 0, -119, 0, -125, 0, -118, 0, -122, 0, -121, 0, -124, 0, -117, 0, -122, 0, -118, 0, -123, 0, -116, 0, -121, 0, -124, 0, -119, 0, -114, 0, -117, 0, -115, 0, -119, 0, -115, 0, -118, 0, -111, 0, -117, 0, -112, 0, -115, 0, -111, 0, -114, 0, -109, 0, -112, 0, -107, 0, -112, 0, -107, 0, -110, 0, -111, 0, -111, 0, -108, 0, -111, 0, -108, 0, -112, 0, -108, 0, -110, 0, -106, 0, -112, 0, -106, 0, -110, 0, -107, 0, -114, 0, -110, 0, -113, 0, -107, 0, -112, 0, -109, 0, -115, 0, -110, 0, -114, 0, -110, 0, -115, 0, -112, 0, -117, 0, -111, 0, -116, 0, -114, 0, -119, 0, -115, 0, -118, 0, -115, 0, -121, 0, -116, 0, -119, 0, -118, 0, -121, 0, -117, 0, -120, 0, -116, 0, -121, 0, -117, 0, -119, 0, -118, 0, -119, 0, -118, 0, -121, 0, -117, 0, -120, 0, -113, 0, -121, 0, -117, 0, -124, 0, -119, 0, -7, 11, 0, 0, 3},
		{-29, -49, 2, 0, -8, 7, -76, 7, 1, 0, 1, 0, 19, 1, 0, 0, 123, 0, 123, 0, -128, 0, -123, 0, -126, 0, -123, 0, -125, 0, -121, 0, -124, 0, -119, 0, -123, 0, -118, 0, -125, 0, -121, 0, -122, 0, -120, 0, -125, 0, -120, 0, -123, 0, -118, 0, -121, 0, -121, 0, -123, 0, -119, 0, -123, 0, -119, 0, -123, 0, -112, 0, -124, 0, -119, 0, -123, 0, -120, 0, -123, 0, -119, 0, -123, 0, -119, 0, -122, 0, -120, 0, -120, 0, -120, 0, -123, 0, -117, 0, -122, 0, -117, 0, -122, 0, -124, 0, -123, 0, -120, 0, -125, 0, -119, 0, -123, 0, -117, 0, -121, 0, -116, 0, -122, 0, -119, 0, -121, 0, -115, 0, -118, 0, -114, 0, -118, 0, -113, 0, -114, 0, -113, 0, -117, 0, -111, 0, -114, 0, -109, 0, -113, 0, -109, 0, -113, 0, -110, 0, -112, 0, -108, 0, -112, 0, -109, 0, -112, 0, -108, 0, -112, 0, -109, 0, -112, 0, -107, 0, -113, 0, -105, 0, -112, 0, -110, 0, -111, 0, -109, 0, -115, 0, -116, 0, -113, 0, -112, 0, -113, 0, -112, 0, -116, 0, -112, 0, -117, 0, -113, 0, -118, 0, -111, 0, -117, 0, -116, 0, -118, 0, -113, 0, -119, 0, -114, 0, -121, 0, -117, 0, -120, 0, -116, 0, -118, 0, -117, 0, -119, 0, -117, 0, -122, 0, -117, 0, -121, 0, -118, 0, -121, 0, -116, 0, -121, 0, -117, 0, -119, 0, -118, 0, -122, 0, -116, 0, -122, 0, -118, 0, -7, 11, 0, 0, 3},
		{-28, -49, 2, 0, -7, 7, -76, 7, 1, 0, 1, 0, 19, 1, 0, 0, 116, 0, 123, 0, -128, 0, -120, 0, -125, 0, -121, 0, -125, 0, -122, 0, -126, 0, -119, 0, -124, 0, -119, 0, -125, 0, -119, 0, -123, 0, -118, 0, -124, 0, -119, 0, -122, 0, -119, 0, 127, 0, -118, 0, -123, 0, -124, 0, -122, 0, -119, 0, -123, 0, -119, 0, -122, 0, -117, 0, -122, 0, -119, 0, -121, 0, -120, 0, -122, 0, -119, 0, -122, 0, -119, 0, -123, 0, -119, 0, -123, 0, -118, 0, -123, 0, -118, 0, -122, 0, -117, 0, -121, 0, -117, 0, -123, 0, -118, 0, -122, 0, -116, 0, -121, 0, -116, 0, -121, 0, -115, 0, -118, 0, -112, 0, -120, 0, -114, 0, -117, 0, -111, 0, -116, 0, -114, 0, -114, 0, -110, 0, -114, 0, -110, 0, -113, 0, -109, 0, -112, 0, -108, 0, -110, 0, -108, 0, -113, 0, -108, 0, -112, 0, -109, 0, -111, 0, -106, 0, -111, 0, -110, 0, -112, 0, -107, 0, -111, 0, -105, 0, -114, 0, -104, 0, -112, 0, -108, 0, -112, 0, -108, 0, -113, 0, -118, 0, -116, 0, -110, 0, -117, 0, -112, 0, -116, 0, -117, 0, -117, 0, -114, 0, -116, 0, -115, 0, -119, 0, -115, 0, -119, 0, -115, 0, -119, 0, -116, 0, -119, 0, -115, 0, -122, 0, -115, 0, -121, 0, -115, 0, -120, 0, -118, 0, -122, 0, -117, 0, -119, 0, -117, 0, -121, 0, -113, 0, -122, 0, -118, 0, -121, 0, -117, 0, -7, 11, 0, 0, 3},
		{-27, -49, 2, 0, -7, 7, -76, 7, 1, 0, 1, 0, 19, 1, 0, 0, 122, 0, 123, 0, 126, 0, -122, 0, -127, 0, -121, 0, -125, 0, -123, 0, -125, 0, -119, 0, -125, 0, -119, 0, -125, 0, -122, 0, -124, 0, -120, 0, -125, 0, -120, 0, -124, 0, -120, 0, -123, 0, -120, 0, -125, 0, -120, 0, -126, 0, -119, 0, -123, 0, -122, 0, -123, 0, -122, 0, -125, 0, -119, 0, -124, 0, -119, 0, -123, 0, -119, 0, -122, 0, -119, 0, -125, 0, -119, 0, -124, 0, -118, 0, -126, 0, -124, 0, -122, 0, -118, 0, -124, 0, -118, 0, -124, 0, -121, 0, -122, 0, -117, 0, -123, 0, -119, 0, -122, 0, -116, 0, -120, 0, -114, 0, -118, 0, -115, 0, -117, 0, -115, 0, -118, 0, -113, 0, -117, 0, -111, 0, -114, 0, -112, 0, -114, 0, -114, 0, -112, 0, -109, 0, -111, 0, -107, 0, -110, 0, -107, 0, -112, 0, -109, 0, -113, 0, -109, 0, -111, 0, -108, 0, -112, 0, -108, 0, -113, 0, -108, 0, -112, 0, -110, 0, -116, 0, -109, 0, -115, 0, -112, 0, -116, 0, -110, 0, -117, 0, -113, 0, -115, 0, -114, 0, -119, 0, -115, 0, -118, 0, -115, 0, -120, 0, -115, 0, -119, 0, -117, 0, -122, 0, -118, 0, -120, 0, -113, 0, -119, 0, -117, 0, -121, 0, -118, 0, -122, 0, -122, 0, -122, 0, -118, 0, -121, 0, -120, 0, -123, 0, -117, 0, -125, 0, -119, 0, -122, 0, -119, 0, -121, 0, -119, 0, -7, 11, 0, 0, 3}

    };

    public static void main(String[] args) {
		for (int i = 0; i < sourceData.length; i++) {
			Tfc tfc = new Tfc();
			tfc.setData(sourceData[i]);
			System.out.println(tfc.m_data.toString());
		}
	}

	public void addImageBuffer(ImageBuffer e) {
		imageBuffers.add(e);
	}

	public void removeImageBuffer(ImageBuffer o) {
		imageBuffers.remove(o);
	}

	public boolean isSendSetting() {
		return sendSetting;
	}

	public void sendSetting() {
		this.sendSetting = true;
	}
}
