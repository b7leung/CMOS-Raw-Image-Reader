package rawReader;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JFrame;

public class Main extends JFrame{
    
    public Main(){
        setTitle(".raw Reader");
        setSize(1000,700);
        
        WindowAdapter window = new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit( 0 );
            }
        };
        addWindowListener(window);
    }
    public static void main(String args[]){
        
        JFrame frame = new Main();
        frame.show();
        
        File rawFile = new File(args[0]);
        ProcessRaw image;
        try {
            image = new ProcessRaw( rawFile );
            System.out.println( "Width = " + image.getWidth() + " Length = " + image.getLength() );
            int x = 0;
            int y = 0;
            System.out.println( "The pixel data at ( " + x + ", " + y + " ) is " + image.getPixelValue( x, y ) );
            x=1;
            y=0;
            System.out.println( "The pixel data at ( " + x + ", " + y + " ) is " + image.getPixelValue( x, y ) );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
       
    }
}
