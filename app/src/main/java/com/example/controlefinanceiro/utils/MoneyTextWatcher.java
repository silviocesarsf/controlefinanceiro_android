package com.example.controlefinanceiro.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import java.text.NumberFormat;
import java.util.Locale;

public class MoneyTextWatcher implements TextWatcher {

    private final EditText editText;
    private final Locale locale = new Locale("pt", "BR");

    public MoneyTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Não é necessário implementar este método
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Não é necessário implementar este método
    }

    @Override
    public void afterTextChanged(Editable s) {
        editText.removeTextChangedListener(this);

        String originalString = s.toString();

        String cleanString = originalString.replaceAll("[^\\d.]", "");

        try {
            double parsed = Double.parseDouble(cleanString);
            String formatted = NumberFormat.getCurrencyInstance(locale).format(parsed / 100);
            editText.setText(formatted);
            editText.setSelection(formatted.length());
        } catch (NumberFormatException e) {
            editText.setText("");
        }

        editText.addTextChangedListener(this);
    }


}
