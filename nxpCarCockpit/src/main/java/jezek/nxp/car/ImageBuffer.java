package jezek.nxp.car;

import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ImageBuffer extends  AbstractImageBuffer {

	private int width;
	private int height;
	private int[] data;
	private int agregation;
	private int rowPosition;
	private int currentAgregatin;
	private double brightness = 1;

	public ImageBuffer(int pixeCount, int zoom, int height, int agregation) {
		super();
		if (height < 1 || agregation < 1) {
			throw new IllegalArgumentException("Wrong size or agregation.");
		}
		this.width = pixeCount*zoom;
		this.height = height;
		this.agregation = agregation;
		data = new int[width * height];
		rowPosition = 0;
		currentAgregatin = 0;
		setChanged();
	}

	public void addRow(int[] row) {
		if (row.length != width) {
			throw new IllegalArgumentException("Wrong row length.");
		}
		if (currentAgregatin > 0) {
			for (int i = rowPosition * width; i < rowPosition * width + width; i++) {
				data[i] += row[i];
			}
			if (currentAgregatin + 1 == agregation) {
				for (int i = rowPosition * width; i < rowPosition * width + width; i++) {
					data[i] /= agregation;
				}
			}
		} else {
			System.arraycopy(row, 0, data, width * rowPosition, width);
		}
		if (currentAgregatin + 1 == agregation) {
			rowPosition = (rowPosition + 1) % height;
		}
		currentAgregatin = (currentAgregatin + 1) % agregation;
		setChanged();
	}

	@Override
	public int[] getImgData(int[] buf) {
		int zoom = buf.length / (width * height);
		if (buf.length % (width * height) != 0 && zoom != 0) {
			throw new IllegalArgumentException("Wrong buffer length.");
		}
		int rotPosition = rowPosition * width;

		for (int i = rotPosition, j = buf.length - zoom; i < data.length; i++, j -= zoom) {
			int intensity = (int) (255 * ((double) data[i]) / 0x0FFF * brightness);
			int value = 0xFF;
			value = (value << 8) + intensity;
			value = (value << 8) + intensity;
			value = (value << 8) + intensity;
			for (int z = 0; z < zoom; z++) {
				buf[j + z] = value;
			}
		}
		for (int i = 0, j = buf.length - (data.length - rotPosition) * zoom - zoom; i < rotPosition; i++, j -= zoom) {
			int intensity = (int) (255 * ((double) data[i]) / 0x0FFF * brightness);
			if(intensity > 255){
				intensity = 255;
			}
			int value = 0xFF;
			value = (value << 8) + intensity;
			value = (value << 8) + intensity;
			value = (value << 8) + intensity;
			for (int z = 0; z < zoom; z++) {
				buf[j + z] = value;
			}
		}
		clearChange();
		return buf;
	}

	public int getAgregation() {
		return agregation;
	}

	public void setAgregation(int agregation) {
		this.agregation = agregation;
		setChanged();
	}

	public int getRowPosition() {
		return rowPosition;
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
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
}
