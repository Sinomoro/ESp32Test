import Gui.GUI;
import javafx.scene.layout.Pane;

public class Controller {
    //ensures that everythng comunicates aproprate
    GUI gui = new GUI();


    public void start()
    {

    }
    public Pane getGui()
    {
        return gui.getGui();
    }
}
