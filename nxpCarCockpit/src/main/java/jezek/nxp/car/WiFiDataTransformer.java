package jezek.nxp.car;

public class WiFiDataTransformer implements DataTransformer{

	private DrivingRecord drivingRecord;

	public WiFiDataTransformer(DrivingRecord drivingRecord) {
		super();
		this.drivingRecord = drivingRecord;
	}

	@Override
	public void processData(byte[] data) {
		int packetCount = data.length / WifiMonitorData.STRUCT_LENGTH;
		for (int i = 0; i < packetCount; i++) {
			WifiMonitorData record = new WifiMonitorData(data,
					WifiMonitorData.STRUCT_LENGTH * i);
			drivingRecord.addData(record);
		}
	}

	@Override
	public boolean isControlToSend() {
		return false;
	}

	@Override
	public byte[] getControlToSend() {
		return null;
	}

	@Override
	public boolean isSettingToSend() {
		return false;
	}

	@Override
	public byte[] getSettingToSend() {
		return null;
	}

	@Override
	public boolean waitForTamplate() {
		return false;
	}

	@Override
	public byte[] getTempalte() {
		return null;
	}

	@Override
	public int dataCount() {
		return 1420;
	}

	@Override
	public boolean checkTail(byte[] data) {
		return true;
	}
}
