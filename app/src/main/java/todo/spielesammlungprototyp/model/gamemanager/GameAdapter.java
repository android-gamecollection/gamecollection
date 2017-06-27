package todo.spielesammlungprototyp.model.gamemanager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.interfaces.ClickListener;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<Game> games = new ArrayList<>();
    private ClickListener clickListener;

    public GameAdapter(List<Game> games) {
        this.games = games;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_game_selection_card_view, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GameViewHolder holder, final int position) {
        Game game = games.get(position);
        holder.gameIcon.setImageResource(game.getGameIconId());
        holder.gameTitle.setText(game.getGameTitle());
        holder.gameDescription.setText(game.getGameDescription());
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public void setClickListener(ClickListener clicklistener) {
        this.clickListener = clicklistener;
    }

    class GameViewHolder extends RecyclerView.ViewHolder {

        final RelativeLayout card;
        final ImageView gameIcon;
        final TextView gameTitle;
        final TextView gameDescription;

        GameViewHolder(View view) {
            super(view);
            card = (RelativeLayout) view.findViewById(R.id.card);
            gameIcon = (ImageView) view.findViewById(R.id.image_game);
            gameTitle = (TextView) view.findViewById(R.id.text_game_title);
            gameDescription = (TextView) view.findViewById(R.id.text_game_description);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.itemClicked(v, getAdapterPosition());
                    }
                }
            });
        }
    }
}
