package mypasswordapp.project.org.mypasswordapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by User Account on 25-01-2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etPass;
    Button bLogin;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginmain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etPass = (EditText) findViewById(R.id.etPass);
        bLogin = (Button) findViewById(R.id.bLogin);
        bLogin.setOnClickListener(this);
        prefs = getApplicationContext().getSharedPreferences("logindetails", MODE_PRIVATE);
        editor = prefs.edit();
        if (!prefs.contains("password")) {
            myTextDialog().show();
        }

    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p/>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                displayAboutDialog().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p/>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p/>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p/>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p/>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bLogin:
                String temp = prefs.getString("password", null);
                if (temp != null && temp.equals(etPass.getText().toString())) {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(intent);
                    finish();
                } else {
                    Snackbar.make(view, "Incorrent Password", Snackbar.LENGTH_SHORT).show();
                    etPass.setText("");
                }
                break;
        }
    }

    private Dialog myTextDialog() {
        final View layout = getLayoutInflater().inflate(R.layout.newlogin, null);
        final EditText newpass = ((EditText) layout.findViewById(R.id.etPassword));
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setIcon(0);
        builder.setTitle("Password Setup");
        builder.setCancelable(false);
        builder.setPositiveButton("Save", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                editor.clear();
                editor.putString("password", newpass.getText().toString());
                editor.commit();
                Toast.makeText(LoginActivity.this,
                        "New Password Set to " + newpass.getText().toString()
                        , Toast.LENGTH_SHORT)
                        .show();
            }
        });
        builder.setView(layout);
        return builder.create();
    }
    private Dialog displayAboutDialog() {
        final View layout = getLayoutInflater().inflate(R.layout.about_me, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setIcon(0);
        builder.setTitle("About Me");
        builder.setCancelable(true);
        builder.setView(layout);
        return builder.create();
    }

}
