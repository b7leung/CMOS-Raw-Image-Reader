package rawReader;

import java.io.File;
import java.nio.file.Path;

public class ImageConverter {
    
    ProcessRaw image;

    public ImageConverter(ProcessRaw image){
       this.image = image; 
    }
    
    public void saveAsBMP(File filepath){
       System.out.println( filepath.getPath() + " with name " + filepath.getName()); 
    }
}
