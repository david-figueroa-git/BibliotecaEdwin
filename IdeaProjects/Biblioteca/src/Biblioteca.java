import java.util.ArrayList;
import java.util.HashMap;

public class Biblioteca {

    private ArrayList<Libro> libros;
    private HashMap<String, Libro> librosPorIsbn;

    public Biblioteca() {
        libros = new ArrayList<>();
        librosPorIsbn = new HashMap<>();
    }


    public boolean agregarLibro(Libro libro) {

        if (librosPorIsbn.containsKey(libro.getIsbn())) {
            return false;
        }

        libros.add(libro);
        librosPorIsbn.put(libro.getIsbn(), libro);

        return true;
    }


    public ArrayList<Libro> obtenerLibros() {
        return libros;
    }


    public ArrayList<Libro> buscarPorAutor(String autor) {

        ArrayList<Libro> resultado = new ArrayList<>();

        for (Libro libro : libros) {
            if (libro.getAutor().equalsIgnoreCase(autor)) {
                resultado.add(libro);
            }
        }

        return resultado;
    }

    public Libro buscarPorIsbn(String isbn) {
        return librosPorIsbn.get(isbn);
    }


    public boolean eliminarLibro(String isbn) {

        Libro libro = librosPorIsbn.get(isbn);

        if (libro == null) {
            return false;
        }

        libros.remove(libro);
        librosPorIsbn.remove(isbn);

        return true;
    }


    public void mostrarLibros() {

        for (Libro libro : libros) {
            System.out.println(libro);
        }
    }
}
