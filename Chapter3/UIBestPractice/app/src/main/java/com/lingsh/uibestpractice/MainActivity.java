package com.lingsh.uibestpractice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Msg> msgList = new ArrayList<>();
    private TextView mInputText;
    private Button mSendButton;
    private MsgAdapter mMsgAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMsgs();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.msg_recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);

        mMsgAdapter = new MsgAdapter(msgList);
        mRecyclerView.setAdapter(mMsgAdapter);

        mInputText = findViewById(R.id.input_text);

        mSendButton = findViewById(R.id.send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mInputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SEND);
                    msgList.add(msg);
                    // 当有新消息时， 刷新RecyclerView中显示
                    mMsgAdapter.notifyItemInserted(msgList.size() - 1);
                    // 将RecyclerView定位到最后一行
                    mRecyclerView.scrollToPosition(msgList.size() - 1);
                    // 清空输入框
                    mInputText.setText("");
                }
            }
        });

    }

    private void initMsgs() {
        String dialog1 = "Hello guy.";
        String dialog2 = "Hello. Who is that?";
        String dialog3 = "This is Tom. Nice talking to you!";

        Msg msg1 = new Msg(dialog1, Msg.TYPE_RECEIVED);
        Msg msg2 = new Msg(dialog2, Msg.TYPE_SEND);
        Msg msg3 = new Msg(dialog3, Msg.TYPE_RECEIVED);

        msgList.add(msg1);
        msgList.add(msg2);
        msgList.add(msg3);
    }
}
