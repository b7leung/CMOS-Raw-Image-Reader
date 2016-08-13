package rawReader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

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
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import java.awt.FlowLayout;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.KeyStroke;
import javax.swing.ProgressMonitor;

import java.awt.event.InputEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JSeparator;

public class Main extends JFrame implements ActionListener{

//public class Main extends JFrame implements ActionListener, PropertyChangeListener {
    // constant string messages
    private static final String UNOPENED_FILE_ERROR = new String( "A .raw file must be opened first, under File > Open." );
    private static final String INVALID_FILE_ERROR = new String(
    "Invalid .raw file. " + "Check that file is a .raw file, permissions to read/write are granted, and that no other processes are accessing the file." );
    private static final String TRANSFORMATION_ERROR = new String( "Could not perform requested transformation." );

    // elements relating to the image chosen
    private File rawFile;
    private int xPixel;
    private int yPixel;
    private RawImage image;
    private boolean fileChosen = false;
    private EditPictureUtils imageEditor;

    // Menu Bar elements
    private final JMenuBar menuBar;
    private final JMenu fileMenu;
    private final JMenuItem openSubmenu;
    private final JMenuItem saveAsSubmenu;
    private final JMenu toolsMenu;
    private final JMenuItem regionOfInterestSubmenu;
    private final JMenuItem pixelValueEditorSubmenu;
    private final JMenuItem rotateRightSubmenu;
    private final JMenuItem rotateLeftSubmenu;
    private final JMenuItem flipVerticallySubmenu;
    private final JMenuItem flipHorizontallySubmenu;
    private final JMenu editMenu;
    private ArrayList<JMenuItem> fileDependent = new ArrayList<JMenuItem>();

    // GUI Elements
    private final JFrame frame;
    private final JLabel FileNameLabel;
    private JLabel HeightLabel;
    private JLabel WidthLabel;
    private final JPanel historyPanel;
    private JTextPane historyTextPane;
    private JMenuItem undoSubmenu;
    private JMenuItem redoSubmenu;
    private JMenu viewMenu;
    private JCheckBoxMenuItem verboseHistoryCheckbox;
    private JLabel lblHistory;
    private JSeparator separator;
    private ProgressMonitor progressMonitor;

    static HistoryManager historyManager;
    ImageChanger imageChanger;

    public Main() {

        // setting up GUI window
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
            System.err.println( "Could not set correct GUI parameters. Aborting program." );
            e1.printStackTrace();
            System.exit( 1 );
        }

        frame = new JFrame( ".raw Reader" );
        frame.setSize( 548, 370 );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        // configuring menu bar elements
        menuBar = new JMenuBar();
        fileMenu = new JMenu( "File" );
        fileMenu.setMnemonic( 'f' );
        menuBar.add( fileMenu );

        openSubmenu = new JMenuItem( "Open" );
        openSubmenu.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_O, InputEvent.CTRL_MASK ) );
        openSubmenu.setMnemonic( 'o' );
        openSubmenu.addActionListener( this );
        fileMenu.add( openSubmenu );

        saveAsSubmenu = new JMenuItem( "Save As Bitmap" );
        saveAsSubmenu.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_S, InputEvent.CTRL_MASK ) );
        saveAsSubmenu.setMnemonic( 's' );
        saveAsSubmenu.setEnabled( false );
        fileDependent.add( saveAsSubmenu );
        fileMenu.add( saveAsSubmenu );
        saveAsSubmenu.addActionListener( this );

        frame.setJMenuBar( menuBar );

        editMenu = new JMenu( "Edit" );
        editMenu.setMnemonic( 'e' );
        menuBar.add( editMenu );

        undoSubmenu = new JMenuItem( "Undo" );
        undoSubmenu.setEnabled( false );
        undoSubmenu.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_Z, InputEvent.CTRL_MASK ) );
        editMenu.add( undoSubmenu );
        undoSubmenu.addActionListener(this);

        redoSubmenu = new JMenuItem( "Redo" );
        redoSubmenu.setEnabled( false );
        redoSubmenu.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_Y, InputEvent.CTRL_MASK ) );
        editMenu.add( redoSubmenu );
        redoSubmenu.addActionListener( this );

        separator = new JSeparator();
        editMenu.add( separator );

        rotateRightSubmenu = new JMenuItem( "Rotate Right" );
        rotateRightSubmenu.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_R, InputEvent.CTRL_MASK ) );
        rotateRightSubmenu.setMnemonic( 'r' );
        rotateRightSubmenu.setEnabled( false );
        fileDependent.add( rotateRightSubmenu );
        editMenu.add( rotateRightSubmenu );
        rotateRightSubmenu.addActionListener( this );

        rotateLeftSubmenu = new JMenuItem( "Rotate Left" );
        rotateLeftSubmenu.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_L, InputEvent.CTRL_MASK ) );
        rotateLeftSubmenu.setMnemonic( 'l' );
        rotateLeftSubmenu.setEnabled( false );
        fileDependent.add( rotateLeftSubmenu );
        editMenu.add( rotateLeftSubmenu );
        rotateLeftSubmenu.addActionListener( this );

        flipHorizontallySubmenu = new JMenuItem( "Flip Horizontally" );
        flipHorizontallySubmenu.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_H, InputEvent.CTRL_MASK ) );
        flipHorizontallySubmenu.setMnemonic( 'h' );
        flipHorizontallySubmenu.setEnabled( false );
        fileDependent.add( flipHorizontallySubmenu );
        editMenu.add( flipHorizontallySubmenu );
        flipHorizontallySubmenu.addActionListener( this );

        flipVerticallySubmenu = new JMenuItem( "Flip Vertically" );
        flipVerticallySubmenu.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_V, InputEvent.CTRL_MASK ) );
        flipVerticallySubmenu.setMnemonic( 'v' );
        flipVerticallySubmenu.setEnabled( false );
        fileDependent.add( flipVerticallySubmenu );
        editMenu.add( flipVerticallySubmenu );

        viewMenu = new JMenu( "View" );
        viewMenu.setMnemonic( 'v' );
        menuBar.add( viewMenu );

        verboseHistoryCheckbox = new JCheckBoxMenuItem( "Verbose History" );
        verboseHistoryCheckbox.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_V, InputEvent.ALT_MASK ) );
        verboseHistoryCheckbox.setMnemonic( 'v' );
        verboseHistoryCheckbox.setToolTipText( "Verbose mode records all actions. Regular mode records only file changes." );
        viewMenu.add( verboseHistoryCheckbox );

        toolsMenu = new JMenu( "Tools" );
        toolsMenu.setMnemonic( 't' );
        menuBar.add( toolsMenu );

        regionOfInterestSubmenu = new JMenuItem( "Calculate Mean of Region" );
        regionOfInterestSubmenu.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_M, InputEvent.CTRL_MASK ) );
        regionOfInterestSubmenu.setMnemonic( 'm' );
        regionOfInterestSubmenu.setEnabled( false );
        fileDependent.add( regionOfInterestSubmenu );
        toolsMenu.add( regionOfInterestSubmenu );
        regionOfInterestSubmenu.addActionListener( this );

        pixelValueEditorSubmenu = new JMenuItem( "Pixel Value Editor" );
        pixelValueEditorSubmenu.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_E, InputEvent.CTRL_MASK ) );
        pixelValueEditorSubmenu.setMnemonic( 'e' );
        pixelValueEditorSubmenu.setEnabled( false );
        fileDependent.add( pixelValueEditorSubmenu );
        toolsMenu.add( pixelValueEditorSubmenu );
        pixelValueEditorSubmenu.addActionListener( this );
        flipVerticallySubmenu.addActionListener( this );

        frame.getContentPane().setLayout( new BorderLayout( 0, 0 ) );

        // creating initial GUI graphic elements
        JPanel imageInfoPane = new JPanel();
        frame.getContentPane().add( imageInfoPane, BorderLayout.NORTH );
        imageInfoPane.setLayout( new GridLayout( 0, 1, 0, 0 ) );

        FileNameLabel = new JLabel( "File Name:" );
        FileNameLabel.setFont( new Font( "Tahoma", Font.PLAIN, 16 ) );
        imageInfoPane.add( FileNameLabel );

        HeightLabel = new JLabel( "Height: " );
        HeightLabel.setFont( new Font( "Tahoma", Font.PLAIN, 16 ) );
        imageInfoPane.add( HeightLabel );

        WidthLabel = new JLabel( "Width:" );
        WidthLabel.setFont( new Font( "Tahoma", Font.PLAIN, 16 ) );
        imageInfoPane.add( WidthLabel );

        lblHistory = new JLabel( "History:" );
        lblHistory.setHorizontalAlignment( SwingConstants.CENTER );
        imageInfoPane.add( lblHistory );

        historyPanel = new JPanel();
        historyPanel.setPreferredSize( new Dimension(600, 250) );;
        frame.getContentPane().add( historyPanel, BorderLayout.CENTER );
        historyPanel.setLayout( new BorderLayout( 0, 0 ) );

        //historyTextArea = new JTextArea( 10, 60 );
        historyTextPane = new JTextPane();
        historyTextPane.setEditable( false);
        
        /*
        StyledDocument doc = historyTextPane.getStyledDocument();
        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground( keyWord, Color.RED );
        StyleConstants.setBold( keyWord, true );
        try {
            doc.insertString( 0, "start", null );
            //doc.insertString( 0, "\nhi", keyWord );
            doc.insertString( doc.getLength(), "\nhi", keyWord );
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       */ 
        //historyTextArea.setBounds( 0, 0, 100, 300 );
        //historyTextArea.setLineWrap( false );
        JScrollPane scroll = new JScrollPane( historyTextPane);
        scroll.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS );
        scroll.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

        historyPanel.add( scroll );

        frame.pack();
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );
    }

    public void actionPerformed(ActionEvent e) {

        // actions for selecting and opening .raw file
        if ( e.getSource() == openSubmenu ) {

            // opening file browser
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter( "RAW files", "raw" );
            fileChooser.setFileFilter( filter );
            int fileOpenReturn = fileChooser.showOpenDialog( null );

            // checking if file chosen was a valid .raw file
            if ( fileOpenReturn == JFileChooser.APPROVE_OPTION ) {
                String filenameBuffer = new String( fileChooser.getSelectedFile() + "" );
                try {
                    String fileExtension = filenameBuffer.substring( filenameBuffer.lastIndexOf( "." ) + 1 );
                    if ( fileExtension.equals( "raw" ) ) {

                        // if the file is valid, then name/height/width values are initialized
                        rawFile = fileChooser.getSelectedFile();
                        fileChosen = true;
                        image = new RawImage( rawFile );
                        historyManager = new HistoryManager( this, image, historyTextPane);
                        imageChanger = new ImageChanger(this, image, historyManager);
                        FileNameLabel.setText( "File Name: " + image.getFilename() );
                        HeightLabel.setText( "Height: " + image.getHeight() + " pixels" );
                        WidthLabel.setText( "Width: " + image.getWidth() + " pixels" );
                        for ( int i = 0; i < fileDependent.size(); i++ ) {
                            fileDependent.get( i ).setEnabled( true );
                        }
                        historyManager.log(new HistoryItem(HistoryItem.SELECT_IMAGE, "Added image.",false));
                    } else {
                        throw new IllegalArgumentException();
                    }
                } catch (Exception fileNameError) {
                    JOptionPane.showMessageDialog( frame, INVALID_FILE_ERROR, "Error", JOptionPane.PLAIN_MESSAGE );
                }
            }
            // actions for opening pixel value editor
        } else if ( e.getSource() == pixelValueEditorSubmenu ) {

            PixelValueEditor pixelEditFrame = new PixelValueEditor( image );
            pixelEditFrame.make();

        } else if ( e.getSource() == regionOfInterestSubmenu ) {

            MeanOfRegion meanOfRegionFrame = new MeanOfRegion( image );
            meanOfRegionFrame.make();

        } else if ( e.getSource() == saveAsSubmenu ) {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle( "Save As" );
            int chosen = fileChooser.showSaveDialog( null );
            if ( chosen == JFileChooser.APPROVE_OPTION ) {
                ImageConverter converter = new ImageConverter( image );
                converter.saveAsBMP( fileChooser.getSelectedFile() );
            }

        } else if ( e.getSource() == rotateRightSubmenu ) {
            //rotateRight();
            imageChanger.apply( HistoryItem.ROTATE_RIGHT, false );
        } else if ( e.getSource() == rotateLeftSubmenu ) {
            rotateLeft();
        } else if ( e.getSource() == flipHorizontallySubmenu ) {

            try {
                imageEditor = new EditPictureUtils( this, image, historyManager );
                image = imageEditor.flipHorizontally();
            } catch (IOException e1) {
                JOptionPane.showMessageDialog( frame, TRANSFORMATION_ERROR, "Error", JOptionPane.PLAIN_MESSAGE );
            }

        } else if ( e.getSource() == flipVerticallySubmenu ) {

            try {
                imageEditor = new EditPictureUtils( this, image, historyManager );
                image = imageEditor.flipVertically();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog( frame, TRANSFORMATION_ERROR, "Error", JOptionPane.PLAIN_MESSAGE );
            }
        } else if (e.getSource()== undoSubmenu){
           try {
            historyManager.undo();
        } catch (BadLocationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } 
            updateReUndo();
        } else if (e.getSource() == redoSubmenu){
            try {
                historyManager.redo();
            } catch (BadLocationException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            updateReUndo();
        }
    }

    public static void main(String args[]) {

        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {

                Main gui = new Main();
            }
        } );
    }
/*
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        
        if ( evt.getPropertyName().equals( "progress" ) ) {
            int progress = ( (Integer) evt.getNewValue() ).intValue();
            progressMonitor.setProgress( progress );
        }
        if(evt.getPropertyName().equals( "state" )&& SwingWorker.StateValue.DONE.equals(evt.getNewValue())){
            System.out.println( "done" );
        }
        
    }
*/
    public void updateImage(RawImage updatedImage) {

        image = updatedImage;
        HeightLabel.setText( "Height: " + image.getHeight() + " pixels" );
        WidthLabel.setText( "Width: " + image.getWidth() + " pixels" );

    }

    public void updateReUndo() {
        System.out.println( "current pos: " + historyManager.getCurrentPos() + " Total Amt: " + historyManager.getAmt());
        if ( historyManager.getCurrentPos() > 0 ) {
            undoSubmenu.setEnabled( true );
        } else {
            undoSubmenu.setEnabled( false );
        }

        if ( historyManager.getCurrentPos() < historyManager.getAmt() - 1 ) {
            redoSubmenu.setEnabled( true );
        } else {
            redoSubmenu.setEnabled( false );
        }
    }
/*
    public void rotateRight() {

        try {
            progressMonitor = new ProgressMonitor( Main.this, "Applying Transformation...", "", 0, 100 );
            progressMonitor.setMillisToDecideToPopup( 0 );
            progressMonitor.setProgress( 0 );
            EditPictureUtils.rotateRight imageEditor = new EditPictureUtils( this, image ).new rotateRight();
            imageEditor.addPropertyChangeListener( this );
            imageEditor.execute();
        } catch (Exception e1) {
            JOptionPane.showMessageDialog( frame, TRANSFORMATION_ERROR, "Error", JOptionPane.PLAIN_MESSAGE );
        }
    }
*/
    public void rotateLeft() {

        try {
            progressMonitor = new ProgressMonitor( Main.this, "Applying Transformation...", "", 0, 100 );
            progressMonitor.setMillisToDecideToPopup( 0 );
            progressMonitor.setProgress( 0 );
            //EditPictureUtils.rotateLeft imageEditor = new EditPictureUtils( this, image ).new rotateLeft();
            //imageEditor.addPropertyChangeListener( this );
            //imageEditor.execute();
        } catch (Exception e1) {
            JOptionPane.showMessageDialog( frame, TRANSFORMATION_ERROR, "Error", JOptionPane.PLAIN_MESSAGE );
        }
    }

}
