package com.parchment.widget.adapterview.snapposition;

import android.view.View;

import com.parchment.widget.adapterview.LayoutManager;
import com.parchment.widget.adapterview.ScrollDirectionManager;

import java.util.List;

/**
 * Created by Emir Hasanbegovic on 2014-03-11.
 */
public class CenterSnapPosition<Cell> implements SnapPositionInterface<Cell> {

    @Override
    public int getDrawLimitMoveForwardOverDrawAdjust(final LayoutManager<Cell> layoutManager, final List<Cell> cells, final int size, final Cell cell, final int cellSpacing) {
        final int cellSize = layoutManager.getCellSize(cell);
        return (size + cellSize) / 2;
    }

    @Override
    public int getDrawLimitMoveBackwardOverDrawAdjust(LayoutManager<Cell> layoutManager, List<Cell> cells, int size, Cell cell, int cellSpacing) {
        final int cellSize = layoutManager.getCellSize(cell);
        return (size - cellSize) / 2;
    }

    @Override
    public int getCellDistanceFromSnapPosition(final LayoutManager<Cell> layoutManager, final int size, final Cell cell, final int cellSpacing) {
        final int currentCellCenter = layoutManager.getCellCenter(cell);
        return Math.abs(currentCellCenter - cellSpacing);
    }

    @Override
    public int getSnapToPixelDistance(LayoutManager<Cell> layoutManager, ScrollDirectionManager scrollDirectionManager, int size, View view, int cellSpacing) {
        final int viewSize = scrollDirectionManager.getViewSize(view);
        final int startPixel = scrollDirectionManager.getViewStart(view);

        return (size - viewSize) / 2 - startPixel;
    }

    @Override
    public int getRedrawOffset(final ScrollDirectionManager scrollDirectionManager, final View incomingView, final View outgoingView, final int cellSpacing) {
        final int outgoingViewStart = scrollDirectionManager.getViewStart(outgoingView);
        final int incomingViewSize = scrollDirectionManager.getViewSize(incomingView);
        final int outgoingViewSize = scrollDirectionManager.getViewSize(outgoingView);
        final int viewDifference = (outgoingViewSize - incomingViewSize) / 2;

        return outgoingViewStart + viewDifference - cellSpacing;
    }
}
