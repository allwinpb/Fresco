package icreate.fresco;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FrescoMain extends Activity {
	private ArrayList<String> category;
	private ListView lv;
	private ArrayAdapter<String> adapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco_main);
        lv = (ListView)findViewById(R.id.list_view);
        category = new ArrayList<String>();
        init();
        adapter = new ArrayAdapter<String>(FrescoMain.this, android.R.layout.simple_list_item_1, category);
        lv.setAdapter(adapter);
        //setListAdapter(new ArrayAdapter<String>(FrescoMain.this, android.R.layout.simple_list_item_1, category));

    }
    
    public void init(){
    	category.add("Friends");
    	category.add("Words");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
    	getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    	switch(item.getItemId()){
        case R.id.add_icon:
        	Intent i = new Intent(this, AddCategory.class);
			startActivityForResult(i, 1);
			break;
        case R.id.search_icon:
        	
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
			category.add(data.getStringExtra("category"));
			adapter.notifyDataSetChanged();
			
	}
    /**
     * A placeholder fragment containing a simple view.
    
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_fresco_main, container, false);
            return rootView;
        }
    }
 */
}
