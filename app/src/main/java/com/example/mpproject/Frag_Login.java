package com.example.mpproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.annotation.Nullable;

public class Frag_Login extends Fragment {

    private EditText emailTxt;
    private EditText pwTxt;
    private Button su_btn;
    private Button lg_btn;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_account, container, false);

        emailTxt = (EditText) view.findViewById(R.id.email_input);
        pwTxt = (EditText) view.findViewById(R.id.password_input);
        su_btn = (Button) view.findViewById(R.id.signup_btn);
        lg_btn = (Button) view.findViewById(R.id.login_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            Frag_profile frag_profile = new Frag_profile();
            ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, frag_profile).addToBackStack(null).commit();
        }


        su_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTxt.getText().toString().trim();
                String pwd = pwTxt.getText().toString().trim();

                firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "회원가입 완료", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        lg_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v) {
                String email = emailTxt.getText().toString().trim();
                String pwd = pwTxt.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "로그인 성공", Toast.LENGTH_SHORT).show();
                            Frag_profile frag_profile = new Frag_profile();
                            ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, frag_profile).addToBackStack(null).commit();
                        } else{
                            Toast.makeText(getActivity(), "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return view;
    }
}

