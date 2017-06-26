package todo.spielesammlungprototyp.model.games.chess;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import todo.spielesammlungprototyp.R;

public class ChessHistoryAdapter extends RecyclerView.Adapter<ChessHistoryAdapter.ItemViewHolder> {

    // Temp item list, will hold moves
    // TODO: Change to a list of move(tuple)s
    private List<String> mItems = new ArrayList<>();

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_game_chess_move, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        String move = mItems.get(position);
        // TODO: Move to Text
//        holder.setContent(
//                R.drawable.game_chess_king_b, "H9", "DD",
//                R.drawable.game_chess_pawn_b, "H2", "CU"
//        );
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
        for (int i = 0; i <= position; i++) {
            mItems.remove(0);
            notifyItemRemoved(0);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ImageView leftImage, rightImage;
        private final TextView leftTextFrom, leftTextTo, rightTextFrom, rightTextTo;

        ItemViewHolder(View itemView) {
            super(itemView);
            LinearLayout viewLeft = (LinearLayout) itemView.findViewById(R.id.move_left);
            LinearLayout viewRight = (LinearLayout) itemView.findViewById(R.id.move_right);
            leftImage = (ImageView) viewLeft.findViewById(R.id.image_piece);
            leftTextFrom = (TextView) viewLeft.findViewById(R.id.text_from);
            leftTextTo = (TextView) viewLeft.findViewById(R.id.text_to);

            rightImage = (ImageView) viewRight.findViewById(R.id.image_piece);
            rightTextFrom = (TextView) viewRight.findViewById(R.id.text_from);
            rightTextTo = (TextView) viewRight.findViewById(R.id.text_to);
        }

        void setContent(int leftImage, String leftTextFrom, String leftTextTo,
                        int rightImage, String rightTextFrom, String rightTextTo) {
            Drawable drawableLeft = ContextCompat.getDrawable(itemView.getContext(), leftImage);
            this.leftImage.setImageDrawable(drawableLeft);
            this.leftTextFrom.setText(leftTextFrom);
            this.leftTextTo.setText(leftTextTo);

            Drawable drawableRight = ContextCompat.getDrawable(itemView.getContext(), rightImage);
            this.rightImage.setImageDrawable(drawableRight);
            this.rightTextFrom.setText(rightTextFrom);
            this.rightTextTo.setText(rightTextTo);
        }
    }
}
