package coastal.clustering;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class ConceptList
{
	// private JList liste;
	
	/** The JScrollPane which contains the list of all areas */
	private JScrollPane jsp_classList;
	/** List's model */
	private DefaultListModel dlm;
	/** JList which contains the various areas */
	private JList jl_classList;
	/** Contains the areas */
	private Vector<Vector<Object>> vectorOfClass;
	
	public ConceptList()
	{
		//this.monIHM = m;
		this.dlm = new DefaultListModel();
		this.jl_classList = new JList(this.dlm);
		this.jl_classList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.jl_classList.setFixedCellHeight(30);
		this.jsp_classList=new JScrollPane(jl_classList);
		this.jsp_classList.setBorder(null);
		this.vectorOfClass=new Vector<Vector<Object>>();
		this.jsp_classList.setPreferredSize(new Dimension(20,20));
	}
	
	public JScrollPane getPane() { return this.jsp_classList; }
	
	public void modifySelectedConcept(Color c,String className) 
	{
		if(!this.jl_classList.isSelectionEmpty()) 
		{
			/** Get the line of the selected class */
			int nb_select=this.jl_classList.getSelectedIndex();
			
			/** Get the vector to modify */
			Vector<Object> modif=this.vectorOfClass.get(nb_select);
			((JButton)modif.get(0)).setBackground(c);
			((JLabel)modif.get(1)).setText(className);
			this.jl_classList.repaint();
		}
	}
	
//	public int addConceptText(Color clr, String className) 
//	{
//		liste.add(className + " ( " + clr + " ) ");
//	}
	
	public int addConceptInTheList(Color clr, String className) 
	{
		/** Creates the vector which is the future area with it's button, label and number of points */
		Vector<Object> class_ = new Vector<Object>(4);
		
		/** Creates the button for the area */
		JButton jb_zonesColor=new JButton();
		jb_zonesColor.setBackground(clr);
		Dimension dim = new Dimension(20,20);
		jb_zonesColor.setPreferredSize(dim);
		jb_zonesColor.setMaximumSize(dim);
		
		/** The label for the area's name */
		JLabel jlbl_className = new JLabel(className);
		
		/** Vector which contains the different points of the area */
		Vector<Point> vpoints=new Vector<Point>();
		
		/** Label which will contains the number of points in the area */
		JLabel jlbl_numberOfPoints = new JLabel(" ("+vpoints.size()+")");
		
		/** Initialise the area's vector */
		class_.add(jb_zonesColor);
		class_.add(jlbl_className);
		class_.add(jlbl_numberOfPoints);
		class_.add(vpoints);
		
		/** Adds the area in the vector of areas */
		this.vectorOfClass.add(class_);
		/** Displays the area in the list */
		this.dlm.addElement(class_);
		/** Selects the added element */
		this.jl_classList.setSelectedIndex(this.dlm.size()-1);
		
		return this.vectorOfClass.size()-1;
	}
	
	
	/**
	 * Returns true if the color <code>clr</code> already exists in the vector 
	 * of classes, false if not.
	 * @param clr the color to check the existence
	 * @return boolean
	 */
	public boolean colorExistsInVectorOfClass(Color clr) {
		boolean OK = false;
		for (int i = 0; i < this.vectorOfClass.size() && !OK; i++) {
			JButton test = (JButton)this.vectorOfClass.get(i).get(0);
			OK = test.getBackground().equals(clr);
		}
		return OK;
	}
	
	public boolean ControleCouleurModif(Color actuel) {
		boolean OK = false;

		for (int i = 0; i < this.vectorOfClass.size() && !OK; i++) {
			if (i!=this.jl_classList.getSelectedIndex()) {
				JButton test = (JButton)this.vectorOfClass.get(i).get(0);
				OK = test.getBackground().equals(actuel);
			}
		}
		return OK;
	}
	
	/**
	 * Returns true if the given <code>name</code> already exists in the vector of classes,
	 * false if not.
	 * @param name the name of the class to find
	 * @return boolean
	 */
	public boolean nameExistsInVectorOfClass(String name) {
		boolean OK = false;
		JLabel essai;
		String nomLabel;
		
		for(int i = 0; i < this.vectorOfClass.size() && !OK; i++) {
			essai = (JLabel) this.vectorOfClass.get(i).get(1);
			nomLabel = essai.getText();
			OK = nomLabel.equals(name);
		}
		return OK;
	}
	
	public boolean ControleNomModif(String nom) {
		boolean OK = false;
		
		for(int i = 0; i < this.vectorOfClass.size() && !OK; i++) {
			
			if (i!=this.jl_classList.getSelectedIndex()) {
				JLabel essai = (JLabel)this.vectorOfClass.get(i).get(1);
				String nomLabel = (String)essai.getText();
				OK = nomLabel.equals(nom);
			}
		}
		return OK;
	}
	
	
	
}