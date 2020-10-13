package coastal;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import fr.unistra.pelican.algorithms.statistics.ConfusionMatrix;

public class ConfusionMatrixPanel extends JFrame implements ActionListener {

	private static final long serialVersionUID = 697878033868684667L;

	private CoastalGUI parent;

	private JLabel compareLabel, vpLabel, vnLabel, fpLabel,
			fnLabel, prodLabel, utilLabel, precisionLabel, kappaLabel;

	private JCheckBox vpjcb, vnjcb, fpjcb, fnjcb, prodjcb,
			utiljcb, precisionjcb, kappajcb;

	private JScrollPane jsp;

	private JButton ok, cancel;

	private ArrayList<JCheckBox> mHMTjcb;
	private ArrayList<JCheckBox> processjcb;

	private JPanel jp;
	private JPanel main;

	private GridBagLayout gbl;

	private static ConfusionMatrixPanel instance = null;

	private ConfusionMatrixPanel(CoastalGUI cgui) {
		this.parent = cgui;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Performance Index");

		mHMTjcb = new ArrayList<JCheckBox>();
		processjcb = new ArrayList<JCheckBox>();

		main = new JPanel();
		main.setPreferredSize(new Dimension(480, 300));
		gbl = new GridBagLayout();
		main.setLayout(gbl);
		this.setContentPane(main);

		vpLabel = new JLabel("True Positive");
		GridBagConstraints cvpLabel = new GridBagConstraints(1, 1, 1, 1, 0, 0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0);
		gbl.setConstraints(vpLabel, cvpLabel);
		main.add(vpLabel);

		vpjcb = new JCheckBox();
		GridBagConstraints cvpjcb = new GridBagConstraints(2, 1, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0);
		gbl.setConstraints(vpjcb, cvpjcb);
		main.add(vpjcb);

		vnLabel = new JLabel("True Negative");
		GridBagConstraints cvnLabel = new GridBagConstraints(3, 1, 1, 1, 0, 0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0);
		gbl.setConstraints(vnLabel, cvnLabel);
		main.add(vnLabel);

		vnjcb = new JCheckBox();
		GridBagConstraints cvnjcb = new GridBagConstraints(4, 1, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0);
		gbl.setConstraints(vnjcb, cvnjcb);
		main.add(vnjcb);

		fpLabel = new JLabel("False Positive");
		GridBagConstraints cfpLabel = new GridBagConstraints(5, 1, 1, 1,
				0, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);
		gbl.setConstraints(fpLabel, cfpLabel);
		main.add(fpLabel);

		fpjcb = new JCheckBox();
		GridBagConstraints cfpjcb = new GridBagConstraints(6, 1, 1, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0);
		gbl.setConstraints(fpjcb, cfpjcb);
		main.add(fpjcb);

		fnLabel = new JLabel("False Negative");
		GridBagConstraints cfnLabel = new GridBagConstraints(7, 1, 1, 1, 0,
				0, GridBagConstraints.EAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);
		gbl.setConstraints(fnLabel, cfnLabel);
		main.add(fnLabel);

		fnjcb = new JCheckBox();
		GridBagConstraints cfnjcb = new GridBagConstraints(8, 1, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0);
		gbl.setConstraints(fnjcb, cfnjcb);
		main.add(fnjcb);

		prodLabel = new JLabel("Producer Accuracy");
		GridBagConstraints cprodLabel = new GridBagConstraints(1, 2, 1, 1, 0, 0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0);
		gbl.setConstraints(prodLabel, cprodLabel);
		main.add(prodLabel);

		prodjcb = new JCheckBox();
		GridBagConstraints cprodjcb = new GridBagConstraints(2, 2, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0);
		gbl.setConstraints(prodjcb, cprodjcb);
		main.add(prodjcb);

		utilLabel = new JLabel("User Accuracy");
		GridBagConstraints cutilLabel = new GridBagConstraints(3, 2, 1, 1, 0, 0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0);
		gbl.setConstraints(utilLabel, cutilLabel);
		main.add(utilLabel);

		utiljcb = new JCheckBox();
		GridBagConstraints cutiljcb = new GridBagConstraints(4, 2, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0);
		gbl.setConstraints(utiljcb, cutiljcb);
		main.add(utiljcb);

		precisionLabel = new JLabel("Global Accuracy");
		GridBagConstraints cprecisionLabel = new GridBagConstraints(5, 2, 1, 1, 0, 0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0);
		gbl.setConstraints(precisionLabel, cprecisionLabel);
		main.add(precisionLabel);

		precisionjcb = new JCheckBox();
		GridBagConstraints cprecisionjcb = new GridBagConstraints(6, 2, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0);
		gbl.setConstraints(precisionjcb, cprecisionjcb);
		main.add(precisionjcb);

		kappaLabel = new JLabel("Kappa statistic");
		GridBagConstraints ckappaLabel = new GridBagConstraints(7, 2, 1, 1, 0, 0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0);
		gbl.setConstraints(kappaLabel, ckappaLabel);
		main.add(kappaLabel);

		kappajcb = new JCheckBox();
		GridBagConstraints ckappajcb = new GridBagConstraints(8, 2, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0);
		gbl.setConstraints(kappajcb, ckappajcb);
		main.add(kappajcb);

		compareLabel = new JLabel(
				"Select the objects you want to compare with the reference");
		GridBagConstraints cSelectLabel = new GridBagConstraints(1, 11, 12, 1,
				0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);
		gbl.setConstraints(compareLabel, cSelectLabel);
		main.add(compareLabel);

		this.definejp();

		ok = new JButton("Compare");
		GridBagConstraints cok = new GridBagConstraints(1, 18, 4, 1, 0, 0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0);
		gbl.setConstraints(ok, cok);
		main.add(ok);

		cancel = new JButton("Cancel");
		GridBagConstraints ccancel = new GridBagConstraints(5, 18, 4, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0);
		gbl.setConstraints(cancel, ccancel);
		main.add(cancel);

		ok.addActionListener(this);
		cancel.addActionListener(this);

		this.setAlwaysOnTop(true);
		pack();
		this.setVisible(true);

	}

	public void definejp() {
		if (jsp != null)
			main.remove(jsp);
		jp = new JPanel();
		int nb = parent.getMHMTResults().size()
				+ parent.getProcessResults().size();
		jp.setLayout(new GridLayout(nb, 2));
		jp.setPreferredSize(new Dimension(450, nb * 15));
		for (int i = 0; i < parent.getProcessResults().size(); i++) {
			JLabel tmplabel = new JLabel(parent.getProcessNames().get(i));
			JCheckBox tmpjcb = new JCheckBox();
			processjcb.add(tmpjcb);
			jp.add(tmplabel);
			jp.add(tmpjcb);
		}
		for (int i = 0; i < parent.getMHMTResults().size(); i++) {
			JLabel tmplabel = new JLabel(parent.getMHMTResultDefinitions().get(
					i).getName());
			JCheckBox tmpjcb = new JCheckBox();
			mHMTjcb.add(tmpjcb);
			jp.add(tmplabel);
			jp.add(tmpjcb);
		}

		jsp = new JScrollPane(jp,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setPreferredSize(new Dimension(480, 200));
		GridBagConstraints cjsp = new GridBagConstraints(1, 12, 12, 5, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0);
		gbl.setConstraints(jsp, cjsp);
		main.add(jsp);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancel)
			this.dispose();
		if (e.getSource() == ok) {
			this.dispose();
			parent.increaseProcessCount();
			Vector<String> columnNames = new Vector<String>();
			columnNames.add("Process");
			if (vpjcb.isSelected())
				columnNames.add("True positive");
			if (vnjcb.isSelected())
				columnNames.add("True negative");
			if (fpjcb.isSelected())
				columnNames.add("False positive");
			if (fnjcb.isSelected())
				columnNames.add("False negative");
			if (prodjcb.isSelected())
				columnNames.add("Producer accuracy");
			if (utiljcb.isSelected())
				columnNames.add("User accuracy");
			if (precisionjcb.isSelected())
				columnNames.add("Global accuracy");
			if (kappajcb.isSelected())
				columnNames.add("Kappa statistic");

			Vector<Vector<String>> rowData = new Vector<Vector<String>>();

			if (parent.getProcessResults().size() != 0) {
				for (int i = 0; i < parent.getProcessResults().size(); i++)
					if (processjcb.get(i).isSelected()) {
						Vector<String> strtmp = new Vector<String>();
						strtmp.add(parent.getProcessNames().get(i));
						Properties prop=(Properties) new ConfusionMatrix()
						.process(parent.getReference(), parent
								.getProcessResults().get(i));
						if (vpjcb.isSelected())
							strtmp.add(String.valueOf(prop.get(ConfusionMatrix.VP)));
						if (vnjcb.isSelected())
							strtmp.add(String.valueOf(prop.get(ConfusionMatrix.VN)));
						if (fpjcb.isSelected())
							strtmp.add(String.valueOf(prop.get(ConfusionMatrix.FP)));
						if (fnjcb.isSelected())
							strtmp.add(String.valueOf(prop.get(ConfusionMatrix.FN)));
						if (prodjcb.isSelected())
							strtmp.add(String.valueOf(prop.get(ConfusionMatrix.PROD)));
						if (utiljcb.isSelected())
							strtmp.add(String.valueOf(prop.get(ConfusionMatrix.UTIL)));
						if (precisionjcb.isSelected())
							strtmp.add(String.valueOf(prop.get(ConfusionMatrix.PRECISION)));
						if (kappajcb.isSelected())
							strtmp.add(String.valueOf(prop.get(ConfusionMatrix.KAPPA)));
						rowData.add(strtmp);
					}
			}

			if (parent.getMHMTResults().size() != 0) {
				for (int i = 0; i < parent.getMHMTResults().size(); i++)
					if (mHMTjcb.get(i).isSelected()) {
						Vector<String> strtmp = new Vector<String>();
						strtmp.add(parent.getMHMTResultDefinitions().get(i)
								.getName());
						Properties prop=(Properties) new ConfusionMatrix()
						.process(parent.getReference(), parent
								.getMHMTResults().get(i));
						if (vpjcb.isSelected())
							strtmp.add(String.valueOf(prop.get(ConfusionMatrix.VP)));
						if (vnjcb.isSelected())
							strtmp.add(String.valueOf(prop.get(ConfusionMatrix.VN)));
						if (fpjcb.isSelected())
							strtmp.add(String.valueOf(prop.get(ConfusionMatrix.FP)));
						if (fnjcb.isSelected())
							strtmp.add(String.valueOf(prop.get(ConfusionMatrix.FN)));
						if (prodjcb.isSelected())
							strtmp.add(String.valueOf(prop.get(ConfusionMatrix.PROD)));
						if (utiljcb.isSelected())
							strtmp.add(String.valueOf(prop.get(ConfusionMatrix.UTIL)));
						if (precisionjcb.isSelected())
							strtmp.add(String.valueOf(prop.get(ConfusionMatrix.PRECISION)));
						if (kappajcb.isSelected())
							strtmp.add(String.valueOf(prop.get(ConfusionMatrix.KAPPA)));
						rowData.add(strtmp);
					}
			}

			JTable results = new JTable(rowData, columnNames);
			ArrayList<String> colN = new ArrayList<String>();
			for (int count = 0; count < columnNames.size(); count++)
				colN.add(columnNames.elementAt(count));
			new EvaluationPanel(results, colN);
			parent.decreaseProcessCount();

		}

	}

	public static void getInstance(CoastalGUI cgui) {
		if (instance == null)
			instance = new ConfusionMatrixPanel(cgui);
		else if (instance.isVisible()) {
			instance.setVisible(true);
		} else {
			instance.dispose();
			instance = new ConfusionMatrixPanel(cgui);
		}
	}

	public static void updateInstance() {
		if (instance != null) {
			instance.definejp();
			instance.repaint();
			if (instance.isVisible())
				instance.setVisible(true);
		}
	}

}
