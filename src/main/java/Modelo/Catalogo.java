package Modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

// Guarda todas las cartas en listas circulares y controla cual se esta viendo.
public class Catalogo {

    // Formas en las que se pueden ver las cartas.
    public enum Modo { TODAS, NIVEL, ATRIBUTO, ATAQUE, DEFENSA }

    // Lista con todas las cartas del archivo.
    private final ListaCircularDoble<CartaYugioh> todas = new ListaCircularDoble<>();

    // Una lista por cada nivel y una lista por cada atributo.
    private final TreeMap<Integer, ListaCircularDoble<CartaYugioh>> porNivel = new TreeMap<>();
    private final TreeMap<String, ListaCircularDoble<CartaYugioh>> porAtributo = new TreeMap<>();

    // Listas ordenadas por puntos de ataque y por puntos de defensa.
    private final ListaCircularDoble<CartaYugioh> ataqueSubiendo = new ListaCircularDoble<>();
    private final ListaCircularDoble<CartaYugioh> ataqueBajando = new ListaCircularDoble<>();
    private final ListaCircularDoble<CartaYugioh> defensaSubiendo = new ListaCircularDoble<>();
    private final ListaCircularDoble<CartaYugioh> defensaBajando = new ListaCircularDoble<>();

    // Lista que se esta viendo y datos de como se eligio.
    private ListaCircularDoble<CartaYugioh> listaActual;
    private Modo modo;
    private int nivelElegido;
    private String atributoElegido;
    private boolean menorAMayor;

    // Lee el archivo, arma las listas y empieza mostrando todas las cartas.
    public Catalogo(InputStream archivo) throws IOException {
        ArrayList<CartaYugioh> cartas = leerArchivo(archivo);
        armarListas(cartas);
        mostrarTodas();
    }

    // Lee el csv linea por linea. La primera linea tiene los titulos y se salta.
    // En el archivo las comas de las descripciones ya vienen cambiadas por punto y coma.
    private ArrayList<CartaYugioh> leerArchivo(InputStream archivo) throws IOException {
        ArrayList<CartaYugioh> cartas = new ArrayList<>();
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(archivo, StandardCharsets.UTF_8))) {
            String linea = lector.readLine();
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 9) {
                    cartas.add(new CartaYugioh(datos[0], datos[1], convertir(datos[2]), datos[3],
                            convertir(datos[4]), convertir(datos[5]), datos[6], datos[7], datos[8].trim()));
                }
            }
        }
        return cartas;
    }

    // Convierte el texto a numero. Si dice N/A regresa menos uno.
    private int convertir(String texto) {
        if (texto.trim().equals("N/A")) {
            return -1;
        }
        return Integer.parseInt(texto.trim());
    }

    // Acomoda cada carta en la lista de todas, en la de su nivel y en la de su atributo.
    // Despues arma las listas ordenadas por ataque y por defensa.
    private void armarListas(ArrayList<CartaYugioh> cartas) {
        for (CartaYugioh carta : cartas) {
            todas.agregar(carta);
            if (carta.esMonstruo()) {
                ListaCircularDoble<CartaYugioh> listaNivel = porNivel.get(carta.getNivel());
                if (listaNivel == null) {
                    listaNivel = new ListaCircularDoble<>();
                    porNivel.put(carta.getNivel(), listaNivel);
                }
                listaNivel.agregar(carta);

                ListaCircularDoble<CartaYugioh> listaAtributo = porAtributo.get(carta.getAtributo());
                if (listaAtributo == null) {
                    listaAtributo = new ListaCircularDoble<>();
                    porAtributo.put(carta.getAtributo(), listaAtributo);
                }
                listaAtributo.agregar(carta);
            }
        }

        ArrayList<CartaYugioh> conAtaque = new ArrayList<>();
        ArrayList<CartaYugioh> conDefensa = new ArrayList<>();
        for (CartaYugioh carta : cartas) {
            if (carta.getAtaque() >= 0) {
                conAtaque.add(carta);
            }
            if (carta.getDefensa() >= 0) {
                conDefensa.add(carta);
            }
        }
        llenarOrdenada(conAtaque, Comparator.comparingInt(CartaYugioh::getAtaque).thenComparing(CartaYugioh::getNombre),
                ataqueSubiendo, ataqueBajando);
        llenarOrdenada(conDefensa, Comparator.comparingInt(CartaYugioh::getDefensa).thenComparing(CartaYugioh::getNombre),
                defensaSubiendo, defensaBajando);
    }

    // Ordena las cartas de menor a mayor y llena las dos listas, una de menor a mayor y otra al reves.
    private void llenarOrdenada(ArrayList<CartaYugioh> cartas, Comparator<CartaYugioh> criterio,
                                ListaCircularDoble<CartaYugioh> subiendo, ListaCircularDoble<CartaYugioh> bajando) {
        cartas.sort(criterio);
        for (int i = 0; i < cartas.size(); i++) {
            subiendo.agregar(cartas.get(i));
        }
        for (int i = cartas.size() - 1; i >= 0; i--) {
            bajando.agregar(cartas.get(i));
        }
    }

    // Los siguientes metodos cambian la lista que se esta viendo.
    public void mostrarTodas() {
        listaActual = todas;
        modo = Modo.TODAS;
        listaActual.irAlInicio();
    }

    public void mostrarPorNivel(int nivel) {
        ListaCircularDoble<CartaYugioh> lista = porNivel.get(nivel);
        if (lista != null) {
            listaActual = lista;
            modo = Modo.NIVEL;
            nivelElegido = nivel;
            listaActual.irAlInicio();
        }
    }

    public void mostrarPorAtributo(String atributo) {
        ListaCircularDoble<CartaYugioh> lista = porAtributo.get(atributo);
        if (lista != null) {
            listaActual = lista;
            modo = Modo.ATRIBUTO;
            atributoElegido = atributo;
            listaActual.irAlInicio();
        }
    }

    public void mostrarPorAtaque(boolean menorAMayor) {
        listaActual = menorAMayor ? ataqueSubiendo : ataqueBajando;
        modo = Modo.ATAQUE;
        this.menorAMayor = menorAMayor;
        listaActual.irAlInicio();
    }

    public void mostrarPorDefensa(boolean menorAMayor) {
        listaActual = menorAMayor ? defensaSubiendo : defensaBajando;
        modo = Modo.DEFENSA;
        this.menorAMayor = menorAMayor;
        listaActual.irAlInicio();
    }

    // Mueve la carta actual una posicion hacia adelante o hacia atras.
    public void avanzar() {
        listaActual.avanzar();
    }

    public void retroceder() {
        listaActual.retroceder();
    }

    public CartaYugioh getActual() {
        return listaActual.verActual();
    }

    public CartaYugioh getSiguiente() {
        return listaActual.verSiguiente();
    }

    public CartaYugioh getAnterior() {
        return listaActual.verAnterior();
    }

    // Posicion de la carta actual y total de cartas de la lista, por ejemplo 34 de 283.
    public int getPosicion() {
        return listaActual.getPosicion();
    }

    public int getTotal() {
        return listaActual.tamanio();
    }

    public Modo getModo() {
        return modo;
    }

    public int getNivelElegido() {
        return nivelElegido;
    }

    public String getAtributoElegido() {
        return atributoElegido;
    }

    public boolean isMenorAMayor() {
        return menorAMayor;
    }

    // Niveles y atributos que existen, para llenar las listas desplegables.
    public ArrayList<Integer> getNiveles() {
        return new ArrayList<>(porNivel.keySet());
    }

    public ArrayList<String> getAtributos() {
        return new ArrayList<>(porAtributo.keySet());
    }
}
