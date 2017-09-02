package gestor;

class Libro implements Comparable<Libro> {
    private String ISBN;
    private String titulo;
    private String autor;
    private String editorial;
    private int edicion;
    private int anioPublicacion;
    
    public Libro(String ISBN, String titulo, String autor, String editorial, int edicion, int anioPublicacion) {
		this.ISBN = ISBN;
		this.titulo = titulo;
		this.autor = autor;
		this.editorial = editorial;
		this.edicion = edicion;
		this.anioPublicacion = anioPublicacion;
	}

	public boolean equals(Object libro) {
        return this == libro || libro instanceof Libro && this.ISBN.equals(((Libro)libro).ISBN);
    }

    public int compareTo(Libro libro) {
        return this.ISBN.compareTo(libro.ISBN);
    }

    public String toString() {
        return "ISBN               : " + this.ISBN + "\n" + "titulo             : " + this.titulo + "\n" + "autor              : " + this.autor + "\n" + "editorial          : " + this.editorial + "\n" + "edicion            : " + this.edicion + "\n" + "anno de publicacion: " + this.anioPublicacion + "\n";
    }

    public String getISBN() {
        return this.ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return this.autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return this.editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getEdicion() {
        return this.edicion;
    }

    public void setEdicion(int edicion) {
        this.edicion = edicion;
    }

    public int getAnioPublicacion() {
        return this.anioPublicacion;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }
}
