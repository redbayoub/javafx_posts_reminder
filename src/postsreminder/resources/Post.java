
package postsreminder.resources;

import java.util.Calendar;

/**
 *
 * @author redayoub
 */
public class Post {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    private String title;
    private String description;
    private long postDate;
    private long postReminder;

    public Post(String title, String description, long postDate, long postReminder) {
        this.title = title;
        this.description = description;
        this.postDate = postDate;
        this.postReminder = postReminder;
    }

    public long getPostDate() {
        return postDate;
    }

    public void setPostDate(long postDate) {
        this.postDate = postDate;
    }

    public long getPostReminder() {
        return postReminder;
    }

    public void setPostReminder(long postReminder) {
        this.postReminder = postReminder;
    }

    

    public Post() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    
    
    
    
}
