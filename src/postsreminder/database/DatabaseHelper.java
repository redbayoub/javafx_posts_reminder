
package postsreminder.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import postsreminder.resources.Post;

/**
 *
 * @author redayoub
 */
public class DatabaseHelper {
    private static Connection conn=null;
    private static Statement stat=null;
    private static final String DB_URL="jdbc:h2:./database/my_db;create=true";
    private static DatabaseHelper helper;
    
    private DatabaseHelper() {
        try {
            Class.forName("org.h2.Driver");
            initDatabase();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static DatabaseHelper getInstance(){
        if(helper==null){
            helper=new DatabaseHelper();
        }
        return helper;
    }
    
    private void createConnection(){
        try {
            conn=DriverManager.getConnection(DB_URL);
            stat=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initDatabase(){
        try {
            createConnection();
            stat.execute(PostTable.CREATE_TABLE);
            System.out.println("Table Created");
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public int addPost(Post post){
        try {
        final String ADD_POST="INSERT INTO "+PostTable.TABLE_NAME+"("+
                PostTable.POST_TITLE+","+
                PostTable.POST_DESCR+" , "+
                PostTable.POST_DATE+" , "+
                PostTable.POST_REMINDER+") VALUES( '"+
                post.getTitle()+"' ,'"+
                post.getDescription()+"' ,'"+
                post.getPostDate()+"' ,'"+
                post.getPostReminder()+"' );";
        createConnection();
       
        int res=stat.executeUpdate(ADD_POST);
        
        return res;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    public ResultSet getPostSet(){
        try {
            final String GET_POST="SELECT * FROM "+PostTable.TABLE_NAME+
                    " ORDER BY "+PostTable.POST_DATE+" DESC ;";
            createConnection();
            ResultSet res=stat.executeQuery(GET_POST);
            
            return res;
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private ResultSet getPostSet(long reminderMin) {
        try {
            final String GET_POST="SELECT * FROM "+PostTable.TABLE_NAME+
                    " WHERE "+PostTable.POST_REMINDER+">"+reminderMin+
                    " ORDER BY "+PostTable.POST_DATE+" DESC ;";
            createConnection();
            ResultSet res=stat.executeQuery(GET_POST);
            
            return res;
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Post getPostById(int id){
        try {
            final String GET_POST="SELECT * FROM "+PostTable.TABLE_NAME+
                    " WHERE "+PostTable.ID+"="+id+" LIMIT 1 ;";
            createConnection();
            ResultSet res=stat.executeQuery(GET_POST);
            res.next();
            Post mPost=new Post(
                    res.getString(PostTable.POST_TITLE),
                    res.getString(PostTable.POST_DESCR),
                    Long.parseLong(res.getString(PostTable.POST_DATE)),
                    Long.parseLong(res.getString(PostTable.POST_REMINDER))
                );
            mPost.setId(res.getInt(PostTable.ID));
            res.close();
            return mPost;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean updatePost(int postID, Post mPost) {
        int aff_rows=0;
        try {
            
            final String UPT_POST="UPDATE "+PostTable.TABLE_NAME+" SET "+
                    PostTable.POST_TITLE+" = '"+mPost.getTitle()+"' ,"+
                    PostTable.POST_DESCR+" = '"+mPost.getDescription()+"' ,"+
                    PostTable.POST_DATE+" = '"+Long.toString(mPost.getPostDate())+"' ,"+
                    PostTable.POST_REMINDER+" = '"+Long.toString(mPost.getPostReminder())+"' "+
                    "WHERE "+PostTable.ID+"="+postID+" ;";
            createConnection();
            aff_rows=stat.executeUpdate(UPT_POST);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn!=null)conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return (aff_rows==1);
    }

    public boolean delPost(int postID) {
        int aff_rows=0;
        try {
            final String DEL_POST="DELETE FROM "+PostTable.TABLE_NAME+
                    " WHERE "+PostTable.ID+" = "+postID+" ;";
            createConnection();
            aff_rows=stat.executeUpdate(DEL_POST);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn!=null)conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return (aff_rows==1);  
    }
    
    public List<Post> getPostList(long reminderMin){
        List<Post> postsList=new ArrayList<>();
        try {
            ResultSet res;
            if(reminderMin==-1){
                res=helper.getPostSet();
            }else{
                if(reminderMin>0){
                    res=helper.getPostSet(reminderMin);
                }else{
                    return null;
                }
            }
            
            if(res==null)return null;
            while(res.next()){
                Post mPost=new Post(
                    res.getString(PostTable.POST_TITLE),
                    res.getString(PostTable.POST_DESCR),
                    Long.parseLong(res.getString(PostTable.POST_DATE)),
                    Long.parseLong(res.getString(PostTable.POST_REMINDER))
                );
                mPost.setId(res.getInt(PostTable.ID));
                postsList.add(mPost);
            }
     
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return postsList;
    }

    
}
