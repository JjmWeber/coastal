package coastal;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.applied.remotesensing.index.IB;
import fr.unistra.pelican.algorithms.applied.remotesensing.index.NDVI;

public class AddBandPanel extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = -5751006610363081947L;
	
	private CoastalGUI parent;
	
	private JLabel nameLabel;
	private JLabel methodLabel;
	private JLabel band1Label;
	private JLabel band2Label;
	
	private JComboBox methodjcc;
	private JComboBox band1jcc;
	private JComboBox band2jcc;
	
	private JTextField nameText;
	
	private JButton launch;
	private JButton cancel;
	
	
	private static AddBandPanel instance=null;
	
	
	private AddBandPanel(CoastalGUI cgui)
	{
		this.parent = cgui;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Add band");
		
		JPanel main = new JPanel();		
		main.setPreferredSize(new Dimension(500,80));
		main.setLayout(new GridLayout(5, 2));
		this.setContentPane(main);
		
		nameLabel = new JLabel("Name");
		nameText = new JTextField();		
		
		methodLabel = new JLabel("Method ");
		methodjcc = new JComboBox();
		methodjcc.setName("band");
		methodjcc.addItem("ND:(band1-band2)/(band1+band2)");
		methodjcc.addItem("BI:sqrt(band1^2+band2^2)/2");
		
		band1Label = new JLabel("Band 1 ");
		band1jcc = new JComboBox();
		for(int i=0;i<parent.getSatellite().getBDim();i++)
			band1jcc.addItem(String.valueOf(i+1));
		
		band2Label = new JLabel("Band 2 ");
		band2jcc = new JComboBox();
		for(int i=0;i<parent.getSatellite().getBDim();i++)
			band2jcc.addItem(String.valueOf(i+1));
			
		launch = new JButton("Launch");
		cancel = new JButton("Cancel");
		
		main.add(nameLabel);
		main.add(nameText);
		main.add(methodLabel);
		main.add(methodjcc);
		main.add(band1Label);
		main.add(band1jcc);
		main.add(band2Label);
		main.add(band2jcc);
		main.add(launch);
		main.add(cancel);
		
		
		
		cancel.addActionListener(this);
		launch.addActionListener(this);
		methodjcc.addActionListener(this);

		this.setAlwaysOnTop(true);
		pack();
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		//Cancel
		if(e.getSource()==cancel)
			this.dispose();
		//Launch
		if(e.getSource()==launch)
			if(methodjcc.getSelectedIndex()==0)
				if(!nameText.getText().equals(""))
				{
					this.dispose();
					parent.addBand((Image) new NDVI().process(parent.getSatellite(),band1jcc.getSelectedIndex(),band2jcc.getSelectedIndex()),nameText.getText());
				}
				else
					JOptionPane.showMessageDialog(this,
		   				    "You must give a name to your band !",
		   				    "No name",
		   				    JOptionPane.ERROR_MESSAGE);
			else
			{
				this.dispose();
				parent.addBand((Image) new IB().process(parent.getSatellite(),band1jcc.getSelectedIndex(),band2jcc.getSelectedIndex()),nameText.getText());
				//parent.addBand(IB.exec(parent.getSatellite()),"Brightness Index");
			}
		//Method change
//		if(e.getSource()==methodjcc)
//		{
//			if(methodjcc.getSelectedIndex()==0)
//			{
//				nameText.setEnabled(true);
//				band1jcc.setEnabled(true);
//				band2jcc.setEnabled(true);
//			}
//			else
//			{
//				nameText.setEnabled(false);
//				band1jcc.setEnabled(false);
//				band2jcc.setEnabled(false);
//			}
//		}
	}
	
	public static void getInstance(CoastalGUI cgui)
	{
		if(instance==null||!instance.isVisible())
			instance=new AddBandPanel(cgui);
		else
		{
			instance.dispose();
			instance=new AddBandPanel(cgui);
		}
	}

}


