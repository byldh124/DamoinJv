package com.moondroid.project01_meetingapp.page_tab4_chatting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.library.RetrofitService;
import com.moondroid.project01_meetingapp.variableobject.ChatItemVO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChattingFragment extends Fragment {

    LinearLayout chatContainer;
    ListView listView;
    Button btnSend;
    EditText etMessage;
    ArrayList<ChatItemVO> chatItems;
    ChatAdapter chatAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference chatRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page_tab4_chatting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chatContainer = view.findViewById(R.id.chat_container);
        listView = view.findViewById(R.id.list_view_chat);
        btnSend = view.findViewById(R.id.btn_chat_send);
        etMessage = view.findViewById(R.id.et_chat_message);

        if (G.currentItemMembers.contains(G.myProfile.userId) == false) {
            chatContainer.setVisibility(View.INVISIBLE);
        }

        btnSend.setOnClickListener(onClickListener);
        chatItems = new ArrayList<>();
        chatAdapter = new ChatAdapter(getContext(), chatItems);
        listView.setAdapter(chatAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        chatRef = firebaseDatabase.getReference("chat/" + G.currentItemBase.meetName);
        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatItemVO chatItem = snapshot.getValue(ChatItemVO.class);
                chatItems.add(chatItem);
                chatAdapter.notifyDataSetChanged();
                listView.setSelection(chatItems.size() - 1);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ChatItemVO chatItemVO = new ChatItemVO(G.myProfile.userId, G.myProfile.userName, new SimpleDateFormat("MM.dd HH:mm").format(new Date()), G.myProfile.userProfileImgUrl, etMessage.getText().toString());
            chatRef.child(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())).setValue(chatItemVO).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    etMessage.setText("");
                    RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).sendFCMMessage(G.currentItemBase.meetName, G.myProfile.userId).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
            });
        }
    };
}
