# Nested and Inner Class

------

## Nested Class ##

> class can be nested in other class

    class Outer {
        private val bar: Int = 1
        class Nested {
            fun foo() = 2
        }
    }
    
    val demo = Outer.Nested().foo() // == 2
    
## Inner class ##

> A class may be marked as inner to be able to access members of outer class. Inner classes carry a reference to an object of an outer class:

    class Outer {
        private val bar: Int = 1
        inner class Inner {
            fun foo() = bar
        }
    }
    
    val demo = Outer().Inner().foo() // == 1
    
    
## Anonymous inner classes ##

> Anonymous inner class instances are created using an object expression

    window.addMouseListener(object: MouseAdapter() {

        override fun mouseClicked(e: MouseEvent) { ... }
                                                                                                                
        override fun mouseEntered(e: MouseEvent) { ... }
    })
    

> ���object�Ǻ���ʽjava�ӿڣ������е������󷽷���Java�ӿڣ���ʵ���������ʹ�ô��ӿ�����ǰ׺��lambda���ʽ������

    val listener = ActionListener { println("clicked") }