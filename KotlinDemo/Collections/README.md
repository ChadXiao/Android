# ����
----------

 - ���ɱ伯��

> List/Set/Map<out T>������һ���ṩֻ��������size��get�ȵĽӿڣ��̳�Collection<T>�����̳�Iterable<T>

  - �ɱ伯��
 
>��MutableList/MutableSet/MutableMap<out T>���ͼ̳�List<T>��MutableCollection<T>,�ṩ�ɶ���д��������

## ��ʼ������ ##

listOf()��mutableListOf(),set/mapͬ��

    val numbers: MutableList<Int> = mutableListOf(1, 2, 3)
    val readOnlyView: List<Int> = numbers
    println(numbers) // ��� "[1, 2, 3]"
    numbers.add(4)
    println(readOnlyView) // ��� "[1, 2, 3, 4]"
    readOnlyView.clear() // -> ���ܱ���
    val strings = hashSetOf("a", "b", "c", "c")
    assert(strings.size == 3)
    

> **Note��** List��son �� parent�����Ը�ֵ��List��parent��������Mutablelist��son �� parent�����ܸ�ֵ��MutableList��parent��

## toList ##

> ��ʱ����������߷���һ��������ĳ���ض�ʱ���һ������, һ����֤�����ģ�

    class Controller {
        private val _items = mutableListOf<String>()
        val items: List<String> get() = _items.toList()
    }
    
## ��չ���� ##

 - **first**

> ��ȡ��һ��Ԫ��

      items.first() == 1

 - **last**
 

> ��ȡ���һ��Ԫ��

    items.last() == 4
    

 - **filter**
 

> ����

    items.filter { it % 2 == 0 } // ���� [2, 4]
    

 - **requireNoNulls**
 

> �������зǿ�Ԫ�أ��������Ϊ�գ�����׳�***IllegalArgumentException***�쳣

    rwList.requireNoNulls() // ���� [1, 2, 3]
    

 - **none**
 

> ���û��Ԫ�������������򷵻�true

    if (rwList.none { it > 6 }) println("No items above 6") // �����No items above 6��
    
- **firstOrNull**

> ���ص�һ��Ԫ�أ��������Ϊ���򷵻�null

    val item = rwList.firstOrNull()
    
�Լ�����ʵ�ù�����sort��zip��fold��reduce�ȵ�

Map ��ѭͬ��ģʽ�����ǿ������׵�ʵ��������ʣ���������

    val readWriteMap = hashMapOf("foo" to 1, "bar" to 2)
    println(readWriteMap["foo"]) // �����1��
    val snapshot: Map<String, Int> = HashMap(readWriteMap)