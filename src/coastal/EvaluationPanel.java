package coastal;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

public class EvaluationPanel extends JFrame implements ActionListener {

	private static final long serialVersionUID = -5967879491383153472L;

	private JTable results;
	
	private JScrollPane jsp;
	
	private JButton save;
	
	private ArrayList<String> colNames;
	
	
	public EvaluationPanel(JTable res, ArrayList<String> colN)
	{
		this.results=res;
		results.setEnabled(false);
		colNames = colN;
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Evaluation results");
		
		JPanel main = new JPanel();		
		main.setPreferredSize(new Dimension(700,160));
		main.setLayout(new BorderLayout());
		this.setContentPane(main);
		
		jsp = new JScrollPane(results, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		 		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setPreferredSize(new Dimension(480,100));
		main.add(jsp,BorderLayout.CENTER);
		
		save= new JButton("Save in csv");
		main.add(save,BorderLayout.SOUTH);
		
		save.addActionListener(this);

		this.setAlwaysOnTop(true);
		pack();
		this.setVisible(true);		
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==save)
		{
			String csv = new String();
			for(int i=0;i<colNames.size();i++)
				if(i!=colNames.size()-1)
					csv=csv+colNames.get(i)+",";
				else
					csv=csv+colNames.get(i);
			csv=csv+"\n";
			for(int row=0;row<results.getRowCount();row++)
			{
				for(int col=0;col<results.getColumnCount();col++)
					if(col!=results.getColumnCount()-1)
						csv=csv+results.getValueAt(row, col)+",";
					else
						csv=csv+results.getValueAt(row, col);
				csv=csv+"\n";
			}
			
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Choose where to save the evaluation results");
			
			int returnVal = chooser.showSaveDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		       System.out.println("You save the evaluation results here : " + chooser.getCurrentDirectory()+File.separator+
		            chooser.getSelectedFile().getName());
		       try{
		    	   FileWriter fr = new FileWriter(chooser.getSelectedFile().
		    			   getAbsolutePath());
		    	   fr.write(csv);
		    	   fr.close();
		       }
		       catch (FileNotFoundException ex) {} catch (HeadlessException ex) {} catch (
		                IOException ex) {}
		    }
			this.dispose();
		}

	}

}
