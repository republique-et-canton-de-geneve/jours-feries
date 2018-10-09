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
package ch.ge.cti.ct.FerieGeneve.persistance;

/**
 * <p>
 * Cette interface est utilisée pour lire les données paramétrables.
 * </p>
 * @author pinaudj
 */
public interface FournisseurParametres {

    /**
     * Retourne la liste des jours fermés à l´État, pour une année donnée.
     * @return la liste des jours fermés au format dd/MM
     */
    public String[] getJoursFermes(int annee);
}
