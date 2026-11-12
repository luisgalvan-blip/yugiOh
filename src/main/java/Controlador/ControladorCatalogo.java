package Controlador;

import Modelo.Catalogo;
import Vista.AccionesVista;
import Vista.VistaCatalogo;

// Controlador. Recibe las acciones de la vista, le pide el cambio al modelo
// y despues le dice a la vista que se dibuje otra vez.
public class ControladorCatalogo implements AccionesVista {
    private final Catalogo catalogo;
    private final VistaCatalogo vista;
    private final ReproductorMusica musica;

    // Conecta el modelo, la vista y la musica, y dibuja el estado inicial.
    public ControladorCatalogo(Catalogo catalogo, VistaCatalogo vista, ReproductorMusica musica) {
        this.catalogo = catalogo;
        this.vista = vista;
        this.musica = musica;
        vista.setAcciones(this);
        vista.cargarOpciones(catalogo.getNiveles(), catalogo.getAtributos());
        musica.iniciar();
        vista.mostrarEstadoMusica(musica.isSonando());
        vista.refrescar(catalogo, "");
    }

    @Override
    public void mostrarTodas() {
        catalogo.mostrarTodas();
        vista.refrescar(catalogo, "");
    }

    // Si no se eligio un nivel se avisa al usuario.
    @Override
    public void mostrarPorNivel(Integer nivel) {
        if (nivel == null) {
            vista.refrescar(catalogo, "Primero elige un nivel.");
        } else {
            catalogo.mostrarPorNivel(nivel);
            vista.refrescar(catalogo, "");
        }
    }

    // Si no se eligio un atributo se avisa al usuario.
    @Override
    public void mostrarPorAtributo(String atributo) {
        if (atributo == null) {
            vista.refrescar(catalogo, "Primero elige un atributo.");
        } else {
            catalogo.mostrarPorAtributo(atributo);
            vista.refrescar(catalogo, "");
        }
    }

    @Override
    public void mostrarPorAtaque(boolean menorAMayor) {
        catalogo.mostrarPorAtaque(menorAMayor);
        vista.refrescar(catalogo, "");
    }

    @Override
    public void mostrarPorDefensa(boolean menorAMayor) {
        catalogo.mostrarPorDefensa(menorAMayor);
        vista.refrescar(catalogo, "");
    }

    @Override
    public void siguiente() {
        catalogo.avanzar();
        vista.refrescar(catalogo, "");
    }

    @Override
    public void anterior() {
        catalogo.retroceder();
        vista.refrescar(catalogo, "");
    }

    // Pone o quita la musica y cambia el icono de la bocina.
    @Override
    public void alternarMusica() {
        musica.alternar();
        vista.mostrarEstadoMusica(musica.isSonando());
    }
}
