package com.letion.green_dao.inputs;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.letion.green_dao.R;

/**
 * <p>
 *
 * @author wuqi
 * @describe ...
 * @date 2018/9/8 0008
 */
public class ChatInputView extends LinearLayout implements View.OnClickListener, TextWatcher {
    private EditText mChatInput;
    private ImageButton mEmojiBtn, mSendBtn;

    private OnMenuItemClickListener mListener;

    public ChatInputView(Context context) {
        this(context, null);
    }

    public ChatInputView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.layout_input_view, this);

        mChatInput = findViewById(R.id.aurora_et_chat_input);
        mEmojiBtn = findViewById(R.id.aurora_menuitem_ib_emoji);
        mSendBtn = findViewById(R.id.aurora_menuitem_ib_send);

        mChatInput.addTextChangedListener(this);
        mEmojiBtn.setOnClickListener(this);
        mSendBtn.setOnClickListener(this);

        mChatInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage();
                }
                return false;
            }
        });

    }

    public void setMenuClickListener(OnMenuItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.aurora_menuitem_ib_emoji) {
            if (mListener != null) {
                mListener.switchToEmojiMode();
            }
        } else if (v.getId() == R.id.aurora_menuitem_ib_send) {
            if (mListener != null) {
                sendMessage();
            }
        }
    }

    private void sendMessage() {
        if (mListener.onSendTextMessage(mChatInput.getText().toString())) {
            mChatInput.setText("");
            mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable
                    .aurora_menuitem_send));
        }
    }

    public EditText getmChatInput() {
        return mChatInput;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() >= 1 && start == 0 && before == 0) {
            triggerSendButtonAnimation(mSendBtn, true);
        } else if (s.length() == 0 && before >= 1) {
            triggerSendButtonAnimation(mSendBtn, false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * Trigger send button animation
     *
     * @param sendBtn    send button
     * @param hasContent EditText has content or photos have been selected
     */
    private void triggerSendButtonAnimation(final ImageButton sendBtn, final boolean hasContent) {
        float[] shrinkValues = new float[]{0.6f};
        AnimatorSet shrinkAnimatorSet = new AnimatorSet();
        shrinkAnimatorSet.playTogether(ObjectAnimator.ofFloat(sendBtn, "scaleX", shrinkValues),
                ObjectAnimator.ofFloat(sendBtn, "scaleY", shrinkValues));
        shrinkAnimatorSet.setDuration(100);

        float[] restoreValues = new float[]{1.0f};
        final AnimatorSet restoreAnimatorSet = new AnimatorSet();
        restoreAnimatorSet.playTogether(ObjectAnimator.ofFloat(sendBtn, "scaleX", restoreValues),
                ObjectAnimator.ofFloat(sendBtn, "scaleY", restoreValues));
        restoreAnimatorSet.setDuration(100);

        restoreAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    requestLayout();
                    invalidate();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        shrinkAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (hasContent) {
                    mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable
                            .aurora_menuitem_send_pres));
                } else {
                    mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable
                            .aurora_menuitem_send));
                }
                restoreAnimatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        shrinkAnimatorSet.start();
    }
}
