package fr.emmanuelroodlyyahoo.nyts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import fr.emmanuelroodlyyahoo.nyts.R;
import fr.emmanuelroodlyyahoo.nyts.adapters.ArticleArrayAdapter;
import fr.emmanuelroodlyyahoo.nyts.model.Article;
import fr.emmanuelroodlyyahoo.nyts.model.MyDialog;

public class SearchActivity extends AppCompatActivity {
    EditText etQuery;
    Button btnSearch;
    GridView gvResults;
    ArrayList<Article> articles;
    ArticleArrayAdapter myAdapter;
    MyDialog d; //appel d'un fragment personnalise
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupsViews();
        fm = getSupportFragmentManager();
        d = new MyDialog();
    }

    public void setupsViews(){
        etQuery = (EditText) findViewById(R.id.etQuery);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        gvResults = (GridView) findViewById(R.id.gvResults);
        articles = new ArrayList<>();
        myAdapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(myAdapter);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent next = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = articles.get(i);
                next.putExtra("article", article);
                startActivity(next);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();
        Toast.makeText(this, "Recherche de : " + query, Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        String key = "a341c7b04a4846318b10b134e35b5c6c";
        RequestParams params = new RequestParams();
        params.put("api-key", key);
        params.put("page", 0);
        params.put("q", query);
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJSONResults = null;
                //Log.d("DEBUG", response.toString());

                try{
                    articleJSONResults = response.getJSONObject("response").getJSONArray("docs");
                    myAdapter.addAll(Article.fromJSONArray(articleJSONResults));
                    //myAdapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

    }

    public void showDialog(MenuItem item) {
        d.show(fm, "Settings");
    }

    //methode qui va retournee les valeurs du fragment de type myDialog
    public void getValuesFromFragment(int jours, int mois, int annee , String r_spinner, boolean sport, boolean fashion, boolean art){
        Toast.makeText(this, "parametres: " + jours +"/"+ mois +"/"+ annee +"/" + " | " + r_spinner + " | " + String.valueOf(sport) + " | " + String.valueOf(fashion) + " | " + String.valueOf(art) + " | ", Toast.LENGTH_LONG).show();
    }
}
