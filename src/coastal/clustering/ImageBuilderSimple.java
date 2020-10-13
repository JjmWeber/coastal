package coastal.clustering;

import fr.unistra.pelican.Algorithm;
import fr.unistra.pelican.AlgorithmException;
import fr.unistra.pelican.ByteImage;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.InvalidNumberOfParametersException;
import fr.unistra.pelican.InvalidTypeOfParameterException;
import coastal.clustering.DrawConcept;

/**
 * The ImageBuilder class allows the user to draw markers with or without a
 * background image calling fr.unistra.pelican.gui.Draw2D
 * 
 * @author Florent Sollier, Jonathan Weber
 * 
 */
public class ImageBuilderSimple extends Algorithm {

	/***************************************************************************
	 * 
	 * 
	 * Attributes
	 * 
	 * 
	 **************************************************************************/

	/**
	 * The marker image
	 */
	public Image output;

	/**
	 * Background image
	 */
	public Image inputImage;

	/**
	 * Image to display (if different from the background image)
	 */
	public Image displayImage = null;

	/**
	 * This boolean informs if there is any background image or not
	 */
	//private static boolean background;

	/**
	 * Name of the background image file
	 */
	public String filename;

	/**
	 * reference to the Draw2D object
	 */
	private DrawConcept d2d;

	/***************************************************************************
	 * 
	 * 
	 * Methods
	 * 
	 * 
	 **************************************************************************/

	/**
	 * Constructor
	 * 
	 */
	public ImageBuilderSimple() {

		super();
		super.inputs = "inputImage,filename";
		super.options = "displayImage";
		super.outputs = "output";

	}

	/**
	 * Method which launch the algorithm
	 */
	public void launch() throws AlgorithmException {

		d2d = new DrawConcept(inputImage, filename, displayImage);
		output = (Image) d2d.output;

	}

	/**
	 * Exec without a background image
	 * 
	 * @param sizeX
	 *            Width of the marker image
	 * @param sizeY
	 *            Height of the marker image
	 * @param filename
	 *            name of the image to be treated.
	 * @return the marker image
	 * @throws InvalidTypeOfParameterException
	 * @throws AlgorithmException
	 * @throws InvalidNumberOfParametersException
	 */
	public static Image exec(int sizeX, int sizeY, String filename) {
		return (Image) new ImageBuilderSimple().process(new ByteImage(sizeX, sizeY,
				1, 1, 3), filename);
	}

	/**
	 * Exec without a background image
	 * 
	 * @param inputImage
	 *            Image to be treated.
	 * @param filename
	 *            name of the image to be treated.
	 * @return the marker image
	 * @throws InvalidTypeOfParameterException
	 * @throws AlgorithmException
	 * @throws InvalidNumberOfParametersException
	 */
	public static Image exec(Image inputImage, String filename) {
		return (Image) new ImageBuilderSimple().process(inputImage, filename);
	}

}