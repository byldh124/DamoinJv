package com.moondroid.project01_meetingapp.createmeet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.account.InterestActivity;
import com.moondroid.project01_meetingapp.account.LocationChoiceActivity;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.library.RetrofitService;
import com.moondroid.project01_meetingapp.page.PageActivity;
import com.moondroid.project01_meetingapp.variableobject.ItemBaseVO;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateActivity extends AppCompatActivity {
    final int REQUEST_CODE_FOR_INTEREST = 0;
    final int REQUEST_CODE_FOR_LOCATION = 1;
    final int REQUEST_CODE_FOR_IMAGE = 2;

    String meetInterest;
    String meetLocation;
    String meetName;
    String titleImgUrl;
    String purposeMessage;

    Uri imgUri;

    String iconUrl;
    Toolbar toolbarCreateActivity;
    EditText etMeetName, etPurposeMessage;
    TextView locationInCreate;
    ImageView ivInterestChoose, ivTitleImage;

    FirebaseStorage firebaseStorage;
    StorageReference titleImgRef;

    ArrayList<String> meets;

    String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        locationInCreate = findViewById(R.id.tv_create_location);
        toolbarCreateActivity = findViewById(R.id.toolbar_create_activity);
        ivInterestChoose = findViewById(R.id.iv_choose_interest);
        etMeetName = findViewById(R.id.et_meet_name);
        etPurposeMessage = findViewById(R.id.et_purpose_message);
        ivTitleImage = findViewById(R.id.iv_title_image);

        setSupportActionBar(toolbarCreateActivity);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToInterestActivity();
            }
        }, 300);// 0.5초 정도 딜레이를 준 후 시작

    }

    public void moveToInterestActivity() {
        Intent intent = new Intent(this, InterestActivity.class);
        intent.putExtra("sendClass", "Create");
        startActivityForResult(intent, REQUEST_CODE_FOR_INTEREST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_FOR_INTEREST:
                if (resultCode == RESULT_OK) {
                    meetInterest = data.getStringExtra("interest");
                    iconUrl = data.getStringExtra("iconUrl");
                    Glide.with(this).load(iconUrl).into(ivInterestChoose);
                }
                break;
            case REQUEST_CODE_FOR_LOCATION:
                if (resultCode == RESULT_OK) {
                    meetLocation = data.getStringExtra("location");
                    locationInCreate.setText(meetLocation);
                }
                break;

            case REQUEST_CODE_FOR_IMAGE:
                if (resultCode == RESULT_OK) {
                    imgUri = data.getData();
                    if (imgUri != null) {
                        Glide.with(this).load(imgUri).into(ivTitleImage);
                        imgPath = getRealPathFromUri(imgUri);
                    }
                }
                break;
        }
    }

    public void clickSearchLocation(View view) {
        Intent intent = new Intent(this, LocationChoiceActivity.class);
        startActivityForResult(intent, REQUEST_CODE_FOR_LOCATION);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickInterestIcon(View view) {
        moveToInterestActivity();
    }

    public void clickCreateMeet(View view) {

        firebaseStorage = FirebaseStorage.getInstance();
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).concat(".png");
        titleImgRef = firebaseStorage.getReference("meetTitleImg/" + fileName);
        meetName = etMeetName.getText().toString();
        purposeMessage = etPurposeMessage.getText().toString();

        MultipartBody.Part filePart = null;

        if (imgPath != null){
            File file = new File(imgPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            filePart = MultipartBody.Part.createFormData("img", file.getName(), requestBody);
        }

        RetrofitService retrofitService = RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class);
        retrofitService.saveItemBaseDataToCreateActivity(new ItemBaseVO(meetName, meetLocation, purposeMessage, null, meetInterest), filePart).enqueue(new Callback<ItemBaseVO>() {
            @Override
            public void onResponse(Call<ItemBaseVO> call, Response<ItemBaseVO> response) {

            }

            @Override
            public void onFailure(Call<ItemBaseVO> call, Throwable t) {

            }
        });

        G.itemsRef.child(meetName).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    new AlertDialog.Builder(CreateActivity.this).setMessage("동일한 모임 이름이 존재합니다.\n다른 모임 이름을 작성해주세요.").setPositiveButton("확인", null).create().show();
                    return;
                } else if (meetName == null || meetName.equals("")) {
                    Toast.makeText(CreateActivity.this, "모임 이름을 설정해주세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (meetLocation == null || meetLocation.equals("")) {
                    Toast.makeText(CreateActivity.this, "모임 지역을 설정해주세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (purposeMessage == null || purposeMessage.equals("")) {
                    Toast.makeText(CreateActivity.this, "모임 목표를 작성해주세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (meetInterest == null || meetInterest.equals("")) {
                    Toast.makeText(CreateActivity.this, "관심사를 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                titleImgRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        titleImgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                titleImgUrl = uri.toString();
                                G.currentItemBase = new ItemBaseVO(meetName, meetLocation, purposeMessage, titleImgUrl, meetInterest);
                                G.currentItem.setItemBaseVO(G.currentItemBase);
                                G.itemsRef.child(meetName).child("base").setValue(G.currentItemBase).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        G.itemsRef.child(meetName).child("members").child("master").setValue(G.myProfile.userId).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                meets = new ArrayList<>();
                                                G.currentItemMember.master = G.myProfile.userId;
                                                G.usersRef.child(G.myProfile.userId).child("meets").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                            meets.add(ds.getValue(String.class));
                                                        }
                                                        meets.add(G.currentItemBase.meetName);
                                                        G.usersRef.child(G.myProfile.userId).child("meets").setValue(meets).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Intent intent = new Intent(CreateActivity.this, PageActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        });
                                                    }
                                                });

                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });


    }

    public void clickImageInput(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_FOR_IMAGE);
    }

    String getRealPathFromUri(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
}