package it.polito.tdp.meteo.db;

import java.util.List;

import it.polito.tdp.meteo.bean.Rilevamento;

public class TestMeteoDAO {

	public static void main(String[] args) {

		MeteoDAO dao = new MeteoDAO();

		List<Rilevamento> list = dao.getAllRilevamenti();

		// 
		System.out.println("STAMPA: localita, giorno, mese, anno, umidita in percentuali:");
		for (Rilevamento r : list) {
			System.out.format("%-10s %2td/%2$2tm/%2$4tY %3d%%\n", r.getLocalita(), r.getData(), r.getUmidita());
		}
		
		System.out.println("\nLivelli di umidità a Genova nel mese di Gennaio: (%)");
		System.out.println(dao.getAllRilevamentiLocalitaMese(1, "Genova"));
		System.out.println("\nUmidità media rilevata nel mese di Marzo a Genova: (%)");
//		System.out.println(dao.getAvgRilevamentiLocalitaMese(3, "Genova"));
		
		System.out.println("\nLivelli di umidità a Milano nel mese di Maggio: (%)");
		System.out.println(dao.getAllRilevamentiLocalitaMese(5, "Milano"));
		System.out.println("\nUmidità media rilevata nel mese di Maggio a Milano: (%)");
//		System.out.println(dao.getAvgRilevamentiLocalitaMese(5, "Milano"));
		
		System.out.println("\nLivelli di umidità a Torino nel mese di Maggio: (%)");
		System.out.println(dao.getAllRilevamentiLocalitaMese(5, "Torino"));
		System.out.println("\nUmidità media rilevata nel mese di Maggio a Torino: (%)");
//		System.out.println(dao.getAvgRilevamentiLocalitaMese(5, "Torino"));
		
	}

}
