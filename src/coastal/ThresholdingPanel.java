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
import fr.unistra.pelican.algorithms.morphology.binary.BinaryGradient;
import fr.unistra.pelican.algorithms.segmentation.ManualThresholding;
import fr.unistra.pelican.util.morphology.FlatStructuringElement2D;

public class ThresholdingPanel extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = -5751006610363081947L;
	
	private CoastalGUI parent;
	
	private JLabel bandLabel;
	private JLabel threshLabel;
	
	private JComboBox bandjcc;

	
	private JTextField threshValue;
	
	private JButton launch;
	private JButton cancel;
	
	private int band;
	private int thresh;
	
	private static ThresholdingPanel instance=null;
	
	private ThresholdingPanel(CoastalGUI cgui)
	{
		this.parent = cgui;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Thresholding");
		
		JPanel main = new JPanel();		
		main.setPreferredSize(new Dimension(300,60));
		main.setLayout(new GridLayout(3, 2));
		this.setContentPane(main);
		
		
		bandLabel = new JLabel("Band ");
		bandjcc = new JComboBox();
		bandjcc.setName("band");
		for(int i=0;i<parent.getSatellite().getBDim();i++)
			bandjcc.addItem(String.valueOf(i+1));
	

		
		threshLabel = new JLabel("Threshold ");
		threshValue = new JTextField("128");
		
		
		launch = new JButton("Launch");
		cancel = new JButton("Cancel");
		
		main.add(bandLabel);
		main.add(bandjcc);

		main.add(threshLabel);
		main.add(threshValue);
		main.add(launch);
		main.add(cancel);
		
		JComboBoxListener jcbl = new JComboBoxListener();
		JTextFieldListener jtfl = new JTextFieldListener();
		
		cancel.addActionListener(this);
		launch.addActionListener(this);
		bandjcc.addActionListener(jcbl);

		threshValue.addKeyListener(jtfl);
		
		band=0;
		thresh=128;

		this.setAlwaysOnTop(true);
		pack();
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		//Cancel
		if(((JButton) e.getSource()).getText()=="Cancel")
			this.dispose();
		//Launch
		if(((JButton) e.getSource()).getText()=="Launch")
			{
				this.dispose();
				parent.increaseProcessCount();
				Image threshed = (Image) new ManualThresholding().process(parent.getSatellite().getImage4D(band, Image.B),thresh);
				threshed = (Image) new BinaryGradient().process(threshed,FlatStructuringElement2D.createSquareFlatStructuringElement(3));
				String name = "Thresholding B"+String.valueOf(band+1)+"_"+String.valueOf(thresh);
				parent.addProcessResult(threshed, name);
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
								"Thresholding : Wrong values",
								JOptionPane.ERROR_MESSAGE);
					}
				}else
				{
					thresh = 128;
					threshValue.setText("128");
					JOptionPane.showMessageDialog(threshValue,
							"The threshold must be a numeric value between 0 and 255",
							"Thresholding : Non-numeric values",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			
		}

		
	}
	
	public static void getInstance(CoastalGUI cgui)
	{
		if(instance==null||!instance.isVisible())
			instance=new ThresholdingPanel(cgui);
		else
			instance.setVisible(true);
	}

}

