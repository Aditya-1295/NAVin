package com.navin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.navin.Location;
import com.navin.Mall;
import com.navin.Store;

import java.util.ArrayList;
import java.util.Map;

import static com.navin.navigation.currShopName;
import static com.navin.navigation.endpoint;
import static com.navin.navigation.zl;

public class PinchZoomPan extends View{

    static Mall main_mall;
    static  double imagewidth ,imageheight;
    static Location curr_s_node;

    public PinchZoomPan(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(touched);
    }
    OnTouchListener touched = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            double xxx = event.getX();
            double yyy = event.getY();

            Store temp = null;

            for(Store i :main_mall.floors.get(0).stores ) {
                if(Math.sqrt(Math.pow(i.X-xxx,2)+Math.pow(i.Y-yyy,2))<40)
                    temp = i;
            }
            if(temp==null)
                Log.e("STORE","NO STORE FOUND");
            else {
                for(Location i : main_mall.floors.get(0).path_nodes){
                    if(i.stores.contains(temp)) {
                        curr_s_node=i;
                        currShopName.setText(temp.ID);
                    }
                }
            }
            return false;// badme true ki backchodi karna
        }
    };


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(main_mall==null)return;
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setStrokeWidth(50);
        p.setColor(Color.BLACK);

        Bitmap bitImage = BitmapFactory.decodeByteArray(main_mall.floors.get(0).background_map,0,main_mall.floors.get(0).background_map.length);
        setLayoutParams(new FrameLayout.LayoutParams(bitImage.getWidth(),bitImage.getHeight()));

        canvas.drawBitmap(bitImage,0,0,p);

        for(Store i :main_mall.floors.get(0).stores ){
            if(navigation.mappings==null) {
                p.setColor(Color.parseColor("#" + i.color.substring(2, 8)));// # is important !!! colour stats from #
                canvas.drawCircle((float) i.X, (float) i.Y, 20, p);
            }

            else{
                String tag_entered = navigation.tagbox.getText().toString().toLowerCase();
                for(Map.Entry<String,String[]> z : navigation.mappings.entrySet()){
                    if(z.getKey().equals(i.ID)){

                        for(int l = 0; l<z.getValue().length;l++){
                            if(z.getValue()[l].contains(tag_entered)){
                                p.setColor(Color.parseColor("#" + i.color.substring(2, 8)));// # is important !!! colour stats from #
                                canvas.drawCircle((float) i.X, (float) i.Y, 20, p);
                            }
                        }
                    }
                }
            }
        }
        if (navigation.path != null) {
            for (int i = 0; i < navigation.path.size(); i++) {
                p.setColor(Color.parseColor("#51B5FF"));
                canvas.drawCircle((float) navigation.path.get(i).X, (float) navigation.path.get(i).Y, 10, p);
                p.setColor(Color.parseColor("#FFFFFF"));
                canvas.drawCircle((float) navigation.path.get(i).X, (float) navigation.path.get(i).Y, 3, p);
            }
        }
    }
}