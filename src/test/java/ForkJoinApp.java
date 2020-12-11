import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author LiBowei
 * @date 2020年-12月-08日
 **/
public class ForkJoinApp {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    final AtomicReference<Thread> rf = new AtomicReference<Thread>();
    Thread a = new Thread(new Runnable() {

      @Override
      public void run() {
        try {
          Thread.sleep(1000);
          System.out.println("a join");
          rf.get().join();
          Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
    rf.set(a);
    a.start();
    a.join(1);

   /* System.out.println(" b invoke");


    ForkJoinPool pool = new ForkJoinPool();
    ForkJoinTask<Object> task = new ForkJoinTask<Object>() {
      @Override
      public Object getRawResult() {
        return null;
      }

      @Override
      protected void setRawResult(Object value) {

      }

      @Override
      protected boolean exec() {
        try {
          Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("exec");
        return true;
      }
    };

    pool.execute(task);
    task.get();*/
    return;
  }

  private static int doFirst(){
    System.out.println("do first");
    return 1;
  }
  private static int doSecond(){
    System.out.println("do second");
    return 1;
  }
  private static int doThird(){
    System.out.println("do third");
    return 1;
  }
  private static int doForth(){
    System.out.println("do forth");
    return 1;
  }
}
