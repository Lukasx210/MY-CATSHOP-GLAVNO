package clients.imageDisplay;

import middle.MiddleFactory;

public class ImageDisplayController
{
  private ImageDisplayModel model = null;
  private ImageDisplayView  view  = null;

  /**
   * Constructor
   * @param view The view from which the interaction came
   * @param middleFactory The MiddleFactory instance needed for ImageDisplayModel
   */
  public ImageDisplayController(ImageDisplayView view, MiddleFactory middleFactory) {
    this.view = view;
    
    // Create the model with the provided MiddleFactory and register the view as an observer
    model = new ImageDisplayModel(middleFactory);
    model.addObserver(view);

    // Start the background thread in the model
    new Thread(model::backgroundRun).start();
  }
}
