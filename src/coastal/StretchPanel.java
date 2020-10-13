package coastal;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class StretchPanel extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = -5751006610363081947L;
	
	private CoastalGUI parent;
	
	private JLabel percentStretchLabel;
	

	
	private JTextField percentStretchValue;
	
	private JButton launch;
	private JButton cancel;
	
	private int percentStretch;
	
	private static StretchPanel instance=null;
	
	private StretchPanel(CoastalGUI cgui)
	{
		this.parent = cgui;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Band Stretching");
		
		JPanel main = new JPanel();		
		main.setPreferredSize(new Dimension(300,40));
		main.setLayout(new GridLayout(2, 2));
		this.setContentPane(main);
		
	
	
		percentStretch=parent.getPercentStretchCut();
		
		percentStretchLabel = new JLabel("Percent ");
		percentStretchValue = new JTextField(String.valueOf(percentStretch));
		
		
		launch = new JButton("Launch");
		cancel = new JButton("Cancel");
		

		main.add(percentStretchLabel);
		main.add(percentStretchValue);
		main.add(launch);
		main.add(cancel);
		
		JTextFieldListener jtfl = new JTextFieldListener();
		
		cancel.addActionListener(this);
		launch.addActionListener(this);

		percentStretchValue.addKeyListener(jtfl);
		


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
				parent.setPercentStretchCut(percentStretch);
			}
		
		
	}
	
	
	//private class JTextFieldListener implements ActionListener
	private class JTextFieldListener implements KeyListener
	{
		//public void actionPerformed(ActionEvent e) 
		public void keyTyped(KeyEvent arg0) {
		}

		public void keyPressed(KeyEvent arg0) {
		}

		public void keyReleased(KeyEvent arg0) {
			String t = percentStretchValue.getText();
			if(t.length()>0)
			{
				boolean charOk=true;
				for(int i=0;i<t.length();i++)
					if(t.charAt(i)<'0'||t.charAt(i)>'9')
						charOk=false;
				if(charOk)
				{
					if(Integer.parseInt(percentStretchValue.getText())<51&&Integer.parseInt(percentStretchValue.getText())>=0)
					{
						percentStretch = Integer.parseInt(percentStretchValue.getText());
					} else
					{
						percentStretch = parent.getPercentStretchCut();
						percentStretchValue.setText(String.valueOf(percentStretch));
						JOptionPane.showMessageDialog(percentStretchValue,
								"The percentage of points to cut must be between 0 and 50",
								"Stretching : Wrong values",
								JOptionPane.ERROR_MESSAGE);
					}
				}else
				{
					percentStretch = parent.getPercentStretchCut();
					percentStretchValue.setText(String.valueOf(percentStretch));
					JOptionPane.showMessageDialog(percentStretchValue,
							"The percentage of points to cut must be a numeric value between 0 and 50",
							"Stretching : Non-numeric values",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			
		}

		
	}
	
	public static void getInstance(CoastalGUI cgui)
	{
		if(instance==null||!instance.isVisible())
			instance=new StretchPanel(cgui);
		else
			instance.setVisible(true);
	}

}

