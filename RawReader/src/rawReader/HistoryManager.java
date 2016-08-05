package rawReader;

import java.awt.Color;
import java.util.LinkedList;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class HistoryManager {
    
    private static final Color LAST_ACTION_COLOR = Color.red;
	private RawImage image;
	private LinkedList<HistoryItem> actionHistory = new LinkedList<HistoryItem>();
	private int amt = 0;
	private int currentPos = -1;
	// position of last action
	private JTextPane historyTextPane;
	private Main guiHandler;
	

	public HistoryManager(Main guiHandler, RawImage image, JTextPane historyTextPane ){
		this.guiHandler = guiHandler;
		this.image = image;
		this.historyTextPane= historyTextPane;
	}
	
	public void log(HistoryItem action) throws BadLocationException{
		
		//historyTextArea.append(image.getFilename()+ " -- " +action.description+"\n");
	    write(image.getFilename()+ " -- " +action.getDescription(), LAST_ACTION_COLOR, true, false);
		actionHistory.add(action);
		if(amt == currentPos+1){
			amt++;
			currentPos++;
		}else{
			currentPos++;
			for(int i = amt - 1; i>currentPos; i--){
			//for(int i = currentPos + 1; i<amt; i++){
				actionHistory.remove(i);
				//historyTextArea.append(  );
			}
		}
	}
	public void undo(){
		HistoryItem previousAction = actionHistory.get(currentPos--);
		if(previousAction.getActionType() == previousAction.ROTATE_RIGHT){
			System.out.println("rotate left");
			guiHandler.rotateLeft();
		}
		
	}
	
	public void redo(){
		HistoryItem nextAction = actionHistory.get(++currentPos);
	}
	
	public int getAmt(){
		return amt;
	}
	
	public int getCurrentPos(){
		return currentPos;
	}
	
	private void parseAction(HistoryItem action){
		
	}
	
	private void write(String text, Color color, boolean bold, boolean italics) throws BadLocationException{
	    StyledDocument doc = historyTextPane.getStyledDocument();
	    SimpleAttributeSet attributes = new SimpleAttributeSet();
	    StyleConstants.setForeground( attributes, color );
	    StyleConstants.setBold( attributes, bold );
	    StyleConstants.setItalic( attributes, italics );
	    doc.insertString( doc.getLength(), text+"\n", attributes );
	}
	

}
