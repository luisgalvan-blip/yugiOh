package Controlador;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

// Reproduce la musica de fondo en ciclo. Si no encuentra el archivo el programa sigue sin musica.
public class ReproductorMusica {
    private MediaPlayer reproductor;
    private boolean sonando;

    // Carga el archivo de musica que esta en resources.
    public ReproductorMusica(String archivo) {
        try {
            URL direccion = getClass().getResource(archivo);
            if (direccion != null) {
                reproductor = new MediaPlayer(new Media(direccion.toExternalForm()));
                reproductor.setCycleCount(MediaPlayer.INDEFINITE);
                reproductor.setVolume(0.35);
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar la musica");
        }
    }

    // Empieza la musica.
    public void iniciar() {
        if (reproductor != null) {
            reproductor.play();
            sonando = true;
        }
    }

    // Si suena la pausa y si esta pausada la vuelve a poner.
    public void alternar() {
        if (reproductor != null) {
            if (sonando) {
                reproductor.pause();
            } else {
                reproductor.play();
            }
            sonando = !sonando;
        }
    }

    // Para la musica al cerrar el programa.
    public void detener() {
        if (reproductor != null) {
            reproductor.stop();
        }
    }

    public boolean isSonando() {
        return sonando;
    }
}
