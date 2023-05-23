package linda.server;

import java.io.Serializable;

import linda.Callback;
import linda.Tuple;

public class CallbackRemote implements Callback, Serializable {

    private Callback originalCallback;

    public CallbackRemote(Callback originalCallback) {
        this.originalCallback = originalCallback;
    }
    @Override
    public void call(Tuple tuple) {
        originalCallback.call(tuple);
    }
}
