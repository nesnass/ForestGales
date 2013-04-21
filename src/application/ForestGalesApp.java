package application;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import uk.gov.forestresearch.forestmodels.gales.FGStandU;
import uk.gov.forestresearch.forestmodels.gales.FGTreeU;
import uk.gov.forestresearch.forestmodels.gales.ForestGalesTreeMechanics;


// RESULTS_COLUMNS defines how many result columns in the table
// SPECIES_FILENAME defines the name of the species file - no other file will be opened
public class ForestGalesApp {

	public final int RESULTS_COLUMNS = 2;
	public final String SPECIES_FILENAME = "species.txt";
	final JFrame frame = new JFrame();
	private int header1 = 0;
	private int header2 = 1000;
	private int header3 = 3;
	private int header4 = 0;
	private final int resultColumns = 2;

	private JList speciesList = new JList();
	private MyTableModel tableModel = new MyTableModel();
	private JTable dataTable = new JTable(tableModel);
	private DefaultListModel listModel;
	private JButton btnSpeciesFile = new JButton("Species file...");
	private JButton btnDataFile = new JButton("Data file...");
	private JButton btnProcess = new JButton("Process");
	private String fileDirectory = "";
	private String treeCode = "";
	
	ForestGalesTreeMechanics mech;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ForestGalesApp window = new ForestGalesApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ForestGalesApp() {
		initialize();
	}

	private void initialize() {
		frame.setBounds(100, 100, 1000, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel northPanel = new JPanel();
		frame.getContentPane().add(northPanel, BorderLayout.NORTH);

		JPanel southPanel = new JPanel();
		frame.getContentPane().add(southPanel, BorderLayout.SOUTH);
		
		JScrollPane dataScrollPane = new JScrollPane(dataTable);
		dataScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		frame.getContentPane().add(dataScrollPane, BorderLayout.CENTER);

		speciesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane speciesScrollPane = new JScrollPane(speciesList);
		
		speciesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		frame.getContentPane().add(speciesScrollPane, BorderLayout.WEST);

		northPanel.add(btnSpeciesFile);
		northPanel.add(btnDataFile);
		southPanel.add(btnProcess);
		
		btnProcess.setEnabled(false);
		btnDataFile.setEnabled(false);
		btnSpeciesFile.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					fileDirectory = file.getAbsolutePath();
					readSpeciesFile(file);
				}
			}
		});
		
		btnDataFile.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fc = new JFileChooser(fileDirectory);
				int returnVal = fc.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					readDataFile(fc.getSelectedFile());
				}
			}
		});
		
		btnProcess.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				String selectedValue = (String) speciesList.getSelectedValue();
				if(selectedValue == null)
					return;
				String splitValues[] = selectedValue.split("\\t");
				treeCode = splitValues[0];
				int rows = tableModel.getRowCount();
				
				Row row;
				// Increment over the table rows and calculate for each, storing back to the table
				for(int i=0; i< rows; i++)
				{
					row = tableModel.getRow(i);
					runMechanics(treeCode, row.getValue(0), row.getValue(1), row.getValue(2));
					row.setValue(3, mech.getProbOfBreak());
					row.setValue(4, mech.getProbOfOverturn());
				}
				writeResultFile();
				btnProcess.setEnabled(false);

				tableModel.fireTableRowsInserted(tableModel.rows.size() - 1, tableModel.rows.size() - 1);
				frame.repaint();
			}
		});
		
		speciesList.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == false) {
					if (speciesList.getSelectedIndex() != -1 && tableModel.getRowCount() > 0) {
						btnProcess.setEnabled(true);

					} else {
						btnProcess.setEnabled(false);
					}
				}
			}
		});

	}


	private void readSpeciesFile(File f)
	{
		if(!f.getName().equalsIgnoreCase(SPECIES_FILENAME))
			return;
		BufferedReader br = null;
		listModel = new DefaultListModel();
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(f));
 
			while ((sCurrentLine = br.readLine()) != null) {
				listModel.addElement(sCurrentLine);
			}
			speciesList.setModel(listModel);
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
				btnDataFile.setEnabled(true);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void readDataFile(File f)
	{
		BufferedReader br = null;
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(f));
			int lineCounter = 0;
			header1 = Integer.parseInt(br.readLine());
			header2 = Integer.parseInt(br.readLine()); // Rows
			header3 = Integer.parseInt(br.readLine()); // Columns
			header4 = Integer.parseInt(br.readLine());
			
			tableModel.clear();
			
			while ((sCurrentLine = br.readLine()) != null && lineCounter < header2) {
				String dataVariables[] = sCurrentLine.split("\\t");
				double rowVariables[] = new double[header3+RESULTS_COLUMNS];
				
				if(dataVariables.length == header3) {
					for(int i=0; i < header3; i++) {
						rowVariables[i] = Double.parseDouble(dataVariables[i]);
					}
					rowVariables[header3] = 0;
					rowVariables[header3+1] = 0;
					tableModel.addRow(new Row(rowVariables));
				}
				lineCounter++;
			}
			tableModel.fireTableRowsInserted(tableModel.rows.size() - 1, tableModel.rows.size() - 1);
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
				btnProcess.setEnabled(true);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private void writeResultFile()
	{
		try {
			int windowsDir = fileDirectory.lastIndexOf('\\');
			int macDir = fileDirectory.lastIndexOf('/');
			String fileName = "";
			Date dateNow = new Date();
			
			if(macDir != -1)
				fileName = fileDirectory.substring(0, macDir+1);
			else
				fileName = fileDirectory.substring(0, windowsDir+1);
			
			File file = new File(fileName + "results_" + treeCode + ".txt");

			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			int rowCount = tableModel.getRowCount();
			Row row;
			for(int i=0; i<rowCount; i++)
			{
				row = tableModel.getRow(i);
				bw.write(row.toString() + "\n");
			}
			bw.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class Row {
		  private final double[] values;

		  public Row(double[] newValues) {
			  values = newValues;
		  }
		  public int getSize() {
		    return values.length;
		  }
		  public double getValue(int i) {
		    return values[i];
		  }
		  public void setValue(int column, double val)
		  {
			  if(column > -1 && column < values.length)
				  values[column] = val;
		  }
		  
		  @Override
		  public String toString() {
			  String results = "";
			  if(values.length > 0) {
				  results = Double.toString(values[0]);
				  for(int i=1; i<values.length; i++)
					  results += "\t" + Double.toString(values[i]);
			  }
			  return results;
		  }
	}
	
	class MyTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 8127511298523094046L;
		private final List<Row> rows;
		private String[] columnNames = {"Variable 1",
	            "Variable 2",
	            "Variable 3",
	            "P(Stem Breakage)",
	            "P(Overturn)"};

		  public MyTableModel() {
		    this.rows = new ArrayList<Row>();
		  }

		  public int getRowCount() {
		    return rows.size();
		  }

		  public int getColumnCount() {
			  return columnNames.length;
		  }
		  
		  public String getColumnName(int col) {
		        return columnNames[col];
		  }

		  public Object getValue(int row, int column) {
		    return rows.get(row).getValue(column);
		  }
		  
		  public void clear() {
			  rows.clear();
		  }

		  public Class<?> getColumnClass(int col) {
		    return Row.class;
		  }
		  
		  public void addRow(Row rowData)
		  {
			  rows.add(rowData);
		  }
		  
		  public Row getRow(int r)
		  {
			  return rows.get(r);
		  }

		  @Override
		  public Object getValueAt(int arg0, int arg1) {
			  return rows.get(arg0).getValue(arg1);
		  }
		}
	
	public void runMechanics(String treeType, double variable1, double variable2, double variable3) {
		FGTreeU tree = new FGTreeU();

		double noOfTrees = 1000;
		double effectiveSpacing = Math.sqrt( 10000.0d/noOfTrees );
		double dams = 18;//10-sheltered 15-18 moderately exposed 19+ severely exposed - no forestry generally above dams 22
		tree.setDominance("mean");
		tree.setForceMethod("");
		//   if(treeType != "")
		//  	tree.setSpecies(treeType);
		//  else
		tree.setSpecies("SS");

		//    if(variable1 != 0)
		//   	tree.setYC(variable1);
		//  else
		tree.setYC( 16 );


		tree.setMeanDbh( 19 );
		tree.setTopHeight( 22 );
		tree.treeCharacteristics( effectiveSpacing );

		String soil = "Gley";
		String cult = "Notched";

		FGStandU stand = new FGStandU();
		stand.setAge( 40 );
		stand.setCultivation( cult );
		stand.setDrainage("Average");

		//use soil and mapper to nail nasty bug
		stand.setSoil( soil );


		mech = new ForestGalesTreeMechanics( stand.getCultivation(), stand.getSoil(), tree.getSpecies(), stand.getDrainage(), dams, tree.getMeanHeight(), tree.getCanopyBreadth(effectiveSpacing), tree.getCanopyDepth(), effectiveSpacing, 0 );//String cultivation, String Soil, String treeSpecies, double dams, double meanHeight) {
		mech.setBrownEdge( false );
		//mech.setGapWidth( 5.0 );
		//mech.setBrownEdge( true );
		mech.doCalculations( effectiveSpacing, tree.stemWeight, tree.Diam[0], tree.getCanopyBreadth( effectiveSpacing), tree.getCanopyDepth(), tree.Diam, tree.Z, tree.mass , tree.getMeanDbh() );

		/*long startTime = new java.util.Date().getTime();
	        for(int i = 0 ; i < 100 ; i++)
	            mech.doCalculations( effectiveSpacing, tree.stemWeight, tree.Diam[0], tree.getCanopyBreadth( effectiveSpacing), tree.getCanopyDepth(), tree.Diam, tree.Z, tree.mass , tree.getMeanDbh() );

	        long endTime   = new java.util.Date().getTime();*/
		//     System.out.println("Probability of stem breakage " + mech.getProbOfBreak()    * 100   + "%" );
		//     System.out.println("Probability of overturn "      + mech.getProbOfOverturn() * 100   + "%" );
		//System.out.println( "time taken = " + (endTime - startTime) );

		return;
	}
	
	
}
