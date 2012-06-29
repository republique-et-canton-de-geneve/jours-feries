package ch.ge.cti.ct.FerieGeneve.persistance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class LectureConfig {
	private static Properties prop;
	private static Object flagsynchrone__ = new Object();
	
	private LectureConfig() {
		
	}
	
	/* (non-Javadoc)
	 * @see ch.ge.cti.ct.FerieGeneve.persistance.LecteurJourFerie#getFerie(int)
	 */
	public static String[] getFerie(int annee) {
		String[] tab = new String[0];
		if (prop == null) {
			synchronized(flagsynchrone__) {
				prop = init();
			}
		}
		
		String valeurs = prop.getProperty("JOURS_FERMETURE_ETAT_" + Integer.toString(annee));
		
		if (valeurs != null && !valeurs.equals("")) {
			tab = valeurs.split(";");
		}
		
		return tab;
	}
	
	private static Properties init() {
		String base = System.getProperty("jonas.base");
		File file = null;
		Properties propT = new Properties();
		
		if (base==null || base.equals("")) {
			base = System.getProperty("distribution.properties");
			if (base!=null)
				file = new File(base);
			else
				return propT;
		}
		else
			file = new File(base + "/Distribution.properties");
			
		
		try {
			propT.load(new FileInputStream(file));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return propT;
	}

}
