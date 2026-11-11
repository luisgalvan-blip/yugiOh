package Vista;

// Acciones que la vista le avisa al controlador.
public interface AccionesVista {
    void mostrarTodas();

    // Si no se eligio nada en la lista desplegable llega null.
    void mostrarPorNivel(Integer nivel);

    void mostrarPorAtributo(String atributo);

    void mostrarPorAtaque(boolean menorAMayor);

    void mostrarPorDefensa(boolean menorAMayor);

    void siguiente();

    void anterior();

    void alternarMusica();
}
