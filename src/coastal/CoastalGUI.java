package coastal;

/**
 * Main frame of the software COASTAL
 * @author Jonathan Weber
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.media.jai.RasterFactory;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.JWindow;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.UIManager;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;


import coastal.clustering.ClusteringGUI;
import coastal.clustering.CreateArffListener;
import coastal.clustering.ImageBuilderCombo;
import coastal.clustering.ImageBuilderSimple;
import coastal.clustering.ToWekaAttributeListener;
import coastal.clustering.ToWekaExplorerListener;
import coastal.clustering.ToWekaVisualizerListener;

import com.sun.media.jai.widget.DisplayJAI;

import fr.unistra.pelican.ByteImage;
import fr.unistra.pelican.DoubleImage;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.arithmetic.AdditionChecked;
import fr.unistra.pelican.algorithms.arithmetic.Blending;
import fr.unistra.pelican.algorithms.draw.DrawCross;
import fr.unistra.pelican.algorithms.geometric.ResamplingByRatio;
import fr.unistra.pelican.algorithms.histogram.ContrastStretchEachBandWithPercentileEdgeCutting;
import fr.unistra.pelican.algorithms.io.ImageBuilder;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import fr.unistra.pelican.algorithms.io.ImageSave;
import fr.unistra.pelican.algorithms.segmentation.labels.FrontiersFromSegmentation;
import fr.unistra.pelican.algorithms.segmentation.labels.LabelsToBinaryMasks;
import fr.unistra.pelican.algorithms.segmentation.labels.MergeLabelsFromClasses;
import fr.unistra.pelican.algorithms.segmentation.MarkerBasedMultiProbashed;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;
import fr.unistra.pelican.util.remotesensing.HdrReader;

public class CoastalGUI extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = -5751006610363081947L;

	public static final int ONLY_STRICT = 0;
	public static final int BOTH = 1;
	
	private Image satellite=null;
	private Image displayed;
	private Image reference=null;
	private Image resultFusion=null;
	private Image satToDisplay;
	private ByteImage watershedMarkers=null;
	
	private Image refColorImage=null;
	
	private Color refColor = null;

	
		public Image monImage;
		public JComboBox lesConcepts;
	
	
	private ArrayList<Image> mHMTResults;
	private ArrayList<LinearObjectDefinition> mHMTResultDefinitions;
	private ArrayList<Image> mHMTResultsColorImage;
	private ArrayList<Color> mHMTResultColors;
	private ArrayList<Integer> mHMTResultType;
	
	private ArrayList<Image> processResults;
	private ArrayList<String> processNames;
	private ArrayList<Color> processColors;
	private ArrayList<Image> processColorImages;
	
	private String satelliteFile;
	
	private ArrayList<String> bandLabels;
	
	private DisplayJAI[] display;
	private RandomIter[] readIterator;
	
	private JTextField coordValue;
	
	private JMenuBar jmb = new JMenuBar();
	
	private JMenu file = new JMenu("File");
	private JMenuItem fileopen = new JMenuItem("Open file");
	private JMenuItem refopen = new JMenuItem("Import reference");
	private JMenuItem resimport = new JMenuItem("Import old result");
	private JMenuItem exporttiff = new JMenuItem("Export results");
	private JMenuItem suppr = new JMenuItem("Delete results");
	private JMenuItem console = new JMenuItem("Display console");	
	private JMenuItem quit = new JMenuItem("Quit");
	
	private JMenu vizu = new JMenu("Display");
	private JMenu vband = new JMenu("Single Band");
	private JMenuItem vcolor = new JMenuItem("Multi Band");
	private JMenu vzoom = new JMenu("Zoom");
	private JMenuItem vzoomquarter = new JMenuItem("X 1/4");
	private JMenuItem vzoomhalf = new JMenuItem("X 1/2");
	private JMenuItem vzoomnorm = new JMenuItem("X 1");
	private JMenuItem vzoom2 = new JMenuItem("X 2");
	private JMenuItem vzoom4 = new JMenuItem("X 4");
	private JMenuItem vzoom8 = new JMenuItem("X 8");
	private JMenuItem stretch = new JMenuItem("Stretch Bands");
	private JMenuItem superposition = new JMenuItem("Superposition");

	
	private JMenu bands = new JMenu("Bands");
	private JMenuItem addBand = new JMenuItem("Add band");
	private JMenu delBand = new JMenu("Erase band");
	private JMenuItem convert = new JMenuItem("Convert to 8 bits");
	
	
	private JMenu objects = new JMenu("MHMT");
	private JMenu definition = new JMenu("Object Definition");
	private JMenu add = new JMenu("Add");	
	private JMenuItem linear = new JMenuItem("Linear");
	private JMenu modify = new JMenu("Modify");	
	private JMenu delete = new JMenu("Delete");	
	private JMenuItem importObj = new JMenuItem("Import");
	private JMenuItem exportObj = new JMenuItem("Export");
	
	private JMenuItem extraction = new JMenuItem("Extraction");
	
	private JMenu markerBased = new JMenu("Marker-Based");
	private JMenuItem loadLabels = new JMenuItem("Load labels");
	private JMenuItem saveLabels = new JMenuItem("Save labels");
	private JMenuItem reinitLabels = new JMenuItem("Re-init labels");
	private JMenuItem editLabels = new JMenuItem("Edit labels and segment");
		
	
	private JMenu classification = new JMenu("Classification");
	private JMenuItem createArff = new JMenuItem("Create an arff file");
	private JMenuItem toWeka = new JMenuItem("Weka Explorer");
	private JMenuItem toWeka3 = new JMenuItem("Weka Attribute Summarizer");
	private JMenuItem clustering = new JMenuItem("Clustering");
	private JMenuItem toWeka2 = new JMenuItem("Weka Visualizer");
	private JMenu roi = new JMenu("Supervised Learning");
	private JMenuItem roiTools = new JMenuItem("Draw Class ...");
	private JMenuItem newModul = new JMenuItem("New Module ...");
	
	
	private JMenu process = new JMenu("Process");
	private JMenu coastline = new JMenu("Coastline");
	private JMenuItem bagli = new JMenuItem("Bagli");
	private JMenuItem erteza = new JMenuItem("Erteza");
	private JMenuItem heene = new JMenuItem("Heene");
	private JMenuItem jishuang = new JMenuItem("Jishuang");
	private JMenuItem thresh = new JMenuItem("Thresholding Borders");
	private JMenuItem edge = new JMenuItem("Edge detection");
	
	private JMenu evaluation = new JMenu("Evaluation");
	private JMenuItem performanceIndex = new JMenuItem("Performance Index");
	private JMenuItem confusionMatrix = new JMenuItem("Confusion Matrix");
		
	private JMenu help = new JMenu("Help");
	private JMenuItem about = new JMenuItem("About");
	
	private JScrollPane scroll;
	private JPanel root;
	
	private MouseHandler mouseHandler;
	
	private boolean color;
	private boolean contrastStretch=false;
	private boolean displaySatellite=true;
	
	private Double zoom;
	
	private Point xy=null;
	private Point xy2=null;
	
	private int[] colorVector;
	private int bandVisible;
	private int transparency;
	private int percentStretchCut=2;
	private int processCount=0;
	
	private ArrayList<LinearObjectDefinition> linearObj;
	
	private String IOPath = System.getProperty("user.dir" );
	
	/**
	 * Constructor
	 */
	private CoastalGUI()
	{
		
		linearObj = new ArrayList<LinearObjectDefinition>();
		mHMTResults = new ArrayList<Image>();
		mHMTResultDefinitions = new ArrayList<LinearObjectDefinition>();
		mHMTResultColors = new ArrayList<Color>();
		mHMTResultsColorImage = new ArrayList<Image>();
		mHMTResultType = new ArrayList<Integer>();
		bandLabels = new ArrayList<String>();
		transparency=0;
		processResults = new ArrayList<Image>();
		processNames = new ArrayList<String>();
		processColors = new ArrayList<Color>();
		processColorImages = new ArrayList<Image>();
		
		
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Coastal Extractor");
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle bounds = env.getMaximumWindowBounds();
		bounds.height=bounds.height/2;
		bounds.width=bounds.width/2;
		this.setBounds(bounds);
		
		
		about.addActionListener(this);
		quit.addActionListener(this);
		fileopen.addActionListener(this);
		vcolor.addActionListener(this);
		vzoomquarter.addActionListener(this);
		vzoomhalf.addActionListener(this);
		vzoomnorm.addActionListener(this);
		vzoom2.addActionListener(this);
		vzoom4.addActionListener(this);
		vzoom8.addActionListener(this);
		bagli.addActionListener(this);
		heene.addActionListener(this);
		jishuang.addActionListener(this);
		thresh.addActionListener(this);
		erteza.addActionListener(this);
		edge.addActionListener(this);
		loadLabels.addActionListener(this);
		saveLabels.addActionListener(this);
		reinitLabels.addActionListener(this);
		editLabels.addActionListener(this);
		linear.addActionListener(this);
		stretch.addActionListener(this);
		exporttiff.addActionListener(this);
		extraction.addActionListener(this);
		exportObj.addActionListener(this);
		importObj.addActionListener(this);
		addBand.addActionListener(this);
		convert.addActionListener(this);
		refopen.addActionListener(this);
		superposition.addActionListener(this);
		performanceIndex.addActionListener(this);
		confusionMatrix.addActionListener(this);
		console.addActionListener(this);
		suppr.addActionListener(this);
		resimport.addActionListener(this);
		toWeka.addActionListener(new ToWekaExplorerListener(this));
		clustering.addActionListener(this);
		toWeka3.addActionListener(new ToWekaAttributeListener(this));
		toWeka2.addActionListener(new ToWekaVisualizerListener(this));
		createArff.addActionListener(new CreateArffListener(this));
		classification.addActionListener(this);
		roiTools.addActionListener(this);
		newModul.addActionListener(this);

		
		file.add(fileopen);
		file.add(refopen);
		file.add(resimport);
		file.add(exporttiff);
		file.add(suppr);
		file.add(console);
		file.add(quit);
		jmb.add(file);
		vizu.add(vband);
		vizu.add(vcolor);
		vzoom.add(vzoomquarter);
		vzoom.add(vzoomhalf);
		vzoom.add(vzoomnorm);
		vzoom.add(vzoom2);
		vzoom.add(vzoom4);
		vzoom.add(vzoom8);
		vizu.add(vzoom);
		vizu.add(stretch);
		vizu.add(superposition);
		jmb.add(vizu);
		bands.add(addBand);
		bands.add(delBand);
		bands.add(convert);
		jmb.add(bands);
		add.add(linear);
		definition.add(add);
		definition.add(modify);
		definition.add(delete);
		definition.add(importObj);
		definition.add(exportObj);
		objects.add(definition);
		objects.add(extraction);
		jmb.add(objects);
		markerBased.add(loadLabels);
		markerBased.add(saveLabels);
		markerBased.add(reinitLabels);
		markerBased.add(editLabels);
		jmb.add(markerBased);
		coastline.add(bagli);
		coastline.add(heene);
		coastline.add(jishuang);
		coastline.add(erteza);
		process.add(coastline);
		process.add(thresh);
		process.add(edge);
		jmb.add(process);
		classification.add(createArff);
		classification.add(toWeka);
		classification.add(toWeka2);
		classification.add(toWeka3);
		classification.add(clustering);
		roi.add(roiTools);
		roi.add(newModul);
		classification.add(roi);
		jmb.add(classification);
		evaluation.add(performanceIndex);
		evaluation.add(confusionMatrix);
		jmb.add(evaluation);
		help.add(about);
		jmb.add(help);
		this.setJMenuBar(jmb);
		
		
		
		/* look and feel initialization */
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    SwingUtilities.updateComponentTreeUI(this);
		}
		catch (Exception ex)
		{
		    System.err.println("LookAndFeelException : "+ex.getMessage());
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		
		// About
		if(((JMenuItem) e.getSource()).getText()=="About")
			JOptionPane.showMessageDialog(this,
				    "Jonathan Weber\nANR JC ECOSGIL 2005-2008\nhttp://ecosgil.u-strasbg.fr\njonathan.weber@iutsd.uhp-nancy.fr \nVersion 0.998 (11/23/11)",
				    "About COASTAL",
				    JOptionPane.PLAIN_MESSAGE);
		
		// Bouton du menu qui permet de lancer l'IHM de clustering
		if(((JMenuItem) e.getSource()).getText()=="Clustering")
		{
			if(satellite==null)
			{
				JOptionPane.showMessageDialog(this,
				    "You must load a picture first",
				    "Alert",
				    JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				JFrame ihm = new ClusteringGUI("Samples", this, this.satellite);
				ihm.setSize(275, 200);
				ihm.setVisible(true);
				ihm.setAlwaysOnTop(true);
			}
		}
				
		if(((JMenuItem) e.getSource()).getText()=="Draw Class ...")
		{
			if(satellite==null)
			{
				JOptionPane.showMessageDialog(this,
						"You must load a picture first",
						"Alert",
						JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				new ImageBuilderSimple().process(satellite,"Draw your Class",satToDisplay);
			}
		}
				
		if(((JMenuItem) e.getSource()).getText()=="New Module ...")
		{
			if(satellite==null)
			{
				JOptionPane.showMessageDialog(this,
					"You must load a picture first",
					"Alert",
				    JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				new ImageBuilderCombo().process(satellite,"Draw your Class",satToDisplay);
			}
		}
		
		// Quit
		if(((JMenuItem) e.getSource()).getText()=="Quit")
			this.dispose();
		// Open File
		if(((JMenuItem) e.getSource()).getText()=="Open file")
			this.openFile();
		// Vizualisation by band
		if(((JMenuItem) e.getSource()).getName()=="disp")
			this.vizuBand(((JMenuItem) e.getSource()).getText());
		// Colorized Vizualisation
		if(((JMenuItem) e.getSource()).getText()=="Multi Band")
			if(satellite!=null)
				if(satellite.getBDim()>=3)
					ColorizedPanel.getInstance(this);
		//Zooms
		if(((JMenuItem) e.getSource()).getText()=="X 1/4")
		{
			zoom=0.25;
			this.displayImage();
		}
		if(((JMenuItem) e.getSource()).getText()=="X 1/2")
		{
			zoom=0.5;
			this.displayImage();
		}
		if(((JMenuItem) e.getSource()).getText()=="X 1")
		{
			zoom=1.;
			this.displayImage();
		}
		if(((JMenuItem) e.getSource()).getText()=="X 2")
		{
			zoom=2.;
			this.displayImage();
		}
		if(((JMenuItem) e.getSource()).getText()=="X 4")
		{
			zoom=4.;
			this.displayImage();
		}
		if(((JMenuItem) e.getSource()).getText()=="X 8")
		{
			zoom=8.;
			this.displayImage();
		}
		//Bagli
		if((JMenuItem) e.getSource()==bagli)
			if(satellite!=null)
					BagliPanel.getInstance(this);
		//Heene
		if((JMenuItem) e.getSource()==heene)
			if(satellite!=null)
					HeenePanel.getInstance(this);
		//Jishuang
		if((JMenuItem) e.getSource()==jishuang)
			if(satellite!=null)
					JishuangPanel.getInstance(this);
		//Thresholding
		if((JMenuItem) e.getSource()==thresh)
			if(satellite!=null)
					ThresholdingPanel.getInstance(this);
		//Erteza
		if((JMenuItem) e.getSource()==erteza)
			if(satellite!=null)
					ErtezaPanel.getInstance(this);
		//Edge detection
		if(((JMenuItem) e.getSource()).getText()=="Edge detection")
			if(satellite!=null)
				EdgeDetectionPanel.getInstance(this);
		//Marker
		if(((JMenuItem)e.getSource())==loadLabels&&satellite!=null)
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Choose the markers to load");
			chooser.setCurrentDirectory(new File(this.getIOPath()));
			
			int returnVal = chooser.showOpenDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) 
		    {
		    	this.setIOPath(chooser.getCurrentDirectory().getAbsolutePath());
		    	System.out.println("You choose to load markers : " + chooser.getCurrentDirectory()+File.separator+
		            chooser.getSelectedFile().getName());
		    	watershedMarkers = (ByteImage) ImageLoader.exec(chooser.getCurrentDirectory()+File.separator+
			            chooser.getSelectedFile().getName());
		    }		    
		}
		
		if(((JMenuItem)e.getSource())==saveLabels&&satellite!=null)
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Choose where to save markers");
			chooser.setApproveButtonText("Save");
			chooser.setCurrentDirectory(new File(this.getIOPath()));
			
			int returnVal = chooser.showOpenDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) 
		    {
		    	this.setIOPath(chooser.getCurrentDirectory().getAbsolutePath());
		    	System.out.println("You save markers here : " + chooser.getCurrentDirectory()+File.separator+
		            chooser.getSelectedFile().getName());
		    	if(watershedMarkers!=null)
		    		ImageSave.exec(watershedMarkers, chooser.getCurrentDirectory()+File.separator+
			            chooser.getSelectedFile().getName() );
		    }		    
		}
		if(((JMenuItem)e.getSource())==reinitLabels&&satellite!=null)
			watershedMarkers=null;
		if(((JMenuItem)e.getSource())==editLabels&&satellite!=null)
		{
			watershedMarkers = ImageBuilder.exec(satToDisplay,"Set your markers",watershedMarkers);
			this.increaseProcessCount();
			Image samples = LabelsToBinaryMasks.exec(watershedMarkers);
			Image mbw = MarkerBasedMultiProbashed.exec(satellite,samples);
			mbw = MergeLabelsFromClasses.exec(mbw,watershedMarkers.copyToIntegerImage());
			mbw = FrontiersFromSegmentation.exec(mbw);
			this.addProcessResult(mbw, "Marker-Based Watershed");
		}
		
		//Add linear objects definition
		if(((JMenuItem) e.getSource()).getText()=="Linear")
			LinearObjectPanel.getInstance(this);
		
		//Stretch and unstretch bands for vizu
		if((JMenuItem) e.getSource()== stretch)
		{
			if(contrastStretch==true)
			{
				contrastStretch = false;
				stretch.setText("Stretch Bands");
				this.displayImage();
			}
			else
			{
				StretchPanel.getInstance(this);
			}
		}
		//Modify SE
		if(((JMenuItem) e.getSource()).getName()=="modify")
		{
			String t=((JMenuItem) e.getSource()).getText();
			if(t.charAt(0)=='L')
			{
				String[] split = t.split("L");
				String[] split2 = split[1].split(":");
				LinearObjectPanel.getInstance(this,Integer.parseInt(split2[0])-1);
			}
		}
		//Delete SE
		if(((JMenuItem) e.getSource()).getName()=="delete")
		{
			String t=((JMenuItem) e.getSource()).getText();
			if(t.charAt(0)=='L')
			{
				String[] split = t.split("L");
				String[] split2 = split[1].split(":");
				this.deleteLinearObject(Integer.parseInt(split2[0])-1);
			}
		}
		
		//Extraction
		if(((JMenuItem) e.getSource()).getText()=="Extraction")
			ExtractionPanel.getInstance(this);
		//Export objects
		if(((JMenuItem) e.getSource()).getText()=="Export")
			SaveObjectPanel.getInstance(this);
		//Import objects
		if(((JMenuItem) e.getSource()).getText()=="Import")
			this.importObjects();
		//Add band
		if(((JMenuItem) e.getSource()).getText()=="Add band")
			if(satellite!=null)
				AddBandPanel.getInstance(this);
		//Delete band
		if(((JMenuItem) e.getSource()).getName()=="del")
			this.delBand(((JMenuItem) e.getSource()).getText());
		// Convert
		if(((JMenuItem) e.getSource()).getText()=="Convert to 8 bits")
			this.convert();
		//Import .tiff
		if(((JMenuItem) e.getSource()).getText()=="Import reference")
			this.openRef();
		//Superposition
		if(((JMenuItem) e.getSource()).getText()=="Superposition")
			SuperpositionPanel.getInstance(this);
		//Exporttiff
		if((JMenuItem) e.getSource()==exporttiff)
			if(satellite!=null)
				ExportPanel.getInstance(this);
		//Performance Index
		if((JMenuItem) e.getSource()==performanceIndex)
			if(satellite!=null&&reference!=null&&(processResults.size()!=0||mHMTResults.size()!=0))
				PerformanceIndexPanel.getInstance(this);
		//Performance Index
		if((JMenuItem) e.getSource()==confusionMatrix)
			if(satellite!=null&&reference!=null&&(processResults.size()!=0||mHMTResults.size()!=0))
				ConfusionMatrixPanel.getInstance(this);
		//Console
		if((JMenuItem) e.getSource()==console)
			ConsolePanel.getInstance();
		//Results Suppression
		if((JMenuItem) e.getSource()==suppr)
			ResultSuppressionPanel.getInstance(this);
		//Results import
		if((JMenuItem) e.getSource()==resimport)
			if(satellite!=null)
				this.importResult();
			
	}
	
	/**
	 * Method for opening reference file
	 */
	private void openRef() {
		
		if(satellite!=null)
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Choose the tiff to load");
			chooser.setCurrentDirectory(new File(this.getIOPath()));
		
			int returnVal = chooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				this.setIOPath(chooser.getCurrentDirectory().getAbsolutePath());
				System.out.println("You choose to load reference: " + chooser.getCurrentDirectory()+File.separator+
						chooser.getSelectedFile().getName());
				Image ref = (Image) new ImageLoader().process(chooser.getCurrentDirectory()+File.separator+
						chooser.getSelectedFile().getName());
				if(ref.getBDim()==1)
					if(ref.getXDim()== satellite.getXDim()&& ref.getYDim()== satellite.getYDim())
						this.setReference(ref);
					else
						JOptionPane.showMessageDialog(this,
		   				    "Reference picture must have same dimensions as satellite picture",
		   				    "Dimension error",
		   				    JOptionPane.ERROR_MESSAGE);
				else
					JOptionPane.showMessageDialog(this,
		   				    "This is not a reference file !",
		   				    "Bad format",
		   				    JOptionPane.ERROR_MESSAGE);
				
			}
		}
		else
		{
			JOptionPane.showMessageDialog(this,
   				    "You must load a satellite picture before loading a reference",
   				    "Import error",
   				    JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Method for importing objects definition
	 */
	private void importObjects() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Choose the objects file to import");
		chooser.setCurrentDirectory(new File(this.getIOPath()));
		
		int returnVal = chooser.showOpenDialog(this);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	this.setIOPath(chooser.getCurrentDirectory().getAbsolutePath());
	       System.out.println("You choose to import objects from : " + chooser.getCurrentDirectory()+File.separator+
	            chooser.getSelectedFile().getName());
	       int lu;
           String load=new String();

           try{
        	   FileReader fr = new FileReader(chooser.getSelectedFile().
        			   getAbsolutePath());
        	   do
        	   {
        		   lu = fr.read();
        		   if (lu != -1)
        		   {
        			   load=load+String.valueOf((char) lu);
        		   }
        	   } while (lu != -1);
        	   fr.close();
           }
           catch (FileNotFoundException ex) {} catch (HeadlessException ex) {} catch (
                   IOException ex) {}
           
           
           String[] objects = load.split("\n\n\n");
           if(objects[0].equals("Linear Objects"))
           {
        	   for(int i=1;i<objects.length;i++)
        	   	   this.addLinearObjectDefinition(LinearObjectDefinition.fromString(objects[i]));
           }
           else
           {
        	   JOptionPane.showMessageDialog(this,
   				    "Bad File Format",
   				    "Import error",
   				    JOptionPane.ERROR_MESSAGE);
           }
	    }
		
	}

	/**
	 * Method for opening satellite file
	 *
	 */
	public void openFile()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Choose the satellite picture to load");
		chooser.setCurrentDirectory(new File(this.getIOPath()));
		
		int returnVal = chooser.showOpenDialog(this);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	this.setIOPath(chooser.getCurrentDirectory().getAbsolutePath());
	       System.out.println("You choose to load : " + chooser.getCurrentDirectory()+File.separator+
	            chooser.getSelectedFile().getName());
	       this.setTitle("Coastal Extractor : "+chooser.getSelectedFile().getName());
	       Image tmp = (Image)new ImageLoader().process(chooser.getCurrentDirectory()+File.separator+chooser.getSelectedFile().getName());
	       linearObj = new ArrayList<LinearObjectDefinition>();
	       mHMTResults = new ArrayList<Image>();
	       mHMTResultDefinitions = new ArrayList<LinearObjectDefinition>();
	       mHMTResultColors = new ArrayList<Color>();
	       mHMTResultsColorImage = new ArrayList<Image>();
	       mHMTResultType = new ArrayList<Integer>();
	   	   reference=null;
	   	   resultFusion=null;
	   	   refColorImage=null;
	   	   refColor = null;
	   	   processResults = new ArrayList<Image>();
	   	   processNames = new ArrayList<String>();
	   	   processColors = new ArrayList<Color>();
	   	   processColorImages = new ArrayList<Image>();
	       
	       bandLabels = new ArrayList<String>();
	       transparency=0;
	       bandLabels.clear();
	       satelliteFile=chooser.getSelectedFile().getName();
	       if(chooser.getSelectedFile().getName().endsWith(".hdr"))
	       {
	    	   HdrReader hdr = new HdrReader();
	    	   hdr.readHeader(chooser.getCurrentDirectory()+File.separator+chooser.getSelectedFile().getName());
	    	   String[] bandNames = hdr.getBandNames();
	    	   for(int i=0;i<tmp.getBDim();i++)
	    		   bandLabels.add("Band "+(i+1)+":"+bandNames[i]);	    	   
	       }
	       else
	       {
	    	   for(int i=0;i<tmp.getBDim();i++)
	    		   bandLabels.add("Band "+(i+1)+":");
	       }
	       this.setSatellite(tmp);
	    }
	}
	
	/**
	 * Method for selecting a single band for the vizualisation
	 * @param String label of the band in the menu
	 */
	public void vizuBand(String str)
	{
		String[] split = str.split("Band ");
		split = split[1].split(":");
		int b = Integer.parseInt(split[0]);
		color = false;
		bandVisible = b-1;
		this.makeSatToDisplay();
		
	}
	
	/**
	 * Method for displaying the picture in the GUI
	 *
	 */
	public void displayImage()
	{
			if(root!=null)
			{
				root.removeAll();
			}
			// transform the image into an array of BufferedImages		
	       final int tdim = 1;
	       final int zdim = 1;
	       DataBufferByte dbb;
	       SampleModel s;
	       Raster r;
	       BufferedImage bimg = null;
	       final int bdim = 3;	       
	       displayed=satToDisplay.copyImage(true);
			if(resultFusion!=null)
				displayed=(Image) new Blending().process(displayed,resultFusion,transparency/100.,true);
		  	if(zoom!=1)
		   		   displayed=(Image) new ResamplingByRatio().process(displayed,zoom,zoom,1.0,1.0,1.0,ResamplingByRatio.NEAREST);
		  	if(xy!=null)
		  	{
		  		ArrayList<Point> crosses = new ArrayList<Point>();
		  		crosses.add(xy);
		  		if(xy2!=null)
		  			crosses.add(xy2);
		  		displayed=DrawCross.exec(displayed,crosses,3,Color.BLACK);
		  	}		  	
		  	display = new DisplayJAI[tdim * zdim];
			readIterator = new RandomIter[tdim * zdim];
			int[] bandOffsets = {0,1,2};
				
			for(int t = 0; t < tdim; t++){
				for(int z = 0; z < zdim; z++){
			
					Image tmp = displayed;
			
					byte[] tmp2 = new byte[tmp.size()];
					for(int i = 0; i < tmp.size(); i++)
						tmp2[i] = (byte)tmp.getPixelByte(i);
			
					dbb = new DataBufferByte(tmp2,tmp.size());
					s = RasterFactory.createPixelInterleavedSampleModel(DataBuffer.TYPE_BYTE,tmp.getXDim(),tmp.getYDim(),bdim,bdim * tmp.getXDim(),bandOffsets);
					r = RasterFactory.createWritableRaster(s,dbb,new Point(0,0));
					bimg = new BufferedImage(tmp.getXDim(),tmp.getYDim(),BufferedImage.TYPE_3BYTE_BGR);
					bimg.setData(r);
						
					display[t * zdim + z] = new DisplayJAI(bimg);
					readIterator[t * zdim + z] = RandomIterFactory.create(bimg,null);
				}
			}
			
			root = new JPanel();

			Toolkit k = Toolkit.getDefaultToolkit();
			Dimension tailleEcran = k.getScreenSize();
			root.setPreferredSize(new Dimension(Math.min(tailleEcran.width-3,satellite.getXDim() + 3),Math.min(tailleEcran.height-81,satellite.getYDim() + 81)));
			root.setLayout(new BorderLayout());
			this.setContentPane(root);
			scroll = new JScrollPane(display[0],ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		 	ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			root.add(scroll,BorderLayout.CENTER);
			coordValue = new JTextField("Coordinates initialized");
			coordValue.setEnabled(false);
			root.add(coordValue,BorderLayout.SOUTH);
			mouseHandler = new MouseHandler(this);
			
			scroll.addMouseListener(mouseHandler);
			scroll.addMouseMotionListener(mouseHandler);
						
			this.setVisible(true);
	}
	
	/**
	 * Method which concats all the results in a single picture with their selected colors.
	 *
	 */
	public void makeResultsFusion()
	{
		resultFusion = null;
		if(refColor!=null)
			resultFusion=refColorImage;
		for(int i=0;i<processColors.size();i++)
			if(processColors.get(i)!=null)
				if(resultFusion==null)
					resultFusion=processColorImages.get(i);
				else
					resultFusion=(Image) new AdditionChecked().process(resultFusion,processColorImages.get(i));
		for(int i=0;i<mHMTResultColors.size();i++)
			if(mHMTResultColors.get(i)!=null)
				if(resultFusion==null)
					resultFusion=mHMTResultsColorImage.get(i);
				else
					resultFusion=(Image) new AdditionChecked().process(resultFusion,mHMTResultsColorImage.get(i));
		
		this.displayImage();
	}
	
	/**
	 * Method that color the reference in the chosen color
	 *
	 */
	public void makeRefColorImage()
	{
		if(refColor==null)
			refColorImage=null;
		else
		{
			refColorImage = new DoubleImage(satellite.getXDim(),satellite.getYDim(),1,1,3);
			refColorImage.fill(0.);
			for(int x=0;x<refColorImage.getXDim();x++)
				for(int y=0;y<refColorImage.getYDim();y++)
					if(reference.getPixelXYDouble(x,y)>0.)
					{
						refColorImage.setPixelByte(x,y,0,0,0,refColor.getRed());
						refColorImage.setPixelByte(x,y,0,0,1,refColor.getGreen());
						refColorImage.setPixelByte(x,y,0,0,2,refColor.getBlue());
					}
						
		}
		this.makeResultsFusion();
	}
	
	/**
	 * Method that make MHMT result in color image
	 * @param int index of the MHMT result to make in color image
	 */
	public void makeMHMTResultsColorImage(int i)
	{
		if(mHMTResultColors.get(i)==null)
			mHMTResultsColorImage.set(i,null);
		else
		{
			Image resTmp = mHMTResults.get(i);
			Image tmp= new DoubleImage(satellite.getXDim(),satellite.getYDim(),1,1,3);
			tmp.fill(0.);
			Color c = mHMTResultColors.get(i);
			for(int x=0;x<tmp.getXDim();x++)
				for(int y=0;y<tmp.getYDim();y++)
					if(resTmp.getPixelXYDouble(x,y)>0.)
					{
						tmp.setPixelByte(x,y,0,0,0,c.getRed());
						tmp.setPixelByte(x,y,0,0,1,c.getGreen());
						tmp.setPixelByte(x,y,0,0,2,c.getBlue());
					}
			mHMTResultsColorImage.set(i,tmp);
		}
		this.makeResultsFusion();
	}
	
	/**
	 * Method that make processes results in color image
	 * @param int index of the process result to make in color image
	 */
	public void makeProcessResultsColorImage(int i)
	{
		if(processColors.get(i)==null)
			processColorImages.set(i, null);
		else
		{
			Image resTmp = processResults.get(i);
			Image tmp= new DoubleImage(satellite.getXDim(),satellite.getYDim(),1,1,3);
			tmp.fill(0.);
			Color c = processColors.get(i);
			for(int x=0;x<tmp.getXDim();x++)
				for(int y=0;y<tmp.getYDim();y++)
					if(resTmp.getPixelXYDouble(x,y)>0.)
					{
						tmp.setPixelByte(x,y,0,0,0,c.getRed());
						tmp.setPixelByte(x,y,0,0,1,c.getGreen());
						tmp.setPixelByte(x,y,0,0,2,c.getBlue());
					}
			processColorImages.set(i, tmp);
		}
		this.makeResultsFusion();
	}
	
	
	
	/**
	 * Main class
	 * @param args
	 */
	public static void main(String[] args) {
		new CoastalGUI().setVisible(true);
	}

	public Image getDisplayed() {
		return displayed;
	}

	public void setDisplayed(Image displayed) {
		this.displayed = displayed;
	}

	public Image getSatellite() {
		return satellite;
	}

	/**
	 * Method that set the satellite picture
	 * @param Image satellite picture
	 */
	public void setSatellite(Image satellite) {
		if(this.satellite!=null&&!Image.haveSameDimensions(satellite, this.satellite))
		{
			this.setReference(null);
			mHMTResults = new ArrayList<Image>();
			mHMTResultDefinitions = new ArrayList<LinearObjectDefinition>();
		}
		this.satellite = satellite;
		if(satellite.getBDim()==3)
	       {
	    	   color=true;
	    	   colorVector = new int[3];
	    	   colorVector[0]=0;
	    	   colorVector[1]=1;
	    	   colorVector[2]=2;
	       }else
	       {
	    	   color=false;
	    	   bandVisible=0;
	       }
	       zoom=1.;
	       this.makeSatToDisplay();
	       vband.removeAll();
	       delBand.removeAll();
	       for(int i=0;i<satellite.getBDim();i++)
	       {
	    	   JMenuItem btmp = new JMenuItem(bandLabels.get(i));
	    	   btmp.addActionListener(this);
	    	   btmp.setName("disp");
	    	   vband.add(btmp);
	    	   JMenuItem btmp2 = new JMenuItem(bandLabels.get(i));
	    	   btmp2.addActionListener(this);
	    	   btmp2.setName("del");
	    	   delBand.add(btmp2);
	       }
	       displaySatellite=true;
	}

	public void makeSatToDisplay() {
		
		if(displaySatellite)
	       {
	    	   if(color)
	    	   {
	    		   satToDisplay=new ByteImage(satellite.getXDim(), satellite.getYDim(), 1, 1, 3);
	    		   satToDisplay.setImage4D(satellite.getImage4D(colorVector[0], Image.B), 0, Image.B);
	    		   satToDisplay.setImage4D(satellite.getImage4D(colorVector[1], Image.B), 1, Image.B);
	    		   satToDisplay.setImage4D(satellite.getImage4D(colorVector[2], Image.B), 2, Image.B);
	    	   }
	    	   else
	    		   satToDisplay=satellite.duplicateDimension(bandVisible, 3, Image.B);
	    	   if(contrastStretch)
		   		   satToDisplay =(Image) new ContrastStretchEachBandWithPercentileEdgeCutting().process(satToDisplay,percentStretchCut/100.);
	       }
	       else
	       {
	    	   satToDisplay=new ByteImage(satellite.getXDim(), satellite.getYDim(), 1, 1, 3);
	    	   satToDisplay.fill(0.0);
	       }
		
		this.displayImage();
	}

	public int getBandVisible() {
		return bandVisible;
	}

	public void setBandVisible(int bandVisible) {
		this.bandVisible = bandVisible;
	}

	public boolean isColor() {
		return color;
	}

	public void setColor(boolean color) {
		this.color = color;
	}

	public int[] getColorVector() {
		return colorVector;
	}

	public void setColorVector(int[] colorVector) {
		this.colorVector = colorVector;
	}

	public void addLinearObjectDefinition(LinearObjectDefinition lod) {
		linearObj.add(lod);
		this.updateObjMenu();
		this.updatePanelInstances();
	}
	


	private void deleteLinearObject(int i) {
		linearObj.remove(i);
		this.updateObjMenu();
		this.updatePanelInstances();
	}

	/**
	 * Method that update the objects menu
	 *
	 */
	public void updateObjMenu()
	{
		modify.removeAll();
		for(int i=0;i<linearObj.size();i++)
		{
			JMenuItem tmp = new JMenuItem("L"+(i+1)+":"+linearObj.get(i).getName());
			tmp.setName("modify");
			tmp.addActionListener(this);
			modify.add(tmp);
		}		
		delete.removeAll();
		for(int i=0;i<linearObj.size();i++)
		{
			JMenuItem tmp = new JMenuItem("L"+(i+1)+":"+linearObj.get(i).getName());
			tmp.setName("delete");
			tmp.addActionListener(this);
			delete.add(tmp);
		}
	}

	public ArrayList<LinearObjectDefinition> getLinearObj() {
		return linearObj;
	}

	public void modifyLinearObjectDefinition(LinearObjectDefinition lod, int index) {
		linearObj.set(index, lod);
		this.updateObjMenu();
	}

	public void addmHMTResults(Image result, LinearObjectDefinition lod, int type) {
		mHMTResults.add(result);
		mHMTResultDefinitions.add(lod);
		mHMTResultColors.add(null);
		mHMTResultsColorImage.add(null);
		mHMTResultType.add(Integer.valueOf(type));
		this.decreaseProcessCount();
		displayResult(result, lod.getName());
	}
	
	public void addProcessResult(Image result, String name)
	{
		processResults.add(result);
		processNames.add(name);
		processColors.add(null);
		processColorImages.add(null);
		this.decreaseProcessCount();
		displayResult(result, name);
	}

	public void displayResult(Image result, String resultName)
	{
		Viewer2D.exec(result, resultName);
		this.updatePanelInstances();
	}


	public ArrayList<Image> getProcessResults() {
		return processResults;
	}

	public ArrayList<String> getProcessNames() {
		return processNames;
	}

	public ArrayList<Color> getProcessColors() {
		return processColors;
	}

	public ArrayList<Image> getProcessColorImages() {
		return processColorImages;
	}

	/**
	 * Method that add a band to the satellite picture
	 * @param Image new band to add
	 * @param String name of the band
	 */
	public void addBand(Image newBand,String nBand) {
		//Image tmp = new ByteImage(satellite.getXDim(), satellite.getYDim(), 1, 1, satellite.getBDim()+1);
		Image tmp = satellite.newInstance(satellite.getXDim(), satellite.getYDim(), 1, 1, satellite.getBDim()+1);
		for(int i=0;i<satellite.getBDim();i++)
			tmp.setImage4D(satellite.getImage4D(i, Image.B), i, Image.B);
		tmp.setImage4D(newBand, satellite.getBDim(), Image.B);
		bandLabels.add("Band "+tmp.getBDim()+":"+nBand);
		this.setSatellite(tmp);		
	}
	
	/**
	 * Method that erase a band of the satellite picture
	 * @param String name of the band in the menu
	 */
	public void delBand(String str)
	{
		if(satellite.getBDim()!=1)
		{
			String[] split = str.split("Band ");
			split = split[1].split(":");
			int index = Integer.parseInt(split[0])-1;
			bandLabels.remove(index);
			//Image tmp = new ByteImage(satellite.getXDim(), satellite.getYDim(), 1, 1, satellite.getBDim()-1);
			Image tmp = satellite.newInstance(satellite.getXDim(), satellite.getYDim(), 1, 1, satellite.getBDim()-1);
			int counter=0;
			for(int i=0;i<satellite.getBDim();i++)
				if(i!=index)
				{
					tmp.setImage4D(satellite.getImage4D(i, Image.B), counter, Image.B);
					bandLabels.set(counter, "Band "+(counter+1)+":"+bandLabels.get(counter).substring((bandLabels.get(counter).split(":"))[0].length()+1,bandLabels.get(counter).length()));
					counter++;
				}
			this.setSatellite(tmp);
		}
		else
		{
			JOptionPane.showMessageDialog(this,
   				    "You can not delete the only band of the picture",
   				    "Delete band error",
   				    JOptionPane.ERROR_MESSAGE);
		}
	}

	public void convert() {
		this.setSatellite(new ByteImage(satellite));
	}
	
	public Image getReference() {
		return reference;
	}

	public void setReference(Image reference) {
		this.reference = reference;
	}

	public int getTransparency() {
		return transparency;
	}

	public void setTransparency(int transparency) {
		this.transparency = transparency;
		this.displayImage();
	}

	public boolean isDisplaySatellite() {
		return displaySatellite;
	}

	public void setDisplaySatellite(boolean displaySatellite) {
		this.displaySatellite = displaySatellite;
		this.makeSatToDisplay();
	}

	public ArrayList<Color> getMHMTResultColors() {
		return mHMTResultColors;
	}

	public void setMHMTResultColors(ArrayList<Color> resultColors) {
		mHMTResultColors = resultColors;
	}
	
	public void setMHMTResultColorsElementAt(Color c,int i)
	{
		mHMTResultColors.set(i, c);
		this.makeMHMTResultsColorImage(i);
	}
	
	public void setProcessResultColorsElementAt(Color c,int i)
	{
		processColors.set(i, c);
		this.makeProcessResultsColorImage(i);
	}
	

	public ArrayList<Image> getMHMTResults() {
		return mHMTResults;
	}

	public void setMHMTResults(ArrayList<Image> results) {
		mHMTResults = results;
	}

	public Color getRefColor() {
		return refColor;
	}

	public void setRefColor(Color refColor) {
		this.refColor = refColor;
		this.makeRefColorImage();
	}

	public ArrayList<LinearObjectDefinition> getMHMTResultDefinitions() {
		return mHMTResultDefinitions;
	}

	public void setMHMTResultDefinitions(
			ArrayList<LinearObjectDefinition> resultDefinitions) {
		mHMTResultDefinitions = resultDefinitions;
	}

	public String getSatelliteFile() {
		return satelliteFile;
	}
	
	public int getPercentStretchCut()
	{
		return percentStretchCut;
	}
	
	public void setPercentStretchCut(int percentStretchCut) {
		this.percentStretchCut = percentStretchCut;
		contrastStretch=true;
		stretch.setText("Unstretch Bands ("+String.valueOf(percentStretchCut)+"%)");
		this.makeSatToDisplay();
	}
	
	public void makeTitle()
	{
		if(processCount==0)
			this.setTitle(satelliteFile);
		else
			this.setTitle(satelliteFile+" : Processing in progress...");		
	}
	
	
	public void increaseProcessCount()
	{
		processCount++;
		this.makeTitle();
	}
	
	public void decreaseProcessCount()
	{
		processCount--;
		this.makeTitle();
	}
	
	public void delProcess(int i)
	{
		processResults.remove(i);
		processNames.remove(i);
		processColorImages.remove(i);
		if(processColors.get(i)!=null)
		{
			processColors.remove(i);
			this.makeResultsFusion();
		}
		else
			processColors.remove(i);
		this.updatePanelInstances();
	}
	
	public void delMHMT(int i)
	{
		mHMTResults.remove(i);
		mHMTResultDefinitions.remove(i);
		mHMTResultsColorImage.remove(i);
		mHMTResultType.remove(i);
		if(mHMTResultColors.get(i)!=null)
		{
			mHMTResultColors.remove(i);
			this.makeResultsFusion();
		}
		else
			mHMTResultColors.remove(i);
		this.updatePanelInstances();		
	}
	
	public void updatePanelInstances()
	{
		SuperpositionPanel.updateInstance();
		ExportPanel.updateInstance();
		ExtractionPanel.updateInstance();
		PerformanceIndexPanel.updateInstance();
		ConfusionMatrixPanel.updateInstance();
		ResultSuppressionPanel.updateInstance();
	}
	
	public void importResult()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Choose the result to load");
		chooser.setCurrentDirectory(new File(this.getIOPath()));
		
		int returnVal = chooser.showOpenDialog(this);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	this.setIOPath(chooser.getCurrentDirectory().getAbsolutePath());
	       System.out.println("You choose to load : " + chooser.getCurrentDirectory()+File.separator+
	            chooser.getSelectedFile().getName());
	       Image tmp = (Image)new ImageLoader().process(chooser.getCurrentDirectory()+File.separator+chooser.getSelectedFile().getName());
	       if(tmp.getXDim()==satellite.getXDim()&&tmp.getYDim()==satellite.getYDim())
	       {
	    	   this.addProcessResult(tmp, chooser.getSelectedFile().getName());
	       }
	       else
	    	   JOptionPane.showMessageDialog(this,
	   				    "This is not a result for this picture !\n Dimensions are different.",
	   				    "Non valid result picture",
	   				    JOptionPane.ERROR_MESSAGE);
	    }
	}


	/**
	 * Class which deals with mouse event
	 * 
	 *
	 */
	private class MouseHandler extends MouseInputAdapter
	{
		private JWindow mercek = null;
		private JPanel root = null;
		private CoastalGUI parent;
		private int width = 100;
		private int height = 100;
		
		private JScrollPane scrollX = null;
		
		private int widthX = 25;
		private int heightX = 25;
		
		private Cursor BLANK_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TRANSLUCENT), new Point(0, 0), "blank");
		
		MouseHandler(CoastalGUI parent)
		{
			this.parent = parent;
			
			mercek = new JWindow(parent);
			
			root = new JPanel();
			root.setPreferredSize(new Dimension(width,height));
			root.setLayout(new BorderLayout());
			
			mercek.setContentPane(root);
			mercek.pack();
		}
		
		private void updateImage(int xdim,int ydim)
		{
			JViewport view = parent.scroll.getViewport();
			Point local = view.getViewPosition();
			
			// get the pixels of the area..25x25.
			// centered on the current pixel
			byte[] pixels = null;
			
			int[] ipixel = null;
			int[] bandOffsets = {0,1,2};
			int[] voidPixel = {0,0,0};
			
			ipixel = new int[3];
			pixels = new byte[width * height * 3];
			
			
			int xref = xdim + local.x - widthX/2;
			int yref = ydim + local.y - heightX/2;

			for(int x = 0; x < widthX; x++){
				for(int y = 0; y < heightX; y++){
					if(xref + x < 0 || xref + x >= displayed.getXDim() || 
							yref + y < 0 || yref + y >= displayed.getYDim()){
						setColorPixel(pixels,x * 4,y * 4,voidPixel);
					}else{
						readIterator[0].getPixel(xref + x,yref + y,ipixel);
						setColorPixel(pixels,x * 4,y * 4,ipixel);
					}
				}
			}
			
			BufferedImage bimg = null;
			DataBufferByte dbb = new DataBufferByte(pixels,width * height * 3);
			SampleModel s = RasterFactory.createPixelInterleavedSampleModel(DataBuffer.TYPE_BYTE,width,height,3,3 * width,bandOffsets);
			Raster r = RasterFactory.createWritableRaster(s,dbb,new Point(0,0));
			bimg = new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
			bimg.setData(r);
			
				
			DisplayJAI disp = new DisplayJAI(bimg);
			
			if(scrollX == null){
				scrollX = new JScrollPane(disp,ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				 		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				root.add(scrollX,BorderLayout.CENTER);
				
				mercek.pack();
			}else{
				scrollX.setViewportView(disp);
			}
			
			Point p = parent.scroll.getLocationOnScreen();
			mercek.setLocation(p.x + xdim - width,p.y + ydim - height);
			mercek.setVisible(true);
		}
		
		public void mousePressed(MouseEvent e)
		{	
			if(e.getButton()==MouseEvent.BUTTON1)
			{
				setCursor(BLANK_CURSOR);
				updateImage(e.getX(),e.getY());
				mouseMoved(e);
			}
			else if(e.getButton()==MouseEvent.BUTTON3)
			{
				int xdim = e.getX();
				int ydim = e.getY();
				
				JViewport view = parent.scroll.getViewport();
				Point local = view.getViewPosition();
				
				xdim += local.x;
				ydim += local.y;
				
				if(xy==null)
				{
					xy = new Point(xdim,ydim);
					displayImage();
				}
				else
				{
					xy2= new Point(xdim,ydim);
					displayImage();
					double dist = Math.sqrt((xdim-xy.x)*(xdim-xy.x)+(ydim-xy.y)*(ydim-xy.y))/zoom;
					JOptionPane.showMessageDialog(scroll,
		   				    "The distance between the two pixels is "+dist+" pixels",
		   				    "Distance measure",
		   				    JOptionPane.INFORMATION_MESSAGE);
					xy=null;
					xy2=null;
					displayImage();
					
				}
			}
			else
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Picture save");
				
				int returnVal = chooser.showSaveDialog(parent);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       System.out.println("You save the picture here : " + chooser.getCurrentDirectory()+"/"+
			            chooser.getSelectedFile().getName());
			       new ImageSave().process(displayed,chooser.getCurrentDirectory()+File.separator+chooser.getSelectedFile().getName());
			    }
			}
		}
		
		public void mouseDragged(MouseEvent e)
		{
			updateImage(e.getX(),e.getY());
			mouseMoved(e);
		}
		
		private void setColorPixel(byte[] pixels,int x,int y,int[] v)
		{
			for(int b = 0; b < 3; b++){
				for(int _x = x; _x < x + 4; _x++){
					for(int _y = y; _y < y + 4; _y++){
						pixels[b + 3 * _x + 3 * width * _y] = (byte)v[b];
					}
				}
			}
		}
		
		public void mouseMoved(MouseEvent e)
		{
			int xdim = e.getX();
			int ydim = e.getY();
			
			JViewport view = parent.scroll.getViewport();
			Point local = view.getViewPosition();
			
			xdim += local.x;
			ydim += local.y;
			
			if(xdim >= displayed.getXDim() || ydim >= displayed.getYDim() || xdim < 0 || ydim < 0) return;
			
			int[] ipixel = new int[3];
			readIterator[0].getPixel(xdim,ydim,ipixel);
			coordValue.setText("("+Math.round(xdim/zoom)+","+Math.round(ydim/zoom)+"):("+ipixel[0]+","+ipixel[1]+","+ipixel[2]+")");
		}
		
		public void mouseReleased(MouseEvent e)
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			mercek.setVisible(false);
		}
	}


	public String getIOPath() {
		return IOPath;
	}

	public void setIOPath(String path) {
		IOPath = path;
	}

	public ArrayList<Integer> getMHMTResultType() {
		return mHMTResultType;
	}
	
	
	public Instances createArff()
	{
	    Instances dataSet;
	    // Vecteur repr�sentant une ligne = un pixel avec ses valeurs en RGB donn�s par des vecteurs de niveau inf�rieur.
	    FastVector wekaAttributes = new FastVector();;

		if(satellite==null)
		{
			JOptionPane.showMessageDialog(this,
				    "You must load a picture first",
				    "Alert",
				    JOptionPane.INFORMATION_MESSAGE);

			dataSet = new Instances("Les Pixels", wekaAttributes, 0); 
		}
		else
		{
			// On construit le vecteur decrivant une instance ( Ici une instance est un pixel, repr�sent� par ses intensit�s dans les diff�rentes bandes composant l'image )
			Attribute Attribute1;
			
			int bdim = satellite.getBDim();

			for(int b = 0; b < bdim; b++)
			{
				Attribute1 = new Attribute("b" + (b+1));
				// Pour chaque bande on cr�� un argument de type num�rique qui contiendra l'intensit�
				wekaAttributes.addElement(Attribute1);
			
			}    	
					
						
			int xdim = this.satellite.getXDim();
			int ydim = this.satellite.getYDim();
			
			// Une ligne par pixel => nbPixel = nbInstances
			int nbPixel = xdim*ydim;
			dataSet = new Instances("Les Pixels", wekaAttributes, nbPixel); 
			
			
			Instance instEx = new Instance(wekaAttributes.size());
			
			for(int y = 0; y < ydim; y++){
				for(int x = 0; x < xdim; x++){

					
					for(int b = 0; b < bdim; b++){
						instEx.setValue((Attribute)wekaAttributes.elementAt(b), satellite.getPixelXYBByte(x, y, b));
					}				
					// Quand on arrive l� on a enregistr� les valeurs du pixel dans chaque bande, on ajoute notre instance � l'ensemble
					dataSet.add(instEx);
				}
			}
			
		}
		return dataSet;
	}
	
	
	/**
	 * Sauvegarde des donn�es dans le buffer
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
	

	
}
