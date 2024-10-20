import java.util.Arrays;

class RangeMinQuerySqrt {
    private int[] arr;
    private int[] block;
    private int blockSize;

    public RangeMinQuerySqrt(int[] input) {
        arr = input;
        blockSize = (int) Math.ceil(Math.sqrt(arr.length));
        block = new int[blockSize];
        Arrays.fill(block, Integer.MAX_VALUE);

        // Preprocessing to fill block array
        for (int i = 0; i < arr.length; i++) {
            int blockIndex = i / blockSize;
            block[blockIndex] = Math.min(block[blockIndex], arr[i]);
        }
    }

    public int query(int left, int right) {
        int min = Integer.MAX_VALUE;
        while (left <= right) {
            if (left % blockSize == 0 && left + blockSize - 1 <= right) {
                // If the entire block is within the range
                min = Math.min(min, block[left / blockSize]);
                left += blockSize;
            } else {
                // Partial block
                min = Math.min(min, arr[left]);
                left++;
            }
        }
        return min;
    }

    public void update(int index, int value) {
        int blockIndex = index / blockSize;
        arr[index] = value;

        // Recalculate the minimum value for the affected block
        block[blockIndex] = Integer.MAX_VALUE;
        int start = blockIndex * blockSize;
        int end = Math.min(start + blockSize, arr.length);
        for (int i = start; i < end; i++) {
            block[blockIndex] = Math.min(block[blockIndex], arr[i]);
        }
    }
}


//sparse tree

class RangeMinQuerySparseTable {
    private int[][] sparseTable;
    private int[] log;

    public RangeMinQuerySparseTable(int[] input) {
        int n = input.length;
        int maxLog = (int) (Math.log(n) / Math.log(2)) + 1;
        sparseTable = new int[n][maxLog];
        log = new int[n + 1];

        // Preprocessing log values
        log[1] = 0;
        for (int i = 2; i <= n; i++) {
            log[i] = log[i / 2] + 1;
        }

        // Initializing the sparse table
        for (int i = 0; i < n; i++) {
            sparseTable[i][0] = input[i];
        }

        // Building the sparse table
        for (int j = 1; j <= log[n]; j++) {
            for (int i = 0; i + (1 << j) <= n; i++) {
                sparseTable[i][j] = Math.min(sparseTable[i][j - 1], sparseTable[i + (1 << (j - 1))][j - 1]);
            }
        }
    }

    public int query(int left, int right) {
        int j = log[right - left + 1];
        return Math.min(sparseTable[left][j], sparseTable[right - (1 << j) + 1][j]);
    }
}
