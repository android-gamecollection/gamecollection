package todo.gamecollection.model.gamemanager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import todo.gamecollection.R;
import todo.gamecollection.view.activity.GameActivity;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<Game> games = new ArrayList<>();

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

    class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String ACTIVITY_PACKAGE = ".view.activity.";
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
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            Context context = v.getContext();
            Game game = games.get(getAdapterPosition());
            String str = context.getPackageName() + ACTIVITY_PACKAGE + game.getActivity();
            intent.setClassName(context, str);
            intent.putExtra(GameActivity.KEY_GAME_UUID, game.getUuid());
            context.startActivity(intent);
        }
    }
}
