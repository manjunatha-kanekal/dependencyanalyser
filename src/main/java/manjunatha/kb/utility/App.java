package manjunatha.kb.utility;

import java.io.FileNotFoundException;

/**
 * Tester class for Dependency Analyser
 *
 */
public class App {
    public static void main( String[] args ) throws IllegalArgumentException {

        String folderPath;
        String inputExcelFilePath;
        String outputExcelFilePath;

        try{
            folderPath = args[0];
            inputExcelFilePath = args[1];
            outputExcelFilePath = args[2];
        } catch(Exception e) {
            throw new IllegalArgumentException("Please pass the SearchFolder, InputExcel and OutputExcel file paths.");
        }

        /*String folderPath = "D:\\workspace\\FolderName\\src";
        String inputExcelFilePath = "C:\\Users\\Manjunath B K\\Desktop\\DependencyAnalyser\\Input.xlsx";
        String outputExcelFilePath = "C:\\Users\\Manjunath B K\\Desktop\\DependencyAnalyser\\Output.xlsx";*/

        try {
            DependencyAnalyser analyser = new DependencyAnalyser(folderPath, inputExcelFilePath, outputExcelFilePath);
            analyser.analyseDependency();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
