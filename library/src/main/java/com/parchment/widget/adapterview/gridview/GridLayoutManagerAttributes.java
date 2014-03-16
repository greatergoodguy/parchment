package com.parchment.widget.adapterview.gridview;

import com.parchment.widget.adapterview.LayoutManagerAttributes;
import com.parchment.widget.adapterview.Orientation;
import com.parchment.widget.adapterview.SnapPosition;

/**
 * Created by Emir Hasanbegovic on 2014-02-25.
 */
public class GridLayoutManagerAttributes extends LayoutManagerAttributes {
    private final int mNumberOfColumns;
    private final Orientation mOrientation;
    private boolean mIsLeft;
    private boolean mIsRight;
    private boolean mIsTop;
    private boolean mIsBottom;

    public GridLayoutManagerAttributes(final int numberOfColumns, final boolean isCircularScroll, final boolean snapToPosition,
                                       final boolean isViewPager, final int viewPagerInterval, final SnapPosition snapPosition,
                                       final int cellSpacing, final boolean selectOnSnap,
                                       final boolean selectWhileScrolling, final boolean isVertical,
                                       final boolean isTop, final boolean isBottom, final boolean isLeft,
                                       final boolean isRight) {
        super(isCircularScroll, snapToPosition, isViewPager, viewPagerInterval, snapPosition, cellSpacing, selectOnSnap, selectWhileScrolling, isVertical);
        mNumberOfColumns = numberOfColumns;
        if (isVertical){
            mOrientation = Orientation.vertical;
        } else {
            mOrientation = Orientation.horizontal;
        }
        mIsTop = isTop;
        mIsBottom = isBottom;
        mIsLeft = isLeft;
        mIsRight = isRight;
    }

    public int getNumberOfColumns(){
        return mNumberOfColumns;
    }

    public Orientation getOrientation() {
        return mOrientation;
    }

    public boolean isTop() {
        return mIsTop;
    }

    public boolean isBottom() {
        return mIsBottom;
    }

    public boolean isLeft() {
        return mIsLeft;
    }

    public boolean isRight() {
        return mIsRight;
    }
}
