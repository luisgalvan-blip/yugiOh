package Modelo;

// Guarda la informacion de una carta de Yu-Gi-Oh.
// Las cartas de magia y trampa no tienen atributo ni puntos, esos datos valen menos uno.
public class CartaYugioh {
    private final String nombre;
    private final String atributo;
    private final int nivel;
    private final String tipo;
    private final int ataque;
    private final int defensa;
    private final String rareza;
    private final String descripcion;
    private final String id;

    public CartaYugioh(String nombre, String atributo, int nivel, String tipo, int ataque,
                       int defensa, String rareza, String descripcion, String id) {
        this.nombre = nombre;
        this.atributo = atributo;
        this.nivel = nivel;
        this.tipo = tipo;
        this.ataque = ataque;
        this.defensa = defensa;
        this.rareza = rareza;
        this.descripcion = descripcion;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAtributo() {
        return atributo;
    }

    public int getNivel() {
        return nivel;
    }

    public String getTipo() {
        return tipo;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public String getRareza() {
        return rareza;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getId() {
        return id;
    }

    // Indica si es un monstruo, las magias y trampas no tienen atributo.
    public boolean esMonstruo() {
        return !atributo.equals("N/A");
    }

    // Los siguientes metodos regresan el dato como texto para mostrarlo.
    public String getNivelTexto() {
        return nivel < 0 ? "N/A" : String.valueOf(nivel);
    }

    public String getAtaqueTexto() {
        return ataque < 0 ? "N/A" : String.valueOf(ataque);
    }

    public String getDefensaTexto() {
        return defensa < 0 ? "N/A" : String.valueOf(defensa);
    }
}
