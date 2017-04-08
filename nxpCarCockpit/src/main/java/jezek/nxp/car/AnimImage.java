package jezek.nxp.car;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author David Ježek
 *
 */
public class AnimImage extends Canvas implements Runnable {

	private static final Logger logger = LogManager.getLogger(AnimImage.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 5370391909405768024L;

	private transient BufferStrategy bf;
	private int fps = 0;
	private int imageWidth;
	private int imageHeight;
	private transient AbstractImageBuffer imageBuffer;
	private transient Thread animator;
	private boolean stop;
	private int[] imgData;

	public AnimImage() {
		this.imageWidth = 300;
		this.imageHeight = 300;
	}
	/**
	 * 
	 * @param width
	 * @param height
	 * @param imageBuffer
	 */
	public AnimImage(AbstractImageBuffer imageBuffer) {
		this.imageWidth = imageBuffer.getWidth();
		this.imageHeight = imageBuffer.getHeight();
		this.imageBuffer = imageBuffer;
		imgData = new int[imageWidth * imageHeight];

		setMinimumSize(new Dimension(imageWidth, imageHeight));
		setMaximumSize(new Dimension(imageWidth, imageHeight));
		setPreferredSize(new Dimension(imageWidth, imageHeight));
		setSize(new Dimension(imageWidth, imageHeight));
		addHierarchyListener(e -> {
			if (e.getChangeFlags() == HierarchyEvent.DISPLAYABILITY_CHANGED) {
				if (isDisplayable()) {
					startAnim();
				} else {
					stopAnim();
				}
			}
		});
	}

	public void startAnim() {
		createBufferStrategy(2);
		bf = getBufferStrategy();
		stop = false;
		if (animator == null) {
			animator = new Thread(this);
			animator.start();
		}

	}

	public void stopAnim() {
		stop = true;
		bf.dispose();
		if (animator != null) {
			animator.interrupt();
			animator = null;
		}
	}

	@Override
	public void run() {
		Random r = new Random();
		int counter = 0;
		long time = System.nanoTime();
//		int[] memBuf = new int[imageWidth + imageHeight];
		do {
//			logger.debug("do anim image");
			Graphics2D g = (Graphics2D) bf.getDrawGraphics();
//			for (int i = 0; i < memBuf.length; i++) {
//				memBuf[i] = r.nextInt();
//			}
			Image image = createImage(
					new MemoryImageSource(imageWidth, imageHeight, imageBuffer.getImgData(imgData), 0, imageWidth));
			g.drawImage(image, 0, 0, imageWidth, imageHeight, null);
			g.dispose();
			bf.show();
			counter++;
 			while (!imageBuffer.isChanged() && !stop) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
// 			logger.debug("image changed + " + ((ImageBuffer2)imageBuffer).getColumnZoom()[0]);
			if (System.nanoTime() - time > 2000000000l) {
				fps = counter/2;
//				logger.debug("fps " + fps);
				time = System.nanoTime();
				counter = 0;
			}
		} while (!stop && !Thread.currentThread().isInterrupted());
	}

	public int getFps() {
		return fps;
	}

}
