# Null Safety

------

## Nullable types and Non-Null Types ##

> n Kotlin, the type system distinguishes between references that can hold null (nullable references) and those that can not (non-null references). For example, a regular variable of type String can not hold null:

    `var a: String = "abc"
    a = null // compilation error
    

> To allow nulls, we can declare a variable as nullable string, written String?:

    var b: String? = "abc"
    b = null // ok
    print(b)

## Checking for null in conditions ##

> you can explicitly check if b is null, and handle the two options separately:

    val l = if (b != null) b.length else -1
    
    
## Safe Calls ##

> Your second option is the safe call operator, written ?.

    val a = "Kotlin"
    val b: String? = null
    println(b?.length)
    println(a?.length)
    
    bob?.department?.head?.name
    
    val listWithNulls: List<String?> = listOf("Kotlin", null)
    for (item in listWithNulls) {
        item?.let { println(it) } // prints A and ignores null
    }
    
    person?.department?.head = managersPool.getManager()
    
## Elvis Operator ##

>¡¡if-expression can be expressed with the Elvis operator, written ?:

    val l = b?.length ?: -1
    
    fun foo(node: Node): String? {
        val parent = node.getParent() ?: return null
        val name = node.getName() ?: throw IllegalArgumentException("name expected")
        // ...
    }
    
The !! Operator

> The third option is for NPE-lovers: the not-null assertion operator (!!) converts any value to a non-null type and throws an exception if the value is null. We can write b!!, and this will return a non-null value of b (e.g., a String in our example) or throw an NPE if b is null:

    val l = b!!.length
    
## Safe Casts ##

> Regular casts may result into a ClassCastException if the object is not of the target type. Another option is to use safe casts that return null if the attempt was not successful:

    val aInt: Int? = a as? Int
    
## Collections of Nullable Type ##

    

> If you have a collection of elements of a nullable type and want to filter non-null elements, you can do so by using filterNotNull:

    val nullableList: List<Int?> = listOf(1, 2, null, 4)
    val intList: List<Int> = nullableList.filterNotNull()