package todo.spielesammlungprototyp.view.fragment;

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

import todo.spielesammlungprototyp.view.ClickListener;
import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.view.GameCardView;
import todo.spielesammlungprototyp.view.GameCardViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CardGameSelection.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CardGameSelection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardGameSelection extends Fragment implements ClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final ArrayList<GameCardView> gameList = new ArrayList<>();
    final int[] gameIconId = {R.mipmap.ic_launcher};
    RecyclerView recyclerView;
    GameCardViewAdapter adapterC;
    RecyclerView.LayoutManager layoutManager;
    String[] gameTitle, gameDetails;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public CardGameSelection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardGameSelection.
     */
    // TODO: Rename and change types and number of parameters
    public static CardGameSelection newInstance(String param1, String param2) {
        CardGameSelection fragment = new CardGameSelection();
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
        gameTitle = getResources().getStringArray(R.array.spiele_titel_kartenspiele);
        gameDetails = getResources().getStringArray(R.array.spiele_details_kartenspiele);
        int counter = 0;
        for (String titel : gameTitle) {
            GameCardView gameCardView = new GameCardView(gameIconId[counter], titel, gameDetails[counter]);
            counter++;
            gameList.add(gameCardView);
        }
        recyclerView = (RecyclerView) getView().findViewById(R.id.spiele_auswahl_RecyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapterC = new GameCardViewAdapter(gameList);
        adapterC.setClickListener(this);
        recyclerView.setAdapter(adapterC);
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
        } */
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
        String[] stringClassnames = getResources().getStringArray(R.array.spiele_activity_kartenspiele);
        intent.setClassName(context, context.getPackageName() + ".view.activity." + stringClassnames[position]);
        context.startActivity(intent);
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
