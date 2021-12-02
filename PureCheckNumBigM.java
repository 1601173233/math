package planningmanagement;

/**
 * 单纯形式（大M法）
 */
public class PureCheckNumBigM {
    /**
     * 设置大M
     *
     * @param pureParams 参数
     */
    public static void convertStandard (PureParams pureParams) {
        if (pureParams.needBigMIndex == null) {
            PureCheckNum.convertStandard(pureParams);
        }

        double[][] conditionArray = pureParams.conditionArray;
        double[] resultArray = pureParams.resultArray;
        double[][] mConditionArray = new double[conditionArray.length][pureParams.needBigMIndex.length + conditionArray[0].length];
        double[] mResultArray = new double[pureParams.needBigMIndex.length + resultArray.length];

        for (int i = 0; i < conditionArray.length; i++) {
            for (int j = 0; j < conditionArray[0].length; j++) {
                mConditionArray[i][j] = conditionArray[i][j];
            }
        }

        for (int i = 0; i < pureParams.needBigMIndex.length; i++) {
            mConditionArray[i][conditionArray[0].length + i] = 1;
        }

        for (int i = 0; i < resultArray.length; i++) {
            mResultArray[i] = resultArray[i];
        }

        for (int j = 0; j < pureParams.needBigMIndex.length; j++) {
            mResultArray[pureParams.needBigMIndex[j] + resultArray.length] = pureParams.bigM;
        }

        pureParams.conditionArray = mConditionArray;
        pureParams.resultArray = mResultArray;

        PureCheckNum.convertStandard(pureParams);

//        MathUtil.printResult(pureParams.resultArray);
//        System.out.println();
//        MathUtil.printResult(pureParams.conditionArray);
//        System.out.println();
//        MathUtil.printResult(pureParams.inIndexArray);
//        System.out.println();
//        MathUtil.printResult(pureParams.outIndexArray);
    }

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


        int M = -100;
        double[][] conditionArray = new double[][]{{3, 1, 0}, {4, 3, -1}, {1, 2, 0}};
        double[] conditionResult = new double[]{3, 6, 4};
        double[] resultArray = new double[]{-4, -1, 0};

//        int M = -10000;
//        // 大M的下标
//        int[] eqIndex = {0, 1, 2, 3, 4};
//        double[][] conditionArray = new double[][]{{1, 1, 0, 0, 0, 0},
//                {0, 0, 1, 1, 0, 0},
//                {0, 0, 0, 0, 1, 1},
//                {1, 0, 1, 0, 1, 0},
//                {0, 1, 0, 1, 0, 1}};
//        double[] conditionResult = new double[]{1000, 1500, 1200, 2300, 1400};
//        double[] resultArray = new double[]{80, 215, 100, 108, 102, 68};

        PureParams pureParams = new PureParams();
        pureParams.resultArray = resultArray;
        pureParams.conditionArray = conditionArray;
        pureParams.conditionResult = conditionResult;
        pureParams.needBigMIndex = new int[]{0, 1};
        pureParams.best = 0;
        pureParams.bigM = M;

        convertStandard(pureParams);
        System.out.println(PureCheckNum.getMax(pureParams));
    }
}
