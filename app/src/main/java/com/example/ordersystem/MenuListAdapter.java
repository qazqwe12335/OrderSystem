package com.example.ordersystem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

//關於商品項目的清單建立
public class MenuListAdapter extends ArrayAdapter {

    Context context;
    List<MenuListModel> menuListModelList;

    public MenuListAdapter(@NonNull Context context, List<MenuListModel> Mlist) {
        super(context, R.layout.menu_listview, R.id.commodity_name, Mlist);

        this.context = context;
        this.menuListModelList = Mlist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        listviewholder listviewholder = null;

        //清單中需要的內容
        //1.商品項目名稱
        //2.大杯價格
        //3.中杯價格

        if (v == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.menu_listview, parent, false);
            listviewholder = new listviewholder(v);
            v.setTag(listviewholder);
        } else {
            listviewholder = (listviewholder) v.getTag();
        }
        MenuListModel menuListModel = menuListModelList.get(position);

        listviewholder.name.setText(menuListModel.getItemName());
        listviewholder.med_cup.setText(String.valueOf(menuListModel.getMPrice()));
        listviewholder.large_cup.setText(String.valueOf(menuListModel.getLPrice()));

        //Toast.makeText(context, listviewholder.name.getText(), Toast.LENGTH_SHORT).show();

        com.example.ordersystem.listviewholder finalListviewholder = listviewholder;
        v.setOnClickListener(new View.OnClickListener() {

            //點擊後進入 選擇細項頁面(甜度 冰塊...等)
            @Override
            public void onClick(View v) {
                Intent chooseIntent = new Intent(context, ChooseActivity.class);
                chooseIntent.putExtra("CommodityName", finalListviewholder.name.getText());
                chooseIntent.putExtra("CommodityLcup", finalListviewholder.large_cup.getText());
                chooseIntent.putExtra("CommodityMcup", finalListviewholder.med_cup.getText());
                context.startActivity(chooseIntent);
            }
        });

        return v;
    }

}
