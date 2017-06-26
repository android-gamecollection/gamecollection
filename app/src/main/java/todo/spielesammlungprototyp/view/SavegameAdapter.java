package todo.spielesammlungprototyp.view;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import todo.spielesammlungprototyp.App;
import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.gamemanager.Game;
import todo.spielesammlungprototyp.model.gamemanager.Games;
import todo.spielesammlungprototyp.model.util.Savegame;
import todo.spielesammlungprototyp.model.util.SavegameStorage;

public class SavegameAdapter extends RecyclerView.Adapter<SavegameAdapter.SavegameViewHolder> {

    private SortedList<Savegame> mSavegames = new SortedList<>(Savegame.class, new SavegameAdapterCallback(this));
    private SavegameStorage savegameStorage = SavegameStorage.getInstance();
    private ClickListener clickListener;

    public SavegameAdapter(ArrayList<Savegame> savegames) {
        this.mSavegames.addAll(savegames);
    }

    @Override
    public SavegameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_savegame_card_view, parent, false);
        return new SavegameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SavegameViewHolder holder, int position) {
        Savegame savegame = mSavegames.get(position);
        Game game = Games.getFromUuid(savegame.gameUuid);
        if (game != null) {
            holder.gameTitle.setText(game.getGameTitle());
            holder.gameSubtitle.setText(game.getGameDescription());
            holder.gameIcon.setImageDrawable(App.getContext().getDrawable(game.getGameIconId()));
            holder.gameDate.setText(savegame.getDateString().toUpperCase());
        }
    }

    @Override
    public int getItemCount() {
        return mSavegames.size();
    }

    public void addItem(Savegame savegame) {
        mSavegames.add(savegame);
        savegameStorage.addSavegame(savegame);
    }

    public void removeItem(int position) {
        Savegame savegame = mSavegames.removeItemAt(position);
        savegameStorage.deleteSavegame(savegame);
    }

    public Savegame get(int position) {
        return mSavegames.get(position);
    }

    public void setClickListener(ClickListener clicklistener) {
        this.clickListener = clicklistener;
    }

    class SavegameViewHolder extends RecyclerView.ViewHolder {

        private final ImageView gameIcon;
        private final TextView gameTitle, gameSubtitle, gameDate;

        SavegameViewHolder(View itemView) {
            super(itemView);
            gameIcon = (ImageView) itemView.findViewById(R.id.icon_savegame);
            gameTitle = (TextView) itemView.findViewById(R.id.text_game_title);
            gameSubtitle = (TextView) itemView.findViewById(R.id.text_game_subtitle);
            gameDate = (TextView) itemView.findViewById(R.id.text_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.itemClicked(v, getAdapterPosition());
                    }
                }
            });
        }
    }

    private class SavegameAdapterCallback extends SortedListAdapterCallback<Savegame> {

        SavegameAdapterCallback(RecyclerView.Adapter adapter) {
            super(adapter);
        }

        @Override
        public int compare(Savegame o1, Savegame o2) {
            return o2.compareTo(o1);
        }

        @Override
        public boolean areContentsTheSame(Savegame oldItem, Savegame newItem) {
            return oldItem.gameUuid.equals(newItem.gameUuid)
                    && oldItem.bundle.equals(newItem.bundle);
        }

        @Override
        public boolean areItemsTheSame(Savegame item1, Savegame item2) {
            return item1.equals(item2);
        }
    }
}
