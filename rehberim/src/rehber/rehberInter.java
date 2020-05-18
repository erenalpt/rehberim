package rehber;

public interface rehberInter {
		
		// print user list
		public void printUserList();
		
		//Inserts the user
		public void ekleKisi( Kisi user );
		
		//Fetch user for specific user id
		public Kisi araKisi(Kisi isim );
		
		public Kisi araKisiSoyisim( Kisi soyisim);
		// modify user
		public void duzeltIsim( Kisi isim);
		
		public void duzeltSoyisim( Kisi soyisim);
		
		public void duzeltTelefon( Kisi telefon);
		// delete user for specific user id
		public void deleteUser( int userId );

	}