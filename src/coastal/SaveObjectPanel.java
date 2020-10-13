package coastal;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;


public class SaveObjectPanel extends JFrame implements ActionListener{

	private static final long serialVersionUID = -1401242095000123039L;
	
	private CoastalGUI parent;
		
	private JLabel selectLabel;
		
	private JScrollPane jsp;
		
	private JPanel jp;
		
	private ArrayList<JCheckBox> jcb;
		
	private JButton ok;
	private JButton cancel;
	
	private static SaveObjectPanel instance=null;
		
	private SaveObjectPanel(CoastalGUI cgui)
	{
		this.parent=cgui;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Export objects");
		
		JPanel main = new JPanel();		
		main.setPreferredSize(new Dimension(480,260));
		GridBagLayout gbl = new GridBagLayout();		
		main.setLayout(gbl);
		this.setContentPane(main);
		
		selectLabel = new JLabel ("Select the objects you want to export");
		GridBagConstraints cSelectLabel = new GridBagConstraints(1,1,5,1,0,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(selectLabel, cSelectLabel);
		main.add(selectLabel);
			
		this.definejp();
					
		jsp = new JScrollPane(jp, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setPreferredSize(new Dimension(480,200));
		GridBagConstraints cjsp = new GridBagConstraints(1,2,2,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(jsp, cjsp);
		main.add(jsp);
			
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
	
	private void definejp()
	{
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
				
	}
	
	public void actionPerformed(ActionEvent e) {
		// Cancel
		if((JButton)e.getSource()==cancel)
		this.dispose();
		//Export
		if((JButton)e.getSource()==ok)
		{
			String str = new String();
			str=str.concat("Linear Objects\n\n\n");
			int extractcount = 0;
			for(int i=0;i<jcb.size();i++)
				if(jcb.get(i).isSelected())
					extractcount++;
			extractcount=0;
			for(int i=0;i<jcb.size();i++)
				if(jcb.get(i).isSelected())
				{
					str=str.concat(parent.getLinearObj().get(i).toString());
					str=str.concat("\n\n");
					extractcount++;
				}
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Choose where to export your objects");
			chooser.setCurrentDirectory(new File(parent.getIOPath()));
			
			int returnVal = chooser.showSaveDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	parent.setIOPath(chooser.getCurrentDirectory().getAbsolutePath());
		       System.out.println("You export the objects here : " + chooser.getCurrentDirectory()+File.separator+
		            chooser.getSelectedFile().getName());
		       try{
		    	   FileWriter fr = new FileWriter(chooser.getSelectedFile().
		    			   getAbsolutePath());
		    	   fr.write(str);
		    	   fr.close();
		       }
		       catch (FileNotFoundException ex) {} catch (HeadlessException ex) {} catch (
		                IOException ex) {}
		    }
			this.dispose();
		}
		
	}
	
	public static void getInstance(CoastalGUI cgui)
	{
		if(instance==null)
			instance=new SaveObjectPanel(cgui);
		else
		{
			instance.dispose();
			instance=new SaveObjectPanel(cgui);
		}
	}
	
}
