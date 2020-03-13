package extras;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.koobookandroidapp.R;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import activities.EnterOneTimePasswordActivity;
import activities.ForgotPasswordActivity;
import activities.SignUpActivity;

//Credit to Musfick for solution: https://github.com/Musfick/JavaMailAPIDemo/blob/master/app/src/main/java/com/teamcreative/javamailapidemo/JavaMailAPI.java
public class JavaMail extends AsyncTask<Void,Void,Void> {

    private Context context;
    private Session session;
    private String email;
    private String subject;
    private String message;
    private ProgressDialog progressDialog;

    public JavaMail(Context context, String email, String subject, String message) {
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    //While the Async task is being execute, the progress dialog will be displyed to inform the user that an email is being send to them
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Sending one time password to your email", "Please wait", false, false);
    }

    //After the Async task has been executed, close the dialog, display "email sent" notification and display the "Reset Password" activity
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        Toast.makeText(context, "One time password sent, please check your email", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(context, EnterOneTimePasswordActivity.class);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected Void doInBackground(Void... voids) {

        //Configure properties for Gmail
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.user", Utils.EMAIL);
        props.put("mail.smtp.password", Utils.PASSWORD);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        //Create session using the host's email and password
        session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Utils.EMAIL,Utils.PASSWORD);
            }
        });

        //Create message object using the session
        //Set the message's properties and transport(send) it
        try{
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(Utils.EMAIL));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);
            Transport.send(mimeMessage);
        } catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
