package todo.spielesammlungprototyp.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import todo.spielesammlungprototyp.ClickListener;
import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.GameCardView;
import todo.spielesammlungprototyp.GameCardViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BoardGameSelection.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BoardGameSelection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardGameSelection extends Fragment implements ClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final ArrayList<GameCardView> spieleListe = new ArrayList<>();
    final int[] spiele_icon_id = {R.mipmap.ic_launcher};
    RecyclerView recyclerView;
    GameCardViewAdapter adapterC;
    RecyclerView.LayoutManager layoutManager;
    String[] spiele_titel, spiele_details;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public BoardGameSelection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BoardGameSelection.
     */
    // TODO: Rename and change types and number of parameters
    public static BoardGameSelection newInstance(String param1, String param2) {
        BoardGameSelection fragment = new BoardGameSelection();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_selection, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spiele_titel = getResources().getStringArray(R.array.spiele_titel_brettspiele);
        spiele_details = getResources().getStringArray(R.array.spiele_details_brettspiele);
        int counter = 0;
        for (String titel : spiele_titel) {
            GameCardView gameCardView = new GameCardView(spiele_icon_id[0], titel, spiele_details[counter]);
            counter++;
            spieleListe.add(gameCardView);
        }
        recyclerView = (RecyclerView) getView().findViewById(R.id.spiele_auswahl_RecyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapterC = new GameCardViewAdapter(spieleListe);
        adapterC.setClickListener(this);
        recyclerView.setAdapter(adapterC);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void itemClicked(View view, int position) {
        Intent intent = new Intent();
        Context context = view.getContext();
        String[] stringClassnames = getResources().getStringArray(R.array.spiele_activity_brettspiele);
        intent.setClassName(context, context.getPackageName() + ".activity." + stringClassnames[position]);
        context.startActivity(intent);
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
