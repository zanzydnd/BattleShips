package Main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mvc.Controller;
import mvc.Model;
import mvc.View;

public class Main extends Application {
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        View view = new View();
        Model model = new Model();
        Controller controller = new Controller(view,model);
        view.setController(controller);
        view.setModel(model);
        Scene scene = new Scene(view,1000,1000);
        primaryStage.setTitle("BattleSea");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
