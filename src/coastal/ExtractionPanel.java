package coastal;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;


import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.applied.remotesensing.coastline.DoubleThresholdCoastlineDetector;
import fr.unistra.pelican.algorithms.detection.MHMTBoundaryDetection;


public class ExtractionPanel extends JFrame implements ActionListener{

	private static final long serialVersionUID = 5293168417159801124L;

	private CoastalGUI parent;
	
	private JLabel selectLabel;
	private JLabel onlyStrictLabel;
	
	private JCheckBox onlyStrictjcb;
	
	private JScrollPane jsp;
	
	private JPanel jp;
	private JPanel main;
	
	private GridBagLayout gbl;
	
	private ArrayList<JCheckBox> jcb;
	
	private JButton ok;
	private JButton cancel;
	
	private static ExtractionPanel instance=null;
	
	private ExtractionPanel(CoastalGUI cgui)
	{
		this.parent=cgui;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Extract objects");
		
		main = new JPanel();		
		main.setPreferredSize(new Dimension(480,320));
		gbl = new GridBagLayout();		
		main.setLayout(gbl);
		this.setContentPane(main);
		
		selectLabel = new JLabel ("Select the objects you want to extract");
		GridBagConstraints cSelectLabel = new GridBagConstraints(1,1,5,1,0,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(selectLabel, cSelectLabel);
		main.add(selectLabel);
		
		this.definejp();
				
		
		
		onlyStrictLabel = new JLabel("Use only strict SEs");
		GridBagConstraints cOnlyStrictLabel = new GridBagConstraints(1,3,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(onlyStrictLabel, cOnlyStrictLabel);
		main.add(onlyStrictLabel);
		
		onlyStrictjcb = new JCheckBox();
		GridBagConstraints cOnlyStrictjcb = new GridBagConstraints(2,3,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(onlyStrictjcb, cOnlyStrictjcb);
		main.add(onlyStrictjcb);
		
		ok = new JButton("Extract");
		GridBagConstraints cok = new GridBagConstraints(1,4,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(ok, cok);
		main.add(ok);
		
		cancel = new JButton("Cancel");
		GridBagConstraints ccancel = new GridBagConstraints(2,4,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(cancel, ccancel);
		main.add(cancel);
		
		ok.addActionListener(this);
		cancel.addActionListener(this);

		this.setAlwaysOnTop(true);
		pack();
		this.setVisible(true);
		
		
	}
	
	private void definejp()
	{
		if(jsp!=null)
			main.remove(jsp);		
		jp = new JPanel();
		jp.setLayout(new GridLayout(parent.getLinearObj().size(), 2));
		jp.setPreferredSize(new Dimension(450,parent.getLinearObj().size()*15));
		jcb = new ArrayList<JCheckBox>();
		for(int i=0;i<parent.getLinearObj().size();i++)
		{
			JCheckBox jcbtmp = new JCheckBox();
			jcbtmp.setSelected(false);
			jp.add(jcbtmp);
			jcb.add(jcbtmp);
			JLabel labeltmp = new JLabel(parent.getLinearObj().get(i).getName());
			jp.add(labeltmp);
		}
		jsp = new JScrollPane(jp, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jsp.setPreferredSize(new Dimension(480,200));
			GridBagConstraints cjsp = new GridBagConstraints(1,2,2,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
			gbl.setConstraints(jsp, cjsp);
			main.add(jsp);
				
	}
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		if((JButton)e.getSource()==cancel)
			this.dispose();
		if((JButton)e.getSource()==ok)
		{
			this.dispose();
			parent.increaseProcessCount();
			for(int i=0;i<jcb.size();i++)
				if(jcb.get(i).isSelected())
				{
					if(onlyStrictjcb.isSelected())
						parent.addmHMTResults((Image) new MHMTBoundaryDetection().process(parent.getSatellite(),parent.getLinearObj().get(i).getStrictParameters()),parent.getLinearObj().get(i),CoastalGUI.ONLY_STRICT);
					else
					{
						Image st = (Image) new MHMTBoundaryDetection().process(parent.getSatellite(),parent.getLinearObj().get(i).getStrictParameters());
						Image sf = (Image) new MHMTBoundaryDetection().process(parent.getSatellite(),parent.getLinearObj().get(i).getSoftParameters());
						parent.addmHMTResults(DoubleThresholdCoastlineDetector.exec(st, sf),parent.getLinearObj().get(i),CoastalGUI.BOTH);
					}
				}
		}
		
	}

	public static void getInstance(CoastalGUI cgui)
	{
		if(instance==null)
			instance=new ExtractionPanel(cgui);
		else if(instance.isVisible())
		{
			instance.setVisible(true);
		}
		else
		{
			instance.dispose();
			instance=new ExtractionPanel(cgui);
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
