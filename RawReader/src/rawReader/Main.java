package rawReader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;

public class Main extends JFrame implements ActionListener{
    
    private static final String UNOPENED_FILE_ERROR = new String("A .raw file must be opened first, under File > Open.");
    private static final String INVALID_FILE_ERROR = new String("Invalid .raw file.");
    private File rawFile;
    private int xPixel;
    private int yPixel;
    private ProcessRaw image;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem openSubmenu;
    private boolean fileChosen = false;
    private JLabel FileNameLabel;
    private JLabel HeightLabel;
    private JLabel WidthLabel;
    private JMenuItem saveAsSubmenu;
    private JMenu toolsMenu;
    private JMenuItem regionOfInterestSubmenu;
    private JMenuItem pixelValueEditorSubmenu;
    private JFrame frame;
    private JPanel historyPanel;
    private JMenu mnEdit;
    private JMenuItem mntmFlip;
    private JMenuItem mntmRotateLeft;
    private JMenuItem mntmFlipVertically;
    private JMenuItem mntmFlipHorizontally;
    private static JTextArea txtrHello;
    private JPanel historyLabelPanel;
    private JLabel historyLabel;

    public Main(){
       
        // setting up GUI window
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
            System.err.println( "Could not set correct GUI parameters. Aborting program." );
            e1.printStackTrace();
            System.exit( 1 );
        }
        
        frame = new JFrame(".raw Reader");
        frame.setSize( 548,370);
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        // configuring menu bar elements
        menuBar = new JMenuBar();      
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic( KeyEvent.VK_F );
        menuBar.add( fileMenu );
        
        openSubmenu = new JMenuItem("Open");
        openSubmenu.addActionListener( this );
        fileMenu.add( openSubmenu );
        
        saveAsSubmenu = new JMenuItem("Save As Bitmap");
        fileMenu.add(saveAsSubmenu);
        saveAsSubmenu.addActionListener( this );
        
        frame.setJMenuBar( menuBar );
        
        toolsMenu = new JMenu("Tools");
        toolsMenu.setMnemonic('T');
        menuBar.add(toolsMenu);
        
        regionOfInterestSubmenu = new JMenuItem("Calculate Mean of Region");
        toolsMenu.add(regionOfInterestSubmenu);
        regionOfInterestSubmenu.addActionListener( this );
        
        pixelValueEditorSubmenu = new JMenuItem("Pixel Value Editor");
        toolsMenu.add(pixelValueEditorSubmenu);
        
        mnEdit = new JMenu("Edit");
        mnEdit.setMnemonic('E');
        menuBar.add(mnEdit);
        
        mntmFlip = new JMenuItem("Rotate Right");
        mnEdit.add(mntmFlip);
        
        mntmRotateLeft = new JMenuItem("Rotate Left");
        mnEdit.add(mntmRotateLeft);
        
        mntmFlipHorizontally = new JMenuItem("Flip Horizontally");
        mnEdit.add(mntmFlipHorizontally);
        
        mntmFlipVertically = new JMenuItem("Flip Vertically");
        mnEdit.add(mntmFlipVertically);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        pixelValueEditorSubmenu.addActionListener( this );
        
        // creating initial GUI graphic elements
        JPanel imageInfoPane = new JPanel();
        frame.getContentPane().add(imageInfoPane, BorderLayout.NORTH);
        imageInfoPane.setLayout(new GridLayout(0, 1, 0, 0));
        
        FileNameLabel = new JLabel("File Name:");
        FileNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        imageInfoPane.add(FileNameLabel);
        
        HeightLabel = new JLabel("Height: ");
        HeightLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        imageInfoPane.add(HeightLabel);
        
        WidthLabel = new JLabel("Width:");
        WidthLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        imageInfoPane.add(WidthLabel);
        
        historyPanel = new JPanel();
        frame.getContentPane().add(historyPanel, BorderLayout.SOUTH);
        
        txtrHello = new JTextArea(10, 60);
        txtrHello.setLineWrap(true);
        txtrHello.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtrHello);
        scroll.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        
        historyPanel.add(scroll);
        
        historyLabelPanel = new JPanel();
        frame.getContentPane().add(historyLabelPanel, BorderLayout.CENTER);
        
        historyLabel = new JLabel("Edit History:");
        historyLabelPanel.add(historyLabel);
        
        frame.pack();
        frame.setVisible( true );
    }
    
    public void actionPerformed(ActionEvent e){
        
        // actions for selecting and opening .raw file
        if( e.getSource() == openSubmenu ){
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter( "RAW files", "raw");
            fileChooser.setFileFilter( filter );
            int fileOpenReturn = fileChooser.showOpenDialog(null);
            if(fileOpenReturn == JFileChooser.APPROVE_OPTION){
                String filename = new String(fileChooser.getSelectedFile()+"");
                try{
                    String fileExtension = filename.substring( filename.lastIndexOf( "." ) +1); 
                    if(fileExtension.equals("raw")){
                        rawFile = fileChooser.getSelectedFile();
                        fileChosen = true;
                        image = new ProcessRaw( rawFile );
                        FileNameLabel.setText( "File Name: " + rawFile.getName());
                        HeightLabel.setText( "Height: "+image.getHeight() + " pixels");
                        WidthLabel.setText( "Width: "+image.getWidth() + " pixels");
                    }else{
                        throw new IllegalArgumentException();
                    }
                }catch(Exception fileNameError){
                    JOptionPane.showMessageDialog( frame, INVALID_FILE_ERROR,
                    "Error", JOptionPane.PLAIN_MESSAGE);
                }
            }
        // actions for opening pixel value editor
        } else if (e.getSource() == pixelValueEditorSubmenu){
            if(fileChosen){
                PixelValueEditor pixelEditFrame = new PixelValueEditor(image);
                pixelEditFrame.make();
            }else{
                JOptionPane.showMessageDialog( frame, UNOPENED_FILE_ERROR,
                "Error", JOptionPane.PLAIN_MESSAGE);
            }
        } else if (e.getSource() == regionOfInterestSubmenu){
            if(fileChosen){
                MeanOfRegion meanOfRegionFrame = new MeanOfRegion(image);
                meanOfRegionFrame.make();
            }else{
                JOptionPane.showMessageDialog( frame, UNOPENED_FILE_ERROR,
                "Error", JOptionPane.PLAIN_MESSAGE);
            }
        } else if (e.getSource() == saveAsSubmenu){
            if(fileChosen){
                
            }else{
                JOptionPane.showMessageDialog( frame, UNOPENED_FILE_ERROR,
                "Error", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
    
    public static void main(String args[]){
        Main gui = new Main();
    }
    public static void editHistory(String text){
       txtrHello.append(text); 
    }
}






