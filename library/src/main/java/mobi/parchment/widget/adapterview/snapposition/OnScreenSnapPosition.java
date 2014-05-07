package mobi.parchment.widget.adapterview.snapposition;

import android.view.View;

import java.util.List;

import mobi.parchment.widget.adapterview.LayoutManager;
import mobi.parchment.widget.adapterview.Move;
import mobi.parchment.widget.adapterview.ScrollDirectionManager;

/**
 * Created by Emir Hasanbegovic on 2014-03-11.
 */
public class OnScreenSnapPosition<Cell> implements SnapPositionInterface<Cell> {

    @Override
    public int getDrawLimitMoveForwardOverDrawAdjust(final LayoutManager<Cell> layoutManager, final List<Cell> cells, final int size, final Cell cell) {
        final int startSizePadding = layoutManager.getStartSizePadding();
        final int endSizePadding = layoutManager.getEndSizePadding();
        final int cellSize = layoutManager.getCellSize(cell);

        final int drawLimit = startSizePadding + cellSize;

        final int lastCellIndex = cells.size() - 1;
        final Cell lastCell = cells.get(lastCellIndex);
        final View lastView = layoutManager.getLastAdapterPositionView(lastCell);
        final int lastViewPosition = layoutManager.getPosition(lastView);
        final boolean isLastItemOnScreen = lastViewPosition == layoutManager.getAdapterCount() - 1;

        final Cell firstCell = cells.get(0);
        final View firstView = layoutManager.getFirstAdapterPositionView(firstCell);
        final int firstViewPosition = layoutManager.getPosition(firstView);
        final boolean isFirstItemOnScreen = firstViewPosition == 0;

        if (isFirstItemOnScreen && isLastItemOnScreen) {
            final int cellSizeTotal = layoutManager.getCellSizeTotal();
            final int sizeTotal = size + startSizePadding + endSizePadding;
            if (cellSizeTotal <= sizeTotal) {
                final int firstCellSize = layoutManager.getCellSize(firstCell);
                return firstCellSize + (sizeTotal - cellSizeTotal) / 2;
            }
        } else if (isLastItemOnScreen) {
            final int cellSizeTotal = layoutManager.getCellSizeTotal();
            return Math.max(drawLimit, startSizePadding + size - (cellSizeTotal - cellSize));
        }

        return drawLimit;
    }

    @Override
    public int getDrawLimitMoveBackwardOverDrawAdjust(LayoutManager<Cell> layoutManager, List<Cell> cells, int size, Cell cell) {
        final int startSizePadding = layoutManager.getStartSizePadding();
        final int endSizePadding = layoutManager.getEndSizePadding();
        final int cellSize = layoutManager.getCellSize(cell);
        final int drawLimit = startSizePadding + size - cellSize;

        final Cell firstCell = cells.get(0);
        final View firstView = layoutManager.getFirstAdapterPositionView(firstCell);
        final int firstViewPosition = layoutManager.getPosition(firstView);
        final boolean isFirstItemOnScreen = firstViewPosition == 0;

        final int lastCellIndex = cells.size() - 1;
        final Cell lastCell = cells.get(lastCellIndex);
        final View lastView = layoutManager.getLastAdapterPositionView(lastCell);
        final int lastViewPosition = layoutManager.getPosition(lastView);
        final boolean isLastItemOnScreen = lastViewPosition == layoutManager.getAdapterCount() - 1;

        if (isFirstItemOnScreen && isLastItemOnScreen) {
            final int cellSizeTotal = layoutManager.getCellSizeTotal();
            final int sizeTotal = size + startSizePadding + endSizePadding;
            if (cellSizeTotal <= sizeTotal) {
                final int x = (sizeTotal - cellSizeTotal) / 2 ;
                return sizeTotal - x - cellSize;
            }
        }

        if (isFirstItemOnScreen) {
            return Math.min(drawLimit, startSizePadding + layoutManager.getCellSizeTotal() - cellSize);
        }

        return drawLimit;
    }

    private Integer getCellDisplacementFromStartSnapPosition(final LayoutManager<Cell> layoutManager, final Cell cell) {
        if (cell == null) {
            return null;
        }
        final int startSizePadding = layoutManager.getStartSizePadding();
        final int currentCellStart = layoutManager.getCellStart(cell);
        return startSizePadding - currentCellStart;
    }

    private Integer getCellDisplacementFromEndSnapPosition(LayoutManager<Cell> layoutManager, int size, Cell cell) {
        if (cell == null) {
            return null;
        }
        final int currentCellEnd = layoutManager.getCellEnd(cell);
        final int startSizePadding = layoutManager.getStartSizePadding();
        final int displacement = startSizePadding + size - currentCellEnd;
        return displacement;
    }

    @Override
    public int getDisplacementFromSnapPosition(LayoutManager<Cell> layoutManager, int size, Cell firstPosition, Cell lastPosition, Move move) {
        final Integer firstDisplacement = getCellDisplacementFromSnapPosition(layoutManager, size, firstPosition);
        final Integer lastDisplacement = getCellDisplacementFromSnapPosition(layoutManager, size, lastPosition);

        if (firstDisplacement != null && lastDisplacement != null) {
            if (firstDisplacement > 0 && lastDisplacement > 0) { //Both to the left
                final int cellSizeTotal = layoutManager.getCellSizeTotal();
                if (cellSizeTotal > size) {
                    return getCellDisplacementFromEndSnapPosition(layoutManager, size, lastPosition);
                } else {
                    return firstDisplacement;
                }
            }

            if (firstDisplacement > 0 && lastDisplacement < 0) { // Both are off screen
                return 0;
            }

            if (firstDisplacement <= 0 && lastDisplacement >= 0) { // Both are fully on screen
                final int cellSizeTotal = layoutManager.getCellSizeTotal();
                final int startSizePadding = layoutManager.getStartSizePadding();
                final int endSizePadding = layoutManager.getEndSizePadding();
                final int sizeTotal = size + startSizePadding + endSizePadding;
                final int sizeStartPosition = (sizeTotal - cellSizeTotal) / 2;
                final int firstSizeStartPosition = layoutManager.getCellStart(firstPosition);
                return sizeStartPosition - firstSizeStartPosition;
            }

            if (firstDisplacement <= 0 && lastDisplacement <= 0) { // Both to the right
                final int cellSizeTotal = layoutManager.getCellSizeTotal();
                if (cellSizeTotal > size) {
                    return getCellDisplacementFromStartSnapPosition(layoutManager, firstPosition);
                } else {
                    return lastDisplacement;
                }
            }

        }

        if (firstDisplacement != null && firstDisplacement < 0) {
            return firstDisplacement;
        } else if (lastDisplacement != null && lastDisplacement > 0) {
            return lastDisplacement;
        }

        return 0;
    }

    private Integer getCellDisplacementFromSnapPosition(LayoutManager<Cell> layoutManager, int size, Cell cell) {
        if (cell == null) {
            return null;
        }
        final int startSizePadding = layoutManager.getStartSizePadding();
        final int currentCellStart = layoutManager.getCellStart(cell);
        final int currentCellEnd = layoutManager.getCellEnd(cell);
        if (currentCellStart < startSizePadding && currentCellEnd < startSizePadding + size) {
            final int displacement = startSizePadding - currentCellStart;
            return displacement;
        } else if (currentCellEnd > startSizePadding + size && currentCellStart > startSizePadding) {
            final int displacement = startSizePadding + size - currentCellEnd;
            return displacement;
        }
        return 0;
    }

    @Override
    public int getCellDistanceFromSnapPosition(LayoutManager<Cell> layoutManager, int size, Cell cell) {
        final int displacement = getCellDisplacementFromSnapPosition(layoutManager, size, cell);
        return Math.abs(displacement);
    }

    @Override
    public int getSnapToPixelDistance(LayoutManager<Cell> layoutManager, ScrollDirectionManager scrollDirectionManager, int size, View view) {
        final int startSizePadding = layoutManager.getStartSizePadding();
        final int endSizePadding = layoutManager.getEndSizePadding();
        final int startPixel = scrollDirectionManager.getViewStart(view);
        final int endPixel = scrollDirectionManager.getViewEnd(view);
        final int viewSize = endPixel - startPixel;

        final int actualSize = size - startSizePadding - endSizePadding;
        final boolean viewIsLargerThanViewGroup = actualSize <= viewSize;
        if (startPixel < startSizePadding || viewIsLargerThanViewGroup) {
            return -startPixel + startSizePadding;
        } else if (endPixel > startSizePadding + size) {
            return startSizePadding + size - endPixel;
        }

        return 0;
    }

    @Override
    public int getRedrawOffset(final ScrollDirectionManager scrollDirectionManager, final View incomingView, final View outgoingView) {
        final int outgoingViewStart = scrollDirectionManager.getViewStart(outgoingView);
        return outgoingViewStart;
    }

    @Override
    public int getAbsoluteSnapPosition(final LayoutManager<Cell> layoutManager, final int size, final int cellSize, final Move move) {
        final int startSizePadding = layoutManager.getStartSizePadding();
        switch (move) {
            case back:
                return startSizePadding + size - cellSize;
            case forward:
            case none:
            default:
                return startSizePadding;
        }
    }
}
