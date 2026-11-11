package Vista;

import Modelo.CartaYugioh;
import Modelo.Catalogo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

// Vista del catalogo. Solo dibuja lo que le indica el controlador
// y le avisa las acciones del usuario.
public class VistaCatalogo {
    // Tamanos de las imagenes, la carta actual es mucho mas grande que los controles.
    private static final double ANCHO_GRANDE = 250;
    private static final double ANCHO_CHICO = 100;
    private static final double PROPORCION = 1.458;

    private static final Color DORADO = Color.web("#d4af37");
    private static final String ESTILO_BOTON = "-fx-background-color: #2a2550; -fx-text-fill: #d4af37; "
            + "-fx-font-size: 14px; -fx-font-weight: bold; -fx-border-color: #d4af37; -fx-border-width: 2; "
            + "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 12 8 12; -fx-cursor: hand;";

    // Objeto al que se le avisan las acciones, que es el controlador.
    private AccionesVista acciones;

    private final BorderPane raiz = new BorderPane();

    // Controles del menu de la izquierda.
    private final Button btnTodas = new Button("Mostrar todas las cartas");
    private final Button btnNivel = new Button("Mostrar por nivel");
    private final Button btnAtributo = new Button("Mostrar por atributo");
    private final Button btnAtaque = new Button("Mostrar por puntos de ataque");
    private final Button btnDefensa = new Button("Mostrar por puntos de defensa");
    private final ComboBox<Integer> cmbNivel = new ComboBox<>();
    private final ComboBox<String> cmbAtributo = new ComboBox<>();
    private final CheckBox chkAtaque = new CheckBox("De menor a mayor");
    private final CheckBox chkDefensa = new CheckBox("De menor a mayor");
    private final Button btnSonido = new Button();

    // Elementos de la zona donde se ven las cartas.
    private final Label lblTitulo = new Label();
    private final StackPane zonaActual = new StackPane();
    private final StackPane zonaAnterior = new StackPane();
    private final StackPane zonaSiguiente = new StackPane();
    private final Button btnAnterior = new Button(" Anterior");
    private final Button btnSiguiente = new Button("Siguiente ");
    private final Label lblContador = new Label();
    private final Label lblNombre = new Label();
    private final Label lblDato = new Label();
    private final Label lblInfo = new Label();
    private final Label lblMensaje = new Label();

    // Imagenes ya cargadas, para no pedirlas otra vez cada que se muestran.
    private final HashMap<String, Image> imagenes = new HashMap<>();

    // Arma la ventana con el menu a la izquierda y las cartas al centro.
    public VistaCatalogo() {
        Label titulo = new Label("Catálogo Yu-Gi-Oh!");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 26));
        titulo.setTextFill(DORADO);

        for (Button boton : new Button[]{btnTodas, btnNivel, btnAtributo, btnAtaque, btnDefensa}) {
            boton.setStyle(ESTILO_BOTON);
        }
        cmbNivel.setPromptText("Elige un nivel");
        cmbNivel.setPrefWidth(170);
        cmbAtributo.setPromptText("Elige un atributo");
        cmbAtributo.setPrefWidth(170);
        chkAtaque.setTextFill(Color.WHITE);
        chkDefensa.setTextFill(Color.WHITE);
        btnSonido.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        // Cada control le avisa al controlador lo que quiso hacer el usuario.
        btnTodas.setOnAction(e -> acciones.mostrarTodas());
        btnNivel.setOnAction(e -> acciones.mostrarPorNivel(cmbNivel.getValue()));
        btnAtributo.setOnAction(e -> acciones.mostrarPorAtributo(cmbAtributo.getValue()));
        btnAtaque.setOnAction(e -> acciones.mostrarPorAtaque(chkAtaque.isSelected()));
        btnDefensa.setOnAction(e -> acciones.mostrarPorDefensa(chkDefensa.isSelected()));
        btnAnterior.setOnAction(e -> acciones.anterior());
        btnSiguiente.setOnAction(e -> acciones.siguiente());
        btnSonido.setOnAction(e -> acciones.alternarMusica());

        Region espacio = new Region();
        VBox.setVgrow(espacio, Priority.ALWAYS);
        HBox filaSonido = new HBox(btnSonido);
        filaSonido.setAlignment(Pos.CENTER_RIGHT);
        VBox menu = new VBox(18, titulo, crearFila(btnTodas, null), crearFila(btnNivel, cmbNivel),
                crearFila(btnAtributo, cmbAtributo), crearFila(btnAtaque, chkAtaque),
                crearFila(btnDefensa, chkDefensa), lblMensaje, espacio, filaSonido);
        menu.setPadding(new Insets(30));
        menu.setPrefWidth(500);

        btnAnterior.setStyle(ESTILO_BOTON);
        btnSiguiente.setStyle(ESTILO_BOTON);

        lblTitulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        lblTitulo.setTextFill(DORADO);
        lblContador.setFont(Font.font(15));
        lblContador.setTextFill(Color.WHITE);
        lblNombre.setFont(Font.font("System", FontWeight.BOLD, 21));
        lblNombre.setTextFill(Color.WHITE);
        lblDato.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblDato.setTextFill(DORADO);
        lblInfo.setFont(Font.font(13));
        lblInfo.setTextFill(Color.LIGHTGRAY);
        lblInfo.setWrapText(true);
        lblInfo.setTextAlignment(TextAlignment.CENTER);
        lblInfo.setMaxWidth(560);
        lblMensaje.setTextFill(Color.ORANGE);
        lblMensaje.setFont(Font.font(14));
        lblMensaje.setWrapText(true);
        lblMensaje.setMaxWidth(450);

        // Las cartas pequenas son solo de vista, para cambiar de carta se usan las flechas de abajo.
        Label etiquetaAnterior = new Label("Anterior");
        Label etiquetaSiguiente = new Label("Siguiente");
        etiquetaAnterior.setTextFill(Color.WHITE);
        etiquetaSiguiente.setTextFill(Color.WHITE);
        VBox columnaAnterior = new VBox(6, zonaAnterior, etiquetaAnterior);
        VBox columnaSiguiente = new VBox(6, zonaSiguiente, etiquetaSiguiente);
        columnaAnterior.setAlignment(Pos.CENTER);
        columnaSiguiente.setAlignment(Pos.CENTER);
        HBox cartas = new HBox(30, columnaAnterior, zonaActual, columnaSiguiente);
        cartas.setAlignment(Pos.CENTER);

        // Fila de abajo con las flechas y el numero de carta.
        HBox flechas = new HBox(25, btnAnterior, lblContador, btnSiguiente);
        flechas.setAlignment(Pos.CENTER);
        VBox visor = new VBox(10, lblTitulo, cartas, lblNombre, lblDato, lblInfo, flechas);
        visor.setAlignment(Pos.CENTER);
        visor.setPadding(new Insets(12, 20, 15, 20));

        raiz.setLeft(menu);
        raiz.setCenter(visor);
        raiz.setStyle("-fx-background-color: linear-gradient(to bottom right, #14122b, #2b1d4a);");
    }

    // Regresa el nodo principal que se pone en la ventana.
    public Parent getRaiz() {
        return raiz;
    }

    // Conecta la vista con el controlador.
    public void setAcciones(AccionesVista acciones) {
        this.acciones = acciones;
    }

    // Llena las listas desplegables con los niveles y atributos que existen.
    public void cargarOpciones(ArrayList<Integer> niveles, ArrayList<String> atributos) {
        cmbNivel.getItems().setAll(niveles);
        cmbAtributo.getItems().setAll(atributos);
    }

    // Dibuja la carta actual, la anterior y la siguiente con sus datos.
    public void refrescar(Catalogo catalogo, String mensaje) {
        CartaYugioh actual = catalogo.getActual();
        lblTitulo.setText(crearTitulo(catalogo));
        zonaActual.getChildren().setAll(crearImagen(actual, ANCHO_GRANDE));
        zonaAnterior.getChildren().setAll(crearImagen(catalogo.getAnterior(), ANCHO_CHICO));
        zonaSiguiente.getChildren().setAll(crearImagen(catalogo.getSiguiente(), ANCHO_CHICO));
        lblContador.setText("(" + catalogo.getPosicion() + "/" + catalogo.getTotal() + ")");
        lblNombre.setText(actual.getNombre());
        lblDato.setText(crearDato(catalogo.getModo(), actual));
        lblInfo.setText(crearInfo(actual));
        lblMensaje.setText(mensaje);
    }

    // Cambia el icono de la bocina segun si suena la musica.
    public void mostrarEstadoMusica(boolean sonando) {
        btnSonido.setGraphic(crearIconoBocina(sonando));
    }

    // Une un boton con el control que va a su derecha.
    private HBox crearFila(Button boton, Node control) {
        boton.setPrefWidth(270);
        HBox fila = new HBox(15, boton);
        if (control != null) {
            fila.getChildren().add(control);
        }
        fila.setAlignment(Pos.CENTER_LEFT);
        return fila;
    }

    // Texto de arriba que dice que se esta viendo.
    private String crearTitulo(Catalogo catalogo) {
        String orden = catalogo.isMenorAMayor() ? "de menor a mayor" : "de mayor a menor";
        return switch (catalogo.getModo()) {
            case TODAS -> "Todas las cartas";
            case NIVEL -> "Cartas de nivel " + catalogo.getNivelElegido();
            case ATRIBUTO -> "Cartas de atributo " + catalogo.getAtributoElegido();
            case ATAQUE -> "Puntos de ataque " + orden;
            case DEFENSA -> "Puntos de defensa " + orden;
        };
    }

    // Dato principal de la carta segun la opcion que se eligio.
    private String crearDato(Catalogo.Modo modo, CartaYugioh carta) {
        return switch (modo) {
            case TODAS -> carta.esMonstruo() ? "Atributo " + carta.getAtributo() + "   Nivel " + carta.getNivelTexto()
                    : "Carta de magia o trampa";
            case NIVEL -> "Nivel " + carta.getNivelTexto();
            case ATRIBUTO -> "Atributo " + carta.getAtributo();
            case ATAQUE -> "Ataque " + carta.getAtaqueTexto();
            case DEFENSA -> "Defensa " + carta.getDefensaTexto();
        };
    }

    // Linea con el resto de los datos de la carta.
    private String crearInfo(CartaYugioh carta) {
        if (carta.esMonstruo()) {
            return carta.getAtributo() + "  |  Nivel " + carta.getNivelTexto() + "  |  " + carta.getTipo()
                    + "  |  ATK " + carta.getAtaqueTexto() + "  |  DEF " + carta.getDefensaTexto()
                    + "  |  " + carta.getRareza();
        }
        return carta.getTipo() + "  |  " + carta.getRareza();
    }

    // Dibuja la imagen de una carta. Detras lleva un rectangulo con el nombre,
    // asi si la imagen no carga se sigue viendo la carta.
    private StackPane crearImagen(CartaYugioh carta, double ancho) {
        Rectangle fondo = new Rectangle(ancho, ancho * PROPORCION);
        fondo.setArcWidth(8);
        fondo.setArcHeight(8);
        fondo.setFill(Color.web("#2a2550"));
        fondo.setStroke(DORADO);

        Label nombre = new Label(carta.getNombre());
        nombre.setWrapText(true);
        nombre.setMaxWidth(ancho - 12);
        nombre.setTextAlignment(TextAlignment.CENTER);
        nombre.setTextFill(Color.WHITE);
        nombre.setFont(Font.font(ancho > 150 ? 16 : 11));

        ImageView imagen = new ImageView(cargarImagen(carta));
        imagen.setFitWidth(ancho);
        imagen.setPreserveRatio(true);
        return new StackPane(fondo, nombre, imagen);
    }

    // Busca la imagen primero en la carpeta imagenes y si no esta la pide por internet.
    private Image cargarImagen(CartaYugioh carta) {
        Image imagen = imagenes.get(carta.getId());
        if (imagen == null) {
            URL local = getClass().getResource("/imagenes/" + carta.getId() + ".jpg");
            String direccion;
            if (local != null) {
                direccion = local.toExternalForm();
            } else {
                direccion = "https://images.ygoprodeck.com/images/cards/" + carta.getId() + ".jpg";
            }
            imagen = new Image(direccion, 300, 0, true, true, true);
            imagenes.put(carta.getId(), imagen);
        }
        return imagen;
    }

    // Dibuja el icono de la bocina, con ondas si suena y con una cruz si esta apagada.
    private Node crearIconoBocina(boolean sonando) {
        Polygon cuerpo = new Polygon(0, 8, 8, 8, 16, 0, 16, 24, 8, 16, 0, 16);
        cuerpo.setFill(DORADO);
        Group icono = new Group(cuerpo);
        if (sonando) {
            for (int radio = 6; radio <= 12; radio += 6) {
                Arc onda = new Arc(16, 12, radio, radio, -45, 90);
                onda.setType(ArcType.OPEN);
                onda.setFill(Color.TRANSPARENT);
                onda.setStroke(DORADO);
                onda.setStrokeWidth(2);
                icono.getChildren().add(onda);
            }
        } else {
            Line linea1 = new Line(22, 6, 34, 18);
            Line linea2 = new Line(34, 6, 22, 18);
            linea1.setStroke(Color.CRIMSON);
            linea2.setStroke(Color.CRIMSON);
            linea1.setStrokeWidth(3);
            linea2.setStrokeWidth(3);
            icono.getChildren().addAll(linea1, linea2);
        }
        return icono;
    }
}
