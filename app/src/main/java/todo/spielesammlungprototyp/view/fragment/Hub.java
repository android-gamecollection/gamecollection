package todo.spielesammlungprototyp.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.util.Savegame;
import todo.spielesammlungprototyp.model.util.SavegameStorage;
import todo.spielesammlungprototyp.view.ClickListener;
import todo.spielesammlungprototyp.view.Game;
import todo.spielesammlungprototyp.view.GameCardViewAdapter;

import static todo.spielesammlungprototyp.model.util.SavegameStorage.getInstance;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Hub.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Hub#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Hub extends Fragment implements ClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String ACTIVITY_PACKAGE = ".view.activity.";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton fab;
    private OnFragmentInteractionListener mListener;
    private ArrayList<Game> games = new ArrayList<>();
    private SavegameStorage savegameStorage;
    private ArrayList<Savegame> savegames;

    public Hub() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Hub.
     */
    // TODO: Rename and change types and number of parameters
    public static Hub newInstance(String param1, String param2) {
        Hub fragment = new Hub();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadGames();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_selection, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerview_game_selection);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        GameCardViewAdapter gcvAdapter = new GameCardViewAdapter(games);
        gcvAdapter.setClickListener(this);
        recyclerView.setAdapter(gcvAdapter);
    }

    @Override
    public void itemClicked(View view, int position) {
        Intent intent = new Intent();
        Context context = view.getContext();
        Game game = games.get(position);
        String str = context.getPackageName() + ACTIVITY_PACKAGE + game.getActivity();
        intent.setClassName(context, str);
        intent.putExtra("UUID", game.getUuid());
        context.startActivity(intent);
    }

    private void loadGames() {
        savegameStorage = getInstance(getActivity().getApplicationContext());
        savegames = savegameStorage.getSavegameList();
        for (Savegame s : savegames) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy  hh:mm:ss", Locale.GERMANY);
            String date = simpleDateFormat.format(s.date);
            games.add(new Game(R.mipmap.ic_launcher, s.activity, date, "", s.activity, s.uuid)); //TODO: gameIcon, gameTitle & gameRules have to be dependent too
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
