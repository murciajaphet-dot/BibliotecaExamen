package hn.uth.poo.biblioteca.excepciones;

public class DatosException extends RuntimeException {
    public DatosException(String message, Throwable cause) {
        super(message, cause);
    }
}