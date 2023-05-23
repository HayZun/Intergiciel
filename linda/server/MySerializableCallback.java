package linda.server;

import linda.Callback;
import linda.Tuple;

import java.io.Serializable;

public class MySerializableCallback implements Callback, Serializable {

    Callback originalCallback;

    private static final long serialVersionUID = 1L;

    public MySerializableCallback(Callback originalCallback) {
        this.originalCallback = originalCallback;
    }

    @Override
    public void call(Tuple tuple) {
        // TODO Auto-generated method stub
        System.out.println("CB got " + tuple);
    }
}
