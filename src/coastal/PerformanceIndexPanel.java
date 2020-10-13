package coastal;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import fr.unistra.pelican.algorithms.statistics.PerformanceIndex;

public class PerformanceIndexPanel extends JFrame implements ActionListener {

	private static final long serialVersionUID = 697878033868684667L;
	
	private CoastalGUI parent;
	
	private JLabel compareLabel, normLabel,minLabel,maxLabel,frechetLabel,distLabel,fpcLabel,fppLabel,fncLabel,fnpLabel;
	
	private JCheckBox normjcb,minjcb,maxjcb,frechetjcb,distjcb,fpcjcb,fppjcb,fncjcb,fnpjcb;
	
	private JScrollPane jsp;
	
	private JButton ok,cancel;
	
	private ArrayList<JCheckBox> mHMTjcb;
	private ArrayList<JCheckBox> processjcb;
	
	private JPanel jp;
	private JPanel main;
	
	private GridBagLayout gbl;
	
	private static PerformanceIndexPanel instance=null;
	
	private PerformanceIndexPanel(CoastalGUI cgui)
	{
		this.parent=cgui;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Performance Index");
		
		mHMTjcb = new ArrayList<JCheckBox>();
		processjcb = new ArrayList<JCheckBox>();
		
		main = new JPanel();		
		main.setPreferredSize(new Dimension(480,300));
		gbl = new GridBagLayout();		
		main.setLayout(gbl);
		this.setContentPane(main);
		
		normLabel = new JLabel ("Normalize results");
		GridBagConstraints cnormLabel = new GridBagConstraints(1,0,7,1,0,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(normLabel, cnormLabel);
		main.add(normLabel);
		
		normjcb = new JCheckBox();
		GridBagConstraints cnormjcb = new GridBagConstraints(9,0,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(normjcb, cnormjcb);
		main.add(normjcb);
		
		minLabel = new JLabel ("MIN");
		GridBagConstraints cminLabel = new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(minLabel, cminLabel);
		main.add(minLabel);
		
		minjcb = new JCheckBox();
		GridBagConstraints cminjcb = new GridBagConstraints(2,1,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(minjcb, cminjcb);
		main.add(minjcb);
		
		maxLabel = new JLabel ("MAX");
		GridBagConstraints cmaxLabel = new GridBagConstraints(3,1,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(maxLabel, cmaxLabel);
		main.add(maxLabel);
		
		maxjcb = new JCheckBox();
		GridBagConstraints cmaxjcb = new GridBagConstraints(4,1,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(maxjcb, cmaxjcb);
		main.add(maxjcb);
		
		frechetLabel = new JLabel ("FRECHET");
		GridBagConstraints cfrechetLabel = new GridBagConstraints(5,1,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(frechetLabel, cfrechetLabel);
		main.add(frechetLabel);
		
		frechetjcb = new JCheckBox();
		GridBagConstraints cfrechetjcb = new GridBagConstraints(6,1,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(frechetjcb, cfrechetjcb);
		main.add(frechetjcb);
		
		distLabel = new JLabel ("DIST");
		GridBagConstraints cdistLabel = new GridBagConstraints(7,1,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(distLabel, cdistLabel);
		main.add(distLabel);
		
		distjcb = new JCheckBox();
		GridBagConstraints cdistjcb = new GridBagConstraints(8,1,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(distjcb, cdistjcb);
		main.add(distjcb);
		
		fpcLabel = new JLabel ("FPC");
		GridBagConstraints cfpcLabel = new GridBagConstraints(1,2,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(fpcLabel, cfpcLabel);
		main.add(fpcLabel);
		
		fpcjcb = new JCheckBox();
		GridBagConstraints cfpcjcb = new GridBagConstraints(2,2,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(fpcjcb, cfpcjcb);
		main.add(fpcjcb);		
		
		fppLabel = new JLabel ("FPP");
		GridBagConstraints cfppLabel = new GridBagConstraints(3,2,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(fppLabel, cfppLabel);
		main.add(fppLabel);
		
		fppjcb = new JCheckBox();
		GridBagConstraints cfppjcb = new GridBagConstraints(4,2,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(fppjcb, cfppjcb);
		main.add(fppjcb);
		
		fncLabel = new JLabel ("FNC");
		GridBagConstraints cfncLabel = new GridBagConstraints(5,2,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(fncLabel, cfncLabel);
		main.add(fncLabel);
		
		fncjcb = new JCheckBox();
		GridBagConstraints cfncjcb = new GridBagConstraints(6,2,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(fncjcb, cfncjcb);
		main.add(fncjcb);		
		
		fnpLabel = new JLabel ("FNP");
		GridBagConstraints cfnpLabel = new GridBagConstraints(7,2,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(fnpLabel, cfnpLabel);
		main.add(fnpLabel);
		
		fnpjcb = new JCheckBox();
		GridBagConstraints cfnpjcb = new GridBagConstraints(8,2,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(fnpjcb, cfnpjcb);
		main.add(fnpjcb);
		
		
		
		
		compareLabel = new JLabel ("Select the objects you want to compare with the reference");
		GridBagConstraints cSelectLabel = new GridBagConstraints(1,11,12,1,0,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(compareLabel, cSelectLabel);
		main.add(compareLabel);
			
		this.definejp();
					
		
			
		ok = new JButton("Compare");
		GridBagConstraints cok = new GridBagConstraints(1,18,4,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(ok, cok);
		main.add(ok);
			
		cancel = new JButton("Cancel");
		GridBagConstraints ccancel = new GridBagConstraints(5,18,4,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(cancel, ccancel);
		main.add(cancel);
		
		ok.addActionListener(this);
		cancel.addActionListener(this);

		this.setAlwaysOnTop(true);
		pack();
		this.setVisible(true);
		
		
	}
	
	public void definejp()
	{
		if(jsp!=null)
			main.remove(jsp);
		jp = new JPanel();
		int nb = parent.getMHMTResults().size()+parent.getProcessResults().size();
		jp.setLayout(new GridLayout(nb, 2));
		jp.setPreferredSize(new Dimension(450,nb*15));
		for(int i=0;i<parent.getProcessResults().size();i++)
		{
			JLabel tmplabel = new JLabel(parent.getProcessNames().get(i));
			JCheckBox tmpjcb = new JCheckBox();	
			processjcb.add(tmpjcb);
			jp.add(tmplabel);
			jp.add(tmpjcb);			
		}
		for(int i=0;i<parent.getMHMTResults().size();i++)
		{
			JLabel tmplabel = new JLabel(parent.getMHMTResultDefinitions().get(i).getName());
			JCheckBox tmpjcb = new JCheckBox();	
			mHMTjcb.add(tmpjcb);
			jp.add(tmplabel);
			jp.add(tmpjcb);			
		}
		
		jsp = new JScrollPane(jp, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setPreferredSize(new Dimension(480,200));
		GridBagConstraints cjsp = new GridBagConstraints(1,12,12,5,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(jsp, cjsp);
		main.add(jsp);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==cancel)
			this.dispose();
		if(e.getSource()==ok)
		{
			this.dispose();
			parent.increaseProcessCount();
			Vector<String> columnNames = new Vector<String>();
			columnNames.add("Process");
			if(minjcb.isSelected())
				columnNames.add("MIN");
			if(maxjcb.isSelected())
				columnNames.add("MAX");
			if(frechetjcb.isSelected())
				columnNames.add("FRECHET");
			if(distjcb.isSelected())
				columnNames.add("DIST");
			if(fppjcb.isSelected())
				columnNames.add("FPP");
			if(fpcjcb.isSelected())
				columnNames.add("FPC");
			if(fnpjcb.isSelected())
				columnNames.add("FNP");
			if(fncjcb.isSelected())
				columnNames.add("FNC");
			
			Vector<Vector<String>> rowData = new Vector<Vector<String>>();
			
			if(parent.getProcessResults().size()!=0)
			{
				for(int i=0;i<parent.getProcessResults().size();i++)
					if(processjcb.get(i).isSelected())
					{
						Vector<String> strtmp = new Vector<String>();
						strtmp.add(parent.getProcessNames().get(i));
						if(minjcb.isSelected())
							strtmp.add(String.valueOf(new PerformanceIndex().process(parent.getReference(), parent.getProcessResults().get(i), PerformanceIndex.MIN, normjcb.isSelected())));
						if(maxjcb.isSelected())
							strtmp.add(String.valueOf(new PerformanceIndex().process(parent.getReference(), parent.getProcessResults().get(i), PerformanceIndex.MAX, normjcb.isSelected())));
						if(frechetjcb.isSelected())
							strtmp.add(String.valueOf(new PerformanceIndex().process(parent.getReference(), parent.getProcessResults().get(i), PerformanceIndex.FRECHET, normjcb.isSelected())));
						if(distjcb.isSelected())
							strtmp.add(String.valueOf(new PerformanceIndex().process(parent.getReference(), parent.getProcessResults().get(i), PerformanceIndex.DIST, normjcb.isSelected())));
						if(fppjcb.isSelected())
							strtmp.add(String.valueOf(new PerformanceIndex().process(parent.getReference(), parent.getProcessResults().get(i), PerformanceIndex.FPP, normjcb.isSelected())));
						if(fpcjcb.isSelected())
							strtmp.add(String.valueOf(new PerformanceIndex().process(parent.getReference(), parent.getProcessResults().get(i), PerformanceIndex.FPC, normjcb.isSelected())));
						if(fnpjcb.isSelected())
							strtmp.add(String.valueOf(new PerformanceIndex().process(parent.getReference(), parent.getProcessResults().get(i), PerformanceIndex.FNP, normjcb.isSelected())));
						if(fncjcb.isSelected())
							strtmp.add(String.valueOf(new PerformanceIndex().process(parent.getReference(), parent.getProcessResults().get(i), PerformanceIndex.FNC, normjcb.isSelected())));
						rowData.add(strtmp);
					}
			}
			
			if(parent.getMHMTResults().size()!=0)
			{
				for(int i=0;i<parent.getMHMTResults().size();i++)
					if(mHMTjcb.get(i).isSelected())
					{
						Vector<String> strtmp = new Vector<String>();
						strtmp.add(parent.getMHMTResultDefinitions().get(i).getName());
						if(minjcb.isSelected())
							strtmp.add(String.valueOf(new PerformanceIndex().process(parent.getReference(), parent.getMHMTResults().get(i), PerformanceIndex.MIN, normjcb.isSelected())));
						if(maxjcb.isSelected())
							strtmp.add(String.valueOf(new PerformanceIndex().process(parent.getReference(), parent.getMHMTResults().get(i), PerformanceIndex.MAX, normjcb.isSelected())));
						if(frechetjcb.isSelected())
							strtmp.add(String.valueOf(new PerformanceIndex().process(parent.getReference(), parent.getMHMTResults().get(i), PerformanceIndex.FRECHET, normjcb.isSelected())));
						if(distjcb.isSelected())
							strtmp.add(String.valueOf(new PerformanceIndex().process(parent.getReference(), parent.getMHMTResults().get(i), PerformanceIndex.DIST, normjcb.isSelected())));
						if(fppjcb.isSelected())
							strtmp.add(String.valueOf(new PerformanceIndex().process(parent.getReference(), parent.getMHMTResults().get(i), PerformanceIndex.FPP, normjcb.isSelected())));
						if(fpcjcb.isSelected())
							strtmp.add(String.valueOf(new PerformanceIndex().process(parent.getReference(), parent.getMHMTResults().get(i), PerformanceIndex.FPC, normjcb.isSelected())));
						if(fnpjcb.isSelected())
							strtmp.add(String.valueOf(new PerformanceIndex().process(parent.getReference(), parent.getMHMTResults().get(i), PerformanceIndex.FNP, normjcb.isSelected())));
						if(fncjcb.isSelected())
							strtmp.add(String.valueOf(new PerformanceIndex().process(parent.getReference(), parent.getMHMTResults().get(i), PerformanceIndex.FNC, normjcb.isSelected())));
						rowData.add(strtmp);
					}
			}
				
			JTable results = new JTable(rowData,columnNames);
			ArrayList<String> colN= new ArrayList<String>();
			for(int count=0;count<columnNames.size();count++)
				colN.add(columnNames.elementAt(count));
			new EvaluationPanel(results,colN);
			parent.decreaseProcessCount();
			
		}

	}
	
	public static void getInstance(CoastalGUI cgui)
	{
		if(instance==null)
			instance=new PerformanceIndexPanel(cgui);
		else if(instance.isVisible())
		{
			instance.setVisible(true);
		}
		else
		{
			instance.dispose();
			instance=new PerformanceIndexPanel(cgui);
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
