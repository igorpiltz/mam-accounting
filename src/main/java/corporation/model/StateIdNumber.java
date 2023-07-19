package corporation.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import corporation.dao.file.FileDao;

public class StateIdNumber implements java.io.Serializable {
	
	static Logger log = LoggerFactory.getLogger(StateIdNumber.class);
	
	private String number; 
	
	public StateIdNumber(String number) {
		// Kolla här att den är 12 siffor
		// om inte så får vi hitta på århundrade siffor
		// utgående från nuvarande årtal eller så skriver vi bara
		// måste vara 12 siffror med eller utan streck. 
		this.number = number; 
		// fixa en koll att number har rätt format.
		if (!number.matches("\\d{6}-\\d{4}"))
			throw new IllegalArgumentException("Wrong format");
		
		 
		if (!kollaKontrollsiffra(number))
			throw new IllegalArgumentException("Wrong controldigit");
	}

	public String getLongVersion() {
		return number;
	}
	
	public String toString() {
		return number;
	}
	
	public static boolean kollaKontrollsiffra(String number)
	{
		// siffersumman för alla heltal mellan 0 och 18
		int [] siffersumma = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

		number = number.replace("-", "");
				
		if (number.length() != 10)
			throw new IllegalArgumentException();
		
		int [] pnr = new int[10];
		
		for (int index = 0; index < 10; index++) {
			pnr[index] = Integer.parseInt(number.substring(index, index+1));
		}
		
		int kSiffra = pnr[9];				// sista siffran är kontrollsiffra
		

		int etttvå = 2;								// 'vikten' är 1 eller 2, börja med 2
		int summa = 0;								// vår beräknade kontrollsiffra

		for (int i = 0; i < 9; i++)
		{
			int siffra = pnr[i];			// plocka ut närsta siffra från höger
			siffra = siffra * etttvå;				// * 1 eller * 2
			etttvå = 3 - etttvå;			// 2 blir ett och vice versa
			summa = summa + siffersumma[siffra]; 	// siffra är max 18 (2 * 9)
		}

		int berkSiffra =  (10 - (summa % 10)) % 10;		// kontrollsiffran är summan:s närmast högre tiotal minus summan
		return kSiffra == berkSiffra;					// ok om de är lika
	}

}
