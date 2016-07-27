package rawReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ProcessRaw {
    
    private final static int PIXEL_BUFFER_SIZE = 4;
    private final static int PIXEL_VALUE_SIZE = 2;
    
    private int height;
    private int width;
    private ArrayList<Integer> pixelData;
    
    private RandomAccessFile reader;
    
    // open image, find length & width, and fill pixel data array.
    public ProcessRaw(File image) throws IOException{
        try {
            reader = new RandomAccessFile(image, "rw");
            byte[] byteWidth = new byte[PIXEL_BUFFER_SIZE];
            byte[] byteLength = new byte[PIXEL_BUFFER_SIZE];
            reader.read(byteWidth, 2, 2);
            reader.read(byteLength, 2, 2);

            this.width = byteArrayToInt( byteWidth  );
            this.height= byteArrayToInt( byteLength );
        } catch (FileNotFoundException e) {
        	throw new FileNotFoundException();
        }
    }
    
    public int getPixelValue(int x, int y) throws IOException{
        reader.seek( this.height* y + x*2 + PIXEL_BUFFER_SIZE );
        byte[] pixelBuffer = new byte[PIXEL_BUFFER_SIZE];
        reader.read( pixelBuffer, 2, 2 );
        return byteArrayToInt( pixelBuffer );
    }
    
    public void setPixelValue(int x, int y, int value){
        try {
            reader.seek( this.height* y + x*2 + PIXEL_BUFFER_SIZE );
            byte[] newPixelValue = intToByteArray(value);
            reader.write( newPixelValue, 2, 2 );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
    
    private int byteArrayToInt(byte[] byteArray){
    	byte[] byteArrayBuffer = {byteArray[2], byteArray[3]};
        ByteBuffer wrapped = ByteBuffer.wrap( byteArrayBuffer ).order(java.nio.ByteOrder.LITTLE_ENDIAN);
        return wrapped.getShort();
    }
    
    private byte[] intToByteArray(int x){
    	byte [] byteArray = ByteBuffer.allocate( PIXEL_BUFFER_SIZE ).putInt( x ).array();
    	byte[] byteArrayBuffer= {0,0, byteArray[3], byteArray[2]};
    	return byteArrayBuffer;
    }
    
    public int getHeight(){
        return height;
    }
    
    public int getWidth(){
        return width;
    }

}
