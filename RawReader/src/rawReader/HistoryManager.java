package rawReader;

import java.awt.Color;
import java.util.LinkedList;

import javax.lang.model.element.Element;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class HistoryManager {
    
    private static final Color PREV_ACTION_COLOR = Color.black;
    private static final Color LAST_ACTION_COLOR = Color.black;
    private static final Color UNDO_ACTION_COLOR = Color.gray;

    private Main guiHandler;
	private JTextPane historyTextPane;
	private RawImage image;
	private LinkedList<HistoryItem> actionHistory = new LinkedList<HistoryItem>();
	// current position of last action in linkedlist
	private int currentPos = -1;
	private ImageChanger imageChanger;


	public HistoryManager(Main guiHandler, RawImage image, JTextPane historyTextPane ){
	    imageChanger = new ImageChanger(guiHandler, image, this);
		this.guiHandler = guiHandler;
		this.image = image;
		this.historyTextPane= historyTextPane;
	}
	
	public void log(HistoryItem action) throws BadLocationException{
		System.out.println( "Amt before: " + getAmt()  );
				
		// if there are previously undoed items upon logging of an entirely new action, the undoed items are erased
		if(currentPos+1< getAmt() && !action.isRedo() ){
		    System.out.println( "wiping" );
			delete(currentPos+1, getAmt());
			//for(int i = getAmt() - 1; i>currentPos; i--){
			for(int i = currentPos+1; i< getAmt(); i++){
				actionHistory.remove(i);
			}
		}
		actionHistory.add(action);
		currentPos++;

		// changing attributes of previous actions
		if(currentPos>0){
		    delete(0,currentPos);
		    for(int i = 0; i < currentPos; i++){
		        write(image.getFilename()+ " -- " +actionHistory.get(i).getDescription(), PREV_ACTION_COLOR, false, false);
		    }
		}
		// writing new action with desired attributes
	    write(image.getFilename()+ " -- " +action.getDescription(), LAST_ACTION_COLOR, true, false);
		System.out.println( "Amt after: " + getAmt()  );
		
	}
	
	private void delete(int start, int end) throws BadLocationException{
	    /*
	    javax.swing.text.Element root = historyTextPane.getDocument().getDefaultRootElement();
	    for(int i = start; i<end; i++){
	        javax.swing.text.Element first = root.getElement( start );
	        historyTextPane.getDocument().remove( first.getStartOffset(), first.getEndOffset() );
	    }
	    */
	    String content = historyTextPane.getDocument().getText( 0, historyTextPane.getDocument().getLength() );
	    int startLinePos=0;
	    int endLinePos=0;
	    int newLineCount = 0;
	    int beginningOfLine= 0;
	    for(int i=0; i<content.length();i++){
	        if(content.charAt( i )=='\n'){
	            if(newLineCount == start){
	                startLinePos = beginningOfLine;
	            }
	            if(newLineCount == end -1){
	                endLinePos = i+1;
	            }
	            beginningOfLine = i+1;
	            newLineCount++;
	        }
	    }
	    //System.out.println( "size: "+content.length() + " removing: " + startLinePos + ", "+ endLinePos );
	    historyTextPane.getDocument().remove( startLinePos, endLinePos -startLinePos);
	}

	public void undo() throws BadLocationException{

		delete(currentPos -1,actionHistory.size());
		write(image.getFilename()+ " -- " +actionHistory.get(currentPos-1).getDescription(), LAST_ACTION_COLOR, true, false );
		for(int i = currentPos; i<actionHistory.size(); i++){
		    write(image.getFilename()+ " -- " +actionHistory.get(i).getDescription(), UNDO_ACTION_COLOR, false, false );
		}
	    HistoryItem previousAction = actionHistory.get(currentPos);
        if(previousAction.getActionType() == previousAction.ROTATE_RIGHT){
            //guiHandler.rotateLeft();
		    imageChanger.apply( HistoryItem.ROTATE_RIGHT, true );
        }
        currentPos--;
	}
	
	public void redo() throws BadLocationException{
	    // changing attributes of last action and the desired action to redo
	    delete(currentPos,getAmt());
		write(image.getFilename()+ " -- " +actionHistory.get(currentPos).getDescription(), PREV_ACTION_COLOR, false, false );
		currentPos++;
		HistoryItem nextAction = actionHistory.get(currentPos);
		if(nextAction.getActionType() == nextAction.ROTATE_RIGHT){
            //guiHandler.rotateRight();
		    imageChanger.apply( HistoryItem.ROTATE_RIGHT, true );
        }
		write(image.getFilename()+ " -- " +actionHistory.get(currentPos).getDescription(), LAST_ACTION_COLOR, true, false );
		for(int i = currentPos + 1; i<getAmt();i++){
		    write(image.getFilename()+ " -- " +actionHistory.get(currentPos).getDescription(), UNDO_ACTION_COLOR, false, false );
		}
	}
	
	public int getAmt(){
		return actionHistory.size();
	}
	
	public int getCurrentPos(){
		return currentPos;
	}
	
	private void parseAction(HistoryItem action){
		
	}
	
	private void write(String text, Color color, boolean underline, boolean italics) throws BadLocationException{
	    StyledDocument doc = historyTextPane.getStyledDocument();
	    SimpleAttributeSet attributes = new SimpleAttributeSet();
	    StyleConstants.setForeground( attributes, color );
	    StyleConstants.setItalic( attributes, italics );
	    StyleConstants.setUnderline( attributes, underline );
	    doc.insertString( doc.getLength(), text+"\n", attributes );
	}
	
}
