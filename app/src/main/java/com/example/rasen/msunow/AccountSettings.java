package com.example.rasen.msunow;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rasen.msunow.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountSettings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountSettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountSettings extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ArrayList<String> users;
    DatabaseReference myRef;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ChildEventListener mChildEventListener;
    private SharedPreferences shprefs;
    private View root;
    // TODO: Get current user in email
    private String email = "user1@montclair.edu";
    private AccountSettings accountSettingRequest;

    private EditText acstFirstName;
    private EditText acstLastName;
    private EditText acstEmail;
    private EditText acstPassword;
    private EditText acstDoB;
    private Button acstSubmit;


    public AccountSettings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountSettings.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountSettings newInstance(String param1, String param2) {
        AccountSettings fragment = new AccountSettings();
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
        View rootView = inflater.inflate(R.layout.fragment_account_settings, container, false);
        root = rootView;
        loadUI();
        return rootView;
    }

    private void loadUI() {

        users = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        myRef = database.getReference("users");

        acstFirstName = (EditText) root.findViewById(R.id.acst_firstName);
        acstLastName = (EditText) root.findViewById(R.id.acst_lastName);
        acstEmail = (EditText) root.findViewById(R.id.acst_Email);
        acstPassword = (EditText) root.findViewById(R.id.acst_Password);
        acstDoB = (EditText) root.findViewById(R.id.acst_DOB);
        acstSubmit = (Button) root.findViewById(R.id.acst_btn_save_settings);

        //useremail.setText(getSharedPreferences(Utils.SHPRFN, MODE_APPEND).getString(Utils.CURRUSER, ""));

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                RegisterRequest users = dataSnapshot.getValue(RegisterRequest.class);
                if(users.getEmail().equals(email)) {
                    acstFirstName.setText(users.getFirstName());
                    acstLastName.setText(users.getLastName());
                    acstEmail.setText(users.getEmail());
                    acstPassword.setText(users.getPassword());
                    acstDoB.setText(users.getDob());
                }
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
}
