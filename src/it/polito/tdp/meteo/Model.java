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
	
	private MeteoDAO meteoDao = null;
	private Double bestPunteggio;
	private List<SimpleCity> soluzione = null;
	private List<Citta> cittaPresenti = null;
	
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
		StringBuilder s = new StringBuilder();
		for(Citta c : cittaPresenti) {
			c.setAvgUmidita(meteoDao.getAvgRilevamentiLocalitaMese(mese, c.getNome()));
			s.append("Località: "+ c.getNome()+"\tUmidità media: "+c.getAvgUmidita()+"\n");
		}
		return s.toString();
	}
	
	public String trovaSequenza(int mese) {
		//inizializzo qui soluzione perché ogni volta che richiamo questa funzione voglio dimenticare la sol precedente
		bestPunteggio = Double.MAX_VALUE;
		soluzione = null;
		
		for(Citta c: cittaPresenti) {
			c.setCounter(0);
			c.setRilevamenti(meteoDao.getAllRilevamentiLocalitaMese(mese, c.getNome()));
		}
		
		//richiamo funzione ricorsiva
		List<SimpleCity> parziale = new ArrayList<SimpleCity>();
		recursive(0,parziale);
		
		if (soluzione != null) {
			System.out.println(String.format("Soluzione migliore per il mese di %s: \n", mese));
			System.out.println(String.format("DEBUG score: %f", this.punteggioSoluzione(soluzione)));
			return soluzione.toString();
		}

		return "Nessuna soluzione trovata";
				
	}

	private void recursive(int step, List<SimpleCity> parziale) {
		
		//condizione di terminazione
//		if(this.controllaParziale(parziale) == false) {
//			return;
//		}
		//se parziale ha stesso livello devo vedere se è migliore della precedente
		if(step >= Model.NUMERO_GIORNI_TOTALI) {
			
			if(this.punteggioSoluzione(parziale)< bestPunteggio) {
				soluzione = new ArrayList<SimpleCity>(parziale);
				bestPunteggio = this.punteggioSoluzione(parziale);
			}
			
			return;
		}
		
		//Generazione di una nuova soluzione parziale
		
		for(Citta c : cittaPresenti) {
			SimpleCity sc = new SimpleCity(c.getNome(), c.getRilevamenti().get(step).getUmidita() );
			parziale.add(sc);
			c.increaseCounter();
			if(this.controllaParziale(parziale)) {
				recursive(step+1,parziale);
			}
			parziale.remove(step);
			c.decreaseCounter();
			
		}
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {
		
		// Controllo che la lista non sia nulla o vuota
		if (soluzioneCandidata == null || soluzioneCandidata.size() == 0)					
			return Double.MAX_VALUE;
		
		// Controllo che la soluzione contenga tutte le citta'
		for (Citta c : cittaPresenti) {
			if (!soluzioneCandidata.contains(new SimpleCity(c.getNome())))
				return Double.MAX_VALUE;
		}
		
		SimpleCity previous = soluzioneCandidata.get(0);
		double score = 0.0;

		for (SimpleCity sc : soluzioneCandidata) {
			if (!previous.equals(sc)) {
				score += Model.COST;
			}
			previous = sc;
			score += sc.getCosto();
		}
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {
		
		// Se e' nulla non e' valida
		if (parziale == null)
			return false;

		// Se la soluzione parziale e' vuota, e' valida
		if (parziale.size() == 0)
			return true;
		
		//controllo non stia più di 6 giorni in ogni città
		for(Citta c : cittaPresenti) {
			if(c.getCounter()> Model.NUMERO_GIORNI_CITTA_MAX) {
				return false;
			}
		}
		
		//controllo che sosti almeno 3 giorni in ogni citta

		SimpleCity previous = parziale.get(0);
		int counter = 0;

		for (SimpleCity sc : parziale) {
			if (!previous.equals(sc)) {
				if (counter < NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {
					return false;
				}
				counter = 1;
				previous = sc;
			} else {
				counter++;
			}
		}

		return true;
	}

}
