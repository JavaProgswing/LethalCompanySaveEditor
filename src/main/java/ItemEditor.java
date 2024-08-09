import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ItemEditor {
    private static String updateJson(String jsonString, int itemId, int itemPrice, float x, float y, float z) {
        // Parse the JSON string into a JsonObject
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        if (!jsonObject.has("shipGrabbableItemIDs")) {
            JsonObject shipGrabbableItemIDs = new JsonObject();
            shipGrabbableItemIDs.addProperty("__type", "System.Int32[],mscorlib");
            shipGrabbableItemIDs.add("value", new JsonArray());
            jsonObject.add("shipGrabbableItemIDs", shipGrabbableItemIDs);
        }
        JsonArray itemIDs = jsonObject.getAsJsonObject("shipGrabbableItemIDs").getAsJsonArray("value");
        itemIDs.add(itemId);

        // Ensure "shipScrapValues" exists
        if (!jsonObject.has("shipScrapValues")) {
            JsonObject shipScrapValues = new JsonObject();
            shipScrapValues.addProperty("__type", "System.Int32[],mscorlib");
            shipScrapValues.add("value", new JsonArray());
            jsonObject.add("shipScrapValues", shipScrapValues);
        }
        JsonArray scrapValues = jsonObject.getAsJsonObject("shipScrapValues").getAsJsonArray("value");
        scrapValues.add(itemPrice);

        // Ensure "shipGrabbableItemPos" exists
        if (!jsonObject.has("shipGrabbableItemPos")) {
            JsonObject shipGrabbableItemPos = new JsonObject();
            shipGrabbableItemPos.addProperty("__type", "UnityEngine.Vector3[],UnityEngine.CoreModule");
            shipGrabbableItemPos.add("value", new JsonArray());
            jsonObject.add("shipGrabbableItemPos", shipGrabbableItemPos);
        }
        JsonArray itemPos = jsonObject.getAsJsonObject("shipGrabbableItemPos").getAsJsonArray("value");
        JsonObject newCoord = new JsonObject();
        newCoord.addProperty("x", x);
        newCoord.addProperty("y", y);
        newCoord.addProperty("z", z);
        itemPos.add(newCoord);

        // Convert the updated JsonObject back to a string
        return jsonObject.toString();
    }

    public static void editGameSave(int itemId, int itemPrice, float x, float y, float z) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        final String saveJson = AesCryptor.fetchLethalGameSave();
        final String newSaveJson = updateJson(saveJson, itemId, itemPrice, x, y, z);
        AesCryptor.saveLethalGameSave(newSaveJson.getBytes(StandardCharsets.UTF_8));
    }
}
