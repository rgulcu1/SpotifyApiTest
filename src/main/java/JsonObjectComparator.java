import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.math.BigInteger;
import java.util.Comparator;

public class JsonObjectComparator implements Comparator<JsonObject> {

    private String[] path;

    private Constant.ValueType valueType;

    public JsonObjectComparator(String path, Constant.ValueType valuetype) {
        this.path = path.split("\\.");
        this.valueType = valuetype;
    }

    private JsonElement getComparedValue(JsonObject o1){

        int i;
        for ( i= 0; i < path.length-1; i++) {
           o1 = o1.getAsJsonObject(path[i]);
        }
        return o1.get(path[i]);
    }

    @Override
    public int compare(final JsonObject o1, final JsonObject o2) {

        JsonElement o1ComparedValue = getComparedValue(o1);
        JsonElement o2ComparedValue = getComparedValue(o2);

        switch (valueType) {
            case STRING:
                final String firstString = o1ComparedValue.getAsString();
                final String secondString = o2ComparedValue.getAsString();

                return firstString.compareTo(secondString);
            case INTEGER:
                final int firstInteger = o1ComparedValue.getAsInt();
                final int secondInteger = o2ComparedValue.getAsInt();

                if(firstInteger > secondInteger) return 1;
                else if (secondInteger > firstInteger) return -1;
                else return 0;

            default: return 1;
        }
    }
}
