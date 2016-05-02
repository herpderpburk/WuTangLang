package wut.lang;

import wut.Wut;

import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Created by josephburkey on 01/05/2016.
 */
public class WutObject {
    static public WutObject base;
    static public WutObject lobby;
    static public WutObject object;
    static public WutObject call;
    static public WutObject message;
    static public WutObject method;
    static public WutObject string;
    static public WutObject number;
    static public WutObject array;
    static public WutObject nil;
    static public WutObject _true;
    static public WutObject _false;

    final ArrayList<WutObject> protos;
    private final HashMap<String, WutObject> slots;
    Object data;

    public WutObject(WutObject proto, Object data) {
        protos = new ArrayList<WutObject>();
        if (proto != null) protos.add(proto);
        slots = new HashMap<String, WutObject>();
        this.data = data;
    }

    public WutObject(WutObject proto) {
        this(proto, null);
    }

    public WutObject() {
        this(null, null);
    }

    public void appendProto(WutObject proto) {
        protos.add(proto);
    }

    public void prependProto(WutObject proto) {
        protos.add(0, proto);
    }

    public WutObject setSlot(String name, WutObject value) {
        // If name starts with a capital letter, we're creating a kind.
        // Automatically set the slot accordingly.
        if (name.length() > 0 && Character.isUpperCase(name.charAt(0))) {
            value.asKind(name);
        }
        slots.put(name, value);
        return value;
    }

    // Set slot and return self. For nicer DSL.
    public WutObject slot(String name, WutObject value) {
        setSlot(name, value);
        return this;
    }

    public boolean hasSlot(String name) {
        if (slots.containsKey(name)) return true;
        for (WutObject proto : protos) {
            if (proto.hasSlot(name)) return true;
        }
        return false;
    }

    public WutObject getSlot(String name) throws SlotNotFound {
        if (slots.containsKey(name)) return slots.get(name);
        for (WutObject proto : protos) {
            if (proto.hasSlot(name)) return proto.getSlot(name);
        }
        if (name.equals("kind")) throw new SlotNotFound("Slot '" + name + "' not found on unknown object");
        throw new SlotNotFound("Slot '" + name + "' not found on " + kind());
    }

    public WutObject removeSlot(String name) {
        return slots.remove(name);
    }

    public String kind() {
        try {
            return getSlot("kind").getDataAsString();
        } catch (SlotNotFound e) {
            return "?";
        }
    }

    public Object getData() {
        return data;
    }

    public Integer getDataAsNumber() {
        return (Integer) ((data instanceof Integer) ? data : null);
    }

    public String getDataAsString() {
        return (String) ((data instanceof String) ? data : null);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<WutObject> getDataAsArray() {
        return (ArrayList<WutObject>) data;
    }

    public String toString() {
        return "<" + kind() + "#" + getObjectID() +
                (data == null ? "" : " data:" + data.toString()) +
                // (slots.isEmpty() ? "" : " slots:" + slots.toString()) +
                ">";
    }

    public Integer getObjectID() {
        return System.identityHashCode(this);
    }

    public WutObject activate(Call call) throws WutException {
        return this;
    }


    @SuppressWarnings("CloneDoesntCallSuperClone")
    public WutObject clone() {
        Object data = this.data;
        if (data instanceof ArrayList) {
            data = new ArrayList<WutObject>();
            // TODO copy array content?
            // Collections.copy(data, this.data);
        }
        return new WutObject(this, data);
    }

    public WutObject with(Object data) {
        this.data = data;
        return this;
    }

    public WutObject asKind(String kind) {
        setSlot("kind", newString(kind));
        return this;
    }

    public boolean isFalse() {
        return this == nil || this == _false;
    }

    public boolean isTrue() {
        return !isFalse();
    }

    public static WutObject require(String file) throws WutException, Wut {
        ArrayList<WutObject> loadPath = WutObject.lobby.getSlot("load_path").getDataAsArray();
        String code = null;
        for (WutObject path : loadPath) {
            String filePath = path.getDataAsString() + "/" + file + ".min";
            if (File.exists(filePath)) {
                file = filePath;
                code = File.read(filePath);
                break;
            }
        }
        if (code == null) throw new Wut("File not found: " + file);
        return Message.parse(code, file).evalOn(WutObject.lobby);
    }
//String
    public static WutObject newString(String str) {
        return WutObject.string.clone().with(str);
    }
//Bool
    public static WutObject newBool(Boolean value) {
        return value ? _true : _false;
    }
//Int
    public static WutObject newNumber(Integer i) {
        return WutObject.number.clone().with(i);
    }
//Array
    public static WutObject newArray(ArrayList<WutObject> items) {
        return WutObject.array.clone().with(items);
    }

    public static WutObject newArray(WutObject[] items) {
        ArrayList<WutObject> array = new ArrayList<WutObject>();
        Collections.addAll(array, items);
        return newArray(array);
    }
}
