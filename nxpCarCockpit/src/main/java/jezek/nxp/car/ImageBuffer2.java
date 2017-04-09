package jezek.nxp.car;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.core.appender.db.ColumnMapping;

public class ImageBuffer2 extends AbstractImageBuffer {

	private int width;
	private int height;
	private int rowPosition;
	private double brightness = 1;
	private int heightZoom = 1;

	int camImageWidth = 128;
	int[] columnPositions;// = new int[] { 1, 130, 231, 332, 433, 535 };
	int[] columnZoom = new int[] { 2, 2, 2, 2, 2, 2 };
	int[] columnWidth = new int[] { camImageWidth, 49, 49, 49, 49, 49 };
	int[] columnBackground = new int[] { 0x0, 0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff };
	int[] middleAxisColor = new int[] { 0xffcccccc, 0xffcccccc, 0xffcccccc, 0xffcccccc, 0xffcccccc, 0xffcccccc };
	int[] middleAxisPosition = new int[] { 64, 25, 25, 25, 25, 25 };
	int[] drawToColumn = new int[] { 0, 1, 2, 3, 4, 5 };
	int[] dataRowColor = new int[] { 0x0, 0xffff0000, 0xff00ff00, 0xff44aa44, 0xff0000ff, 0xff00ffff };
	int frameColor = 0xffff0000;
	int voidColor = 0xffaaaaaa;
	int missingColor = 0xff550000;
	private PropertyChangeListener updateListener;
	private DrivingRecord drivingRecord;

	public ImageBuffer2(int height, DrivingRecord drivingRecord) {
		this.height = height;
		this.drivingRecord = drivingRecord;
		init();
		updateListener = evt -> update();
		drivingRecord.addPropertyDataListener(updateListener);
	}

	public void unregisterUpdater() {
		drivingRecord.removePropertyDataListener(updateListener);
	}

	public void setColumns(int[] columnWidth, int[] columnZoom) {
		this.columnWidth = columnWidth;
		this.columnZoom = columnZoom;
		init();
	}

	public void setDrawToColumn(int[] drawToColumn) {
		this.drawToColumn = drawToColumn;
	}

	private void init() {
		int position = 1;
		columnPositions = new int[columnWidth.length];
		for (int i = 0; i < columnWidth.length; i++) {
			columnPositions[i] = position;
			position += columnWidth[i] * columnZoom[i];
		}
		for (int i = 0; i < columnWidth.length; i++) {
			middleAxisPosition[i] = (columnWidth[i]) / 2;
		}
		if (height < 1) {
			throw new IllegalArgumentException("Wrong size or agregation.");
		}
		this.width = calculateWidth();
		rowPosition = drivingRecord.getData().size() - 1;
	}

	private int calculateWidth() {
		int value = 1;
		for (int i = 0; i < columnWidth.length; i++) {
			value += columnWidth[i] * columnZoom[i] + 1;
		}
		return value;
	}

	public void update() {
		rowPosition = drivingRecord.getData().size() - 1;
		setChanged();
	}

	private void writePixel(int[] buf, int offset, int zoom, int data) {
		for (int z = 0; z < zoom; z++) {
			buf[offset + z] = data;
		}
	}

	private int bwIntensity(int intensity, boolean selected) {
		int value = 0xFF;
		value = (value << 8) + intensity;
		value = (value << 8) + intensity;
		value = (value << 8) + intensity;
		// if(intensity <= 50){
		// value = 0xff0000ff;sele
		// }
		// if(intensity > 150){
		// value = 0xff00ff00;
		// }
		if (selected) {
			value = new Color(value).darker().getRGB();
		}
		return value;
	}

	private int camPixelIntensity(WifiMonitorData data, int index, double brightness) {
		return (int) (255 * ((double) data.getImage()[index] / 255 * brightness));
	}

	private void eraseColumn(int[] buf, int rowBegin, int columnIndex, boolean selected) {
		int color = columnBackground[columnIndex];
		if (selected) {
			color = new Color(color).darker().getRGB();
		}
		for (int i = 0; i < columnWidth[columnIndex] * columnZoom[columnIndex]; i++) {
			buf[rowBegin + columnPositions[columnIndex] + i] = color;
		}
	}

	private void drawDataRow(int index, int value, int imgRowBegin, int[] buf) {
		drawDataRow(index, value, imgRowBegin, buf, false);
	}

	private void drawDataRow(int index, int value, int imgRowBegin, int[] buf, boolean darker) {
		int color = dataRowColor[index];
		if (darker) {
			color = new Color(color).darker().darker().getRGB();
		}
		double recalculatedValue = columnWidth[drawToColumn[index]] * columnZoom[drawToColumn[index]] / 2000.0
				* (value + 1000);
		int bufIndex = imgRowBegin + columnPositions[drawToColumn[index]] + (int) recalculatedValue;
		writePixel(buf, bufIndex, columnZoom[drawToColumn[index]], color);
	}

	@Override
	public int[] getImgData(int[] buf) {
		if (buf.length % (width) != 0) {
			throw new IllegalArgumentException("Wrong buffer length.");
		}
		synchronized (drivingRecord.getData()) {
			int currentRowPosition = rowPosition;

			for (int imageRowIndex = 0; imageRowIndex < height; imageRowIndex++) {
				int imgRowBegin = imageRowIndex * width;
				if (currentRowPosition - imageRowIndex / heightZoom < 0 || drivingRecord.getData().isEmpty()
						|| drivingRecord.getData().get(currentRowPosition - imageRowIndex / heightZoom).isMissing()) {
					int color = currentRowPosition - imageRowIndex / heightZoom < 0 ? voidColor : missingColor;
					for (int i = 0; i < width; i++) {
						buf[imgRowBegin + i] = color;
					}
					continue;
				}
				WifiMonitorData wifiMonitorData = drivingRecord.getData()
						.get(currentRowPosition - (imageRowIndex / heightZoom));
				for (int i = 0; i < columnWidth.length; i++) {
					eraseColumn(buf, imgRowBegin, i, wifiMonitorData.isSelected());
				}
				drawFrameAndAxis(buf, imgRowBegin);
				drawCamData(buf, currentRowPosition, imageRowIndex, imgRowBegin, wifiMonitorData.isSelected());
				drawDataRow(1, wifiMonitorData.getServo(), imgRowBegin, buf);
				drawDataRow(2, wifiMonitorData.getPwm()[0], imgRowBegin, buf);
				drawDataRow(3, wifiMonitorData.getPwm()[1], imgRowBegin, buf);
				drawDataRow(4, wifiMonitorData.getFb()[0], imgRowBegin, buf);
				drawDataRow(5, wifiMonitorData.getFb()[1], imgRowBegin, buf);
				if (wifiMonitorData.getResponseData() != null) {
					drawDataRow(1, wifiMonitorData.getResponseData().getServo(), imgRowBegin, buf, true);
					drawDataRow(2, wifiMonitorData.getResponseData().getPwm()[0], imgRowBegin, buf, true);
					drawDataRow(3, wifiMonitorData.getResponseData().getPwm()[1], imgRowBegin, buf, true);
				}
			}
		}
		clearChange();
		return buf;
	}

	private int drawCamData(int[] buf, int currentRowPosition, int imageRowIndex, int imgRowBegin, boolean selected) {
		int columnBegin = imgRowBegin + columnPositions[0];
		for (int camImageIndex = 0; camImageIndex < camImageWidth; camImageIndex++) {
			writePixel(buf, columnBegin + camImageIndex * columnZoom[0], columnZoom[0],
					bwIntensity(camPixelIntensity(
							drivingRecord.getData().get(currentRowPosition - imageRowIndex / heightZoom), camImageIndex,
							brightness), selected));
		}
		return columnBegin;
	}

	private void drawFrameAndAxis(int[] buf, int imgRowBegin) {
		buf[imgRowBegin] = frameColor;
		for (int i = 0; i < columnPositions.length; i++) {
			buf[imgRowBegin + columnPositions[i] + columnWidth[i] * columnZoom[i]] = frameColor;
		}
		for (int i = 0; i < columnPositions.length; i++) {
			writePixel(buf, imgRowBegin + columnPositions[i] + middleAxisPosition[i] * columnZoom[i], columnZoom[i],
					middleAxisColor[i]);
		}
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	public void setScrollPosition(int position) {
		if (position > 0 && position < drivingRecord.getData().size()) {
			rowPosition = position;
			setChanged();
		}
	}

	public double getBrightness() {
		return brightness;
	}

	public void setBrightness(double brightness) {
		this.brightness = brightness;
		setChanged();
	}

	public int[] getColumnZoom() {
		return this.columnZoom;
	}

	public int getHeightZoom() {
		return heightZoom;
	}

	public void setHeightZoom(int heightZoom) {
		this.heightZoom = heightZoom;
	}

	public void selectRow(int imageRowIndex) {
		int selectedDataIndex = rowPosition - ((imageRowIndex - 1) / heightZoom);
		if (selectedDataIndex < 0 || selectedDataIndex > drivingRecord.getData().size()) {
			return;
		}
		WifiMonitorData data = drivingRecord.getData().get(selectedDataIndex);
		data.setSelected(!data.isSelected());
		setChanged();
	}

	public void insertResponse(int width, int height) {
		int selectedDataIndex = rowPosition - ((height - 1) / heightZoom);
		if (selectedDataIndex < 0 || selectedDataIndex > drivingRecord.getData().size()) {
			return;
		}
		int column = -1;
		int min = -1;
		int max = -1;
		for (int i = 0; i < columnPositions.length; i++) {
			if (width >= columnPositions[i] && width < columnPositions[i + 1] - 1) {
				column = i;
				min = columnPositions[i];
				max = columnPositions[i + 1] - 1;
				break;
			}
		}
		if (column == -1) {
			column = columnPositions.length - 1;
			min = columnPositions[column];
			max = height - 1;
		}
		int center = middleAxisPosition[column];
		int value = 0;
		double recalculatedX = (width - min)/(double)columnZoom[column];
		value = (int) (1000 * (recalculatedX-center) / (double) (columnWidth[column]/2));
		setResponseValue(column, value, selectedDataIndex);
		setChanged();
	}

	private void setResponseValue(int columnIndex, int value, int recordIndex) {
		for (int i = 0; i < drawToColumn.length; i++) {
			if (drawToColumn[i] == columnIndex) {
				setResponseDataValue(i, value, recordIndex);
			}
		}
	}

	private void setResponseDataValue(int dataIndex, int value, int recordIndex) {
		if (drivingRecord.getData().get(recordIndex).getResponseData() == null) {
			drivingRecord.getData().get(recordIndex).setResponseData(new ResponseData());
		}
		switch (dataIndex) {
		case 1:
			drivingRecord.getData().get(recordIndex).getResponseData().setServo(value);
			break;
		case 2:
			drivingRecord.getData().get(recordIndex).getResponseData().getPwm()[0] = value;
			break;
		case 3:
			drivingRecord.getData().get(recordIndex).getResponseData().getPwm()[1] = value;
			break;
		default:
			break;
		}
	}
}
