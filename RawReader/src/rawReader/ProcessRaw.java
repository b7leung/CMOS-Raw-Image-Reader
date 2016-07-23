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
            reader = new RandomAccessFile(image, "rw");
            
            byte[] byteWidth = new byte[PIXEL_VALUE_SIZE];
            byte[] byteLength = new byte[PIXEL_VALUE_SIZE];
            reader.read(byteWidth, 2, 2);
            reader.read(byteLength, 2, 2);
            /*
            for(int i = 0; i< byteWidth.length; i++){
            	Byte byt = new Byte(byteWidth[i]);
            	System.out.print(byt.intValue() + " ");
            }
            System.out.println();
            System.out.println(byteWidth);
            System.out.println(reverseByteArray( byteWidth ) );
            System.out.println(byteArrayToInt(byteWidth));
            System.out.println(byteArrayToInt( reverseByteArray( byteWidth ) ));
            */
            this.width = byteArrayToInt( reverseByteArray( byteWidth ) );
            this.height= byteArrayToInt( reverseByteArray( byteLength ) );

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
        System.out.println(byteArrayToInt(pixelBuffer));
        System.out.println(byteArrayToInt( reverseByteArray( pixelBuffer) ));
        return byteArrayToInt( reverseByteArray( pixelBuffer) );
    }
    
    public void setPixelValue(int x, int y, int value){
        try {
            reader.seek( this.height* y + x*2 + PIXEL_VALUE_SIZE );
            byte[] newPixelValue = intToByteArray(value);
            reader.write( newPixelValue, 2, 2 );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
    
    private int byteArrayToInt(byte[] byteArray){
        ByteBuffer wrapped = ByteBuffer.wrap( byteArray ).order(java.nio.ByteOrder.BIG_ENDIAN);
        return wrapped.getInt();
    }
    
    private byte[] intToByteArray(int x){
        return ByteBuffer.allocate( PIXEL_VALUE_SIZE ).putInt( x ).array();
    }
    
    private byte[] reverseByteArray(byte[] byteArray){
    	byte[] reversedArray = new byte[byteArray.length];
    	for(int i = 0; i < byteArray.length; i++){
    		reversedArray[i] = byteArray[byteArray.length-1-i];
    	}
    	return reversedArray;
    }
    
    public int getHeight(){
        return height;
    }
    
    public int getWidth(){
        return width;
    }

}
