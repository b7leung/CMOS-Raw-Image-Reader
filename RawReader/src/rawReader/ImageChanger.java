package rawReader;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;

import rawReader.EditPictureUtils.rotateRight;

public class ImageChanger implements PropertyChangeListener{
    
    private ProgressMonitor progressMonitor;
    private Main guiHandler;
    private RawImage image;
    private HistoryManager historyManager;
    private boolean redo;

    public ImageChanger(Main guiHandler,RawImage image, HistoryManager historyManager){
       this.guiHandler = guiHandler;
       this.image = image;
       this.historyManager = historyManager;
    }
    
    // applies transformations, then updates history
    public void apply(int action, boolean redo){
        
       this.redo = redo;
       if(action == HistoryItem.ROTATE_RIGHT){
            progressMonitor = new ProgressMonitor( guiHandler, "Applying Transformation...", "", 0, 100 );
            progressMonitor.setMillisToDecideToPopup( 0 );
            progressMonitor.setProgress( 0 );
            EditPictureUtils.rotateRight imageEditor = new EditPictureUtils( guiHandler, image, historyManager ).new rotateRight();
            imageEditor.addPropertyChangeListener( this );
            imageEditor.execute();
       }
        
        
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ( evt.getPropertyName().equals( "progress" ) ) {
            int progress = ( (Integer) evt.getNewValue() ).intValue();
            progressMonitor.setProgress( progress );
        }
        if(evt.getPropertyName().equals( "state" )&& SwingWorker.StateValue.DONE.equals(evt.getNewValue())){
            if(!redo){
            try {
                historyManager.log( new HistoryItem( HistoryItem.ROTATE_RIGHT, EditPictureUtils.ROTATE_RIGHT_MESSAGE, redo) );
                guiHandler.updateReUndo();
            } catch (BadLocationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            } 
        }
        
    }
}
