package gt.com.tigo.integradorhome.util;

import gt.com.tigo.integradorhome.util.exception.ResourceConversionException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

public class Functions {

    private Functions() {
        // no-op
    }

    public static String getCellValueAsString(Cell cell) throws ResourceConversionException {

        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING: {
                return cell.getStringCellValue();
            }
            case NUMERIC: {
                DataFormatter df = new DataFormatter();
                return df.formatCellValue(cell);
            }
            case BLANK: {
                return null;
            }
            default: {
                throw new ResourceConversionException();
            }
        }
    }

    public static Double getCellValueAsDouble(Cell cell) throws ResourceConversionException {
        switch (cell.getCellType()) {
            case STRING: {
                return Double.parseDouble(cell.getStringCellValue());
            }
            case NUMERIC: {
                return cell.getNumericCellValue();
            }
            case BLANK: {
                return null;
            }
            default: {
                throw new ResourceConversionException();
            }
        }
    }

}
