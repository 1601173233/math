package planningmanagement;

/**
 * 指定矩阵的最大值
 *
 * A * X = B
 * C * X = ?
 *
 * @author jack.huang
 */
public class MatrixMax {

    /**
     * 计算矩阵结果
     */
    public static void getMatrixResult(double[][] matrix, double[] conditionResult) {
        int startIndex = 0;
        for (int i = 0; i < matrix.length; i++) {
            // 寻找出现前缀的字段
            int j = i;
            boolean isFinish = false;
            do {
                for (; j < matrix.length; j++) {
                    if (matrix[j][startIndex] != 0) {
                        break;
                    }
                }

                if (j == matrix.length) {
                    startIndex++;
                    j = i;

                    if (startIndex >= matrix[i].length) {
                        isFinish = true;
                        break;
                    }
                } else {
                    break;
                }
            } while (true);

            if (isFinish) {
                break;
            }

            double n = matrix[j][startIndex];
            for (int k = startIndex; k < matrix[j].length; k++) {
                matrix[j][k] = matrix[j][k] / n;
            }
            conditionResult[j] /= n;

            if (j != i) {
                for (int k = startIndex; k < matrix[j].length; k++) {
                    matrix[i][k] += matrix[j][k];
                }

                conditionResult[i] += conditionResult[j];
            }

            for (int l = 0; l < matrix.length; l++) {
                if (i != l && matrix[l][startIndex] != 0) {
                    n = matrix[l][startIndex];
                    for (int k = 0; k < matrix[l].length; k++) {
                        matrix[l][k] -= n * matrix[i][k];
                    }

                    conditionResult[l] -= conditionResult[i] * n;
                }
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            boolean isAllZero = true;

            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != 0) {
                    isAllZero = false;
                    break;
                }
            }

            if (isAllZero && conditionResult[i] != 0) {
                throw new RuntimeException("该矩阵无解！");
            }
        }
    }

    /**
     * 获取结果矩阵
     */
    public static void getResult(double[][] matrix, double[] conditionResult, double[] resultArray) {
        int startIndex = 0;
        double[] resultMatrix = new double[resultArray.length + 1];

        for (int i = 0; i < matrix.length; i++) {
            for (; startIndex < matrix[i].length; startIndex++) {
                if (matrix[i][startIndex] == 1) {
                    break;
                }
            }

            if (startIndex == matrix[i].length) {
                break;
            }

            for (int j = startIndex + 1; j < matrix[i].length; j++) {
                resultMatrix[j] -= resultArray[startIndex] * matrix[i][j];
            }

            resultMatrix[matrix.length] += conditionResult[i] * resultArray[startIndex];
            startIndex++;
        }

        MathUtil.printResult(conditionResult);
        System.out.println();
        MathUtil.printResult(matrix);
        System.out.println();
        MathUtil.printResult(resultMatrix);
    }

    public static void main(String[] args) {
        
        double[][] conditionArray = new double[][]{{1, 1, 0, 0, 0, 0, 0},
                                                   {-1, 0, 1, 0, 0, -1, 0},
                                                   {0, -1, -1, 1, 1, 0, 0},
                                                   {0, 0, 0, -1, 0, 1, 1},
                                                   {0, 0, 0, 0, -1, 0, -1} };
        double[] conditionResult = new double[]{1, -1, 0, 0, 0};
        double[] resultArray = new double[]{100, 30, 20, 10, 60, 15, 50};


//        double[][] conditionArray = new double[][]{{3,  5, -4},
//                                                   {-3, -2, 4},
//                                                   {6,  1, -8}};
//        double[] conditionResult = new double[]{7, -1, -4};
//        double[] resultArray = new double[]{-100, -30, -20, -10, -60, -15, -50};

        getMatrixResult(conditionArray, conditionResult);
        getResult(conditionArray, conditionResult, resultArray);
    }
}
