import javafx.application.Application;

public class Driver {

    public static void main(String[] args) {
        try {
            Application.launch(App.class, args);
        } catch (UnsupportedOperationException e) {
            System.out.println(e);
            System.err.println("Possible X server connection time out. Can be fixed by logging out and logging back in");
            System.exit(1);
        }
    }
}