package todo.spielesammlungprototyp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class Spiel_CardViewAdapter extends RecyclerView.Adapter<Spiel_CardViewAdapter.SpieleViewHolder>{

    private static final int VIEW_TYPE_FIRST  = 0;
    private static final int VIEW_TYPE_SECOND = 1;
    private ArrayList<Spiel_CardView> spiele_cardview = new ArrayList<>();

    Spiel_CardViewAdapter(ArrayList<Spiel_CardView> spiele_cardview) {
        this.spiele_cardview = spiele_cardview;
    }

    @Override
    public SpieleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_FIRST: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_brettspiele_auswahl_card_view, parent, false);
                return new SpieleViewHolder(view);
            }
            case VIEW_TYPE_SECOND: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_kartenspiele_auswahl_card_view, parent, false);
                return new SpieleViewHolder(view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(SpieleViewHolder holder, int position) {
        Spiel_CardView SCV = spiele_cardview.get(position);
        holder.spiel_icon.setImageResource(SCV.getSpiele_icon_id());
        holder.spiel_titel.setText(SCV.getSpiele_titel());
        holder.spiel_details.setText(SCV.getSpiele_details());
    }

    @Override
    public int getItemCount() {
        return spiele_cardview.size();
    }

    static class SpieleViewHolder extends RecyclerView.ViewHolder {

        ImageView spiel_icon;
        TextView spiel_titel,spiel_details;

        SpieleViewHolder(View view) {
            super(view);
            spiel_icon = (ImageView) view.findViewById(R.id.spiele_icon);
            spiel_titel = (TextView) view.findViewById(R.id.spiele_titel);
            spiel_details = (TextView) view.findViewById(R.id.spiele_details);
        }
    }
}
