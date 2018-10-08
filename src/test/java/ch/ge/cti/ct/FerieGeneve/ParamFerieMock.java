package ch.ge.cti.ct.FerieGeneve;

import org.apache.log4j.Logger;

import ch.ge.cti.ct.FerieGeneve.persistance.ParamFermesAble;

public class ParamFerieMock implements ParamFermesAble {

    private Logger LOG = Logger.getLogger(ParamFerieMock.class);

    public String[] getJoursFermes(int annee) {
        LOG.info("lecture mock test FerieGenev");
        String[] dates = new String[] { "01/01", "02/02", "03/03" };
        System.out.println("lecture mock test FerieGenev");
        return dates;
    }

}
