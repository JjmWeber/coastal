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
import fr.unistra.pelican.algorithms.applied.remotesensing.coastline.ErtezaCoastlineDetector;

public class ErtezaPanel extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = -5751006610363081947L;
	
	private CoastalGUI parent;
	

	private JLabel landLabel;
	private JLabel threshMethLabel;
	private JLabel threshLabel;
	private JLabel bandLabel;

	
	private JComboBox bandjcc;

	private JComboBox landjcc;
	private JComboBox threshMethjcc;
	
	private JTextField threshValue;
	
	private JButton launch;
	private JButton cancel;
	
	private int land;
	private int threshmeth;
	private int thresh;
	private int band;
	
	private static ErtezaPanel instance=null;
	
	private ErtezaPanel(CoastalGUI cgui)
	{
		this.parent = cgui;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Erteza");
		
		JPanel main = new JPanel();		
		main.setPreferredSize(new Dimension(300,80));
		main.setLayout(new GridLayout(5, 2));
		this.setContentPane(main);
		
		
	
		
		
		landLabel = new JLabel("Land position ");
		landjcc = new JComboBox();
		landjcc.setName("land");
		landjcc.addItem("Bottom");
		landjcc.addItem("Right");
		landjcc.addItem("Left");
		landjcc.addItem("Top");
		

		
		threshMethLabel = new JLabel("Thresholding Method ");
		threshMethjcc = new JComboBox();
		threshMethjcc.setName("threshmeth");
		threshMethjcc.addItem("Manual");
		threshMethjcc.addItem("Mean");
		threshMethjcc.addItem("Otsu");
		
		threshLabel = new JLabel("Threshold ");
		threshValue = new JTextField("128");
		
		bandLabel = new JLabel("Band");
		bandjcc = new JComboBox();
		bandjcc.setName("band");
		for(int i=0;i<parent.getSatellite().getBDim();i++)
			bandjcc.addItem(String.valueOf(i+1));
		
		launch = new JButton("Launch");
		cancel = new JButton("Cancel");
		
		main.add(landLabel);
		main.add(landjcc);
		main.add(threshMethLabel);
		main.add(threshMethjcc);
		main.add(threshLabel);
		main.add(threshValue);
		main.add(bandLabel);
		main.add(bandjcc);
		main.add(launch);
		main.add(cancel);
		
		JComboBoxListener jcbl = new JComboBoxListener();
		JTextFieldListener jtfl = new JTextFieldListener();
		
		cancel.addActionListener(this);
		launch.addActionListener(this);
		landjcc.addActionListener(jcbl);
		threshMethjcc.addActionListener(jcbl);
		bandjcc.addActionListener(jcbl);
		threshValue.addKeyListener(jtfl);
		
		land=ErtezaCoastlineDetector.LAND_IS_BOTTOM;
		threshmeth=ErtezaCoastlineDetector.OTHER;
		thresh=128;
		band=0;

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
				Image erteza =(Image) new ErtezaCoastlineDetector().process(parent.getSatellite(),land,threshmeth,thresh,band);
				String name = "Erteza B"+String.valueOf(band+1);
				if(threshmeth==ErtezaCoastlineDetector.MEAN)
					name = name + " MEAN";
				else if(threshmeth==ErtezaCoastlineDetector.OTSU)
					name = name + " OTSU";
				else
					name = name + " M"+String.valueOf(thresh);
				if(land==ErtezaCoastlineDetector.LAND_IS_BOTTOM)
					name = name+" BOTTOM";
				else if(land==ErtezaCoastlineDetector.LAND_IS_RIGHT)
					name = name+" RIGHT";
				else if(land==ErtezaCoastlineDetector.LAND_IS_LEFT)
					name = name+" LEFT";
				else  name = name+" UP";				
				parent.addProcessResult(erteza, name);
			}
		
		
	}
	
	private class JComboBoxListener implements ActionListener
	{
		
		
		
		public void actionPerformed(ActionEvent e) {

			// methodjcc change
			if(((JComboBox) e.getSource()).getName()=="method")
			{
				if(((JComboBox) e.getSource()).getSelectedIndex()==0)
				{
					land=ErtezaCoastlineDetector.LAND_IS_BOTTOM;
				}
				else if(((JComboBox) e.getSource()).getSelectedIndex()==1)
				{
					land=ErtezaCoastlineDetector.LAND_IS_RIGHT;
				}
				else if(((JComboBox) e.getSource()).getSelectedIndex()==2)
				{
					land=ErtezaCoastlineDetector.LAND_IS_LEFT;
				}
				else 
				{
					land=ErtezaCoastlineDetector.LAND_IS_TOP;
				}
			}
			
			// threshmethjcc change
			if(((JComboBox) e.getSource()).getName()=="threshmeth")
			{
				if(((JComboBox) e.getSource()).getSelectedIndex()==0)
				{
					threshmeth=ErtezaCoastlineDetector.OTHER;
				}
				else if(((JComboBox) e.getSource()).getSelectedIndex()==1)
				{
					threshmeth=ErtezaCoastlineDetector.MEAN;
				}
				else
				{
					threshmeth=ErtezaCoastlineDetector.OTSU;
				}
				
				if(threshmeth!=ErtezaCoastlineDetector.OTHER)
				{
					threshValue.setEnabled(false);
				}
				else
				{
					threshValue.setEnabled(true);
				}
			}
			
			//Band change
			if(((JComboBox) e.getSource())==bandjcc)
				band=bandjcc.getSelectedIndex();
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
								"Erteza : Wrong values",
								JOptionPane.ERROR_MESSAGE);
					}
				}else
				{
					thresh = 128;
					threshValue.setText("128");
					JOptionPane.showMessageDialog(threshValue,
							"The threshold must be a numeric value between 0 and 255",
							"Erteza : Non-numeric values",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			
		}

		
	}
	
	public static void getInstance(CoastalGUI cgui)
	{
		if(instance==null||!instance.isVisible())
			new ErtezaPanel(cgui);
		else
			instance.setVisible(true);
	}

}

