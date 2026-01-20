package mate.academy;

import java.util.List;
import java.util.concurrent.RecursiveAction;

public class MergeSortAction extends RecursiveAction {
    private static final int THRESHOLD = 32;

    private final int[] initialArray;
    private final int startIndex;
    private final int endIndex;

    public MergeSortAction(int[] array) {
        this(array, 0, array.length);

    }

    private MergeSortAction(int[] array, int startIndex, int endIndex) {
        this.initialArray = array;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    protected void compute() {
        if (endIndex - startIndex > THRESHOLD) {
            int midIndex = startIndex + (endIndex - startIndex) / 2;
            invokeAll(createSubTasks(midIndex));
            merge(midIndex);
        } else {
            bubbleSort();
        }
    }

    private void bubbleSort() {
        for (int i = startIndex; i < endIndex - 1; i++) {
            for (int j = startIndex; j < endIndex - 1 - (i - startIndex); j++) {
                if (initialArray[j] > initialArray[j + 1]) {
                    int temp = initialArray[j];
                    initialArray[j] = initialArray[j + 1];
                    initialArray[j + 1] = temp;
                }
            }
        }
    }

    private List<RecursiveAction> createSubTasks(int midIndex) {
        RecursiveAction leftAction = new MergeSortAction(initialArray, startIndex, midIndex);
        RecursiveAction rightAction = new MergeSortAction(initialArray, midIndex, endIndex);
        return List.of(leftAction, rightAction);
    }

    private void merge(int midIndex) {
        int[] temp = new int[endIndex - startIndex];
        int leftIndex = startIndex;
        int rightIndex = midIndex;
        int iterator = 0;
        while (leftIndex < midIndex && rightIndex < endIndex) {
            if (initialArray[leftIndex] <= initialArray[rightIndex]) {
                temp[iterator++] = initialArray[leftIndex++];
            } else {
                temp[iterator++] = initialArray[rightIndex++];
            }
        }
        while (leftIndex < midIndex) {
            temp[iterator++] = initialArray[leftIndex++];
        }
        while (rightIndex < endIndex) {
            temp[iterator++] = initialArray[rightIndex++];
        }
        System.arraycopy(temp, 0, initialArray, startIndex, temp.length);
    }
}
