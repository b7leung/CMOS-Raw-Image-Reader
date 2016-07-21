package rawReader;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class PixelValueEditor implements ActionListener{

    private JTextField xTextField;
    private JTextField yTextField;
    private JButton findButton;
    private JButton changeButton;
    private int xPixel;
    private int yPixel;
    private ProcessRaw image;
    private JLabel PixelValue;
    
    public PixelValueEditor(ProcessRaw image){
        this.image = image;
    }

    public void make(){
        JFrame frmPixelValueEditor = new JFrame("demo");
        frmPixelValueEditor.setTitle("Pixel Value Editor");
        frmPixelValueEditor.setSize( 548,370);
        frmPixelValueEditor.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel panel = new JPanel();
        frmPixelValueEditor.getContentPane().add(panel, BorderLayout.NORTH);
        
        JLabel lblX = new JLabel("x");
        panel.add(lblX);
        
        xTextField = new JTextField();
        panel.add(xTextField);
        xTextField.setColumns(10);
        
        JLabel lblY = new JLabel("y");
        panel.add(lblY);
        
        yTextField = new JTextField();
        panel.add(yTextField);
        yTextField.setColumns(10);
        
        findButton = new JButton("find");
        panel.add(findButton);
        findButton.addActionListener( this );
        
        
        JPanel panel_1 = new JPanel();
        frmPixelValueEditor.getContentPane().add(panel_1, BorderLayout.CENTER);
        
        PixelValue = new JLabel("Pixel Value: ");
        panel_1.add(PixelValue);
        
        JLabel currentPixelValue = new JLabel("0");
        panel_1.add(currentPixelValue);
        
        changeButton = new JButton("Change");
        panel_1.add(changeButton);
        
        JPanel panel_2 = new JPanel();
        frmPixelValueEditor.getContentPane().add(panel_2, BorderLayout.SOUTH);

        frmPixelValueEditor.setVisible( true );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==findButton){
            xPixel = Integer.parseInt( xTextField.getText() );
            yPixel = Integer.parseInt(  yTextField.getText() );
            boolean valid = true;
            if(xPixel >= image.getWidth() ){
                System.out.println( "Error: " + xPixel + " is too big for the image width of " + image.getWidth() );
                valid = false;
            }
            if (xPixel < 0){
                System.out.println( "Error: pixel's x-coordinate cannot be negative" ); 
                valid = false;
            }
            if ( yPixel >= image.getWidth() ){
                System.out.println( "Error: " + yPixel + " is too big for the image height of " + image.getHeight() );
                valid = false;
            }
            if ( yPixel < 0 ){
                System.out.println( "Error: pixel's y-coordinate cannot be negative" ); 
                valid = false;
            } 
            if(valid){
                try {
                    PixelValue.setText( ""+image.getPixelValue( xPixel, yPixel ) );
                } catch (IOException e1) {
                    System.err.println( "Pixel Data could not be read." );
                }
            }
        }
        
        
    }
}
