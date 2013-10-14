
import java.io.*;
import java.util.Properties;

/**
 * User: alexthornburg
 * Date: 9/16/13
 * Time: 2:09 PM
 */
public class Computation {
    static String operationsFile;
    static String graphsFile;

    public static void main(String[]args){

            operationsFile = "ThreadPooledDataFlow/src/test/resources/operations.txt";
            graphsFile = "ThreadPooledDataFlow/src/test/resources/graphs.txt";

       try {
            ThreadDataFlow computation = new ThreadDataFlow(new File(operationsFile),
                    new File(graphsFile),5);
            computation.populate();
            computation.traverse();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



    }
    }
