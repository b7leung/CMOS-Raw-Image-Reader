package rawReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ProcessRaw {
    
    private final static int PIXEL_VALUE_SIZE = 4;
    
    private int height;
    private int width;
    private ArrayList<Integer> pixelData;
    
    private RandomAccessFile reader;
    
    // open image, find length & width, and fill pixel data array.
    public ProcessRaw(File image) throws IOException{
        try {
            reader = new RandomAccessFile(image, "r");
            
            byte[] byteWidth = new byte[PIXEL_VALUE_SIZE];
            byte[] byteLength = new byte[PIXEL_VALUE_SIZE];
            reader.read(byteWidth, 2, 2);
            reader.read(byteLength, 2, 2);
            this.width = byteArrayToInt(byteWidth);
            this.height= byteArrayToInt(byteLength);

        } catch (FileNotFoundException e) {
            System.err.println( "ERROR: File could not be opened." );
            e.printStackTrace();
            System.exit( 1 );
        }
    }
    
    public int getPixelValue(int x, int y) throws IOException{
        reader.seek( this.height* y + x*2 + PIXEL_VALUE_SIZE );
        byte[] pixelBuffer = new byte[PIXEL_VALUE_SIZE];
        reader.read( pixelBuffer, 2, 2 );
        return byteArrayToInt(pixelBuffer);
    }
    
    private int byteArrayToInt(byte[] byteArray){
        ByteBuffer wrapped = ByteBuffer.wrap( byteArray );
        return wrapped.getInt();
    }
    
    public int getHeight(){
        return height;
    }
    
    public int getWidth(){
        return width;
    }

}
