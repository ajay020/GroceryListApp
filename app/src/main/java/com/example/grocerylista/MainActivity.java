package com.example.grocerylista;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;

    private EditText mEditTextName;
    private Button mBtnIncrease,mBtnDecrease,mBtnAdd;
    private RecyclerView mRecyclerView;
    private TextView mTextviewAmount;
    private int mAmount = 0;
    private GroceryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GroceryDBHelper dbHelper = new GroceryDBHelper(this);
         mDatabase = dbHelper.getWritableDatabase();

        mTextviewAmount  = findViewById(R.id.tv_quantity);
        mEditTextName = findViewById(R.id.et_name_field);
        mBtnAdd = findViewById(R.id.btn_add);
        mBtnDecrease = findViewById(R.id.btn_decrease);
        mBtnIncrease = findViewById(R.id.btn_increase);

        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GroceryAdapter(this,getAllItems());
        mRecyclerView.setAdapter(mAdapter);

      new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
              ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
          @Override
          public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
              return false;
          }

          @Override
          public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

              removeItem((long)viewHolder.itemView.getTag());
              Toast.makeText(MainActivity.this, "item deleted", Toast.LENGTH_SHORT).show();

          }
      }).attachToRecyclerView(mRecyclerView);

        mBtnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increase();
            }
        });

        mBtnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrease();
            }
        });

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

    }

    private void removeItem(long id) {
        mDatabase.delete(GroceryContract.GroceryEntry.TABLE_NAME, GroceryContract.GroceryEntry._ID + "=" + id,null );
        mAdapter.swapCursor(getAllItems());
    }

    private void addItem() {

        if(mAmount == 0 || mEditTextName.getText().toString().trim().length() ==  0){
           return;
        }

        String name = mEditTextName.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(GroceryContract.GroceryEntry.COLUMN_NAME,name);
        cv.put(GroceryContract.GroceryEntry.COLUMN_AMOUNT,mAmount);

        mDatabase.insert(GroceryContract.GroceryEntry.TABLE_NAME,null,cv);
        mAdapter.swapCursor(getAllItems());
        mEditTextName.getText().clear();

    }

    private void decrease() {

        if(mAmount > 0) {
            mAmount--;
            mTextviewAmount.setText(String.valueOf(mAmount));
        }
    }

    private void increase() {

        mAmount++;
        mTextviewAmount.setText(String.valueOf(mAmount));
    }


    private Cursor getAllItems(){

        return mDatabase.query(
                GroceryContract.GroceryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                GroceryContract.GroceryEntry.COLUMN_TIMESTAMP + " DESC "
        );
    }

}
