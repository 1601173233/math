package planningmanagement;

/**
 * 单纯形式（大M法）
 */
public class PureBigM {

    public static void main(String[] args) {
//        int M = -10000;
//        // 大M的下标
//        int[] eqIndex = {0, 1, 2, 3, 4};
//        double[][] conditionArray = new double[][]{{1, 1, 0, 0, 0, 0, 0},
//                                                   {-1, 0, 1, 0, 0, -1, 0},
//                                                   {0, -1, -1, 1, 1, 0, 0},
//                                                   {0, 0, 0, -1, 0, 1, 1},
//                                                   {0, 0, 0, 0, -1, 0, -1} };
//        double[] conditionResult = new double[]{1, -1, 0, 0, 0};
//        double[] resultArray = new double[]{100, 30, 20, 10, 60, 15, 50};


//        int M = -10000;
//        // 大M的下标
//        int[] eqIndex = {0, 1};
//        double[][] conditionArray = new double[][]{{3, 1, 0}, {4, 3, -1}, {1, 2, 0}};
//        double[] conditionResult = new double[]{3, 6, 4};
//        double[] resultArray = new double[]{4, 1, 0};

        int M = -10000;
        // 大M的下标
        int[] eqIndex = {0, 1, 2, 3, 4};
        double[][] conditionArray = new double[][]{{1, 1, 0, 0, 0, 0},
                {0, 0, 1, 1, 0, 0},
                {0, 0, 0, 0, 1, 1},
                {1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 1}};
        double[] conditionResult = new double[]{1000, 1500, 1200, 2300, 1400};
        double[] resultArray = new double[]{80, 215, 100, 108, 102, 68};

        double[][] mConditionArray = new double[conditionArray.length][conditionArray.length + conditionArray[0].length];
        double[] mResultArray = new double[conditionArray.length + resultArray.length];

        for (int i = 0; i < conditionArray.length; i++) {
            for (int j = 0; j < conditionArray[0].length; j++) {
                mConditionArray[i][j] = conditionArray[i][j];
            }

            mConditionArray[i][conditionArray[0].length + i] = 1;
        }

        for (int i = 0; i < resultArray.length; i++) {
            mResultArray[i] = resultArray[i];

            for (int j = 0; j < eqIndex.length; j++) {
                mResultArray[i] += conditionArray[eqIndex[j]][i] * M;
            }
        }

        double best = 0;
        for (int j = 0; j < eqIndex.length; j++) {
            best  += conditionResult[eqIndex[j]] * M;
        }

        System.out.println(Pure.getMax(mConditionArray, conditionResult, mResultArray, best));
    }
}
