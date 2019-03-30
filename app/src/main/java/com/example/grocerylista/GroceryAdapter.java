package com.example.grocerylista;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public GroceryAdapter(Context mContext, Cursor mCursor) {
        this.mContext = mContext;
        this.mCursor = mCursor;
    }



    @NonNull
    @Override
    public GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.row_list_item, viewGroup, false);

        return new GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryViewHolder holder, int position) {

        if(!mCursor.moveToPosition(position)){
            return;
        }

        String itemName  = mCursor.getString(mCursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_NAME));
        int amount  = mCursor.getInt(mCursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_AMOUNT));
        long id = mCursor.getLong(mCursor.getColumnIndex(GroceryContract.GroceryEntry._ID));

        if(position % 2 ==  0){
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        holder.mItemName.setText(itemName);
        holder.mItemQuantity.setText(String.valueOf(amount));
        holder.itemView.setTag(id);


    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){

        if(mCursor != null){
            mCursor.close();
        }

        mCursor = newCursor;

        if(newCursor != null){
            notifyDataSetChanged();
        }

    }


    public class GroceryViewHolder extends RecyclerView.ViewHolder{

        public TextView mItemName,mItemQuantity;

        public GroceryViewHolder(@NonNull View itemView) {
            super(itemView);

            mItemName = itemView.findViewById(R.id.tv_item_name);
            mItemQuantity = itemView.findViewById(R.id.tv_item_quantity);

        }
    }
}
