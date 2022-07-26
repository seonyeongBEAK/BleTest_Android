package com.example.adutest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MainActivity extends AppCompatActivity {

    ExtendedFloatingActionButton btn_edit, btn_control;
    Button btn_shop, button_visible;


    //숨겨진 페이지가 열렸는지 확인하는 변수
    boolean isPageOpen = false;
    LinearLayout page;
    ImageButton instagram, youtube;
    Animation fadeIn;
    Animation fadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btn_edit = findViewById(R.id.btn_edit);

        btn_edit.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, WriteActivity.class)));
        btn_control = findViewById(R.id.btn_control);
        btn_control.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ControlActivity.class)));
        btn_shop = findViewById(R.id.btn_shop);
        btn_shop.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.xn--hu1bz7nj6gp2c.com/"))));
        instagram = findViewById(R.id.instagram);
        instagram.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.instagram.com/a_du_jung_bok/?hl=ko"))));
        youtube = findViewById(R.id.youtube);
        youtube.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://youtube.com/channel/UCIK0pD42mcReMDo5HZLJgtA"))));

        page = findViewById(R.id.page);

        //anim 폴더의 애니메이션을 가져와서 준비
        fadeIn = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this,R.anim.fade_out);

        //페이지 슬라이딩 이벤트가 발생했을때 애니메이션이 시작 됐는지 종료 됐는지 감지할 수 있다.
        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();

        fadeIn.setAnimationListener(animListener);
        fadeOut.setAnimationListener(animListener);

        button_visible = findViewById(R.id.button_visible);
        button_visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPageOpen){
                    page.startAnimation(fadeIn);
                }else{
                    page.setVisibility(View.VISIBLE);
                    page.startAnimation(fadeOut);
                }
            }
        });
    }


    private class SlidingPageAnimationListener implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {

        }

        public void onAnimationEnd(Animation animation){
            if(isPageOpen){
                page.setVisibility(View.INVISIBLE);

                button_visible.setText("OPEN");
                isPageOpen = false;
            }else{
                button_visible.setText("CLOSE");
                isPageOpen = true;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}