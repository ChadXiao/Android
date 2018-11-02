# ����

------

 - ����


----------


     

> Functions in Kotlin are declared using the fun keyword:



    fun double(x: Int): Int {
                return 2 * x
            }

 - ����


----------


 

> Calling functions uses the traditional approach:

    val result = double(2)

> Calling member functions uses the dot notation:

    //��Ա����
    Sample().foo()

 - ����


----------


 

> Function parameters are defined using Pascal notation, i.e. name: type. Parameters are separated using commas. Each parameter must be explicitly typed:

    fun powerOf(number: Int, exponent: Int) { ... }
    

 - Ĭ�ϲ���


----------


 

> Function parameters can have default values, which are used when a corresponding argument is omitted. This allows for a reduced number of overloads compared to other languages:

    fun read(b: Array<Byte>, off: Int = 0, len: Int = b.size) { ... }
    
    fun foo(bar: Int = 0, baz: Int) { ... }
    foo(baz = 1) // The default value bar = 0 is used
    
    

> ������дʱ���Ҫ���ɸ�������Ĭ�ϲ����������ں������������к���Ĭ�ϲ���

    open class A {
        open fun foo(i: Int = 10) { ... }
    }
    
    class B : A() {
        override fun foo(i: Int) { ... } 
    }
    

 - Named Arguments


----------


 

> Function parameters can be named when calling functions. This is very convenient when a function has a high number of parameters or default ones.

    
    fun reformat(str: String,
             normalizeCase: Boolean = true,
             upperCaseFirstLetter: Boolean = true,
             divideByCamelHumps: Boolean = false,
             wordSeparator: Char = ' ') {...}
    reformat(str)
    reformat(str, true, true, false, '_')
    reformat(str,
        normalizeCase = true,
        upperCaseFirstLetter = true,
        divideByCamelHumps = false,
        wordSeparator = '_'
    )
    reformat(str, wordSeparator = '_')
    
    

 - �ɱ䳤����


----------


 

> A parameter of a function (normally the last one) may be marked with vararg modifier:

    fun <T> asList(vararg ts: T): List<T> {
        val result = ArrayList<T>()
        for (t in ts) // ts is an Array
            result.add(t)
        return result
    }
    
    var list = asList(1, 2, 3)
    

> When we call a vararg-function, we can pass arguments one-by-one, e.g. asList(1, 2, 3), or, if we already have an array and want to pass its contents to the function, we use the spread operator (prefix the array with *):

    val a = arrayOf(1, 2, 3)
    val list = asList(-1, 0, *a, 4)
    

 - ��������


----------
    fun foo(bar: Int = 0, baz: Int = 1, qux: () -> Unit) { ... }

    foo(1) { println("hello") } // Uses the default value baz = 1 
    foo { println("hello") } 

 - ���ز���
 

> ���п���ĺ�������ʼ����ȷָ���������ͣ������������ڷ���Unit����������������ǿ�ѡ�ġ� Kotlinû���ƶϾ��п���ĺ����ķ������ͣ���Ϊ��Щ�������������ھ��и��ӵĿ����������ҷ������Ͷ��ڶ�����˵�ǲ����Եģ���ʱ�������ڱ�����Ҳ����ˣ���


----------


    //�շ��ز���:
    fun printHello(name: String?): Unit {
        if (name != null)
            println("Hello ${name}")
        else
            println("Hi there!")
        // `return Unit` or `return` is optional
    }

    fun printHello(name: String?) { ... }
    

 - Single-Expression functions
 


----------

    fun double(x: Int): Int = x * 2
    
    

> Explicitly declaring the return type is optional when this can be inferred by the compiler:

    fun double(x: Int) = x * 2
    

 - Infix notation


----------
    infix fun Int.shl(x: Int): Int { ... }

    // calling the function using the infix notation
    1 shl 2
    
    // is the same as
    1.shl(2)

    

> Infix functions must satisfy the following requirements:
> 
> They must be member functions or extension functions; (��Ա��������չ����)
> They must have a single parameter; ������ֻ��һ��������
> The parameter must not accept variable number of arguments and must have no default value.������������Ĭ��ֵ��



> ���ȼ�С�����������������boolean������


----------


> Note that infix functions always require both the receiver and the parameter to be specified. When you're calling a method on the current receiver using the infix notation, you need to use this explicitly; unlike regular method calls, it cannot be omitted. This is required to ensure unambiguous parsing.

    class MyStringCollection {
        infix fun add(s: String) { ... }
        
        fun build() {
            this add "abc"   // Correct
            add("abc")       // Correct
            add "abc"        // Incorrect: the receiver must be specified
        }
    }

 Local Functions
Kotlin supports local functions, i.e. a function inside another function:


fun dfs(graph: Graph) {
    fun dfs(current: Vertex, visited: Set<Vertex>) {
        if (!visited.add(current)) return
        for (v in current.neighbors)
            dfs(v, visited)
    }
?
    dfs(graph.vertices[0], HashSet())
}
Local function can access local variables of outer functions (i.e. the closure), so in the case above, the visited can be a local variable:


fun dfs(graph: Graph) {
    val visited = HashSet<Vertex>()
    fun dfs(current: Vertex) {
        if (!visited.add(current)) return
        for (v in current.neighbors)
            dfs(v)
    }
?
    dfs(graph.vertices[0])
}
Member Functions
A member function is a function that is defined inside a class or object:


class Sample() {
    fun foo() { print("Foo") }
}
Member functions are called with dot notation:


Sample().foo() // creates instance of class Sample and calls foo
For more information on classes and overriding members see Classes and Inheritance.

Generic Functions
Functions can have generic parameters which are specified using angle brackets before the function name:


fun <T> singletonList(item: T): List<T> { ... }
For more information on generic functions see Generics.

Inline Functions
Inline functions are explained here.

Extension Functions
Extension functions are explained in their own section.

Higher-Order Functions and Lambdas
Higher-Order functions and Lambdas are explained in their own section.

Tail recursive functions
Kotlin supports a style of functional programming known as tail recursion. This allows some algorithms that would normally be written using loops to instead be written using a recursive function, but without the risk of stack overflow. When a function is marked with the tailrec modifier and meets the required form, the compiler optimises out the recursion, leaving behind a fast and efficient loop based version instead:


val eps = 1E-10 // "good enough", could be 10^-15
?
tailrec fun findFixPoint(x: Double = 1.0): Double
        = if (Math.abs(x - Math.cos(x)) < eps) x else findFixPoint(Math.cos(x))
This code calculates the fixpoint of cosine, which is a mathematical constant. It simply calls Math.cos repeatedly starting at 1.0 until the result doesn't change any more, yielding a result of 0.7390851332151611 for the specified eps precision. The resulting code is equivalent to this more traditional style:


val eps = 1E-10 // "good enough", could be 10^-15
?
private fun findFixPoint(): Double {
    var x = 1.0
    while (true) {
        val y = Math.cos(x)
        if (Math.abs(x - y) < eps) return x
        x = Math.cos(x)
    }
}
To be eligible for the tailrec modifier, a function must call itself as the last operation it performs. You cannot use tail recursion when there is more code after the recursive call, and you cannot use it within try/catch/finally blocks. Currently tail recursion is only supported in the JVM backend.
    
