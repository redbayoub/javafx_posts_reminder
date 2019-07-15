/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postsreminder.images;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

/**
 *
 * @author redayoub
 */
public class Images {
    public final static URL posts_icon_img_url=Images.class.getResource("posts_icon.png");
    
    public static Image loadImage(String name){
        URL url=Images.class.getResource(name);
        java.awt.Image image=Toolkit.getDefaultToolkit().getImage(url);
        
        return image;
    }
}
