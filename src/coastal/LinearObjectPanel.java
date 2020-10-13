package coastal;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import fr.unistra.pelican.util.detection.MHMTDetectionParameters;

public class LinearObjectPanel extends JFrame implements ActionListener{

	private static final long serialVersionUID = -5751006610363081947L;
	
	private CoastalGUI parent;
	
	private JLabel nameLabel;
	private JLabel resolutionLabel;
	private JLabel modifyLabel;
	private JLabel deleteLabel;
	private JLabel duplicateLabel;
	
	private JTextField nameText;
	private JTextField resolutionText;
	
	private JComboBox modifyjcc;
	private JComboBox deletejcc;
	private JComboBox duplicatejcc;
	
	private JButton addSE;
	private JButton modifySE;
	private JButton deleteSE;
	private JButton validate;
	private JButton cancel;
	private JButton duplicateSE;
	
	private JScrollPane jsp;
	
	private JTable sETable;
	
	private ArrayList<MHMTDetectionParameters> strictmhmtdp;
	private ArrayList<MHMTDetectionParameters> softmhmtdp;
	
	private double resolution=0;
	
	private boolean modify=false;
	
	private int index;

	private static LinearObjectPanel instance=null;
	
	private LinearObjectPanel(CoastalGUI cgui)
	{
		parent=cgui;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Define new linear object");
		
		JPanel main = new JPanel();		
		main.setPreferredSize(new Dimension(480,260));
		GridBagLayout gbl = new GridBagLayout();		
		main.setLayout(gbl);
		this.setContentPane(main);
		
		
		nameLabel = new JLabel ("Name ");
		GridBagConstraints cNameLabel = new GridBagConstraints(1,1,5,1,0,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(nameLabel, cNameLabel);
		main.add(nameLabel);
		
		nameText = new JTextField("");
		nameText.setColumns(20);
		GridBagConstraints cNameText = new GridBagConstraints(7,1,8,1,0,0,GridBagConstraints.EAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(nameText, cNameText);
		main.add(nameText);
		
		resolutionLabel = new JLabel ("Resolution  (m) ");
		GridBagConstraints cResolutionLabel = new GridBagConstraints(1,2,5,1,0,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(resolutionLabel, cResolutionLabel);
		main.add(resolutionLabel);
		
		resolutionText = new JTextField("");
		GridBagConstraints cResolutionText = new GridBagConstraints(7,2,8,1,0,0,GridBagConstraints.EAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(resolutionText, cResolutionText);
		main.add(resolutionText);

		strictmhmtdp = new ArrayList<MHMTDetectionParameters>();
		softmhmtdp = new ArrayList<MHMTDetectionParameters>();
		
		this.defineSETable();
		
		jsp = new JScrollPane(sETable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		 		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setPreferredSize(new Dimension(480,100));
		
		GridBagConstraints cjsp = new GridBagConstraints(1,3,14,6,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(jsp, cjsp);
		main.add(jsp);
		
		addSE = new JButton("Add SE");
		GridBagConstraints cAddSE = new GridBagConstraints(1,9,4,1,0,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(addSE, cAddSE);
		main.add(addSE);
		

		
		modifyLabel = new JLabel("Modify ");
		GridBagConstraints cModifyLabel = new GridBagConstraints(1,10,4,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(modifyLabel, cModifyLabel);
		main.add(modifyLabel);
		
		modifyjcc = new JComboBox();
		GridBagConstraints cModifyjcc = new GridBagConstraints(5,10,4,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(modifyjcc, cModifyjcc);
		main.add(modifyjcc);
		
		modifySE = new JButton("OK");
		GridBagConstraints cModifySE = new GridBagConstraints(9,10,2,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(modifySE, cModifySE);
		main.add(modifySE);
		
		deleteLabel = new JLabel("Delete ");
		GridBagConstraints cDeleteLabel = new GridBagConstraints(1,11,4,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(deleteLabel, cDeleteLabel);
		main.add(deleteLabel);
		
		deletejcc = new JComboBox();
		GridBagConstraints cDeletejcc = new GridBagConstraints(5,11,4,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(deletejcc, cDeletejcc);
		main.add(deletejcc);
				
		deleteSE = new JButton("OK");
		GridBagConstraints cDeleteSE = new GridBagConstraints(9,11,2,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(deleteSE, cDeleteSE);
		main.add(deleteSE);
		
		duplicateLabel = new JLabel("Duplicate ");
		GridBagConstraints cDuplicateLabel = new GridBagConstraints(1,12,4,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(duplicateLabel, cDuplicateLabel);
		main.add(duplicateLabel);
		
		duplicatejcc = new JComboBox();
		GridBagConstraints cDuplicatejcc = new GridBagConstraints(5,12,4,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(duplicatejcc, cDuplicatejcc);
		main.add(duplicatejcc);
				
		duplicateSE = new JButton("OK");
		GridBagConstraints cDuplicateSE = new GridBagConstraints(9,12,2,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		gbl.setConstraints(duplicateSE, cDuplicateSE);
		main.add(duplicateSE);
		
		validate = new JButton("Validate");
		GridBagConstraints cValidate = new GridBagConstraints(13,13,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(0,0,0,2),0,0);
		gbl.setConstraints(validate, cValidate);
		main.add(validate);
		
		cancel = new JButton("Cancel");
		GridBagConstraints cCancel = new GridBagConstraints(14,13,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,2),0,0);
		gbl.setConstraints(cancel, cCancel);
		main.add(cancel);
		
		
		addSE.addActionListener(this);
		deleteSE.addActionListener(this);
		modifySE.addActionListener(this);
		duplicateSE.addActionListener(this);
		cancel.addActionListener(this);
		validate.addActionListener(this);
		
		JTextFieldListener jtfl = new JTextFieldListener();
		
		resolutionText.addKeyListener(jtfl);
		

		this.setAlwaysOnTop(true);
		pack();
		this.setVisible(true);

	}
	
	private LinearObjectPanel(CoastalGUI cgui,int index)
	{
		this(cgui);
		modify=true;
		this.index=index;
		LinearObjectDefinition lod = parent.getLinearObj().get(index);
		nameText.setText(lod.getName());
		resolution = lod.getResolution();
		strictmhmtdp = lod.getStrictParameters();
		softmhmtdp = lod.getSoftParameters();
		resolutionText.setText(String.valueOf(resolution));
		this.defineSETable();
	}


	public void actionPerformed(ActionEvent e) {
		//Cancel
		if(((JButton) e.getSource()).getText()=="Cancel")
			this.dispose();
		
		//Add SE
		if(((JButton) e.getSource())==addSE)
		{
			if(resolution!=0)
				SEPanel.getInstance(this);
			else
				JOptionPane.showMessageDialog((JButton)e.getSource(),
						"Before adding SE, you must choose the resolution which had to be greater than 0",
						"Resolution needed",
						JOptionPane.ERROR_MESSAGE);
		}
		//Delete SE
		if(((JButton) e.getSource())==deleteSE)
		{
			if(strictmhmtdp.size()+softmhmtdp.size()==0)
				JOptionPane.showMessageDialog((JButton)e.getSource(),
						"Before deleting SE, you must have defined SEs...",
						"No SEs defined",
						JOptionPane.ERROR_MESSAGE);
			else
			{
				if(deletejcc.getSelectedIndex()<strictmhmtdp.size())
				{
					delStrictMhmtdp(deletejcc.getSelectedIndex());
				}
				else
				{
					delSoftMhmtdp(deletejcc.getSelectedIndex()-strictmhmtdp.size());
				}
				
			}
		}
		//Modify SE
		if(((JButton) e.getSource())==modifySE)
		{
			if(strictmhmtdp.size()+softmhmtdp.size()==0)
				JOptionPane.showMessageDialog((JButton)e.getSource(),
						"Before modifying SE, you must have defined SEs...",
						"No SEs defined",
						JOptionPane.ERROR_MESSAGE);
			else
				if(modifyjcc.getSelectedIndex()<strictmhmtdp.size())
				{
					SEPanel.getInstance(this,modifyjcc.getSelectedIndex(),SEPanel.STRICT);
				}
				else
				{
					SEPanel.getInstance(this,modifyjcc.getSelectedIndex()-strictmhmtdp.size(),SEPanel.SOFT);
				}
				
		}
		//Duplicate SE
		if(((JButton) e.getSource())==duplicateSE)
		{
			if(strictmhmtdp.size()+softmhmtdp.size()==0)
				JOptionPane.showMessageDialog((JButton)e.getSource(),
						"Before duplicating SE, you must have defined SEs...",
						"No SEs defined",
						JOptionPane.ERROR_MESSAGE);
			else
				if(duplicatejcc.getSelectedIndex()<strictmhmtdp.size())
					this.addMHMTDP(new MHMTDetectionParameters(strictmhmtdp.get(duplicatejcc.getSelectedIndex()).getBand(),strictmhmtdp.get(duplicatejcc.getSelectedIndex()).getThresh(),strictmhmtdp.get(duplicatejcc.getSelectedIndex()).isErosion(),strictmhmtdp.get(duplicatejcc.getSelectedIndex()).getSegmentShift(),strictmhmtdp.get(duplicatejcc.getSelectedIndex()).getSegmentLength()), SEPanel.STRICT);
				else
					this.addMHMTDP(new MHMTDetectionParameters(softmhmtdp.get(duplicatejcc.getSelectedIndex()-strictmhmtdp.size()).getBand(),softmhmtdp.get(duplicatejcc.getSelectedIndex()-strictmhmtdp.size()).getThresh(),softmhmtdp.get(duplicatejcc.getSelectedIndex()-strictmhmtdp.size()).isErosion(),softmhmtdp.get(duplicatejcc.getSelectedIndex()-strictmhmtdp.size()).getSegmentShift(),softmhmtdp.get(duplicatejcc.getSelectedIndex()-strictmhmtdp.size()).getSegmentLength()), SEPanel.SOFT);
			
		}
		
		//Validate SE
		if(((JButton) e.getSource())==validate)
		{
			if(nameText.getText().length()==0)
			{
				JOptionPane.showMessageDialog((JButton)e.getSource(),
						"Your must choose a name",
						"No name",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(resolution==0)
			{
				JOptionPane.showMessageDialog((JButton)e.getSource(),
						"Your must choose a resolution",
						"No resolution",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(strictmhmtdp.size()+softmhmtdp.size()==0)
			{
				JOptionPane.showMessageDialog((JButton)e.getSource(),
						"Your must add SEs",
						"No SEs",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			LinearObjectDefinition lod = new LinearObjectDefinition(nameText.getText(),resolution,strictmhmtdp, softmhmtdp);
			if(modify)
				parent.modifyLinearObjectDefinition(lod,index);
			else
				parent.addLinearObjectDefinition(lod);
			this.dispose();
		}
		
	}

	public void addMHMTDP(MHMTDetectionParameters param, int type)
	{
		if(type==SEPanel.STRICT)
			strictmhmtdp.add(param);
		else
			softmhmtdp.add(param);
		this.defineSETable();
	}

	public void defineSETable()
	{
		Vector<Vector<String>> vectVal = new Vector<Vector<String>>();
		Vector<String> vectStr = new Vector<String>();
		
		vectStr.add("Index");
		vectStr.add("Band");
		vectStr.add("Threshold");
		vectStr.add("Type");
		vectStr.add("Shift");
		vectStr.add("Length");
		vectStr.add("Options");
		
		if(strictmhmtdp.size()+softmhmtdp.size()!=0)
		{
			this.getContentPane().remove(modifyjcc);
			modifyjcc = new JComboBox();
			GridBagConstraints cModifyjcc = new GridBagConstraints(5,10,4,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
			((GridBagLayout)this.getContentPane().getLayout()).setConstraints(modifyjcc, cModifyjcc);
			this.getContentPane().add(modifyjcc);
			this.getContentPane().remove(deletejcc);
			deletejcc = new JComboBox();
			GridBagConstraints cDeletejcc = new GridBagConstraints(5,11,4,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
			((GridBagLayout)this.getContentPane().getLayout()).setConstraints(deletejcc, cDeletejcc);
			this.getContentPane().add(deletejcc);
			this.getContentPane().remove(duplicatejcc);
			duplicatejcc = new JComboBox();
			GridBagConstraints cDuplicatejcc = new GridBagConstraints(5,12,4,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
			((GridBagLayout)this.getContentPane().getLayout()).setConstraints(duplicatejcc, cDuplicatejcc);
			this.getContentPane().add(duplicatejcc);
			for(int i=0;i<strictmhmtdp.size();i++)
			{
				Vector<String> vectTmp = new Vector<String>();
				MHMTDetectionParameters mhmt = strictmhmtdp.get(i);
				vectTmp.add(String.valueOf(i+1));
				vectTmp.add(String.valueOf(mhmt.getBand()+1));
				vectTmp.add(String.valueOf(Math.round(mhmt.getThresh()*255)));
				if(mhmt.isErosion())
					vectTmp.add("lower");
				else
					vectTmp.add("upper");
				vectTmp.add(String.valueOf(((double)mhmt.getSegmentShift())*resolution));
				vectTmp.add(String.valueOf(((double)mhmt.getSegmentLength())*resolution));
				vectTmp.add("strict");
				vectVal.add(vectTmp);
				modifyjcc.addItem("SE "+(i+1));
				deletejcc.addItem("SE "+(i+1));
				duplicatejcc.addItem("SE "+(i+1));
			}
			for(int i=0;i<softmhmtdp.size();i++)
			{
				Vector<String> vectTmp = new Vector<String>();
				MHMTDetectionParameters mhmt = softmhmtdp.get(i);
				vectTmp.add(String.valueOf(i+1+strictmhmtdp.size()));
				vectTmp.add(String.valueOf(mhmt.getBand()+1));
				vectTmp.add(String.valueOf(Math.round(mhmt.getThresh()*255)));
				if(mhmt.isErosion())
					vectTmp.add("lower");
				else
					vectTmp.add("upper");
				vectTmp.add(String.valueOf(((double)mhmt.getSegmentShift())*resolution));
				vectTmp.add(String.valueOf(((double)mhmt.getSegmentLength())*resolution));
				vectTmp.add("soft");
				vectVal.add(vectTmp);
				modifyjcc.addItem("SE "+(i+strictmhmtdp.size()+1));
				deletejcc.addItem("SE "+(i+strictmhmtdp.size()+1));
				duplicatejcc.addItem("SE "+(i+strictmhmtdp.size()+1));
			}
			
		}
		
		sETable = new JTable(vectVal,vectStr);
		
		
		if(jsp!=null)
		{
			this.getContentPane().remove(jsp);
			
			jsp = new JScrollPane(sETable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			 		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jsp.setPreferredSize(new Dimension(480,100));
			
			GridBagConstraints cjsp = new GridBagConstraints(1,3,14,6,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
			((GridBagLayout)this.getContentPane().getLayout()).setConstraints(jsp, cjsp);
			this.getContentPane().add(jsp);
			this.pack();
			this.setVisible(true);
		}
		sETable.setEnabled(false);
	}
	
	public CoastalGUI getParent() {
		return parent;
	}
	
	private class JTextFieldListener implements KeyListener
	{
		
		public void keyTyped(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
			String t = ((JTextField)e.getSource()).getText();
			if(t.length()>0)
			{
				try{
					resolution=Double.parseDouble(t);
				}
				catch(NumberFormatException ex)
				{
					JOptionPane.showMessageDialog((JTextField)e.getSource(),
							"The resolution must be a positive double value",
							"Resolution : Bad values",
							JOptionPane.ERROR_MESSAGE);
					resolutionText.setText("");
					resolution=0;
					return;
				}
			}						
		}

		
	}

	public ArrayList<MHMTDetectionParameters> getStrictMhmtdp() {
		return strictmhmtdp;
	}
	
	public ArrayList<MHMTDetectionParameters> getSoftMhmtdp() {
		return softmhmtdp;
	}


	public void delStrictMhmtdp(int index) {
		strictmhmtdp.remove(index);
		this.defineSETable();
	}
	
	public void delSoftMhmtdp(int index) {
		softmhmtdp.remove(index);
		this.defineSETable();
	}


	public void modifyMHMTDP(MHMTDetectionParameters parameters, int index, int type,int initialType) {
		if(initialType==type)
			if(type==SEPanel.STRICT)
				strictmhmtdp.set(index, parameters);
			else
				softmhmtdp.set(index, parameters);
		else
			if(initialType==SEPanel.STRICT)
			{
				strictmhmtdp.remove(index);
				softmhmtdp.add(parameters);
			}
			else
			{
				softmhmtdp.remove(index);
				strictmhmtdp.add(parameters);
			}
		
		
		
		strictmhmtdp.set(index, parameters);
		this.defineSETable();
	}
	
	
	public double getResolution() {
		return resolution;
	}
	
	public static void getInstance(CoastalGUI cgui)
	{
		if(instance==null)
			instance=new LinearObjectPanel(cgui);
		else
		{
			instance.dispose();
			instance=new LinearObjectPanel(cgui);
		}
	}
	
	public static void getInstance(CoastalGUI cgui, int index)
	{
		if(instance==null)
			instance=new LinearObjectPanel(cgui,index);
		else
		{
			instance.dispose();
			instance=new LinearObjectPanel(cgui,index);
		}
			
	}
	
}
