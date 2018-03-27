package kz.nis.history;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class MyTextWatcher implements TextWatcher {

	private Button button = null;
	private EditText[] edits = null;

	public MyTextWatcher(Button button, EditText[] edits){
		this.button = button;
		this.edits = edits;
		button.setEnabled(false);
	}
	
	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i2,
			int i3) {
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
		if (button != null)
			button.setEnabled(getEnableButton());
	}

	@Override
	public void afterTextChanged(Editable editable) {
	}

	private boolean getEnableButton() {

		for (int i = 0; i < edits.length; i++) {
			String text = edits[i].getText().toString();
			if (text.equals(""))
				return false;
		}
		return true;
	}
}
