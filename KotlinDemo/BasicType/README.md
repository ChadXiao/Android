# 基本类型

------

## Numbers ##

| Type | Bit width |
|--------|-------|
| Double | 64 |
| Float | 32 |
| Long | 64 | 
| int | 32 |
| Short | 16 | 
| Byte | 8 |

Decimals: 123
Longs are tagged by a capital L: 123L
Hexadecimals: 0x0F
Binaries: 0b00001011

Doubles by default: 123.5, 123.5e10
Floats are tagged by f or F: 123.5f

数字可以加下滑线:
---------

    val oneMillion = 1_000_000
    val creditCardNumber = 1234_5678_9012_3456L
    val socialSecurityNumber = 999_99_9999L
    val hexBytes = 0xFF_EC_DE_5E
    val bytes = 0b11010010_01101001_10010100_10010010
    
#### Representation ####

> On the Java platform, numbers are physically stored as JVM primitive types, unless we need a nullable number reference (e.g. Int?) or generics are involved. In the latter cases numbers are boxed.
> 
> Note that boxing of numbers does not necessarily preserve identity:

    val a: Int = 10000
    println(a === a) // Prints 'true'
    val boxedA: Int? = a
    val anotherBoxedA: Int? = a
    println(boxedA === anotherBoxedA) // !!!Prints 'false'!!!
    

> On the other hand, it preserves equality:

    val a: Int = 10000
    println(a == a) // Prints 'true'
    val boxedA: Int? = a
    val anotherBoxedA: Int? = a
    println(boxedA == anotherBoxedA) // Prints 'true'
    
#### Explicit Conversions 

    // Hypothetical code, does not actually compile:
    val a: Int? = 1 // A boxed Int (java.lang.Integer)
    val b: Long? = a // implicit conversion yields a boxed Long (java.lang.Long)
    print(b == a) // Surprise! This prints "false" as Long's equals() checks whether the other is Long as well
    

 - Every number type supports the following conversions:

   

>  toByte(): Byte
>  toShort(): Short
>  toInt(): Int
>  toLong(): Long
>  toFloat(): Float
>  toDouble(): Double
>  toChar(): Char

#### Operations ####

>shl(bits) – signed shift left (Java's <<)
shr(bits) – signed shift right (Java's >>)
ushr(bits) – unsigned shift right (Java's >>>)
and(bits) – bitwise and
or(bits) – bitwise or
xor(bits) – bitwise xor
inv() – bitwise inversion

 - Floating Point Numbers Comparison

   

>  Equality checks: a == b and a != b
>     Comparison operators: a < b, a > b, a <= b, a >= b
>     Range instantiation and range checks: a..b, x in a..b, x !in a..b

 
## Characters ##


## Boolean ##

> || – lazy disjunction 
&& – lazy conjunction 
! - negation

## Arrays（数组） ##

> Arrays in Kotlin are represented by the Array class, that has get and set functions (that turn into [] by operator overloading conventions), and size property, along with a few other useful member functions:

    class Array<T> private constructor() {
        val size: Int
        operator fun get(index: Int): T
        operator fun set(index: Int, value: T): Unit
    
        operator fun iterator(): Iterator<T>
        // ...
    }
    
    

 - 空数组
 

> arrayOfNulls()

 - lambda
 

> val asc = Array(5){
    i -> (i * i).toString() 
}

 - 遍历

 

>       asc.forEach { println(it) }


> Kotlin also has specialized classes to represent arrays of primitive types without boxing overhead: ByteArray, ShortArray, IntArray and so on. These classes have no inheritance relation to the Array class, but they have the same set of methods and properties. Each of them also has a corresponding factory function:

    val x: IntArray = intArrayOf(1, 2, 3)
    x[0] = x[1] + x[2]

## Unsigned integers ##

> kotlin.UByte: an unsigned 8-bit integer, ranges from 0 to 255
kotlin.UShort: an unsigned 16-bit integer, ranges from 0 to 65535
kotlin.UInt: an unsigned 32-bit integer, ranges from 0 to 2^32 - 1
kotlin.ULong: an unsigned 64-bit integer, ranges from 0 to 2^64 - 1

## Specialized classes ##

> kotlin.UByteArray: an array of unsigned bytes
kotlin.UShortArray: an array of unsigned shorts
kotlin.UIntArray: an array of unsigned ints
kotlin.ULongArray: an array of unsigned longs

    val b: UByte = 1u  // UByte, expected type provided
    val s: UShort = 1u // UShort, expected type provided
    val l: ULong = 1u  // ULong, expected type provided
    
    val a1 = 42u // UInt: no expected type provided, constant fits in UInt
    val a2 = 0xFFFF_FFFF_FFFFu // ULong: no expected type provided, constant doesn't fit in UInt
    val a = 1UL 
    
    
## Strings ##

遍历

    for (c in str) {
        println(c)
    }
    
拼接

    val s = "abc" + 1
    println(s + "def")
    
String Literals

    val text = """
        for (c in "foo")
            print(c)
    """

> You can remove leading whitespace with trimMargin() function:By
> default | is used as margin prefix, but you can choose another
> character and pass it as a parameter, like trimMargin(">").

    
    val text = """
    |Tell me and I forget.
    |Teach me and I remember.
    |Involve me and I learn.
    |(Benjamin Franklin)
    """.trimMargin()
    
## String Templates ##

    val i = 10
    println("i = $i") // prints "i = 10"
    
    val s = "abc"
    println("$s.length is ${s.length}") // prints "abc.length is 3"

    val price = """
    ${'$'}9.99
    """