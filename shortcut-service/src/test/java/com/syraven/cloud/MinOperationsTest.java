package com.syraven.cloud;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-09-08 20:40
 */
public class MinOperationsTest {

    public int minOperations(String str){
        char[] chars = str.toCharArray();
        int n = chars.length;
        int[] nums = new int[26];
        for (int i = 0; i < n; i++) {
            nums[chars[i] - 'a']++;
        }
        int others = 0;
        for (int i = 0; i <26 ; i++) {
            if (nums[i] % 2 != 0){
                others++;
            }
        }
        int target = 26 - others;
        if (n - others <= target * 2){
            return (n -others) /2;
        }
        n = n- others - target * 2;
        return target + n;
    }


    public static void main(String[] args) {
        MinOperationsTest minOperationsTest = new MinOperationsTest();
        System.out.println(minOperationsTest.minOperations("abab"));
    }
}
