package wut.lang;

import wut.Wut;

/**
 * Created by josephburkey on 01/05/2016.
 */
public class Method extends WutObject {

    private Message message;
    private String[] argNames;

    public Method() {
        super(WutObject.method);
    }

    public Method(Call call) {
        this();
        message = call.message.args.get(call.message.args.size() - 1);
        int nArgs = call.message.args.size() - 1;
        argNames = new String[nArgs];
        for (int i = 0; i < nArgs; i++) argNames[i] = call.message.args.get(i).name;
    }

    public WutObject activate(Call call) throws WutException {
        return message.evalOn(makeContext(call));
    }

    public WutObject callOn(WutObject on) throws WutException, Wut {
        return activate(new Call(on));
    }

    private WutObject makeContext(Call call) throws WutException {
        WutObject context = call.receiver.clone();
        context.asKind("MethodContext").
                slot("self", call.receiver).
                slot("@", call.receiver).
                slot("call", call).
                slot("context", context);
        for (int i = 0; i < argNames.length; i++)
            context.setSlot(argNames[i], call.evalArg(i));
        return context;
    }

}