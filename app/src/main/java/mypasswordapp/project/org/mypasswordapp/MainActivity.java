package mypasswordapp.project.org.mypasswordapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {

    SharedPreferences prefs, loginprefs;
    SharedPreferences.Editor editor, loginEditor;
    Map<String, ?> map;
    ListView listView;
    String values[];
    ArrayAdapter<String> adapter;
    Set<String> setDisplayAllEntries, setSingleEntry;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs = getApplicationContext().getSharedPreferences("listall", MODE_PRIVATE);
        loginprefs = getApplicationContext().getSharedPreferences("logindetails", MODE_PRIVATE);
        editor = prefs.edit();
        loginEditor = loginprefs.edit();
        listView = (ListView) findViewById(R.id.listAll);
        handler = new Handler();
        handler.post(runnable);
        /*getAllEntries();
        setupList();*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        fab.setOnClickListener(this);

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getAllEntries();
            setupList();
        }
    };

    private void setupList() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
    }

    private void getAllEntries() {
        map = prefs.getAll();
        setDisplayAllEntries = map.keySet();
        values = setDisplayAllEntries.toArray(new String[setDisplayAllEntries.size()]);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.change_password:
                myTextDialog().show();
                break;
            case R.id.clearAll:
                editor.clear();
                editor.commit();
                getAllEntries();
                setupList();
                break;
            case R.id.about:
                displayAboutDialog().show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

    private Dialog myTextDialog() {
        //LayoutInflater li = getLayoutInflater();
        final View layout = getLayoutInflater().inflate(R.layout.mydialog, null);
        final EditText newpass = ((EditText) layout.findViewById(R.id.etNewPass));
        final EditText confirmPass = ((EditText) layout.findViewById(R.id.etConfirmPass));
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(0);
        builder.setTitle("Password Change");
        builder.setPositiveButton("Save", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (newpass.length() >= 4) {
                    if (newpass.getText().toString().equals(confirmPass.getText().toString())) {
                        loginEditor.clear();
                        loginEditor.putString("password", newpass.getText().toString());
                        loginEditor.commit();
                        Toast.makeText(MainActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this
                            , "Password not changed.\nPassword must be more than at least 4 characters."
                            , Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        builder.setView(layout);
        return builder.create();
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Set<String> useridpass = prefs.getStringSet(values[position], null);
        if (useridpass != null) {
            String[] str = useridpass.toArray(new String[useridpass.size()]);
            displayValues(str).show();
        }
    }

    /**
     * Callback method to be invoked when an item in this view has been
     * clicked and held.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need to access
     * the data associated with the selected item.
     *
     * @param parent   The AbsListView where the click happened
     * @param view     The view within the AbsListView that was clicked
     * @param position The position of the view in the list
     * @param id       The row id of the item that was clicked
     * @return true if the callback consumed the long click, false otherwise
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        deleteEntry(position).show();
        return true;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                /*Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                newEntry().show();
                //handler.post(runnable);
                /*getAllEntries();
                setupList();*/
                break;
        }
    }

    private Dialog newEntry() {
        //LayoutInflater li = getLayoutInflater();
        final View layout = getLayoutInflater().inflate(R.layout.newentry, null);
        final EditText newDomain = ((EditText) layout.findViewById(R.id.newDomain));
        final EditText newUser = ((EditText) layout.findViewById(R.id.newUser));
        final EditText newPass = ((EditText) layout.findViewById(R.id.newPassword));
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(0);
        builder.setTitle("New Entry");
        builder.setPositiveButton("Save", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (newUser.getText().toString().length() > 0 && newPass.getText().toString().length() > 0 && newDomain.getText().toString().length() > 0) {
                    String[] temp = {newUser.getText().toString(), newPass.getText().toString()};
                    setSingleEntry = new HashSet<String>(Arrays.asList(temp));
                    editor.putStringSet(newDomain.getText().toString(), setSingleEntry);
                    editor.commit();
                    Toast.makeText(MainActivity.this, "New Entry Added", Toast.LENGTH_SHORT).show();
                    getAllEntries();
                    setupList();
                }
            }
        });
        builder.setView(layout);
        return builder.create();
    }

    private Dialog displayValues(String[] str) {
        View view = getLayoutInflater().inflate(R.layout.displayvalues, null);
        TextView displayUser = ((TextView) view.findViewById(R.id.displayUser));
        TextView displayPass = ((TextView) view.findViewById(R.id.displayPassword));
        displayUser.setText(str[0]);
        displayPass.setText(str[1]);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(0);
        builder.setView(view);
        return builder.create();
    }

    private Dialog displayAboutDialog() {
        final View layout = getLayoutInflater().inflate(R.layout.about_me, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(0);
        builder.setTitle("About Me");
        builder.setCancelable(true);
        builder.setView(layout);
        return builder.create();
    }

    private Dialog deleteEntry(final int position) {
        final View layout = getLayoutInflater().inflate(R.layout.delete_entry, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(0);
        builder.setTitle("Confirm Delete");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor.remove(values[position]);
                editor.apply();
                /*List<String> l = new ArrayList();
                Collections.addAll(l, values);
                l.remove(position);
                values = new String[l.size()];
                values = l.toArray(new String[l.size()]);*/
                getAllEntries();
                setupList();
            }
        });
        builder.setCancelable(true);
        builder.setView(layout);
        return builder.create();
    }


}

