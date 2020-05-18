package rehber;

public class Kisi {
	private int id;
	private String isim;
	private String soyisim;
	private String telefon;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getisim() {
		return isim;
	}

	public void setIsim(String isim) {
		this.isim = isim;
	}

	public String getsoyisim() {
		return soyisim;
	}

	public void setsoyisim(String soyisim) {
		this.soyisim = soyisim;
	}
	
	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String toString() {
		return id + " " + isim + " " + soyisim + " " + telefon;
	}

	public Object [] getObjects(){
        
        Object [] listveriler = {id,isim,soyisim,telefon};
       
        return listveriler;
  }
	
}