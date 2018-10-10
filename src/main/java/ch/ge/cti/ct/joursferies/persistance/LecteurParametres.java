/*
 * Jours fériés
 *
 * Copyright (C) 2012-2018 République et Canton de Genève
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.ge.cti.ct.joursferies.persistance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cette classe permet la lecture du fichier jours-feries.properties afin d'y trouver les dates des jours fermés
 * à l'État.
 *
 * @author pinaudj
 */
public final class LecteurParametres implements FournisseurParametres {

    private Properties prop;

    private static final Logger LOG = LoggerFactory.getLogger(LecteurParametres.class);

    /**
     * Constructeur par défaut.
     * Trouve et lit le fichier jours-feries.properties
     */
    public LecteurParametres() {
        File file;
        prop = new Properties();

        String filePath = System.getProperty("jours-feries.config.file");
        if (filePath != null) {
            file = new File(filePath);
        } else {
            LOG.error("Pas de fichier des jours fermés fourni");
            return;
        }

        // remplissage du Properties
        try (InputStream is = new FileInputStream(file)) {
            prop.load(is);
        } catch (FileNotFoundException e) {
            LOG.error("Fichier [{}] pas trouvé", file, e);
        } catch (IOException e) {
            LOG.error("Erreur lors de la lecture du fichier [{}]", file, e);
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
