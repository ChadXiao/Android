# Class

------

���캯��
----

 - **�����캯��**

> �� Kotlin �е�һ���������һ��**�����캯**���Լ�һ������**�ι��캯��**�������캯������ͷ��һ���֣����������������ѡ�����Ͳ�������

    class Person constructor(firstName: String) { ... }//constructor�ؼ��ֿ���ʡ��
    
**�����캯��**��������������η�

    class Person(val firstName: String, val lastName: String, var age: Int) { ���� }
    
**�����캯��**Ĭ����public���εģ������Ϊ���������η�����Ҫ����constructor�ؼ���

    class Customer public @Inject constructor(name: String) { ���� }

 

 - **�ι��캯��**

�����������constructorǰ׺��**�ι��캯��**
 
 

    class Person {
        constructor(parent: Person) {
            parent.children.add(this)
        }
    }
    
�ι��캯������ͨ��this�ؼ��ֵ��������캯��

    class Person(val name: String) {
        constructor(name: String, parent: Person) : this(name) {
            parent.children.add(this)
        }
    }
    
�����û�������κΣ�����Σ����캯������������һ�����������������캯������ϣ�����й��еĹ��캯������Ҫ����һ�����з�Ĭ�Ͽɼ��Ե������캯��

    class DontCreateMe private constructor () { ... }
    
��������캯�����в�������Ĭ��ֵ����ô������һ��������޲������캯��������ʹ��Ĭ��ֵ����ʹ��kotlin������ʹ��Jackson��JPA�����Ŀ�

    class Customer(val customerName: String = "")
    
## ��ʼ���� ##

 

> ��ʼ��������ԷŴ�init�ؼ�����Ϊǰ׺�ĳ�ʼ�����У���ʼ�����������캯��֮�����ڴι��캯��֮ǰִ�У���ʼ�����ִ��˳�������������г��ֵ�˳��һ�£����������Գ�ʼ����֯��һ��

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
    
    

> �����캯���Ĳ��������ڳ�ʼ��������Գ�ʼ������ʹ��

    class Customer(name: String) {
        val customerKey = name.toUpperCase()
    }

## �̳� ##
 
 kotlin���е��඼��һ����ͬ�ĳ���Any����ͬ��java.lang.Object������equals������hasCode������toString��û���κγ�Ա
 
    open class Base(p: Int)

    class Derived(p: Int) : Base(p)
    
���캯��������super�ؼ��ֵ��ø���Ĺ��캯��
 
 
 

    class MyView : View {
        constructor(ctx: Context) : super(ctx)
    
        constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)
    }

## ���� ##

 - ���Ƿ���
 
kotlin������ʾ��ע�ɸ��ǳ�Ա�Լ����Ǻ��Ա

    open class Base {
        open fun v() { ... }//�ؼ���open�Ǳ����
        fun nv() { ... }//û��open�ؼ����޷�������
    }
    class Derived() : Base() {
        override fun v() { ... }//�ؼ���override�Ǳ����
    }
    

 

 - ��������

���Ը����뷽���������ƣ��ڳ���������Ȼ�������������������������Ա����� override ��ͷ���������Ǳ�����м��ݵ����͡�ÿ�����������Կ����ɾ��г�ʼ���������Ի��߾��� getter ���������Ը��ǡ�

    open class Foo {
        open val x: Int get() { ���� }
    }
    
    class Bar1 : Foo() {
        override val x: Int = ����
    }
    
�������Կ����޸�val��var����

    interface Foo {
        val count: Int
    }
    
    class Bar1(override val count: Int) : Foo
    
    class Bar2 : Foo {
        override var count: Int = 0
    }
 

> **Note��**��¼�ĳ�ʼ������������ĳ�ʼ���߼�֮ǰ���ã���˲����ڻ���ĳ�ʼ���߼��е���open����

���ó������ͨ��super�ؼ��ֵ��ã������ⲿ��ĳ������ͨ��super@Outer����

> **Note��**���һ���༯�ɳ�Ա�ж����ͬ��ʵ�֣������븲������������ṩ�Լ���ʵ��

    open class A {
        open fun f() { print("A") }
        fun a() { print("a") }
    }
    
    interface B {
        fun f() { print("B") } // �ӿڳ�ԱĬ�Ͼ��ǡ�open����
        fun b() { print("b") }
    }
    
    class C() : A(), B {
        // ������Ҫ�󸲸� f()��
        override fun f() {
            super<A>.f() // ���� A.f()
            super<B>.f() // ���� B.f()
      }
    }
    
## ������ ##
������ʹ��abstract���Σ�����Ҫʹ��open�ؼ���
