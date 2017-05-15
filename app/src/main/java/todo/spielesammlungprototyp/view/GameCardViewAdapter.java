package todo.spielesammlungprototyp.view;

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

public class GameCardViewAdapter extends RecyclerView.Adapter<GameCardViewAdapter.GameViewHolder> {

    private List<GameCardView> gameCardViews = new ArrayList<>();
    private ClickListener clickListener = null;

    public GameCardViewAdapter(List<GameCardView> gameCardViews) {
        this.gameCardViews = gameCardViews;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_game_selection_card_view, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GameViewHolder holder, final int position) {
        GameCardView SCV = gameCardViews.get(position);
        holder.gameIcon.setImageResource(SCV.getGameIconId());
        holder.gameTitle.setText(SCV.getGameTitle());
        holder.gameDetails.setText(SCV.getGameDetails());
    }

    @Override
    public int getItemCount() {
        return gameCardViews.size();
    }

    public void setClickListener(ClickListener clicklistener) {
        this.clickListener = clicklistener;
    }

    class GameViewHolder extends RecyclerView.ViewHolder {

        final RelativeLayout card;
        final ImageView gameIcon;
        final TextView gameTitle;
        final TextView gameDetails;

        GameViewHolder(View view) {
            super(view);
            card = (RelativeLayout) view.findViewById(R.id.card);
            gameIcon = (ImageView) view.findViewById(R.id.image_game);
            gameTitle = (TextView) view.findViewById(R.id.text_game_title);
            gameDetails = (TextView) view.findViewById(R.id.text_game_details);

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
