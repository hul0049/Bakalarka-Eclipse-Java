package jezek.nxp.car;

public class ResponseData {

	private int servo;
	private int[] pwm;
	
	public ResponseData() {
		super();
		pwm = new int[2];
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
	
}
