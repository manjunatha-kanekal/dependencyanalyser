package manjunatha.kb.utility;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependencyChecker {
    private List<String> textListToSearch;
    private String folderPath;
    private String filePath = "";
    private int fileCount = 0;

    public DependencyChecker(String folderPath, List<String> textListToSearch) {

        if(folderPath == null || textListToSearch == null || textListToSearch.isEmpty()) {
            throw new IllegalArgumentException("Folder Path and Text List to be searched are mandatory!");
        }

        this.folderPath = folderPath;
        this.textListToSearch = textListToSearch;
    }

    public Map<String, List<String>> checkDependency() throws FileNotFoundException {

        Map<String, List<String>> resultMap = new HashMap<String, List<String>>();

        File dir = new File(folderPath);
        List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE,
                DirectoryFileFilter.DIRECTORY);

        for (File file : files) {
            try {
                filePath = file.getCanonicalPath();
                if (file.isFile()) {
                    fileCount++;
                    int lineNumber = 0;
                    try {
                        FileReader reader = new FileReader(filePath);
                        BufferedReader br = new BufferedReader(reader);
                        String s;
                        while ((s = br.readLine()) != null) {
                            lineNumber++;
                            for(String textToSearch : textListToSearch) {
                                if(StringUtils.containsIgnoreCase(s, textToSearch)) {
                                    if(resultMap.containsKey(textToSearch)) {
                                        List<String> resultList = resultMap.get(textToSearch);
                                        resultList.add(file.getName() + " at " + lineNumber + " line");
                                        resultMap.put(textToSearch, resultList);
                                    } else {
                                        List<String> resultList = new ArrayList<String>();
                                        resultList.add(file.getName() + " at " + lineNumber + " line");
                                        resultMap.put(textToSearch, resultList);;
                                    }
                                }
                            }
                        }
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Number of files scanned = "+fileCount);

        return resultMap;
    }
}
