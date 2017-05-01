package com.example.rasen.msunow;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rasen.msunow.InputTopic.Topic;
import com.example.rasen.msunow.Utils.Utils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomePage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View root;
    private Button input;
    private ArrayList<String> trending;
    DatabaseReference myRef;
    FirebaseDatabase database;
    ChildEventListener mChildEventListener;
    Spinner trendSpin;
    ArrayList<Topic> trendingTopics;
    ArrayAdapter<String> tAdapter;

    private OnFragmentInteractionListener mListener;

    public HomePage() {
        // Required empty public constructor
    }

    public void loadUI(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("topic");
        trendingTopics = new ArrayList<>();

        AutoCompleteTextView search = (AutoCompleteTextView) root.findViewById(R.id.hp_searchbox);

        Button searchbtn = (Button) root.findViewById(R.id.hp_searchbtn);

        input = (Button) root.findViewById(R.id.hp_input);
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UserInputActivity.class));
            }
        });

        Spinner trendSpin = (Spinner) root.findViewById(R.id.hp_trending_spinner);
        trendSpin = (Spinner) root.findViewById(R.id.hp_trending_spinner);
        String[] trendTimes = {"Past Hour", "Past Day", "Past Week", "Past Month", "All Time"};
        ArrayAdapter<String> spAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, trendTimes);
        trendSpin.setAdapter(spAdapter);
        trendSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setTrending();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ListView lvTrend = (ListView) root.findViewById(R.id.hp_listview_trending);
        final String[] exampleTrend = {"TEST1", "TEST2", "TEST3", "TEST4", "TEST5"};
        trending = new ArrayList<>();
        tAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, trending);
        lvTrend.setAdapter(tAdapter);
        lvTrend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ForumPage.class);
                String[] sList = trending.get(position).split("\t-\t");
                intent.putExtra("TITLE", sList[0]);
                intent.putExtra("ROOM", sList[1]);
                startActivity(intent);
            }
        });

        ListView lvRooms = (ListView) root.findViewById(R.id.hp_listview_rooms);
        final String[] rooms = getActivity().getResources().getStringArray(R.array.rooms);
        ArrayAdapter<String> rAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, rooms);
        lvRooms.setAdapter(rAdapter);
        lvRooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RoomPage.class);
                intent.putExtra("ROOM", rooms[position]);
                startActivity(intent);
            }
        });

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Topic topic = dataSnapshot.getValue(Topic.class);
                    trendingTopics.add(topic);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.addChildEventListener(mChildEventListener);

        sortTopics();
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePage.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePage newInstance(String param1, String param2) {
        HomePage fragment = new HomePage();
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
        View rootView = inflater.inflate(R.layout.fragment_home_page, container, false);
        root = rootView;
        loadUI();
        return rootView;
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    private void sortTopics() {
        for(int i=0;i<trendingTopics.size();i++){
            int highestKarma = i;
            for(int j=i+1;j<trendingTopics.size();j++){
                if(trendingTopics.get(highestKarma).getKarma()<trendingTopics.get(j).getKarma()){
                    highestKarma = j;
                }
            }
            trendingTopics.add(i, trendingTopics.remove(highestKarma));
        }
    }

    public long differencceInTime(String date) {
        SimpleDateFormat format = new SimpleDateFormat(Utils.DATEFORMAT);
        try {
            return Calendar.getInstance().getTime().getTime() - format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void setTrending() {
        trending.clear();
        tAdapter.clear();
        for(Topic topic: trendingTopics) {
            if (trending.size() < 5) {
                Log.i("TIME", String.valueOf(differencceInTime(topic.getTime())));
                switch (trendSpin.getSelectedItemPosition()) {
                    case 0:
                        if (differencceInTime(topic.getTime()) <= 3600000)
                            trending.add(topic.getTitle()+"\t-\t"+topic.getRoom());
                        break;
                    case 1:
                        if (differencceInTime(topic.getTime()) <= 86400000)
                            trending.add(topic.getTitle()+"\t-\t"+topic.getRoom());
                        break;
                    case 2:
                        if (differencceInTime(topic.getTime()) <= 604800000)
                            trending.add(topic.getTitle()+"\t-\t"+topic.getRoom());
                        break;
                    case 3:
                        long num = (long) 259200000*10;
                        if (differencceInTime(topic.getTime()) <= num)
                            trending.add(topic.getTitle()+"\t-\t"+topic.getRoom());
                        break;
                    case 4:
                        trending.add(topic.getTitle()+"\t-\t"+topic.getRoom());
                        break;
                    default:
                        break;

                }
            }
        }

    }
}
