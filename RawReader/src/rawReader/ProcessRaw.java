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
    
    public void setWidth(int x) throws IOException{
        reader.seek( 0 );
        byte[] newWidth = intToByteArray(x);
        reader.write( newWidth, 2, 2 );
        this.width = x;
    }
    
    public void setHeight(int y) throws IOException{
        reader.seek( 2 );
        byte[] newHeight = intToByteArray(y);
        reader.write( newHeight, 2, 2 );
        this.height = y;
    }
    
    public int getPixelValue(int x, int y) throws IOException{
        reader.seek( (this.width* y *2) + x*2 + PIXEL_BUFFER_SIZE );
        byte[] pixelBuffer = new byte[PIXEL_BUFFER_SIZE];
        reader.read( pixelBuffer, 2, 2 );
        return byteArrayToInt( pixelBuffer );
    }
    
    public void setPixelValue(int x, int y, int value) throws IOException{
            reader.seek( (this.width* y *2)+ x*2 + PIXEL_BUFFER_SIZE );
            byte[] newPixelValue = intToByteArray(value);
            reader.write( newPixelValue, 2, 2 );
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
