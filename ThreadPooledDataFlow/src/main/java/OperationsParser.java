import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
/**
 * User: alexthornburg
 * Date: 9/17/13
 * Time: 8:17 PM
 */
public class OperationsParser {

    Map<String,String[]> operations = new HashMap<String,String[]>();
    File inputFile;

    public OperationsParser(File file){
        this.inputFile=file;
    }

    public Map<String,String[]> parse(){
        try{
        Scanner sc = new Scanner(inputFile);
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                String[] splitLine = line.split(" ");
                operations.put(splitLine[0],splitLine);
                return operations;
            }
        }catch(FileNotFoundException e){
            System.out.println("Operations file not found.");
        }
          return null;
    }

    public String[] getOperation(String op){
        return operations.get(op);
    }

    public String[] deleteOperation(String op){
          return operations.remove(op);
    }
}
