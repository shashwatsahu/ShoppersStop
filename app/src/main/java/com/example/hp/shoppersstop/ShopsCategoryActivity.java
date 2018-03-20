package com.example.hp.shoppersstop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShopsCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_category);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.shops_cat_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle(R.string.shop_category);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ArrayList<ShopCategoryItem> arrayList = new ArrayList<>();
        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Automobile shop"));
        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Auto-parts shop"));

        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Bakery shop"));
        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Barber shop"));
        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Book shop"));

        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Clothing"));
        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Cosmetics"));

        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Dairy shop"));
        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Devotional souvenir shop"));
        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "DVDs shop"));

        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Electronic Appliances"));

        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Flower shop"));
        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Food shop"));

        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Gift shop"));
        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Grocery"));

        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Jewellery shop"));
        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Juice shop"));

        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Kiosks"));

       // arrayList.add(new ShopCategoryItem(R.mipmap.ic_launcher, "Mall"));
        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Medical shop"));

        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Pet shop"));
        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Petrol station"));

        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Saloon"));
        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Shoe shop"));
        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Souvenir"));
        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Stationary shop"));
        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "SuperMarket"));

        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Tea shop"));

        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Vegetable shop"));



        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Grocery"));
        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Grocery"));
        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Grocery"));
        arrayList.add(new ShopCategoryItem(getDrawable(R.mipmap.ic_launcher), "Grocery"));

        GridView gridview =  findViewById(R.id.grid_view_id);
        gridview.setAdapter(new ShopCategoryAdapter(this,arrayList));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(ShopsCategoryActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ShopsCategoryActivity.this, ShopsActivity.class);

                switch (position){

                    case 0:
                        intent.putExtra(Constants.SHOP_KEY, "automobile");
                        startActivity(intent);

                        break;
                    case 1:
                        intent.putExtra(Constants.SHOP_KEY, "autoparts");
                        startActivity(intent);

                        break;
                    case 2:
                        intent.putExtra(Constants.SHOP_KEY, "bakery");
                        startActivity(intent);

                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        break;
                    case 9:
                        break;
                    case 10:
                        break;
                    case 11:
                        break;
                    case 12:
                        break;
                    case 13:
                        intent.putExtra(Constants.SHOP_KEY, "gift");
                        startActivity(intent);

                        break;
                    case 14:

                        intent.putExtra(Constants.SHOP_KEY, "grocery");
                        startActivity(intent);

                        break;
                    case 15:
                        break;
                    case 16:
                        break;
                    case 17:
                        break;
                    case 18:
                        break;
                    case 19:
                        break;
                    case 20:
                        break;
                    case 21:
                        break;
                    case 22:
                        break;
                    case 23:
                        break;

                }
            }
        });

    }
}
