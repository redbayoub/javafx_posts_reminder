
package postsreminder.resources;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import postsreminder.database.DatabaseHelper;

/**
 *
 * @author redayoub
 */
public class ReminderThread extends Thread{
    private final int MIN_IN_MS=60000;
    private final int SLEEP_TIMEOUT_IN_MIN=1;
    private DatabaseHelper helper;
    private volatile boolean running;
    public ReminderThread() {
        super();
        helper=DatabaseHelper.getInstance();
        this.setDaemon(true);
    }

    
    
    @Override
    public void run() {
        running=true;
        ScheduledExecutorService ses=Executors.newSingleThreadScheduledExecutor();
        
        while (running) {            
            // i working here
            // get list of posts from databse (add thsi intent is select arg)
            //check evrey post
                // if post is more or less than 1 min show notification
            try {
                Thread.sleep(SLEEP_TIMEOUT_IN_MIN*MIN_IN_MS);
            } catch (InterruptedException ex) {
                Logger.getLogger(ReminderThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    
    
    public void stopTh(){
        running=false;
    }
    
}
