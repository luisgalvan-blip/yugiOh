package Modelo;

import java.util.ArrayList;

// Lista doblemente ligada y circular, es la misma del Eight Off.
// Se le agrego un nodo actual para poder avanzar y retroceder entre las cartas.
public class ListaCircularDoble<T> {

    // Nodo con el dato, el siguiente y el anterior.
    private static class Nodo<T> {
        T dato;
        Nodo<T> sig;
        Nodo<T> ant;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    // La posicion del nodo actual empieza en 1.
    private Nodo<T> cabeza;
    private Nodo<T> actual;
    private int tamanio;
    private int posicion;

    // Agrega un dato al final. En una lista circular el final esta antes de la cabeza.
    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            nuevo.sig = nuevo;
            nuevo.ant = nuevo;
            cabeza = nuevo;
            actual = nuevo;
            posicion = 1;
        } else {
            Nodo<T> ultimo = cabeza.ant;
            ultimo.sig = nuevo;
            nuevo.ant = ultimo;
            nuevo.sig = cabeza;
            cabeza.ant = nuevo;
        }
        tamanio++;
    }

    // Quita el primer dato y lo regresa. El nodo actual vuelve al inicio.
    public T removerPrimero() {
        if (cabeza == null) {
            return null; // no hay datos
        }
        T dato = cabeza.dato;
        if (tamanio == 1) {
            cabeza = null;
            actual = null;
            posicion = 0;
        } else {
            Nodo<T> ultimo = cabeza.ant;
            cabeza = cabeza.sig;
            cabeza.ant = ultimo;
            ultimo.sig = cabeza;
            irAlInicio();
        }
        tamanio--;
        return dato;
    }

    // Pone el nodo actual en la cabeza.
    public void irAlInicio() {
        actual = cabeza;
        posicion = (cabeza == null) ? 0 : 1;
    }

    // Avanza al siguiente nodo. Despues del ultimo sigue el primero.
    public void avanzar() {
        if (actual != null) {
            actual = actual.sig;
            posicion = posicion % tamanio + 1;
        }
    }

    // Retrocede al nodo anterior. Antes del primero esta el ultimo.
    public void retroceder() {
        if (actual != null) {
            actual = actual.ant;
            posicion = (posicion == 1) ? tamanio : posicion - 1;
        }
    }

    // Regresa el dato del nodo actual.
    public T verActual() {
        if (actual == null) {
            return null; // lista vacia
        }
        return actual.dato;
    }

    // Regresa el dato del nodo siguiente sin avanzar.
    public T verSiguiente() {
        if (actual == null) {
            return null; // lista vacia
        }
        return actual.sig.dato;
    }

    // Regresa el dato del nodo anterior sin retroceder.
    public T verAnterior() {
        if (actual == null) {
            return null; // lista vacia
        }
        return actual.ant.dato;
    }

    // Regresa en que posicion va el nodo actual.
    public int getPosicion() {
        return posicion;
    }

    public int tamanio() {
        return tamanio;
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    // Elimina todos los elementos.
    public void vaciar() {
        cabeza = null;
        actual = null;
        tamanio = 0;
        posicion = 0;
    }

    // Copia los elementos a un ArrayList dando una sola vuelta al circulo.
    public ArrayList<T> toArrayList() {
        ArrayList<T> resultado = new ArrayList<>();
        Nodo<T> nodo = cabeza;
        for (int i = 0; i < tamanio; i++) {
            resultado.add(nodo.dato);
            nodo = nodo.sig;
        }
        return resultado;
    }
}
