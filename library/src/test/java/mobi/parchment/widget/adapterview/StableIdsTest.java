package mobi.parchment.widget.adapterview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import mobi.parchment.widget.adapterview.listview.ListLayoutManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by Emir Hasanbegovic
 */
@RunWith(RobolectricTestRunner.class)
public class StableIdsTest {

    public static final int VIEW_GROUP_SIZE = 299;
    public static final int VIEW_SIZE = 10;
    public static final int CELL_SPACING = 10;
    final MyViewGroup mViewGroup = new MyViewGroup(Robolectric.application);
    final AdapterViewManager adapterViewManager = new AdapterViewManager();
    TestAdapter mTestAdapter;
    LayoutManagerAttributes attributes;
    ListLayoutManager listLayoutManager;

    @Test
     public void canRecycleView(){
        attributes = new LayoutManagerAttributes(false, true, false, 0, SnapPosition.center, CELL_SPACING, true, true, false);

        listLayoutManager = new ListLayoutManager(mViewGroup, null, adapterViewManager, attributes);
        mTestAdapter = new TestAdapter(VIEW_SIZE);
        mTestAdapter.setAdapterSize(10);
        adapterViewManager.setAdapter(mTestAdapter);
        doFirstLayout(VIEW_GROUP_SIZE);
        doLayout();
        assertThat(mViewGroup.mViews.get(0).getTag()).isEqualTo(0);
        mViewGroup.mViews.get(0).setTag(1);
        mTestAdapter.notifyDataSetChanged();
        doLayout();
        assertThat(mViewGroup.mViews.get(0).getTag()).isEqualTo(1);
    }

    @Test
    public void notGettingViewMoreThanOneInstanceOfViewOnScreen(){

        attributes = new LayoutManagerAttributes(false, true, false, 0, SnapPosition.center, CELL_SPACING, true, true, false);

        listLayoutManager = new ListLayoutManager(mViewGroup, null, adapterViewManager, attributes);
        mTestAdapter = new TestAdapter(VIEW_SIZE);
        mTestAdapter.setAdapterSize(1);
        adapterViewManager.setAdapter(mTestAdapter);
        doFirstLayout(VIEW_GROUP_SIZE);
        doLayout();
        mTestAdapter.notifyDataSetChanged();
        doLayout();
        mTestAdapter.setAdapterSize(2);
        doLayout();
        assertThat(mViewGroup.mViews.get(0)).isNotEqualTo(mViewGroup.mViews.get(1));
    }

    @Test
    public void canRecycleViewsTwice(){
        attributes = new LayoutManagerAttributes(false, true, false, 0, SnapPosition.center, CELL_SPACING, true, true, false);

        listLayoutManager = new ListLayoutManager(mViewGroup, null, adapterViewManager, attributes);
        mTestAdapter = new TestAdapter(VIEW_SIZE);
        mTestAdapter.setAdapterSize(10);
        adapterViewManager.setAdapter(mTestAdapter);
        doFirstLayout(VIEW_GROUP_SIZE);
        doLayout();
        assertThat(mViewGroup.mViews.get(0).getTag()).isEqualTo(0);
        mViewGroup.mViews.get(0).setTag(1);
        mTestAdapter.notifyDataSetChanged();
        doLayout();
        assertThat(mViewGroup.mViews.get(0).getTag()).isEqualTo(1);
        mTestAdapter.notifyDataSetChanged();
        doLayout();
        assertThat(mViewGroup.mViews.get(0).getTag()).isEqualTo(1);
    }

    private void doLayout() {
        doLayout(new Animation());
    }

    private void doLayout(Animation animation) {
        listLayoutManager.layout(mViewGroup, animation, false, 0, 0, VIEW_GROUP_SIZE, VIEW_GROUP_SIZE);
    }

    private void doFirstLayout(int viewGroupSize) {
        final int measureSpec = View.MeasureSpec.makeMeasureSpec(VIEW_GROUP_SIZE, View.MeasureSpec.EXACTLY);
        mViewGroup.measure(measureSpec, measureSpec);
        mViewGroup.layout(0, 0, viewGroupSize, viewGroupSize);
    }

    public class MyViewGroup extends LinearLayout implements AdapterViewHandler {
        public final List<View> mViews = new ArrayList<View>();

        public MyViewGroup(Context context) {
            super(context);
        }

        public View forPosition(int position) {
            Collections.sort(mViews, new Comparator<View>() {
                @Override
                public int compare(View lhs, View rhs) {
                    return lhs.getLeft() - rhs.getLeft();
                }
            });

            return mViews.get(position);
        }

        @Override
        public boolean addViewInAdapterView(View view, int index, ViewGroup.LayoutParams layoutParams) {
            mViews.add(index, view);
            return true;
        }

        @Override
        public void removeViewInAdapterView(View view) {
            mViews.remove(view);
        }
    }

    public class TestAdapter extends BaseAdapter {
        private int mAdapterSize;
        private int mViewSize;

        @Override
        public boolean hasStableIds() {
            return true;
        }

        public TestAdapter(int viewSize) {
            mViewSize = viewSize;
        }

        public void setAdapterSize(final int adapterSize) {
            mAdapterSize = adapterSize;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mAdapterSize;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = new FrameLayout(Robolectric.application);
            }

            final FrameLayout outer = (FrameLayout) convertView;
            outer.setTag(position);
            outer.setLayoutParams(new ViewGroup.LayoutParams(mViewSize, mViewSize));

            // TODO: necessary to have an outer and an inner?
            final FrameLayout inner = new FrameLayout(Robolectric.application);
            inner.setLayoutParams(new ViewGroup.LayoutParams(mViewSize, mViewSize));
            outer.addView(inner);
            return outer;
        }
    }
}
