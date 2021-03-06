/*
 * Copyright (c) The Good Company. All rights reserved.
 * Licensed under the MIT License.
 */

package thegoodcompany.common.utilsdemo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.microsoft.fluentui.bottomsheet.BottomSheetDialog;
import com.microsoft.fluentui.bottomsheet.BottomSheetItem;
import com.microsoft.fluentui.listitem.ListItemDivider;
import com.microsoft.fluentui.listitem.ListItemView;
import com.microsoft.fluentui.snackbar.Snackbar;
import com.microsoft.fluentui.util.ThemeUtil;
import com.microsoft.fluentui.util.ThemeUtilsKt;
import com.microsoft.fluentui.util.ViewUtilsKt;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import thegoodcompany.common.utils.recyclerview.HeaderItem;
import thegoodcompany.common.utilsdemo.databinding.ActivityListItemBinding;
import thegoodcompany.common.utilsdemo.utilis.list.Item;
import thegoodcompany.common.utilsdemo.utilis.list.ItemSection;
import thegoodcompany.common.utilsdemo.utilis.list.ListItemAdapter;

public class ListItemActivity extends DemoActivity {
    private static final int DEFAULT_SECTION_ITEM_COUNT = 4;
    private static final int PRIMARY_SECTION_COUNT = 5;

    private ActivityListItemBinding binding;
    private ListItemAdapter<SectionIdentifier> adapter;

    private int coverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListItemBinding.bind(getContentView());

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_item_activity, menu);

        Drawable addSectionIcon = ThemeUtilsKt.getTintedDrawable(this, R.drawable.ic_fluent_add_circle_24_selector,
                ThemeUtil.INSTANCE.getThemeAttrColor(this, R.attr.fluentuiToolbarIconColor));

        menu.findItem(R.id.menu_add_section).setIcon(addSectionIcon);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add_section) {
            for (SectionIdentifier identifier : SectionIdentifier.values()) {
                if (!adapter.hasSection(identifier)) {
                    adapter.addSection(identifier, createSection(identifier));
                    collapseAppbar();
                    binding.listContainer.scrollToPosition(adapter.getItemCount() - 1);
                    return true;
                }
            }
        }

        Snackbar.Companion.make(binding.getRoot(), getString(R.string.section_full), Snackbar.LENGTH_SHORT, Snackbar.Style.REGULAR)
                .setAction(getString(R.string.reset), view -> {
                    coverage = 0;
                    adapter = new ListItemAdapter<>(this, createSectionMap());
                    binding.listContainer.swapAdapter(adapter, true);
                })
                .show();

        return false;
    }

    @Override
    protected DemoListActivity.Demo getDemo() {
        return DemoListActivity.Demo.LIST_ITEM;
    }

    private void init() {
        LinkedHashMap<SectionIdentifier, ItemSection> sectionMap = createSectionMap();
        adapter = new ListItemAdapter<>(this, sectionMap);

        binding.listContainer.addItemDecoration(new ListItemDivider(this, DividerItemDecoration.VERTICAL));
        binding.listContainer.setAdapter(adapter);
    }

    @NonNull
    private LinkedHashMap<SectionIdentifier, ItemSection> createSectionMap() {
        LinkedHashMap<SectionIdentifier, ItemSection> sectionMap = new LinkedHashMap<>();

        SectionIdentifier[] identifiers = SectionIdentifier.values();
        for (int i = 0; i < PRIMARY_SECTION_COUNT; i++) {
            SectionIdentifier identifier = identifiers[i];
            sectionMap.put(identifier, createSection(identifier));
        }

        return sectionMap;
    }

    private Item createItem() {
        ImageView customView = ViewUtilsKt.createImageView(this, R.drawable.ic_fluent_list_24_regular,
                ThemeUtil.INSTANCE.getThemeAttrColor(this, R.attr.fluentuiForegroundSecondaryIconColor));

        return new Item("Item " + ++coverage)
                .setSubtitle("Subtitle " + coverage)
                .setCustomView(customView, ListItemView.CustomViewSize.SMALL)
                .setOnClickListener(view -> Snackbar.Companion.make(binding.getRoot(), getString(R.string.on_list_item_click), Snackbar.LENGTH_SHORT, Snackbar.Style.REGULAR)
                        .show());
    }

    @NonNull
    private ItemSection createSection(@NonNull SectionIdentifier identifier) {
        HeaderItem headerItem = new HeaderItem(identifier.title).setAccessoryView(createSectionAccessoryView(identifier));
        ArrayList<Item> items = new ArrayList<>();

        for (int i = 0; i < DEFAULT_SECTION_ITEM_COUNT; i++) {
            Item item = createItem();
            item.setAccessoryView(createItemAccessoryView(identifier, item));

            items.add(item);
        }

        return new ItemSection(headerItem, items);
    }

    @NonNull
    private View createItemAccessoryView(SectionIdentifier section, Item item) {
        ImageView accessoryView = new ImageView(this);

        int tint = ThemeUtil.INSTANCE.getThemeAttrColor(this, R.attr.fluentuiForegroundSecondaryIconColor);
        accessoryView.setImageDrawable(ThemeUtilsKt.getTintedDrawable(this, R.drawable.ic_fluent_more_vertical_24_regular, tint));

        accessoryView.setOnClickListener(view -> showItemActions(section, item));

        return accessoryView;
    }

    @NonNull
    private View createSectionAccessoryView(SectionIdentifier section) {
        TextView accessoryView = new TextView(this);
        TextViewCompat.setTextAppearance(accessoryView, R.style.TextAppearance_AppTheme_SectionAccessoryValue);
        accessoryView.setText(R.string.action);

        accessoryView.setOnClickListener(view -> showSectionActions(section));

        return accessoryView;
    }

    private void showItemActions(SectionIdentifier section, Item item) {
        ArrayList<BottomSheetItem> sheetItems = new ArrayList<>();
        sheetItems.add(new BottomSheetItem(R.id.action_remove_item, R.drawable.ic_fluent_delete_24_regular, getString(R.string.remove_item)));

        BottomSheetDialog dialog = new BottomSheetDialog(this, sheetItems);
        dialog.setOnItemClickListener(bottomSheetItem -> {
            if (bottomSheetItem.getId() == R.id.action_remove_item) {
                adapter.removeItemFromSection(section, item);
            }
        });

        dialog.show();
    }

    private void showSectionActions(SectionIdentifier section) {
        ArrayList<BottomSheetItem> sheetItems = new ArrayList<>();
        sheetItems.add(new BottomSheetItem(R.id.action_add_item, R.drawable.ic_fluent_add_24_regular, getString(R.string.add_item)));
        sheetItems.add(new BottomSheetItem(R.id.action_clear_section, R.drawable.ic_fluent_line_horizontal_1_20_regular, getString(R.string.remove_section_items)));
        sheetItems.add(new BottomSheetItem(R.id.action_remove_section, R.drawable.ic_fluent_delete_24_regular, getString(R.string.remove_section)));

        BottomSheetDialog dialog = new BottomSheetDialog(this, sheetItems);
        dialog.setOnItemClickListener(bottomSheetItem -> {
            int id = bottomSheetItem.getId();
            if (id == R.id.action_add_item) {
                Item item = createItem();
                item.setAccessoryView(createItemAccessoryView(section, item));

                adapter.addItem(section, item);
                collapseAppbar();
                binding.listContainer.scrollToPosition(adapter.getItemPosition(item));
            } else if (id == R.id.action_clear_section) {
                adapter.clearSectionItems(section);
            } else if (id == R.id.action_remove_section) {
                adapter.removeSection(section);
            }
        });

        dialog.show();
    }

    private enum SectionIdentifier {
        SECTION_1("Section 1"),
        SECTION_2("Section 2"),
        SECTION_3("Section 3"),
        SECTION_4("Section 4"),
        SECTION_5("Section 5"),
        ADDITIONAL_SECTION_1("Additional Section 1"),
        ADDITIONAL_SECTION_2("Additional Section 2"),
        ADDITIONAL_SECTION_3("Additional Section 3");

        private final String title;

        SectionIdentifier(String title) {
            this.title = title;
        }
    }
}