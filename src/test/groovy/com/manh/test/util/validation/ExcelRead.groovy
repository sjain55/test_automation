package com.manh.test.util.validation

import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class ExcelRead {

    Workbook workbook;
    def fis;
    public static Map<String, Vector<String>> masterKeyMap = new TreeMap<String, Vector<String>>();
    public static Map<String, String> queryKeyMap = new TreeMap<String, String>();


    public void readExcelData(String fileName) {
        Sheet sheet;
        ArrayList<String> sheetNames = new ArrayList<String>();

        workbook = setWorkbookData(fileName);

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

            if (workbook.getSheetAt(i).getSheetName().equals("MASTER_KEY_LIST")) {
                readMasterSheetData(workbook.getSheetAt(i));
            } else {
                readKeySheetData(workbook.getSheetAt(i));
            }
        }
        closeWorkbook(workbook, fileName);
    }

    private Workbook setWorkbookData(String fileName) {

        try {
            //fis = new FileInputStream(fileName);
            fis = this.getClass().getClassLoader().getResourceAsStream("testdata/excel/${fileName}")
        } catch (FileNotFoundException e) {
            println fileName + " not found... Terminating";
            throw new FileNotFoundException();
        }

        if (fileName.toLowerCase().endsWith("xlsx")) {
            workbook = new XSSFWorkbook(fis);
        } else if (fileName.toLowerCase().endsWith("xls")) {
            workbook = new HSSFWorkbook(fis);
        }
        return workbook;
    }

    private ArrayList<String> readSheetNames(String fileName, Workbook workbook) {

        ArrayList<String> sheetNames = new ArrayList<String>();
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            sheetNames.add(workbook.getSheetAt(i).getSheetName());
        }
        return sheetNames;
    }

    private void readMasterSheetData(Sheet sheet) {

        int lastRowNum = sheet.getLastRowNum();

        for (int i = 0; i <= lastRowNum; i++) {

            Row row = sheet.getRow(i);

            List<String> list = new Vector<String>();

            if (row != null) {
                int cellCount = row.getLastCellNum();

                for (int j = 1; j < cellCount; j++) {

                    Cell cell = row.getCell(j);

                    if (cell != null && !(getCellValue(cell).equals(""))) {
                        list.add(getCellValue(row.getCell(j)));
                    }
                }
                masterKeyMap.put(getCellValue(row.getCell(0)), list);
            }
        }
    }

    private void readKeySheetData(Sheet sheet) {

        int lastRowNum = sheet.getLastRowNum();

        for (int i = 0; i <= lastRowNum; i++) {

            Row row = sheet.getRow(i);

            if (row != null) {
                int cellCount = row.getLastCellNum();

                if (cellCount > 1) {
                    Cell cell = row.getCell(0);

                    if (cell != null && !(getCellValue(cell).equals(""))) {
                        queryKeyMap.put(getCellValue(row.getCell(0)), getCellValue(row.getCell(1)));
                    }
                }
            }
        }
    }

    private String getCellValue(HSSFCell hCell) {

        if (hCell.getCellType() == 0) {
            return String.valueOf(Math.round(hCell.getNumericCellValue()));
        } else if (hCell.getCellType() == 1) {
            return hCell.getStringCellValue();
        } else if (hCell.getCellType() == 2) {
            return hCell.getStringCellValue();
        } else if (hCell.getCellType() == 3) {
            return "";
        } else if (hCell.getCellType() == 4) {
            return String.valueOf(hCell.getBooleanCellValue());
        } else if (hCell.getCellType() == 5) {
            return String.valueOf(hCell.getErrorCellValue());
        } else {
            try {
                return hCell.getStringCellValue();
            }
            catch (Exception e) {
                return "";
            }
        }
    }

    private String getCellValue(XSSFCell xCell) {

        if (xCell.getCellType() == 0) {
            return String.valueOf(Math.round(xCell.getNumericCellValue()));
        } else if (xCell.getCellType() == 1) {
            return xCell.getStringCellValue();
        } else if (xCell.getCellType() == 2) {
            return xCell.getStringCellValue();
        } else if (xCell.getCellType() == 3) {
            return "";
        } else if (xCell.getCellType() == 4) {
            return String.valueOf(xCell.getBooleanCellValue());
        } else if (xCell.getCellType() == 5) {
            return String.valueOf(xCell.getErrorCellValue());
        } else {
            try {
                return xCell.getStringCellValue();
            }
            catch (Exception e) {
                return "";
            }
        }
    }

    private void closeWorkbook(Workbook workbook, String fileName) {

        FileOutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
        fileOut.close();
    }
}

