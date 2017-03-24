package jezek.nxp.car;

public abstract class AbstractImageBuffer {

	private boolean changed = false;
	
	public int[] createImageBuffer(){
		return new int[getWidth()+getHeight()];
	}
	
	public abstract int getWidth();
	public abstract int getHeight();
	
	public boolean isChanged(){
		return changed;
	}

	protected void setChanged(){
		this.changed = true;
	}
	
	protected void clearChange(){
		changed = false;
	}
	public abstract int[] getImgData(int[] buf);

}
