package com.example.li.eve_united.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.example.li.eve_united.R;
import com.example.li.eve_united.adapter.ChatMessageAdapter;
import com.example.li.eve_united.bean.ChatBean;
import com.example.li.eve_united.db.ChatProvider;
import com.example.li.eve_united.service.NetService;
import com.example.li.eve_united.utils.SPUtils;

/**
 * Created by Li on 2015/11/12.
 */
public class ChatActivity extends AppCompatActivity {
    private static final int LOADER_ID=1;
    private ListView mChatListView;
    private Button mChatsend;
    private EditText mChated;
    private ChatMessageAdapter mChatAdapter;
    private MyLoaderListener mLoader = new MyLoaderListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_chat_toolbar);
        toolbar.setTitle("和你的么么哒正在……聊天");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initViews();
        getLoaderManager().initLoader(LOADER_ID,null,mLoader);
    }

    private void initViews() {
        mChatListView= (ListView) findViewById(R.id.id_chat_listview_msgs);
        mChated= (EditText) findViewById(R.id.id_chat_input_msg);
        mChatsend = (Button) findViewById(R.id.id_chat_send_msg);
        mChatAdapter=new ChatMessageAdapter(this,null,false);
        mChatListView.setAdapter(mChatAdapter);
        mChatsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mChated.getText().toString().equals("")){
                    Intent intent = new Intent(NetService.NETSERVICE_RECEIVER);
                    Bundle bundle = new Bundle();
                    String user = (String) SPUtils.get(ChatActivity.this,"toUser","xx");
                    ChatBean chatBean = new ChatBean("1",user,mChated.getText().toString());
                    bundle.putSerializable("ChatBean",chatBean);
                    intent.putExtra("ChatBean", bundle);
                    ChatActivity.this.sendBroadcast(intent);
                }
            }
        });


    }
    class MyLoaderListener implements LoaderManager.LoaderCallbacks<Cursor>{

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            CursorLoader loader = new CursorLoader(getApplicationContext(),ChatProvider.URI_CHAT_ALL,null,null,null,null);
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if(loader.getId()==LOADER_ID){
                mChatAdapter.swapCursor(data);
                mChatListView.setSelection(mChatAdapter.getCount()-1);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mChatAdapter.swapCursor(null);
        }
    }
}
