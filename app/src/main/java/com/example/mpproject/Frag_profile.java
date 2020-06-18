package com.example.mpproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import javax.annotation.Nullable;

public class Frag_profile extends Fragment {

    TextView userText;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    Button logout;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        GetUserRating.getInstance().GetUserRatingData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_onlogin, container, false);



        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        userText = view.findViewById(R.id.login_id);
        userText.setText(user.getEmail());


        logout = (Button)view.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Frag_Login frag_login = new Frag_Login();
                ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, frag_login).addToBackStack(null).commit();
                Toast.makeText(getActivity(), "로그아웃", Toast.LENGTH_SHORT).show();
            }
        });

        ListView lv = (ListView)view.findViewById(R.id.rating_lv);
        final MyAdapter myAdapter = new MyAdapter(getContext());
        System.out.println(GetUserRating.getInstance().userRatingList.size());
        lv.setAdapter(myAdapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                String rp_name = GetUserRating.getInstance().userRatingList.get(position).GetName().toString();
                String rp_code = "";
                for(int i = 0; i < GetRecipe.getInstance().recipeInfo.size(); i++) {
                    if(GetRecipe.getInstance().recipeInfo.get(i).GetName().equals(rp_name)){
                        rp_code = GetRecipe.getInstance().recipeInfo.get(i).GetCode();
                    }
                }

                Recipe_contents.getInstance().InitializeList();
                Recipe_contents.getInstance().Extract_Title(rp_code);
                Recipe_contents.getInstance().Extract_Ingredient(rp_code);
                Recipe_contents.getInstance().Extract_Process(rp_code);

                Frag_contents frag_contents = new Frag_contents();

                ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, frag_contents).addToBackStack(null).commit();
            }
        });

        return view;
    }

    public class MyAdapter extends BaseAdapter{
        Context mContext = null;

        public MyAdapter(Context context){
            mContext = context;
        }

        @Override
        public int getCount(){
            return GetUserRating.getInstance().userRatingList.size();
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public UserRating getItem(int position){
            return GetUserRating.getInstance().userRatingList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_rating, parent, false);
            }

            TextView nm = (TextView)convertView.findViewById(R.id.list_item_nm);
            nm.setText(GetUserRating.getInstance().userRatingList.get(position).GetName());

            TextView rating = (TextView)convertView.findViewById(R.id.list_rating);
            rating.setText(GetUserRating.getInstance().userRatingList.get(position).GetRate());

            return convertView;

        }

    }
}
