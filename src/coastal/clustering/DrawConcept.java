package coastal.clustering;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import javax.media.jai.GraphicsJAI;
import javax.media.jai.JAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.RenderedOp;
import javax.media.jai.TiledImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileFilter;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import com.sun.media.jai.widget.DisplayJAI;

import fr.unistra.pelican.ByteImage;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.IntegerImage;
import fr.unistra.pelican.algorithms.conversion.AverageChannels;
import fr.unistra.pelican.algorithms.conversion.GrayToRGB;
import fr.unistra.pelican.algorithms.segmentation.labels.LabelsToPredefinedColor;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;

/**
 * The Draw2D class allows the user to draw markers with or without a background
 * image.
 * 
 * @author florent
 * 
 */
public class DrawConcept extends JPanel
{

	/***************************************************************************
	 * 
	 * 
	 * Attributes
	 * 
	 * 
	 **************************************************************************/

	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;

	/**
	 * Scroll pane for the image
	 */
	public JScrollPane scroll;

	/**
	 * Name of the background image file
	 */
	public String title;

	/**
	 * Maximum stroke size for the bruch
	 */
	public final int MAX_BRUSH_SIZE = 100;

	/**
	 * Defaut stroke size of the brush
	 */
	public final int DEFAULT_BRUSH = 5;

	/**
	 * Button wich allows the user to add a new label
	 */
	public JButton plusButton = new JButton();

	/**
	 * Combobox which contains all the labels
	 */
	public JComboBox labelComboBox = new JComboBox();

	/**
	 * Spinner which allows the user to change the stroke size of the brush
	 */
	public JSpinner brushSpinner = new JSpinner();

	/**
	 * Spinner number model of the brush spinner
	 */
	public SpinnerNumberModel brushSpinnerModel;

	/**
	 * Panel which contains the color of the current label
	 */
	public JPanel labelColor = new JPanel();

	/**
	 * Label of the brush stroke size
	 */
	public JLabel bruschLabel = new JLabel();

	/**
	 * label for the transparency slider
	 */
	public JLabel transparencyLabel = new JLabel();

	/**
	 * To change the transparency of the marker image
	 */
	public JSlider transparencySlider = new JSlider();

	/**
	 * Button to undo the last label painting
	 */
	public JButton undoButton = new JButton();

	/**
	 * Button to confirm marker drawing's end
	 */
	public JButton okButton = new JButton();

	/**
	 * Button to begin a new marker image
	 */
	public JButton resetButton = new JButton();

	/**
	 * Image builder frame
	 */
	public JDialog frame = new JDialog();

	/**
	 * Instance of MarkerDisplayJAI
	 */
	public MarkerDisplayJAI display = new MarkerDisplayJAI(this);

	/**
	 * The background image converted in BufferedImage
	 */
	private BufferedImage bimg = null;

	/**
	 * The marker image // L'image de sortie, avec les concepts
	 */
	public Image output;
	
	/**
	 * Background image // L'image d'entrée
	 */
	public Image inputImage;
	
	/**
	 * Image to display (if different from background)
	 */
	public Image displayImage;

	/**
	 * true if resetting
	 */
	private boolean reset = false;
	
	
	
	
	// New
	 // Gestion des concepts
		Concept infos;
		public ArrayList<String> nomConcept = new ArrayList<String>();
		public ArrayList<Color> colorConcept = new ArrayList<Color>();
	
	
	 // Boutons
		public JButton modifyButton = new JButton();
		public JButton createArff = new JButton();
		public JToggleButton gommeButton = new JToggleButton();
		public JToggleButton  drawAnAreaButton = new JToggleButton ();
	
		
	// Ajout pour choisir la méthode de classification
		private String[] methodesSupervisees = { "Sélectionnez une méthode", "Naive Bayes", "J48", "IBk"};
		public JComboBox jcb_lesMethodes = new JComboBox(methodesSupervisees);
	
	// Panneau caché pour la méthode IBK
		JPanel jPanel4 = new JPanel();
		public JLabel lbl_nb_ibk = new JLabel("Nombre de K : ");
		public JTextField nb_ibk = new JTextField("k", 5);
	
	// Pour module sauvegarde ROI
		public JButton saveButton = new JButton(new ImageIcon("./saveIcon.jpg"));
		public JButton openButton = new JButton(new ImageIcon("./openIcon.jpg"));
		
		final static String ROI_NAME = "; ROI name: ";
		final static String ROI_COLOR = "; ROI rgb value: ";
		final static String ROI_NBPTS = "; ROI npts: ";
		
	/***************************************************************************
	 * 
	 * 
	 * Constructors
	 * 
	 * 
	 **************************************************************************/

	/**
	 * Constructor with a background image
	 * 
	 * @param inputImage
	 *            the fr.unistra.pelican image to set as background
	 * @param fileName Name of the background image file
	 */
	public DrawConcept(Image inputImage, String title) {

		this.inputImage = inputImage;
		this.title = title;
		bimg = pelicanImageToBufferedImage(this.inputImage);
		// The BufferedImage is setted to display
		display.set(bimg);
		guiInitialisation();
	}
	/**
	 * Constructor with a background image
	 * 
	 * @param inputImage
	 *            the fr.unistra.pelican image to set as background
	 * @param fileName Name of the background image file
	 * @param displayImage Image to display (if different from background)
	 */
	public DrawConcept(Image inputImage, String title, Image displayImage) {

		this.inputImage = inputImage;
		this.title = title;
		this.displayImage = displayImage;
		bimg = pelicanImageToBufferedImage(this.displayImage);
		// The BufferedImage is setted to display
		display.set(bimg);
		guiInitialisation();
	}

	
	/***************************************************************************
	 * 
	 * 
	 * Methods
	 * 
	 * 
	 **************************************************************************/

	/**
	 * Initialisation of the Draw2D GUI
	 * 
	 */
	private void guiInitialisation() {

		frame = new JDialog();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setModal(true);

		this.setLayout(new BorderLayout());

		JPanel jPanel2 = new JPanel();
		jPanel2.setLayout(new GridBagLayout());

		JPanel jPanel3 = new JPanel();
		jPanel3.setLayout(new FlowLayout());

		JPanel jPanel5 = new JPanel();
		jPanel5.setLayout(new FlowLayout());
		
		JPanel jPanelSud = new JPanel();
		jPanelSud.setLayout(new BorderLayout());
	
		
		// Component which contains tha MarkerDisplayJAI instance (display)
		scroll = new JScrollPane(display,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		
		this.add(scroll, BorderLayout.CENTER);	
		

		brushSpinnerModel = new SpinnerNumberModel(DEFAULT_BRUSH, 1,
				MAX_BRUSH_SIZE, 1);

		
		// Gestion des items du ComboBox
		
		labelComboBox.setBackground(SystemColor.control);
		labelComboBox.setOpaque(false);		
		
		
		this.InitialisationCB();
		
		labelComboBox.addActionListener(new Draw2D_labelComboBox_actionAdapter(this));
		

		
		/**
		 * GESTIONNAIRES & DISPOSITION :
		 */
		
		// Gestion des ajouts de concepts
		
		plusButton.setText("Ajouter un Concept");
		plusButton.addActionListener(new Draw2D_plusButton_actionAdapter(this));

		
		modifyButton.setText("Modifier le Concept");
		modifyButton.addActionListener(new Draw2D_modifyLabel_actionAdapter(this));
		
		
		labelColor.setBorder(BorderFactory.createLineBorder(Color.black));
		labelColor.setMinimumSize(new Dimension(20, 20));
		labelColor.setPreferredSize(new Dimension(20, 20));
		
		bruschLabel.setBackground(SystemColor.control);
		bruschLabel.setText(" Epaisser du trait : ");
		
		brushSpinner.setModel(brushSpinnerModel);
		brushSpinner.setMaximumSize(new Dimension(32767, 32767));
		brushSpinner.setMinimumSize(new Dimension(40, 18));
		brushSpinner.setPreferredSize(new Dimension(40, 18));
		brushSpinner.addChangeListener(new Draw2D_brushSpinner_actionAdapter(this));
		
		transparencyLabel.setText("Label Transparency :");
		
		transparencySlider.setExtent(0);
		transparencySlider.setMaximum(255);
		transparencySlider.setPaintLabels(false);
		transparencySlider.setPaintTicks(false);
		transparencySlider.setPaintTrack(true);
		transparencySlider.setBackground(SystemColor.control);
		transparencySlider.setMaximumSize(new Dimension(32767, 24));
		transparencySlider.setValue(255);
		transparencySlider.addChangeListener(new Draw2D_transparencySlider_changeAdapter(this));

		
		/** Module de sauvegarde ROI */
		saveButton.addActionListener(new Draw2D_saveButton_actionAdapter(this));
		jPanel3.add(saveButton);
		openButton.addActionListener(new Draw2D_openButton_actionAdapter(this));
		jPanel3.add(openButton);
		
				// Gestion de la gommme
			gommeButton.setText("Gommer");
			gommeButton.addActionListener(new Draw2D_gommeButton_actionAdapter(this));
		jPanel3.add(gommeButton);
			
				// Gestion du dessin de polygones
			drawAnAreaButton.setText("Dessiner un polygone");
			drawAnAreaButton.addActionListener(new Draw2D_drawAnAreaButton_actionAdapter(this));
		jPanel3.add(drawAnAreaButton);
			
			
			undoButton.setText("Annuler la dernière action");
			undoButton.addActionListener(new Draw2D_undoButton_actionAdapter(this));
		jPanel3.add(undoButton);
			undoButton.setEnabled(false);
			
			
			resetButton.setText("Reset");
			resetButton.addActionListener(new Draw2D_resetButton_actionAdapter(this));
		jPanel3.add(resetButton);
			resetButton.setEnabled(false);
			
//			// Gestion du dessin de polygones
//			createArff.setText("create Arff");
//			createArff.addActionListener(new Draw2D_createArffButton_actionAdapter(this));
//		jPanel3.add(createArff);
			
			
			jcb_lesMethodes.setSelectedIndex(0);
			jcb_lesMethodes.addActionListener(new Selection_changeAdapter(this));
			jPanel5.add(jcb_lesMethodes);
		
			
			jPanel4.setLayout(new FlowLayout());
			jPanel4.add(lbl_nb_ibk);
			jPanel4.add(nb_ibk);
				
			jPanel5.add(jPanel4);
				jPanel4.setVisible(false);
			
			
			okButton.setText("Ok");
			okButton.addActionListener(new Draw2D_okButton_actionAdapter(this));
		jPanel5.add(okButton);
			okButton.setEnabled(false);
			
			jPanelSud.add(jPanel3, BorderLayout.NORTH);
			jPanelSud.add(jPanel5, BorderLayout.SOUTH);


		jPanel2.add(modifyButton, new GridBagConstraints(3, 0, 1, 2, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
							5, 5, 5, 5), 0, 0));
		jPanel2.add(labelColor, new GridBagConstraints(1, 0, 1, 2, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						5, 0, 5, 0), 0, 0));
		jPanel2.add(brushSpinner, new GridBagConstraints(5, 0, 1, 2, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						0, 5, 0), 0, 0));
		jPanel2.add(plusButton, new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 0, 0));
		jPanel2.add(labelComboBox, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						15, 5, 0), 0, 0));
		jPanel2.add(bruschLabel, new GridBagConstraints(4, 0, 1, 2, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						15, 5, 0), 0, 0));
		jPanel2.add(transparencyLabel, new GridBagConstraints(7, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 0, 0, 5), 0, 0));
		jPanel2.add(transparencySlider, new GridBagConstraints(8, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 0, 5, 5), 0, 0));

		this.add(jPanel2, BorderLayout.NORTH);
		this.add(jPanelSud, BorderLayout.SOUTH);

		frame.setTitle(title);
		frame.setContentPane(this);
		frame.pack();
		frame.setPreferredSize(new Dimension(800, 400));
		frame.setVisible(true);

	}

	/**
	 * Method which converts the background image (Image type) to a
	 * BufferedImage
	 */
	@SuppressWarnings("deprecation")
	private BufferedImage pelicanImageToBufferedImage(Image inputImage) {

		if (inputImage.getBDim()!=3)
			inputImage=GrayToRGB.exec(AverageChannels.exec(inputImage));
		
		BufferedImage bimg = null;

		final int bdim = inputImage.getBDim();
		final int tdim = inputImage.getTDim();
		final int zdim = inputImage.getZDim();

		// transform the image into an array of BufferedImages
		DataBufferByte dbb;
		SampleModel s;
		Raster r;
		
		int[] bandOffsets = { 0, 1, 2 };

		for (int t = 0; t < tdim; t++) {
			for (int z = 0; z < zdim; z++) {

				ByteImage tmp = inputImage.getColorByteChannelZT(z, t);				

				byte[] tmp2 = new byte[tmp.size()];
				for (int i = 0; i < tmp.size(); i++)
					tmp2[i] = (byte) tmp.getPixelByte(i);

				dbb = new DataBufferByte(tmp2, tmp.size());
				s = RasterFactory.createPixelInterleavedSampleModel(
						DataBuffer.TYPE_BYTE, tmp.getXDim(), tmp.getYDim(),
						bdim, bdim * tmp.getXDim(), bandOffsets);
				r = RasterFactory.createWritableRaster(s, dbb, new Point(0, 0));
				bimg = new BufferedImage(tmp.getXDim(), tmp.getYDim(),
						BufferedImage.TYPE_3BYTE_BGR);
				bimg.setData(r);

			}
		}
		return bimg;
	}

	/***************************************************************************
	 * 
	 * 
	 * GUI functionnalities
	 * 
	 * 
	 **************************************************************************/

	/**
	 * Method which allows the user to remove or re-insert the last marker drawn
	 */
	public void undoButton_actionPerformed(ActionEvent e) {

		display.undo();
	}

	/**
	 * this method will remove all the markers drawn by the user
	 * 
	 * @param e
	 */
	public void resetButton_actionPerformed(ActionEvent e)
	{
		reset();
	}

	/**
	 * Sets the thicknees of the pen
	 * 
	 * @param e
	 */
	public void brushSpinner_changed(ChangeEvent e) {

		if (brushSpinner.getValue() instanceof Number) {
			Number n = (Number) brushSpinner.getValue();
			float brushSize = n.floatValue();
			display.setStroke(new BasicStroke(brushSize));
		}

	}

	/**
	 * Changes the transparency of the markers
	 * 
	 * @param e
	 */
	public void transparencySlider_stateChanged(ChangeEvent e) {

		int transparency = transparencySlider.getValue();
		display.setMarkerTransparency(transparency);
	}

	/**
	 * Adds a new label in the combobox and sets the new color
	 * 
	 * @param e
	 */
	public void labelComboBox_actionPerformed(ActionEvent e) {

		if (!reset) 
		{
			display.setMarker(labelComboBox.getSelectedIndex());
			
			Color color = colorConcept.get(labelComboBox.getSelectedIndex());
			labelColor.setBackground(color);
			
			
//			display.setMarker(labelComboBox.getSelectedIndex());

			// set the current color to labelColor
//			Color color = display.getColorMarker();
//			labelColor.setBackground(color);
		}

	}

	/**
	 * Adds a new label
	 * 
	 * @param e
	 */
	public void plusButton_actionPerformed(ActionEvent e) 
	{
		ConceptCreator dialogue;
		dialogue = new ConceptCreator();

			Concept infos = new Concept();
			//RefineryUtilities.centerFrameOnScreen(dialogue);

			dialogue.initConceptCreator();
			
		ConceptList classList = new ConceptList();
		
		dialogue.lanceDial(infos, classList);
		
		if (dialogue.getAccord())
		{		
			nomConcept.add(infos.nomLabel);
			colorConcept.add(infos.couleurLabel);
			// On ajoute le concept
			ajoutConcept(infos.nomLabel, infos.couleurLabel);					
		}
	}
	
	
	/**
	 * Modify a label
	 * 
	 * @param e
	 */
	public void createArffButton_actionPerformed(ActionEvent e) 
	{		
			CreerArffAvecClasseTexte();
			CreerArffSansClasseTexte();
	}	
	
	
	/**
	 * save a ROI file
	 * 
	 * @param e
	 * @throws IOException 
	 * @throws IOException 
	 */
	public void saveButton_actionPerformed(ActionEvent e) throws IOException
	{
		createOutputImage();

		FileWriter fw;
		BufferedWriter bf;
		PrintWriter pw;

		String path = "";
		boolean bonFichier = false; // True si on a un fichier où écrire.
		
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(new RoiFilter());
		chooser.setAcceptAllFileFilterUsed(false);

		File fichier_roi;
		fichier_roi = chooser.getSelectedFile();
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			fichier_roi = chooser.getSelectedFile();
			path = chooser.getSelectedFile().getAbsolutePath();
		}
		
		if(!isROIExtension(new File(path)))
		{
			path += ".roi";
			fichier_roi = new File(path);
		}

		if(!fichier_roi.exists())
		{
			try
			{
				fichier_roi.createNewFile();
				bonFichier = true;
			}
			catch(IOException e1)
			{
				e1.printStackTrace();
			}
		}
		else
		{
			int rep = JOptionPane.showConfirmDialog(this,
					"Le fichier existe déjà." + "\nSouhaitez vous écraser le fichier ?",
					null, JOptionPane.YES_NO_OPTION);
			if (rep == JOptionPane.YES_OPTION)
			{
				bonFichier = true;
			}
		}

		if(bonFichier == true)
		{
			fw = new FileWriter(fichier_roi);

			bf = new BufferedWriter (fw);
			pw = new PrintWriter (bf);
	
			pw.println ("; ENVI Output of ROIs");
			pw.println ("; Description: generated by ModuleIHM Trie par classe, map info de " + path + "");
			pw.println ("; Number of ROIs: " + (this.nomConcept.size()-1));
			pw.println ("; File Dimension: " + inputImage.getXDim() + " x " + inputImage.getYDim());
			pw.println ("");
			
			/* Impression de chaque label du vecteur */
			for (int i = 1; i < this.nomConcept.size(); i++)
			{
				Color tmp = this.colorConcept.get(i);
				pw.println (ROI_NAME + this.nomConcept.get(i));
				pw.println (ROI_COLOR + "{"+tmp.getRed()+", "+tmp.getGreen()+", "+tmp.getBlue()+"}");
				pw.println (ROI_NBPTS + "");
				pw.println ("");
			}
	
			pw.println (";   ID    X    Y       Map X       Map Y        Lat       Lon  B1  B2  B3");
	
			for (int i = 1; i < nomConcept.size(); i++) 
			{
				int nbPixels = 1;
				for (int y = 0; y < inputImage.getYDim(); y++) 
				{
					for (int x = 0; x < inputImage.getXDim(); x++) 
					{
						if(output.getPixelXYBByte(x, y, 0) == i)
						{
							pw.println (nbPixels + " " + x + " " + y);
							nbPixels++;
						}
					}
				}
				pw.println ("");
			}

			pw.close();
		}
	}	


	public boolean isROIExtension(File f)
	{
		String extension = getExtension(f);
		boolean result = false;

		if (extension != null)
			result = extension.equals(RoiFilter.ROI);

		return result;
	}



	/**
	 * open a ROI file
	 * 
	 * @param e
	 */
	public void openButton_actionPerformed(ActionEvent e) 
	{		
	// On remet tout à zéro des concepts actuels
		reset();

	// On analyse le fichier ROI
		String path = "";
		JFileChooser jfc = new JFileChooser();
		jfc.addChoosableFileFilter(new RoiFilter());
		jfc.setAcceptAllFileFilterUsed(false);

		Boolean bonneCouleur;
		Color couleur;

		if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			path = jfc.getSelectedFile().getAbsolutePath();

		    try
		    {
		    	BufferedReader fichier= new BufferedReader(new FileReader(path)); 

			    String ligne;

			    while( (ligne = fichier.readLine()) != null && (! ligne.startsWith(";   ID    X ")))
			    {
			    	if(ligne.equals(""))
			    	{
			    		// Ligne vide
			    	}
			    	else if(ligne.startsWith(ROI_NAME))
			    	{
			    		String nom = ligne.substring(ROI_NAME.length()); 
			    		nomConcept.add(nom);
			    	}
			    	else if(ligne.startsWith(ROI_COLOR))
			    	{
			    		String couleurString = ligne.substring(ROI_COLOR.length()); 
			    		couleur = new Color(0,0,0);
			    		bonneCouleur = false;
			    		StringTokenizer st = new StringTokenizer(couleurString, "{, }");
			    		while(st.hasMoreTokens())
			    		{
			    			couleur = new Color(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
			    			bonneCouleur = true;
			    		}
			    		if(bonneCouleur == true)
			    		{
				    		colorConcept.add(couleur);
			    		}
			    	}
			    }

			    for(int i = 1; i < nomConcept.size(); i++)
			    {
			    	// On ajoute les concepts (nom + couleur) à notre programme : insertion dans la checkbox et mise en place des couleurs dans le tableau
			    	this.ajoutConcept(nomConcept.get(i), colorConcept.get(i));
			    }

			    int x;
			    int y;

			    int indexConcept = 1;

	    		Graphics2D g2d = display.markerImage1.createGraphics();
				Stroke fin = new BasicStroke(1.0f);
				g2d.setStroke(fin);

				display.setMarker(indexConcept);
				g2d.setColor(new Color(display.markerImage1.getColorModel().getColorSpace(), display.currentColorIndex, 1.0f));


			    while( (ligne = fichier.readLine()) != null )
			    {
			    	if(!ligne.equals(""))
			    	{
			    		// On récupère les coordonnées du point
				    		StringTokenizer st = new StringTokenizer(ligne, " ");
				    		st.nextToken();
				    		x = Integer.parseInt(st.nextToken());
				    		y = Integer.parseInt(st.nextToken());
				    		
				    	// On colorie le point dans la couleur du concept actuellement traité
				    		g2d.draw(new Line2D.Double(x, y, x, y));
			    	}
			    	else
			    	{
			    		if(!(indexConcept == (nomConcept.size()-1)))
			    		{
			    		// On saute une ligne, donc on change de concept, alors on change de couleur.
				    		indexConcept++;
				    		display.setMarker(indexConcept);
				    		g2d.setColor(new Color(display.markerImage1.getColorModel().getColorSpace(), display.currentColorIndex, 1.0f));
			    		}
			    	}
			    }
				// repaint the component
				display.createColorMarkerImage();
				display.repaint();
				okButton.setEnabled(true);
				resetButton.setEnabled(true);
		    }
		    catch (IOException e1)
		    {
		    	e1.printStackTrace();
		    }
		}
	}
	
	public class RoiFilter extends FileFilter {
		public final static String ROI = "roi";
		
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}

			String extension = getExtension(f);
			if (extension != null) {
				if (extension.equals(RoiFilter.ROI)) {
					return true;
				} else {
					return false;
				}
			}

			return false;
		}
		
		// The description of this filter
		public String getDescription() {
			return "Fichier.roi";
		}
	}
	
	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}
	
	
	
	/**
	 * Modify a label
	 * 
	 * @param e
	 */
	public void modifyLabel_actionPerformed(ActionEvent e) 
	{
		// On regarde si l'utilisateur n'essaie pas de modifier le concept gomme
		if(this.labelComboBox.getItemAt(labelComboBox.getSelectedIndex())=="Gomme")
		{
			JOptionPane.showMessageDialog(this,
	   			    "Impossible de modifier la gomme",
	   			    "Information",
	   			    JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			ConceptCreator dialogue;
			dialogue = new ConceptCreator();
			
			// On initialise le concept avec les valeurs de celui que l'on veut modifier.
			int i = labelComboBox.getSelectedIndex();
			Concept infos = new Concept();
			infos.nomLabel = nomConcept.get(i); 
			infos.couleurLabel = colorConcept.get(i);
			
			dialogue.modifyClass(infos);
				
			ConceptList classList = new ConceptList();
			
			dialogue.lanceDial(infos, classList);
			
			if (dialogue.getAccord())
			{
				// Update currentColorIndex
				display.setMarker(i);											

				nomConcept.set(i, infos.nomLabel);
				colorConcept.set(i, infos.couleurLabel);
				
						display.colorMap[i][0] = infos.couleurLabel.getRed();
						display.colorMap[i][1] = infos.couleurLabel.getGreen();
						display.colorMap[i][2] = infos.couleurLabel.getBlue();
						display.colorMap[i][3] = 255;
				
				Color color = infos.couleurLabel;
				labelColor.setBackground(color);
			
				// Modifier la valeur dans la JComboBox
						//ComboBoxEditor uhh = labelComboBox.getEditor();
						//labelComboBox.setEditor(uhh);
						//labelComboBox.getItemAt(i) = infos.nomLabel;
				labelComboBox.setSelectedItem(infos.nomLabel);
			}				
			
			// repaint the component
			display.createColorMarkerImage();
			display.repaint();
		}
	}	
	
	
	
// DESSIN POLYGONE :
	@SuppressWarnings("deprecation")
	public void drawAnAreaButton_actionPerformed(ActionEvent e)
	{
		if(display.drawArea == false)
		{
		// Si on entre ici c'est qu'on ne dessinait pas un polygone, donc on commence à en dessiner un
			display.drawArea = true;
			this.drawAnAreaButton.setSelected(true);
			this.drawAnAreaButton.setText("Arrêter polygone");
			this.labelComboBox.enable(false);
			this.jcb_lesMethodes.enable(false);
			this.modifyButton.setEnabled(false);
			this.plusButton.setEnabled(false);
			this.undoButton.setEnabled(false);
			this.resetButton.setEnabled(false);
			this.okButton.setEnabled(false);
			this.gommeButton.setEnabled(false);
		}
		else
		{
			int rep = JOptionPane.showConfirmDialog(this,
					"Si vous êtes en train de dessiner un polygone non fermé il sera perdu !" + "\nEtes-vous sûr de vouloir continuer ?",
					null, JOptionPane.YES_NO_OPTION);
			if (rep == JOptionPane.YES_OPTION)
			{
				//Si on arrive ici, c'est que la personne a fini ou arrête de dessiner le polygone.
					ramPolygone();
			}
		}
	}	
// ------------------
	
	
// Ajout d'une gomme :
	/**
	 * @param e
	 */
	@SuppressWarnings("deprecation")
	public void gommeButton_actionPerformed(ActionEvent e) 
	{
			boolean en_dev = false;
			if(en_dev == false)
			{
				if(display.gomme == false)
				{				
					display.gomme = true;
					display.setMarker(0);
					this.gommeButton.setSelected(true);
					this.gommeButton.setText("Arrêter de Gommer");
					this.labelComboBox.enable(false);
					this.drawAnAreaButton.setEnabled(false);
					this.modifyButton.setEnabled(false);
					this.plusButton.setEnabled(false);
					this.undoButton.setEnabled(false);
					//this.resetButton.setEnabled(false);
				}
				else
				{
					ramGomme();		
				}
				
			}
			else
			{
				JOptionPane.showMessageDialog(this,
		   			    "A venir",
		   			    "Information",
		   			    JOptionPane.INFORMATION_MESSAGE);
			}
	}	
	

	/**
	 * Generates an fr.unistra.pelican.image from the marker image
	 * @param e
	 */
	public void okButton_actionPerformed(ActionEvent e) 
	{
		if(jcb_lesMethodes.getSelectedItem() == "Sélectionnez une méthode")
		{
			JOptionPane.showMessageDialog(this,
	   			    "Aucune méthode sélectionnée",
	   			    "Information",
	   			    JOptionPane.INFORMATION_MESSAGE);
		}
		else 
		{
			createOutputImage();

			// Création du fichiers arff :
				CreerArffAvecClasseTexte();
				CreerArffSansClasseTexte();
			
			if(jcb_lesMethodes.getSelectedItem() == "Naive Bayes")
			{
				
				//Création des dataSet :
				Instances dataSet_sansClasse = CreerDataSetObjetSansClasse();
				dataSet_sansClasse.setClassIndex(dataSet_sansClasse.numAttributes()-1);
				
				Instances dataSet_avecClasse = CreerDataSetObjetAvecClasse();
				dataSet_avecClasse.setClassIndex(dataSet_avecClasse.numAttributes()-1);
				
				NaiveBayes bayes = new NaiveBayes();
				try
				{
					bayes.buildClassifier(dataSet_avecClasse);
				}
				catch(Exception e1)
				{
					e1.printStackTrace();
				}
		
				int[] resultat;
				resultat = new int[dataSet_sansClasse.numInstances()];
		
		
		
				for(int i = 0; i < dataSet_sansClasse.numInstances(); i++)
				{
					try 
					{
						int value = (int)bayes.classifyInstance(dataSet_sansClasse.instance(i));
						resultat[i] = value;
					} 
					catch(Exception e1) 
					{
						e1.printStackTrace();
					}
				}
		
				
				frame.dispose();
				
				
				Image monImage;
				
				monImage = new IntegerImage(output.getXDim(),output.getYDim(),output.getZDim(),output.getTDim(),1);
				
			
				for(int i = 0; i < dataSet_sansClasse.numInstances(); i++) 
				{
						monImage.setPixelInt(i, resultat[i]);
				}
				
			// On ne prend autant de couleurs qu'on a de concepts créés par l'utilisateur
				Color mesColors[] = new Color[colorConcept.size()];
				
				
				for(int i1 = 0; i1 < colorConcept.size(); i1++)
				{
					mesColors[i1]= colorConcept.get(i1);
				}
				
				monImage = LabelsToPredefinedColor.exec(monImage, mesColors);
				 

				Viewer2D.exec(monImage,"try");
			}
			if(jcb_lesMethodes.getSelectedItem() == "J48")
			{

				//Création des dataSet :
				Instances dataSet_sansClasse = CreerDataSetObjetSansClasse();
				dataSet_sansClasse.setClassIndex(dataSet_sansClasse.numAttributes()-1);
				
				Instances dataSet_avecClasse = CreerDataSetObjetAvecClasse();
				dataSet_avecClasse.setClassIndex(dataSet_avecClasse.numAttributes()-1);
				
				J48 tree = new J48();
				try
				{
					tree.buildClassifier(dataSet_avecClasse);
				}
				catch(Exception e1)
				{
					e1.printStackTrace();
				}
		
				int[] resultat;
				resultat = new int[dataSet_sansClasse.numInstances()];
		
		
		
				for(int i = 0; i < dataSet_sansClasse.numInstances(); i++)
				{
					try 
					{
						int value = (int)tree.classifyInstance(dataSet_sansClasse.instance(i));
						resultat[i] = value;
					} 
					catch(Exception e1) 
					{
						e1.printStackTrace();
					}
				}
			
				frame.dispose();
				
				Image monImage;
				
				monImage = new IntegerImage(output.getXDim(),output.getYDim(),output.getZDim(),output.getTDim(),1);
				
			
				for(int i = 0; i < dataSet_sansClasse.numInstances(); i++) 
				{
						monImage.setPixelInt(i, resultat[i]);
				}
				
			// On ne prend autant de couleurs qu'on a de concepts créés par l'utilisateur
				Color mesColors[] = new Color[colorConcept.size()];
				
				
				for(int i1 = 0; i1 < colorConcept.size(); i1++)
				{
					mesColors[i1]= colorConcept.get(i1);
				}
				
				monImage = LabelsToPredefinedColor.exec(monImage, mesColors);
				 

				Viewer2D.exec(monImage,"try");
			}
			if(jcb_lesMethodes.getSelectedItem() == "IBk")
			{
				boolean bonneValeurK = false;
				
				int nb_k;
				nb_k = 1;
				
				if(this.nb_ibk.getText() != "k")
				{
					try
					{
						nb_k = Integer.parseInt(this.nb_ibk.getText());
						
						bonneValeurK = true;
					}
					catch(NumberFormatException nfe)
					{
						JOptionPane.showMessageDialog(this,
				   			    "Vous devez entrer un entier pour k",
				   			    "Information",
				   			    JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this,
			   			    "Veuillez entrer la valeur de K",
			   			    "Information",
			   			    JOptionPane.INFORMATION_MESSAGE);
				}
				if(bonneValeurK == true)
				{
					//Création des dataSet :
					Instances dataSet_sansClasse = CreerDataSetObjetSansClasse();
					dataSet_sansClasse.setClassIndex(dataSet_sansClasse.numAttributes()-1);
					
					Instances dataSet_avecClasse = CreerDataSetObjetAvecClasse();
					dataSet_avecClasse.setClassIndex(dataSet_avecClasse.numAttributes()-1);
					
					IBk test = new IBk();
					
					try
					{
						test.setKNN(nb_k);
					}
					catch(Exception e1)
					{
						e1.printStackTrace();
					}
					try
					{
						test.buildClassifier(dataSet_avecClasse);
					}
					catch(Exception e1)
					{
						e1.printStackTrace();
					}
			
					int[] resultat;
					resultat = new int[dataSet_sansClasse.numInstances()];
			
			
			
					for(int i = 0; i < dataSet_sansClasse.numInstances(); i++)
					{
						try 
						{
							int value = (int)test.classifyInstance(dataSet_sansClasse.instance(i));
							resultat[i] = value;
						} 
						catch(Exception e1) 
						{
							e1.printStackTrace();
						}
					}
				
					frame.dispose();
					
					Image monImage;
					
					monImage = new IntegerImage(output.getXDim(),output.getYDim(),output.getZDim(),output.getTDim(),1);
					
				
					for(int i = 0; i < dataSet_sansClasse.numInstances(); i++) 
					{
							monImage.setPixelInt(i, resultat[i]);
					}
					
				// On ne prend autant de couleurs qu'on a de concepts créés par l'utilisateur
					Color mesColors[] = new Color[colorConcept.size()];
					
					
					for(int i1 = 0; i1 < colorConcept.size(); i1++)
					{
						mesColors[i1]= colorConcept.get(i1);
					}

					monImage = LabelsToPredefinedColor.exec(monImage, mesColors);
					 
	
					Viewer2D.exec(monImage,"try");
				}
			}

			
	
	// Fin du else, si l'utilisateur a choisit une méthode.
		}


	}

	/***************************************************************************
	 * 
	 * Listener inner-classes
	 * 
	 **************************************************************************/

	/**
	 * brushSpinner actionAdapter uses to call brushSpinner_changed(e)
	 */
	public class Draw2D_brushSpinner_actionAdapter implements javax.swing.event.ChangeListener
	{
		DrawConcept d2d;

		Draw2D_brushSpinner_actionAdapter(DrawConcept d2d)
		{
			this.d2d = d2d;
		}

		public void stateChanged(ChangeEvent e)
		{
			d2d.brushSpinner_changed(e);
		}
	}

	/**
	 * undoButton actionAdapter uses to call undoButton_actionPerformed(e)
	 */
	public class Draw2D_undoButton_actionAdapter implements	java.awt.event.ActionListener
	{
		DrawConcept d2d;

		Draw2D_undoButton_actionAdapter(DrawConcept d2d)
		{
			this.d2d = d2d;
		}

		public void actionPerformed(ActionEvent e)
		{
			d2d.undoButton_actionPerformed(e);
		}
	}

	/**
	 * labelComboBox actionAdapter uses to call labelComboBox_actionPerformed(e)
	 */
	public class Draw2D_labelComboBox_actionAdapter implements java.awt.event.ActionListener
	{
		DrawConcept d2d;

		Draw2D_labelComboBox_actionAdapter(DrawConcept d2d)
		{
			this.d2d = d2d;
		}

		public void actionPerformed(ActionEvent e)
		{
			d2d.labelComboBox_actionPerformed(e);
		}
	}

	/**
	 * okButton actionAdapter uses to call okButton_actionPerformed(e)
	 */
	public class Draw2D_okButton_actionAdapter implements java.awt.event.ActionListener
	{
		DrawConcept d2d;

		Draw2D_okButton_actionAdapter(DrawConcept d2d)
		{
			this.d2d = d2d;
		}

		public void actionPerformed(ActionEvent e)
		{
			d2d.okButton_actionPerformed(e);
		}
	}

	/**
	 * resetButton actionAdapter uses to call resetButton_actionPerformed(e)
	 */
	public class Draw2D_resetButton_actionAdapter implements java.awt.event.ActionListener
	{
		DrawConcept d2d;

		Draw2D_resetButton_actionAdapter(DrawConcept d2d)
		{
			this.d2d = d2d;
		}

		public void actionPerformed(ActionEvent e)
		{
			d2d.resetButton_actionPerformed(e);
		}
	}

	/**
	 * plusButton actionAdapter uses to call plusButton_actionPerformed(e)
	 */
	public class Draw2D_plusButton_actionAdapter implements	java.awt.event.ActionListener
	{
		DrawConcept d2d;

		Draw2D_plusButton_actionAdapter(DrawConcept d2d)
		{
			this.d2d = d2d;
		}

		public void actionPerformed(ActionEvent e)
		{
			d2d.plusButton_actionPerformed(e);
		}
	}
	
	public class Draw2D_modifyLabel_actionAdapter implements java.awt.event.ActionListener
	{
		DrawConcept d2d;
		
		Draw2D_modifyLabel_actionAdapter(DrawConcept d2d)
		{
			this.d2d = d2d;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			d2d.modifyLabel_actionPerformed(e);
		}
	}
	
	public class Draw2D_createArffButton_actionAdapter implements java.awt.event.ActionListener
	{
		DrawConcept d2d;
		
		Draw2D_createArffButton_actionAdapter(DrawConcept d2d)
		{
			this.d2d = d2d;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			d2d.createArffButton_actionPerformed(e);
		}
	}
	
	public class Draw2D_gommeButton_actionAdapter implements java.awt.event.ActionListener
	{
		DrawConcept d2d;
		
		Draw2D_gommeButton_actionAdapter(DrawConcept d2d)
		{
			this.d2d = d2d;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			d2d.gommeButton_actionPerformed(e);
		}
	}
	
	public class Draw2D_drawAnAreaButton_actionAdapter implements java.awt.event.ActionListener
	{
		DrawConcept d2d;
		
		Draw2D_drawAnAreaButton_actionAdapter(DrawConcept d2d)
		{
			this.d2d = d2d;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			d2d.drawAnAreaButton_actionPerformed(e);
		}
	}
	
	
	public class Draw2D_saveButton_actionAdapter implements java.awt.event.ActionListener
	{
		DrawConcept d2d;
		
		Draw2D_saveButton_actionAdapter(DrawConcept d2d)
		{
			this.d2d = d2d;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				d2d.saveButton_actionPerformed(e);
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
	}
	
	public class Draw2D_openButton_actionAdapter implements java.awt.event.ActionListener
	{
		DrawConcept d2d;
		
		Draw2D_openButton_actionAdapter(DrawConcept d2d)
		{
			this.d2d = d2d;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			d2d.openButton_actionPerformed(e);
		}
	}
	

	/**
	 * transparencySlider changeAdapter uses to call
	 * transparencySlider_stateChanged(e)
	 */
	public class Draw2D_transparencySlider_changeAdapter implements javax.swing.event.ChangeListener
	{
		DrawConcept d2d;

		Draw2D_transparencySlider_changeAdapter(DrawConcept d2d)
		{
			this.d2d = d2d;
		}

		public void stateChanged(ChangeEvent e)
		{
			d2d.transparencySlider_stateChanged(e);
		}
	}
	
	
	public class Selection_changeAdapter implements ActionListener 
	{
		DrawConcept ihm;
	    public String new_methode_choisie;

	    public Selection_changeAdapter(DrawConcept ihm)
	    {
	    	this.ihm = ihm;
	    }

	    public void actionPerformed(ActionEvent e) {
	        JComboBox cb = (JComboBox)e.getSource();
	    	this.new_methode_choisie = (String)cb.getSelectedItem();
	    	if(new_methode_choisie == "IBk")
	    	{
	    		jPanel4.setVisible(true);
	    	}
	    	else
	    	{
	    		jPanel4.setVisible(false);
	    	}
	    }
	}
	
	/***************************************************************************
	 * 
	 * 
	 * MarkerDisplayJAI
	 * 
	 * 
	 **************************************************************************/

	/**
	 * The MarkerDisplayJAI handles all the image traitments, including for the
	 * background image and for the marker image.
	 */
	 class MarkerDisplayJAI extends DisplayJAI {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * The marker image
		 */
		public TiledImage markerImage1;

		/**
		 * oldMarkerRi is use to save the marker image after after each draw in
		 * case of an undo.
		 */
		public WritableRenderedImage oldMarkerRi;

		/**
		 * Indice of the transparency of the markers (255 = completely visible)
		 */
		public int markerTransparency = 255;

		/**
		 * last x position of the cursor
		 */
		public int last_x;

		/**
		 * last y position of the cursor
		 */
		public int last_y;

		/**
		 * The color marker image (after the createColorMarkerImage process)
		 */
		public RenderedOp colorMarkerImage;

		/**
		 * Determines if the user is drawing or not
		 */
		public boolean drawing = false;

		/**
		 * Thickness of the pen
		 */
		public Stroke stroke = new BasicStroke(5.0f);

		/**
		 * Contain the current color index
		 */
		float currentColorIndex[];

		/**
		 * Button to add a new label
		 */
		int plusButtonIndex;

		/**
		 * Instance of random to generate random numbers
		 */
		Random random;

		/**
		 * Map which contains all the label with their associated color
		 */
		int[][] colorMap = new int[257][4];
		
		
		
		/**
		 * Boolean : vrai si on gomme, faux sinon.
		 */
		boolean gomme = false;
		
		
// DESSIN POLYGONE :	
		/**
		 * Boolean : vrai si on dessine un polygone, faux sinon.
		 */
		boolean drawArea = false;
		
		/**
		 * Tableau qui contient les différents points de notre polygone.
		 */
		ArrayList<Point> lesPoints = new ArrayList<Point>();
		
		/**
		 * Variable qui contient le nombre de points actuellement sur notre polygone.
		 */
		int nbPoints = 0;
		
		/**
		 * Variable qui contient les coordonnées d'un point courant.
		 */
		Point ancienPoint = new Point();
		
		/**
		 * Lien vers l'IHM appelant :
		 */
		DrawConcept IHM;
		
		/**
		 * oldMarkerRi is use to save the marker image before drawing a polygon
		 */
		public WritableRenderedImage avantPolygone;
		
		/**
		 * Boolean : vrai si on dessine un polygone, faux sinon.
		 */
		boolean polygoneTermine = false;
		
// ------------------	
		

		/**
		 * Constructor
		 * 
		 */
		MarkerDisplayJAI() {
			super();
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

			// Initializes variables
			currentColorIndex = new float[1];
			currentColorIndex[0] = (float) 1 / 255;
			plusButtonIndex = 0;

			addMouseListener(this);
			addMouseMotionListener(this);

		}
		
		MarkerDisplayJAI(DrawConcept monIHM) {
			super();
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			
			IHM = monIHM;

			// Initializes variables
			currentColorIndex = new float[1];
			currentColorIndex[0] = (float) 1 / 255;
			plusButtonIndex = 0;

			addMouseListener(this);
			addMouseMotionListener(this);

		}

		/**
		 * Method which sets the background image and creates the invisible
		 * image based on the dimension of the background image.
		 */
		public void set(RenderedImage im) {
			super.set(im);

			// Create a marker image without an alpha channel
			markerImage1 = ImageCreator.createGrayImage(im.getWidth(), im
					.getHeight());

			// repaint the component
			createColorMarkerImage();
			repaint();

		}

		/**
		 * 
		 * @return the marker image
		 */
		public RenderedImage getMarkerImage() {
			return markerImage1;
		}

		/**
		 * 
		 * @return a copy of the marker image
		 */
		public WritableRenderedImage copyMarkerImage() {
			WritableRenderedImage copy = ImageCreator.createGrayImage(
					markerImage1.getWidth(), markerImage1.getHeight());
			copy.setData(markerImage1.copyData());
			return copy;
		}

		/**
		 * Set the marker transparency
		 * 
		 * @param markerTransparency
		 *            indice of transparency
		 */
		public void setMarkerTransparency(int markerTransparency) {
			if (colorMap != null) {
				this.markerTransparency = markerTransparency;

				// Set the colorMap transparency for all but the first
				// element
				for (int i = 1; i < 256; i++) {
					colorMap[i][3] = (byte) markerTransparency;
				}
				// refresh
				createColorMarkerImage();
				repaint();
			}
		}

		/**
		 * paintComponent method
		 */
		public void paintComponent(Graphics g)
		{
			// Paint the image in super class
			super.paintComponent(g);

			if (colorMarkerImage != null)
			{
				// Get graphics and create GraphicsJAI
				Graphics2D g2d = (Graphics2D) g;
				GraphicsJAI gj = GraphicsJAI.createGraphicsJAI(g2d, this);

				// Draw marker image
				gj.drawRenderedImage(colorMarkerImage, new AffineTransform());
			}
		}


		/**
		 * Mouse pressed method
		 */
		@SuppressWarnings("static-access")
		public void mousePressed(MouseEvent e) 
		{
			if (e.getButton() == e.BUTTON1) 
			{
				if (markerImage1 != null) 
				{
					if(drawArea == true)
					{
						if(nbPoints == 0)
						{
							/** On dessine le premier point*/
							
								// On commence donc un polygone, qui n'est pas terminé :
								polygoneTermine=false;
							
								// On sauvegarde l'image avant de commencer le polygone
								avantPolygone = copyMarkerImage();
			
								drawing = true;
			
								last_x = e.getX();
								last_y = e.getY();
			
								Graphics2D g2d = markerImage1.createGraphics();
			
								// Set line width and marker (Aplha is 1.0,
								// because colorMap handels transparency)
								Stroke fin = new BasicStroke(1.0f);
								g2d.setStroke(fin);
								
								g2d.setColor(new Color(markerImage1.getColorModel().getColorSpace(), currentColorIndex, 1.0f));
								// Draw the line
								g2d.draw(new Line2D.Double(last_x, last_y, last_x, last_y));
			
								// Update
								createColorMarkerImage();
			
								// Repaint the component
								repaint();
								
							
							lesPoints.add(new Point(last_x, last_y));
							nbPoints++;
							
							ancienPoint.x = last_x;
							ancienPoint.y = last_y;
							
						}
						else
						{
							/** <b> On dessine un nouveau point, qui n'est pas le premier </b>*/
			
								drawing = true;
			
								last_x = e.getX();
								last_y = e.getY();
			
								Graphics2D g2d = markerImage1.createGraphics();
			
								// Set line width and marker (Aplha is 1.0,
								// because colorMap handels transparency)
								
								Stroke fin = new BasicStroke(1.0f);
								g2d.setStroke(fin);
								
								g2d.setColor(new Color(markerImage1.getColorModel().getColorSpace(), currentColorIndex, 1.0f));
								// Draw the line
								g2d.draw(new Line2D.Double(last_x, last_y, last_x, last_y));
			
								// Update
								createColorMarkerImage();
			
								// Repaint the component
								repaint();
								
								
								lesPoints.add(new Point(last_x, last_y));
								nbPoints++;
							
							/** On trace le trait*/
								g2d.draw(new Line2D.Double(ancienPoint.x, ancienPoint.y, last_x, last_y));
							
								ancienPoint.x = last_x;
								ancienPoint.y = last_y;
						}	
					}
					else
					{
						if(this.gomme == false)
						{ 
								// Enable buttons
							this.IHM.undoButton.setEnabled(true);
							this.IHM.okButton.setEnabled(true);
							this.IHM.resetButton.setEnabled(true);
						}
						
						// save old image for undo
						oldMarkerRi = copyMarkerImage();
	
						drawing = true;
	
						last_x = e.getX();
						last_y = e.getY();
	
						Graphics2D g2d = markerImage1.createGraphics();
	
						// Set line width and marker (Aplha is 1.0,
						// because colorMap handels transparency)
						g2d.setStroke(stroke);
						
						
						g2d.setColor(new Color(markerImage1.getColorModel().getColorSpace(), currentColorIndex, 1.0f));
						// Draw the line
						g2d.draw(new Line2D.Double(e.getX(), e.getY(), e.getX(), e
								.getY()));
	
						// Update
						createColorMarkerImage();
	
						// Repaint the component
						repaint();
					}
					
				}
			}
			// Fermeture du polygone :
			if (e.getButton() == e.BUTTON3)
			{
				if(drawArea == true)
				{
					if(nbPoints > 2)
					{
						/** On trace juste la ligne entre le dernier point et le premier */
		
							drawing = true;
		
					//		last_x = e.getX();
					//		last_y = e.getY();
		
							Graphics2D g2d = markerImage1.createGraphics();
		
							// Set line width and marker (Aplha is 1.0,
							// because colorMap handels transparency)
							Stroke fin = new BasicStroke(1.0f);
							g2d.setStroke(fin);
							
							g2d.setColor(new Color(markerImage1.getColorModel().getColorSpace(), currentColorIndex, 1.0f));
							// Draw the line
							g2d.draw(new Line2D.Double(lesPoints.get(0).x, lesPoints.get(0).y, ancienPoint.x, ancienPoint.y));

							
	// Le polygone est terminé :
							polygoneTermine = true;
							
								// Création d'un polygone correspondant à la forme dessinée.
									
									int nb_points = lesPoints.size();
									int[]x = new int[nb_points];
									int[]y = new int[nb_points];
									for(int i=0; i < nb_points; i++)
									{
										x[i]=lesPoints.get(i).x;
										y[i]=lesPoints.get(i).y;
									}
									
									Polygon poly = new Polygon(x, y, nb_points);
									g2d.fillPolygon(poly); 
									
							// Update
							createColorMarkerImage();
		
							// Repaint the component
							repaint();
							
						/** Il faudra remettre à zéro tout ce qui concerne polygone : nbPoint, drawArea, etc .. */
							IHM.resetDessinPolygone();
					}
					else
					{
						JOptionPane.showMessageDialog(this,
			   			    "Il faut au moins avoir placé 3 points pour former un polygone ! ( Placez un autre point avant de refaire clic droit, ou recliquez sur <Arrêter polygone> pour annuler la création",
			   			    "Information",
			   			    JOptionPane.INFORMATION_MESSAGE);
						
						polygoneTermine = false;
					}
				}
			}
		}

		/**
		 * Mouse dragged method
		 */
		public void mouseDragged(MouseEvent e) 
		{
			if (drawing == true) 
			{
				if (markerImage1 != null) 
				{
					if(this.drawArea == false) // Si on est en train de dessiner un polygone on desactive la fonction
					{
						Graphics2D g2d = markerImage1.createGraphics();
	
						// Set line width and marker (Aplha is 1.0 this time,
						// because lut handels transparency)
						g2d.setStroke(stroke);
						
						g2d.setColor(new Color(markerImage1.getColorModel().getColorSpace(), currentColorIndex, 1.0f));
						// Draw the line
						g2d.draw(new Line2D.Double(e.getX(), e.getY(), last_x,
								last_y));
	
						// Update
						createColorMarkerImage();
	
						// Repaint the component
						repaint();
	
						last_x = e.getX();
						last_y = e.getY();
					}
				}
			}
		}

		/**
		 * Mouse released method
		 */
		@SuppressWarnings("static-access")
		public void mouseReleased(MouseEvent e) 
		{
			if (e.getButton() == e.BUTTON1) 
			{
				drawing = false;
			}
			if (markerImage1 != null) 
			{
				// Si on veut pasa avoir les boutons lors gommage et poly :
				if((this.IHM.display.gomme == false) && (this.IHM.display.drawArea == false))
				{
						// Enable buttons
					this.IHM.undoButton.setEnabled(true);
					this.IHM.okButton.setEnabled(true);
					this.IHM.resetButton.setEnabled(true);
				}

			}
		}

		/**
		 * Set the thickness of the pen
		 * 
		 * @param stroke
		 *            value of the stroke
		 */
		public void setStroke(Stroke stroke) {
			this.stroke = stroke;
		}

		/**
		 * 
		 * Creates a color marker image from the marker image (greyscale)
		 */
		public void createColorMarkerImage() {

			byte[] reds = new byte[256];
			byte[] greens = new byte[256];
			byte[] blues = new byte[256];
			byte[] alpha = new byte[256];

			for (int i = 0; i < 255; i++) {
				reds[i] = (byte) colorMap[i][0];
				greens[i] = (byte) colorMap[i][1];
				blues[i] = (byte) colorMap[i][2];
				alpha[i] = (byte) colorMap[i][3];
			}

			byte[][] lut = new byte[4][256];
			lut[0] = reds;
			lut[1] = greens;
			lut[2] = blues;
			lut[3] = alpha;

			LookupTableJAI table = new LookupTableJAI(lut);

			ParameterBlock pb = new ParameterBlock();
			pb.addSource(markerImage1);
			pb.add(table);

			colorMarkerImage = JAI.create("lookup", pb, null);
		}

		/**
		 * Method which allows the user to remove or re-insert the last marker
		 * drawn
		 */
		public void undo() {

			// re-display the last marker image saved in oldMarkerRi
			WritableRenderedImage tempoRi = copyMarkerImage();
			markerImage1.setData(oldMarkerRi.getData());
			oldMarkerRi = tempoRi;
			

			createColorMarkerImage();
			repaint();
		}
		
		/**
		 * Method which allows the user to remove or re-insert the marker before polygon
		 * 
		 */
		public void undoPolygon() {

			// re-display the last marker image saved in oldMarkerRi
			if(avantPolygone != null)
			{
				markerImage1.setData(this.IHM.display.avantPolygone.getData());
				oldMarkerRi = this.IHM.display.avantPolygone;
			}
			

			createColorMarkerImage();
			repaint();
		}

		/**
		 * 
		 * @return the indice of the current marker color
		 */
		public int getMarker() {
			return (int) (currentColorIndex[0] * 255);
		}

		/**
		 * Set the indice of the current marker color
		 * 
		 * @param marker
		 */
		public void setMarker(int marker) {
			this.currentColorIndex[0] = (float) (marker / 255f);

		}


		/**
		 * 
		 * @return the current marker color
		 */
		public Color getColorMarker() {
			Color color = new Color((int) colorMap[(int) (getMarker())][0],
					(int) colorMap[(int) (getMarker())][1],
					(int) colorMap[(int) (getMarker())][2]);
			return color;
		}
		
		
	}


	/**
	 * FIN DE MARKERJDISPLAY
	 * * ---------------------------------------------------------------------------------------
	 * Mes ajouts :
	 */
/** Mes ajouts */
	
	
	public void CreerArffAvecClasseIndex()
	{
	    Instances dataSet;
	    // Vecteur représentant une ligne = un pixel avec ses valeurs en RGB donnés par des vecteurs de niveau inférieur.
	    FastVector wekaAttributes = new FastVector();;

		// On construit le vecteur decrivant une instance ( Ici une instance est un pixel, représenté par ses intensités dans les différentes bandes composant l'image )
		Attribute Attribute1;
		
		int bdim = inputImage.getBDim();

		for(int b = 0; b < bdim; b++)
		{
			Attribute1 = new Attribute("b" + (b+1));
			// Pour chaque bande on créé un argument de type numérique qui contiendra l'intensité
			wekaAttributes.addElement(Attribute1);
		}
		
		// POUR APPRENTISSAGE SUPERVISE
		// On ajoute un attribut classe qui peut prendre comme valeurs tous les concepts créés

		FastVector classVector = new FastVector();
		for (int i = 1; i < this.nomConcept.size(); i++) 
		{
			classVector.addElement(i);
		}

		
		wekaAttributes.addElement(new Attribute("class", classVector));	
		
					
		int xdim = this.inputImage.getXDim();
		int ydim = this.inputImage.getYDim();
		
		// Une ligne par pixel => nbPixel = nbInstances
		int nbPixel = xdim*ydim;
		dataSet = new Instances("Les Pixels", wekaAttributes, nbPixel); 
		
		
		// POUR APPRENTISSAGE SUPERVISE
		// On définit l'attribut qui jouera le rôle de classe
		dataSet.setClassIndex(wekaAttributes.size()-1);
		
		
		Instance instEx = new Instance(wekaAttributes.size());
		
	
		for(int y = 0; y < ydim; y++){
			for(int x = 0; x < xdim; x++){

				
				for(int b = 0; b < bdim; b++){
					instEx.setValue((Attribute)wekaAttributes.elementAt(b), inputImage.getPixelXYBByte(x, y, b));
				}			
				// On a ajouté les valeurs pour chaque bande, on peut maintenant ajouter la valeur de la classe :
				instEx.setValue((Attribute)wekaAttributes.elementAt(bdim), output.getPixelXYBByte(x, y, 0));
				
				// Quand on arrive là on a enregistré les valeurs du pixel dans chaque bande, on ajoute notre instance à l'ensemble
				dataSet.add(instEx);
			}
		}
		
		// Enregistrement des données :
		try {
			saveData(dataSet, "./newDatasetSupervisedIndex.arff");
 		}
		catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
	
	
	
	public void CreerArffAvecClasseTexte()
	{
	    Instances dataSet = CreerDataSetObjetAvecClasse();
		
		// Enregistrement des données :
		try {
			saveData(dataSet, "./newDatasetSupervised.arff");
 		}
		catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
	
	
	public void CreerArffSansClasseTexte()
	{
	    Instances dataSet = CreerDataSetObjetSansClasse();
		
		// Enregistrement des données :
		try {
			saveData(dataSet, "./newDatasetSansClasse.arff");
 		}
		catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
	
	
	/**
	 * Sauvegarde des données dans le buffer
	 * @param L'ensemble des instances
	 */ 
	public void saveData(Instances dataSet, String localisation)throws Exception {
	{
		// Instances dataSet;
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(localisation));
		writer.write(dataSet.toString());
		writer.flush();
		writer.close();
	}}
	
	
	
	public Instances CreerDataSetObjetAvecClasse()
	{
	    Instances dataSet;
	    // Vecteur représentant une ligne = un pixel avec ses valeurs en RGB donnés par des vecteurs de niveau inférieur.
	    FastVector wekaAttributes = new FastVector();;

		// On construit le vecteur decrivant une instance ( Ici une instance est un pixel, représenté par ses intensités dans les différentes bandes composant l'image )
		Attribute Attribute1;
		
		int bdim = inputImage.getBDim();

		for(int b = 0; b < bdim; b++)
		{
			Attribute1 = new Attribute("b" + (b+1));
			// Pour chaque bande on créé un argument de type numérique qui contiendra l'intensité
			wekaAttributes.addElement(Attribute1);
		}    	
		
		
		// POUR APPRENTISSAGE SUPERVISE
		// On ajoute un attribut classe
		FastVector classVector = new FastVector();
		for (int i = 0; i < this.nomConcept.size(); i++) 
		{
			
			classVector.addElement(this.nomConcept.get(i));
		}

		
		wekaAttributes.addElement(new Attribute("class", classVector));	
		
				// WIKI WEKA : mettre des string :
				//atts.addElement(new Attribute("att3", (FastVector) null));
		
			//wekaAttributes.addElement((new Attribute("Classe", (FastVector) null)));
		
					
		int xdim = this.inputImage.getXDim();
		int ydim = this.inputImage.getYDim();
		
		// Une ligne par pixel => nbPixel = nbInstances
		int nbPixel = xdim*ydim;
		dataSet = new Instances("Les Pixels", wekaAttributes, nbPixel); 
		
		
		// POUR APPRENTISSAGE SUPERVISE
		// On définit l'attribut qui jouera le rôle de classe
		dataSet.setClassIndex(wekaAttributes.size()-1);
		
		
		Instance instEx = new Instance(wekaAttributes.size());
		
	
		for(int y = 0; y < ydim; y++)
		{
			for(int x = 0; x < xdim; x++)
			{
				if((String) this.labelComboBox.getItemAt(output.getPixelXYBByte(x, y, 0)) != "Gomme")
				{
					for(int b = 0; b < bdim; b++){
						instEx.setValue((Attribute)wekaAttributes.elementAt(b), inputImage.getPixelXYBByte(x, y, b));
					}			
					// On a ajouté les valeurs pour chaque bande, on peut maintenant ajouter la valeur de la classe :
					instEx.setValue((Attribute)wekaAttributes.elementAt(bdim), (String) this.labelComboBox.getItemAt(output.getPixelXYBByte(x, y, 0)));
					
					// Quand on arrive là on a enregistré les valeurs du pixel dans chaque bande, on ajoute notre instance à l'ensemble
					dataSet.add(instEx);
				}
			}
		}
		
		return dataSet;
	}
	
	
	
	
	public Instances CreerDataSetObjetSansClasse()
	{
	    Instances dataSet;
	    // Vecteur représentant une ligne = un pixel avec ses valeurs en RGB donnés par des vecteurs de niveau inférieur.
	    FastVector wekaAttributes = new FastVector();;

		// On construit le vecteur decrivant une instance ( Ici une instance est un pixel, représenté par ses intensités dans les différentes bandes composant l'image )
		Attribute Attribute1;
		
		int bdim = inputImage.getBDim();

		for(int b = 0; b < bdim; b++)
		{
			Attribute1 = new Attribute("b" + (b+1));
			// Pour chaque bande on créé un argument de type numérique qui contiendra l'intensité
			wekaAttributes.addElement(Attribute1);
		}    	
		
		
		FastVector classVector = new FastVector();
		for (int i = 0; i < this.nomConcept.size(); i++) 
		{
			
			classVector.addElement(this.nomConcept.get(i));
		}
		
		wekaAttributes.addElement(new Attribute("class", classVector));	
						// WIKI WEKA : mettre des string :
						//atts.addElement(new Attribute("att3", (FastVector) null));
				
						//wekaAttributes.addElement((new Attribute("Classe", (FastVector) null)));
						
					
		int xdim = this.inputImage.getXDim();
		int ydim = this.inputImage.getYDim();
		
		// Une ligne par pixel => nbPixel = nbInstances
		int nbPixel = xdim*ydim;
		dataSet = new Instances("Les Pixels", wekaAttributes, nbPixel); 
		
		
		// POUR APPRENTISSAGE SUPERVISE
		// On définit l'attribut qui jouera le rôle de classe
		dataSet.setClassIndex(wekaAttributes.size()-1);
		
		
		Instance instEx = new Instance(wekaAttributes.size());
		
	
		for(int y = 0; y < ydim; y++)
		{
			for(int x = 0; x < xdim; x++)
			{
					for(int b = 0; b < bdim; b++)
					{
						instEx.setValue((Attribute)wekaAttributes.elementAt(b), inputImage.getPixelXYBByte(x, y, b));
					}
						// On a ajouté les valeurs pour chaque bande, on peut maintenant ajouter la valeur de la classe :
					// instEx.setValue((Attribute)wekaAttributes.elementAt(bdim), (String) this.labelComboBox.getItemAt(output.getPixelXYBByte(x, y, 0)));
					
					// Quand on arrive là on a enregistré les valeurs du pixel dans chaque bande, on ajoute notre instance à l'ensemble
					dataSet.add(instEx);
			}
		}
		
		return dataSet;
	}
	
	
	/**
	 * Fonction qui initialise la ComboBox ainsi que les deux ArrayList associées avec les valeurs des concepts de base.
	 */
	public void InitialisationCB()
	{
		// On remet tout à 0.
			nomConcept.clear();
			colorConcept.clear();
			labelComboBox.removeAllItems();
		
		// On peut se servir de cette couleur qui ne représente aucun concept pour gommer sur l'écran si on le souahite ..
		labelComboBox.addItem("Gomme");
		nomConcept.add("Gomme");
		// Couleur transparente de la gomme :
		Color colorTransparente = new Color(127, 127, 127, 0);
		colorConcept.add(colorTransparente);
		display.colorMap[0][0] = colorTransparente.getRed();
		display.colorMap[0][1] = colorTransparente.getGreen();
		display.colorMap[0][2] = colorTransparente.getBlue();
		display.colorMap[0][3] = 0;
		
/*
		// Label d'exemple
		labelComboBox.addItem("Label 1");
		nomConcept.add("Label 1");
		Color premierLabelColor = new Color(255, 255, 0, 255);
		colorConcept.add(premierLabelColor); //jaune
		display.colorMap[1][0] = premierLabelColor.getRed();
		display.colorMap[1][1] = premierLabelColor.getGreen();
		display.colorMap[1][2] = premierLabelColor.getBlue();
		display.colorMap[1][3] = 255;
*/

		// On définit l'item qui sera sélectionné :		
		labelComboBox.setSelectedIndex(0);
		// On applique la couleur correspondant à l'item sélectionné
		labelColor.setBackground(new Color(display.colorMap[0][0],display.colorMap[0][1],display.colorMap[0][2]));
	}
	
	public void resetDessinPolygone()
	{
		// Reset de tout ce qui concerne la création de polygones.
		this.drawAnAreaButton.setSelected(false);
		display.nbPoints = 0;
		display.lesPoints.clear();
	}
	
	@SuppressWarnings("deprecation")
	public void ramGomme()
	{
		//Si on arrive ici, c'est que la personne a finit de gommer
		this.labelComboBox.enable(true);
		this.modifyButton.setEnabled(true);
		this.drawAnAreaButton.setEnabled(true);
		this.plusButton.setEnabled(true);
		this.undoButton.setEnabled(true);
		//this.resetButton.setEnabled(true);
		this.gommeButton.setSelected(false);
		this.gommeButton.setText("Gomme");
		display.setMarker(labelComboBox.getSelectedIndex());
		display.gomme = false;	
	}
	
	
	@SuppressWarnings("deprecation")
	public void ramPolygone()
	{
			/** On regarde si il a terminé son polygone ou non : */
			if(display.polygoneTermine == false)
			{
				display.undoPolygon();
			}
		display.drawArea = false;
		this.resetDessinPolygone();
		this.drawAnAreaButton.setText("Dessiner un polygone");
		this.labelComboBox.enable(true);
		this.jcb_lesMethodes.enable(true);
		this.modifyButton.setEnabled(true);
		this.plusButton.setEnabled(true);
		this.undoButton.setEnabled(true);
		this.resetButton.setEnabled(true);
		this.okButton.setEnabled(true);
		this.gommeButton.setEnabled(true);
	}
	
	public void reset()
	{
		// resetting
		reset = true;

		// Reset variables
		display.set(bimg);
		display.plusButtonIndex = 0;
		display.currentColorIndex[0] = (float) 1 / 255;
		
		// Reset des couleurs et de la ComboBox
		this.InitialisationCB();
		display.setMarker(labelComboBox.getSelectedIndex());
		
		this.resetDessinPolygone();
		if(display.gomme == true)
		{
			ramGomme();
		}
		
		// reset process ended
		reset = false;
	}
	
	public void ajoutConcept(String nom, Color couleur)
	{
		// Update plusButtonIndex
		display.plusButtonIndex++;	
		
		display.setMarker(display.plusButtonIndex);											
		
				display.colorMap[display.plusButtonIndex][0] = couleur.getRed();
				display.colorMap[display.plusButtonIndex][1] = couleur.getGreen();
				display.colorMap[display.plusButtonIndex][2] = couleur.getBlue();
				display.colorMap[display.plusButtonIndex][3] = 255;

		Color color = couleur;
		labelColor.setBackground(color);
	
		labelComboBox.addItem(nom);
		labelComboBox.setSelectedItem(nom);	
	}
	
	public void createOutputImage()
	{
		// Gets the marker image
		RenderedImage source = display.getMarkerImage();
		// Transform the marker image as a bufferedImage for the transformation
		BufferedImage im = ((PlanarImage) source).getAsBufferedImage();
		// raster gets the value of each pixel from im
		Raster raster = im.getData();
		// Save the type of im
		int type = im.getType();
		// Save the height of im
		int height = raster.getHeight();
		// Save the width of im
		int width = raster.getWidth();
		// Set the number of band to 1, we are looking for a greyscale image
		// without alpha channel
		int band = 1;
		
		// Instanciates output with the correct width, height and number of band
		output = new ByteImage(width, height, 1, 1, band);

		// Transfers each byte from raster to output
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				output.setPixelXYBByte(i, j, 0, (byte) raster.getSample(i, j, 0));

		// Set the color paramater
		output.setColor(false);
		// set the type parameter
		output.type = type;
	}
}
