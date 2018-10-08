package ch.ge.cti.ct.FerieGeneve.persistance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Cette classe permet la lecture du Distribution.properties afin d'y trouver les dates des jours fermés
 * à l'État.
 *
 * @author pinaudj
 */
public final class LectureConfig implements ParamFermesAble {

    private Properties prop;

    private static final Logger LOG = Logger.getLogger(LectureConfig.class);

    /**
     * Constructeur par défaut.
     * Trouve et lit le Distribution.properties
     */
    public LectureConfig() {
        String base = System.getProperty("jonas.base");
        File file = null;
        prop = new Properties();

        // si pas de jonas.base dans les propriétés système on cherche distribution.properties
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
        try (InputStream is = new FileInputStream(file)) {
            prop.load(is);
        } catch (FileNotFoundException e) {
            LOG.error("fichier Distribution.properties non trouvé", e);
        } catch (IOException e) {
            LOG.error("erreur lecture fichier Distribution.properties", e);
        }
    }

    /**
     * Retourne la liste des jours fermés à l'État, pour une année donnée.
     */
    public String[] getJoursFermes(int annee) {
        String[] tab = new String[0];

        // obtention des valeurs pour l'année donnée
        String valeurs = prop.getProperty("JOURS_FERMETURE_ETAT_" + Integer.toString(annee));

        //parsing
        if (valeurs != null && !valeurs.equals("")) {
            tab = valeurs.split(";");
        }

        return tab;
    }

}
