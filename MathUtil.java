package planningmanagement;

/**
 * 工具类
 *
 * @author jack.huang
 */
public class MathUtil {

    /**
     * 数组复制
     *
     * @param array 原数组
     * @return 目标数组
     */
    public static double[] copy(double[] array) {
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
    public static void printResult(int[] result) {
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
    public static void printResult(double[] result) {
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
    public static void printResult(double[][] result) {
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                System.out.print(result[i][j] + " ");
            }

            System.out.println();
        }
    }
}
