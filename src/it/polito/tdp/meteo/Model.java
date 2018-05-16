package it.polito.tdp.meteo;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.CittaUmidita;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;


public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	private List<Citta> leCitta;
	private List<Citta> best;
	
	public Model() {
		MeteoDAO dao = new MeteoDAO();
		this.leCitta = dao.getAllCitta();
	}

	
	public List<Citta> getLeCitta() {
		return leCitta;
	}


	public Double getUmiditaMedia(Month mese, Citta citta) {
		MeteoDAO dao = new MeteoDAO();
		return dao.getUmiditaMedia(mese, citta);
	}
	
	public List<CittaUmidita> getUmiditaMedia(int mese){
		return null;
	}
	/**
	 * Funzione che chiama ricorsione, "trovaSoluzione" nel mio branch
	 * @param mese
	 * @return
	 */
	public List<Citta> calcolaSequenza(Month mese){
		List<Citta> parziale = new ArrayList<>();
		//non LInkedList perché devo raggiungere il terzo elemento, il quarto ecc
		this.best=null;
		//carico dentro ciascuna delle città la lista dei rilevamenti nel mese considerato
		//e le memorizzo con citta.setRilevamenti(dao.getRilevalementiCittaMese(citta,mese))
		
		cerca(parziale,0);
		return best;
	}
	
	/**
	 * é la funzione ricorsiva, "recursive" nel mio branch
	 * @param parziale
	 * @param livello
	 */
	public void cerca(List<Citta> parziale, int livello) {
		
		if(livello == NUMERO_GIORNI_TOTALI) {
			//condizione terminale
			Double costo = calcolaCosto(parziale);
			if(best==null || costo < calcolaCosto(best)) {
				best = new ArrayList<>(parziale);
			}
			
			System.out.println(parziale);
			
		}else {
			
			//caso intermedio trovo nuova soluzione
			for(Citta prova: leCitta) {
				
				if(aggiuntaValida(prova,parziale)) {
					
					parziale.add(prova);
					cerca(parziale, livello+1);
					parziale.remove(parziale.size()-1);
					
				}
			}
		}
	}
	/**
	 * funzione punteggioSoluzione nel mio branch
	 * @param parziale
	 * @return
	 */
	private Double calcolaCosto(List<Citta> parziale) {
		//sommatoria di tutte le umidità in ciascuna città considerando il rilevamento del giorno giusto
		//SOMMA parziale.get(giorno-1).getRilevamenti(giorno-1)
		
		//a cui sommo 100* numero di volte in cui cambio città
		
		return null;
	}

	/**
	 * funzione controllaParziale nel mio branch
	 * @param prova
	 * @param parziale
	 * @return
	 */
	private boolean aggiuntaValida(Citta prova, List<Citta> parziale) {
		
		//verifica giorni massimi
		int conta = 0;
		for(Citta precedente: parziale)  //guardo quante volte compare la città che sto per aggiungere
			if(precedente.equals(prova))
				conta++;
		if(conta>=NUMERO_GIORNI_CITTA_MAX)
			return false;
		
		
		//verifica giorni minimi: 
		
		if(parziale.size()==0) //primo giorno
			return true;
		if(parziale.size()==1 || parziale.size()==2) { //secondo o terzo giorno non posso cambiare
			//la prova deve essere uguale all'ultima città vista
			return parziale.get(parziale.size()-1).equals(prova);
		}
		if(parziale.get(parziale.size()-1).equals(prova))//giorni successivi, posso SEMPRE rimanere
			return true;
		
		//voglio cambiare citta: posso farlo solo se in quella precedente ci son stato almeno 3 giorni
		if(parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) &&
				parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3)))
			return true;
		
		return false;
	}

	
}
