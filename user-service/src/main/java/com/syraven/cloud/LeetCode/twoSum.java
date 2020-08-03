package com.syraven.cloud.LeetCode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/6/29 16:35
 */
public class twoSum {

    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }


    //方法一：暴力法
   // 暴力法很简单，遍历每个元素 xx，并查找是否存在一个值与 target - xtarget−x 相等的目标元素。
    public static int[] twoSum2(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[j] == target - nums[i]) {
                    return new int[]{i, j};
                }
            }
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    public static int[] twoSum3(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length ; i++) {
            map.put(nums[i],i);
        }

        for (int i = 0; i <nums.length ; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement) && map.get(complement) != i) {
                return new int[]{i,map.get(complement)};
            }
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    public static void main(String[] args) {
        int[] nums = {2, 5,7, 11, 15};
        int target = 9;
        //int[] result = twoSum(nums, target);
        int[] result = twoSum3(nums, target);
        System.out.println(Arrays.toString(result));
    }
}