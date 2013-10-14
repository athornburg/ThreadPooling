/**
 *
 * User: alexthornburg
 * Date: 9/5/13
 * Time: 9:57 PM
 *
 */
import java.io.FileNotFoundException;
import java.util.Map;
import java.io.File;
import java.util.Scanner;
import java.util.HashMap;

public class ThreadExecutor extends Thread{
    private String command;
    private Map<String,String[]>operations;
    File file;

    public ThreadExecutor(String s,File f){
        this.command = s;
        this.file=f;
        operations = new HashMap<String,String[]>();
    }


    @Override
    public void run() {
        System.out.println("START task = "+ command);
        processCommand(command,file);
        System.out.println("END "+command);
    }

    private void processCommand(String command,File resourceFile) {
        getOperations(resourceFile);
        for(Map.Entry<String,String[]> e:operations.entrySet()){
        if(command.equals(e.getKey())&& e.getValue()[1].contains("Sleep")){
            try {
                Thread.sleep(Integer.parseInt(e.getValue()[2]));
            } catch (InterruptedException error) {
                error.printStackTrace();
            }

        }else if(command.equals(e.getKey())&& e.getValue()[1].contains("Sum")){
                int sum =0;
                for(int i=0;i<=Integer.parseInt(e.getValue()[2]);i++){
                     sum+=i;
                }
            }
        }
    }

    public void getOperations(File file){
        OperationsParser parser = new OperationsParser(file);
        operations = parser.parse();

    }

    @Override
    public String toString(){
        return this.command;
    }
}

