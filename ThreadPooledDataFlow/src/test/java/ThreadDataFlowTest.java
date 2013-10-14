import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Properties;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 *
 * User: alexthornburg
 * Date: 9/6/13
 * Time: 8:37 PM
 *
 */
public class ThreadDataFlowTest {
    private String operationsFile;
    private String graphsFile;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void startStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void finishStreams() {
        System.setOut(null);
    }


    @Before
    public void loadFiles(){
        operationsFile = "ThreadPooledDataFlow/src/test/resources/operations.txt";
        graphsFile = "ThreadPooledDataFlow/src/test/resources/graphs.txt";

}


    /**
     * Testing graph creation
     */
    @Test
    public void testCreateGraph(){
        try {
            ThreadDataFlow computation = new ThreadDataFlow(new File(operationsFile),
                    new File(graphsFile),5);
            Scanner sc = new Scanner(new File(graphsFile));
            int count = 0;
            while(sc.hasNextLine()){
                computation.populate();
                String dagTest = sc.nextLine();
                String[] dagArray = dagTest.split("->");
                DirectedAcyclicGraph dag = computation.dagList.get(count);
                for(int i=0;i<dagArray.length;i++){
                    assertEquals(dagArray[i],dag.topologicalSort().get(i));
                }
                count +=1;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Kind of a black box test.
     * This tests only works if tasks are
     * ordered in terms of time.
     * T1 = slowest
     * T2 = faster
     * T3 = fastest
     */
    @Test
    public void testInOrder(){
        try {
            ThreadDataFlow computation = new ThreadDataFlow(new File(operationsFile),
                    new File(graphsFile),5);
            Scanner sc = new Scanner(new File(graphsFile));
            int count = 0;
            computation.populate();
            computation.traverse();
            StringBuilder sb = new StringBuilder();
            while(sc.hasNextLine()){
                String dagTest = sc.nextLine();
                String[] dagArray = dagTest.split("->");

                for(int i=0;i<dagArray.length;i++){
                    if(dagArray[i].contains("_")){
                        String[] concurrent = dagArray[i].split("_");
                        for(int j =0;j<concurrent.length;j++){
                            if(concurrent[j].contains("(")){
                                String task = concurrent[j].substring(1);
                                sb.append("START task = "+task+"\n");
                            }else if(concurrent[j].contains(")")){
                                int index = concurrent[j].indexOf(")");
                                String task = concurrent[j].substring(0, index);
                                sb.append("START task = "+task+"\n");
                            }else{
                                sb.append("START task = "+concurrent[j]+"\n");
                            }
                        }
                    }else{
                        sb.append("START task = "+dagArray[i]+"\n");
                    }
                    if(dagArray[i].contains("_")){
                        String[] concurrent = dagArray[i].split("_");
                        List<String>finishedTasks = new ArrayList<String>();
                        for(int j =0;j<concurrent.length;j++){
                            if(concurrent[j].contains("(")){
                                String task = concurrent[j].substring(1);
                                finishedTasks.add(task);
                            }else if(concurrent[j].contains(")")){
                                int index = concurrent[j].indexOf(")");
                                String task = concurrent[j].substring(0,index);
                                finishedTasks.add(task);
                            }else{
                                finishedTasks.add(concurrent[j]);
                            }
                        }
                        Collections.sort(finishedTasks, new Comparator<String> () {

                            @Override
                            public int compare(String o1, String o2) {
                                return o1.compareTo(o2);
                            }
                        });
                        Collections.reverse(finishedTasks);
                        for(String s:finishedTasks){
                            sb.append("END "+s+"\n");
                        }
                    }else{
                        sb.append("END "+dagArray[i]+"\n");
                    }

                }

            }
            assertEquals(outContent.toString(),sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

      }

}
