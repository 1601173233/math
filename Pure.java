package planningmanagement;

/**
 * 单纯形式
 */
public class Pure {

    /**
     * 计算线性规划
     *
     * @param conditionArray 条件参数
     * @param conditionResult 条件结果矩阵
     * @param resultArray 结果参数
     * @return 结果集
     */
    public static Double getMax(double[][] conditionArray,
                                 double[] conditionResult,
                                 double[] resultArray,
                                 double best) {
        double[] conditionParams = new double[conditionArray[0].length];
        int[] notNullIndex = new int[conditionResult.length];
        int[] nullIndex = new int[conditionResult.length];

        for (int i = 0; i < nullIndex.length; i++) {
            nullIndex[i] = i;
        }

        for (int i = 0; i < notNullIndex.length; i++) {
            notNullIndex[i] = conditionParams.length + i - notNullIndex.length;
        }

        // 初始解
        for (int i = 0; i < notNullIndex.length; i++) {
            conditionParams[notNullIndex[i]] = conditionResult[i];
        }

        // 出入基
        while (!isBest(resultArray)) {
            // 入基下标
            int inIndex = getInIndex(nullIndex, resultArray);

            // 出基下标
            int outIndex = getOutIndex(conditionArray, conditionResult, notNullIndex, nullIndex, inIndex);

            int nullIndexNum = nullIndex[inIndex];
            int notNullIndexNum = notNullIndex[outIndex];

            notNullIndex[outIndex] = nullIndexNum;
            nullIndex[outIndex] = notNullIndexNum;

            double conditionNum = conditionArray[outIndex][inIndex];
            for (int i = 0; i < conditionArray[0].length; i++) {
                conditionArray[outIndex][i] /= conditionNum;
            }

            conditionResult[outIndex] = conditionResult[outIndex] / conditionNum;

            for (int i = 0; i < conditionArray.length; i++) {
                if (i != outIndex) {
                    double outConditionNum = conditionArray[i][outIndex];
                    for (int j = 0; j < conditionArray[0].length; j++) {
                        conditionArray[i][j] += - outConditionNum * conditionArray[outIndex][j];
                    }

                    conditionResult[i] += -outConditionNum * conditionResult[outIndex];
                }
            }

            double outConditionNum = resultArray[outIndex];
            for (int i = 0; i < resultArray.length; i++) {
                resultArray[i] += -outConditionNum * conditionArray[outIndex][i];
            }

            best += -outConditionNum * conditionResult[outIndex];
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
     * @param nullIndex 非基变量数组
     * @param resultArray 结果系数
     * @return 入基变量
     */
    public static int getInIndex(int[] nullIndex, double[] resultArray) {
        // 获取入基变量
        int inIndex = 0;
        double inMin = 0;
        for (int i = 0; i < nullIndex.length; i++) {
            int index = nullIndex[i];
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
     * @param conditionArray 条件系数
     * @param conditionResult 条件结果
     * @param notNullIndex 基变量数组
     * @param nullIndex 非基变量数组
     * @param inIndex 非基变量数组下标
     * @return 出基变量
     */
    public static int getOutIndex(double[][] conditionArray,
                                  double[] conditionResult,
                                  int[] notNullIndex,
                                  int[] nullIndex,
                                  int inIndex) {

        int outIndex = 0;
        double outMin = Double.MAX_VALUE;
        for (int i = 0; i < notNullIndex.length; i++) {
            if (conditionArray[i][nullIndex[inIndex]] == 0) {
                continue;
            }

            double temp = conditionResult[i] / conditionArray[i][nullIndex[inIndex]];

            if (temp > 0 && temp < outMin) {
                outIndex = i;
                outMin = temp;
            }
        }

        return outIndex;
    }

    public static void main(String[] args) {
        double[][] conditionArray = new double[][]{{6, 4}, {1, 2}, {-1, 1}, {0, 1}};
        double[] conditionResult = new double[]{24, 6, 1, 2};
        double[] resultArray = new double[]{-5, -4};

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
        }

        System.out.println(getMax(mConditionArray, conditionResult, mResultArray, 0));
    }
}
