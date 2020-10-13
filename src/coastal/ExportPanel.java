package coastal;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import fr.unistra.pelican.algorithms.io.ImageSave;

public class ExportPanel extends JFrame implements ActionListener{

	private static final long serialVersionUID = 0L;

	private CoastalGUI parent;
	
	private JLabel saveLabel;
	
	private JScrollPane jsp;
	
	private ArrayList<JCheckBox> mHMTjcb;
	private ArrayList<JCheckBox> processjcb;
	
	private JPanel jp;
	private JPanel main;

	private GridBagLayout gbl;
	
	private JButton ok;
	private JButton cancel;
	
	private static ExportPanel instance=null;
	
	private ExportPanel(CoastalGUI cgui)
	{
		this.parent=cgui;
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Export results to tiff");
		
		mHMTjcb = new ArrayList<JCheckBox>();
		processjcb = new ArrayList<JCheckBox>();
		
		main = new JPanel();		
		main.setPreferredSize(new Dimension(480,260));
		gbl = new GridBagLayout();		
		main.setLayout(gbl);
		this.setContentPane(main);
		
		saveLabel = new JLabel ("Select the results you want to save");
		GridBagConstraints cSelectLabel = new GridBagConstraints(1,1,5,1,0,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(saveLabel, cSelectLabel);
		main.add(saveLabel);
		
		this.definejp();
				
		
		
		ok = new JButton("Export");
		GridBagConstraints cok = new GridBagConstraints(1,3,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(ok, cok);
		main.add(ok);
		
		cancel = new JButton("Cancel");
		GridBagConstraints ccancel = new GridBagConstraints(2,3,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
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
		int nb = parent.getMHMTResults().size() + parent.getProcessResults().size();
		
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
			JLabel tmplabel;
			if(parent.getMHMTResultType().get(i)==CoastalGUI.ONLY_STRICT)
				tmplabel = new JLabel(parent.getMHMTResultDefinitions().get(i).getName()+"(Only Strict)");
			else
				tmplabel = new JLabel(parent.getMHMTResultDefinitions().get(i).getName()+"(Both)");
			JCheckBox tmpjcb = new JCheckBox();	
			mHMTjcb.add(tmpjcb);
			jp.add(tmplabel);
			jp.add(tmpjcb);
			
		}
		
		jsp = new JScrollPane(jp, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jsp.setPreferredSize(new Dimension(480,200));
			GridBagConstraints cjsp = new GridBagConstraints(1,2,2,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
			gbl.setConstraints(jsp, cjsp);
			main.add(jsp);
	}
	
	private void saveResults()
	{
		String filename = parent.getSatelliteFile().substring(0,parent.getSatelliteFile().length()-4);
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle("Choose where you want to export the results");
		chooser.setApproveButtonText("Export");
		chooser.setCurrentDirectory(new File(parent.getIOPath()));
		int returnVal = chooser.showOpenDialog(this);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	parent.setIOPath(chooser.getCurrentDirectory().getAbsolutePath());
	       System.out.println("You choose to export results here : " + chooser.getSelectedFile()+File.separator);
	       if(processjcb.size()!=0)
	    	   for(int i=0;i<processjcb.size();i++)
	    		   if(processjcb.get(i).isSelected())
	    			 new ImageSave().process(parent.getProcessResults().get(i),chooser.getSelectedFile()+File.separator+filename+"_"+parent.getProcessNames().get(i).replace(' ', '_')+".tiff");  
	    
	       if(mHMTjcb.size()!=0)
	    	   for(int i=0;i<mHMTjcb.size();i++)
	    		   if(mHMTjcb.get(i).isSelected())
	    			 if(parent.getMHMTResultType().get(i)==CoastalGUI.ONLY_STRICT)
	    			   new ImageSave().process(parent.getMHMTResults().get(i),chooser.getSelectedFile()+File.separator+filename+"_"+parent.getMHMTResultDefinitions().get(i).getName().replace(' ', '_')+"_OS"+".tiff");
	    			 else
	    				 new ImageSave().process(parent.getMHMTResults().get(i),chooser.getSelectedFile()+File.separator+filename+"_"+parent.getMHMTResultDefinitions().get(i).getName().replace(' ', '_')+"_BOTH"+".tiff");
	    }
	    this.dispose();
	}
	
		
	public void actionPerformed(ActionEvent e) {
		//Cancel
		if((JButton)e.getSource()==cancel)
			this.dispose();
		//Ok
		if((JButton)e.getSource()==ok)
			this.saveResults();
		
	}
	
	public static void getInstance(CoastalGUI cgui)
	{
		if(instance==null)
			instance=new ExportPanel(cgui);
		else if(instance.isVisible())
		{
			instance.setVisible(true);
		}
		else
		{
			instance.dispose();
			instance=new ExportPanel(cgui);
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
