class MaxHeap {
    private int[] data;
    private int size;

    public MaxHeap(int capacity) {
        data = new int[capacity];
        size = 0;
    }
    
    private int getLeftChildIndex(int index) { return 2 * index + 1; }
    private int getRightChildIndex(int index) { return 2 * index + 2; }
    private int getParentIndex(int index) { return (index - 1) / 2; }
    
    private boolean hasLeftChild(int index) { return getLeftChildIndex(index) < size; }
    private boolean hasRightChild(int index) { return getRightChildIndex(index) < size; }
    private boolean hasParent(int childIndex) { return getParentIndex(childIndex) >= 0;  }
    
    private void swap(int index1, int index2) {
        int temp = data[index1];
        data[index1] = data[index2];
        data[index2] = temp;
    }
    
    public int pop() {
        if (size < 1) { throw new IllegalStateException("Cannot pop from empty heap"); }
        int maxValue = data[0];
        data[0] = data[size - 1];
        size--;
        heapifyDown();
        return maxValue;
    }
    
    public void push(int value) {
        if (size == data.length) { throw new IllegalStateException("Cannot push on a full Heap"); }
        data[size] = value;
        size++;
        heapifyUp();
    }
    
    private void heapifyUp() {
        int index = size - 1;

        while (hasParent(index) && data[getParentIndex(index)] < data[index]) {
            int parentIndex = getParentIndex(index);
            swap(parentIndex, index);
            index = parentIndex;
        }
    }
    
    private void heapifyDown() {
        int index = 0;
        
        while (hasLeftChild(index)) {
            int largestIndex = getLeftChildIndex(index);
            
            if (hasRightChild(index) && data[getRightChildIndex(index)] > data[largestIndex]) {
                largestIndex = getRightChildIndex(index);
            }
            
            if (data[largestIndex] < data[index]) {
                break;
            } else {
                swap(largestIndex, index);
                index = largestIndex;
            }
        }
    }
}