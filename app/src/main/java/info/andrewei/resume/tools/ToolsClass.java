package info.andrewei.resume.tools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class ToolsClass {

    public static void sendEmail(Context context){
        Intent intent = new Intent(Intent.ACTION_SEND);
        String[] tos = { "2775581248@qq.com" };
        intent.putExtra(Intent.EXTRA_EMAIL, tos); //收件者
        intent.putExtra(Intent.EXTRA_TEXT, "邮件内容");
        intent.putExtra(Intent.EXTRA_SUBJECT, "邮件标题");
        intent.setType("message/rfc882");
        Intent.createChooser(intent, "选择应用");
        context.startActivity(intent);
    }

    public static void callPhone(Context context) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + 112));
        try {
            context.startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
