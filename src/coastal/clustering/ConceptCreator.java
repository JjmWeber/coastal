package coastal.clustering;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** 
 * This class provides a dialog to create and modify class
 */
public class ConceptCreator extends JDialog  implements ActionListener, KeyListener
{
	private static final long serialVersionUID = 1L;
	
	
    private JButton okBouton = new JButton("OK");           // Bouton de validation
	private JButton cancelBouton = new JButton("Cancel");   // Bouton exit
	
	private JButton jb_chooseColor = new JButton();             // Creation du bouton de couleur
	
    private JPanel jp_north = new JPanel();						// Panneau nord
    private JPanel jp_center = new JPanel();					// Panneau central
    private JPanel jp_south = new JPanel();						// Panneau sud
    
    private JTextField jtf_className = new JTextField(15);		// Champ texte pour le nomLabel
    
    private Color classColor;										// Couleur du label
    
    private Container contenu;								// Contenu, panneau qui rassemble tout les panneau
    
    boolean ok = false;								// Boolean pour le bouton valider
    boolean accord = false;							// Boolean qui indique si l'utilisateur a ou non valider le dialogue


    Concept nouveau_concept = new Concept();
    
    /**
     * Create the dialog for adding and modifying a class and put it on 
     * the screen in invisible mode.
     */
	public ConceptCreator()
	{
		super(new JFrame(), "New Class", true);
		this.setSize(420, 150);
		this.contenu = getContentPane();
        this.createNorthPanel(this.contenu);
        this.createCenterPanel(this.contenu);
        this.createSouthPanel(this.contenu);
        this.pack();

        // this.setVisible(true) // On le rend visible que dans les lanceDial
	}

    public void createNorthPanel(Container contenu)
    {
        this.jtf_className.addKeyListener(this);
        this.jp_north.add(new JLabel("Name : "));
        this.jp_north.add(this.jtf_className);
        this.contenu.add(this.jp_north, BorderLayout.NORTH);
    }
    
    public void createCenterPanel(Container contenu)
    {
        this.jb_chooseColor.setPreferredSize(new Dimension (20, 20));
        this.jb_chooseColor.addActionListener(this);
        this.jp_center.add(new JLabel("Color : "));
        this.jp_center.add(this.jb_chooseColor);
        this.contenu.add(this.jp_center, BorderLayout.CENTER);
    }

    public void createSouthPanel(Container contenu)
    {
    	this.okBouton.addActionListener(this);
    	this.cancelBouton.addActionListener(this);
    	this.jp_south.add(this.okBouton);
    	this.jp_south.add(this.cancelBouton);
    	this.contenu.add(this.jp_south, BorderLayout.SOUTH);
    }

	public void lanceDial(Concept infos, ConceptList pl_centre)
	{
		this.ok = false ;			// Boolean de la boite de dialogue
		this.accord = false;		// Mise à zero du boolean d'accord de l'utilisateur
		this.setVisible(true) ;

        if (ok)
        {
       		accordSaveInfo(infos);
        }
	}
	
	public void lanceDialModif(Concept infos, ConceptList classLst)
	{
		this.ok = false;		// Boolean de la boie de dialogue
		this.accord = false;	// Mise a zero du boolean d'accord de l'utilisateur
		this.setVisible (true); // La boie de dialogue devient visible
	}
	
	private void accordSaveInfo (Concept infos)
	{
		this.accord = true;								// Accord de l'utilisateur donne
    	infos.nomLabel = jtf_className.getText();				// Sauvegarde du nom de label dans la structure Infos
    	infos.couleurLabel = jb_chooseColor.getBackground();// Sauvegarde de la couleur du label dans la structure
	}
	
	/**
	 * Fonction qui reinitialise les champs du champ de texte
	 * Utilise seulement lors de la creation d'un nouveau label
	 */
	public void initConceptCreator()
	{
		this.setTitle("Nouveau Concept");
		this.jtf_className.setText("");
		this.jb_chooseColor.setBackground(new Color(238, 238, 238));
	}
	
	/**
	 * Modify a class by settings its informations (name and color)
	 */
	public void modifyClass(Concept infos)
	{
		this.setTitle("Modify the class " + infos.nomLabel);
		this.jtf_className.setText(infos.nomLabel);
		this.jb_chooseColor.setBackground(infos.couleurLabel);
	}
	
	/**
	 * Open a JColorChooser for choosing the class color.
	 */
	private void chooseClassColor ()
	{
		this.classColor = JColorChooser.showDialog(ConceptCreator.this, "Choose a color", classColor);
        if (this.classColor == null)
        	this.classColor = Color.white;
        else
            this.jb_chooseColor.setOpaque(true);
        	this.jb_chooseColor.setBackground(classColor);
        	this.jb_chooseColor.setForeground(classColor);
	}
	
	/**
	 * Valid the informations when the user click on the OK button.
	 * If no name is entered, a warning is displayed.
	 */
	private void validClassInformations ()
	{
		if (this.jtf_className.getText().length() == 0)
			JOptionPane.showMessageDialog(this,
										  "Veuillez entre le nom de la classe",
										  "Warning",
										  JOptionPane.WARNING_MESSAGE);
		else
		{
			this.setVisible(false);
			this.ok = true;
		}
	}
	
	/**
	 * Fonction qui renvoie un boolean representant l'accord ou non 
	 * de l'utilisateur pour la creation ou modifiaation d'un label
	 * @return boolean
	 */
	public boolean getAccord() { return this.accord; }
	
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == this.okBouton)
        	validClassInformations();
        if (e.getSource() == this.cancelBouton)
        	this.setVisible(false);     // on retire simplement la boite de dialogue
        if (e.getSource() == jb_chooseColor)
        	chooseClassColor();
    }
    
    public void keyPressed(KeyEvent e){}
    public void keyTyped(KeyEvent e) {}
    
	public void keyReleased(KeyEvent e) 
	{
		// The user press "Enter"
		if (e.getKeyChar() == KeyEvent.VK_ENTER)
			validClassInformations();
		// The user press "Escape"
		if (e.getKeyChar() == KeyEvent.VK_ESCAPE)
			this.setVisible(false);
	}
}
