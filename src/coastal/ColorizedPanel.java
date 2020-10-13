package coastal;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;


import javax.media.jai.RasterFactory;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.media.jai.widget.DisplayJAI;

import fr.unistra.pelican.ByteImage;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.geometric.ResamplingByValue;



public class ColorizedPanel extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = -5751006610363081947L;
	
	private JSlider redSlider;
	private JSlider greenSlider;
	private JSlider blueSlider;
	
	private JLabel redLabel;
	private JLabel greenLabel;
	private JLabel blueLabel;
	
	private JLabel redSet;
	private JLabel greenSet;
	private JLabel blueSet;
	
	private JPanel main;
	private JPanel left;
	
	private CoastalGUI parent;
	private Image satresized;
	private Image preview;
	
	private JButton apply;
	
	private JScrollPane scroll;
	
	private int[] colorVector;
	
	private static ColorizedPanel instance=null;
	
	
	private ColorizedPanel(CoastalGUI cgui)
	{
		this.parent=cgui;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Colorization");
		this.satresized = ResamplingByValue.exec(parent.getSatellite(),100,100,1,1,parent.getSatellite().getBDim(),ResamplingByValue.NEAREST);
		
		if(parent.getColorVector()!=null)
			this.colorVector = parent.getColorVector();
		else
		{
			this.colorVector = new int[3];
			colorVector[0]=0;
			colorVector[1]=1;
			colorVector[2]=2;
		}
		
		main = new JPanel();
		main.setPreferredSize(new Dimension(400,100));
		main.setLayout(new BorderLayout());
		this.setContentPane(main);
		
		left = new JPanel();
		left.setLayout(new GridLayout(4,1));
		
		JPanel redPn = new JPanel();
		JPanel greenPn = new JPanel();
		JPanel bluePn = new JPanel();
		redPn.setLayout(new BorderLayout());
		greenPn.setLayout(new BorderLayout());
		bluePn.setLayout(new BorderLayout());
		
		
		JPanel slidePn = new JPanel();
		slidePn.setLayout(new BorderLayout());
		redSlider = new JSlider(SwingConstants.HORIZONTAL,1,satresized.getBDim(),1);
		greenSlider = new JSlider(SwingConstants.HORIZONTAL,1,satresized.getBDim(),2);
		blueSlider = new JSlider(SwingConstants.HORIZONTAL,1,satresized.getBDim(),3);
		redSlider.setSnapToTicks(true);
		greenSlider.setSnapToTicks(true);
		blueSlider.setSnapToTicks(true);
		redSlider.setValue(colorVector[0]+1);
		greenSlider.setValue(colorVector[1]+1);
		blueSlider.setValue(colorVector[2]+1);
		
		redPn.add(redSlider,BorderLayout.CENTER);
		greenPn.add(greenSlider,BorderLayout.CENTER);
		bluePn.add(blueSlider,BorderLayout.CENTER);
		
		JPanel bandLabelPn = new JPanel();
		bandLabelPn.setLayout(new BorderLayout());
		redLabel = new JLabel("Red Band ");
		greenLabel = new JLabel("Green Band ");
		blueLabel = new JLabel("Blue Band ");

		redPn.add(redLabel,BorderLayout.WEST);
		greenPn.add(greenLabel,BorderLayout.WEST);
		bluePn.add(blueLabel,BorderLayout.WEST);
		
		JPanel bandSetPn = new JPanel();
		bandSetPn.setLayout(new BorderLayout());
		redSet = new JLabel("  "+String.valueOf(redSlider.getValue()));
		greenSet = new JLabel("  "+String.valueOf(greenSlider.getValue()));
		blueSet = new JLabel("  "+String.valueOf(blueSlider.getValue()));

		
		redPn.add(redSet,BorderLayout.EAST);
		greenPn.add(greenSet,BorderLayout.EAST);
		bluePn.add(blueSet,BorderLayout.EAST);
		
		apply=new JButton("Apply");
		apply.addActionListener(this);
		
		left.add(redPn);
		left.add(greenPn);
		left.add(bluePn);
		left.add(apply);
		
		this.makePreview();
		
		main.add(left,BorderLayout.WEST);		
		
		redSlider.addChangeListener(new ChangeListener(){
            	public void stateChanged(ChangeEvent e){
            		colorVector[0] = redSlider.getValue()-1;
            		redSet.setText("  "+String.valueOf(redSlider.getValue()));
            		makePreview();
			}
		});
		greenSlider.addChangeListener(new ChangeListener(){
        	public void stateChanged(ChangeEvent e){
        		colorVector[1] = greenSlider.getValue()-1;
        		greenSet.setText("  "+String.valueOf(greenSlider.getValue()));
        		makePreview();
        	}
		});
		blueSlider.addChangeListener(new ChangeListener(){
        	public void stateChanged(ChangeEvent e){
        		colorVector[2] = blueSlider.getValue()-1;
        		blueSet.setText("  "+String.valueOf(blueSlider.getValue()));
        		makePreview();
        	}
		});
		this.setAlwaysOnTop(true);
		pack();
		this.setVisible(true);
	}
	
	public static void getInstance(CoastalGUI cgui)
	{
		if(instance==null||!instance.isVisible())
			instance=new ColorizedPanel(cgui);		
	}

	/**
	 * Method for displaying the picture in the GUI
	 *
	 */
	private void displayImage()
	{
			if(scroll!=null)
				main.remove(scroll);
			
			// transform the image into an array of BufferedImages		
	       final int tdim = 1;
	       final int zdim = 1;
	       DataBufferByte dbb;
	       SampleModel s;
	       Raster r;
	       BufferedImage bimg = null;
	       final int bdim = 3;	       
	       DisplayJAI[] display = new DisplayJAI[tdim * zdim];
	       RandomIter[] readIterator = new RandomIter[tdim * zdim];
	       int[] bandOffsets = {0,1,2};
				
			for(int t = 0; t < tdim; t++){
				for(int z = 0; z < zdim; z++){
			
					Image tmp = preview;
			
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
			
			scroll = new JScrollPane(display[0],JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			
			scroll.setPreferredSize(new Dimension(100,100));
			main.add(scroll,BorderLayout.EAST);
						
			this.setVisible(true);
	}
	
	public void makePreview() 
	{		
		preview=new ByteImage(satresized.getXDim(), satresized.getYDim(), 1, 1, 3);
	    preview.setImage4D(satresized.getImage4D(colorVector[0], Image.B), 0, Image.B);
	    preview.setImage4D(satresized.getImage4D(colorVector[1], Image.B), 1, Image.B);
	    preview.setImage4D(satresized.getImage4D(colorVector[2], Image.B), 2, Image.B);
	    this.displayImage();
	}

	public void actionPerformed(ActionEvent arg0) {
		parent.setColorVector(colorVector);
		parent.setColor(true);
		parent.makeSatToDisplay();
		this.dispose();		
	}
	
}
