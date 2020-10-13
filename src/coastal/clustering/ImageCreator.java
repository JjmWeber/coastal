package coastal.clustering;

import java.awt.Point;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;

import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.TiledImage;

public class ImageCreator 
{
	private ImageCreator() {

	}

	/**
	 * Creates an empty TiledImage without an alpha channel
	 * 
	 * @param width
	 * @param height
	 * @return the TiledImage object
	 */
	public static TiledImage createGrayImage(int width, int height) {
		byte[][] imageData = new byte[width][height];

		// Fill with zeros
		// For testing, fill with something else
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				imageData[w][h] = 0;
			}
		}

		byte[] imageDataSingleArray = new byte[width * height];
		int count = 0;
		// Convert to single array
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				imageDataSingleArray[count++] = imageData[w][h];
			}
		}

		DataBufferByte dbuffer = new DataBufferByte(imageDataSingleArray, width
				* height);

		SampleModel sampleModel = RasterFactory.createBandedSampleModel(
				DataBuffer.TYPE_BYTE, width, height, 1);

		ColorModel colorModel = PlanarImage.createColorModel(sampleModel);

		Raster raster = RasterFactory.createWritableRaster(sampleModel,
				dbuffer, new Point(0, 0));

		TiledImage tiledImage = new TiledImage(0, 0, width, height, 0, 0,
				sampleModel, colorModel);

		tiledImage.setData(raster);

		return tiledImage;
	}

	/**
	 * Creates an empty TiledImage with an alpha channel
	 * 
	 * @param width
	 * @param height
	 * @return the TiledImage object
	 */
	public static TiledImage createGrayImageWithAlpha(int width, int height) {
		byte[][][] imageData = new byte[width][height][2];

		// Fill with zeros
		// For testing, fill with something else
		// Set alpha to transparent
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				for (int channel = 0; channel < 2; channel++) {
					imageData[w][h][channel] = 0;
				}
			}
		}

		byte[] imageDataSingleArray = new byte[width * height * 2];
		int count = 0;
		// Convert to single array
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				for (int channel = 0; channel < 2; channel++) {
					imageDataSingleArray[count++] = imageData[w][h][channel];
				}
			}
		}

		DataBufferByte dbuffer = new DataBufferByte(imageDataSingleArray, width
				* height * 2);

		SampleModel sampleModel = RasterFactory
				.createPixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, width,
						height, 2);

		ColorModel colorModel = PlanarImage.createColorModel(sampleModel);

		Raster raster = RasterFactory.createWritableRaster(sampleModel,
				dbuffer, new Point(0, 0));

		TiledImage tiledImage = new TiledImage(0, 0, width, height, 0, 0,
				sampleModel, colorModel);

		tiledImage.setData(raster);

		return tiledImage;
	}

}