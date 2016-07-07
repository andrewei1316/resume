package info.andrewei.resume.otherItem;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import info.andrewei.resume.R;
import info.andrewei.resume.tools.ToolsClass;

public class ContactMe extends Activity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_me);
        Button btn_call_phone = (Button) this.findViewById(R.id.btn_call_phone);
        Button btn_send_email = (Button) this.findViewById(R.id.btn_send_email);
        Button btn_cancel = (Button) this.findViewById(R.id.btn_cancel);

        LinearLayout layout = (LinearLayout)findViewById(R.id.pop_layout);

        layout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                        Toast.LENGTH_SHORT).show();
            }
        });
        //添加按钮监听
        btn_cancel.setOnClickListener(this);
        btn_send_email.setOnClickListener(this);
        btn_call_phone.setOnClickListener(this);
    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call_phone:
                ToolsClass.callPhone(v.getContext());
                break;
            case R.id.btn_send_email:
                ToolsClass.sendEmail(v.getContext());
                break;
            case R.id.btn_cancel:
                break;
            default:
                break;
        }
        finish();
    }
}
