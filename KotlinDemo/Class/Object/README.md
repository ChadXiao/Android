# Object 修饰符

------

 - 匿名对象
 - 单例对象
 - 全局变量

## 匿名对象 ##

    window.addMouseListener(object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) { …… }
    
        override fun mouseEntered(e: MouseEvent) { …… }
    })
    
    

超类有构造参数，或多重继承

    open class A(x: Int) {
        public open val y: Int = x
    }
    
    interface B { …… }
    
    val ab: A = object : A(1), B {
        override val y = 15
    }

没有超类的匿名对象，只可以作用在局部变量或私有变量。如果使用在公有函数的返回类型或者公有属性的类型，那么该函数或实际类型会是匿名对象声明的超类型，如果没有超类，默认会是Any，那么无法范围匿名对象的成员：

    class C {
        // 私有函数，所以其返回类型是匿名对象类型
        private fun foo() = object {
            val x: String = "x"
        }
    
        // 公有函数，所以其返回类型是 Any
        fun publicFoo() = object {
            val x: String = "x"
        }
    
        fun bar() {
            val x1 = foo().x        // 没问题
            val x2 = publicFoo().x  // 错误：未能解析的引用“x”
        }
    }
    
匿名对象可以访问来自包含它的作用域的遍历（java只能使用final变量， kotlin不用）

    fun countClicks(window: JComponent) {
        var clickCount = 0
        var enterCount = 0
    
        window.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                clickCount++
            }
    
            override fun mouseEntered(e: MouseEvent) {
                enterCount++
            }
        })
        // ……
    }


## 单例对象 ##

kotlin实现单例模式可以通过Object修饰符

    object DataProviderManager {
        fun registerDataProvider(provider: DataProvider) {
            // ……
        }
    
        val allDataProviders: Collection<DataProvider>
            get() = // ……
    }
    
    DataProviderManager.registerDataProvider(……)
    
## 类对象（Companion Object） ##

类对象可以使用Companion关键字声明：

    class MyClass {
        companion object Factory {
            //Factory对象名可以省略
            fun create(): MyClass = MyClass()
        }
    }
    
    //使用
    MyClass.Factory.create（）    //Factory可以省略

类对象可以继承

    interface Factory<T> {
        fun create(): T
    }
    
    class MyClass {
        companion object : Factory<MyClass> {
            override fun create(): MyClass = MyClass()
        }
    }


## Object的特点 ##

 - 匿名类在使用时马上被执行和初始化
 - 单例对象在第一次使用时被初始化
 - 类对象当相应的类被加载时初始化，与java静态初始化器语义一样
 
 
