package coastal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SuperpositionPanel extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 7209877425518292594L;

	private CoastalGUI parent;
	
	private JLabel transparencyLabel;
	private JLabel displaySatelliteLabel;
	
	private JLabel referenceLabel=null;

	
	
	private JComboBox referencejcb=null;

	
	private ArrayList<JComboBox> mHMTjcb;
	
	private ArrayList<JComboBox> processjcb;
	
	private JCheckBox displaySatellitejcb;
	
	private JSlider transparencySlider;
	
	private JScrollPane jsp;
	
	private JPanel jp;
	private JPanel main;
	
	private GridBagLayout gbl;
	
	private Color[] colors;
	
	private final int nbColors = 11;
	
	private static SuperpositionPanel instance=null;
	
	private SuperpositionPanel(CoastalGUI cgui)
	{
		mHMTjcb = new ArrayList<JComboBox>();
		processjcb = new ArrayList<JComboBox>();
		
		colors = new Color[nbColors];
		
		colors[0] = null;
		colors[1] = Color.white;
		colors[2] = Color.blue;
		colors[3] = Color.gray;
		colors[4] = Color.green;
		colors[5] = Color.magenta;
		colors[6] = Color.orange;
		colors[7] = Color.pink;
		colors[8] = Color.red;
		colors[9] = Color.yellow;
		colors[10] = Color.cyan;
		
		this.parent=cgui;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Superposition parameters");
		
		main = new JPanel();		
		main.setPreferredSize(new Dimension(480,260));
		gbl = new GridBagLayout();		
		main.setLayout(gbl);
		this.setContentPane(main);
		
		transparencyLabel = new JLabel ("Select transparency of detected features");
		GridBagConstraints cTransparencyLabel = new GridBagConstraints(1,1,4,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(transparencyLabel, cTransparencyLabel);
		main.add(transparencyLabel);
		
		transparencySlider = new JSlider();
		transparencySlider.setValue(parent.getTransparency());
		GridBagConstraints cTransparencySlider = new GridBagConstraints(5,1,4,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(transparencySlider, cTransparencySlider);
		main.add(transparencySlider);
		
		displaySatelliteLabel = new JLabel ("Display satellite picture");
		GridBagConstraints cDisplaySatelliteLabel = new GridBagConstraints(1,2,4,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(displaySatelliteLabel, cDisplaySatelliteLabel);
		main.add(displaySatelliteLabel);
		
		displaySatellitejcb = new JCheckBox();
		if(parent.isDisplaySatellite())
			displaySatellitejcb.setSelected(true);
		else
			displaySatellitejcb.setSelected(false);
		GridBagConstraints cDisplaySatellitejcb = new GridBagConstraints(5,2,4,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(displaySatellitejcb, cDisplaySatellitejcb);
		main.add(displaySatellitejcb);
		
		this.definejp();		
		
		displaySatellitejcb.addActionListener(this);
		transparencySlider.addChangeListener(new sliderChangeListener());
		if(referencejcb!=null)
			referencejcb.addActionListener(this);
		for(int i=0;i<processjcb.size();i++)
			processjcb.get(i).addActionListener(this);
		for(int i=0;i<mHMTjcb.size();i++)
			mHMTjcb.get(i).addActionListener(this);

		this.setAlwaysOnTop(true);		
		pack();
		this.setVisible(true);
	}



	private void definejp() {
		if(jsp!=null)
			main.remove(jsp);
		jp = new JPanel();
		int nb = parent.getMHMTResults().size()+parent.getProcessResults().size();
		if(parent.getReference()!=null)
			nb++;
		jp.setLayout(new GridLayout(nb, 2));
		jp.setPreferredSize(new Dimension(450,nb*15));
		nb=1;
		if(parent.getReference()!=null)
		{
			referenceLabel = new JLabel("Reference");
			referencejcb = new JComboBox();
			for(int i=0;i<nbColors;i++)
				referencejcb.addItem(colorToName(colors[i]));
			referencejcb.setSelectedItem(colorToName(parent.getRefColor()));
			jp.add(referenceLabel);
			jp.add(referencejcb);
		}
		for(int i=0;i<parent.getProcessResults().size();i++)
		{
			JLabel tmplabel = new JLabel(parent.getProcessNames().get(i));
			JComboBox tmpjcb = new JComboBox();
			for(int j=0;j<nbColors;j++)
				tmpjcb.addItem(colorToName(colors[j]));
			tmpjcb.setSelectedItem(colorToName(parent.getProcessColors().get(i)));
			tmpjcb.setName(String.valueOf(i));
			processjcb.add(tmpjcb);
			jp.add(tmplabel);
			jp.add(tmpjcb);
			
		}
		for(int i=0;i<parent.getMHMTResults().size();i++)
		{
			JLabel tmplabel = new JLabel(parent.getMHMTResultDefinitions().get(i).getName());
			JComboBox tmpjcb = new JComboBox();
			for(int j=0;j<nbColors;j++)
				tmpjcb.addItem(colorToName(colors[j]));
			tmpjcb.setSelectedItem(colorToName(parent.getMHMTResultColors().get(i)));
			tmpjcb.setName(String.valueOf(i));
			mHMTjcb.add(tmpjcb);
			jp.add(tmplabel);
			jp.add(tmpjcb);
			
		}
		jsp = new JScrollPane(jp, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jsp.setPreferredSize(new Dimension(480,200));
			GridBagConstraints cjsp = new GridBagConstraints(1,3,8,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
			gbl.setConstraints(jsp, cjsp);
			main.add(jsp);
		
	}
	
	private String colorToName(Color c)
	{
		if(c==null)
			return ("No display");
		else if(c==Color.white)
			return ("White");
		else if(c==Color.blue)
			return ("Blue");
		else if(c==Color.cyan)
			return ("Cyan");
		else if(c==Color.gray)
			return ("Gray");
		else if(c==Color.green)
			return ("Green");
		else if(c==Color.magenta)
			return ("Magenta");	
		else if(c==Color.orange)
			return ("Orange");	
		else if(c==Color.pink)
			return ("Pink");
		else if(c==Color.red)
			return ("Red");
		else if(c==Color.yellow)
			return ("Yellow");
		else
			return("Ooops");
	}



	public void actionPerformed(ActionEvent e) {
		//displaySatellitejcb
		if(e.getSource() instanceof JCheckBox)
		{
			parent.setDisplaySatellite(displaySatellitejcb.isSelected());
		}
		else
		{
			if((JComboBox) e.getSource()==referencejcb)
				parent.setRefColor(colors[referencejcb.getSelectedIndex()]);
			else
			{
				for(int i=0;i<processjcb.size();i++)
					if((JComboBox) e.getSource()==processjcb.get(i))
						parent.setProcessResultColorsElementAt(colors[processjcb.get(i).getSelectedIndex()], i);
				for(int i=0;i<mHMTjcb.size();i++)
					if((JComboBox) e.getSource()==mHMTjcb.get(i))
						parent.setMHMTResultColorsElementAt(colors[mHMTjcb.get(i).getSelectedIndex()], i);
		
			}
		}
		
	}
	
	private class sliderChangeListener implements ChangeListener
	{
		// Transparency slider moves
		public void stateChanged(ChangeEvent e) {
			parent.setTransparency(transparencySlider.getValue());			
		}
		
	}
	
	public static void getInstance(CoastalGUI cgui)
	{
		if(instance==null)
			instance=new SuperpositionPanel(cgui);
		else if(instance.isVisible())
		{
			instance.setVisible(true);
		}
		else
		{
			instance.dispose();
			instance=new SuperpositionPanel(cgui);
		}
	}
	
	public static void updateInstance()
	{
		if(instance!=null)
		{
			instance.definejp();
			instance.repaint();
			if(instance.isVisible())
				instance.setVisible(true);
		}
	}


}
