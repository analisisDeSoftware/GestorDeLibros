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
        return this == libro || libro instanceof Libro && this.ISBN.equals(((Libro)libro).ISBN);//Compara si son el mismo libro
    }

    public int compareTo(Libro libro) {
        return this.ISBN.compareTo(libro.ISBN);//Compara ISBN
    }

    public String toString() {//Parsea el libro a String
        return "ISBN               : " + this.ISBN + "\n" + "titulo             : " + this.titulo + "\n" + "autor              : " + this.autor + "\n" + "editorial          : " + this.editorial + "\n" + "edicion            : " + this.edicion + "\n" + "anno de publicacion: " + this.anioPublicacion + "\n";
    }

    public String getISBN() {
        return this.ISBN;//Obtiene ISBN
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;//Escribe ISBN
    }

    public String getTitulo() {
        return this.titulo;//Obtiene titulo
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;//Escribe titulo
    }

    public String getAutor() {
        return this.autor;//Obtiene autor
    }

    public void setAutor(String autor) {
        this.autor = autor;//Escribe autor
    }

    public String getEditorial() {
        return this.editorial;//Obtiene editorial
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;//Escribe editorial
    }

    public int getEdicion() {
        return this.edicion;//Obtiene edicion
    }

    public void setEdicion(int edicion) {
        this.edicion = edicion;//Escribe edicion
    }

    public int getAnioPublicacion() {
        return this.anioPublicacion;//Obtiene anio de publicacion
    }

    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;//Escribe anio de publicacion
    }
}
