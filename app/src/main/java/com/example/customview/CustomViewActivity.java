package com.example.customview;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.example.R;
import com.example.base.BaseActivity;
import com.example.databinding.ActivityCustomviewBinding;

import java.util.ArrayList;
import java.util.List;

public class CustomViewActivity extends BaseActivity<ActivityCustomviewBinding> {
    final static String TAG = CustomViewActivity.class.getSimpleName();
    private int progress = 0;
    List<Integer> stepSeconds;

    @Override
    protected int getLayout() {
        return R.layout.activity_customview;
    }
    @Override
    protected void bindView() {
        stepSeconds = new ArrayList<>();
        stepSeconds.add(2);
        stepSeconds.add(2);
        stepSeconds.add(2);
        stepSeconds.add(2);
        stepSeconds.add(2);

        stepSeconds.add(2);
        stepSeconds.add(2);
        stepSeconds.add(4);
        stepSeconds.add(4);
        stepSeconds.add(4);
        stepSeconds.add(4);
        stepSeconds.add(4);

        binding.timePb.setStep(9,stepSeconds);

        updateAlarmMinuteView(mBuilder.toString());
    }

    public void click(View v){
        Log.i(TAG, "click: ");
        switch (v.getId()){
            case R.id.btn_reset:
                progress = 0;
                binding.timePb.setCurrentPos(progress);
                break;
            case R.id.btn_next:
                if(progress>20) {
                    binding.timePb.setCurrentPos(progress);
                    progress++;
                }
                break;
            case R.id.btn_del:
                binding.ntText.del();

                if(mBuilder.length() > 0){
                    mBuilder.deleteCharAt(mBuilder.length() - 1);
                    updateAlarmMinuteView(mBuilder.toString());
                }
                break;
            case R.id.btn_append:
                int max = 57,min = 48;
                int random = (int) (Math.random()*(max-min)+min);

                binding.ntText.appCode(random);

                mBuilder.appendCodePoint(random);
                if(mBuilder.length() > numbLen){
                    mBuilder.deleteCharAt(0);
                }
                updateAlarmMinuteView(mBuilder.toString());
                break;
            case R.id.btn_show:
                translateVisibility(binding.ntText,true,true);
                break;
            case R.id.btn_hide:
                translateVisibility(binding.ntText,false,false);
                break;
        }
    }
    private void translateVisibility(View view,boolean visible,boolean downup){
        // 下降 fY -0.5f tY 0
        // 上升 fY 0 tY -0.5f
        float fY = downup ? -0.5f : 0.0f;
        float tY = downup ? 0.0f : 0.5f;
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f,
                Animation.RELATIVE_TO_SELF,0.0f,
                Animation.RELATIVE_TO_SELF,fY,
                Animation.RELATIVE_TO_SELF,tY);
        animation.setDuration(400);
        view.startAnimation(animation);
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    private StringBuilder mBuilder = new StringBuilder();
    private final int numbLen = 4;

    private void updateAlarmMinuteView(String text){
        String format = String.format("%0" + numbLen + "d", Integer.parseInt(TextUtils.isEmpty(text) ? "0": text));
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(format).append("\b");

        int lastIndex = leftZeroIndex(format);

        Log.i(TAG, "updateAlarmMinuteView: "+format+","+lastIndex);
        for(int i=0; i < format.length() ; i++){
            NumSpan hotSpan = new NumSpan(getApplicationContext(), lastIndex < i ? R.color.numColor:R.color.zeroColor);
            hotSpan.setRightMarginDpValue(10);
            Log.i(TAG, "updateAlarmMinuteView: "+i+","+(i+1));
            builder.setSpan(hotSpan, i, i+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        binding.tvNumb.setText(builder);
    }
    private int leftZeroIndex(String str){
        int lastIndex = -1;
        char[] chars = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {
            if(chars[i] != '0') break;
            lastIndex = i;
        }
        return lastIndex;
    }
}
