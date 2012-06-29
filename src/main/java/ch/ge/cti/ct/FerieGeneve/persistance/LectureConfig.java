package ch.ge.cti.ct.FerieGeneve.persistance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public final class LectureConfig {
	private static Properties prop;
	private static Logger LOG = Logger.getLogger(LectureConfig.class);

	static {
		prop = init();
	}

	private LectureConfig() {

	}

	/* (non-Javadoc)
	 * @see ch.ge.cti.ct.FerieGeneve.persistance.LecteurJourFerie#getFerie(int)
	 */
	public static final String[] getFerie(int annee) {
		String[] tab = new String[0];

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
			if (base!=null) {
				file = new File(base);
			}
			else {
				return propT;
			}
		}
		else {
			file = new File(base + "/Distribution.properties");
		}
		
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			propT.load(is);
		}
		catch (FileNotFoundException e) {
			LOG.error("fichier Distribution.properties non trouvé", e);
		}
		catch (IOException e) {
			LOG.error("erreur lecture fichier Distribution.properties", e);
		}
		finally {
			try {
				is.close();
			}
			catch (IOException e) {
				LOG.error("erreur fermeture fichier Distribution.properties", e);
			}
		}

		return propT;
	}

}
