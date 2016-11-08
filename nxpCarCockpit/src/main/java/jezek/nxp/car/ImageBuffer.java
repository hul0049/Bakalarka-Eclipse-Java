package jezek.nxp.car;

import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ImageBuffer implements ImageProducer {

	private int width;
	private int height;
	private int[] data;
	private int agregation;
	private int rowPosition;
	private int currentAgregatin;
	private double brightness = 1;
	private Set<ImageConsumer> imageConsumers = new HashSet<>();

	public ImageBuffer(int width, int height, int agregation) {
		super();
		if (width < 1 || height < 1 || agregation < 1) {
			throw new IllegalArgumentException("Wrong size or agregation.");
		}
		this.width = width;
		this.height = height;
		this.agregation = agregation;
		data = new int[width * height];
		rowPosition = 0;
		currentAgregatin = 0;
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
	}

	public static void main(String[] args) {
		for (int i = 1; i < 500; i++) {
			compar(i);
		}

	}

	private static void compar(int size) {
		System.gc();
		int[] src = new int[size];
		int[] dest = new int[size];
		long start = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			System.arraycopy(src, 0, dest, 0, size);
		}
		long end = System.nanoTime();
		long arraycopy = end - start;
		start = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			for (int j = 0; j < size; j++) {
				dest[j] = src[j];
			}
		}
		end = System.nanoTime();
		long copy = end - start;
		System.out.println(String.format("size = %d arraycopy/copy = %f", size, ((double) arraycopy / copy)));
	}

	@Override
	public void addConsumer(ImageConsumer ic) {
		if (!imageConsumers.contains(ic)) {
			imageConsumers.add(ic);
			ic.setDimensions(width, height);
			ic.setColorModel(ColorModel.getRGBdefault());
			ic.setHints(ImageConsumer.TOPDOWNLEFTRIGHT | ImageConsumer.COMPLETESCANLINES | ImageConsumer.SINGLEPASS
					| ImageConsumer.SINGLEFRAME);
			// ic.setPixels(0, 0, width, height - rowPosition,
			// ColorModel.getRGBdefault(), data, rowPosition*width, width);
			// ic.setPixels(0, height - rowPosition, width, rowPosition,
			// ColorModel.getRGBdefault(), data, 0, width);
			ic.setPixels(0, 0, width, height, ColorModel.getRGBdefault(), data, 0, width);
		}
	}

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
		return buf;
	}

	@Override
	public boolean isConsumer(ImageConsumer ic) {
		return imageConsumers.contains(ic);
	}

	@Override
	public void removeConsumer(ImageConsumer ic) {
		imageConsumers.remove(ic);
	}

	@Override
	public void startProduction(ImageConsumer ic) {
		addConsumer(ic);

	}

	@Override
	public void requestTopDownLeftRightResend(ImageConsumer ic) {
		/* ignore like memory image source */
	}

	public int getAgregation() {
		return agregation;
	}

	public void setAgregation(int agregation) {
		this.agregation = agregation;
	}

	public int getRowPosition() {
		return rowPosition;
	}

	public double getBrightness() {
		return brightness;
	}

	public void setBrightness(double brightness) {
		this.brightness = brightness;
	}

}
