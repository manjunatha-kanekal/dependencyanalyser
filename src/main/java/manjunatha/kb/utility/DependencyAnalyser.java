package manjunatha.kb.utility;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DependencyAnalyser {
    private String folderPath;
    private String inputExcelFilePath;
    private String outputFilePath;

    public DependencyAnalyser(String folderPath, String inputExcelFilePath, String outputFilePath) {

        if(folderPath == null || inputExcelFilePath == null || outputFilePath == null) {
            throw new IllegalArgumentException("Folder Path and Excel File path are mandatory!");
        }

        this.folderPath = folderPath;
        this.inputExcelFilePath = inputExcelFilePath;
        this.outputFilePath = outputFilePath;
    }

    public boolean analyseDependency() throws Exception {

        try {
            ExcelParser parser = new ExcelParser();
            JsonArray dataArray = parser.parseSheet(inputExcelFilePath);
            System.out.println("Parsed Data " + dataArray);

            if(dataArray != null && dataArray.size() > 0) {
                List<String> searchStringList = new ArrayList<String>();
                for(int n=0; n<dataArray.size(); n++) {
                    JsonObject json = (JsonObject) dataArray.get(n);
                    if(json.has(Constants.INPUT_FILE_COLUMN_NAMES)) {
                        searchStringList.add(json.get(Constants.INPUT_FILE_COLUMN_NAMES).getAsString());
                    }
                }

                DependencyChecker checker =  new DependencyChecker(folderPath, searchStringList);
                Map<String, List<String>> resultMap = checker.checkDependency();

                if(resultMap != null) {
                    parser.dumpToExcel(outputFilePath, resultMap);
                }

            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }
}
