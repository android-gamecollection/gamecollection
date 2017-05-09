package todo.spielesammlungprototyp.Fragments;

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
import todo.spielesammlungprototyp.Spiel_CardView;
import todo.spielesammlungprototyp.Spiel_CardViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Kartenspiele_AuswahlFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Kartenspiele_AuswahlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Kartenspiele_AuswahlFragment extends Fragment implements ClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    Spiel_CardViewAdapter adapterC;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Spiel_CardView> spieleListe = new ArrayList<>();
    int[] spiele_icon_id = {R.mipmap.ic_launcher};
    String[] spiele_titel, spiele_details;

    private OnFragmentInteractionListener mListener;

    public Kartenspiele_AuswahlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Kartenspiele_AuswahlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Kartenspiele_AuswahlFragment newInstance(String param1, String param2) {
        Kartenspiele_AuswahlFragment fragment = new Kartenspiele_AuswahlFragment();
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
        return inflater.inflate(R.layout.fragment_spiele_auswahl, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spiele_titel = getResources().getStringArray(R.array.spiele_titel_kartenspiele);
        spiele_details = getResources().getStringArray(R.array.spiele_details_kartenspiele);
        int counter = 0;
        for (String titel : spiele_titel) {
            Spiel_CardView spiel_cardView = new Spiel_CardView(spiele_icon_id[counter], titel, spiele_details[counter]);
            counter++;
            spieleListe.add(spiel_cardView);
        }
        recyclerView = (RecyclerView) getView().findViewById(R.id.spiele_auswahl_RecyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapterC = new Spiel_CardViewAdapter(spieleListe);
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
        intent.setClassName(context, context.getPackageName() + "." + stringClassnames[position]);
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
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
