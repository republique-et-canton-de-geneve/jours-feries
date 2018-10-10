package ch.ge.cti.ct.joursferies.exception;

/**
 * Exception levée en cas d'erreur technique rencontrée par un service.
 */
public class JoursFeriesException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JoursFeriesException(Throwable t) {
        super(t);
    }

    public JoursFeriesException(String message, Throwable t) {
        super(message, t);
    }

}
