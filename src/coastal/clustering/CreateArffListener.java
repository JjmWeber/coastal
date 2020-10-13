package coastal.clustering;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.swing.JOptionPane;

import coastal.CoastalGUI;
import weka.core.Instances;
import fr.unistra.pelican.Image;

public class CreateArffListener implements ActionListener 
{
    public CoastalGUI IHM;
    public Image displayed;
    public Image satellite;
    private Instances dataSet;
	
    
	public CreateArffListener(CoastalGUI appli)
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
			// On crée l'ensemble des instances
			dataSet = IHM.createArff();
			
			// Enregistrement des données :
			try {
				saveData(dataSet);
	 		}
			catch (Exception e) {
				System.out.println(e.toString());
			}		
					
			JOptionPane.showMessageDialog(IHM,
				    " Les données ont été enregistrées dans : " + " ./newDataset.arff ",
				    " Information ",
				    JOptionPane.INFORMATION_MESSAGE);
			
		}
  
	}
    
	/**
	 * Sauvegarde des données dans le buffer
	 * @param L'ensemble des instances
	 */ 
	public void saveData(Instances dataSet)throws Exception {
	{
		// Instances dataSet;
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("./newDataset.arff"));
		writer.write(dataSet.toString());
		writer.flush();
		writer.close();
	}}
}