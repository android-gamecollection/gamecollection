package todo.spielesammlungprototyp.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;

import java.util.ArrayList;
import java.util.UUID;

public class Savegame {

    private ArrayList<SavegameObject> saveGameObjectList;
    private Genson genson = new Genson();
    private String SAVE_DATA_NAME = "Test.Savegames";
    private String SAVE_DATA_KEY = "TEST";

    public Savegame(Context context) {
        SharedPreferences savegames = context.getSharedPreferences(SAVE_DATA_NAME, Context.MODE_PRIVATE);
        String saveGameListAsString = savegames.getString(SAVE_DATA_KEY, "");
        if(!saveGameListAsString.isEmpty()) {
            this.saveGameObjectList =  genson.deserialize(saveGameListAsString, new GenericType<ArrayList<SavegameObject>>(){});
        }
        else {
            this.saveGameObjectList = new ArrayList<>();
        }
    }

    /*
    public Savegame(Context context, UUID id) {
        this.uuid = id;
        SharedPreferences savegames = context.getSharedPreferences(SAVE_DATA_NAME, Context.MODE_PRIVATE);
        String saveGameListAsString = savegames.getString("LIST", "");
        if(!saveGameListAsString.isEmpty()) {
            this.saveGameObjectList =  genson.deserialize(saveGameListAsString, new GenericType<ArrayList<SavegameObject>>(){});
        }
        else {
            this.saveGameObjectList = new ArrayList<>();
        }
    }
    */

    public void addSavegame(Context context, String savegame) {
        String activity = context.getClass().getSimpleName();
        SharedPreferences savegames = context.getSharedPreferences(SAVE_DATA_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = savegames.edit();
        saveGameObjectList.add(new SavegameObject(savegame, activity));
        putStringToEditor(editor);
    }

    public boolean updateSavegame(Context context, SavegameObject savegameObject) {
        return modifySavegame(context, savegameObject.getUuid(), savegameObject.getValue(), false);
    }

    public boolean deleteSavegame(Context context, SavegameObject savegameObject) {
        return modifySavegame(context, savegameObject.getUuid(), savegameObject.getValue(), true);
    }

    private boolean modifySavegame(Context context, UUID uuid, String savegame, Boolean delete) {
        SavegameObject toUpdate = findByUUID(uuid);
        if(toUpdate != null) {
            String activity = context.getClass().getSimpleName();
            SharedPreferences savegames = context.getSharedPreferences(SAVE_DATA_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = savegames.edit();
            saveGameObjectList.remove(toUpdate);
            // delete == true -> delete value ... delete == false -> update value
            if(!delete) {
                // new SavegameObject with old UUID
                saveGameObjectList.add(new SavegameObject(uuid, savegame, activity));
            }
            putStringToEditor(editor);
            // UUID found
            return true;
        } else {
            // UUID not found
            return false;
        }
    }

    private void putStringToEditor(SharedPreferences.Editor editor) {
        String saveGameListToString = genson.serialize(saveGameObjectList, new GenericType<ArrayList<String>>(){});
        editor.putString(SAVE_DATA_KEY, saveGameListToString);
        editor.apply();
    }

    public ArrayList<SavegameObject> getSavegameObjectList() {
        return this.saveGameObjectList;
    }

    private SavegameObject findByUUID(UUID uuid) {
        for(SavegameObject e : saveGameObjectList) {
            if(e.getUuid().equals(uuid)) return e;
        }
        return null;

    }
}
