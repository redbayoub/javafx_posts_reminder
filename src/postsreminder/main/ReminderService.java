
package postsreminder.main;

import eu.hansolo.enzo.notification.Notification;
import eu.hansolo.enzo.notification.NotifierBuilder;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.util.Duration;
import postsreminder.database.DatabaseHelper;
import postsreminder.resources.Post;

/**
 *
 * @author redayoub
 */
public class ReminderService {
    private static ReminderService service=null;
    private DatabaseHelper helper;
    private ScheduledExecutorService ses;
    private boolean running;
    
    
    private ReminderService() {
        ses=Executors.newScheduledThreadPool(1, (runnable)->{
            Thread t=new Thread(runnable);
            t.setDaemon(true);
            return t;
        });
        helper=DatabaseHelper.getInstance();
        running=false;
        
    }
    
    public static ReminderService getInstance(){
        if(service==null){
            service=new ReminderService();
        }
        return service;
    }
    
    public void startService(){
        if(!running){
            InitiateSevice();
        }
        
    }
    public void stopService(){
        if(running){
            ses.shutdown();
            running=false;
        }
    }
    
    public void addTask(Post mPost){
        if(running){
            long nowTime=(new Date()).getTime();
            ses.schedule(() -> {
                System.out.println(mPost.getTitle()+" added");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        showNotification(mPost.getTitle(), mPost.getTitle()+" reminder has arrived");
                    }
                });
                
            }, mPost.getPostReminder()-nowTime, TimeUnit.MILLISECONDS);
        }
    }
    
    public void InitiateSevice(){
        if(running){
            stopService();
            ses.shutdownNow();
            ses=null;
            ses=Executors.newScheduledThreadPool(1, (runnable)->{
            Thread t=new Thread(runnable);
            t.setDaemon(true);
            return t;
            });
        }
        running=true;
        long nowTime=(new Date()).getTime();
        List<Post> postsList=helper.getPostList(nowTime);
        for(Post mPost:postsList){
               addTask(mPost);
        }
            
    }
    
    private void showNotification(String title,String content){
        Notification notification=new Notification(title, content,Notification.INFO_ICON);
        
        Notification.Notifier notifire= NotifierBuilder.create()
                .popupLifeTime(Duration.seconds(5))
                .popupLocation(Pos.TOP_RIGHT)
                .build();
        notifire.notify(notification);
        notifire.setOnHideNotification((ev)->{
            notifire.stop();
        });
    }
}
