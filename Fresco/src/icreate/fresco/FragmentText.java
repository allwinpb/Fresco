package icreate.fresco;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class FragmentText extends Fragment {
	
	EditText editText;
	
	public static FragmentText createFragment(String content) {
		Bundle bundle = new Bundle();
		bundle.putString(Constant.CONTENT, content);
		
		FragmentText fragment = new FragmentText();
		fragment.setArguments(bundle);
		
		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_text, container, false);
		
		String content = this.getArguments().getString(Constant.CONTENT);
		
		editText = (EditText) view.findViewById(R.id.cardEditText);
		editText.setText(content);
		int position = editText.length();
		editText.setSelection(position);
		
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(),
				0);
		
		editText.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				String content = editText.getText().toString();
				((AddEditActivity)getActivity()).setContent(content);
			}
			
		});
        
        return view;
    }
	
	public String getContent() {
		return editText.getText().toString();
	}

	public boolean isEmpty() {
		String content = editText.getText().toString();
		return content.isEmpty();
	}
}
