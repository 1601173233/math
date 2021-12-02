package planningmanagement;

/**
 * 单纯形式
 */
public class Pure {

    /**
     * 计算线性规划
     *
     * @return 结果集
     */
    public static Double getMax(PureParams pureParams) {
        int[] outIndexArray = pureParams.outIndexArray;
        int[] inIndexArray = pureParams.inIndexArray;
        double[][] conditionArray = pureParams.conditionArray;
        double[] conditionResult = pureParams.conditionResult;
        double[] resultArray = pureParams.resultArray;
        double best = pureParams.best;

        // 出入基
        while (!isBest(pureParams.resultArray)) {
            // 入基下标
            int inIndex = getInIndex(pureParams);

            // 出基下标
            int outIndex = getOutIndex(pureParams, inIndex);

            int inIndexNum = outIndexArray[inIndex];
            int outIndexNum = inIndexArray[outIndex];

            inIndexArray[outIndex] = inIndexNum;
            outIndexArray[inIndex] = outIndexNum;

            System.out.println("入基变量:" + (inIndexNum + 1) + "，出基变量:" + (outIndexNum + 1));

            // 矩阵转换
            double divisor = conditionArray[outIndex][inIndexNum];
            for (int i = 0; i < conditionArray[0].length; i++) {
                conditionArray[outIndex][i] /= divisor;
            }
            conditionResult[outIndex] = conditionResult[outIndex] / divisor;

            for (int i = 0; i < conditionArray.length; i++) {
                if (i != outIndex) {
                    divisor = conditionArray[i][inIndexNum];
                    for (int j = 0; j < conditionArray[i].length; j++) {
                        conditionArray[i][j] -= divisor * conditionArray[outIndex][j];
                    }
                    conditionResult[i] -= divisor * conditionResult[outIndex];
                }
            }

            double outConditionNum = resultArray[inIndexNum];
            for (int i = 0; i < resultArray.length; i++) {
                resultArray[i] += -outConditionNum * conditionArray[outIndex][i];
            }

            best += -outConditionNum * conditionResult[outIndex];

//            System.out.println("\r\nresultArray:");
//            MathUtil.printResult(pureParams.resultArray);
//            System.out.println("\nconditionArray:");
//            MathUtil.printResult(pureParams.conditionArray);
//            System.out.println("\nconditionResult:");
//            MathUtil.printResult(pureParams.conditionResult);
//            System.out.println("\ninIndexArray:");
//            MathUtil.printResult(pureParams.inIndexArray);
//            System.out.println("\noutIndexArray:");
//            MathUtil.printResult(pureParams.outIndexArray);
//            System.out.println("\nbest:");
//            System.out.println(best);
//            System.out.println();
        }

        return best;
    }

    /**
     * 测试是否为最优解
     *
     */
    public static boolean isBest(double[] resultArray) {
        for (double i : resultArray) {
            if (i < 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * 获取入基变量
     *
     * @param pureParams 参数
     * @return 入基变量
     */
    public static int getInIndex(PureParams pureParams) {
        // 获取入基变量
        int inIndex = 0;
        double inMin = 0;
        double[] resultArray = pureParams.resultArray;
        int[] outIndexArray = pureParams.outIndexArray;

        for (int i = 0; i < outIndexArray.length; i++) {
            int index = outIndexArray[i];
            if (resultArray[index] < 0 && resultArray[index] < inMin) {
                inIndex = i;
                inMin = resultArray[index];
            }
        }

        return inIndex;
    }

    /**
     * 获取出基变量
     *
     * @param pureParams 参数
     * @param inIndex 非基变量数组下标
     * @return 出基变量
     */
    public static int getOutIndex(PureParams pureParams,
                                  int inIndex) {

        int outIndex = 0;
        double outMin = Double.MAX_VALUE;
        for (int i = 0; i < pureParams.inIndexArray.length; i++) {
            double condition = pureParams.conditionArray[i][pureParams.outIndexArray[inIndex]];
            if (condition == 0) {
                continue;
            }

            double temp = pureParams.conditionResult[i] / condition;

            if (temp > 0 && temp < outMin) {
                outIndex = i;
                outMin = temp;
            }
        }

        return outIndex;
    }

    public static void main(String[] args) {
        double[][] conditionArray = new double[][]{{1, 2}, {4, 0}, {0, 4}};
        double[] conditionResult = new double[]{8, 16, 12};
        double[] resultArray = new double[]{-2, -3};

//        double[][] conditionArray = new double[][]{{6, 4}, {1, 2}, {-1, 1}, {0, 1}};
//        double[] conditionResult = new double[]{24, 6, 1, 2};
//        double[] resultArray = new double[]{-5, -4};

        PureParams pureParams = new PureParams();
        pureParams.resultArray = resultArray;
        pureParams.conditionArray = conditionArray;
        pureParams.conditionResult = conditionResult;
        pureParams.best = 0;

        // 转换为标准型
        PureCheckNum.convertStandard(pureParams);
        System.out.println(getMax(pureParams));
    }
}
