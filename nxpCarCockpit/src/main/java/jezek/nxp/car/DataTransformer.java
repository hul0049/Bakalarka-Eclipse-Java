package jezek.nxp.car;

public interface DataTransformer {
	public void processData(byte[] data);
	public boolean isControlToSend();
	public byte[] getControlToSend();
	public boolean isSettingToSend();
	public byte[] getSettingToSend();
	public boolean waitForTamplate();
	public byte[] getTempalte();
	public int dataCount();
	public boolean checkTail(byte [] data);
}
