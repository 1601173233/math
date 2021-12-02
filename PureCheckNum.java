package planningmanagement;

/**
 * 单纯形式（检验数法）
 *
 * 标准型式
 * （1）目标函数一定是最大值函数；
 * （2）约束条件必须全都是等式；
 * （3）约束条件的右端必须为正数；
 * （4）所有变量的取值都必须是非负数。
 *  参考：
 *  作者：三工景页 https://www.bilibili.com/read/cv5287905 出处：bilibili
 */
public class PureCheckNum {

    /**
     * 计算线性规划
     *
     * @param pureParams 参数
     * @return 结果集
     */
    public static double getMax(PureParams pureParams) {
        double best = pureParams.best;

        int time = 0;
        while (true) {
            System.out.println("第" + ++time + "次循环");
            // 1.计算检验数
            double[] checkNumArray = getCheckNum(pureParams);

            int inIndex = getInIndex(checkNumArray);
            if (inIndex == -1) {
                break;
            }

            int outIndex = getOutIndex(pureParams, inIndex);

            System.out.println("入基变量:" + (pureParams.outIndexArray[inIndex] + 1) + "，出基变量:" + (pureParams.inIndexArray[outIndex] + 1));
            inOutCal(pureParams, inIndex, outIndex);
        }

        // 计算结果集
        double[] resultArray = pureParams.resultArray;

        double[] xArray = new double[resultArray.length];
        for (int inIndex = 0 ; inIndex < pureParams.inIndexArray.length; inIndex++) {
            xArray[pureParams.inIndexArray[inIndex]] = pureParams.conditionResult[inIndex];
        }

        for (int i = 0; i < resultArray.length; i++) {
            best += xArray[i] * resultArray[i];
        }

        return best;
    }

    /**
     * 获取检验数
     *
     * @return 检验数数组
     */
    public static double[] getCheckNum(PureParams pureParams) {
        double[] checkNumArray = new double[pureParams.outIndexArray.length];

        for (int i = 0; i < pureParams.outIndexArray.length;i++) {
            int outIndex = pureParams.outIndexArray[i];

            checkNumArray[i] = pureParams.resultArray[outIndex];
            for (int j = 0; j < pureParams.inIndexResultArray.length; j++) {
                checkNumArray[i] -= pureParams.conditionArray[j][outIndex] * pureParams.inIndexResultArray[j];
            }
        }

        MathUtil.printResult(checkNumArray);
        System.out.println();

        return checkNumArray;
    }

    /**
     * 获取入基变量下标
     *
     * @param checkNumArray 校验数数组
     * @return 入基变量下标
     */
    public static int getInIndex(double[] checkNumArray) {
        double max = 0d;
        int inIndex = -1;
        for (int i = 0; i < checkNumArray.length; i++) {
            if (max < checkNumArray[i]) {
                max = checkNumArray[i];
                inIndex = i;
            }
        }

        return inIndex;
    }

    /**
     * 获取出基变量的下标
     *
     * @return 出基变量的下标
     */
    public static int getOutIndex(PureParams pureParams, int inIndex) {
        int outIndex = -1;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < pureParams.inIndexArray.length; i++) {
            double condition = pureParams.conditionArray[i][pureParams.outIndexArray[inIndex]];
            if (condition == 0) {
                continue;
            }

            double scale = pureParams.conditionResult[i] / condition;
            if (scale > 0 && scale < min) {
                min = scale;
                outIndex = i;
            }
        }

        return outIndex;
    }

    /**
     * 出入基变换
     *
     * @return
     */
    public static void inOutCal(PureParams pureParams, int inIndex, int outIndex) {
        // 基下标交换
        int outIndexNum = pureParams.inIndexArray[outIndex];
        int inIndexNum = pureParams.outIndexArray[inIndex];
        pureParams.inIndexArray[outIndex] = inIndexNum;
        pureParams.outIndexArray[inIndex] = outIndexNum;

        // 矩阵变换
        // 入基那一行，系数变为1
        double[][] conditionArray = pureParams.conditionArray;
        double[] conditionResult = pureParams.conditionResult;
        double divisor = conditionArray[outIndex][inIndexNum];
        for (int i = 0; i < conditionArray[0].length; i++) {
            conditionArray[outIndex][i] /= divisor;
        }

        conditionResult[outIndex] /= divisor;
        pureParams.inIndexResultArray[outIndex] = pureParams.resultArray[inIndexNum];

        // 剩余行全部下标都为0
        for (int i = 0; i < conditionResult.length; i++) {
            if (i == outIndex) {
                continue;
            }

            divisor = conditionArray[i][inIndexNum];
            for (int j = 0; j < conditionArray[i].length; j++) {
                conditionArray[i][j] -= divisor * conditionArray[outIndex][j];
            }
            conditionResult[i] -= divisor * conditionResult[outIndex];
        }

//        MathUtil.printResult(pureParams.resultArray);
//        System.out.println();
//        MathUtil.printResult(pureParams.conditionArray);
//        System.out.println();
//        MathUtil.printResult(pureParams.inIndexArray);
//        System.out.println();
//        MathUtil.printResult(pureParams.outIndexArray);
//        System.out.println();
//        MathUtil.printResult(pureParams.inIndexResultArray);
    }

    /**
     * 化为标准型式
     *
     * @param pureParams 参数
     */
    public static void convertStandard (PureParams pureParams) {
        double[][] conditionArray = pureParams.conditionArray;
        double[] resultArray = pureParams.resultArray;
        boolean[] hasOneArray = new boolean[conditionArray.length];
        pureParams.inIndexArray = new int[conditionArray.length];
        pureParams.inIndexResultArray = new double[conditionArray.length];
        int count = 0;

        for (int i = 0; i < conditionArray[0].length; i++) {
            int hasOne = 0;
            int index = 0;
            for (int j = 0; j < conditionArray.length; j++) {
                if (conditionArray[j][i] == 1) {
                    if (hasOne == 0) {
                        hasOne = 1;
                        index = j;
                    } else {
                        hasOne = 2;
                        break;
                    }
                } else if (conditionArray[j][i] != 0){
                    hasOne = 0;
                    break;
                }
            }

            if (hasOne == 1) {
                hasOneArray[index] = true;
                pureParams.inIndexArray[index] = i;
                pureParams.inIndexResultArray[index] = resultArray[i];
                count++;
            }
        }

        double[][] mConditionArray = new double[conditionArray.length][conditionArray.length - count + conditionArray[0].length];
        double[] mResultArray = new double[conditionArray.length - count + conditionArray[0].length];
        pureParams.outIndexArray = new int[conditionArray[0].length - count];

        for (int i = 0; i < conditionArray.length; i++) {
            for (int j = 0; j < conditionArray[0].length; j++) {
                mConditionArray[i][j] = conditionArray[i][j];
            }
        }

        for (int i = 0, index = 0; i < hasOneArray.length; i++) {
            if (!hasOneArray[i]) {
                mConditionArray[i][conditionArray[0].length + index] = 1;
                pureParams.inIndexArray[i] = conditionArray[0].length + index;
                pureParams.inIndexResultArray[i] = 0;
                index++;
            }
        }

        // 出基下标
        int outIndex = 0;
        for (int i = 0; i < conditionArray[0].length; i++) {
            boolean isSame = false;
            for (int mInIndex : pureParams.inIndexArray) {
                if (mInIndex == i) {
                    isSame = true;
                    break;
                }
            }

            if (!isSame) {
                pureParams.outIndexArray[outIndex++] = i;
            }
        }

        for (int i = 0; i < resultArray.length; i++) {
            mResultArray[i] = resultArray[i];
        }

        pureParams.conditionArray = mConditionArray;
        pureParams.resultArray = mResultArray;

//        MathUtil.printResult(pureParams.resultArray);
//        System.out.println();
//        MathUtil.printResult(pureParams.conditionArray);
//        System.out.println();
//        MathUtil.printResult(pureParams.inIndexArray);
//        System.out.println();
//        MathUtil.printResult(pureParams.outIndexArray);
    }

    public static void main(String[] args) {
//        double[][] conditionArray = new double[][]{{6, 0}, {1, 1}, {-1, 0}, {0, 0}};
//        double[] conditionResult = new double[]{24, 6, 1, 2};
//        double[] resultArray = new double[]{-5, -4};


        double[][] conditionArray = new double[][]{{1, 2}, {4, 0}, {0, 4}};
        double[] conditionResult = new double[]{8, 16, 12};
        double[] resultArray = new double[]{2, 3};

        PureParams pureParams = new PureParams();
        pureParams.resultArray = resultArray;
        pureParams.conditionArray = conditionArray;
        pureParams.conditionResult = conditionResult;
        pureParams.best = 0;

        convertStandard(pureParams);
        getMax(pureParams);
    }
}


/**
 * 参数
 */
class PureParams {
    /** 约束条件 参数矩阵 */
    double[][] conditionArray;

    /** 约束条件结果 */
    double[] conditionResult;

    /** 目标函数的价值系数 */
    double[] resultArray;

    /** 入基变量坐标 */
    int[] inIndexArray;

    /** 出基变量坐标 */
    int[] outIndexArray;

    /** 入基变量 - 价格系数 */
    double[] inIndexResultArray;

    /** 需要大M的下标 */
    int[] needBigMIndex;

    /** 大M的下标 */
    int[] bigMIndex;

    /** 当前最大值 */
    double best;

    /** 大M */
    double bigM;
}