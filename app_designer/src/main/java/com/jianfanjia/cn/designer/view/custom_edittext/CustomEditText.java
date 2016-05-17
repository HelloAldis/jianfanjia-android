package com.jianfanjia.cn.designer.view.custom_edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.designer.view.custom_edittext
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-17 11:55
 */
public class CustomEditText extends FrameLayout {

    private static final String TAG = CustomEditText.class.getName();

    private EditText inputText;

    private TextView lengthText;

    private int currentCanInputSize;

    private boolean isShowCurrentInputSize = false;

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.CustomEditText);

        isShowCurrentInputSize = a.getBoolean(R.styleable.CustomEditText_show_inputlength_limit, false);

        currentCanInputSize = a.getInt(R.styleable.CustomEditText_inputlength_limit_size,0);

        a.recycle();

        View view = inflate(context, R.layout.custom_edittext_has_length,null);

        inputText = (EditText)view.findViewById(R.id.edittext);

        lengthText = (TextView)view.findViewById(R.id.input_length);

        if(isShowCurrentInputSize){
            lengthText.setVisibility(VISIBLE);
            lengthText.setText(String.valueOf(currentCanInputSize));
        }else{
            lengthText.setVisibility(View.GONE);
        }

        InputFilter[] inputFilters = new InputFilter[]{new InputFilter.LengthFilter(currentCanInputSize)};
        inputText.setFilters(inputFilters);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addView(view);
    }

    public String getText(){
        return  inputText.getEditableText().toString();
    }

    public void addTextChangedListener(final TextWatcher textWatcher){
        if(textWatcher != null){
            inputText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    textWatcher.beforeTextChanged(s,start,count,after);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    textWatcher.onTextChanged(s,start,before,count);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    textWatcher.afterTextChanged(s);
                    if(isShowCurrentInputSize){
                        int leaveLenth = currentCanInputSize - s.toString().length();
                        LogTool.d(TAG, "leaveLength =" + leaveLenth);
                        lengthText.setText(String.valueOf(leaveLenth));
                    }
                }
            });
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if(enabled){
            if(isShowCurrentInputSize){
                lengthText.setVisibility(View.VISIBLE);
            }
        }else {
            lengthText.setVisibility(View.GONE);
        }
        inputText.setEnabled(enabled);
    }
}
