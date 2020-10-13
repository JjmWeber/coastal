package coastal.clustering;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import coastal.CoastalGUI;
import fr.unistra.pelican.Image;


class CancelListener implements ActionListener 
{
    public ClusteringGUI appli;

    public void actionPerformed(ActionEvent contexte) 
    {
    	appli.dispose();
    }

    public CancelListener(ClusteringGUI appli)
    {
    	this.appli = appli;
    }
}


class SelectionListener implements ActionListener 
{
	ClusteringGUI ihm;

    public SelectionListener(ClusteringGUI ihm)
    {
    	this.ihm = ihm;
    }

    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
    	this.ihm.MajMethode((String)cb.getSelectedItem());
    }
}

public class ClusteringGUI extends JFrame
{
	// ?
	private static final long serialVersionUID = -73490172434824849L;
	
    public CoastalGUI IHM;
    public Image displayed;
    public Image satellite;	
    
    
	// Liste des méthodes disponibles
	private String[] methodes = { "Sélectionnez une méthode", "Simple Kmeans", "Méthode2", "Méthode3"};
	
	//    LISTE DES COMPOSANTS DE L'INTERFACE
	JLabel jlbl_methode = new JLabel ("Sélectionner la méthode à utiliser : "); 
	JComboBox jcb_methode;
	JLabel jlbl_parametre1 = new JLabel ("Nombre de clusters désiré : ");
	JTextField jtf_parametre1 = new JTextField ("2");
	JLabel jlbl_parametre2 = new JLabel ("Paremètre 2 : ");
	JTextField jtf_parametre2 = new JTextField ("?");
	JButton jb_lancer = new JButton ("Lancer");
	JButton jb_annuler = new JButton ("Annuler");
	
	// Stockage de la méthode choisie :
	private String methode_choisie = new String();
	
	//public JFrame jf = new ClusteringIHM("Les Exemples", IHM, satellite);
	public void main(String[] args)
	{
		JFrame jf = new ClusteringGUI("Les Exemples", IHM, satellite);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
	}
	
	public void MajMethode(String new_methode_choisie)
	{
		this.methode_choisie = new_methode_choisie;
	}
	
	public String getMethodeChoisie()
	{
		return this.methode_choisie;
	}

	public ClusteringGUI(String titre, CoastalGUI IHM, Image satellite)
	{
		super(titre);
		
		this.IHM = IHM;
		this.satellite = satellite;

		// Remplissage de la ComboBox
		jcb_methode = new JComboBox(methodes);
		jcb_methode.setSelectedIndex(0);
		
		// Initialisation pour signifier que l'utilisateur n'a encore choisit aucune méthode.
		this.methode_choisie = "Sélectionnez une méthode";
		
		//Ajout des Listeners
		jcb_methode.addActionListener(new SelectionListener(this));
		jb_lancer.addActionListener(new RunClusteringListener(this.IHM, this.satellite, this));
		jb_annuler.addActionListener(new CancelListener(this));
		
		
		JPanel exemples = new JPanel(new GridLayout(5,1));
		exemples.add(jlbl_methode);
		exemples.add(jcb_methode);
		exemples.add(jlbl_parametre1);
		exemples.add(jtf_parametre1);
	
		
		JPanel boutons=new JPanel(new FlowLayout());
		boutons.add(jb_lancer);
		boutons.add(jb_annuler);
		
		exemples.add(boutons);
		
		this.add(exemples);
	}
}