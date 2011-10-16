/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dramble;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author troy
 */
public class ExcelComparison {

    public String[] excelPaths = new String[2];
    public ArrayList<String>[] excelColumns = new ArrayList[2];
    private Workbook[] wbs = new Workbook[2];
    public boolean[] isFirstRowColumns = {true, true};
    private FormulaEvaluator[] evaluators = new FormulaEvaluator[2];
    private DataFormatter cellFormatter = new DataFormatter();
    public ArrayList<String>[] primaryKeysList = new ArrayList[2];
    private int[] primaryKeys = {0, 0};

    public ExcelComparison(String path1, String path2, boolean columns1, boolean columns2, int primaryKey1, int primaryKey2) throws FileNotFoundException, IOException, InvalidFormatException {
        this.excelPaths[0] = path1;
        this.excelPaths[1] = path2;
        this.isFirstRowColumns[0] = columns1;
        this.isFirstRowColumns[1] = columns2;
        this.primaryKeys[0] = primaryKey1;
        this.primaryKeys[1] = primaryKey2;

        InputStream inp1 = new FileInputStream(this.excelPaths[0]);
        this.wbs[0] = WorkbookFactory.create(inp1);
        this.evaluators[0] = wbs[0].getCreationHelper().createFormulaEvaluator();

        InputStream inp2 = new FileInputStream(this.excelPaths[1]);
        this.wbs[1] = WorkbookFactory.create(inp2);
        this.evaluators[1] = wbs[1].getCreationHelper().createFormulaEvaluator();

        this.makeColumnNamesArray();

        this.makePrimaryKeysList();
    }

    private void makeColumnNamesArray() {
        int i;
        for (i = 0; i <= 1; i++) {
            Sheet sheet = this.wbs[i].getSheetAt(0);
            Row row = sheet.getRow(0);
            int numCells = row.getPhysicalNumberOfCells();
            this.excelColumns[i] = new ArrayList(numCells);
            int j;
            for (j = 0; j < numCells; j++) {
                Cell cell = row.getCell(j);
                this.excelColumns[i].add(this.cellFormatter.formatCellValue(cell, this.evaluators[i]));
            }
        }
    }

    private ArrayList<String>[] makeColumnsList(int column1, int column2) {
        ArrayList<String>[] columnsList = new ArrayList[2];
        int[] columns = {column1, column2};
        int i;
        for( i = 0 ; i < 2 ; i ++) {
            Sheet sheet = wbs[i].getSheetAt(0);
            int numRows = sheet.getPhysicalNumberOfRows();
            columnsList[i] = new ArrayList(numRows);
            int j;
            int start = this.isFirstRowColumns[i]? 1:0;

            for( j = start ; j < numRows ; j ++) {
                Row row = sheet.getRow(j);
                Cell cell = row.getCell(columns[i]);
                columnsList[i].add(this.cellFormatter.formatCellValue(cell, this.evaluators[i]));
            }
        }
        return columnsList;
    }

    private void makePrimaryKeysList() {
        int i;

        for (i = 0; i < 2; i++) {
            Sheet sheet = this.wbs[i].getSheetAt(0);
            int numRows = sheet.getPhysicalNumberOfRows();
            int j;
            int start = this.isFirstRowColumns[i]? 1:0;
            this.primaryKeysList[i] = new ArrayList(numRows);
            for (j = start; j < numRows; j++) {
                Row row = sheet.getRow(j);
                Cell cell = row.getCell(this.primaryKeys[i]);
                this.primaryKeysList[i].add(this.cellFormatter.formatCellValue(cell, this.evaluators[i]));
            }
        }
    }

    public ArrayList<String[]> comparePrimaryKeys() {
        int i;
        ArrayList<String[]> missings = new ArrayList();
        ArrayList<String> primaryKeysList1Temp = new ArrayList();
        primaryKeysList1Temp.addAll(this.primaryKeysList[1]);


        for (i = 0; i < this.primaryKeysList[0].size(); i++) {
            String primaryKey = this.primaryKeysList[0].get(i);
            if (!primaryKeysList1Temp.remove(primaryKey)) {
                String[] missing = {primaryKey, "present", "missing"};
                missings.add(missing);
            }
        }

        for (i = 0; i < primaryKeysList1Temp.size(); i++) {
            String[] missing = {primaryKeysList1Temp.get(i), "missing", "present"};
            missings.add(missing);
        }

        return missings;
    }

    public ArrayList<String[]> compareColumn(int column1, int column2) {
        ArrayList<String[]> changes = new ArrayList();
        ArrayList<String>[] columnsList = this.makeColumnsList(column1, column2);

        int primaryKey1Int;
        for(primaryKey1Int = 0 ; primaryKey1Int < this.primaryKeysList[0].size() ; primaryKey1Int ++) {
            String primaryKeyString = this.primaryKeysList[0].get(primaryKey1Int);
            if(this.primaryKeysList[1].contains(primaryKeyString)) {
                int primaryKey2Int = this.primaryKeysList[1].indexOf(primaryKeyString);
                String column1String = columnsList[0].get(primaryKey1Int);
                String column2String = columnsList[1].get(primaryKey2Int);
                if(!column1String.equals(column2String)) {
                    String[] change = {primaryKeyString,column1String,column2String};
                    changes.add(change);
                }
            }
        }

        return changes;
    }
}
