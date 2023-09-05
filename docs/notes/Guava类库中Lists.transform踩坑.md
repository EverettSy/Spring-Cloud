# <div style="text-align: center;">Guava 类库中 Lists.transform 踩坑</div>

## 前言
>最近在 Java 踩坑的道路上越踩越多坑，随手写篇笔记做个记录。

## 问题分析
Guava 类库很强大，但也有一点坑，最近在做项目的正好遇到。使用Lists.transform转换数据遇到的问题，
1、修改源对象数据会直接影响转换后数据 
2、直接修改转换后数据修改无效

**代码例子：**

```java
//源对象
@Data
@AllArgsConstructor
public class People {

    private String name;

    private int age;

    private String desc;

}
// 转换目标对象
@Data
public class PeopleVo {
    private String name;

    private int age;

    private String desc;

    public PeopleVo() {
    }


}

public class GuavaExample {

    public static PeopleVo peopleToVo(People people) {
        PeopleVo peopleVo = new PeopleVo();
        peopleVo.setName(people.getName());
        peopleVo.setAge(people.getAge());
        peopleVo.setDesc(people.getDesc() + " To Vo");
        return peopleVo;
    }

    public static void main(String[] args) {
        List<People> peopleList = ImmutableList.of(
                new People("icharle", 18, "good"),
                new People("pad", 19, "better"),
                new People("soarteam", 20, "best")
        );

        List<PeopleVo> peopleVos = Lists.transform(peopleList, GuavaExample::peopleToVo);
        //1.对源对象数据进行修改会影响到转换对象的数据
        System.out.println("-----------------源对象数据-----------------");
        System.out.println(peopleVos.toString());

        System.out.println("----------------对源对象修改-----------------");
        for(People people : peopleList){
            people.setAge(people.getAge()+1);
        }
        System.out.println(peopleVos.toString());


        //2.对转换后的对象进行修改数据，修改无效
        System.out.println("-----------------修改转换对象数据-----------------");
        for(PeopleVo peopleVo : peopleVos){
            peopleVo.setAge(peopleVo.getAge()+1);
        }
        System.out.println(peopleVos.toString());
    }

}


```
**结果输出：**

```shell

-----------------源对象数据-----------------
[PeopleVo(name=icharle, age=18, desc=good To Vo), PeopleVo(name=pad, age=19, desc=better To Vo), PeopleVo(name=soarteam, age=20, desc=best To Vo)]
----------------对源对象修改-----------------
[PeopleVo(name=icharle, age=19, desc=good To Vo), PeopleVo(name=pad, age=20, desc=better To Vo), PeopleVo(name=soarteam, age=21, desc=best To Vo)]
-----------------修改转换对象数据-----------------
[PeopleVo(name=icharle, age=19, desc=good To Vo), PeopleVo(name=pad, age=20, desc=better To Vo), PeopleVo(name=soarteam, age=21, desc=best To Vo)]

```

**情况说明**
* 输出判断一跟片段二对比，当对源数据peopleList的 age 加一操作情况下，输出转换对象后的数据是更变的。
* 输出片段二跟片段三对比，当对转换后数据peopleVos的 age 加一的操作情况下，输出转换对象的数据是不改变。

* 查阅谷歌相关文章，发现转换后的数据是一个视图模型，因此再修改原对象情况下，必然会影响视图内容；而直接修改视图，对它的任何更改都是无效的。

* 先看看Lists.transform是怎样实现的。

```java
    public static <F, T> List<T> transform(List<F> fromList, Function<? super F, ? extends T> function) {
        return (List)(fromList instanceof RandomAccess ? new TransformingRandomAccessList(fromList, function) : new TransformingSequentialList(fromList, function));
    }

        //Lists.transform返回的是一个新创建的TransformingSequentialList实例
        public ListIterator<T> listIterator(int index) {
            return new TransformedListIterator<F, T>(this.fromList.listIterator(index)) {
                T transform(F from) {
                    return TransformingRandomAccessList.this.function.apply(from);
                }
            };
        }
        //TransformingSequentialList每次遍历都会从原来的list中遍历来从新计算得到function


```


!>转换后的数据不支持 add、addAll、set 方法