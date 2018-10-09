package ch.ge.cti.ct.FerieGeneve;

import ch.ge.cti.ct.FerieGeneve.persistance.ParamFermesAble;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParamFerieMock implements ParamFermesAble {

    private Logger LOG = LoggerFactory.getLogger(ParamFerieMock.class);

    public String[] getJoursFermes(int annee) {
        LOG.info("lecture mock test FerieGenev");
        String[] dates = new String[] { "01/01", "02/02", "03/03" };
        return dates;
    }

}
