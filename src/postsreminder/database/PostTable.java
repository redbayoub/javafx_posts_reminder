package postsreminder.database;

/**
 *
 * @author redayoub
 */
public class PostTable {

    public static final String TABLE_NAME = "posts";
    public static final String ID = "id";
    public static final String POST_TITLE = "post_title";
    public static final String POST_DESCR = "post_description";
    public static final String POST_DATE = "post_date";
    public static final String POST_REMINDER = "post_reminder";
    
     public static final String CREATE_TABLE = 
             "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+
             ID+" INT AUTO_INCREMENT  PRIMARY KEY ,"+
             POST_TITLE+" TEXT NOT NULL ,"+
             POST_DESCR+" TEXT ,"+
             POST_DATE+" TEXT NOT NULL ,"+
             POST_REMINDER+" TEXT ) ;";
}
