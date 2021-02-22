package com.moondroid.project01_meetingapp.page_tab1_info;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.variableobject.ItemBaseVO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class OptionModifyActivity extends AppCompatActivity {
    final int REQUEST_CODE_FOR_INTRO_IMG_SELECT = 0;
    final int REQUEST_CODE_FOR_INTEREST_ICON = 1;

    Toolbar toolbar;
    ImageView imageViewIntro;
    ImageView imageViewIcon;
    TextView textViewTitle;
    TextView textViewMessage;

    Uri introImgUri = null;
    String interest;
    String iconUrl;
    String meetName;
    EditText editText;

    boolean meetNameIsChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_modify);

        //xml Reference
        toolbar = findViewById(R.id.toolbar_modify_activity);
        imageViewIntro = findViewById(R.id.iv_page_modify_intro_image);
        imageViewIcon = findViewById(R.id.iv_page_modify_interest_icon);
        textViewTitle = findViewById(R.id.tv_page_modify_title);
        textViewMessage = findViewById(R.id.tv_page_modify_message);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (G.currentItemDetail.introImgUrl != null)
            Glide.with(this).load(G.currentItemDetail.introImgUrl).into(imageViewIntro);

        interest = G.currentItemBase.meetInterest;

        String interest = G.currentItemBase.meetInterest;
        ArrayList<String> interests = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.interest_list)));
        Glide.with(this).load(getResources().getStringArray(R.array.interest_icon_img_url)[interests.indexOf(interest)]).into(imageViewIcon);

        textViewTitle.setText(G.currentItemBase.meetName);

        textViewMessage.setText(G.currentItemDetail.message);
    }

    public void clickIntro(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_FOR_INTRO_IMG_SELECT);
    }

    public void clickInterestIcon(View view) {
        Intent intent = new Intent(this, InterestActivity.class);
        intent.putExtra("sendClass", "Modify");
        startActivityForResult(intent, REQUEST_CODE_FOR_INTEREST_ICON);
    }

    public void clickTitle(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view1 = getLayoutInflater().inflate(R.layout.view_for_modify_title_edit, null);
        editText = view1.findViewById(R.id.edit_modify_title);
        editText.setText(G.currentItemBase.meetName);
        builder.setTitle("모임명 설정").setView(view1).setNegativeButton("저장 안함", null).setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                meetName = editText.getText().toString();
                textViewTitle.setText(meetName);
                if (!editText.getText().toString().equals(G.currentItemBase.meetName))
                    meetNameIsChanged = true;
            }
        }).create().show();
    }

    public void clickMessage(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view1 = getLayoutInflater().inflate(R.layout.view_for_modify_message_edit, null);
        editText = view1.findViewById(R.id.edit_modify_title);
        if (G.currentItemDetail != null) editText.setText(G.currentItemDetail.message);
        builder.setTitle("모임명 설정").setView(view1).setNegativeButton("저장 안함", null).setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textViewMessage.setText(editText.getText().toString());
            }
        }).create().show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CODE_FOR_INTRO_IMG_SELECT:
                introImgUri = data.getData();
                Glide.with(this).load(introImgUri).into(imageViewIntro);
                break;
            case REQUEST_CODE_FOR_INTEREST_ICON:
                interest = data.getStringExtra("interest");
                iconUrl = data.getStringExtra("iconUrl");
                Glide.with(this).load(iconUrl).into(imageViewIcon);
                break;

        }
    }

    public void clickSave(View view) {
        G.currentItemBase.meetInterest = interest;
        G.currentItemDetail.message = textViewMessage.getText().toString();
        StorageReference introImgRef = FirebaseStorage.getInstance().getReference("introImgs/" + new SimpleDateFormat("yyyyMMddHHssmm").format(new Date()) + ".png");
        if (introImgUri != null) {
            introImgRef.putFile(introImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    introImgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            G.currentItemDetail.introImgUrl = uri.toString();

                            if (meetNameIsChanged) {
                                G.itemsRef.child(meetName).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                    @Override
                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null) {
                                            Toast.makeText(OptionModifyActivity.this, "중복된 모임 이름이 있습니다.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            G.itemsRef.child(meetName).child("base").setValue(new ItemBaseVO(meetName, G.currentItemBase.meetLocation, G.currentItemBase.purposeMessage, G.currentItemBase.titleImgUrl, G.currentItemBase.meetInterest)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    G.itemsRef.child(meetName).child("detail").setValue(G.currentItemDetail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            G.itemsRef.child(meetName).child("members").setValue(G.currentItemMember).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    G.itemsRef.child(G.currentItemBase.meetName).removeValue();
                                                                    G.currentItemBase.meetName = meetName;
                                                                    G.currentItem.setItemBaseVO(G.currentItemBase);
                                                                    G.currentItem.setItemDetailVO(G.currentItemDetail);
                                                                    G.currentItem.setItemMemberVO(G.currentItemMember);
                                                                    Toast.makeText(OptionModifyActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                                                                    finish();
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
                            } else {
                                G.itemsRef.child(G.currentItemBase.meetName).child("base").setValue(G.currentItemBase).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        G.itemsRef.child(G.currentItemBase.meetName).child("detail").setValue(G.currentItemDetail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                G.itemsRef.child(G.currentItemBase.meetName).child("members").setValue(G.currentItemMember).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        G.currentItem.setItemBaseVO(G.currentItemBase);
                                                        G.currentItem.setItemDetailVO(G.currentItemDetail);
                                                        G.currentItem.setItemMemberVO(G.currentItemMember);
                                                        Toast.makeText(OptionModifyActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
            });
        } else {
            if (meetNameIsChanged) {
                G.itemsRef.child(meetName).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            Toast.makeText(OptionModifyActivity.this, "중복된 모임 이름이 있습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            G.itemsRef.child(meetName).child("base").setValue(new ItemBaseVO(meetName, G.currentItemBase.meetLocation, G.currentItemBase.purposeMessage, G.currentItemBase.titleImgUrl, G.currentItemBase.meetInterest)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    G.itemsRef.child(meetName).child("detail").setValue(G.currentItemDetail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            G.itemsRef.child(meetName).child("members").setValue(G.currentItemMember).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    G.itemsRef.child(G.currentItemBase.meetName).removeValue();
                                                    G.currentItemBase.meetName = meetName;
                                                    G.currentItem.setItemBaseVO(G.currentItemBase);
                                                    G.currentItem.setItemDetailVO(G.currentItemDetail);
                                                    G.currentItem.setItemMemberVO(G.currentItemMember);
                                                    Toast.makeText(OptionModifyActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            } else {
                G.itemsRef.child(G.currentItemBase.meetName).child("base").setValue(G.currentItemBase).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        G.itemsRef.child(G.currentItemBase.meetName).child("detail").setValue(G.currentItemDetail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                G.itemsRef.child(G.currentItemBase.meetName).child("members").setValue(G.currentItemMember).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        G.currentItem.setItemBaseVO(G.currentItemBase);
                                        G.currentItem.setItemDetailVO(G.currentItemDetail);
                                        G.currentItem.setItemMemberVO(G.currentItemMember);
                                        Toast.makeText(OptionModifyActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }

    }
}



