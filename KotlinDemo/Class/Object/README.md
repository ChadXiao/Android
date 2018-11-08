# Object ���η�

------

 - ��������
 - ��������
 - ȫ�ֱ���

## �������� ##

    window.addMouseListener(object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) { ���� }
    
        override fun mouseEntered(e: MouseEvent) { ���� }
    })
    
    

�����й������������ؼ̳�

    open class A(x: Int) {
        public open val y: Int = x
    }
    
    interface B { ���� }
    
    val ab: A = object : A(1), B {
        override val y = 15
    }

û�г������������ֻ���������ھֲ�������˽�б��������ʹ���ڹ��к����ķ������ͻ��߹������Ե����ͣ���ô�ú�����ʵ�����ͻ����������������ĳ����ͣ����û�г��࣬Ĭ�ϻ���Any����ô�޷���Χ��������ĳ�Ա��

    class C {
        // ˽�к����������䷵��������������������
        private fun foo() = object {
            val x: String = "x"
        }
    
        // ���к����������䷵�������� Any
        fun publicFoo() = object {
            val x: String = "x"
        }
    
        fun bar() {
            val x1 = foo().x        // û����
            val x2 = publicFoo().x  // ����δ�ܽ��������á�x��
        }
    }
    
����������Է������԰�������������ı�����javaֻ��ʹ��final������ kotlin���ã�

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
        // ����
    }


## �������� ##

kotlinʵ�ֵ���ģʽ����ͨ��Object���η�

    object DataProviderManager {
        fun registerDataProvider(provider: DataProvider) {
            // ����
        }
    
        val allDataProviders: Collection<DataProvider>
            get() = // ����
    }
    
    DataProviderManager.registerDataProvider(����)
    
## �����Companion Object�� ##

��������ʹ��Companion�ؼ���������

    class MyClass {
        companion object Factory {
            //Factory����������ʡ��
            fun create(): MyClass = MyClass()
        }
    }
    
    //ʹ��
    MyClass.Factory.create����    //Factory����ʡ��

�������Լ̳�

    interface Factory<T> {
        fun create(): T
    }
    
    class MyClass {
        companion object : Factory<MyClass> {
            override fun create(): MyClass = MyClass()
        }
    }


## Object���ص� ##

 - ��������ʹ��ʱ���ϱ�ִ�кͳ�ʼ��
 - ���������ڵ�һ��ʹ��ʱ����ʼ��
 - �������Ӧ���౻����ʱ��ʼ������java��̬��ʼ��������һ��
 
 
