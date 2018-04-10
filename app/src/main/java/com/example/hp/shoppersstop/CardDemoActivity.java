package com.example.hp.shoppersstop;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.ChangeTransform;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hp on 03-02-2018.
 */

public class CardDemoActivity extends Activity {
    private static final String TAG = "CARDDEMO";
    CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setSharedElementEnterTransition(new ChangeTransform());

        try {
        Bundle extras = getIntent().getExtras();
        int id = extras.getInt(Constants.KEY_ID);
        ListItem model = ListRecyclerView.findById(id);

         Log.i(TAG, "Mag:" + id);

        setContentView(R.layout.activity_cardview_demo);
        cardView = findViewById(R.id.card_view_item_layout);
        View innerContainer = cardView.findViewById(R.id.container_inner_item);
        innerContainer.setTransitionName(Constants.NAME_INNER_CONTAINER);

        Log.i(TAG,"Id:"+id+" name inner container:" + Constants.NAME_INNER_CONTAINER);

        TextView nam = cardView.findViewById(R.id.name_txt);
        TextView qnt = cardView.findViewById(R.id.quantity_txt);
        TextView prc = cardView.findViewById(R.id.price_txt);
        TextView wt = cardView.findViewById(R.id.weight_txt);
        TextView brd = cardView.findViewById(R.id.brand_txt);

        nam.setText(model.getName());
        qnt.setText(model.getQuant());
        prc.setText(String.valueOf(model.getPrice()));
        wt.setText(String.valueOf(model.getWeight()));
        brd.setText(String.valueOf(model.getBrand()));

            Log.i(TAG, "Model:" + model.getName());
        } catch ( NullPointerException e){
            Log.i(TAG, "mag :" + e);
        }
          catch (ArrayIndexOutOfBoundsException e){
            Log.i(TAG, "Exception :" + e);
          }
    }
}
