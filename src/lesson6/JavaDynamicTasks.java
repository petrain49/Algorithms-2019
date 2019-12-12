package lesson6;

import kotlin.NotImplementedError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     *
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
     * При сравнении подстрок, регистр символов *имеет* значение.
     */
    /**
     * O(first.length() * second.length())
     */
    public static String longestCommonSubSequence(String first, String second) {
        String[] A = first.split("");
        String[] B = second.split("");

        List<String> res = common(A, B);

        StringBuilder sb = new StringBuilder();
        for (String s: res) sb.append(s);
        return sb.reverse().toString();
    }

    private static <T> List<T> common(T[] A, T[] B) {
        int n = A.length;
        int m = B.length;
        int[][] matrix = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (A[i - 1].equals(B[j - 1])) matrix[i][j] = matrix[i - 1][j - 1] + 1;
                else matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i][j - 1]);
            }
        }
        //System.out.println(Arrays.deepToString(matrix));

        List<T> res = new ArrayList<>();
        while (n > 0 && m > 0) {
            if (A[n - 1].equals(B[m - 1])) {
                res.add(A[n - 1]);
                n--;
                m--;
            }
            else if (matrix[n - 1][m] == matrix[n][m]) n--;
            else m--;
        }
        return res;
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Сложная
     *
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     */
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        if (list.size() == 0) return Collections.emptyList();

        List<Integer> slist = soort(list);
        List<Integer> res = (List<Integer>) (List) common(list.toArray(), slist.toArray());
        Collections.reverse(res);
        return res;
    }

    private static List<Integer> soort(List<Integer> list) {
        Integer[] arr = list.toArray(new Integer[list.size()]);

        for(int i = arr.length-1 ; i > 0 ; i--) {
            int tmp;
            for(int j = 0 ; j < i ; j++){
                if( arr[j] > arr[j+1] ){
                    tmp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = tmp;
                }
            }
        }
        return Arrays.asList(arr);
    }

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Средняя
     *
     * В файле с именем inputName задано прямоугольное поле:
     *
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     *
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     *
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */
    public static int shortestPathOnField(String inputName) {
        throw new NotImplementedError();
    }

    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}
