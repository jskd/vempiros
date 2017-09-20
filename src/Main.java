
import controller.ScreensController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.ScreenMenu;

/**
 * Main class of program
 */
public class Main extends Application {

    public static final String MENU_SCREEN = "menu";

    @Override
    public void start(Stage primaryStage) throws Exception{

        ScreensController mainContainer = new ScreensController();

        mainContainer.loadScreen(Main.MENU_SCREEN, new ScreenMenu());
        mainContainer.setScreen(Main.MENU_SCREEN);

        Scene scene = new Scene(mainContainer, 860, 640);
        scene.getStylesheets().add("stylesheet.css");

        primaryStage.setTitle("Vempiros");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(860);
        primaryStage.setMinHeight(640);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
