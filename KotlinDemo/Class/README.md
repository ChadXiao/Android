# Class

------

构造函数
----

 - **主构造函数**

> 在 Kotlin 中的一个类可以有一个**主构造函**数以及一个或多个**次构造函数**。主构造函数是类头的一部分：它跟在类名（与可选的类型参数）后。

    class Person constructor(firstName: String) { ... }//constructor关键字可以省略
    
**主构造函数**参数可以添加修饰符

    class Person(val firstName: String, val lastName: String, var age: Int) { …… }
    
**主构造函数**默认是public修饰的，如果改为其他的修饰符，需要加上constructor关键字

    class Customer public @Inject constructor(name: String) { …… }

 

 - **次构造函数**

类可以声明有constructor前缀的**次构造函数**
 
 

    class Person {
        constructor(parent: Person) {
            parent.children.add(this)
        }
    }
    
次构造函数可以通过this关键字调用主构造函数

    class Person(val name: String) {
        constructor(name: String, parent: Person) : this(name) {
            parent.children.add(this)
        }
    }
    
如果类没有声明任何（主或次）构造函数，它会生成一个不带参数的主构造函数，不希望类有公有的构造函数，需要声明一个带有非默认可见性的主构造函数

    class DontCreateMe private constructor () { ... }
    
如果主构造函数所有参数都有默认值，那么会生成一个额外的无参数构造函数，它将使用默认值。这使得kotlin更易于使用Jackson或JPA这样的库

    class Customer(val customerName: String = "")
    
## 初始化块 ##

 

> 初始化代码可以放大init关键字作为前缀的初始化块中，初始化块在主构造函数之后并且在次构造函数之前执行，初始化块的执行顺序与他们在类中出现的顺序一致，并且与属性初始化交织在一起

    class InitOrderDemo(name: String) {
        val firstProperty = "First property: $name".also(::println)
        
        init {
            println("First initializer block that prints ${name}")
        }
        
        val secondProperty = "Second property: ${name.length}".also(::println)
        
        init {
            println("Second initializer block that prints ${name.length}")
        }
    }
    
    

> 主构造函数的参数可以在初始化块和属性初始化器中使用

    class Customer(name: String) {
        val customerKey = name.toUpperCase()
    }

## 继承 ##
 
 kotlin所有的类都有一个共同的超类Any，不同于java.lang.Object，除了equals（）、hasCode（）和toString外没有任何成员
 
    open class Base(p: Int)

    class Derived(p: Int) : Base(p)
    
构造函数可以用super关键字调用父类的构造函数
 
 
 

    class MyView : View {
        constructor(ctx: Context) : super(ctx)
    
        constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)
    }

## 覆盖 ##

 - 覆盖方法
 
kotlin必须显示标注可覆盖成员以及覆盖后成员

    open class Base {
        open fun v() { ... }//关键字open是必须的
        fun nv() { ... }//没有open关键字无法被覆盖
    }
    class Derived() : Base() {
        override fun v() { ... }//关键字override是必须的
    }
    

 

 - 覆盖属性

属性覆盖与方法覆盖类似；在超类中声明然后在派生类中重新声明的属性必须以 override 开头，并且它们必须具有兼容的类型。每个声明的属性可以由具有初始化器的属性或者具有 getter 方法的属性覆盖。

    open class Foo {
        open val x: Int get() { …… }
    }
    
    class Bar1 : Foo() {
        override val x: Int = ……
    }
    
覆盖属性可以修改val或var属性

    interface Foo {
        val count: Int
    }
    
    class Bar1(override val count: Int) : Foo
    
    class Bar2 : Foo {
        override var count: Int = 0
    }
 

> **Note：**记录的初始化会在派生类的初始化逻辑之前调用，因此不能在基类的初始化逻辑中调用open属性

调用超类可以通过super关键字调用，调用外部类的超类可以通过super@Outer调用

> **Note：**如果一个类集成成员有多个相同的实现，它必须覆盖这个域名并提供自己的实现

    open class A {
        open fun f() { print("A") }
        fun a() { print("a") }
    }
    
    interface B {
        fun f() { print("B") } // 接口成员默认就是“open”的
        fun b() { print("b") }
    }
    
    class C() : A(), B {
        // 编译器要求覆盖 f()：
        override fun f() {
            super<A>.f() // 调用 A.f()
            super<B>.f() // 调用 B.f()
      }
    }
    
## 抽象类 ##
抽象类使用abstract修饰，不需要使用open关键字
