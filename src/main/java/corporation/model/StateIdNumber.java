package corporation.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import corporation.dao.file.FileDao;

public class StateIdNumber implements java.io.Serializable {
	
	static Logger log = LoggerFactory.getLogger(StateIdNumber.class);
	
	private String number; 
	
	public StateIdNumber(String number) {
		// Kolla h�r att den �r 12 siffor
		// om inte s� f�r vi hitta p� �rhundrade siffor
		// utg�ende fr�n nuvarande �rtal eller s� skriver vi bara
		// m�ste vara 12 siffror med eller utan streck. 
		this.number = number; 
		// fixa en koll att number har r�tt format.
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
		// siffersumman f�r alla heltal mellan 0 och 18
		int [] siffersumma = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

		number = number.replace("-", "");
				
		if (number.length() != 10)
			throw new IllegalArgumentException();
		
		int [] pnr = new int[10];
		
		for (int index = 0; index < 10; index++) {
			pnr[index] = Integer.parseInt(number.substring(index, index+1));
		}
		
		int kSiffra = pnr[9];				// sista siffran �r kontrollsiffra
		

		int etttv� = 2;								// 'vikten' �r 1 eller 2, b�rja med 2
		int summa = 0;								// v�r ber�knade kontrollsiffra

		for (int i = 0; i < 9; i++)
		{
			int siffra = pnr[i];			// plocka ut n�rsta siffra fr�n h�ger
			siffra = siffra * etttv�;				// * 1 eller * 2
			etttv� = 3 - etttv�;			// 2 blir ett och vice versa
			summa = summa + siffersumma[siffra]; 	// siffra �r max 18 (2 * 9)
		}

		int berkSiffra =  (10 - (summa % 10)) % 10;		// kontrollsiffran �r summan:s n�rmast h�gre tiotal minus summan
		return kSiffra == berkSiffra;					// ok om de �r lika
	}

}
