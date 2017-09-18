package com.github.wanghy360.player.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.Size;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.wanghy360.player.R;
import com.github.wanghy360.player.activity.PlayerDetailActivity;
import com.github.wanghy360.player.util.InputUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.OnTouch;


/**
 * Created by Wanghy on 2017/6/13
 */
public class MagicLayout extends BaseRelativeLayout {
    //rootView
    @Bind(R.id.id_magic_root)
    RelativeLayout rootView;
    //输入框
    @Bind(R.id.id_send_chat)
    Button sendBtn;
    @Bind(R.id.id_chat_edit)
    EditText chatEdit;
    @Bind(R.id.id_chat_bottom)
    RelativeLayout bottomChatContainer;
    //工具栏开关
    private boolean showTool = true;
    //工具栏高度
    private int toolHeight;
    private boolean isLand = false;
    private boolean isKeyboardVisible = false;

    public MagicLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MagicLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MagicLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MagicLayout(Context context) {
        super(context);
    }

    public MagicLayout(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.view_magic;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        initUI();
    }

    protected void initUI() {
        chatEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        && chatEdit.getId() == v.getId()) {
                    onClickGiftSend();
                    return true;
                }
                return false;
            }
        });
        chatEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    sendBtn.setBackgroundResource(R.drawable.bg_rect_corner_black);
                } else {
                    sendBtn.setBackgroundResource(R.drawable.bg_rect_corner_press);
                }
            }
        });

    }

    public void updateOrientation(boolean isLand) {
        this.isLand = isLand;
        updateChatView(isKeyboardVisible);
        if (isLand) {
            showView(bottomChatContainer, true);
        }
    }

    public void updateKeyboardVisibility(boolean isVisible) {
        isKeyboardVisible = isVisible;
    }


    @OnClick(R.id.id_send_chat)
    public void onClickGiftSend() {//点击发送聊天
        String content = chatEdit.getText().toString();
        sendChatMsg(content);
    }

    private String lastChatMsg;
    private long currChatMillisLeft = 0;
    private CountDownTimer countDownTimer;

    private void sendChatMsg(String chatMsg) {
        if (!TextUtils.isEmpty(chatMsg)) {
            if (chatMsg.equals(lastChatMsg)) {
                Toast.makeText(getActivity(), R.string.chat_err_repeat, Toast.LENGTH_SHORT).show();
                return;
            }
            lastChatMsg = chatMsg;
            if (currChatMillisLeft > 0) {
                String msg = String.format(getResources().getString(R.string.chat_err_too_frequent), (int) Math.max(currChatMillisLeft / 1000, 1));
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                return;
            } else {
                currChatMillisLeft = 2500;
                if (countDownTimer == null) {
                    countDownTimer = new CountDownTimer(2500, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            currChatMillisLeft = millisUntilFinished;
                        }

                        @Override
                        public void onFinish() {
                            lastChatMsg = null;
                            currChatMillisLeft = 0;
                        }
                    };
                }
                countDownTimer.start();
            }

            updateChatUIAfterSend();
        }
    }


    public void updateShowTool(boolean isShow) {
        showTool = isShow;
    }

    public void setToolHeight(int toolHeight) {
        this.toolHeight = toolHeight;
    }

    public void updateChatView(boolean isKeyboardVisible) {
        if (isKeyboardVisible) {
            bottomChatContainer.setBackgroundColor(Color.WHITE);
            sendBtn.setVisibility(View.VISIBLE);
        } else {
            sendBtn.setVisibility(View.GONE);
            if (isLand) {
                bottomChatContainer.setBackgroundColor(Color.TRANSPARENT);
                if (showTool) {
                    bottomChatContainer.setTranslationY(0);
                } else {
                    bottomChatContainer.setTranslationY(toolHeight);
                }
            } else {
                bottomChatContainer.setBackgroundColor(Color.WHITE);
            }
        }
    }

    public void updateChatUIAfterSend() {
        hideSoftInputWindow();
        chatEdit.setText("");
    }

    public void showSoftInputWindow() {
        if (chatEdit != null)
            InputUtils.showSoftInputWindow(getActivity(), chatEdit);
    }

    public void hideSoftInputWindow() {
        if (chatEdit != null)
            InputUtils.hideSoftInputWindow(getActivity(), chatEdit);
    }

    public void showChatInput() {
        bottomChatContainer.animate().setInterpolator(new AccelerateInterpolator()).translationY(0).setDuration(300).start();
    }

    public void hideChatInput() {
        bottomChatContainer.animate().setInterpolator(new DecelerateInterpolator()).translationY(toolHeight).setDuration(300).start();
    }

    @OnTouch(R.id.id_chat_edit)
    public boolean onTouchChat() {//点击输入框
        onClickChatInput();
        return false;
    }

    @OnLongClick(R.id.id_chat_edit)
    public boolean onLongClickChat() {//点击输入框
        onClickChatInput();
        return false;
    }

    private void onClickChatInput() {
        if (isLand) {
            getRealActivity().hideOrShowTool(false, true);
        }
        showSoftInputWindow();
    }


    public void getLocationInWindow(@Size(2) int[] leftTop) {
        bottomChatContainer.getLocationInWindow(leftTop);
    }

    public void translateInput(int height) {
        bottomChatContainer.setTranslationY(-height);
    }

    private PlayerDetailActivity getRealActivity() {
        return (PlayerDetailActivity) getActivity();
    }

    public Context getActivity() {
        return getContext();
    }

    public void clear() {
    }

    public void unBind() {
        ButterKnife.unbind(this);
    }

}