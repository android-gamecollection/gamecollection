package todo.spielesammlungprototyp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GameCardViewAdapter extends RecyclerView.Adapter<GameCardViewAdapter.GameViewHolder> {

    private ArrayList<GameCardView> gameCardViews = new ArrayList<>();
    private ClickListener clicklistener = null;

    public GameCardViewAdapter(ArrayList<GameCardView> gameCardViews) {
        this.gameCardViews = gameCardViews;
    }

    public void setClickListener(ClickListener clicklistener) {
        this.clicklistener = clicklistener;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_spiele_auswahl_card_view, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GameViewHolder holder, final int position) {
        GameCardView SCV = gameCardViews.get(position);
        holder.spiel_icon.setImageResource(SCV.getSpiele_icon_id());
        holder.spiel_titel.setText(SCV.getSpiele_titel());
        holder.spiel_details.setText(SCV.getSpiele_details());
    }

    @Override
    public int getItemCount() {
        return gameCardViews.size();
    }

    class GameViewHolder extends RecyclerView.ViewHolder {

        final RelativeLayout card;
        final ImageView spiel_icon;
        final TextView spiel_titel;
        final TextView spiel_details;

        GameViewHolder(View view) {
            super(view);
            card = (RelativeLayout) view.findViewById(R.id.complete_card);
            spiel_icon = (ImageView) view.findViewById(R.id.spiele_icon);
            spiel_titel = (TextView) view.findViewById(R.id.spiele_titel);
            spiel_details = (TextView) view.findViewById(R.id.spiele_details);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clicklistener != null) {
                        clicklistener.itemClicked(v, getAdapterPosition());
                    }
                }
            });
        }
    }

}
