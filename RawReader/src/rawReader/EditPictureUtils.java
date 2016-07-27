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
	
	public ProcessRaw rotateRight(){
		Main.editHistory( ROTATE_RIGHT_MESSAGE );
		return image;
	}
	
	public ProcessRaw rotateLeft(){
		Main.editHistory( ROTATE_LEFT_MESSAGE );
		return image;
	}
	
	public ProcessRaw flipHorizontally(){
		Main.editHistory( FLIP_HORIZONTALLY_MESSAGE );
		return image;
	}
	
	public ProcessRaw flipVertically() throws IOException{
		int buffer;
		int temp;
		//int x = 0;
		//
		int y = 0;
		for(int y = 0; y<image.getHeight(); y++){
			for(int x = 0; x<(image.getWidth()/2); x++){
				buffer = image.getPixelValue(x, y);
				image.setPixelValue( x, y, image.getPixelValue(image.getWidth()-x -1, y) );
				image.setPixelValue( image.getWidth()-x -1, y, buffer );
				if(y<10){
				System.out.println("FIRST: "+buffer + " LOCATION: ( " + x + ", "+ y + " )"+
				" ________________________ SWITCHED WITH: "+image.getPixelValue(image.getWidth()-x -1, y) + " LOCATION: ( "+(image.getWidth()-x -1)+ ", "+ y + " )");
				
				}
			}
		}
		Main.editHistory( FLIP_VERTICALLY_MESSAGE );
		return image;
	}

}
