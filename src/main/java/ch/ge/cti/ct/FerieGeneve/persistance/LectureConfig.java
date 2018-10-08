package ch.ge.cti.ct.FerieGeneve.persistance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Cette classe permet la lecture du Distribution.properties afin d'y trouver les d�tes des jours �tats ferm�s
 *
 * @author pinaudj
 */
public final class LectureConfig implements ParamFermesAble {

    private Properties prop;

    private static final Logger LOG = Logger.getLogger(LectureConfig.class);

    /**
     * Constructeur par d�faut Trouve et lit le Distribution.properties
     */
    public LectureConfig() {
        String base = System.getProperty("jonas.base");
        File file = null;
        prop = new Properties();

        // si pas de jonas.base dans les propri�t�s syst�me on cherche distribution.properties
        if (base == null || base.equals("")) {
            base = System.getProperty("distribution.properties");
            if (base != null) {
                file = new File(base);
            } else {
                return;
            }
        }
        // sinon on charge le fichier
        else {
            file = new File(base + "/Distribution.properties");
        }

        // remplissage du Properties
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            prop.load(is);
        } catch (FileNotFoundException e) {
            LOG.error("fichier Distribution.properties non trouv�", e);
        } catch (IOException e) {
            LOG.error("erreur lecture fichier Distribution.properties", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                LOG.error("erreur fermeture fichier Distribution.properties", e);
            }
        }
    }

    /**
     * Retourne la liste des jours �tats ferm�s pour une ann�e donn�es
     *
     * @param annee
     *
     * @return
     */
    public String[] getJoursFermes(int annee) {
        String[] tab = new String[0];

        // obtention des valeurs pour l'ann�e donn�e
        String valeurs = prop.getProperty("JOURS_FERMETURE_ETAT_" + Integer.toString(annee));

        //parsing
        if (valeurs != null && !valeurs.equals("")) {
            tab = valeurs.split(";");
        }

        return tab;
    }
}
