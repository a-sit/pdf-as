package at.gv.egiz.pdfas.lib.util;

import at.gv.egiz.pdfas.common.exceptions.PDFASError;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import lombok.val;

public class TimedFunction {
    public interface ThrowingCallable<S, T extends Throwable> {
        S invoke() throws T;
    }

    private final String timerName;
    private final Timer successTimer;
    public TimedFunction(String timerName) {
        this.timerName = timerName;
        this.successTimer = Metrics.timer(timerName, "status", "success");
    }

    public <S,T extends Throwable> S timed(ThrowingCallable<S, T> fn) throws T {
        val timer = start();
        try {
            S result = fn.invoke();
            timer.finishSuccess();
            return result;
        } catch (final Throwable ex) {
            timer.finishFailure(ex);
            throw ex;
        }
    }

    public class Context {
      final Timer.Sample timer = Timer.start();
      public void finishSuccess() { timer.stop(successTimer); }
      public void finishFailure(Throwable ex) {
        if (ex instanceof PDFASError e) {
          Metrics.timer(timerName, "status", "failure", "exception", e.getClass().getName(), "errorCode", Long.toString(e.getCode()));
        } else {
          Metrics.timer(timerName, "status", "failure", "exception", ex.getClass().getName());
        }
      }
    }
    public Context start() { return new Context(); }
}
