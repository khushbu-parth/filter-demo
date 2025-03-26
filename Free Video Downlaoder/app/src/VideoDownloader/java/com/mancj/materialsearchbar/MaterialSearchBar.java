package com.mancj.materialsearchbar;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mancj.materialsearchbar.EditTextStyleHelper;
import com.mancj.materialsearchbar.adapter.DefaultSuggestionsAdapter;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;

import java.util.ArrayList;
import java.util.List;


public class MaterialSearchBar extends RelativeLayout implements View.OnClickListener, Animation.AnimationListener, SuggestionsAdapter.OnItemViewClickListener, View.OnFocusChangeListener, TextView.OnEditorActionListener {
    public static final int BUTTON_BACK = 3;
    public static final int BUTTON_NAVIGATION = 2;
    public static final int BUTTON_SPEECH = 1;
    public static final int VIEW_INVISIBLE = 0;
    public static final int VIEW_VISIBLE = 1;
    private SuggestionsAdapter adapter;
    private ImageView arrowIcon;
    private int arrowIconRes;
    private int arrowIconTint;
    private boolean arrowIconTintEnabled;
    private boolean borderlessRippleEnabled = false;
    private ImageView clearIcon;
    private int clearIconRes;
    private int clearIconTint;
    private boolean clearIconTintEnabled;
    private float destiny;
    private int dividerColor;
    private int highlightedTextColor;
    private int hintColor;
    private CharSequence hintText;
    private LinearLayout inputContainer;
    private int leftTextSelectorRes;
    private int leftTextSelectorTint;
    private int maxSuggestionCount;
    private View menuDivider;
    private boolean menuDividerEnabled;
    private ImageView menuIcon;
    private int menuIconRes;
    private int menuIconTint;
    private boolean menuIconTintEnabled;
    private int menuResource;
    private int middleTextSelectorRes;
    private int middleTextSelectorTint;
    private boolean navButtonEnabled;
    private ImageView navIcon;
    private int navIconResId;
    private boolean navIconShown = true;
    private int navIconTint;
    private boolean navIconTintEnabled;
    private OnSearchActionListener onSearchActionListener;
    private TextView placeHolder;
    private int placeholderColor;
    private CharSequence placeholderText;
    private PopupMenu popupMenu;
    private int rightTextSelectorRes;
    private int rightTextSelectorTint;
    private boolean roundedSearchBarEnabled;
    private CardView searchBarCardView;
    private int searchBarColor;
    private EditText searchEdit;
    private boolean searchEnabled;
    private ImageView searchIcon;
    private int searchIconRes;
    private int searchIconTint;
    private boolean searchIconTintEnabled;
    private int speechIconRes;
    private boolean speechMode;
    private View suggestionDivider;
    private boolean suggestionsVisible;
    private int textColor;
    private int textCursorColor;
    private boolean textSelectorTintEnabled;

    public interface OnSearchActionListener {
        void onButtonClicked(int i);

        void onSearchConfirmed(CharSequence charSequence);

        void onSearchStateChanged(boolean z);
    }

    public void onAnimationRepeat(Animation animation) {
    }

    public void onAnimationStart(Animation animation) {
    }

    public MaterialSearchBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public MaterialSearchBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet);
    }

    @SuppressLint("NewApi")
    public MaterialSearchBar(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(attributeSet);
    }

    private void init(AttributeSet attributeSet) {
        inflate(getContext(), R.layout.searchbar, this);
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.MaterialSearchBar);
        this.speechMode = obtainStyledAttributes.getBoolean(R.styleable.MaterialSearchBar_mt_speechMode, false);
        this.maxSuggestionCount = obtainStyledAttributes.getInt(R.styleable.MaterialSearchBar_mt_maxSuggestionsCount, 3);
        this.navButtonEnabled = obtainStyledAttributes.getBoolean(R.styleable.MaterialSearchBar_mt_navIconEnabled, false);
        this.roundedSearchBarEnabled = obtainStyledAttributes.getBoolean(R.styleable.MaterialSearchBar_mt_roundedSearchBarEnabled, false);
        this.menuDividerEnabled = obtainStyledAttributes.getBoolean(R.styleable.MaterialSearchBar_mt_menuDividerEnabled, false);
        this.dividerColor = obtainStyledAttributes.getColor(R.styleable.MaterialSearchBar_mt_dividerColor, ContextCompat.getColor(getContext(), R.color.searchBarDividerColor));
        this.searchBarColor = obtainStyledAttributes.getColor(R.styleable.MaterialSearchBar_mt_searchBarColor, ContextCompat.getColor(getContext(), R.color.searchBarPrimaryColor));
        this.menuIconRes = obtainStyledAttributes.getResourceId(R.styleable.MaterialSearchBar_mt_menuIconDrawable, R.drawable.ic_dots_vertical_black_48dp);
        this.searchIconRes = obtainStyledAttributes.getResourceId(R.styleable.MaterialSearchBar_mt_searchIconDrawable, R.drawable.ic_magnify_black_48dp);
        this.speechIconRes = obtainStyledAttributes.getResourceId(R.styleable.MaterialSearchBar_mt_speechIconDrawable, R.drawable.ic_microphone_black_48dp);
        this.arrowIconRes = obtainStyledAttributes.getResourceId(R.styleable.MaterialSearchBar_mt_backIconDrawable, R.drawable.ic_arrow_left_black_48dp);
        this.clearIconRes = obtainStyledAttributes.getResourceId(R.styleable.MaterialSearchBar_mt_clearIconDrawable, R.drawable.ic_close_black_48dp);
        this.navIconTint = obtainStyledAttributes.getColor(R.styleable.MaterialSearchBar_mt_navIconTint, ContextCompat.getColor(getContext(), R.color.searchBarNavIconTintColor));
        this.menuIconTint = obtainStyledAttributes.getColor(R.styleable.MaterialSearchBar_mt_menuIconTint, ContextCompat.getColor(getContext(), R.color.searchBarMenuIconTintColor));
        this.searchIconTint = obtainStyledAttributes.getColor(R.styleable.MaterialSearchBar_mt_searchIconTint, ContextCompat.getColor(getContext(), R.color.searchBarSearchIconTintColor));
        this.arrowIconTint = obtainStyledAttributes.getColor(R.styleable.MaterialSearchBar_mt_backIconTint, ContextCompat.getColor(getContext(), R.color.searchBarBackIconTintColor));
        this.clearIconTint = obtainStyledAttributes.getColor(R.styleable.MaterialSearchBar_mt_clearIconTint, ContextCompat.getColor(getContext(), R.color.searchBarClearIconTintColor));
        this.navIconTintEnabled = obtainStyledAttributes.getBoolean(R.styleable.MaterialSearchBar_mt_navIconUseTint, true);
        this.menuIconTintEnabled = obtainStyledAttributes.getBoolean(R.styleable.MaterialSearchBar_mt_menuIconUseTint, true);
        this.searchIconTintEnabled = obtainStyledAttributes.getBoolean(R.styleable.MaterialSearchBar_mt_searchIconUseTint, true);
        this.arrowIconTintEnabled = obtainStyledAttributes.getBoolean(R.styleable.MaterialSearchBar_mt_backIconUseTint, true);
        this.clearIconTintEnabled = obtainStyledAttributes.getBoolean(R.styleable.MaterialSearchBar_mt_clearIconUseTint, true);
        this.borderlessRippleEnabled = obtainStyledAttributes.getBoolean(R.styleable.MaterialSearchBar_mt_borderlessRippleEnabled, false);
        this.hintText = obtainStyledAttributes.getString(R.styleable.MaterialSearchBar_mt_hint);
        this.placeholderText = obtainStyledAttributes.getString(R.styleable.MaterialSearchBar_mt_placeholder);
        this.textColor = obtainStyledAttributes.getColor(R.styleable.MaterialSearchBar_mt_textColor, ContextCompat.getColor(getContext(), R.color.searchBarTextColor));
        this.hintColor = obtainStyledAttributes.getColor(R.styleable.MaterialSearchBar_mt_hintColor, ContextCompat.getColor(getContext(), R.color.searchBarHintColor));
        this.placeholderColor = obtainStyledAttributes.getColor(R.styleable.MaterialSearchBar_mt_placeholderColor, ContextCompat.getColor(getContext(), R.color.searchBarPlaceholderColor));
        this.textCursorColor = obtainStyledAttributes.getColor(R.styleable.MaterialSearchBar_mt_textCursorTint, ContextCompat.getColor(getContext(), R.color.searchBarCursorColor));
        this.leftTextSelectorTint = obtainStyledAttributes.getColor(R.styleable.MaterialSearchBar_mt_leftTextSelectorTint, ContextCompat.getColor(getContext(), R.color.leftTextSelectorColor));
        this.middleTextSelectorTint = obtainStyledAttributes.getColor(R.styleable.MaterialSearchBar_mt_middleTextSelectorTint, ContextCompat.getColor(getContext(), R.color.middleTextSelectorColor));
        this.rightTextSelectorTint = obtainStyledAttributes.getColor(R.styleable.MaterialSearchBar_mt_rightTextSelectorTint, ContextCompat.getColor(getContext(), R.color.rightTextSelectorColor));
        this.leftTextSelectorRes = obtainStyledAttributes.getResourceId(R.styleable.MaterialSearchBar_mt_leftTextSelectorDrawable, R.drawable.text_select_handle_left_mtrl_alpha_mtrlsearch);
        this.middleTextSelectorRes = obtainStyledAttributes.getResourceId(R.styleable.MaterialSearchBar_mt_middleTextSelectorDrawable, R.drawable.text_select_handle_middle_mtrl_alpha_mtrlsearch);
        this.rightTextSelectorRes = obtainStyledAttributes.getResourceId(R.styleable.MaterialSearchBar_mt_rightTextSelectorDrawable, R.drawable.text_select_handle_right_mtrl_alpha_mtrlsearch);
        this.textSelectorTintEnabled = obtainStyledAttributes.getBoolean(R.styleable.MaterialSearchBar_mt_handlesTintEnabled, true);
        this.highlightedTextColor = obtainStyledAttributes.getColor(R.styleable.MaterialSearchBar_mt_highlightedTextColor, ContextCompat.getColor(getContext(), R.color.searchBarTextHighlightColor));
        this.destiny = getResources().getDisplayMetrics().density;
        if (this.adapter == null) {
            this.adapter = new DefaultSuggestionsAdapter(LayoutInflater.from(getContext()));
        }
        SuggestionsAdapter suggestionsAdapter = this.adapter;
        if (suggestionsAdapter instanceof DefaultSuggestionsAdapter) {
            ((DefaultSuggestionsAdapter) suggestionsAdapter).setListener(this);
        }
        this.adapter.setMaxSuggestionsCount(this.maxSuggestionCount);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.mt_recycler);
        recyclerView.setAdapter(this.adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        obtainStyledAttributes.recycle();
        this.searchBarCardView = (CardView) findViewById(R.id.mt_container);
        this.suggestionDivider = findViewById(R.id.mt_divider);
        this.menuDivider = findViewById(R.id.mt_menu_divider);
        this.menuIcon = (ImageView) findViewById(R.id.mt_menu);
        this.clearIcon = (ImageView) findViewById(R.id.mt_clear);
        this.searchIcon = (ImageView) findViewById(R.id.mt_search);
        this.arrowIcon = (ImageView) findViewById(R.id.mt_arrow);
        this.searchEdit = (EditText) findViewById(R.id.mt_editText);
        this.placeHolder = (TextView) findViewById(R.id.mt_placeholder);
        this.inputContainer = (LinearLayout) findViewById(R.id.inputContainer);
        this.navIcon = (ImageView) findViewById(R.id.mt_nav);
        findViewById(R.id.mt_clear).setOnClickListener(this);
        setOnClickListener(this);
        this.arrowIcon.setOnClickListener(this);
        this.searchIcon.setOnClickListener(this);
        this.searchEdit.setOnFocusChangeListener(this);
        this.searchEdit.setOnEditorActionListener(this);
        this.navIcon.setOnClickListener(this);
        postSetup();
    }

    public void inflateMenu(int i) {
        inflateMenuRequest(i, -1);
    }

    public void inflateMenu(int i, int i2) {
        inflateMenuRequest(i, i2);
    }

    private void inflateMenuRequest(int i, int i2) {
        this.menuResource = i;
        if (i > 0) {
            ImageView imageView = (ImageView) findViewById(R.id.mt_menu);
            if (i2 != -1) {
                this.menuIconRes = i2;
                imageView.setImageResource(i2);
            }
            LayoutParams layoutParams = (LayoutParams) this.searchIcon.getLayoutParams();
            layoutParams.rightMargin = (int) (this.destiny * 48.0f);
            this.searchIcon.setLayoutParams(layoutParams);
            imageView.setVisibility(VISIBLE);
            imageView.setOnClickListener(this);
            PopupMenu popupMenu2 = new PopupMenu(getContext(), imageView);
            this.popupMenu = popupMenu2;
            popupMenu2.inflate(i);
            this.popupMenu.setGravity(5);
        }
    }

    public PopupMenu getMenu() {
        return this.popupMenu;
    }

    private void postSetup() {
        setupTextColors();
        setupRoundedSearchBarEnabled();
        setupSearchBarColor();
        setupIcons();
        setupMenuDividerEnabled();
        setupSearchEditText();
    }

    private void setupRoundedSearchBarEnabled() {
        if (!this.roundedSearchBarEnabled || Build.VERSION.SDK_INT < 21) {
            this.searchBarCardView.setRadius(getResources().getDimension(R.dimen.corner_radius_default));
        } else {
            this.searchBarCardView.setRadius(getResources().getDimension(R.dimen.corner_radius_rounded));
        }
    }

    private void setupSearchBarColor() {
        this.searchBarCardView.setCardBackgroundColor(this.searchBarColor);
        setupDividerColor();
    }

    private void setupDividerColor() {
        this.suggestionDivider.setBackgroundColor(this.dividerColor);
        this.menuDivider.setBackgroundColor(this.dividerColor);
    }

    private void setupTextColors() {
        this.searchEdit.setHintTextColor(this.hintColor);
        this.searchEdit.setTextColor(this.textColor);
        this.placeHolder.setTextColor(this.placeholderColor);
    }

    private void setupSearchEditText() {
        try {
            EditTextStyleHelper.applyChanges(this.searchEdit, this.textCursorColor, this.leftTextSelectorTint, this.rightTextSelectorTint, this.middleTextSelectorTint, this.leftTextSelectorRes, this.rightTextSelectorRes, this.middleTextSelectorRes, this.textSelectorTintEnabled);
        } catch (EditTextStyleHelper.EditTextStyleChangeError e) {
            Log.e("ContentValues", "init: ", e);
        }
        this.searchEdit.setHighlightColor(this.highlightedTextColor);
        CharSequence charSequence = this.hintText;
        if (charSequence != null) {
            this.searchEdit.setHint(charSequence);
        }
        if (this.placeholderText != null) {
            this.arrowIcon.setBackground((Drawable) null);
            this.placeHolder.setText(this.placeholderText);
        }
    }

    private void setupIcons() {
        int i = R.drawable.ic_menu_animated;
        this.navIconResId = i;
        this.navIcon.setImageResource(i);
        setNavButtonEnabled(this.navButtonEnabled);
        if (this.popupMenu == null) {
            findViewById(R.id.mt_menu).setVisibility(GONE);
        }
        setSpeechMode(this.speechMode);
        this.arrowIcon.setImageResource(this.arrowIconRes);
        this.clearIcon.setImageResource(this.clearIconRes);
        setupNavIconTint();
        setupMenuIconTint();
        setupSearchIconTint();
        setupArrowIconTint();
        setupClearIconTint();
        setupIconRippleStyle();
    }

    private void setupNavIconTint() {
        if (this.navIconTintEnabled) {
            this.navIcon.setColorFilter(this.navIconTint, PorterDuff.Mode.SRC_IN);
        } else {
            this.navIcon.clearColorFilter();
        }
    }

    private void setupMenuIconTint() {
        if (this.menuIconTintEnabled) {
            this.menuIcon.setColorFilter(this.menuIconTint, PorterDuff.Mode.SRC_IN);
        } else {
            this.menuIcon.clearColorFilter();
        }
    }

    private void setupSearchIconTint() {
        if (this.searchIconTintEnabled) {
            this.searchIcon.setColorFilter(this.searchIconTint, PorterDuff.Mode.SRC_IN);
        } else {
            this.searchIcon.clearColorFilter();
        }
    }

    private void setupArrowIconTint() {
        if (this.arrowIconTintEnabled) {
            this.arrowIcon.setColorFilter(this.arrowIconTint, PorterDuff.Mode.SRC_IN);
        } else {
            this.arrowIcon.clearColorFilter();
        }
    }

    private void setupClearIconTint() {
        if (this.clearIconTintEnabled) {
            this.clearIcon.setColorFilter(this.clearIconTint, PorterDuff.Mode.SRC_IN);
        } else {
            this.clearIcon.clearColorFilter();
        }
    }

    private void setupMenuDividerEnabled() {
        if (this.menuDividerEnabled) {
            this.menuDivider.setVisibility(VISIBLE);
        } else {
            this.menuDivider.setVisibility(GONE);
        }
    }

    private void setupIconRippleStyle() {
        if (Build.VERSION.SDK_INT > 16) {
            TypedValue typedValue = new TypedValue();
            if (!this.borderlessRippleEnabled || Build.VERSION.SDK_INT < 21) {
                getContext().getTheme().resolveAttribute(16843534, typedValue, true);
            } else {
                getContext().getTheme().resolveAttribute(16843868, typedValue, true);
            }
            this.navIcon.setBackgroundResource(typedValue.resourceId);
            this.searchIcon.setBackgroundResource(typedValue.resourceId);
            this.menuIcon.setBackgroundResource(typedValue.resourceId);
            this.arrowIcon.setBackgroundResource(typedValue.resourceId);
            this.clearIcon.setBackgroundResource(typedValue.resourceId);
            return;
        }
        Log.w("ContentValues", "setupIconRippleStyle() Only Available On SDK Versions Higher Than 16!");
    }

    public void setOnSearchActionListener(OnSearchActionListener onSearchActionListener2) {
        this.onSearchActionListener = onSearchActionListener2;
    }

    public void disableSearch() {
        animateNavIcon();
        this.searchEnabled = false;
        Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        Animation loadAnimation2 = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_right);
        loadAnimation.setAnimationListener(this);
        this.searchIcon.setVisibility(VISIBLE);
        this.inputContainer.startAnimation(loadAnimation);
        this.searchIcon.startAnimation(loadAnimation2);
        if (this.placeholderText != null) {
            this.placeHolder.setVisibility(VISIBLE);
            this.placeHolder.startAnimation(loadAnimation2);
        }
        if (listenerExists()) {
            this.onSearchActionListener.onSearchStateChanged(false);
        }
        if (this.suggestionsVisible) {
            animateSuggestions(getListHeight(false), 0);
        }
    }

    public void enableSearch() {
        animateNavIcon();
        this.adapter.notifyDataSetChanged();
        this.searchEnabled = true;
        Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_left);
        Animation loadAnimation2 = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_left);
        loadAnimation.setAnimationListener(this);
        this.placeHolder.setVisibility(GONE);
        this.inputContainer.setVisibility(VISIBLE);
        this.inputContainer.startAnimation(loadAnimation);
        if (listenerExists()) {
            this.onSearchActionListener.onSearchStateChanged(true);
        }
        this.searchIcon.startAnimation(loadAnimation2);
    }

    private void animateNavIcon() {
        if (this.navIconShown) {
            this.navIcon.setImageResource(R.drawable.ic_menu_animated);
        } else {
            this.navIcon.setImageResource(R.drawable.ic_back_animated);
        }
        Drawable drawable = this.navIcon.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
        this.navIconShown = !this.navIconShown;
    }

    private void animateSuggestions(int i, int i2) {
        this.suggestionsVisible = i2 > 0;
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.last);
        final ViewGroup.LayoutParams layoutParams = relativeLayout.getLayoutParams();
        if (i2 != 0 || layoutParams.height != 0) {
            ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{i, i2});
            ofInt.setDuration(200);
            ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    layoutParams.height = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                    relativeLayout.setLayoutParams(layoutParams);
                }
            });
            if (this.adapter.getItemCount() > 0) {
                ofInt.start();
            }
        }
    }

    public void showSuggestionsList() {
        animateSuggestions(0, getListHeight(false));
    }

    public void hideSuggestionsList() {
        animateSuggestions(getListHeight(false), 0);
    }

    public void clearSuggestions() {
        if (this.suggestionsVisible) {
            animateSuggestions(getListHeight(false), 0);
        }
        this.adapter.clearSuggestions();
    }

    public boolean isSuggestionsVisible() {
        return this.suggestionsVisible;
    }

    public void setMenuIcon(int i) {
        this.menuIconRes = i;
        this.menuIcon.setImageResource(i);
    }

    public void setSearchIcon(int i) {
        this.searchIconRes = i;
        this.searchIcon.setImageResource(i);
    }

    public void setArrowIcon(int i) {
        this.arrowIconRes = i;
        this.arrowIcon.setImageResource(i);
    }

    public void setClearIcon(int i) {
        this.clearIconRes = i;
        this.clearIcon.setImageResource(i);
    }

    public void setNavIconTint(int i) {
        this.navIconTint = i;
        setupNavIconTint();
    }

    public void setMenuIconTint(int i) {
        this.menuIconTint = i;
        setupMenuIconTint();
    }

    public void setSearchIconTint(int i) {
        this.searchIconTint = i;
        setupSearchIconTint();
    }

    public void setArrowIconTint(int i) {
        this.arrowIconTint = i;
        setupArrowIconTint();
    }

    public void setClearIconTint(int i) {
        this.clearIconTint = i;
        setupClearIconTint();
    }

    public void setIconRippleStyle(boolean z) {
        this.borderlessRippleEnabled = z;
        setupIconRippleStyle();
    }

    public void setHint(CharSequence charSequence) {
        this.hintText = charSequence;
        this.searchEdit.setHint(charSequence);
    }

    public void setPlaceHolder(CharSequence charSequence) {
        this.placeholderText = charSequence;
        this.placeHolder.setText(charSequence);
    }

    public void setMenuDividerEnabled(boolean z) {
        this.menuDividerEnabled = z;
        setupMenuDividerEnabled();
    }

    public void setSpeechMode(boolean z) {
        this.speechMode = z;
        if (z) {
            this.searchIcon.setImageResource(this.speechIconRes);
            this.searchIcon.setClickable(true);
            return;
        }
        this.searchIcon.setImageResource(this.searchIconRes);
        this.searchIcon.setClickable(false);
    }

    public boolean isSpeechModeEnabled() {
        return this.speechMode;
    }

    public boolean isSearchEnabled() {
        return this.searchEnabled;
    }

    public void setMaxSuggestionCount(int i) {
        this.maxSuggestionCount = i;
        this.adapter.setMaxSuggestionsCount(i);
    }

    public void setCustomSuggestionAdapter(SuggestionsAdapter suggestionsAdapter) {
        this.adapter = suggestionsAdapter;
        ((RecyclerView) findViewById(R.id.mt_recycler)).setAdapter(this.adapter);
    }

    public List getLastSuggestions() {
        return this.adapter.getSuggestions();
    }

    public void updateLastSuggestions(List list) {
        int listHeight = getListHeight(false);
        if (list.size() > 0) {
            this.adapter.setSuggestions(new ArrayList(list));
            animateSuggestions(listHeight, getListHeight(false));
            return;
        }
        animateSuggestions(listHeight, 0);
    }

    public void setLastSuggestions(List list) {
        this.adapter.setSuggestions(list);
    }

    public void setSuggstionsClickListener(SuggestionsAdapter.OnItemViewClickListener onItemViewClickListener) {
        SuggestionsAdapter suggestionsAdapter = this.adapter;
        if (suggestionsAdapter instanceof DefaultSuggestionsAdapter) {
            ((DefaultSuggestionsAdapter) suggestionsAdapter).setListener(onItemViewClickListener);
        }
    }

    public void setTextColor(int i) {
        this.textColor = i;
        setupTextColors();
    }

    public void setTextHintColor(int i) {
        this.hintColor = i;
        setupTextColors();
    }

    public void setPlaceHolderColor(int i) {
        this.placeholderColor = i;
        setupTextColors();
    }

    public void setTextHighlightColor(int i) {
        this.highlightedTextColor = i;
        this.searchEdit.setHighlightColor(i);
    }

    public void setDividerColor(int i) {
        this.dividerColor = i;
        setupDividerColor();
    }

    public void setNavButtonEnabled(boolean z) {
        this.navButtonEnabled = z;
        if (z) {
            this.navIcon.setVisibility(VISIBLE);
            this.navIcon.setClickable(true);
            this.navIcon.getLayoutParams().width = (int) (this.destiny * 50.0f);
            ((LayoutParams) this.inputContainer.getLayoutParams()).leftMargin = (int) (this.destiny * 50.0f);
            this.arrowIcon.setVisibility(GONE);
        } else {
            this.navIcon.getLayoutParams().width = 1;
            this.navIcon.setVisibility(INVISIBLE);
            this.navIcon.setClickable(false);
            ((LayoutParams) this.inputContainer.getLayoutParams()).leftMargin = (int) (this.destiny * 0.0f);
            this.arrowIcon.setVisibility(VISIBLE);
        }
        this.navIcon.requestLayout();
        this.placeHolder.requestLayout();
        this.arrowIcon.requestLayout();
    }

    public void setRoundedSearchBarEnabled(boolean z) {
        this.roundedSearchBarEnabled = z;
        setupRoundedSearchBarEnabled();
    }

    public void setCardViewElevation(int i) {
        ((CardView) findViewById(R.id.mt_container)).setCardElevation((float) i);
    }

    public void setText(String str) {
        this.searchEdit.setText(str);
    }

    public String getText() {
        return this.searchEdit.getText().toString();
    }

    public void addTextChangeListener(TextWatcher textWatcher) {
        this.searchEdit.addTextChangedListener(textWatcher);
    }

    private boolean listenerExists() {
        return this.onSearchActionListener != null;
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == getId()) {
            if (!this.searchEnabled) {
                enableSearch();
            }
        } else if (id == R.id.mt_arrow) {
            disableSearch();
        } else if (id == R.id.mt_search) {
            if (listenerExists()) {
                this.onSearchActionListener.onButtonClicked(1);
            }
        } else if (id == R.id.mt_clear) {
            this.searchEdit.setText("");
        } else if (id == R.id.mt_menu) {
            this.popupMenu.show();
        } else if (id == R.id.mt_nav && listenerExists()) {
            if (this.navIconShown) {
                this.onSearchActionListener.onButtonClicked(2);
            } else {
                this.onSearchActionListener.onButtonClicked(3);
            }
        }
    }

    public void onAnimationEnd(Animation animation) {
        if (!this.searchEnabled) {
            this.inputContainer.setVisibility(GONE);
            this.searchEdit.setText("");
            return;
        }
        this.searchIcon.setVisibility(GONE);
        this.searchEdit.requestFocus();
        if (!this.suggestionsVisible) {
            showSuggestionsList();
        }
    }

    public void onFocusChange(View view, boolean z) {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (z) {
            inputMethodManager.showSoftInput(view, 1);
        } else {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (listenerExists()) {
            this.onSearchActionListener.onSearchConfirmed(this.searchEdit.getText());
        }
        if (this.suggestionsVisible) {
            hideSuggestionsList();
        }
        SuggestionsAdapter suggestionsAdapter = this.adapter;
        if (!(suggestionsAdapter instanceof DefaultSuggestionsAdapter)) {
            return true;
        }
        suggestionsAdapter.addSuggestion(this.searchEdit.getText().toString());
        return true;
    }

    private int getListHeight(boolean z) {
        float itemCount;
        float f;
        if (!z) {
            itemCount = (float) this.adapter.getListHeight();
            f = this.destiny;
        } else {
            itemCount = (float) ((this.adapter.getItemCount() - 1) * this.adapter.getSingleViewHeight());
            f = this.destiny;
        }
        return (int) (itemCount * f);
    }

    public void OnItemClickListener(int i, View view) {
        if (view.getTag() instanceof String) {
            this.searchEdit.setText((String) view.getTag());
        }
    }

    public void OnItemDeleteListener(int i, View view) {
        if (view.getTag() instanceof String) {
            animateSuggestions(getListHeight(false), getListHeight(true));
            this.adapter.deleteSuggestion(i, (String) view.getTag());
        }
    }


    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        int unused = savedState.isSearchBarVisible = this.searchEnabled ? 1 : 0;
        int unused2 = savedState.suggestionsVisible = this.suggestionsVisible ? 1 : 0;
        int unused3 = savedState.speechMode = this.speechMode ? 1 : 0;
        int unused4 = savedState.navIconResId = this.navIconResId;
        int unused5 = savedState.searchIconRes = this.searchIconRes;
        List unused6 = savedState.suggestions = getLastSuggestions();
        int unused7 = savedState.maxSuggestions = this.maxSuggestionCount;
        CharSequence charSequence = this.hintText;
        if (charSequence != null) {
            String unused8 = savedState.hint = charSequence.toString();
        }
        return savedState;
    }


    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        boolean z = true;
        this.searchEnabled = savedState.isSearchBarVisible == 1;
        if (savedState.suggestionsVisible != 1) {
            z = false;
        }
        this.suggestionsVisible = z;
        setLastSuggestions(savedState.suggestions);
        if (this.suggestionsVisible) {
            animateSuggestions(0, getListHeight(false));
        }
        if (this.searchEnabled) {
            this.inputContainer.setVisibility(VISIBLE);
            this.placeHolder.setVisibility(GONE);
            this.searchIcon.setVisibility(GONE);
        }
    }

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public String hint;
        public int isSearchBarVisible;
        public int maxSuggestions;
        public int navIconResId;
        public int searchIconRes;
        public int speechMode;
        public List suggestions;
        public int suggestionsVisible;

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.isSearchBarVisible);
            parcel.writeInt(this.suggestionsVisible);
            parcel.writeInt(this.speechMode);
            parcel.writeInt(this.searchIconRes);
            parcel.writeInt(this.navIconResId);
            parcel.writeString(this.hint);
            parcel.writeList(this.suggestions);
            parcel.writeInt(this.maxSuggestions);
        }

        public SavedState(Parcel parcel) {
            super(parcel);
            this.isSearchBarVisible = parcel.readInt();
            this.suggestionsVisible = parcel.readInt();
            this.speechMode = parcel.readInt();
            this.navIconResId = parcel.readInt();
            this.searchIconRes = parcel.readInt();
            this.hint = parcel.readString();
            this.suggestions = parcel.readArrayList((ClassLoader) null);
            this.maxSuggestions = parcel.readInt();
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() != 4 || !this.searchEnabled) {
            return super.dispatchKeyEvent(keyEvent);
        }
        animateSuggestions(getListHeight(false), 0);
        disableSearch();
        return true;
    }
}
