package planningmanagement;

/**
 * 对偶单纯形式（检验数法）
 *
 * 标准型式
 * （1）目标函数一定是最大值函数；
 * （2）约束条件必须全都是等式；
 * （3）约束条件的右端必须为正数；
 * （4）所有变量的取值都必须是非负数。
 *  参考：
 *  作者：三工景页 https://www.bilibili.com/read/cv5287905 出处：bilibili
 */
public class DualPureCheckNum {

    /**
     * 计算线性规划
     *
     * @param pureParams 参数
     * @return 结果集
     */
    public static double getMax(PureParams pureParams) {
        int outIndex = getDualOutIndex(pureParams);

        int time = 0;
        while (outIndex != -1) {
//            System.out.println("对偶单纯形法： 第" + ++time + "次循环");
            double[] checkNum = PureCheckNum.getCheckNum(pureParams);
            int inIndex = getDualInIndex(pureParams, checkNum, outIndex);

//            System.out.println("入基变量:" + (pureParams.outIndexArray[inIndex] + 1) + "，出基变量:" + (pureParams.inIndexArray[outIndex] + 1));
            PureCheckNum.inOutCal(pureParams, inIndex, outIndex);
            outIndex = getDualOutIndex(pureParams);
        }

        return PureCheckNum.getMax(pureParams);
    }

    /**
     * 获取对偶换出基变量下标
     *
     * @return 检验数数组
     */
    public static int getDualOutIndex(PureParams pureParams) {
        int index = -1;
        double min = 0;
        for (int i = 0; i < pureParams.inIndexArray.length;i++) {
            double conditionResult = pureParams.conditionResult[i];
            if (conditionResult < min) {
                index = i;
                min = conditionResult;
            }
        }

        return index;
    }

    /**
     * 获取对偶换入基变量下标
     *
     * @return 入基变量的下标
     */
    public static int getDualInIndex(PureParams pureParams, double[] checkNum, int outIndex) {
        int inIndex = -1;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < checkNum.length; i++) {
            if (checkNum[i] >= 0) {
                continue;
            }

            double condition = pureParams.conditionArray[outIndex][i];
            double scale = checkNum[i] / condition;
            if (scale > 0 && scale < min) {
                min = scale;
                inIndex = i;
            }
        }

        return inIndex;
    }

    public static void main(String[] args) {
//        double[][] conditionArray = new double[][]{{1, 2}, {4, 0}, {0, 4}};
//        double[] conditionResult = new double[]{8, 16, 12};
//        double[] resultArray = new double[]{2, 3};

        double[][] conditionArray = new double[][]{{-2, -4, -5, -1}, {-3, 1, -7, 2}, {-5, -2, -1, -6}};
        double[] conditionResult = new double[]{-4, -2, -15};
        double[] resultArray = new double[]{-3, -2, -1, -4};

        PureParams pureParams = new PureParams();
        pureParams.resultArray = resultArray;
        pureParams.conditionArray = conditionArray;
        pureParams.conditionResult = conditionResult;
        pureParams.best = 0;

        PureCheckNum.convertStandard(pureParams);
    }
}