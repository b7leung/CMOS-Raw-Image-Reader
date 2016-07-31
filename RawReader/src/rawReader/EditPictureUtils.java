package rawReader;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

public class EditPictureUtils{
	
	private RawImage image;
	private static final String ROTATE_RIGHT_MESSAGE = "Rotated image right.";
	private static final String ROTATE_LEFT_MESSAGE = "Rotated image left.";
	private static final String FLIP_HORIZONTALLY_MESSAGE = "Flipped image horizontally.";
	private static final String FLIP_VERTICALLY_MESSAGE = "Flipped image vertically.";

	public EditPictureUtils(RawImage image){
		this.image = image;
	}
	
	class rotateRight extends SwingWorker<RawImage,String>{
		
		// rotates image right
		@Override
		protected RawImage doInBackground() throws IOException {
			int progress = 0;
			setProgress(progress);
			
		    int x,y;
		    int[] pixelValueBuffer = new int[image.getHeight()*image.getWidth()];
		    int bufferIndex = 0;
		    
		    for(x=0; x< image.getWidth(); x++){
		        for(y=image.getHeight()-1; y>= 0; y--){
		           pixelValueBuffer[bufferIndex++] = image.getPixelValue( x, y ); 
		           //bufferIndex++;
		        }
		        progress = (int)( ((double)x) / ((double)image.getWidth()) * 50 );
		        setProgress(progress);
		    }
		    
		    bufferIndex = 0;
		    
		    for(y=0; y<image.getHeight(); y++){
		        for(x=0; x<image.getWidth(); x++){
		            image.setPixelValue( x, y, pixelValueBuffer[bufferIndex++] );
		        }
		        progress = 50 + (int)( ((double)y)/((double)image.getHeight())*50 );
		        setProgress(progress);
		    }
		    
		    int temp = image.getHeight();
		    image.setHeight( image.getWidth() );
		    image.setWidth( temp );
		    setProgress(100);
		    
			//Main.historyManager.log( new HistoryItem( HistoryItem.ROTATE_RIGHT, ROTATE_RIGHT_MESSAGE ) );
			Main.editHistory( new HistoryItem( HistoryItem.ROTATE_RIGHT, ROTATE_RIGHT_MESSAGE ) );
			return image;
		}

		@Override 
		protected void done(){
			try {
				Main.updateImage(get());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// rotates image left
	public class rotateLeft extends SwingWorker<RawImage, String>{

		@Override
		protected RawImage doInBackground() throws Exception {
			
			int progress = 0;
			setProgress(progress);
			int x,y;
			int[] pixelValueBuffer = new int[image.getHeight()*image.getWidth()];
			int bufferIndex = 0;
			
			for(x = image.getWidth() -1; x>=0; x--){
				for(y=0; y<image.getHeight(); y++){
					pixelValueBuffer[bufferIndex++] = image.getPixelValue(x, y);
					//bufferIndex++;
				}
				progress = (int) ( ((double)image.getWidth()-x) / ((double)image.getWidth()) * 50 );
				setProgress(progress);
			}
			
			bufferIndex = 0;
			
			for(y=0; y<image.getHeight(); y++){
				for(x=0; x<image.getWidth(); x++){
					image.setPixelValue(x, y, pixelValueBuffer[bufferIndex++]);
				}
				progress = 50 + (int) (((double)y)/((double)image.getHeight())*50);
				setProgress(progress);
			}
			
			int temp = image.getHeight();
		    image.setHeight( image.getWidth() );
		    image.setWidth( temp );
		    setProgress(100);

			//Main.editHistory( ROTATE_LEFT_MESSAGE );
			return image;
		}
		
		@Override
		protected void done(){
			try {
				Main.updateImage(get());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public RawImage flipHorizontally() throws IOException{
	    int buffer;
	    for(int x = 0; x < image.getWidth(); x++){
	        for(int y = 0; y < (image.getHeight()/2); y++){
	            buffer = image.getPixelValue( x, y );
	            image.setPixelValue( x, y, image.getPixelValue( x, image.getHeight()-y-1 ) );
	            image.setPixelValue( x, image.getHeight()-y-1, buffer );
	            
	        }
	    }
		//Main.editHistory( FLIP_HORIZONTALLY_MESSAGE );
		return image;
	}
	
	public RawImage flipVertically() throws IOException{
		int buffer;
		for(int y = 0; y<image.getHeight(); y++){
			for(int x = 0; x<(image.getWidth()/2); x++){
				buffer = image.getPixelValue(x, y);
				image.setPixelValue( x, y, image.getPixelValue(image.getWidth()-x -1, y) );
				image.setPixelValue( image.getWidth()-x -1, y, buffer );
			}
		}	
		//Main.editHistory( FLIP_VERTICALLY_MESSAGE );
		return image;
	}

}
