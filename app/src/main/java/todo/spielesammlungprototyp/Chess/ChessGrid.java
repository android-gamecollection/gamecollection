package todo.spielesammlungprototyp.Chess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import todo.spielesammlungprototyp.R;

public class ChessGrid extends BaseAdapter {
    private Context mContext;
    private final int[] Imageid;
    private final String[] chessFigure;

    public ChessGrid(Context c, int[] Imageid, String[] chessFigure) {
        mContext = c;
        this.Imageid = Imageid;
        this.chessFigure = chessFigure;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Imageid.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            //grid = new View(mContext);
            grid = inflater.inflate(R.layout.chess_grid_single, null);
            ImageView imageView = (ImageView)grid.findViewById(R.id.chess_plate);
            TextView textView = (TextView)grid.findViewById(R.id.chess_figure);
            textView.setText(chessFigure[position]);
            imageView.setImageResource(Imageid[position]);

        } else {
            grid = convertView;
        }

        return grid;
    }
}