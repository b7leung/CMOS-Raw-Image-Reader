package rawReader;

import java.io.File;
import java.nio.file.Path;

public class ImageConverter {
    
    RawImage image;

    public ImageConverter(RawImage image){
       this.image = image; 
    }
    
    public void saveAsBMP(File filepath){
       System.out.println( filepath.getPath() + " with name " + filepath.getName()); 
    }
}
