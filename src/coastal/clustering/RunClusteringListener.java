package coastal.clustering;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import coastal.CoastalGUI;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.IntegerImage;
import fr.unistra.pelican.algorithms.segmentation.labels.LabelsToPredefinedColor;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;


public class RunClusteringListener implements ActionListener 
{
    public ClusteringGUI ClusterIhm;
    public String methode;
    public int NbClusters;
    
    public CoastalGUI IHM;
    public Image displayed;
    public Image satellite;
    public Image monImage;
    public Image test;
    
    private Instances dataSet;
    
	public int[] resultat;
    
	public RunClusteringListener(CoastalGUI IHM, Image satellite, ClusteringGUI ClusterIhm)
	{
		this.IHM = IHM;
		this.satellite = satellite;
		this.ClusterIhm = ClusterIhm;
	}

    public void actionPerformed(ActionEvent contexte)
    {	
    	this.methode = ClusterIhm.getMethodeChoisie();
    	this.NbClusters = Integer.parseInt(ClusterIhm.jtf_parametre1.getText());
    	
    	// On regarde quelle m�thode l'utilisateur � s�lectionn�
    	
    	if(methode=="S�lectionnez une m�thode")
    		JOptionPane.showMessageDialog(IHM,
			    "Vous devez d'abord s�lectionner une m�thode !",
			    "Information",
			    JOptionPane.INFORMATION_MESSAGE);
    	else if(methode=="Simple Kmeans")
    		try
    		{
    			simpleKmeans();
    		}
    		catch(Exception ex)
    		{
    			System.out.println(ex.toString());
    		}
    	else if(methode=="M�thode2")
    		JOptionPane.showMessageDialog(IHM,
			    "M�thode2 � venir !",
			    "Information",
			    JOptionPane.INFORMATION_MESSAGE);
    	else if(methode=="M�thode3")
    		JOptionPane.showMessageDialog(IHM,
    				
			    "M�thode3 � venir !",
			    "Information",
			    JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    public void simpleKmeans()throws Exception
    {    	
//-----------------------------//
//     Cr�ation du dataSet     //
//-----------------------------//
    	
    	// On peut aussi cr�er le dataSet � partir d'un fichier Arff
    	// dataSet = new Instances(new BufferedReader(new FileReader("./newDataset.arff")));
		dataSet = IHM.createArff();
			
//------------------------------//
//  Application du Clustering   //
//------------------------------//

			SimpleKMeans kmeans = new SimpleKMeans();
			
			try{
				kmeans.setNumClusters(NbClusters);}
			catch (Exception e){
				System.out.println(e.toString());}
			
			try{
				kmeans.buildClusterer(dataSet);}
			catch (Exception e){
				System.out.println(e.toString());}
			
			// On cr�� un tableau de la taille du nombre d'instances ( = nb de pixels )
	    	resultat = new int[dataSet.numInstances()];
	    	
	    	
			for(int i = 0; i < dataSet.numInstances(); i++) 
			{
				try
				{
					int value = (int)kmeans.clusterInstance(dataSet.instance(i));
					resultat[i] = value;
				}
				catch (Exception e)
				{
					System.out.println(e.toString());
				}
			}


			// On cr�� une image aux m�me dimension quel l'image satellite, mais avec une seule bande.

		// Il FAUT utiliser une IntegerImage pour faire un LabelsToPredefinedColors .. !!
		// On utilise le constructeur qui conserve la taille, etc .. de l'ancienne image
			monImage = new IntegerImage(satellite.getXDim(),satellite.getYDim(),satellite.getZDim(),satellite.getTDim(),1);
			
			for(int i = 0; i < dataSet.numInstances(); i++) 
			{
					monImage.setPixelInt(i, resultat[i]);
			}

			
			Color colors[] = {Color.BLACK, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, Color.PINK, 
					Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.DARK_GRAY, 
					Color.GRAY, Color.LIGHT_GRAY};
			
		// On ne prend qu'autant de couleurs qu'on a de clusters d�sir�s, sinon on a une ereur 
		//	lors de l'application de LabelsToPredefinedColor
			Color mesColors[] = new Color[this.NbClusters];
			
			for(int i1 = 0; i1 < NbClusters; i1++)
			{
				if(i1 >= colors.length)
					mesColors[i1]= new Color(((int) Math.random()) * 255,((int) Math.random()) * 255, ((int) Math.random()) * 255);
				else
					mesColors[i1] = colors[i1];
			}

			
	// M�thodes d'affiachages dans : fr / unistra / pelican / algorithms / segmentation / labels

			 monImage = LabelsToPredefinedColor.exec(monImage, mesColors);
				
			 
// Essaie de reconstitution selon �l�ment ..
// monImage = LabelsToColorByMeanValue.exec(monImage, satellite);
			
			// this.IHM.setDisplayed(monImage);
			Viewer2D.exec(monImage,"try");
			//this.IHM.setSatellite(monImage);
    }
}