import Controlador.ControladorCatalogo;
import Controlador.ReproductorMusica;
import Modelo.Catalogo;
import Vista.VistaCatalogo;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;

// Clase principal. Crea el modelo, la vista y el controlador y los conecta.
public class YugiohApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    // Lee el archivo de cartas y abre la ventana.
    @Override
    public void start(Stage stage) {
        try {
            Catalogo catalogo = new Catalogo(getClass().getResourceAsStream("/cartas_yugioh_completo.csv"));
            VistaCatalogo vista = new VistaCatalogo();
            ReproductorMusica musica = new ReproductorMusica("/musica.wav");
            new ControladorCatalogo(catalogo, vista, musica);

            stage.setTitle("Catálogo de cartas Yu-Gi-Oh!");
            // el tamano de la ventana se ajusta a la pantalla para que no se corte
            double ancho = Math.min(1150, Screen.getPrimary().getVisualBounds().getWidth() - 20);
            double alto = Math.min(780, Screen.getPrimary().getVisualBounds().getHeight() - 50);
            stage.setScene(new Scene(vista.getRaiz(), ancho, alto));
            stage.setOnHidden(e -> musica.detener());
            stage.show();
        } catch (Exception e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("No se pudo abrir el archivo de cartas");
            alerta.setContentText("Revisa que cartas_yugioh_completo.csv este en la carpeta resources.");
            alerta.show();
        }
    }
}
