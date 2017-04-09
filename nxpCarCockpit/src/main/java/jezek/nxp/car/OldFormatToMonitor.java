package jezek.nxp.car;

public class OldFormatToMonitor implements DataTransformer{

	private DrivingRecord drivingRecord;
	private Tfc tfc = new Tfc();

	public OldFormatToMonitor(DrivingRecord drivingRecord) {
		super();
		this.drivingRecord = drivingRecord;
	}

	@Override
	public void processData(byte[] data) {
		tfc.processData(data);
		WifiMonitorData record = new WifiMonitorData();
		record.setMissing(false);
		record.setFb(new int[]{tfc.ReadFB_i(0), tfc.ReadFB_i(1)});
		record.setPwm(new int[]{0, 0});
		record.setServo(0);
		int[] img = tfc.getImage(0, Tfc.NUM_LINE_SCAN);
		for (int i = 0; i < img.length; i++) {
			img[i] = img[i]/16; 
		}
		record.setImage(img);
		drivingRecord.addData(record);
	}

	@Override
	public boolean isControlToSend() {
		return false;
	}

	@Override
	public byte[] getControlToSend() {
		return new byte[0];
	}

	@Override
	public boolean isSettingToSend() {
		return false;
	}

	@Override
	public byte[] getSettingToSend() {
		return new byte[0];
	}

	@Override
	public boolean waitForTamplate() {
		return true;
	}

	@Override
	public byte[] getTempalte() {
		return Tfc.DATA_HEADER;
	}

	@Override
	public int dataCount() {
		return 277;
	}

	@Override
	public boolean checkTail(byte[] data) {
		return data[276] != Tfc.ETX;
	}
}
