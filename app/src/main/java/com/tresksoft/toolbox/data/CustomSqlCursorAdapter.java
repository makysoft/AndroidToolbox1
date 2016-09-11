package com.tresksoft.toolbox.data;

import com.tresksoft.toolbox.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;

public class CustomSqlCursorAdapter extends SimpleCursorAdapter implements OnClickListener{

	private Context context;
	private Cursor currentCursor;
	private HelperDatabase dbHelper;
	
	public CustomSqlCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, HelperDatabase dbHelper){
		super(context, layout, c, from, to);
		this.currentCursor = c;
		this.context = context;
		this.dbHelper = dbHelper;
	}
	
	public View getView(int pos, View inView, ViewGroup parent){
		View v = inView;
		if (v == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.item_process, null);
		}
		this.currentCursor.moveToPosition(pos);
		
		CheckBox cBox = (CheckBox) v.findViewById(R.id.plain_cb);
		cBox.setTag(Integer.parseInt(this.currentCursor.getString(this.currentCursor.getColumnIndex(Constants.COLUMN_ID))));
		if (this.currentCursor.getString(this.currentCursor.getColumnIndex(Constants.COLUMN_SELECTED)) != null && 
				Integer.parseInt(this.currentCursor.getString(this.currentCursor.getColumnIndex(Constants.COLUMN_SELECTED))) != 0) {
			cBox.setChecked(true);
		}else{
			cBox.setChecked(false);
		}
		cBox.setOnClickListener(this);
		
		return (v);
	}
	
	public void onClick(View v) {
		CheckBox cBox = (CheckBox) v;
		Integer _id = (Integer) cBox.getTag();
		
		ContentValues values = new ContentValues();
		values.put(" selected", cBox.isChecked() ? 1: 0);
		this.dbHelper.dbSqlite.update(Constants.TBL_NAME, values, "id=?", new String[] {Integer.toString(_id)});
	}
}
