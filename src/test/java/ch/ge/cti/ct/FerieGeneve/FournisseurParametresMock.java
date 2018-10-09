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
package ch.ge.cti.ct.FerieGeneve;

import ch.ge.cti.ct.FerieGeneve.persistance.FournisseurParametres;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FournisseurParametresMock implements FournisseurParametres {

    private Logger LOG = LoggerFactory.getLogger(FournisseurParametresMock.class);

    public String[] getJoursFermes(int annee) {
        LOG.info("lecture mock test FerieGenev");
        String[] dates = new String[] { "01/01", "02/02", "03/03" };
        return dates;
    }

}
