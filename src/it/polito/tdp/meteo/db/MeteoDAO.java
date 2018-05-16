package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;

public class MeteoDAO {

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public List<Citta> getAllCitta(){
		String sql = "SELECT DISTINCT localita FROM situazione ORDER BY localita";
		List<Citta> result = new ArrayList<Citta>();
		
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				result.add(new Citta(rs.getString("localita")));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		
		return null;
	}

	public Double getUmiditaMedia( Month mese, Citta citta) {
		final String sql = "SELECT AVG(umidita) AS U FROM situazione WHERE localita = ? AND MONTH(data)= ?";
		
		
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, citta.getNome());
			st.setInt(2, mese.getValue()); //avessi usato il Calendar avrei dovuto mettere +1 perché vanno da 0 a 11 i mesi lì
			
			ResultSet rs = st.executeQuery();
			
			rs.next(); //posiziono cursore sulla rima riga
			Double u = rs.getDouble("U");

			conn.close();
			
			return u;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	public Double getAvgRilevamentiLocalitaMese(int mese, String localita) {

		return 0.0;
	}

}
