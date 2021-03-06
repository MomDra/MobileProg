package com.example.teamproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CheckListActivity extends AppCompatActivity {
    private ListView checkListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listItem;
    private String checkListString = "";
    private String checkedString = "";
    private String FILECHECKED = "file_checked.txt";
    private String FILELIST = "file_list.txt";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        checkedString = fileRead(FILECHECKED);
        checkListString = fileRead(FILELIST);
        checkListAdapt();
        AdapterView.OnItemClickListener a = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checkListView.onSaveInstanceState();
            }
        };
        checkListView.setOnItemClickListener(a);
        longClickDelete();
    }

    public void checkListAdapt(){
        String []stringCheckList = checkListString.split("/");
        String []checkedList = checkedString.split("/");
        listItem = new ArrayList<>(Arrays.asList(stringCheckList));
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, listItem);
        checkListView = findViewById(R.id.regionListView);
        checkListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        checkListView.setAdapter(adapter);
        //???????????? ?????? ????????? ??????, stringCheckList??? null??? ????????? [""]???.
        if (stringCheckList[0].equals("")){
            ArrayList <String> items = new ArrayList<String>();
            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, items);
            checkListView.setAdapter(adapter);
            return;
        }
        int i = 0;
        for(String str : stringCheckList){
            for (String strc : checkedList)
                if (str.equals(strc))
                    checkListView.setItemChecked(i,true);
            i++;
        }
    }

    public void onClickSave(View view){
        SparseBooleanArray checkedItems = checkListView.getCheckedItemPositions();
        int count = adapter.getCount() ;
        checkedString = "";
        for (int i = 0; i < count; i++) {
            if (checkedItems.get(i)) {
                String st = (String) checkListView.getAdapter().getItem(i);
                checkedString += st + "/";
            }
        }
        fileWrite(FILELIST, checkListString);
        fileWrite(FILECHECKED, checkedString);
        Toast.makeText(getApplicationContext(),"????????????", Toast.LENGTH_SHORT).show();
    }

    public void initialize(){
        //??????????????? ??????????????? ??????.
        String[] checkList = {"?????? ??????", "?????????", "???????????????", "?????????", "???????????????", "??????", "???",
                "??????", "??????", "??????", "??????", "??????", "?????????", "????????????", "????????????", "??????", "?????????", "????????????"};
        String arrayToString = String.join("/", checkList);
        fileWrite(FILELIST, arrayToString);
        fileWrite(FILECHECKED,"");
        System.out.println("????????? ?????? ??????");
        System.out.println(arrayToString);
    }

    public void longClickDelete(){
        checkListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> a_parent, View a_view, int a_position, long a_id) {
                String []stringCheckList = checkListString.split("/");
                delete(stringCheckList[a_position]);
                return true;
            }
        });
    }

    public void delete(String target){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("??????").setMessage("?????? ????????? ?????????????????????????");
        builder.setPositiveButton("???", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                checkListString = checkListString.replace(target + "/","");
                checkedString = checkedString.replace(target + "/","");
                checkListString = checkListString.replace(target,"");
                checkedString = checkedString.replace(target,"");
                checkListAdapt();
                Toast.makeText(getApplicationContext(),"????????????", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("?????????", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void onClickAppend(View view){
        EditText edittext = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("??????").setMessage("??????????????? ?????? ?????? ??????.").setView(edittext);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                String []stringCheckList = checkListString.split("/");
                String append_str = edittext.getText().toString();
                if (append_str.equals("")){
                    Toast.makeText(getApplicationContext(),"??????????????? ?????? ????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                }
                else if (append_str.contains("/")){
                    Toast.makeText(getApplicationContext(),"???????????? / ??? ?????? ??? ??? ????????????.", Toast.LENGTH_SHORT).show();
                }
                else if (Arrays.asList(stringCheckList).contains(append_str))
                    Toast.makeText(getApplicationContext(),"?????? ???????????????.", Toast.LENGTH_SHORT).show();
                else{
                    checkListString += append_str +"/";
                    checkListAdapt();
                    Toast.makeText(getApplicationContext(),append_str + " ????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    //test

    public void fileWrite(String fileName ,String toWrite){
        //?????? ??????
        try {
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(toWrite.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String fileRead(String fileName){
        String ret = "";
        //?????? ??????
        try {
            FileInputStream fis = openFileInput(fileName);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            ret = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
