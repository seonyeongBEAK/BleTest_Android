package com.example.adutest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class WriteActivity extends AppCompatActivity {
    // import한 BluetoothSPP 변수 선언
    private BluetoothSPP bt;
    TextView send_list, txt_result, txt_compile, count;
    EditText txt_send;
    MyAdapter myAdapter;
    private TextView mAutofitOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        TextView text_color = findViewById(R.id.text_color);
        send_list = findViewById(R.id.send_list);
        count = findViewById(R.id.count);
        txt_send = findViewById(R.id.txt_send);
        txt_result = findViewById(R.id.txt_result);
        txt_compile = findViewById(R.id.txt_compile);

        mAutofitOutput = findViewById(R.id.output_autofit);

        txt_send.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mAutofitOutput.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        });



        myAdapter= new MyAdapter("Data List");



        // 객체 생성 후 미리 선언한 변수에 넣음
        bt = new BluetoothSPP(this); //Initializing

        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가라면
            // 사용불가라고 토스트 띄워줌
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            // 화면 종료
            finish();
        }

        // ------------------------------ 데이터 수신부 ----------------------------- //


        //데이터 수신되면
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
            public void onDataReceived(byte[] data, String message) {
                String[] array = message.split(",");
                txt_result.setText(array[0].concat("."));

//                for(int i = 0; i<data.length; i++) {
//                    txt_result.setText(array[i].concat("\n"));
//
////                    txt_result.append(array[i]);
//                    Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
//                }
            }
        });


        // 블루투스가 잘 연결이 되었는지 감지하는 리스너
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "연결 성공 " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
                text_color.setBackgroundColor(Color.parseColor("#720040FF"));
            }

            public void onDeviceDisconnected() { //연결해제
                   Toast.makeText(getApplicationContext()
                        , "연결 해제", Toast.LENGTH_SHORT).show();
                text_color.setBackgroundColor(Color.parseColor("#72EFE394"));
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "연결 실패", Toast.LENGTH_SHORT).show();
                text_color.setBackgroundColor(Color.parseColor("#72FF0000"));
            }
        });
        // 연결하는 기능 버튼 가져와서 이용하기
//        ImageButton btnConnect = findViewById(R.id.btn_connect); //연결시도
//        // 버튼 클릭하면
//        btnConnect.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) { // 현재 버튼의 상태에 따라 연결이 되어있으면 끊고, 반대면 연결
//                    bt.disconnect();
//                } else {
//                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
//                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
//                }
//            }
//        });


        Switch switch_connect = findViewById(R.id.switch_connect);
        switch_connect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // switchButton이 체크된 경우
                    if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) { // 현재 버튼의 상태에 따라 연결이 되어있으면 끊고, 반대면 연결
                        bt.disconnect();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                        switch_connect.setChecked(true);
                    }
                }else {
                    switch_connect.setChecked(false);
                    bt.disconnect();
                }
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) { // 현재 버튼의 상태에 따라 연결이 되어있으면 끊고, 반대면 연결
                    switch_connect.setChecked(true);
                }else{
                    switch_connect.setChecked(false);
                }
            }
        });

    }




    // 앱 중단시 (액티비티 나가거나, 특정 사유로 중단시)
    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //블루투스 중지
    }
    // 앱이 시작하면
    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) { // 앱의 상태를 보고 블루투스 사용 가능하면
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // 새로운 액티비티 띄워줌, 거기에 현재 가능한 블루투스 정보 intent로 넘겨
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) { // 블루투스 사용 불가
                // setupService() 실행하도록
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기끼리
                // 셋팅 후 연결되면 setup()으로
                setup();
            }
        }
    }

    // 블루투스 사용 - 데이터 전송
    public void setup() {

        ImageButton btnSend = findViewById(R.id.btn_send); //데이터 전송
        btnSend.setOnClickListener(v -> {
            String sendtxt = txt_send.getText().toString();

            if(sendtxt.getBytes().length <= 0){//빈값이 넘어올때의 처리
                txt_result.setText("Error: value is null");
                txt_compile.setText("업로드 에러");
            }
            else{

                if (sendtxt.equals("for(pos=160; pos>=10; pos-=6)")) {
                    bt.send(sendtxt, false);    //CRLF: 새 줄 문자(new line)라는 말로 "개행", "줄 바꿈"
                    txt_compile.setText("업로드 완료");
                    txt_result.setText("결과 데이터 없음");
                }else if (sendtxt.equals("for(pos=10; pos<=160; pos+=6)")) {
                    bt.send(sendtxt, false);    //CRLF: 새 줄 문자(new line)라는 말로 "개행", "줄 바꿈"
                    txt_compile.setText("업로드 완료");
                    txt_result.setText("결과 데이터 없음");
                }else if (sendtxt.equals("myservo.write(0);")) {
                    bt.send(sendtxt, false);    //CRLF: 새 줄 문자(new line)라는 말로 "개행", "줄 바꿈"
                    txt_compile.setText("업로드 완료");
                    txt_result.setText("결과 데이터 없음");
                }else if (sendtxt.equals("myservo.write(45);")) {
                    bt.send(sendtxt, false);    //CRLF: 새 줄 문자(new line)라는 말로 "개행", "줄 바꿈"
                    txt_compile.setText("업로드 완료");
                    txt_result.setText("결과 데이터 없음");
                }else if (sendtxt.equals("myservo.write(90);")) {
                    bt.send(sendtxt, false);    //CRLF: 새 줄 문자(new line)라는 말로 "개행", "줄 바꿈"
                    txt_compile.setText("업로드 완료");
                    txt_result.setText("결과 데이터 없음");
                }else if (sendtxt.equals("myservo.write(135);")) {
                    bt.send(sendtxt, false);    //CRLF: 새 줄 문자(new line)라는 말로 "개행", "줄 바꿈"
                    txt_compile.setText("업로드 완료");
                    txt_result.setText("결과 데이터 없음");
                }else if (sendtxt.equals("myservo.write(180);")) {
                    bt.send(sendtxt, false);    //CRLF: 새 줄 문자(new line)라는 말로 "개행", "줄 바꿈"
                    txt_compile.setText("업로드 완료");
                    txt_result.setText("결과 데이터 없음");
                }else if (sendtxt.equals("digitalWrite(LG,150);")) {
                    bt.send(sendtxt, false);    //CRLF: 새 줄 문자(new line)라는 말로 "개행", "줄 바꿈"
                    txt_compile.setText("업로드 완료");
                    txt_result.setText("결과 데이터 없음");
                }else if (sendtxt.equals("digitalWrite(RG,150);")) {
                    bt.send(sendtxt, false);    //CRLF: 새 줄 문자(new line)라는 말로 "개행", "줄 바꿈"
                    txt_compile.setText("업로드 완료");
                    txt_result.setText("결과 데이터 없음");
                }else if (sendtxt.equals("digitalWrite(LB,150);")) {
                    bt.send(sendtxt, false);    //CRLF: 새 줄 문자(new line)라는 말로 "개행", "줄 바꿈"
                    txt_compile.setText("업로드 완료");
                    txt_result.setText("결과 데이터 없음");
                }else if (sendtxt.equals("digitalWrite(RB,150);")) {
                    bt.send(sendtxt, false);    //CRLF: 새 줄 문자(new line)라는 말로 "개행", "줄 바꿈"
                    txt_compile.setText("업로드 완료");
                    txt_result.setText("결과 데이터 없음");
                }else if (sendtxt.equals("BT.println(distance);")) {
                    bt.send(sendtxt, false);    //CRLF: 새 줄 문자(new line)라는 말로 "개행", "줄 바꿈"
                    txt_compile.setText("업로드 완료");
                    txt_result.setText("결과 데이터 없음");
                }else if (sendtxt.equals("allcode")) {
                    bt.send(sendtxt, false);    //CRLF: 새 줄 문자(new line)라는 말로 "개행", "줄 바꿈"
                    txt_compile.setText("업로드 완료");
                    txt_result.setText("결과 데이터 없음");
                }else if (sendtxt.equals("clean")) {
                    bt.send(sendtxt, false);    //CRLF: 새 줄 문자(new line)라는 말로 "개행", "줄 바꿈"
                    txt_compile.setText("업로드 완료");
                    txt_result.setText("결과 데이터 없음");
                }else {
                    txt_compile.setText("업로드 실패");
                    txt_result.setText("Error: value is false");
                }
                txt_send.setText(null);
                Data dat1 = new Data(sendtxt);   //인스턴스 생성
                myAdapter.addItem(dat1);   //Student를 보낸다 스쿨에 있는 array에 들어간다 private에 접근하는 방법
                count.setText(""+myAdapter.size()+"");
            }
            String output = myAdapter.toString();
            send_list.setText(output);
        });
    }
    public  void onCompile(View view){
        String sendtxt = txt_send.getText().toString();

        if(sendtxt.equals("")){//빈값이 넘어올때의 처리
            txt_result.setText("Error: value is null");
            txt_compile.setText("컴파일 에러");
        }else {
            if (sendtxt.equals("for(pos=160; pos>=10; pos-=6)")){
                txt_compile.setText("컴파일 완료");
                txt_result.setText("true");
            }else if (sendtxt.equals("for(pos=10; pos<=160; pos+=6)")) {
                txt_compile.setText("컴파일 완료");
                txt_result.setText("true");
            }else if (sendtxt.equals("myservo.write(0);")) {
                txt_compile.setText("컴파일 완료");
                txt_result.setText("true");
            }else if (sendtxt.equals("myservo.write(45);")) {
                txt_compile.setText("컴파일 완료");
                txt_result.setText("true");
            }else if (sendtxt.equals("myservo.write(90);")) {
                txt_compile.setText("컴파일 완료");
                txt_result.setText("true");
            }else if (sendtxt.equals("myservo.write(135);")) {
                txt_compile.setText("컴파일 완료");
                txt_result.setText("true");
            }else if (sendtxt.equals("myservo.write(180);")) {
                txt_compile.setText("컴파일 완료");
                txt_result.setText("true");
            }else if (sendtxt.equals("digitalWrite(LG,150);")) {
                txt_compile.setText("컴파일 완료");
                txt_result.setText("true");
            }else if (sendtxt.equals("digitalWrite(RG,150);")) {
                txt_compile.setText("컴파일 완료");
                txt_result.setText("true");
            }else if (sendtxt.equals("digitalWrite(LB,150);")) {
                txt_compile.setText("컴파일 완료");
                txt_result.setText("true");
            }else if (sendtxt.equals("digitalWrite(RB,150);")) {
                txt_compile.setText("컴파일 완료");
                txt_result.setText("true");
            }else if (sendtxt.equals("BT.println(distance);")) {
                txt_compile.setText("컴파일 완료");
                txt_result.setText("true");
            }else if (sendtxt.equals("allcode")) {
                txt_compile.setText("컴파일 완료");
                txt_result.setText("true");
            }else if (sendtxt.equals("clean")) {
                txt_compile.setText("컴파일 완료");
                txt_result.setText("true");
            }else {
                txt_result.setText("Error: value is false");
                txt_compile.setText("컴파일 에러");
                txt_send.setText(null);
            }
        }
    }


    // 새로운 액티비티 (현재 액티비티의 반환 액티비티?)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 아까 응답의 코드에 따라 연결 가능한 디바이스와 연결 시도 후 ok 뜨면 데이터 전송
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) { // 연결시도
            if (resultCode == Activity.RESULT_OK) // 연결됨
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) { // 연결 가능
            if (resultCode == Activity.RESULT_OK) { // 연결됨
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else { // 사용불가
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void onList(View view){
//        String output = myAdapter.toString();
//        send_list.setText(output);

        finish();//인텐트 종료
        overridePendingTransition(0, 0);//인텐트 효과 없애기
        Intent intent = getIntent(); //인텐트
        startActivity(intent); //액티비티 열기
        overridePendingTransition(0, 0);//인텐트 효과 없애기

    }


}