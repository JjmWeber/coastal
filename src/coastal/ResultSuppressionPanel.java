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

public class ResultSuppressionPanel extends JFrame implements ActionListener{

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
	
	private static ResultSuppressionPanel instance=null;
	
	private ResultSuppressionPanel(CoastalGUI cgui)
	{
		this.parent=cgui;
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Delete results");
		
		mHMTjcb = new ArrayList<JCheckBox>();
		processjcb = new ArrayList<JCheckBox>();
		
		main = new JPanel();		
		main.setPreferredSize(new Dimension(480,260));
		gbl = new GridBagLayout();		
		main.setLayout(gbl);
		this.setContentPane(main);
		
		saveLabel = new JLabel ("Select the results you want to delete");
		GridBagConstraints cSelectLabel = new GridBagConstraints(1,1,5,1,0,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(saveLabel, cSelectLabel);
		main.add(saveLabel);
		
		this.definejp();
				
		ok = new JButton("Delete");
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
			JLabel tmplabel = new JLabel(parent.getMHMTResultDefinitions().get(i).getName());
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
	
	private void deleteResults()
	{
		int deletedItems =0;
	    if(processjcb.size()!=0)
	       for(int i=0;i<processjcb.size();i++)
	    	   if(processjcb.get(i).isSelected())
	    	   {
	    			 parent.delProcess(i-deletedItems);
	    			 deletedItems++;
	    	   }
	    deletedItems = 0;
	    if(mHMTjcb.size()!=0)
	       for(int i=0;i<mHMTjcb.size();i++)
	    	   if(mHMTjcb.get(i).isSelected())
	    	   {
	    			 parent.delMHMT(i-deletedItems);
	    			 deletedItems++;
	    	   }	 
	    this.dispose();
	}
	
		
	public void actionPerformed(ActionEvent e) {
		//Cancel
		if((JButton)e.getSource()==cancel)
			this.dispose();
		//Ok
		if((JButton)e.getSource()==ok)
			this.deleteResults();
		
	}
	
	public static void getInstance(CoastalGUI cgui)
	{
		if(instance==null)
			instance=new ResultSuppressionPanel(cgui);
		else if(instance.isVisible())
		{
			instance.setVisible(true);
		}
		else
		{
			instance.dispose();
			instance=new ResultSuppressionPanel(cgui);
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
