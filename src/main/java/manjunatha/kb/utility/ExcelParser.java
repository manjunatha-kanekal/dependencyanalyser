package manjunatha.kb.utility;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ExcelParser {
    List<String> headerList;

    public JsonArray parseSheet(String absolutefilePath) throws Exception {
        JsonArray parsedArray = new JsonArray();
        if(absolutefilePath == null) {
            throw new IllegalArgumentException("File Path can not be null. Please provide proper file path.");
        }

        Workbook workbook = null;
        try {
            FileInputStream excelFile = new FileInputStream(new File(absolutefilePath));
            workbook = new XSSFWorkbook(excelFile);
            Sheet workingSheet = workbook.getSheetAt(0);

            Iterator<Row> iterator = workingSheet.iterator();
            Row headerRow = iterator.next();

            /* Check for the very first row and grab the excel headers which will be keys in JSON Object */
            if (headerRow.getRowNum() == 0) {
                parseHeaderRow(headerRow);
            }

            System.out.println("Header List " + headerList);
            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                parsedArray.add(parseDataRow(currentRow));
            }

        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            if(workbook != null) {
                workbook.close();
            }
        }
        return parsedArray;
    }

    private void parseHeaderRow(Row headerRow) {
        headerList = new ArrayList<String>();
        Iterator<Cell> cellIterator = headerRow.iterator();

        while (cellIterator.hasNext()) {
            Cell currentCell = cellIterator.next();
            if (currentCell.getCellTypeEnum() == CellType.STRING) {
                headerList.add(currentCell.getStringCellValue());
            } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                headerList.add(currentCell.getNumericCellValue() + "");
            }
        }
    }


    private JsonObject parseDataRow(Row dataRow) {
        JsonObject json = new JsonObject();
        Iterator<Cell> cellIterator = dataRow.iterator();

        while (cellIterator.hasNext()) {

            Cell currentCell = cellIterator.next();
            int index = currentCell.getColumnIndex();
            if (currentCell.getCellTypeEnum() == CellType.STRING) {
                json.addProperty(headerList.get(index), currentCell.getStringCellValue());
            } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                json.addProperty(headerList.get(index), currentCell.getNumericCellValue());
            }
        }

        return json;
    }

    public void dumpToExcel(String absolutefilePath, Map<String, List<String>> dataMap) throws Exception {

        if( absolutefilePath == null || dataMap == null ) {
            throw new IllegalArgumentException("File Path and Data can not be null. Please provide proper Inputs.");
        }

        List<String> headers = Arrays.asList(Constants.OUTPUT_FILE_COLUMN_NAMES);
        XSSFWorkbook workbook = null;

        try {
            workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(Constants.OUTPUT_FILE_SHEET_NAME);

            int rowNumber = 0;

            // For Header Row
            Row headerRow = sheet.createRow(rowNumber++);
            int columnNumber = 0;
            for(String headerString : headers) {
                Cell cell = headerRow.createCell(columnNumber++);
                cell.setCellValue(headerString);
            }

            // For Data Rows
            for (Map.Entry<String, List<String>> entry : dataMap.entrySet()) {
                List<String> findings = entry.getValue();
                String numberOfMatches = String.valueOf(findings.size());
                String api_name = entry.getKey();
                int MergeStartRowNumber = rowNumber;
                for(String res : findings) {
                    Row row = sheet.createRow(rowNumber++);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(api_name);

                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(res);

                    Cell cell3 = row.createCell(2);
                    cell3.setCellValue(numberOfMatches);
                }

            }

            FileOutputStream outputStream = new FileOutputStream(absolutefilePath);
            workbook.write(outputStream);
            workbook.close();

        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            if(workbook != null) {
                workbook.close();
            }
        }

    }
}
