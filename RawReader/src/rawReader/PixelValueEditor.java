package rawReader;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
    private JLabel PixelValueLabel;
    private JFrame PixelValueEditorFrame;
    
    public PixelValueEditor(ProcessRaw image){
        this.image = image;
    }

    /**
     * @wbp.parser.entryPoint
     */
    public void make(){
        PixelValueEditorFrame = new JFrame("demo");
        PixelValueEditorFrame.setTitle("Pixel Value Editor");
        PixelValueEditorFrame.setSize( 548,370);
        PixelValueEditorFrame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        
        JPanel inputPanel = new JPanel();
        PixelValueEditorFrame.getContentPane().add(inputPanel, BorderLayout.NORTH);
        
        JLabel xLabel = new JLabel("x:");
        inputPanel.add(xLabel);
        
        xTextField = new JTextField();
        inputPanel.add(xTextField);
        xTextField.setColumns(10);
        
        JLabel yLabel = new JLabel("y:");
        inputPanel.add(yLabel);
        
        yTextField = new JTextField();
        inputPanel.add(yTextField);
        yTextField.setColumns(10);
        
        findButton = new JButton("Find");
        inputPanel.add(findButton);
        findButton.addActionListener( this );
        
        
        JPanel infoPanel = new JPanel();
        PixelValueEditorFrame.getContentPane().add(infoPanel, BorderLayout.CENTER);
        
        PixelValueLabel = new JLabel("Pixel Value: ");
        infoPanel.add(PixelValueLabel);
        
        changeButton = new JButton("Change");
        infoPanel.add(changeButton);

        PixelValueEditorFrame.setVisible( true );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==findButton){
            if(xTextField.getText().equals( "" )){
                
            }
            xPixel = Integer.parseInt( xTextField.getText() );
            yPixel = Integer.parseInt(  yTextField.getText() );
            boolean valid = true;
            if(xPixel >= image.getWidth() ){
                JOptionPane.showMessageDialog( PixelValueEditorFrame,
                "Error: " + xPixel + " is too big for the image width of " + image.getWidth(),
                "Error", JOptionPane.PLAIN_MESSAGE);
                valid = false;
            }
            if (xPixel < 0){
                JOptionPane.showMessageDialog( PixelValueEditorFrame,
                "Error: pixel's x-coordinate cannot be negative",
                "Error", JOptionPane.PLAIN_MESSAGE);
                valid = false;
            }
            if ( yPixel >= image.getWidth() ){
                JOptionPane.showMessageDialog( PixelValueEditorFrame,
                "Error: " + yPixel + " is too big for the image height of " + image.getHeight(),
                "Error", JOptionPane.PLAIN_MESSAGE);
                valid = false;
            }
            if ( yPixel < 0 ){
                JOptionPane.showMessageDialog( PixelValueEditorFrame,
                "Error: pixel's y-coordinate cannot be negative",
                "Error", JOptionPane.PLAIN_MESSAGE);
                valid = false;
            }
            if(valid){
                try {
                    PixelValueLabel.setText( "Pixel Value: "+image.getPixelValue( xPixel, yPixel ) );
                } catch (IOException e1) {
                    System.err.println( "Pixel Data could not be read." );
                }
            }
        }
        
        
    }
}
