package rawReader;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PixelFieldChecker{
	
	public static final String PIXEL_VALUE = "Pixel value";
	public static final String HEIGHT = "Y-coordinate";
	public static final String WIDTH = "X-coordinate";
	
	private String EXCEED_MAX_VALUE_ERROR = " cannot be larger than the maximum value (";
	private String NOT_INTEGER_VALUE_ERROR = " must be a non-negative integer value.";
	public String EMPTY_VALUE_ERROR = " cannot be blank.";
	
	private char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	private int maxPixelValue;
	private String pixelValue;
	private JFrame frame;
	
	public PixelFieldChecker(String pixelValue, int maxPixelValue, String fieldType, JFrame frame){
		
		this.frame = frame;
		this.maxPixelValue = maxPixelValue;
		this.pixelValue = pixelValue;
		EMPTY_VALUE_ERROR = fieldType + EMPTY_VALUE_ERROR;
		NOT_INTEGER_VALUE_ERROR = fieldType + NOT_INTEGER_VALUE_ERROR;
		EXCEED_MAX_VALUE_ERROR = fieldType + EXCEED_MAX_VALUE_ERROR + maxPixelValue + ").";
	}
	
	// returns true if entry is valid. Else, returns false and shows an error window.
	public boolean verifyValid(){
		
		if(pixelValue.equals("")){
			displayError(EMPTY_VALUE_ERROR);
			return false;
		}
		int d;
		for(int i = 0; i<pixelValue.length(); i++){
			for( d = 0; d < digits.length; d++ ){	
				if(pixelValue.charAt(i)==digits[d]){
					break;
				}
			}
			if(d == digits.length){
				displayError(NOT_INTEGER_VALUE_ERROR);
				return false;
			}
		}
		
		if(Integer.parseInt(pixelValue)>maxPixelValue){
			displayError(EXCEED_MAX_VALUE_ERROR);
			return false;
		}
		
		return true;
	}
	
	public void displayError(String errorMessage){
        JOptionPane.showMessageDialog( frame,
        errorMessage, "Error", JOptionPane.PLAIN_MESSAGE);
	}
}



