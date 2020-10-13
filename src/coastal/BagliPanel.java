package coastal;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.applied.remotesensing.coastline.BagliCoastlineDetector;
import fr.unistra.pelican.algorithms.morphology.binary.BinaryGradient;
import fr.unistra.pelican.util.morphology.FlatStructuringElement2D;

public class BagliPanel extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = -5751006610363081947L;
	
	private CoastalGUI parent;
	
	private JLabel bandLabel;
	private JLabel methodLabel;
	private JLabel threshMethLabel;
	private JLabel threshLabel;
	
	private JComboBox bandjcc;
	private JComboBox methodjcc;
	private JComboBox threshMethjcc;
	
	private JTextField threshValue;
	
	private JButton launch;
	private JButton cancel;
	
	private int band;
	private int method;
	private int threshmeth;
	private int thresh;
	
	private static BagliPanel instance=null;
	
	private BagliPanel(CoastalGUI cgui)
	{
		this.parent = cgui;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Bagli");
		
		JPanel main = new JPanel();		
		main.setPreferredSize(new Dimension(300,100));
		main.setLayout(new GridLayout(5, 2));
		this.setContentPane(main);
		
		
		bandLabel = new JLabel("Band ");
		bandjcc = new JComboBox();
		bandjcc.setName("band");
		for(int i=0;i<parent.getSatellite().getBDim();i++)
			bandjcc.addItem(String.valueOf(i+1));
	
		
		
		methodLabel = new JLabel("Method ");
		methodjcc = new JComboBox();
		methodjcc.setName("method");
		methodjcc.addItem("Seed Region Growing");
		methodjcc.addItem("Watershed");
		methodjcc.setSelectedIndex(1);
		
		
		threshMethLabel = new JLabel("Thresholding Method ");
		threshMethjcc = new JComboBox();
		threshMethjcc.setName("threshmeth");
		threshMethjcc.addItem("Manual");
		threshMethjcc.addItem("Mean");
		threshMethjcc.addItem("Otsu");
		
		threshLabel = new JLabel("Threshold ");
		threshValue = new JTextField("128");
		
		
		launch = new JButton("Launch");
		cancel = new JButton("Cancel");
		
		main.add(bandLabel);
		main.add(bandjcc);
		main.add(methodLabel);
		main.add(methodjcc);
		main.add(threshMethLabel);
		main.add(threshMethjcc);
		main.add(threshLabel);
		main.add(threshValue);
		main.add(launch);
		main.add(cancel);
		
		JComboBoxListener jcbl = new JComboBoxListener();
		JTextFieldListener jtfl = new JTextFieldListener();
		
		cancel.addActionListener(this);
		launch.addActionListener(this);
		bandjcc.addActionListener(jcbl);
		methodjcc.addActionListener(jcbl);
		threshMethjcc.addActionListener(jcbl);
		threshValue.addKeyListener(jtfl);
		
		band=0;
		method=BagliCoastlineDetector.WS;
		threshmeth=BagliCoastlineDetector.MANUAL;
		thresh=128;

		this.setAlwaysOnTop(true);
		pack();
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		//Cancel
		if(((JButton) e.getSource()).getText()=="Cancel")
		{
			this.dispose();
		}
		//Launch
		if(((JButton) e.getSource()).getText()=="Launch")
			{
				this.dispose();
				parent.increaseProcessCount();
				Image bagli = (Image) new BagliCoastlineDetector().process(parent.getSatellite(),method,threshmeth,thresh,band);
				bagli = (Image) new BinaryGradient().process(bagli,FlatStructuringElement2D.createSquareFlatStructuringElement(3));
				String bagliN = "Bagli B"+String.valueOf(band+1);
				if(method==BagliCoastlineDetector.SRG)
					bagliN = bagliN +" SRG";
				else
					bagliN = bagliN +" WS";
				if(threshmeth==BagliCoastlineDetector.MEAN)
					bagliN = bagliN + " MEAN";
				else if(threshmeth==BagliCoastlineDetector.OTSU)
					bagliN = bagliN + " OTSU";
				else
					bagliN = bagliN + " M"+String.valueOf(thresh);
				parent.addProcessResult(bagli, bagliN);
			}
		
		
	}
	
	private class JComboBoxListener implements ActionListener
	{
		
		
		
		public void actionPerformed(ActionEvent e) {
			//	bandjcc change
			if(((JComboBox) e.getSource()).getName()=="band")
			{
				band=((JComboBox) e.getSource()).getSelectedIndex();
			}
			// methodjcc change
			if(((JComboBox) e.getSource()).getName()=="method")
			{
				if(((JComboBox) e.getSource()).getSelectedIndex()==0)
				{
					method=BagliCoastlineDetector.SRG;
				}
				else
				{
					method=BagliCoastlineDetector.WS;
				}
			}
			
			// threshmethjcc change
			if(((JComboBox) e.getSource()).getName()=="threshmeth")
			{
				if(((JComboBox) e.getSource()).getSelectedIndex()==0)
				{
					threshmeth=BagliCoastlineDetector.MANUAL;
				}
				else if(((JComboBox) e.getSource()).getSelectedIndex()==1)
				{
					threshmeth=BagliCoastlineDetector.MEAN;
				}
				else
				{
					threshmeth=BagliCoastlineDetector.OTSU;
				}
				
				if(threshmeth!=BagliCoastlineDetector.MANUAL)
				{
					threshValue.setEnabled(false);
				}
				else
				{
					threshValue.setEnabled(true);
				}
			}
			
		}
		
	}
	
	//private class JTextFieldListener implements ActionListener
	private class JTextFieldListener implements KeyListener
	{
		//public void actionPerformed(ActionEvent e) 
		public void keyTyped(KeyEvent arg0) {
		}

		public void keyPressed(KeyEvent arg0) {
		}

		public void keyReleased(KeyEvent arg0) {
			String t = threshValue.getText();
			if(t.length()>0)
			{
				boolean charOk=true;
				for(int i=0;i<t.length();i++)
					if(t.charAt(i)<'0'||t.charAt(i)>'9')
						charOk=false;
				if(charOk)
				{
					if(Integer.parseInt(threshValue.getText())<256&&Integer.parseInt(threshValue.getText())>=0)
					{
						thresh = Integer.parseInt(threshValue.getText());
					} else
					{
						thresh = 128;
						threshValue.setText("128");
						JOptionPane.showMessageDialog(threshValue,
								"The threshold must be between 0 and 255",
								"Bagli : Wrong values",
								JOptionPane.ERROR_MESSAGE);
					}
				}else
				{
					thresh = 128;
					threshValue.setText("128");
					JOptionPane.showMessageDialog(threshValue,
							"The threshold must be a numeric value between 0 and 255",
							"Bagli : Non-numeric values",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			
		}

		
	}
	
	public static void getInstance(CoastalGUI cgui)
	{
		if(instance==null||!instance.isVisible())
			instance=new BagliPanel(cgui);
		else
			instance.setVisible(true);
	}

}

