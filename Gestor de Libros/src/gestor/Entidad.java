package gestor;

public class Entidad {
	
	private int id;
	private String nombre;
	
	public Entidad(int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public int getId() {
		return id;//OBtiene id
	}

	public void setId(int id) {
		this.id = id;//Escribe id
	}

	public String getNombre() {
		return nombre;//OBtiene nombre
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;//Escribe nombre
	}
}
