# 集合
----------

 - 不可变集合

> List/Set/Map<out T>类型是一个提供只读操作如size、get等的接口，继承Collection<T>进而继承Iterable<T>

  - 可变集合
 
>　MutableList/MutableSet/MutableMap<out T>类型继承List<T>和MutableCollection<T>,提供可读可写操作集合

## 初始化方法 ##

listOf()、mutableListOf(),set/map同样

    val numbers: MutableList<Int> = mutableListOf(1, 2, 3)
    val readOnlyView: List<Int> = numbers
    println(numbers) // 输出 "[1, 2, 3]"
    numbers.add(4)
    println(readOnlyView) // 输出 "[1, 2, 3, 4]"
    readOnlyView.clear() // -> 不能编译
    val strings = hashSetOf("a", "b", "c", "c")
    assert(strings.size == 3)
    

> **Note：** List《son ： parent》可以赋值给List《parent》，但是Mutablelist《son ： parent》不能赋值给MutableList《parent》

## toList ##

> 有时你想给调用者返回一个集合在某个特定时间的一个快照, 一个保证不会变的：

    class Controller {
        private val _items = mutableListOf<String>()
        val items: List<String> get() = _items.toList()
    }
    
## 扩展方法 ##

 - **first**

> 获取第一个元素

      items.first() == 1

 - **last**
 

> 获取最后一个元素

    items.last() == 4
    

 - **filter**
 

> 过滤

    items.filter { it % 2 == 0 } // 返回 [2, 4]
    

 - **requireNoNulls**
 

> 返回所有非空元素，如果集合为空，则会抛出***IllegalArgumentException***异常

    rwList.requireNoNulls() // 返回 [1, 2, 3]
    

 - **none**
 

> 如果没有元素满足条件，则返回true

    if (rwList.none { it > 6 }) println("No items above 6") // 输出“No items above 6”
    
- **firstOrNull**

> 返回第一个元素，如果集合为空则返回null

    val item = rwList.firstOrNull()
    
以及其它实用工具如sort、zip、fold、reduce等等

Map 遵循同样模式。它们可以容易地实例化与访问，像这样：

    val readWriteMap = hashMapOf("foo" to 1, "bar" to 2)
    println(readWriteMap["foo"]) // 输出“1”
    val snapshot: Map<String, Int> = HashMap(readWriteMap)