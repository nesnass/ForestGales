package application;

//////////////////////////////////////////////////
//
//   GUI interfacing authored by Richard Nesnass
//   nesnass@gmail.com
//

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

public class ForestGalesApp {

	// RESULTS_COLUMNS defines how many result columns in the table
	// SPECIES_FILENAME defines the name of the species file - no other file will be opened
	public final int RESULTS_COLUMNS = 2;
	public final String SPECIES_FILENAME = "species.txt";
	final JFrame frame = new JFrame();
	@SuppressWarnings("unused")
	private int header1 = 0;
	private int header2 = 1000;  // header2 must contain the number of rows
	private int header3 = 3;   // header3  must contain the number of columns
	@SuppressWarnings("unused")
	private int header4 = 0;

	private JList speciesList = new JList();
	private MyTableModel tableModel;
	private JTable dataTable;
	private DefaultListModel listModel;
	private JButton btnSpeciesFile = new JButton("Species file...");
	private JButton btnDataFile = new JButton("Data file...");
	private JButton btnProcess = new JButton("Process");
	private String fileDirectory = "";
	private String treeCode = "";
	JScrollPane dataScrollPane;
	
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
					runMechanics(treeCode, row.getValues());
					row.setValue(header3, mech.getProbOfBreak());
					row.setValue(header3+1, mech.getProbOfOverturn());
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
			
			header1 = Integer.parseInt(br.readLine().trim());
			header2 = Integer.parseInt(br.readLine().trim()); // Rows
			header3 = Integer.parseInt(br.readLine().trim()); // Columns
			header4 = Integer.parseInt(br.readLine().trim());
			
			tableModel = new MyTableModel(header3);
			dataTable = new JTable(tableModel);

			dataScrollPane = new JScrollPane(dataTable);	
			dataTable.setFillsViewportHeight(true);
			
			dataScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			frame.getContentPane().add(dataScrollPane, BorderLayout.CENTER);
			
			while ((sCurrentLine = br.readLine()) != null && lineCounter < header2) {
				String dataVariables[] = sCurrentLine.split("\\t");
				Double rowVariables[] = new Double[header3+RESULTS_COLUMNS];
				
				if(dataVariables.length == header3) {
					for(int i=0; i < header3; i++) {
						rowVariables[i] = Double.valueOf(dataVariables[i]);
					}
					for(int j=0; j < RESULTS_COLUMNS; j++) {
						rowVariables[header3+j] = 0.0;
					}
					tableModel.addRow(new Row(rowVariables));
				}
				lineCounter++;
			}
		//	tableModel.fireTableDataChanged();
			tableModel.fireTableRowsInserted(tableModel.rows.size() - 1, tableModel.rows.size() - 1);
			frame.validate();
 
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
			
			// Header
			bw.write(String.valueOf(RESULTS_COLUMNS) + "\n" + "ProbOfBreak" + "\n" + "ProbOfOverturn" + "\n" + "time = no" + "\n" + String.valueOf(header2) + "\n");
			
			int rowCount = tableModel.getRowCount();
			Row row;
			for(int i=0; i<rowCount; i++)
			{
				row = tableModel.getRow(i);
			//	bw.write(row.toString() + "\n");
				bw.write(row.getValue(header3) + "\t" + row.getValue(header3+1) + "\n");
			}
			bw.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class Row {
		  private final Double[] values;

		  public Row(Double[] newValues) {
			  values = newValues;
		  }
		  @SuppressWarnings("unused")
		  public int getSize() {
		    return values.length;
		  }
		  public String getValue(int i) {
		    return String.valueOf(values[i]);
		  }
		  public Double[] getValues() {
			  return values;
		  }
		  
		  public void setValue(int column, Double val)
		  {
			  if(column > -1 && column < values.length)
				  values[column] = val;
		  }
		  
		  @Override
		  public String toString() {
			  String results = "";
			  if(values.length > 0) {
				  results = String.valueOf(values[0]);
				  for(int i=1; i<values.length; i++)
					  results += "\t" + values[i];
			  }
			  return results;
		  }
	}
	
	class MyTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 8127511298523094046L;
		private final List<Row> rows;
		private String[] columnNames;

		  public MyTableModel() {
		    this.rows = new ArrayList<Row>();
		  }

		  public MyTableModel(int dataColumns) {
			    this();
		    	columnNames = new String[dataColumns+RESULTS_COLUMNS];
			    for(int i=0; i< dataColumns;i++)
			    {
			    	columnNames[i] = "V " + String.valueOf(i+1);
			    }
			    for(int j=0; j< RESULTS_COLUMNS; j++)
			    {
			    	columnNames[dataColumns+j] = "R " + String.valueOf(j+1);
			    }
		  }
		  
		  public int getRowCount() {
		    return rows.size();
		  }

		  public int getColumnCount() {
			  return columnNames.length;
		  }
		  
		  public String getColumnName(int col) {
		        return columnNames[col].toString();
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
	
	public void runMechanics(String treeType, Double[] variables) {
		
		
// Here specify how input variables are applied to the simulation
// Variables in code correlate to Excel Spreadsheet subtract 1:  V - 1
		
		FGTreeU tree = new FGTreeU();
		FGStandU stand = new FGStandU();
		
		tree.setSpecies(treeType);

// Variables used in code, but not defined in Excel sheet:

		tree.setDominance("mean");
		tree.setForceMethod("");
		tree.setYC( 16 );				// getYC not called anywhere in code.


// Variable 0:  Age
		stand.setAge(variables[0].intValue());
		
// Variable 1:  Planting Year
		stand.setPlantingYear(variables[1].intValue());
			
// Variable 2:  soil type
		stand.setSoil(variables[2].intValue());
		
// Variable 3:  DBH
		tree.setMeanDbh(variables[3]);
		
// Variable 4:  Stem Density
		tree.setStemDensity(variables[4]);

// Variable 5: Crown Density
		tree.setCanopyDensity(variables[5]);
		
// Variable 6: Spacing
		// double effectiveSpacing = Math.sqrt( 10000.0d/noOfTrees );
		double effectiveSpacing = (variables[6]);

// Variable 7: dams
		double dams = (variables[7]);     //10-sheltered 15-18 moderately exposed 19+ severely exposed - no forestry generally above dams 22
		
// Variable 8: topHeight
		tree.setTopHeight(variables[8]);

// Variable 9: cultivation
		stand.setCultivation( variables[9].intValue() );
		
// Variable 10: drainage
		stand.setDrainage(variables[10].intValue());
	
// The next set of variables are set internally by calling this method:		
		tree.treeCharacteristics( effectiveSpacing );

// Create a new simulator
		mech = new ForestGalesTreeMechanics( stand.getCultivation(), stand.getSoil(), tree.getSpecies(), stand.getDrainage(), dams, tree.getMeanHeight(), tree.getCanopyBreadth(effectiveSpacing), tree.getCanopyDepth(), effectiveSpacing, 0 );//String cultivation, String Soil, String treeSpecies, double dams, double meanHeight) {

// Variable 11: edge		
		mech.setBrownEdge( Boolean.parseBoolean(String.valueOf(variables[11])));
		
// Run the simulations
		mech.doCalculations( effectiveSpacing, tree.stemWeight, tree.Diam[0], tree.getCanopyBreadth( effectiveSpacing), tree.getCanopyDepth(), tree.Diam, tree.Z, tree.mass , tree.getMeanDbh() );

		return;
	}
}
