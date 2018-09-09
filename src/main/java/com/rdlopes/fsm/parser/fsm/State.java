package com.rdlopes.fsm.parser.fsm;

import java.util.function.Predicate;

public interface State extends Predicate<Context> {
    State process(Context context) throws StateProcessingException;
}
