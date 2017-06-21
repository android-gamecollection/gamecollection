package todo.spielesammlungprototyp.model.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import java.util.ArrayList;

import todo.spielesammlungprototyp.App;

public class SavegameStorage {

    private static SavegameStorage instance = null;
    private final String SAVE_DATA_NAME = "Test.Savegames";
    private final String SAVE_DATA_KEY = "TESTKEY0";
    private ArrayList<Savegame> saveGameList;
    private Genson genson = new GensonBuilder().useClassMetadata(true).useRuntimeType(true).create();
    private SharedPreferences savegamesSharedP;

    private SavegameStorage() {
        saveGameList = new ArrayList<>();
        savegamesSharedP = App.getContext().getSharedPreferences(SAVE_DATA_NAME, Context.MODE_PRIVATE);
        String saveGameListAsString = savegamesSharedP.getString(SAVE_DATA_KEY, "");
        if (!saveGameListAsString.isEmpty()) {
            saveGameList = genson.deserialize(saveGameListAsString, new GenericType<ArrayList<Savegame>>() {
            });
        }
    }

    public static SavegameStorage getInstance() {
        if (instance == null) {
            synchronized (SavegameStorage.class) {
                if (instance == null) {
                    instance = new SavegameStorage();
                }
            }
        }
        return instance;
    }

    public synchronized void addSavegame(Savegame savegame) {
        if (savegame != null) {
            SharedPreferences.Editor editor = savegamesSharedP.edit();
            saveGameList.add(savegame);
            putStringToEditor(editor);
        }
    }

    public synchronized boolean updateSavegame(Savegame savegame) {
        return modifySavegame(savegame, false);
    }

    public synchronized boolean deleteSavegame(Savegame savegame) {
        return modifySavegame(savegame, true);
    }

    private synchronized boolean modifySavegame(Savegame savegameMODIFIED, Boolean delete) {
        Savegame toUpdate = getFromUuid(savegameMODIFIED.uuid);
        // UUID found
        if (toUpdate != null) {
            SharedPreferences.Editor editor = savegamesSharedP.edit();
            // delete == true -> delete value ... delete == false -> update value
            if (!delete) {
                toUpdate.update(savegameMODIFIED.value);
            } else {
                saveGameList.remove(toUpdate);
            }
            putStringToEditor(editor);
            return true;
        } else {
            return false;
        }
    }

    private synchronized void putStringToEditor(SharedPreferences.Editor editor) {
        String saveGameListToString = genson.serialize(saveGameList, new GenericType<ArrayList<Savegame>>() {
        });
        editor.putString(SAVE_DATA_KEY, saveGameListToString);
        editor.apply();
    }

    public ArrayList<Savegame> getSavegameList() {
        return this.saveGameList;
    }

    public synchronized Savegame getFromUuid(String uuid) {
        for (Savegame e : saveGameList) {
            if (e.uuid.equals(uuid)) {
                return e;
            }
        }
        return null;
    }

}
