package clients.imageDisplay;

import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;
import java.awt.*;

/**
 * The standalone Image Display Client.
 * Displays images of stock items.
 * @author Mike Smith University of Brighton
 * @version 1.0
 */
public class ImageDisplayClient
{
    public static void main(String args[])
    {
        String stockURL = args.length < 1     // URL of stock RW
                ? Names.STOCK_RW      //  default  location
                : args[0];            //  supplied location

        RemoteMiddleFactory mrf = new RemoteMiddleFactory();
        mrf.setStockRWInfo(stockURL);
        displayGUI(mrf);                       // Create GUI
    }

    private static void displayGUI(MiddleFactory mf)
    {
        JFrame window = new JFrame();

        window.setTitle("Image Display Client");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageDisplayModel model = new ImageDisplayModel(mf);
        ImageDisplayView view = new ImageDisplayView(window, mf, 0, 0);
        ImageDisplayController cont = new ImageDisplayController(model, view);
        view.setController(cont);

        model.addObserver(view);       // Add observer to the model
        window.setVisible(true);       // Display Screen
    }
}
