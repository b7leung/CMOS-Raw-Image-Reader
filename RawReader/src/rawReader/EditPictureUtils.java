package rawReader;

import java.io.IOException;

public class EditPictureUtils {
	
	ProcessRaw image;
	private static final String ROTATE_RIGHT_MESSAGE = "Rotated image right.";
	private static final String ROTATE_LEFT_MESSAGE = "Rotated image left.";
	private static final String FLIP_HORIZONTALLY_MESSAGE = "Flipped image horizontally.";
	private static final String FLIP_VERTICALLY_MESSAGE = "Flipped image vertically.";

	public EditPictureUtils(ProcessRaw image){
		this.image = image;
	}
	
	public ProcessRaw rotateRight() throws IOException{
	    int temp = image.getHeight();
	    image.setHeight( image.getWidth() );
	    image.setWidth( temp );
		Main.editHistory( ROTATE_RIGHT_MESSAGE );
		return image;
	}
	
	public ProcessRaw rotateLeft() throws IOException{
	    int temp = image.getHeight();
	    image.setHeight( image.getWidth() );
	    image.setWidth( temp );
		Main.editHistory( ROTATE_LEFT_MESSAGE );
		return image;
	}
	
	public ProcessRaw flipHorizontally() throws IOException{
	    int buffer;
	    for(int x = 0; x < image.getWidth(); x++){
	        for(int y = 0; y < (image.getHeight()/2); y++){
	            buffer = image.getPixelValue( x, y );
	            image.setPixelValue( x, y, image.getPixelValue( x, image.getHeight()-y-1 ) );
	            image.setPixelValue( x, image.getHeight()-y-1, buffer );
	            
	        }
	    }
		Main.editHistory( FLIP_HORIZONTALLY_MESSAGE );
		return image;
	}
	
	public ProcessRaw flipVertically() throws IOException{
		int buffer;
		//int temp;
		for(int y = 0; y<image.getHeight(); y++){
			for(int x = 0; x<(image.getWidth()/2); x++){
				buffer = image.getPixelValue(x, y);
			    //System.out.println( "BEFORE: ( "+ x + ", "+y+" ) = "+ image.getPixelValue( x, y ) + " ---- ( "+ (image.getWidth()-x-1)+", "+ y +") = " + image.getPixelValue( image.getWidth()-x-1, y ));
				//temp = image.getPixelValue( image.getWidth()-x-1, y );
				image.setPixelValue( x, y, image.getPixelValue(image.getWidth()-x -1, y) );
				image.setPixelValue( image.getWidth()-x -1, y, buffer );
				/*
				system.out.println("first: "+buffer + " location: ( " + x + ", "+ y + " )"+
				" ___ switched with: "+temp + " location: ( "+(image.getwidth()-x -1)+ ", "+ y + " )");
				
			    system.out.println( "after: ( "+ x + ", "+y+" ) = "+ image.getpixelvalue( x, y ) + " ---- ( "+ (image.getwidth()-x-1)+", "+ y +") = " + image.getpixelvalue( image.getwidth()-x-1, y ));
			    system.out.println(  );
			    */
			}
		}	
		Main.editHistory( FLIP_VERTICALLY_MESSAGE );
		return image;
	}
}
