package coastal;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.unistra.pelican.util.detection.MHMTDetectionParameters;

public class SEPanel extends JFrame implements ActionListener{

	public static final int STRICT = 0;
	public static final int SOFT = 1;
	
	private static final long serialVersionUID = -7915414698440692416L;

	private LinearObjectPanel parent;
	
	private JLabel bandLabel;
	private JLabel threshLabel;
	private JLabel typeLabel;
	private JLabel shiftLabel;
	private JLabel segmentLabel;
	private JLabel sfstLabel;
	
	private JComboBox bandjcc;
	private JComboBox typejcc;
	private JComboBox sfstjcc;
	
	private JTextField threshText;
	private JTextField shiftText;
	private JTextField segmentText;
	
	private JButton ok;
	private JButton cancel;
	
	private int index;
	private int type;
	private int initialType;
	
	private boolean modify=false;
	
	private int band;
	private double threshold;
	private boolean erosion;
	private int shift;
	private int length;
	
	private static SEPanel instance=null;
	
	private SEPanel(LinearObjectPanel parent)
	{
		this.parent = parent;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Add a SE");
		
		JPanel main = new JPanel();		
		main.setPreferredSize(new Dimension(270,125));
		main.setLayout(new GridLayout(7, 2));
		this.setContentPane(main);
		
		bandLabel = new JLabel("Band ");
		bandjcc = new JComboBox();
		bandjcc.setName("band");
		for(int i=0;i<parent.getParent().getSatellite().getBDim();i++)
			bandjcc.addItem(String.valueOf(i+1));
		
		threshLabel = new JLabel("Threshold ");
		threshText = new JTextField("128");
		threshText.setName("Threshold");
		
		typeLabel = new JLabel("Type");
		typejcc= new JComboBox();
		typejcc.setName("type");
		typejcc.addItem("Upper");
		typejcc.addItem("Lower");
		
		shiftLabel = new JLabel("Shift (m)");
		shiftText = new JTextField("1");
		shiftText.setName("Shift");
		
		segmentLabel = new JLabel("Length (m)");
		segmentText = new JTextField("1");
		segmentText.setName("Length");
		
		sfstLabel = new JLabel("Options");
		sfstjcc= new JComboBox();
		sfstjcc.addItem("Strict");
		sfstjcc.addItem("Soft");
		
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		
		main.add(bandLabel);
		main.add(bandjcc);
		main.add(threshLabel);
		main.add(threshText);
		main.add(typeLabel);
		main.add(typejcc);
		main.add(shiftLabel);
		main.add(shiftText);
		main.add(segmentLabel);
		main.add(segmentText);
		main.add(sfstLabel);
		main.add(sfstjcc);
		main.add(ok);
		main.add(cancel);
		
		JTextFieldListener jtfl = new JTextFieldListener();
		
		threshText.addKeyListener(jtfl);
		shiftText.addKeyListener(jtfl);
		segmentText.addKeyListener(jtfl);
		ok.addActionListener(this);
		cancel.addActionListener(this);

		this.setAlwaysOnTop(true);
		pack();
		this.setVisible(true);
		
	}
	
	private SEPanel(LinearObjectPanel parent, int index, int type)
	{
		this(parent);
		modify=true;
		MHMTDetectionParameters mhmtdp;
		this.type=type;
		this.initialType=type;
		if(type==STRICT)
			mhmtdp = parent.getStrictMhmtdp().get(index);
		else
			mhmtdp = parent.getSoftMhmtdp().get(index);
		this.index=index;
		bandjcc.setSelectedIndex(mhmtdp.getBand());
		threshText.setText(String.valueOf(Math.round(mhmtdp.getThresh()*255)));
		if(mhmtdp.isErosion())
			typejcc.setSelectedIndex(1);
		else
			typejcc.setSelectedIndex(0);
		if(type==SEPanel.STRICT)
			sfstjcc.setSelectedIndex(0);
		else
			sfstjcc.setSelectedIndex(1);
		shiftText.setText(String.valueOf(Math.round(mhmtdp.getSegmentShift()*parent.getResolution())));
		segmentText.setText(String.valueOf(Math.round(mhmtdp.getSegmentLength()*parent.getResolution())));
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(((JButton)e.getSource())==cancel)
			this.dispose();
		if(((JButton)e.getSource())==ok)
		{
			
			band=bandjcc.getSelectedIndex();
			try{
				threshold=(Integer.parseInt(threshText.getText()))/255.;
			}
			catch(NumberFormatException ex)
			{
				JOptionPane.showMessageDialog(threshText,
						"The threshold must be non void",
						"Void threshold",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(typejcc.getSelectedIndex()==0)
				erosion=false;
			else
				erosion=true;
			type = sfstjcc.getSelectedIndex();
			try{
			shift=(int)Math.round(Double.parseDouble(shiftText.getText())/parent.getResolution());
			length=(int)Math.round(Double.parseDouble(segmentText.getText())/parent.getResolution());
			}
			catch(NumberFormatException ex)
			{
				JOptionPane.showMessageDialog(shiftText,
						"Shift and length must be valid double",
						"Bad double",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(modify)
				parent.modifyMHMTDP(new MHMTDetectionParameters(band,threshold,erosion,shift,length),index,type,initialType);
			else
				parent.addMHMTDP(new MHMTDetectionParameters(band,threshold,erosion,shift,length),type);
			
			this.dispose();
		}
	}
	

	private class JTextFieldListener implements KeyListener
	{
		
		public void keyTyped(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
			boolean charOk=false;
			String t = ((JTextField)e.getSource()).getText();
			if(t.length()>0&&(JTextField)e.getSource()!=threshText)
			{
				charOk=true;
				for(int i=0;i<t.length();i++)
					if((t.charAt(i)<'0'||t.charAt(i)>'9')&&t.charAt(i)!='-'&&t.charAt(i)!='.')
						charOk=false;
				if(!charOk)
				{
					JOptionPane.showMessageDialog((JTextField)e.getSource(),
							"The "+((JTextField)e.getSource()).getName()+" must be a numeric value",
							"Non-numeric values",
							JOptionPane.ERROR_MESSAGE);
					((JTextField)e.getSource()).setText("1");
				}
			}
			else
			{
				charOk=true;
				for(int i=0;i<t.length();i++)
					if(t.charAt(i)<'0'||t.charAt(i)>'9')
						charOk=false;
				if(!charOk)
				{
					JOptionPane.showMessageDialog((JTextField)e.getSource(),
							"The "+((JTextField)e.getSource()).getName()+" must be a numeric and integer value",
							"Non-integer value",
							JOptionPane.ERROR_MESSAGE);
					((JTextField)e.getSource()).setText("128");
				}
			}
			
			if(((JTextField)e.getSource())==threshText&&charOk)
			{
				if(!((JTextField)e.getSource()).getText().equals(""))
					if(!(Double.parseDouble(threshText.getText())<256&&Double.parseDouble(threshText.getText())>=0))
					{
						threshText.setText("128");
						JOptionPane.showMessageDialog(threshText,
							"The threshold must be between 0 and 255",
							"Wrong values",
							JOptionPane.ERROR_MESSAGE);
					}
			}			
		}
		
	}

	public static void getInstance(LinearObjectPanel lop)
	{
		if(instance==null)
			instance=new SEPanel(lop);
		else
		{
			instance.dispose();
			instance=new SEPanel(lop);
		}
	}
	
	public static void getInstance(LinearObjectPanel lop,int index,int type)
	{
		if(instance==null)
			instance=new SEPanel(lop, index,type);
		else
		{
			instance.dispose();
			instance=new SEPanel(lop, index,type);
		}
	}
}
