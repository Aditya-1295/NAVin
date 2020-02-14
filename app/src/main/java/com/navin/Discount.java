package com.navin;

import android.app.ListActivity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Discount extends AppCompatActivity {

    private ListView listView;
    private String countryNames[] = {
            "Maggi",
            "Kathi Junction",
            "Coffee Day",
            "Quenc",
            "no"
    };

    private String capitalNames[] = {
            "50% off",
            "50% off",
            "50% off",
            "50% off",
            "50% off"
    };


    private Integer imageid[] = {
            R.drawable.maggi,
            R.drawable.kj2,
            R.drawable.ccd,
            R.drawable.quench,
            R.drawable.kj

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);

        // Setting header
        TextView textView = new TextView(this);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setText("List of Restaurants");

        listView=findViewById(R.id.listD);

        // For populating list data
       DiscountList customCountryList = new DiscountList(this, countryNames, capitalNames, imageid);
       listView.setAdapter(customCountryList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getApplicationContext(),"You Selected "+countryNames[position-1]+ " as Restaurant",Toast.LENGTH_SHORT).show();        }
        });
    }
}




