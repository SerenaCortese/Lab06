package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;




public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	private MeteoDAO meteoDao;
	private String soluzione;
	//private int giorniTotali = 0;
	private Double bestPunteggio;
	private List<Citta> soluzioneArray;
	private List<Citta> cittaPresenti;
	
	public Model() {
		meteoDao = new MeteoDAO();
		cittaPresenti = new ArrayList<Citta>();
		for(Rilevamento r : meteoDao.getAllRilevamenti()) {
			Citta c = new Citta(r.getLocalita());
			if(!cittaPresenti.contains(c))
				cittaPresenti.add(c);
		}
	}

	public String getUmiditaMedia(int mese) {
		String s = "";
		for(Citta c : cittaPresenti) {
			c.setAvgUmidita(meteoDao.getAvgRilevamentiLocalitaMese(mese, c.getNome()));
			s += c.getNome()+" "+c.getAvgUmidita()+"\n";
		}
		return s;
	}
	
	public String trovaSequenza(int mese) {
		//inizializzo qui soluzione perché ogni volta che richiamo questa funzione voglio dimenticare la sol precedente
		soluzione = "";
		bestPunteggio = 1000000000000000000000000.9;
		soluzioneArray = new ArrayList<Citta>();
		
		for(Citta c: cittaPresenti) {
			c.setRilevamenti(meteoDao.getAllRilevamentiLocalitaMese(mese, c.getNome()));
		}
		
		//richiamo funzione ricorsiva
		int step = 0;
		List<Citta> parziale = new ArrayList<Citta>();
		recursive(step,parziale,mese);
		
		for(Citta c : soluzioneArray) {
			soluzione += c.toString()+ "\n";
		}
		return soluzione;
	}

	private void recursive(int step, List<Citta> parziale, int mese) {
		//Debug
		System.out.println(parziale);
		
		//condizione di terminazione
//		if(step > Model.NUMERO_GIORNI_TOTALI) {
//			return;
//		}
//		
		//condizione di terminazione
		if(this.controllaParziale(parziale) == false) {
			return;
		}
		//se parziale ha stesso livello devo vedere se è migliore della precedente
		if(step >= Model.NUMERO_GIORNI_TOTALI) {
			if(this.punteggioSoluzione(parziale, step)>= bestPunteggio) {
				return;
			}
			soluzioneArray = new ArrayList<Citta>(parziale);
			bestPunteggio = punteggioSoluzione(parziale,step);
			return;
//			
//			boolean visitate = true;
//			for(Citta c : parziale) {//controllo che passi in tutte le città almeno un giorno
//				if(c.getCounter()==0) {
//					visitate = false;
//				}
//			}
//			if(punteggioSoluzione(parziale,step)< bestPunteggio && visitate == true && this.controllaParziale(parziale)) {
//				//salvo soluzione parziale creando una deep copy
//				soluzioneArray = new ArrayList<Citta>(parziale);
//				bestPunteggio = punteggioSoluzione(parziale,step);
//				return;
//				}
			}
		//Generazione di una nuova soluzione parziale
		for(Citta c : cittaPresenti) {
			if(diversaCitta(c,parziale)) {
				for(int i=0; i< NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN;i++) {
					parziale.add(c);
					c.increaseCounter();
				}
				this.recursive(step+3,parziale, mese);
				for(int i=0; i< NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN;i++) {
					parziale.remove(parziale.size()-1);
					c.decreaseCounter();
				}
			}
			else {
				parziale.add(c);
				c.increaseCounter();
				this.recursive(step+1,parziale, mese);
				parziale.remove(c);
				c.decreaseCounter();
			}
		}
		
//		for(Citta c : cittaPresenti) {
//			parziale.add(c);
//			c.increaseCounter();
//			if(this.controllaParziale(parziale)) {
//				recursive(step+1,parziale,mese);
//			}
//			parziale.remove(c);
//			c.decreaseCounter();
//			
//		}
	}

	private boolean diversaCitta(Citta c, List<Citta> parziale) {
		if(parziale.size() == 0) {
			return true;
		}
		return !c.getNome().equals(parziale.get(parziale.size()-1).getNome());
	}

	private Double punteggioSoluzione(List<Citta> soluzioneCandidata, int step) {
		
		double score = 0.0;
		for(Citta c : soluzioneCandidata) {
			score += c.getCosto(step);
			if(this.diversaCitta(c, soluzioneCandidata)) {
				score += Model.COST;
			}
		}
		
		return score;
	}

	private boolean controllaParziale(List<Citta> parziale) {
		
		//controllo non stia più di 6 giorni in ogni città
		for(Citta c : parziale) {
			if(c.getCounter()> Model.NUMERO_GIORNI_CITTA_MAX) {
				return false;
			}
		}
		
		//controllo che sosti almeno 3 giorni in ogni citta
		for(int i = 0; parziale.size()>=3 && i<parziale.size()-2;i++) {
			if(!parziale.get(i).getNome().equals(parziale.get(i+1).getNome()) 
				|| !parziale.get(i).getNome().equals(parziale.get(i+2).getNome())) {
				return false;
			}
		}

		return true;
	}

}
