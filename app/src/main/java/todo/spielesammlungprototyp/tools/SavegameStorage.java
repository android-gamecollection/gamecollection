package todo.spielesammlungprototyp.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;

import java.util.ArrayList;
import java.util.Date;

public class SavegameStorage {

    private ArrayList<Savegame> saveGameList;
    private Genson genson = new Genson();
    private String SAVE_DATA_NAME = "Test.Savegames";
    private String SAVE_DATA_KEY = "TESTER1";
    private SharedPreferences savegamesSharedP;

    public SavegameStorage(Context context) {
        saveGameList = new ArrayList<>();
        savegamesSharedP = context.getSharedPreferences(SAVE_DATA_NAME, Context.MODE_PRIVATE);
        String saveGameListAsString = savegamesSharedP.getString(SAVE_DATA_KEY, "");
        if(!saveGameListAsString.isEmpty()) {
            saveGameList = genson.deserialize(saveGameListAsString, new GenericType<ArrayList<Savegame>>(){});
        }
    }

    public void addSavegame(Context context, String savegame) {
        String activity = context.getClass().getSimpleName();
        savegamesSharedP = context.getSharedPreferences(SAVE_DATA_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = savegamesSharedP.edit();
        saveGameList.add(new Savegame(savegame, activity));
        putStringToEditor(editor);
    }

    public boolean updateSavegame(Context context, Savegame savegame) {
        return modifySavegame(context, savegame, false);
    }

    public boolean deleteSavegame(Context context, Savegame savegame) {
        return modifySavegame(context, savegame, true);
    }

    private boolean modifySavegame(Context context, Savegame savegameMODIFIED, Boolean delete) {
        Savegame toUpdate = findByUUID(savegameMODIFIED.getUuid());
        // UUID found
        if(toUpdate != null) {
            savegamesSharedP = context.getSharedPreferences(SAVE_DATA_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = savegamesSharedP.edit();
            // delete == true -> delete value ... delete == false -> update value
            if(!delete) {
                toUpdate.setValue(savegameMODIFIED.getValue());
                toUpdate.setDate(new Date());
            } else {
                saveGameList.remove(toUpdate);
            }
            putStringToEditor(editor);

            return true;
        }
        // UUID not found
        else {
            return false;
        }
    }

    private void putStringToEditor(SharedPreferences.Editor editor) {
        String saveGameListToString = genson.serialize(this.saveGameList, new GenericType<ArrayList<Savegame>>(){});
        editor.putString(SAVE_DATA_KEY, saveGameListToString);
        editor.apply();
    }

    public ArrayList<Savegame> getSavegameList() {
        return this.saveGameList;
    }

    private Savegame findByUUID(String uuid) {
        for(Savegame e : saveGameList) {
            if(e.getUuid().equals(uuid)) return e;
        }
        return null;

    }

}
