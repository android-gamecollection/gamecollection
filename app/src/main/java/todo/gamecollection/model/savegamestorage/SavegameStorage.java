package todo.gamecollection.model.savegamestorage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import todo.gamecollection.App;

public class SavegameStorage {

    private static final String SAVE_DATA_NAME = "Gamecollection";
    private static final String SAVE_DATA_KEY = "SAVEGAMES";
    private static final Type gsonType = new TypeToken<ArrayList<Savegame>>() {
    }.getType();
    private static SavegameStorage instance = null;
    private ArrayList<Savegame> saveGameList;
    private Gson gson = new Gson();
    private SharedPreferences savegamesSharedP;

    private SavegameStorage() {
        saveGameList = new ArrayList<>();
        savegamesSharedP = App.getContext().getSharedPreferences(SAVE_DATA_NAME, Context.MODE_PRIVATE);
        String saveGameListAsString = savegamesSharedP.getString(SAVE_DATA_KEY, "");
        if (!saveGameListAsString.isEmpty()) {
            saveGameList = gson.fromJson(saveGameListAsString, gsonType);
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
            saveGameList.add(savegame);
            putStringToEditor();
        }
    }

    public synchronized void updateSavegame(Savegame savegame) {
        modifySavegame(savegame, false);
    }

    public synchronized void deleteSavegame(Savegame savegame) {
        modifySavegame(savegame, true);
    }

    private synchronized void modifySavegame(Savegame savegame, Boolean delete) {
        Savegame foundSavegame = getFromUuid(savegame.uuid);
        if (foundSavegame == null) {
            addSavegame(savegame);
        } else {
            // delete == true -> delete value ... delete == false -> update value
            if (delete) {
                saveGameList.remove(foundSavegame);
            } else {
                foundSavegame.update(savegame.bundle);
            }
            putStringToEditor();
        }
    }

    private synchronized void putStringToEditor() {
        for (Savegame savegame : saveGameList) {
            savegame.bundle.setClassLoader(null);
        }
        String saveGameListToString = gson.toJson(saveGameList, gsonType);
        SharedPreferences.Editor editor = savegamesSharedP.edit();
        editor.putString(SAVE_DATA_KEY, saveGameListToString);
        editor.apply();
    }

    public ArrayList<Savegame> getSavegameList() {
        return this.saveGameList;
    }

    public synchronized Savegame getFromUuid(String uuid) {
        if (uuid != null) {
            for (Savegame savegame : saveGameList) {
                if (savegame.uuid.equals(uuid)) {
                    return savegame;
                }
            }
        }
        return null;
    }
}
