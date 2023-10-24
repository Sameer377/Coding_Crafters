package com.agpitcodeclub.app.fragments;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.agpitcodeclub.app.Dashboard.userLogin;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.agpitcodeclub.app.Models.CommunityModel;
import com.agpitcodeclub.app.Models.User;
import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.credentials.PasswordGenerator;
import com.agpitcodeclub.app.utils.Credentials;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;


public class Login extends Fragment implements View.OnClickListener {
    private EditText[] editTexts;
    private EditText cre_name, cre_email, cre_password;
    private RelativeLayout rel_otp_4edt;
    private AppCompatButton login_btn;
    private CheckBox checkbox_mem;
    private String userId="";
    private TextView l_txt,txt_page_t,txt_link_signup;
    private LinearLayout lin_page_t;
    private LinearProgressIndicator signup_status_prg;
    private boolean signup =true;
    private EditText otp_edit_1,otp_edit_2,otp_edit_3,otp_edit_4;
    private ImageView otp_ic;
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference databaseReference;

    public Login() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Context context ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initUi(view);
        initListener();
        context=container.getContext();

        return view;
    }

    private void initListener() {
        login_btn.setOnClickListener(this);
        lin_page_t.setOnClickListener(this);
    }

    private void initUi(View view) {
        rel_otp_4edt=view.findViewById(R.id.rel_otp_4edt);
        cre_name = view.findViewById(R.id.cre_name);
        cre_email = view.findViewById(R.id.cre_email);
        cre_password = view.findViewById(R.id.cre_password);
        login_btn = view.findViewById(R.id.login_btn);
        checkbox_mem = view.findViewById(R.id.checkbox_mem);
        l_txt=view.findViewById(R.id.l_txt);
        lin_page_t=view.findViewById(R.id.lin_page_t);
        txt_link_signup=view.findViewById(R.id.txt_link_signup);
        txt_page_t=view.findViewById(R.id.txt_page_t);
        signup_status_prg=view.findViewById(R.id.signup_status_prg);
        otp_ic=view.findViewById(R.id.otp_ic);

        otp_edit_1=view.findViewById(R.id.otp_edt_1);
        otp_edit_2=view.findViewById(R.id.otp_edt_2);
        otp_edit_3=view.findViewById(R.id.otp_edt_3);
        otp_edit_4=view.findViewById(R.id.otp_edt_4);

        editTexts = new EditText[]{otp_edit_1, otp_edit_2, otp_edit_3, otp_edit_4};

        otp_edit_1.addTextChangedListener(new PinTextWatcher(0));
        otp_edit_2.addTextChangedListener(new PinTextWatcher(1));
        otp_edit_3.addTextChangedListener(new PinTextWatcher(2));
        otp_edit_4.addTextChangedListener(new PinTextWatcher(3));

        otp_edit_1.setOnKeyListener(new PinOnKeyListener(0));
        otp_edit_2.setOnKeyListener(new PinOnKeyListener(1));
        otp_edit_3.setOnKeyListener(new PinOnKeyListener(2));
        otp_edit_4.setOnKeyListener(new PinOnKeyListener(3));

        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference(FirebasePath.USERS);


    }


    private void signUp(){
        otp_ic.setVisibility(View.GONE);
        cre_email.setVisibility(View.VISIBLE);
        cre_password.setVisibility(View.VISIBLE);
        txt_link_signup.setVisibility(View.VISIBLE);
        txt_page_t.setVisibility(View.VISIBLE);
        rel_otp_4edt.setVisibility(View.GONE);
        cre_name.setVisibility(View.VISIBLE);
        l_txt.setText("Sign Up");
        login_btn.setText("Sign Up");
        txt_link_signup.setText("Login");
        txt_page_t.setText("Already have  an Account ?");
        checkbox_mem.setVisibility(View.INVISIBLE);
    }

    private void login(){
        otp_ic.setVisibility(View.GONE);

        cre_email.setVisibility(View.VISIBLE);
        cre_password.setVisibility(View.VISIBLE);
        txt_link_signup.setVisibility(View.VISIBLE);
        txt_page_t.setVisibility(View.VISIBLE);
        cre_name.setVisibility(View.INVISIBLE);
        l_txt.setText("Login");
        login_btn.setText("Login");
        txt_page_t.setText("New User? ");
        txt_link_signup.setText("Sign Up");
        checkbox_mem.setVisibility(View.VISIBLE);
    }

    private void otp(){
        otp_ic.setVisibility(View.VISIBLE);
        sendSMS("8999596143", String.valueOf(otp));
        rel_otp_4edt.setVisibility(View.VISIBLE);
        cre_name.setVisibility(View.GONE);
        cre_email.setVisibility(View.INVISIBLE);
        cre_password.setVisibility(View.INVISIBLE);
        l_txt.setVisibility(View.INVISIBLE);
        login_btn.setText("Verify");
        txt_link_signup.setVisibility(View.GONE);
        txt_page_t.setVisibility(View.GONE);
        checkbox_mem.setVisibility(View.GONE);
    }

    public static boolean isMember=false;
    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
    private void adduser() {

        String name = cre_name.getText().toString().trim();
        String email = cre_email.getText().toString().trim();
        String password = cre_password.getText().toString().trim();

        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regex);

        if (name.isEmpty()) {
            cre_name.setError("Invalid name");
            cre_name.requestFocus();
            return;
        }  if (email.isEmpty() || !Pattern.compile(regex).matcher(email).matches()) {
            cre_email.setError("Invalid email");
            cre_email.requestFocus();
            return;
        }  if (password.isEmpty()) {
            cre_password.setError("Invalid Password");
            cre_password.requestFocus();
            return;
        }

            signup_status_prg.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NotNull Task<AuthResult> task) {
                            FirebaseUser userI = FirebaseAuth.getInstance().getCurrentUser();

                            if (userI != null) {
                                userId=userI.getUid();
                            }

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("status : ", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                User user1=new User(name,email,password);
                                mFirebaseDatabase       .child(userId).setValue(user1);
                                signup_status_prg.setVisibility(View.GONE);
                                userLogin=true;
                                storeUserData(email,password,name,null,null, null);
                                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.dashboardframe, new Profile(), "NewFragmentTag");
                                ft.commit();

                                Toast.makeText(getContext(), "User Added",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                signup_status_prg.setVisibility(View.GONE);
                                // If sign in fails, display the message to the user.
                                Toast.makeText(getContext(), "Authentication failed",
                                        Toast.LENGTH_SHORT).show();
                                Log.w("UserCreation", "createUserWithEmail:failure", task.getException());
                            }
                        }
                    });


    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case   R.id.login_btn:
                if (signup){
                    adduser();
                }else {
                    loginUser();
                }

                break;

            case R.id.lin_page_t:

                if (signup){
                    login();

                    signup =false;
                }else {
                    signUp();

                    signup =true;
                }

                break;
        }
    }
    char otp[] =new char[4];

    private void loginUser() {
        String name = cre_name.getText().toString().trim();
        String email = cre_email.getText().toString().trim();
        String password = cre_password.getText().toString().trim();

        //generate OTP
        PasswordGenerator obj =new PasswordGenerator();
        otp=obj.password(4);

        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regex);

        if (email.isEmpty() || !Pattern.compile(regex).matcher(email).matches()) {
            cre_email.setError("Invalid email");
            cre_email.requestFocus();
            return;
        }  if (password.isEmpty()) {
            cre_password.setError("Invalid Password");
            cre_password.requestFocus();
            return;
        }
        signup_status_prg.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NotNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                           UserID= FirebaseAuth.getInstance().getCurrentUser().getUid();
                            isMember=checkbox_mem.isChecked();
                            checkDataInDatabase();

                        } else {
                            signup_status_prg.setVisibility(View.GONE);
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String UserID;
    private String token_des = "";
    private DatabaseReference reference;


    void isOTPVerified(FragmentTransaction fragmentTransaction,String email,String pass,String name,String year,String designation,String imgAddress ){

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userotp=otp_edit_1.getText().toString().trim()+otp_edit_2.getText().toString().trim()+otp_edit_3.getText().toString().trim()+otp_edit_4.getText().toString().trim();
                if(userotp.equals(String.valueOf(otp))){
                    storeUserData(email,pass,name,year,designation,imgAddress);

                    fragmentTransaction.commit();
                }else {
                    Toast.makeText(getContext(),"Verification Failed !",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void checkDataInDatabase(){
        otp();
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.dashboardframe, new Profile(), "NewFragmentTag");
        if(isMember){
            databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.MEMBER_USERTOKENS);

            databaseReference.child(UserID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NotNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        if (task.getResult().getValue()==null){
                            Toast.makeText(getContext(),"User not found ! ",Toast.LENGTH_SHORT).show();

                        }else{
                            token_des = task.getResult().getValue().toString();
                        }

                        reference = FirebaseDatabase.getInstance().getReference(FirebasePath.COMMUNITY);



                        if(!token_des.equals(FirebasePath.DEVELOPER)&& !token_des.equals(FirebasePath.MEMBER)) {

                            reference.child(token_des).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    CommunityModel user = snapshot.getValue(CommunityModel.class);
                                    if (user != null) {
                                        userLogin=true;

                                        isOTPVerified(ft,user.getemail(),user.getPassword(),user.getName(),user.getPersuing(),token_des,user.getProfile());
                                    }else {
                                        Toast.makeText(getContext(),"User not found ! ",Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                        else if(token_des.equals(FirebasePath.DEVELOPER)) {
                            reference.child(token_des).child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    CommunityModel user = snapshot.getValue(CommunityModel.class);
                                    if (user != null) {
                                        userLogin=true;


                                        isOTPVerified(ft,user.getemail(),user.getPassword(),user.getName(),user.getPersuing(),token_des,user.getProfile());


                                    }else {
                                        Toast.makeText(getContext(),"User not found ! ",Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }else if(token_des.equals(FirebasePath.MEMBER)) {
                            reference.child(token_des).child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    CommunityModel user = snapshot.getValue(CommunityModel.class);
                                    if (user != null) {
                                        userLogin=true;

                                        isOTPVerified(ft,user.getemail(),user.getPassword(),user.getName(),user.getPersuing(),token_des,user.getProfile());


                                    }else {
                                        Toast.makeText(getContext(),"User not found ! ",Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    }
                }
            });
        }else{

            databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.USERS);


            databaseReference.child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    CommunityModel user = snapshot.getValue(CommunityModel.class);
                    if (user != null) {
                        userLogin=true;
                        isOTPVerified(ft,user.getemail(),user.getPassword(),user.getName(),null,FirebasePath.USERS,user.getProfile());


                    }else{
                        Toast.makeText(getContext(),"User not found !",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();

                }
            });


        }
        signup_status_prg.setVisibility(View.GONE);

    }


    private void storeUserData(String email,String pass,String name,String year,String designation,String imgAddress){
        SharedPreferences.Editor editor = getContext().getSharedPreferences(Credentials.USER_DATA, getContext().MODE_PRIVATE).edit();
        editor.putString(Credentials.USER_EMAIL, email);
        editor.putString(Credentials.USER_PASS, pass);
        editor.putString(Credentials.USER_NAME,name);





           if(designation!=FirebasePath.USERS&&designation!=null){
               editor.putString(Credentials.USER_PROFILE_IMG,imgAddress);
               editor.putString(Credentials.USER_YEAR,year);
               editor.putString(Credentials.USER_DESIGNATION,designation);
           }else {
               editor.putString(Credentials.USER_PROFILE_IMG,null);
               editor.putString(Credentials.USER_YEAR,null);
               editor.putString(Credentials.USER_DESIGNATION,null);

           }



        editor.apply();



    }




    public class PinTextWatcher implements TextWatcher {

        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newTypedString = s.subSequence(start, start + count).toString().trim();
        }


        @Override
        public void afterTextChanged(Editable s) {

            String text = newTypedString;

            /* Detect paste event and set first char */
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0)); // TODO: We can fill out other EditTexts

            editTexts[currentIndex].removeTextChangedListener(this);
            editTexts[currentIndex].setText(text);
            editTexts[currentIndex].setSelection(text.length());
            editTexts[currentIndex].addTextChangedListener(this);

            if (text.length() == 1) {
                moveToNext();
            }
            else if (text.length() == 0)
            {
                int otp_t_flag=1;
                if(otp_t_flag==1){
                    otp_t_flag--;
                }else{
                    moveToPrevious();
                }
                otp_t_flag=1;

            }


        }

        private void moveToNext() {
            if (!isLast)
                editTexts[currentIndex + 1].requestFocus();

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                editTexts[currentIndex].clearFocus();
                hideKeyboard();
            }
        }

        private void moveToPrevious() {
            if (!isFirst)
                editTexts[currentIndex - 1].requestFocus();
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : editTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }

        private void hideKeyboard() {
            if (getActivity().getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        }

    }

    public class PinOnKeyListener implements View.OnKeyListener {

        private int currentIndex;

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (editTexts[currentIndex].getText().toString().isEmpty() && currentIndex != 0)
                    editTexts[currentIndex - 1].requestFocus();
            }
            return false;
        }


    }
}