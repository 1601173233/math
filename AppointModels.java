package planningmanagement;

/**
 * 指派模型
 */
public class AppointModels {

    /**
     * 计算线性规划
     *
     * @param conditionResult 条件结果矩阵
     * @param xResultArray 横结果参数
     * @param yResultArray 列结果参数
     * @return 结果集
     */
    public static Double getMax(double[][] conditionResult, double[] xResultArray, double[] yResultArray) {

        // 1.获取初始解
        boolean[][] baseArray = new boolean[xResultArray.length][yResultArray.length];
        double[][] baseResultArray = getFirstResult(xResultArray, yResultArray, baseArray);

        System.out.println();
        printResult(baseResultArray);
        System.out.println();

        while (true) {
            // 2.计算乘子
            double[] xMultiplicator = new double[xResultArray.length];
            double[] yMultiplicator = new double[yResultArray.length];
            getMultiplicator(conditionResult, baseArray, xMultiplicator, yMultiplicator);

            // 3.计算非基变量计算 ui + vj - cij
            double[][] notBaseVarArray = calNotBaseVar(conditionResult, baseArray, xMultiplicator, yMultiplicator);
            int maxX = 0;
            int maxY = 0;
            double maxNotBaseVar = 0;
            for (int xIndex = 0; xIndex < notBaseVarArray.length; xIndex++) {
                for (int yIndex = 0; yIndex < notBaseVarArray[0].length; yIndex++) {
                    if (maxNotBaseVar < notBaseVarArray[xIndex][yIndex]) {
                        maxNotBaseVar = notBaseVarArray[xIndex][yIndex];
                        maxX = xIndex;
                        maxY = yIndex;
                    }
                }
            }

            if (maxNotBaseVar == 0) {
                break;
            }

            baseArray[maxX][maxY] = true;
            int[][] closedLoop = searchClosedLoop(baseArray, maxX, maxY);

            int minX = 0;
            int minY = 0;
            double min = Double.MAX_VALUE;
            for (int x = 0; x < closedLoop.length; x++) {
                for (int y = 0; y < closedLoop[x].length; y++) {
                    if (closedLoop[x][y] == 1 && min > baseResultArray[x][y]) {
                        minX = x;
                        minY = y;
                        min = baseResultArray[x][y];
                    }
                }
            }

            for (int x = 0; x < closedLoop.length; x++) {
                for (int y = 0; y < closedLoop[x].length; y++) {
                    if (closedLoop[x][y] == 1) {
                        baseResultArray[x][y] -= min;
                    } else if(closedLoop[x][y] == 2) {
                        baseResultArray[x][y] += min;
                    }
                }
            }

            baseArray[minX][minY] = false;
            baseResultArray[maxX][maxY] = min;

            System.out.println();
            printResult(baseResultArray);
            System.out.println();
        }

        double result = 0;
        for (int xIndex = 0; xIndex < baseResultArray.length; xIndex++) {
            for (int yIndex = 0; yIndex < baseResultArray[0].length; yIndex++) {
                result += conditionResult[xIndex][yIndex] * baseResultArray[xIndex][yIndex];
            }
        }
        return result;
    }

    /**
     * 获取初始解
     *
     * 左上角法
     *
     * @param baseArray 进基变量矩阵
     * @return 初始解矩阵
     */
    public static double[][] getFirstResult(double[] xResultArray,
                                            double[] yResultArray,
                                            boolean[][] baseArray) {
        double[][] result = new double[xResultArray.length][yResultArray.length];

        double[] xArray = copy(xResultArray), yArray = copy(yResultArray);

        int xIndex = 0, yIndex = 0;
        while (xIndex < xArray.length && yIndex < yArray.length) {
            result[xIndex][yIndex] = Math.min(xArray[xIndex], yArray[yIndex]);
            baseArray[xIndex][yIndex] = true;

            if (xArray[xIndex] == result[xIndex][yIndex]) {
                yArray[yIndex] -= result[xIndex][yIndex];
                xIndex++;
            } else {
                xArray[xIndex] -= result[xIndex][yIndex];
                yIndex++;
            }
        }

        return result;
    }

    /**
     * 计算乘子
     *
     * @param conditionResult 条件结果矩阵
     * @param baseArray 进基变量矩阵
     * @param xMultiplicator 横乘子结果参数
     * @param yMultiplicator 列乘子结果参数
     */
    private static void getMultiplicator(double[][] conditionResult,
                                         boolean[][] baseArray,
                                         double[] xMultiplicator,
                                         double[] yMultiplicator) {
        int[] xHasResultArray = new int[xMultiplicator.length], yHasResultArray = new int[yMultiplicator.length];

        xMultiplicator[0] = 0;
        xHasResultArray[0] = 1;

        boolean isX = true;
        int count = 0;
        int maxCount = xMultiplicator.length * yMultiplicator.length;
        while (!getMultiplicatorFinish(xHasResultArray, yHasResultArray) && count++ < maxCount) {
            if (isX) {
                for (int xIndex = 0; xIndex < xHasResultArray.length; xIndex++) {
                    if (xHasResultArray[xIndex] == 1) {
                        for (int yIndex = 0; yIndex < yHasResultArray.length; yIndex++) {
                            if (yHasResultArray[yIndex] == 0 && baseArray[xIndex][yIndex]) {
                                yMultiplicator[yIndex] = conditionResult[xIndex][yIndex] - xMultiplicator[xIndex];
                                yHasResultArray[yIndex] = 1;
                            }
                        }

                        xHasResultArray[xIndex] = 2;
                    }
                }
            } else {
                for (int yIndex = 0; yIndex < yHasResultArray.length; yIndex++) {
                    if (yHasResultArray[yIndex] == 1) {
                        for (int xIndex = 0; xIndex < xHasResultArray.length; xIndex++) {
                            if (xHasResultArray[xIndex] == 0 && baseArray[xIndex][yIndex]) {
                                xMultiplicator[xIndex] = conditionResult[xIndex][yIndex] - yMultiplicator[yIndex];
                                xHasResultArray[xIndex] = 1;
                            }
                        }

                        yHasResultArray[yIndex] = 2;
                    }
                }
            }

            isX = !isX;
        }
    }

    /**
     * 校验是否已经完成了
     *
     * @param xHasResultArray x结果
     * @param yHasResultArray y结果
     * @return 已完成
     */
    private static boolean getMultiplicatorFinish(int[] xHasResultArray, int[] yHasResultArray) {
        for (int result : xHasResultArray) {
            if (result == 0) {
                return false;
            }
        }

        for (int result : yHasResultArray) {
            if (result == 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * 计算非基变量计算 ui + vj - cij
     *
     * @param conditionResult 条件结果矩阵
     * @param baseArray 基变量
     * @param xMultiplicator 横乘子结果参数
     * @param yMultiplicator 列乘子结果参数
     * @return 已完成
     */
    private static double[][] calNotBaseVar(double[][] conditionResult,
                                            boolean[][] baseArray,
                                            double[] xMultiplicator,
                                            double[] yMultiplicator) {
        double[][] result = new double[xMultiplicator.length][yMultiplicator.length];

        for (int xIndex = 0; xIndex < xMultiplicator.length; xIndex++) {
            for (int yIndex = 0; yIndex < yMultiplicator.length; yIndex++) {
                if (!baseArray[xIndex][yIndex]) {
                    result[xIndex][yIndex] = xMultiplicator[xIndex] + yMultiplicator[yIndex] - conditionResult[xIndex][yIndex];
                }
            }
        }

        return result;
    }

    /**
     * 寻找闭环
     *
     * @param baseArray 进基数组
     * @param xIndex 初始下标x
     * @param yIndex 初始下标y
     * @return
     */
    private static int[][] searchClosedLoop(boolean[][] baseArray, int xIndex, int yIndex) {
        int[][] result = new int[baseArray.length][baseArray[0].length];

        result[xIndex][yIndex] = -1; // 开始节点

        searchClosedLoop(baseArray, result, -1, xIndex, yIndex, true);
        return result;
    }

    /**
     * 寻找闭环
     *
     * @param baseArray 进基数组
     * @param xIndex 初始下标x
     * @param yIndex 初始下标y
     * @return
     */
    private static boolean searchClosedLoop(boolean[][] baseArray,
                                            int[][] result,
                                            int orientation,
                                            int xIndex,
                                            int yIndex,
                                            boolean type) {
        for (int i = 0; i < 4; i++) {
            if (i == orientation || i == orientation - 2 || i == orientation + 2) {
                continue;
            }

            switch (i) {
                case 0:
                for (int x = xIndex + 1; x < baseArray.length; x++) {
                    if (searchCloseLoopCheck(baseArray, result, i, x, yIndex, type)) {
                        return true;
                    }
                }
                break;
                case 1:
                    for (int y = yIndex + 1; y < baseArray[0].length; y++) {
                        if (searchCloseLoopCheck(baseArray, result, i, xIndex, y, type)) {
                            return true;
                        }
                    }
                    break;
                case 2:
                    for (int x = xIndex - 1; x >= 0; x--) {
                        if (searchCloseLoopCheck(baseArray, result, i, x, yIndex, type)) {
                            return true;
                        }
                    }
                    break;
                case 3:
                    for (int y = yIndex - 1; y >= 0; y--) {
                        if (searchCloseLoopCheck(baseArray, result, i, xIndex, y, type)) {
                            return true;
                        }
                    }
                    break;
                default:
            }
        }

        return false;
    }

    /**
     * 闭环校验
     *
     * @param baseArray 数组
     * @param result 结果集
     * @param i 方向
     * @param x x坐标
     * @param y y坐标
     * @return 闭环校验
     */
    private static Boolean searchCloseLoopCheck(boolean[][] baseArray,
                                                int[][] result,
                                                int i,
                                                int x,
                                                int y,
                                                boolean type) {
        if (baseArray[x][y]) {
            if (result[x][y] == -1) {
                return true;
            } else if (result[x][y] >= 1) {
                return false;
            }

            result[x][y] = type ? 1 : 2;
            if (searchClosedLoop(baseArray, result, i, x, y, !type)) {
                return true;
            } else {
                result[x][y] = 0;
            }
        }

        return false;
    }

    /**
     * 数组复制
     *
     * @param array 原数组
     * @return 目标数组
     */
    private static double[] copy(double[] array) {
        double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }

        return array;
    }

    /**
     * 打印结果
     *
     * @param result 结果
     */
    private static void printResult(double[] result) {
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + " ");
        }

        System.out.println();
    }

    /**
     * 打印结果
     *
     * @param result 结果
     */
    private static void printResult(double[][] result) {
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                System.out.print(result[i][j] + " ");
            }

            System.out.println();
        }
    }

    public static void main(String[] args) {
//        double[][] conditionResult = {{10, 2, 20, 11}, {12, 7, 9, 20}, {4, 14, 16, 18}};
//        double[] xResultArray = {15, 25, 10};
//        double[] yResultArray = {5, 15, 15, 15d};

//        double[][] conditionResult = {{15, 10, 9}, {9, 15, 10}, {10, 12, 8}};
//        double[] xResultArray = {1, 1, 1};
//        double[] yResultArray = {1, 1, 1};

        double[][] conditionResult = {{15, 10, 9}, {9, 15, 10}, {10, 12, 8}};
        double[] xResultArray = {1, 1, 1};
        double[] yResultArray = {1, 1, 1};

        System.out.println(getMax(conditionResult, xResultArray, yResultArray));
    }
}
