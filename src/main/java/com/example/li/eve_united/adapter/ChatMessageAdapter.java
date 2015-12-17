package com.example.li.eve_united.adapter;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.li.eve_united.R;
import com.example.li.eve_united.utils.TimeUtils;


public class ChatMessageAdapter extends CursorAdapter
{
	private LayoutInflater mInflater;
	private Context mContext;
	public ChatMessageAdapter(Context context, Cursor c) {
		this(context, c, true);
	}
	public ChatMessageAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		mContext = context;
		mInflater=LayoutInflater.from(context);
	}

	public ChatMessageAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		mContext = context;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = mInflater.inflate(R.layout.chat_msg_main, parent, false);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView date=null;
		TextView msg =null;
		if(cursor.getInt(cursor.getColumnIndex("type"))==0) {
			view.findViewById(R.id.id_chat_form_layout).setVisibility(View.VISIBLE);
			view.findViewById(R.id.id_Chat_to_layout).setVisibility(View.GONE);
			date=(TextView) view
					.findViewById(R.id.id_chat_form_msg_date);
			msg = (TextView) view
					.findViewById(R.id.id_chat_from_msg_info);
		}else {
			view.findViewById(R.id.id_Chat_to_layout).setVisibility(View.VISIBLE);
			view.findViewById(R.id.id_chat_form_layout).setVisibility(View.GONE);
			date=(TextView) view
					.findViewById(R.id.id_chat_to_msg_date);
			msg = (TextView) view
					.findViewById(R.id.id_chat_to_msg_info);
		}

		date.setText(TimeUtils.timeSimple(Long.parseLong(cursor.getString(cursor.getColumnIndex("date")))));
		msg.setText(cursor.getString(cursor.getColumnIndex("msg")));

	}
}
