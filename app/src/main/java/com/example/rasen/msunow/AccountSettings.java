package com.example.rasen.msunow;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


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

    DatabaseReference myRef;
    DatabaseReference userRef;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;
    private View root;
    private String currentUserEmail;
    private EditText acstFirstName;
    private EditText acstLastName;
    private EditText acstEmail;
    private EditText acstPassword;
    private EditText acstDoB;
    private Button acstUpdateChanges;

    Map<String, String> activeUser;

    Calendar myCalendar = Calendar.getInstance();


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

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        myRef = database.getReference("users");
        user = auth.getCurrentUser();
        currentUserEmail = user.getEmail();


        acstFirstName = (EditText) root.findViewById(R.id.acst_firstName);
        acstLastName = (EditText) root.findViewById(R.id.acst_lastName);
        acstEmail = (EditText) root.findViewById(R.id.acst_Email);
        acstPassword = (EditText) root.findViewById(R.id.acst_Password);

        acstDoB = (EditText) root.findViewById(R.id.acst_DOB);
        acstDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        acstUpdateChanges = (Button) root.findViewById(R.id.acst_btn_update_changes);
        acstUpdateChanges.setOnClickListener(new UpdateChangesLsnr());


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                activeUser = getUsers((Map<String, Object>) dataSnapshot.getValue());
                if (activeUser != null) {
                    acstFirstName.setText(activeUser.get("firstName"));
                    acstLastName.setText(activeUser.get("lastName"));
                    acstEmail.setText(activeUser.get("email"));
                    acstPassword.setText(activeUser.get("password"));
                    acstDoB.setText(activeUser.get("dob"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Map<String, String> getUsers(Map<String, Object> user) {
        ArrayList<String> users = new ArrayList<>();
        for (Map.Entry<String, Object> entry : user.entrySet()) {
            Map<String, String> singleUser = (Map<String, String>) entry.getValue();
            if (singleUser.get("email").equals(currentUserEmail)) {
                return singleUser;

            }
        }
        return null;
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

    private class UpdateChangesLsnr implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            myRef = database.getReference("users");
            String updateUserId = activeUser.get("userId");

            // get the user entered data in variable
            final String firstName = acstFirstName.getText().toString().trim();
            final String lastName = acstLastName.getText().toString().trim();
            final String password = acstPassword.getText().toString().trim();
            final String dob = acstDoB.getText().toString().trim();

            clearErrors();
            //validating the entered data
            if (verifyData(firstName, lastName, password, dob)) {
                userRef = myRef.child(updateUserId);
                Map<String, Object> userMap = new HashMap<String, Object>();
                userMap.put("firstName", firstName);
                userMap.put("lastName", lastName);
                userMap.put("password", password);
                userMap.put("dob", dob);
                userRef.updateChildren(userMap);
            }
            Toast.makeText(getContext(),"Values Updated",Toast.LENGTH_SHORT).show();
        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDoB();
        }
    };

    private void updateDoB() {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        acstDoB.setText(sdf.format(myCalendar.getTime()));
    }

    private void clearErrors() {
        acstFirstName.setError(null);
        acstLastName.setError(null);
        acstPassword.setError(null);
        acstDoB.setError(null);
    }

    private boolean verifyData(String firstName, String lastName, String password, String dob) {
        Boolean isValid = true;

        // FirstName validation
        if (TextUtils.isEmpty(firstName)) {
            acstFirstName.setError("Please enter a first name");
            isValid = false;
        }

        // LastName validation
        if (TextUtils.isEmpty(lastName)) {
            acstLastName.setError("Please enter your last name");
            isValid = false;
        }

        // DoB validation
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        if (TextUtils.isEmpty(dob)) {
            acstDoB.setError("Please select valid date of birth");
            isValid = false;
        }

        // Password validation, minimum 6 characters required
        String passwordPattern = "(?=.*[A-Za-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$";
        if (TextUtils.isEmpty(password) || !(password.matches(passwordPattern))) {
            acstPassword.setError("Password should include alpha,no. & special characters with min length 6");
            isValid = false;

        }

        return isValid;
    }
}
