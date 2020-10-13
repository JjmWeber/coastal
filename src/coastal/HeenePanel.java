package coastal;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.applied.remotesensing.coastline.HeeneCoastlineDetector;


public class HeenePanel extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = -5751006610363081947L;
	
	private CoastalGUI parent;
	
	private JLabel bandLabel;

	
	private JComboBox bandjcc;

	

	
	private JButton launch;
	private JButton cancel;
	
	private int band;

	private static HeenePanel instance=null;
	
	private HeenePanel(CoastalGUI cgui)
	{
		this.parent = cgui;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Heene");
		
		JPanel main = new JPanel();		
		main.setPreferredSize(new Dimension(300,40));
		main.setLayout(new GridLayout(2, 2));
		this.setContentPane(main);
		
		
		bandLabel = new JLabel("Band ");
		bandjcc = new JComboBox();
		bandjcc.setName("band");
		for(int i=0;i<parent.getSatellite().getBDim();i++)
			bandjcc.addItem(String.valueOf(i+1));
	
		
	
		
		
		launch = new JButton("Launch");
		cancel = new JButton("Cancel");
		
		main.add(bandLabel);
		main.add(bandjcc);
		main.add(launch);
		main.add(cancel);
		
		JComboBoxListener jcbl = new JComboBoxListener();
		
		cancel.addActionListener(this);
		launch.addActionListener(this);
		bandjcc.addActionListener(jcbl);
		
		band=0;

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
				Image heene = (Image) new HeeneCoastlineDetector().process(parent.getSatellite(),band);
				String names = "Heene B"+String.valueOf(band+1);
				parent.addProcessResult(heene, names);
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
	
	public static void getInstance(CoastalGUI cgui)
	{
		if(instance==null||!instance.isVisible())
			instance=new HeenePanel(cgui);
		else
			instance.setVisible(true);
	}
	
	
}