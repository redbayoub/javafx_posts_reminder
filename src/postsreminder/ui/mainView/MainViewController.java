package postsreminder.ui.mainView;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import postsreminder.database.DatabaseHelper;
import postsreminder.main.Main;
import postsreminder.main.ReminderService;
import postsreminder.resources.Post;


/**
 *
 * @author redayoub
 */
public class MainViewController implements Initializable {

    @FXML
    private Text close;
    
    @FXML
    private JFXListView<GridPane> postsList;
    
    private DatabaseHelper helper;
    @FXML
    private StackPane rootPane;
    @FXML
    private JFXTextField postTitle;
    @FXML
    private Label postDate;
    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXDatePicker timePicker;
    @FXML
    private JFXTextArea postDescr;
    @FXML
    private JFXButton saveBtn;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Text closePostView;
    @FXML
    private JFXButton addBtn;
    @FXML
    private JFXButton delBtn;
    
    private int postID;
    private ReminderService service;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        postsList.setExpanded(true);
        postsList.depthProperty().set(1);
        close.setOnMouseClicked((ev)->{
            Main.hide(Main.myStage);
        });
        closePostView.setOnMouseClicked((ev)->{setPostView(false);});
       helper=DatabaseHelper.getInstance(); 
       initList();
       postsList.setOnMouseClicked((ev)->{openPost(ev);});
        setPostView(false);
        service=ReminderService.getInstance();
       //helper.addPost(new Post("my test post", "my test post descr", (new Date()).getTime(),  (new Date()).getTime()));
        
    } 
    
     @FXML
    void addPost(ActionEvent event) {
        setPostView(true);
        postDate.setText((new Date()).toString());
        postTitle.setText("");
        postDescr.setText("");
        timePicker.setTime(LocalTime.now());
        datePicker.setValue(LocalDate.now());
        addBtn.setVisible(true);
        saveBtn.setVisible(false);
        delBtn.setVisible(false);
    }
    
    private GridPane createPostNode(Post post){
        GridPane gp=new GridPane();
        gp.setVgap(2);
        gp.setHgap(2);
        Label titleLab=new Label("Title :");
        Text titleText=new Text(post.getTitle());
        Label dateLab=new Label("Post date :");
        Text dateText=new Text((new Date(post.getPostDate())).toString());
        Label DescrLab=new Label("Description :");
        Text DescrText=new Text(post.getDescription());
        gp.setId(Integer.toString(post.getId()));
        gp.add(titleLab, 0, 0);
        gp.add(titleText, 1, 0);
        gp.add(dateLab, 0, 1);
        gp.add(dateText, 1, 1);
        gp.add(DescrLab, 0, 2);
        gp.add(DescrText, 1, 2);
        return gp;
    }
    
    private void initList(){
        List<Post> myPostsFDb=helper.getPostList(-1);
        if((myPostsFDb==null)||myPostsFDb.isEmpty())return;
        postsList.getItems().clear();
        for (Post mPost:myPostsFDb){
            postsList.getItems().add(createPostNode(mPost));
        }
        
    }

    private void openPost(MouseEvent ev) {
        if (ev.getTarget() instanceof GridPane) {
            GridPane gp=(GridPane) ev.getTarget();
            int id=Integer.parseInt(gp.getId());
            Post mPost=helper.getPostById(id);
            if(mPost==null)return;
            postID=id;
            postTitle.setText(mPost.getTitle());
            postDate.setText((new Date(mPost.getPostDate())).toString());
            postDescr.setText(mPost.getDescription());
            Calendar reminder=Calendar.getInstance();
            reminder.setTime(new Date(mPost.getPostReminder()));
            timePicker.setTime(LocalTime.of(reminder.get(Calendar.HOUR_OF_DAY), reminder.get(Calendar.MINUTE)));
            datePicker.setValue(LocalDate.of(
                                reminder.get(Calendar.YEAR), 
                                reminder.get(Calendar.MONTH)+1, 
                                reminder.get(Calendar.DAY_OF_MONTH)));
            addBtn.setVisible(false);
            saveBtn.setVisible(true);
            delBtn.setVisible(true);
            
            setPostView(true);
        }
    }
    private void setPostView(boolean visible) {
        borderPane.setVisible(visible);
    }
  
    @FXML
    private void addAction(ActionEvent event) {
        // checkFileds
        if(postDescr.getText().equals(""))return;
        LocalDateTime reminderDaTim=LocalDateTime.of(datePicker.getValue(),timePicker.getTime());
        
        Calendar reminderCal=Calendar.getInstance();
        reminderCal.set(
                reminderDaTim.getYear(),
                reminderDaTim.getMonthValue()-1, 
                reminderDaTim.getDayOfMonth(),
                reminderDaTim.getHour(),
                reminderDaTim.getMinute());
        Post mPost=new Post(
                postTitle.getText(),
                postDescr.getText(),
                (new Date()).getTime(),
                reminderCal.getTime().getTime());
        helper.addPost(mPost);
        service.addTask(mPost);
        initList();
        setPostView(false);
    }

    @FXML
    private void saveAction(ActionEvent event) {
        // checkFileds
        if(postDescr.getText().equals(""))return;
        LocalDateTime reminderDaTim=LocalDateTime.of(datePicker.getValue(),timePicker.getTime());
        
        Calendar reminderCal=Calendar.getInstance();
        reminderCal.set(
                reminderDaTim.getYear(),
                reminderDaTim.getMonthValue()-1, 
                reminderDaTim.getDayOfMonth(),
                reminderDaTim.getHour(),
                reminderDaTim.getMinute());
        Post mPost=new Post(
                postTitle.getText(),
                postDescr.getText(),
                (new Date()).getTime(),
                reminderCal.getTime().getTime());
        mPost.setId(postID);
        boolean res=helper.updatePost(postID,mPost);
        if(res){
            int gpIndex=0;
            for (GridPane gp:postsList.getItems()){
                if(gp.getId().equals(Integer.toString(postID))){
                    break;
                }
                gpIndex++;
            }
            postsList.getItems().set(gpIndex, createPostNode(mPost));
        }
        service.InitiateSevice();
        setPostView(false);
    }

    @FXML
    private void delAction(ActionEvent event) {
        if(postID!=-1){ //del post
            boolean res=helper.delPost(postID);
            if(!res)return;
            int gpIndex=0;
            for (GridPane gp:postsList.getItems()){
                if(gp.getId().equals(Integer.toString(postID))){
                    break;
                }
                gpIndex++;
            }
            postsList.getItems().remove(gpIndex);
        }
        service.InitiateSevice();
        setPostView(false);
    }
    
}
