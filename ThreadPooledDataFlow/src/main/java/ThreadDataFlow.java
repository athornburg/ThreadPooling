
/**
 *
 * User: alexthornburg
 * Date: 8/30/13
 * Time: 10:31 AM
 *
 */
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.*;

public class ThreadDataFlow {


    File dagFile;
    File operations;
    ArrayList<DirectedAcyclicGraph> dagList = new ArrayList<DirectedAcyclicGraph>();
    int poolSize;

    public ThreadDataFlow(File ops, File dagFile,int poolSize) throws FileNotFoundException {
        this.dagFile = dagFile;
        this.operations = ops;
        this.poolSize = poolSize;

    }


    public ArrayList<DirectedAcyclicGraph> populate(){

        try {
            Scanner sc = new Scanner(dagFile);
            while(sc.hasNextLine()){
                DirectedAcyclicGraph<String> dag = new DirectedAcyclicGraph<String>();
                String line = sc.nextLine();
                String[] dagArray = line.split("->");
                for(int i=0;i<dagArray.length-1;i++){
                    dag.add(dagArray[i],dagArray[i+1]);
                }
                dagList.add(dag);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return dagList;
    }

    public void runConcurrently(String[] simul){
        ExecutorService service = Executors.newFixedThreadPool(poolSize);
        for(int i=0;i<simul.length;i++){

            if(simul[i].contains("(")){
                String task = simul[i].substring(1);
                Thread worker = new ThreadExecutor(task,operations);
                service.execute(worker);
            }else if(simul[i].contains(")")){
                int index = simul[i].indexOf(")");
                String task = simul[i].substring(0,index);
                Thread worker = new ThreadExecutor(task,operations);
                service.execute(worker);
            }else{
                Thread worker = new ThreadExecutor(simul[i],operations);
                service.execute(worker);
            }
        }
        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE,TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public void traverse(){
       for(DirectedAcyclicGraph dag:dagList){
        long startTime = System.currentTimeMillis();
        if(!dag.hasCycle()){
            System.out.println("Not a Dag");
            System.exit(1);
        }
        List<String> sorted = dag.topologicalSort();
        for(String s:sorted){
                if(s.contains("_")){
                    String[] simul = s.split("_");
                    runConcurrently(simul);
                } else{
                    Thread worker = new ThreadExecutor(s,operations);
                    worker.run();


                }

            }
           long endTime = System.currentTimeMillis();
           long runTime = endTime-startTime;

        }
    }
}
