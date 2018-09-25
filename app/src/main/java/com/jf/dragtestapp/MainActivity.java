package com.jf.dragtestapp;

import android.app.Activity;
import android.content.ClipData;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String BLUE = "BLUE";

    private TextView edt_1;
    private TextView edt_2;

    private String dragStr;

    private Vibrator vibrator;
    private int TONE_VOLUM = 100;
    private int VIBRATOR_SHORT = 40;
    private int VIBRATOR_MIDDLE = 120;
    private int VIBRATOR_LONG = 500;
    private int TONE_SHORT = 80;
    private int TONE_MIDDLE = 500;
    private ToneGenerator toneGenerator;

    View.OnDragListener listener = new View.OnDragListener() {

        private float lastX,lastY;

        @Override
        public boolean onDrag(View v, DragEvent event) {
            //v 永远是设置该监听的view，这里即fl_blue
            Log.w(BLUE, "view :"+v.getTag());
            //获取事件
            int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.i(BLUE, "开始拖拽");
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.i(BLUE, "结束拖拽");
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.i(BLUE, "拖拽的view进入监听的view时");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.i(BLUE, "拖拽的view离开监听的view时");
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    lastX = event.getX();
                    lastY = event.getY();
                    //long l = SystemClock.currentThreadTimeMillis();
                    Log.i(BLUE, "拖拽的view在监听view中的位置:x =" + lastX + ",y=" + lastY);
                    break;
                case DragEvent.ACTION_DROP:
                    Log.i(BLUE, "释放拖拽的view");
                    if("edt_2".equals(v.getTag().toString())){
                        edt_2.setText(edt_2.getText()+dragStr);
                    }
                    break;
            }
            //是否响应拖拽事件，true响应，返回false只能接受到ACTION_DRAG_STARTED事件，后续事件不会收到
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_1 = findViewById(R.id.edt_1);
        edt_1.setTag("edt_1");
        edt_2 = findViewById(R.id.edt_2);
        edt_2.setTag("edt_2");

        initVibratorAndTone();

        edt_1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(edt_1.getText() == null || edt_1.getText().length()<=0){
                    return false;
                }
//                int start = edt_1.getSelectionStart();
//                int end = edt_1.getSelectionEnd();
//                if(start < 0 || end < 0 || start == end){
//                    dragStr = "";
//                }else{
//                    dragStr = edt_1.getText().subSequence(start,end).toString();
//                }
                dragStr = ((DragTextView)edt_1).getSelectedStr();
                ClipData clipData = ClipData.newPlainText("drag","");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(edt_1);
                edt_1.startDrag(clipData,shadowBuilder,null,0);
                vibrate(VIBRATOR_SHORT);
                tone(ToneGenerator.TONE_SUP_ERROR,TONE_SHORT);
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                return false;
            }
        });
        edt_1.setOnDragListener(listener);
        edt_2.setOnDragListener(listener);
    }

    private void initVibratorAndTone() {
        try {
            vibrator = (Vibrator) getSystemService(Activity.VIBRATOR_SERVICE);
            toneGenerator = new ToneGenerator(AudioManager.STREAM_ALARM, TONE_VOLUM);
        } catch (RuntimeException e) {
            Log.e(BLUE,"Init tone & vibrator cast exception " + e.getMessage());
        }
    }

    public void vibrate(long duration) {
        if (vibrator != null) vibrator.vibrate(duration);
    }

    public void tone(int type, int length) {
        if (toneGenerator != null) toneGenerator.startTone(type, length);
    }
}
