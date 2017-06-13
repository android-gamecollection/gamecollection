package todo.spielesammlungprototyp.model.games.chess;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import todo.spielesammlungprototyp.R;

public class ChessHistoryAdapter extends RecyclerView.Adapter<ChessHistoryAdapter.ItemViewHolder> {

    // Temp item list, will hold moves
    private List<String> mItems = new ArrayList<>();

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_game_chess_move, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addItem() {
        mItems.add(0, "");
        notifyItemInserted(0);
    }

    public void removeItem(int position) {
        for (; position >= 0; position--) {
            mItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        ItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
