package com.example.mpproject;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;


public class Frag_recipeBoard extends Fragment {

    private static final int PICK_FROM_ALBUM = 1;
    private File tempFile;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    TableLayout tbLayout;
    TableLayout tbLayout2;
    Button add_ing;
    Button remove_ing;
    Button add_prc;
    Button remove_prc;

    Button enroll;
    ImageView food_img;
    ImageView addedIV;
    ImageView first_prc_img;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_board, container, false);

        tedPermission();

        add_ing = view.findViewById(R.id.add_ing_btn);
        add_prc = view.findViewById(R.id.add_process_btn);
        remove_ing = view.findViewById(R.id.remove_ing_btn);
        remove_prc = view.findViewById(R.id.remove_process_btn);

        enroll = view.findViewById(R.id.enroll_btn);
        food_img = view.findViewById(R.id.food_img);
        first_prc_img = view.findViewWithTag("processImg_1");

        tbLayout = view.findViewById(R.id.ing_table);
        tbLayout2 = view.findViewById(R.id.prc_table);


        add_ing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow row = new TableRow(getContext());
                row.setTag("ing_row" + (tbLayout.getChildCount() + 1));
                TableRow.LayoutParams lp = new TableRow.LayoutParams();
                row.setLayoutParams(lp);

                EditText ing_name = new EditText(getContext());
                ing_name.setHint("재료명");
                String ingTag = "ing_" + (tbLayout.getChildCount() + 1);
                ing_name.setTag(ingTag);
                ing_name.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));

                EditText ing_amt = new EditText(getContext());
                ing_amt.setHint("용량");
                String amtTag = "amt_"+(tbLayout.getChildCount()+ 1);
                ing_amt.setTag(amtTag);
                ing_amt.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));

                row.addView(ing_name);
                row.addView(ing_amt);
                tbLayout.addView(row);
            }
        });

        remove_ing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rowTag = "ing_row" + tbLayout.getChildCount();
                TableRow row = getView().findViewWithTag(rowTag);
                tbLayout.removeViewInLayout(row);
            }
        });

        add_prc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow row = new TableRow(getContext());
                TableRow.LayoutParams lp = new TableRow.LayoutParams();
                row.setLayoutParams(lp);
                row.setTag("row" + (tbLayout2.getChildCount() + 1));

                TextView prc_num = new TextView(getContext());
                prc_num.setText(""+(tbLayout2.getChildCount() + 1));
                String prcNumTag = "num_" + (tbLayout2.getChildCount() + 1);
                prc_num.setTag(prcNumTag);
                prc_num.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.1f));

                EditText prc = new EditText(getContext());
                prc.setHint("레시피를 입력하세요");
                String prcTag = "process_" + (tbLayout2.getChildCount() + 1);
                prc.setTag(prcTag);
                prc.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.7f));

                final ImageView prc_img = new ImageView(getContext());
                prc_img.setImageResource(R.drawable.ic_photo_black_24dp);
                prc_img.setTag("processImg_"+ (tbLayout2.getChildCount() + 1));
                prc_img.setLayoutParams(new TableRow.LayoutParams((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,getResources().getDisplayMetrics()), (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,getResources().getDisplayMetrics()), 0.2f));
                prc_img.setScaleType(ImageView.ScaleType.FIT_CENTER);
                prc_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(intent, PICK_FROM_ALBUM);
                        addedIV = prc_img;
                    }
                });

                row.addView(prc_num);
                row.addView(prc);
                row.addView(prc_img);
                tbLayout2.addView(row);
            }
        });

        remove_prc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rowTag = "row" + tbLayout2.getChildCount();
                TableRow row = getView().findViewWithTag(rowTag);
                tbLayout2.removeViewInLayout(row);
            }
        });


        food_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
                addedIV = food_img;
            }
        });

        first_prc_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
                addedIV = first_prc_img;
            }
        });


        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String in_storage = "in_storage";
                String doc_name = ""+GetRecipe.getInstance().recipeInfo.size();
                String recipe_code = "u"+(Integer.parseInt(doc_name) + 1);

                //Store Recipe info into recipe_info document
                Map<String, Object> recipe_info = new HashMap<>();

                recipe_info.put("recipe_code", recipe_code);
                recipe_info.put("recipe_img", in_storage);
                recipe_info.put("recipe_name", ((EditText)getView().findViewById(R.id.user_recipe_name)).getText().toString());
                recipe_info.put("recipe_info", ((EditText)getView().findViewById(R.id.user_recipe_info)).getText().toString());


                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference info_ref = storageRef.child(recipe_code + ".png");
                StorageReference info_img_ref = storageRef.child("images/"+recipe_code + ".png");
                info_ref.getName().equals(info_img_ref.getName());

                Bitmap bm = ((BitmapDrawable)food_img.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = info_ref.putBytes(data);

                db.collection("recipes_info").document(recipe_code).set(recipe_info);

                //Store Recipe ingredient into recipe_ingredient document
                for(int i = 0; i < tbLayout.getChildCount(); i++){
                    Map<String, Object> recipe_ing = new HashMap<>();
                    String doc_name2 = "u"+ (GetRecipe.getInstance().recipeIngredient.size() + i);
                    recipe_ing.put("recipe_code", recipe_code);
                    recipe_ing.put("recipe_ing_name", ((EditText)getView().findViewWithTag("ing_"+(i+1))).getText().toString());
                    recipe_ing.put("recipe_ing_amount", ((EditText)getView().findViewWithTag("amt_"+(i+1))).getText().toString());

                    db.collection("recipes_ing").document(doc_name2).set(recipe_ing);
                }

                for(int i = 0; i < tbLayout2.getChildCount(); i++) {
                    Map<String, Object> recipe_pr = new HashMap<>();
                    String doc_name3 = "u"+ (GetRecipe.getInstance().recipeProcess.size() + i);
                    recipe_pr.put("recipe_code", recipe_code);
                    recipe_pr.put("recipe_pr", ((EditText)getView().findViewWithTag("process_"+(i+1))).getText().toString());


                    ImageView pr_img_res = (ImageView)getView().findViewWithTag("processImg_"+(i+1));

                    StorageReference storageRef2 = FirebaseStorage.getInstance().getReference();
                    StorageReference pr_ref = storageRef.child(doc_name3 + ".png");
                    StorageReference pr_img_ref = storageRef.child("images/"+doc_name3 + ".png");
                    pr_ref.getName().equals(pr_img_ref.getName());

                    Bitmap bm2 = ((BitmapDrawable)pr_img_res.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                    bm2.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data2 = baos.toByteArray();
                    UploadTask uploadTask2 = pr_ref.putBytes(data);


                    recipe_pr.put("recipe_pr_img", in_storage);
                    recipe_pr.put("recipe_pr_num", ((TextView)getView().findViewWithTag("num_"+(i+1))).getText().toString());

                    db.collection("recipes_pr").document(doc_name3).set(recipe_pr);
                }

                GetRecipe.getInstance().GetRecipeInfo();
                GetRecipe.getInstance().GetRecipeAllInfo();
                Frag_recipes goBack = new Frag_recipes();
                ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, goBack).addToBackStack(null).commit();

                Toast.makeText(getContext(), "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_FROM_ALBUM){
            Uri photoUri = data.getData();
            Cursor cursor = null;

            try{
                String[] proj = {MediaStore.Images.Media.DATA};

                assert photoUri != null;
                cursor = getActivity().getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));
            } finally {
                if(cursor != null){
                    cursor.close();
                }
            }

            setImage(addedIV);
        }
    }

    public void setImage(ImageView iv){
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        iv.setImageBitmap(originalBm);
    }

    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(getContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }



}
