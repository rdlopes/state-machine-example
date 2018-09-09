package com.rdlopes.fsm.parser.fsm;

public interface State {
    boolean canProcess(Context context);

    State process(Context context) throws StateProcessingException;
}
