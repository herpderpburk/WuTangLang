package wut.lang;

/**
 * Created by josephburkey on 02/05/2016.
 */
import java.util.ArrayList;

public class Call extends WutObject {

    final ArrayList<Message> args;
    WutObject receiver;
    final WutObject base;
    final Message message;

    public Call(Message message, WutObject receiver, WutObject base, ArrayList<Message> args) {
        super(WutObject.call);
        this.message = message;
        this.receiver = receiver;
        this.base = base;
        this.args = args;
    }

    public Call(WutObject receiver) {
        this(null, receiver, receiver, new ArrayList<Message>());
    }

    public WutObject evalArg(int at) throws WutException {
        if (at >= args.size()) return WutObject.nil;
        return args.get(at).evalOn(base);
    }

    public String toString() {
        return "<" + kind() + "#" + getObjectID() +
                " message:" + message.toString() +
                " receiver:" + receiver.toString() +
                " base:" + base.toString() +
                " args:" + args.toString() +
                ">";
    }

}
