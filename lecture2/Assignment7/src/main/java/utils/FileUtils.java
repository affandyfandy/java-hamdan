package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

public class FileUtils {

    public static List<String[]> readCsv(String filePath) throws IOException {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(values);
            }
        }
        return records;
    }

    public static List<String[]> readCsvWithOpenCsv(String filePath) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            return reader.readAll();
        } catch (IOException | CsvException e) {
            throw new IOException("An error occurred while reading the CSV file", e);
        }
    }

    public static void writeCsv(String filePath, List<String[]> data) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
            writer.writeAll(data);
        }
    }

    public static void convertXlsxToCsv(String xlsxFilePath, String csvFilePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(new File(xlsxFilePath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath),
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END)) {
                for (Row row : sheet) {
                    List<String> rowData = new ArrayList<>();
                    for (Cell cell : row) {
                        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                            rowData.add(new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue()));
                        } else {
                            rowData.add(cell.toString().trim());
                        }
                    }
                    writer.writeNext(rowData.toArray(new String[0]));
                }
            }
        }
    }
}

