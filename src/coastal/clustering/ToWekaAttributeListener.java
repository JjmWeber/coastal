package coastal.clustering;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import coastal.CoastalGUI;

import weka.core.Instances;
import weka.gui.beans.AttributeSummarizer;
import fr.unistra.pelican.Image;

public class ToWekaAttributeListener implements ActionListener 
{
    public CoastalGUI IHM;
    public Image displayed;
    public Image satellite;
    private Instances dataSet;
	    
	public ToWekaAttributeListener(CoastalGUI appli)
	{
		this.IHM = appli;
	}
	

    public void actionPerformed(ActionEvent contexte)
    {	
    	satellite = this.IHM.getSatellite();
		
		if(satellite==null)
		{
			JOptionPane.showMessageDialog(IHM,
				    "Vous devez d'abord charger une image",
				    "Alert",
				    JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			dataSet = IHM.createArff();
			
			// Affichage des donneés :
			try {		
				JFrame jf = createJFrameAttributeSummarizer(dataSet);
				jf.setSize(800, 600);
				jf.setVisible(true);
			}
			catch (Exception e) {
				System.out.println(e.toString());
			}
			
		}
		
    }
    
	/**
	 * Ouvre le visualiseur a partir d'un jeu d'essai sous forme d'objet Instances
	 * @param inst Instances
	 * @return JFrame
	 * @throws Exception
	 */
	public static JFrame createJFrameAttributeSummarizer(Instances dataSet) throws Exception
	{
		AttributeSummarizer as;
		as = new AttributeSummarizer();
		as.setInstances(dataSet);
		JFrame jf = new JFrame("Weka Explorer :");
		jf.add(as);
		jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		return jf;
	}
}