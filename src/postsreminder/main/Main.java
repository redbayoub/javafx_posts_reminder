package postsreminder.main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import eu.hansolo.enzo.notification.Notification;
import eu.hansolo.enzo.notification.NotifierBuilder;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import postsreminder.database.DatabaseHelper;
import postsreminder.images.Images;

/**
 *
 * @author redayoub
 */
public class Main extends Application {
    public static Stage myStage;
    // to make window dragable
    double xOffset=0;
    double yOffset=0;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/postsreminder/ui/mainView/MainView.fxml"));
        
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        scene.getStylesheets().add(getClass().getResource("/postsreminder/resources/style.css").toExternalForm());
        stage.setTitle("Posts Reminder");
        stage.setScene(scene);
        // to make window dragable
        root.setOnMousePressed((ev)->{
                xOffset=ev.getSceneX();
                yOffset=ev.getSceneY();
        });
        root.setOnMouseDragged((ev2)->{
            stage.setX(ev2.getScreenX()- xOffset);
            stage.setY(ev2.getScreenY()- yOffset);
        });
        
        stage.show();
        myStage=stage;
        DatabaseHelper helper=DatabaseHelper.getInstance();
        
        // add the pgm to sys tray
        if (!SystemTray.isSupported()) {
            return;
        }
        // initReminder Service
        ReminderService rs=ReminderService.getInstance();
        Platform.setImplicitExit(false);
        stage.setOnCloseRequest((ev)->{hide(stage);});
        SystemTray tray=SystemTray.getSystemTray();
        java.awt.Image image=Images.loadImage("posts_icon.png");
        PopupMenu menu=new PopupMenu();
        MenuItem start=new MenuItem("Start Service");
        MenuItem stop=new MenuItem("Stop Service");
        // start the service in the beginnig
        rs.startService();
        start.setEnabled(false);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 // start lisnining
                 stop.setEnabled(true);
                 start.setEnabled(false);
                 rs.startService();
            }
        });
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 // stop lisnining
                 start.setEnabled(true);
                 stop.setEnabled(false);
                 rs.stopService();
            }
        });
        MenuItem showPgm=new MenuItem("Show Program");
        showPgm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 // show pgm
                if (!stage.isShowing()) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            stage.show();
                        }
                    });
                    
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        stage.toFront();
                    }
                });
                
            }
        });
        MenuItem exit=new MenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 // exit pgm
                 System.exit(0); 
            }
        });
        menu.add(start);
        menu.add(stop);
        menu.addSeparator();
        menu.add(showPgm);
        menu.addSeparator();
        menu.add(exit);
        TrayIcon pgmIcon=new TrayIcon(image, "Posts Reminder", menu);
        pgmIcon.setImageAutoSize(true);
        pgmIcon.addMouseListener(new MouseListener() {@Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton()!=MouseEvent.BUTTON1)return;
                 if (!stage.isShowing()) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            stage.show();
                            
                        }
                    });   
                }
                 Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        stage.toFront();
                    }
                });
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseClicked(MouseEvent e) {}
        });
        tray.add(pgmIcon);
        
        //showNotification("this my title", "this is the content");
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static void hide(Stage stage){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (SystemTray.isSupported()) {
                    stage.hide();
                    
                } else {
                    System.exit(0);
                }
            }
        });
    }
    
    private void showNotification(String title, String content){
        
//        Notification notification=new Notification(title, content,Notification.INFO_ICON);
//        
//        Notification.Notifier notifire= NotifierBuilder.create()
//                .popupLifeTime(Duration.seconds(5))
//                .popupLocation(Pos.TOP_RIGHT)
//                .build();
//        notifire.notify(notification);
//        notifire.setOnHideNotification((ev)->{
//            notifire.stop();
//        });
    }

  
    
}
