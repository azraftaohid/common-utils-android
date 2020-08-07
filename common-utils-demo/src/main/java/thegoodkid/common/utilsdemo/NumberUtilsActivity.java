package thegoodkid.common.utilsdemo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.microsoft.fluentui.bottomsheet.BottomSheet;
import com.microsoft.fluentui.bottomsheet.BottomSheetItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import thegoodkid.common.utils.NumberUtils;
import thegoodkid.common.utilsdemo.databinding.ActivityNumberUtilsBinding;
import thegoodkid.common.utilsdemo.utilis.ViewUtils;

public class NumberUtilsActivity extends DemoActivity implements BottomSheetItem.OnClickListener {
    private static final String SELECT_READ_MODE_TAG = "thegoodkid.common.utilsdemo.NumberUtilsActivity.tag.READ_MODE";

    private ActivityNumberUtilsBinding binding;
    private NumberUtils.ReadMode numberReadMode = NumberUtils.ReadMode.NUMBER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNumberUtilsBinding.bind(getContentView());

        init();
    }

    @Override
    protected DemoListActivity.Demo getDemo() {
        return DemoListActivity.Demo.NUMBER_UTILS;
    }

    private void init() {
        View readModeChangeIcon = ViewUtils.createIcon(this, R.drawable.ic_fluent_text_change_settings_24_regular);
        readModeChangeIcon.setOnClickListener(view -> {
            ArrayList<BottomSheetItem> items = new ArrayList<>();
            items.add(new BottomSheetItem(R.id.read_mode_digit, R.drawable.ic_fluent_number_row_24_regular, getString(R.string.read_mode_digit)));
            items.add(new BottomSheetItem(R.id.read_mode_number, R.drawable.ic_fluent_convert_to_text_24_regular, getString(R.string.read_mode_number)));

            BottomSheetItem header = new BottomSheetItem(getString(R.string.select_read_mode));

            BottomSheet bottomSheet = BottomSheet.newInstance(items, header);
            bottomSheet.show(getSupportFragmentManager(), SELECT_READ_MODE_TAG);
        });

        binding.numberConversionResult.setCustomAccessoryView(readModeChangeIcon);

        binding.numberExtractionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Double number = NumberUtils.extractNumbers(editable.toString(), null);
                binding.numberExtractionResult.setTitle(number == null ? getString(R.string.result_number_extraction) : number.toString());
            }
        });

        binding.numberConversionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateNumberConversionResult();
            }
        });
    }

    private void updateNumberConversionResult() {
        String input = binding.numberConversionEditText.getText().toString();

        binding.numberConversionResult.setTitle(input.length() == 0 ?
                getString(R.string.result_number_conversion) : NumberUtils.toWord(input, numberReadMode));
    }

    @Override
    public void onBottomSheetItemClick(@NotNull BottomSheetItem bottomSheetItem) {
        switch (bottomSheetItem.getId()) {
            case R.id.read_mode_digit:
                numberReadMode = NumberUtils.ReadMode.DIGIT;
                break;
            case R.id.read_mode_number:
                numberReadMode = NumberUtils.ReadMode.NUMBER;
                break;
        }

        updateNumberConversionResult();
    }
}