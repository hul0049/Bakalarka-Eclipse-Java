package jezek.nxp.car;

public class ImageBuffer extends AbstractImageBuffer implements RotateDataBufferInterface{

	private RotateDataBufferInterface dataBuffer;
	private int zoom;
	private double brightness = 1;
//	private int leftBorderCut = 127 - 15;
//	private int rightBorderCut = 19;

	public ImageBuffer(RotateDataBufferInterface rotateDataBuffer, int zoom) {
		super();
		dataBuffer = rotateDataBuffer;
		if (dataBuffer.getHeight() < 1 || dataBuffer.getAgregation() < 1) {
			throw new IllegalArgumentException("Wrong size or agregation.");
		}
		this.zoom = zoom;
		setChanged();
	}

	public ImageBuffer(int pixeCount, int zoom, int height, int agregation) {
		super();
		dataBuffer = new RotateDataBuffer(pixeCount, height, agregation);
		if (height < 1 || agregation < 1) {
			throw new IllegalArgumentException("Wrong size or agregation.");
		}
		this.zoom = zoom;
		setChanged();
	}

	public void addRow(int[] row) {
		dataBuffer.addRow(row);
		setChanged();
	}

	@Override
	public int[] getImgData(int[] buf) {
		int width = dataBuffer.getWidth();
		int height = dataBuffer.getHeight();
		int zoom = buf.length / (width * height);
		if (buf.length % (width * height) != 0 && zoom != 0) {
			throw new IllegalArgumentException("Wrong buffer length.");
		}
		int rotPosition = dataBuffer.getRowPosition() * width;

		for (int i = rotPosition, j = buf.length - zoom; i < dataBuffer.getData().length; i++, j -= zoom) {
			processPixel(buf, zoom, i, j);
		}
		for (int i = 0, j = buf.length - (dataBuffer.getData().length - rotPosition) * zoom - zoom; i < rotPosition; i++, j -= zoom) {
			processPixel(buf, zoom, i, j);
		}
		mirrorImage(buf, zoom);
		clearChange();
		return buf;
	}

	private void mirrorImage(int[] buf, int zoom) {
		int rowLength = dataBuffer.getWidth() * zoom;
		for (int i = 0; i < buf.length / rowLength; i++) {
			for (int j = 0; j < rowLength / 2; j++) {
				int value = buf[i * rowLength + j];
				buf[i * rowLength + j] = buf[i * rowLength + rowLength - 1 - j];
				buf[i * rowLength + rowLength - 1 - j] = value;
			}
		}
	}

	private void processPixel(int[] buf, int zoom, int dataIndex, int bufferIndex) {
		int intensity = (int) (255 * ((double) dataBuffer.getData()[dataIndex]) / 0x0FFF * brightness);
		if (intensity > 255) {
			intensity = 255;
		}
		int value = 0xFF;
		value = (value << 8) + intensity;
		value = (value << 8) + intensity;
		value = (value << 8) + intensity;
		int width = dataBuffer.getWidth();
		if (isBorderToCut(dataIndex % width)) {
			value = 0xFF;
			value = (value << 8) + intensity;
			value = (value << 8) + intensity;
			value = (value << 8) + 100 + (int) (155 * intensity / 255.0);

		}
		value = drawComputedData(dataIndex / width, dataIndex % width, value);
		for (int z = 0; z < zoom; z++) {
			buf[bufferIndex + z] = value;
		}
	}

	private int redefineColor(int color, int red, int green, int blue) {
		int value = 0xFF;
		value = (value << 8) + ((red >= 0) ? red : (int) ((-red) * (((color & 0x00FF0000) >> 16) / 255.0)));
		value = (value << 8) + ((green >= 0) ? green : (int) ((-green) * (((color & 0x0000FF00) >> 8) / 255.0)));
		value = (value << 8) + ((blue >= 0) ? blue : (int) ((-blue) * ((color & 0x000000FF) / 255.0)));
		return value;
	}

	private int drawComputedData(int rowIndex, int columnIndex, int color) {
		RowDataInfo rowDataInfo = dataBuffer.getRowDataInfos()[rowIndex];
		int value = color;
		if (rowDataInfo.candidates != null && rowDataInfo.candidates.isEmpty()
				&& columnIndex < 5) {
			value = redefineColor(value, 255, -100, -100);
		}
		if (rowDataInfo.candidates != null && (rowDataInfo.candidates.contains(columnIndex))) {
			value = redefineColor(value, 255, -100, 255);
		}
		if (rowDataInfo.hotCandidates != null
				&& (rowDataInfo.hotCandidates.stream().map(c -> c.index).anyMatch(c -> c == columnIndex))) {
			value = redefineColor(value, 200, -100, 200);
		}
		if (rowDataInfo.lines != null && (rowDataInfo.lines.contains(columnIndex))) {
			value = redefineColor(value, 255, -200, -200);
		}
		if (rowDataInfo.getLeftIndex() == columnIndex) {
			value = redefineColor(value, 00, 255, 00);
		}
		if (rowDataInfo.getRightIndex() == columnIndex) {
			value = redefineColor(value, 00, 00, 255);
		}
		return value;
	}

	private boolean isBorderToCut(int rowIndex) {
		return rowIndex < dataBuffer.getRightBorderCut() || rowIndex > dataBuffer.getLeftBorderCut();
	}

	public int getAgregation() {
		return dataBuffer.getAgregation();
	}

	public void setAgregation(int agregation) {
		dataBuffer.setAgregation(agregation);
		setChanged();
	}

	public double getBrightness() {
		return brightness;
	}

	public void setBrightness(double brightness) {
		this.brightness = brightness;
		setChanged();
	}

	@Override
	public int getWidth() {
		return dataBuffer.getWidth() * zoom;
	}

	@Override
	public int getHeight() {
		return dataBuffer.getHeight();
	}

	public int[] getData() {
		return dataBuffer.getData();
	}

	public RowDataInfo[] getRowDataInfos() {
		return dataBuffer.getRowDataInfos();
	}

	public int getLeftBorderCut() {
		return dataBuffer.getLeftBorderCut();
	}

	public int getRightBorderCut() {
		return dataBuffer.getRightBorderCut();
	}

	public int getRowPosition() {
		return dataBuffer.getRowPosition();
	}

	public int getLastRight(int rowIndex, int limit) {
		return dataBuffer.getLastRight(rowIndex, limit);
	}

	public int getLastLeft(int rowIndex, int limit) {
		return dataBuffer.getLastLeft(rowIndex, limit);
	}

}
