package ch.ge.cti.ct.joursferies.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.ge.cti.ct.joursferies.exception.JoursFeriesException;

/**
 * Classe statique de validation de dates.
 *
 * @author COURTOISJU
 */
public final class DateValidator {

    private DateValidator() {
    }

    /**
     * Vérification de la validité de la date au format Date.
     */
    public static boolean checkValidite(Date date) {
        if (date == null) {
            throw new JoursFeriesException("La date en paramètre est non valide.", new RuntimeException());
        }
        return true;
    }

    /**
     * Vérification de la validité de la date au format DTD.
     */
    public static boolean checkValidite(String date, String dateFromat) {
        if (date == null){
            throw new JoursFeriesException("La date est nulle", new RuntimeException());
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);

        try {
            sdf.parse(date);
        } catch (ParseException e) {
            throw new JoursFeriesException("La date [" + date + "] est invalide", new RuntimeException());
        }
        return true;
    }

    /**
     * Vérification de la validité de l'année.
     */
    public static boolean checkAnneeValidite(int annee) {
        if (annee < 1950 || annee > 2500) {
            throw new JoursFeriesException("L'année [" + annee + "] est invalide", new RuntimeException());
        }

        return true;
    }

}
