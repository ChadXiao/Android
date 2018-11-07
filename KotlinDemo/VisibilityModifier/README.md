# Visibility Modifiers

------

 - private
 - public(Default)
 - protected
 - internal

## Package ##

> Functions, properties and classes, objects and interfaces can be declared on the "top-level", i.e. directly inside a package:

 - If you do not specify any visibility modifier, **public** is used by default, which means that your declarations will be visible everywhere;
 - If you mark a declaration **private**, it will only be visible inside the file containing the declaration;
 - If you mark it **internal**, it is visible everywhere in the same module;
 - **protected** is not available for top-level declarations.

        // file name: example.kt
        package foo
        
        private fun foo() { ... } // visible inside example.kt
        
        public var bar: Int = 5 // property is visible everywhere
            private set         // setter is visible only in example.kt
            
        internal val baz = 6    // visible inside the same module

 ## Classes and Interfaces ##
 
 - **private** means visible inside this class only (including all its members);
 - **protected** ¡ª same as private + visible in subclasses too;
 - **internal** ¡ª any client inside this module who sees the declaring class sees its internal members;
 - **public** ¡ª any client who sees the declaring class sees its public members.
  

        open class Outer {
            private val a = 1
            protected open val b = 2
            internal val c = 3
            val d = 4  // public by default
            
            protected class Nested {
                public val e: Int = 5
            }
        }
        
        class Subclass : Outer() {
            // a is not visible
            // b, c and d are visible
            // Nested and e are visible
        
            override val b = 5   // 'b' is protected
        }
        
        class Unrelated(o: Outer) {
            // o.a, o.b are not visible
            // o.c and o.d are visible (same module)
            // Outer.Nested is not visible, and Nested::e is not visible either 
        }
        
## Constructors ##

> To specify a visibility of the primary constructor of a class, use the following syntax (note that you need to add an explicit constructor keyword):

    class C private constructor(a: Int) { ... }
    
## Local declarations ##

> Local variables, functions and classes can not have visibility modifiers.


## Modules ##
 - an IntelliJ IDEA module;
 - a Maven project;
 - a Gradle source set (with the exception that the test source set can access the internal declarations of main);
 - a set of files compiled with one invocation of the <kotlinc> Ant task.
 - a set of files compiled with one invocation of the <kotlinc> Ant task.