package ch.ge.cti.ct.FerieGeneve.persistance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class LectureConfig {
	Properties prop;
	
	public LectureConfig() {
		String base = System.getProperty("jonas.base");
		File file = null;
		prop = new Properties();
		
		if (base==null || base.equals("")) {
			base = System.getProperty("distribution.properties");
			if (base!=null)
				file = new File(base);
			else
				return;
		}
		else
			file = new File(base + "/Distribution.properties");
			
		
		try {
			prop.load(new FileInputStream(file));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String[] getFerie(int annee) {
		String[] tab = new String[0];
		
		String valeurs = prop.getProperty("JOURS_FERMETURE_ETAT_" + Integer.toString(annee));
		
		if (valeurs != null && !valeurs.equals("")) {
			tab = valeurs.split(";");
		}
		
		return tab;
	}
}
