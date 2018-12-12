# Chapter 2: Design Overview


----------

> This chapter focuses on major design issues in the JNI. Most design issues in this section are related to native methods. The design of the Invocation API is covered in Chapter 5: The Invocation API.

本章重点介绍JNI中的主要设计问题。 本节中的大多数设计问题都与本机方法有关。 [第5章：Invocation API][1]中介绍了Invocation API的设计。

This chapter covers the following topics:
- <a href="#1">JNI Interface Functions and Pointers</a>
- <a href="#2">Compiling, Loading and Linking Native Methods</a>
  -  <a href="#21">Resolving Native Method Names</a>
  -  <a href="#22">Native Method Arguments</a>
- <a href="#3">Referencing Java Objects</a>
  - <a href="#31">Global and Local References</a>
  - <a href="#32">Implementing Local References</a>
- <a href="#4">Accessing Java Objects</a>
  - <a href="#41">Accessing Primitive Arrays</a>
  - <a href="#42">Accessing Fields and Methods</a>
- <a href="#5">Reporting Programming Errors</a>
- <a href="#6">Java Exceptions</a>
  - <a href="#61">Exceptions and Error Codes</a>
  - <a href="#62">Asynchronous Exceptions</a>
  - <a href="#63">Exception Handling</a>


## <a name="1">JNI Interface Functions and Pointers</a>

> Native code accesses Java VM features by calling JNI functions. JNI functions are available through an interface pointer. An interface pointer is a pointer to a pointer. This pointer points to an array of pointers, each of which points to an interface function. Every interface function is at a predefined offset inside the array. The following figure, <a href="a">Interface Pointer</a>, illustrates the organization of an interface pointer.

Native代码通过调用JNI函数来访问Java VM功能。 JNI函数可通过接口指针获得。 接口指针是指向指针的指针。 该指针指向一个指针数组，每个指针指向一个接口函数。 每个接口函数都在数组内的预定义偏移处。 下图<a href="a">接口指针</a>说明了接口指针的组织。

![enter description here][2]

[Description of Figure Interface Pointer][3]

> The JNI interface is organized like a C++ virtual function table or a COM interface. The advantage to using an interface table, rather than hard-wired function entries, is that the JNI name space becomes separate from the native code. A VM can easily provide multiple versions of JNI function tables. For example, the VM may support two JNI function tables:
> - one performs thorough illegal argument checks, and is suitable for debugging;
> - the other performs the minimal amount of checking required by the JNI specification, and is therefore more efficient.

JNI接口的组织方式类似于C ++虚函数表或COM接口。 使用接口表而不是硬连接函数条目的优点是JNI名称空间与本机代码分离。 VM可以轻松提供多个版本的JNI功能表。 例如，VM可能支持两个JNI函数表：
 - 执行彻底的非法参数检查，适合调试;
 - 执行JNI规范所需的最小量检查，同时效率更高。

> The JNI interface pointer is only valid in the current thread. A native method, therefore, must not pass the interface pointer from one thread to another. A VM implementing the JNI may allocate and store thread-local data in the area pointed to by the JNI interface pointer.

JNI接口指针仅在当前线程中有效。 因此，本机方法不能将接口指针从一个线程传递到另一个线程。 实现JNI的VM可以在JNI接口指针指向的区域中分配和存储线程本地数据。

> Native methods receive the JNI interface pointer as an argument. The VM is guaranteed to pass the same interface pointer to a native method when it makes multiple calls to the native method from the same Java thread. However, a native method can be called from different Java threads, and therefore may receive different JNI interface pointers.


Native方法接收JNI接口指针作为参数。 当VM从同一Java线程多次调用本机方法时，保证将VM传递给本机方法。 但是，可以从不同的Java线程调用本机方法，因此可以接收不同的JNI接口指针。

##  <a name="2">Compiling, Loading and Linking Native Methods</a>

> Since the Java VM is multithreaded, native libraries should also be compiled and linked with multithread aware native compilers. For example, the -mt flag should be used for C++ code compiled with the Sun Studio compiler. For code complied with the GNU gcc compiler, the flags -D_REENTRANT or -D_POSIX_C_SOURCE should be used. For more information please refer to the native compiler documentation.

由于Java VM是多线程的，因此本机库也应该与多线程感知的本机编译器一起编译和链接。 例如，-mt标志应该用于使用Sun Studio编译器编译的C ++代码。 对于符合GNU gcc编译器的代码，应使用标志-D_REENTRANT或-D_POSIX_C_SOURCE。 有关更多信息，请参阅本机编译器文档。

> Native methods are loaded with the System.loadLibrary method. In the following example, the class initialization method loads a platform-specific native library in which the native method f is defined:

本机方法使用System.loadLibrary方法加载。 在以下示例中，类初始化方法加载特定于平台的本机库，其中定义了本机方法f：

	package pkg; 

	class Cls {
		native double f(int i, String s);
		static {
			System.loadLibrary(“pkg_Cls”);
		}
	}
	
> The argument to System.loadLibrary is a library name chosen arbitrarily by the programmer. The system follows a standard, but platform-specific, approach to convert the library name to a native library name. For example, a Solaris system converts the name pkg_Cls to libpkg_Cls.so, while a Win32 system converts the same pkg_Cls name to pkg_Cls.dll.

System.loadLibrary的参数是程序员任意选择的库名。 系统遵循标准但特定于平台的方法将库名称转换为本机库名称。 例如，Solaris系统将名称pkg_Cls转换为libpkg_Cls.so，而Win32系统将相同的pkg_Cls名称转换为pkg_Cls.dll。

> The programmer may use a single library to store all the native methods needed by any number of classes, as long as these classes are to be loaded with the same class loader. The VM internally maintains a list of loaded native libraries for each class loader. Vendors should choose native library names that minimize the chance of name clashes.

程序员可以使用单个库来存储任意数量的类所需的所有本机方法，只要这些类要使用相同的类加载器加载即可。 VM在内部维护每个类加载器的已加载本机库列表。 供应商应选择本地库名称，以尽量减少名称冲突的可能性。

> 
A native library may be statically linked with the VM. The manner in which the library and VM image are combined is implementation dependent. A System.loadLibrary or equivalent API must succeed for this library to be considered loaded.

本机库可以与VM静态链接。 组合库和VM映像的方式取决于实现。 System.loadLibrary或等效的API必须成功才能将此库视为已加载。

> A library L whose image has been combined with the VM is defined as statically linked if and only if the library exports a function called JNI_OnLoad_L.

当且仅当库导出名为JNI_OnLoad的函数时，其图像已与VM组合的库L被定义为静态链接。

> If a statically linked library L exports a function called JNI_OnLoad_L and a function called JNI_OnLoad, the JNI_OnLoad function will be ignored.

如果静态链接库L导出名为JNI_OnLoad L的函数和名为JNI_OnLoad的函数，则将忽略JNI_OnLoad函数。

> If a library L is statically linked, then upon the first invocation of System.loadLibrary("L") or equivalent API, a JNI_OnLoad_L function will be invoked with the same arguments and expected return value as specified for the JNI_OnLoad function.

如果库L是静态链接的，那么在第一次调用System.loadLibrary（“L”）或等效的API时，将使用为JNI_OnLoad函数指定的相同参数和预期返回值调用JNI_OnLoad_L函数。

> A library L that is statically linked will prohibit a library of the same name from being loaded dynamically.

静态链接的库L将禁止动态加载同名库。

> When the class loader containing a statically linked native library L is garbage collected, the VM will invoke the JNI_OnUnload_L function of the library if such a function is exported.

当包含静态链接的本机库L的类加载器被垃圾收集时，如果导出这样的函数，VM将调用库的JNI_OnUnload_L函数。

> If a statically linked library L exports a function called JNI_OnUnLoad_L and a function called JNI_OnUnLoad, the JNI_OnUnLoad function will be ignored.

如果静态链接库L导出名为JNI_OnLoad Land的函数，该函数名为JNI_OnLoad，则将忽略JNI_OnLoad函数。

> The programmer can also call the JNI function RegisterNatives() to register the native methods associated with a class. The RegisterNatives() function is particularly useful with statically linked functions.

程序员还可以调用JNI函数RegisterNatives（）来注册与类关联的本机方法。 RegisterNatives（）函数对于静态链接函数特别有用。

### <a name="21">Resolving Native Method Names</a>

> Dynamic linkers resolve entries based on their names. A native method name is concatenated from the following components:
> - the prefix Java_
> - a mangled fully-qualified class name
> - an underscore (“_”) separator
> - a mangled method name
> - for overloaded native methods, two underscores ("__") followed by the mangled argument signature

动态链接器根据其名称解析条目。 本机方法名称由以下组件连接：
- 前缀Java_
- 一个错位的完全限定的类名
- 下划线（“_”）分隔符
- 一个错位的方法名称
- 对于重载的本机方法，两个下划线（“__”）后跟错位的参数签名

> The VM checks for a method name match for methods that reside in the native library. The VM looks first for the short name; that is, the name without the argument signature. It then looks for the long name, which is the name with the argument signature. Programmers need to use the long name only when a native method is overloaded with another native method. However, this is not a problem if the native method has the same name as a nonnative method. A nonnative method (a Java method) does not reside in the native library.

VM检查驻留在本机库中的方法的方法名称匹配。 VM首先查找短名称; 也就是说，没有参数签名的名称。 然后它查找长名称，这是带有参数签名的名称。 只有当本机方法使用另一个本机方法重载时，程序员才需要使用长名称。 但是，如果本机方法与非本地方法具有相同的名称，则这不是问题。 非本地方法（Java方法）不驻留在本机库中。

> In the following example, the native method g does not have to be linked using the long name because the other method g is not a native method, and thus is not in the native library.

在以下示例中，本机方法g不必使用长名称进行链接，因为其他方法g不是本机方法，因此不在本机库中。

	class Cls1 {
		int g(int i);
		native int g(double d);
	}
	
> We adopted a simple name-mangling scheme to ensure that all Unicode characters translate into valid C function names. We use the underscore (“_”) character as the substitute for the slash (“/”) in fully qualified class names. Since a name or type descriptor never begins with a number, we can use _0, ..., _9 for escape sequences, as the following table illustrates:

我们采用了一种简单的名称修改方案，以确保所有Unicode字符都转换为有效的C函数名称。 我们使用下划线（“_”）字符代替完全限定类名中的斜杠（“/”）。 由于名称或类型描述符从不以数字开头，因此我们可以使用_0，...，_9作为转义序列，如下表所示：

|   Escape Sequence  |   Denotes  |
| --- | --- |
|   _0XXXX  |  a Unicode character XXXX. Note that lower case is used to represent non-ASCII Unicode characters, for example, _0abcd as opposed to _0ABCD.   |
|  _1   | 	the character “_”    |
|  _2   |  	the character “;” in signatures   |
|  _3   |    	the character “[“ in signatures  |

> Both the native methods and the interface APIs follow the standard library-calling convention on a given platform. For example, UNIX systems use the C calling convention, while Win32 systems use __stdcall

本机方法和接口API都遵循给定平台上的标准库调用约定。 例如，UNIX系统使用C调用约定，而Win32系统使用__stdcall

### <a name="#22">Native Method Arguments</a>

> The JNI interface pointer is the first argument to native methods. The JNI interface pointer is of type JNIEnv. The second argument differs depending on whether the native method is static or nonstatic. The second argument to a nonstatic native method is a reference to the object. The second argument to a static native method is a reference to its Java class.

JNI接口指针是本机方法的第一个参数。 JNI接口指针的类型为JNIEnv。 第二个参数根据本机方法是静态方法还是非静态方法而有所不同。 非静态本机方法的第二个参数是对该对象的引用。 静态本机方法的第二个参数是对其Java类的引用。

> The remaining arguments correspond to regular Java method arguments. The native method call passes its result back to the calling routine via the return value. [Chapter 3: JNI Types and Data Structures][4] describes the mapping between Java and C types.

其余参数对应于常规Java方法参数。 本机方法调用通过返回值将其结果传递回调用例程。  [ 第3章：JNI类型和数据结构][4]描述了Java和C类型之间的映射。

> The following code example illustrates using a C function to implement the native method f. The native method f is declared as follows:

以下代码示例说明如何使用C函数实现本机方法f。 本机方法f声明如下：

	package pkg; 

	class Cls {
		native double f(int i, String s);
		// ...
	}
	
> The C function with the long mangled name Java_pkg_Cls_f_ILjava_lang_String_2 implements native method f:

具有长错位名称Java_pkg_Cls_f_ILjava_lang_String_2的C函数实现本机方法f：

	jdouble Java_pkg_Cls_f__ILjava_lang_String_2 (
		 JNIEnv *env,        /* interface pointer */
		 jobject obj,        /* "this" pointer */
		 jint i,             /* argument #1 */
		 jstring s)          /* argument #2 */
	{
		 /* Obtain a C-copy of the Java string */
		 const char *str = (*env)->GetStringUTFChars(env, s, 0);

		 /* process the string */
		 ...

		 /* Now we are done with str */
		 (*env)->ReleaseStringUTFChars(env, s, str);

		 return ...
	}
	
> Note that we always manipulate Java objects using the interface pointer env. Using C++, you can write a slightly cleaner version of the code, as shown in the following code example:

请注意，我们总是使用接口指针env操作Java对象。 使用C ++，您可以编写稍微更简洁的代码版本，如以下代码示例所示：

	extern "C" /* specify the C calling convention */ 

	jdouble Java_pkg_Cls_f__ILjava_lang_String_2 (

		 JNIEnv *env,        /* interface pointer */
		 jobject obj,        /* "this" pointer */
		 jint i,             /* argument #1 */
		 jstring s)          /* argument #2 */

	{
		 const char *str = env->GetStringUTFChars(s, 0);

		 // ...

		 env->ReleaseStringUTFChars(s, str);

		 // return ...
	}
	
> With C++, the extra level of indirection and the interface pointer argument disappear from the source code. However, the underlying mechanism is exactly the same as with C. In C++, JNI functions are defined as inline member functions that expand to their C counterparts.

使用C ++，额外的间接级别和接口指针参数从源代码中消失。 但是，底层机制与C完全相同。在C ++中，JNI函数被定义为内联成员函数，它们扩展为C对应函数。

## <a name="3">Referencing Java Objects</a>

> Primitive types, such as integers, characters, and so on, are copied between Java and native code. Arbitrary Java objects, on the other hand, are passed by reference. The VM must keep track of all objects that have been passed to the native code, so that these objects are not freed by the garbage collector. The native code, in turn, must have a way to inform the VM that it no longer needs the objects. In addition, the garbage collector must be able to move an object referred to by the native code.

原始类型（如整数，字符等）在Java和本机代码之间复制。 另一方面，任意Java对象都通过引用传递。 VM必须跟踪已传递给本机代码的所有对象，以便垃圾收集器不会释放这些对象。 反过来，本机代码必须有一种方法来通知VM它不再需要这些对象。 此外，垃圾收集器必须能够移动本机代码引用的对象。

### <a name="31">Global and Local References</a>

> The JNI divides object references used by the native code into two categories: local and global references. Local references are valid for the duration of a native method call, and are automatically freed after the native method returns. Global references remain valid until they are explicitly freed.

JNI将本机代码使用的对象引用分为两类：本地引用和全局引用。 本地引用在本机方法调用的持续时间内有效，并在本机方法返回后自动释放。 全局引用在显式释放之前仍然有效。

> Objects are passed to native methods as local references. All Java objects returned by JNI functions are local references. The JNI allows the programmer to create global references from local references. JNI functions that expect Java objects accept both global and local references. A native method may return a local or global reference to the VM as its result.

对象作为本地引用传递给本机方法。 JNI函数返回的所有Java对象都是本地引用。 JNI允许程序员从本地引用创建全局引用。 JNI函数，期望Java对象接受全局和本地引用。 本机方法可以返回对VM的本地或全局引用作为其结果。

> In most cases, the programmer should rely on the VM to free all local references after the native method returns. However, there are times when the programmer should explicitly free a local reference. Consider, for example, the following situations:
> - A native method accesses a large Java object, thereby creating a local reference to the Java object. The native method then performs additional computation before returning to the caller. The local reference to the large Java object will prevent the object from being garbage collected, even if the object is no longer used in the remainder of the computation.
> - A native method creates a large number of local references, although not all of them are used at the same time. Since the VM needs a certain amount of space to keep track of a local reference, creating too many local references may cause the system to run out of memory. For example, a native method loops through a large array of objects, retrieves the elements as local references, and operates on one element at each iteration. After each iteration, the programmer no longer needs the local reference to the array element.

在大多数情况下，程序员应该依赖VM在本机方法返回后释放所有本地引用。 但是，有时程序员应该明确地释放本地引用。 例如，考虑以下情况：

- Native方法访问大型Java对象，从而创建对Java对象的本地引用。 然后，本机方法在返回调用方之前执行其他计算。 对大型Java对象的本地引用将阻止对象被垃圾回收，即使该对象不再用于计算的其余部分。
- 本机方法会创建大量本地引用，但并非所有引用都同时使用。 由于VM需要一定的空间来跟踪本地引用，因此创建太多本地引用可能会导致系统内存不足。 例如，本机方法循环遍历大量对象，将元素作为本地引用检索，并在每次迭代时对一个元素进行操作。 在每次迭代之后，程序员不再需要对数组元素的本地引用。

> The JNI allows the programmer to manually delete local references at any point within a native method. To ensure that programmers can manually free local references, JNI functions are not allowed to create extra local references, except for references they return as the result.

JNI允许程序员在本机方法中的任何点手动删除本地引用。 为了确保程序员可以手动释放本地引用，不允许JNI函数创建额外的本地引用，除了它们作为结果返回的引用。

> Local references are only valid in the thread in which they are created. The native code must not pass local references from one thread to another.

本地引用仅在创建它们的线程中有效。 本机代码不能将本地引用从一个线程传递到另一个线程。

### <a name="32">Implementing Local References</a>

> To implement local references, the Java VM creates a registry for each transition of control from Java to a native method. A registry maps nonmovable local references to Java objects, and keeps the objects from being garbage collected. All Java objects passed to the native method (including those that are returned as the results of JNI function calls) are automatically added to the registry. The registry is deleted after the native method returns, allowing all of its entries to be garbage collected.

为了实现本地引用，Java VM为从Java到本机方法的每次控制转换创建了一个注册表。 注册表将不可移动的本地引用映射到Java对象，并防止对象被垃圾回收。 传递给本机方法的所有Java对象（包括那些作为JNI函数调用结果返回的对象）都会自动添加到注册表中。 在本机方法返回后删除注册表，允许其所有条目被垃圾回收。

> There are different ways to implement a registry, such as using a table, a linked list, or a hash table. Although reference counting may be used to avoid duplicated entries in the registry, a JNI implementation is not obliged to detect and collapse duplicate entries.

有不同的方法来实现注册表，例如使用表，链表或哈希表。 虽然引用计数可用于避免注册表中的重复条目，但JNI实现没有义务检测和折叠重复条目。

> Note that local references cannot be faithfully implemented by conservatively scanning the native stack. The native code may store local references into global or heap data structures.

请注意，通过保守扫描本机堆栈，无法忠实地实现本地引用。 本机代码可以将本地引用存储到全局或堆数据结构中。

 ## <a name="4">Accessing Java Objects</a>
 
 > The JNI provides a rich set of accessor functions on global and local references. This means that the same native method implementation works no matter how the VM represents Java objects internally. This is a crucial reason why the JNI can be supported by a wide variety of VM implementations.

JNI在全局和本地引用上提供了丰富的访问器函数。 这意味着无论VM如何在内部表示Java对象，相同的本机方法实现都会起作用。 这是JNI可以被各种VM实现支持的关键原因。

> The overhead of using accessor functions through opaque references is higher than that of direct access to C data structures. We believe that, in most cases, Java programmers use native methods to perform nontrivial tasks that overshadow the overhead of this interface.

通过不透明引用使用访问器函数的开销高于直接访问C数据结构的开销。 我们相信，在大多数情况下，Java程序员使用本机方法来执行非常重要的任务，这些任务会掩盖此接口的开销。

### <a name="41">Accessing Primitive Arrays</a>

> 
This overhead is not acceptable for large Java objects containing many primitive data types, such as integer arrays and strings. (Consider native methods that are used to perform vector and matrix calculations.) It would be grossly inefficient to iterate through a Java array and retrieve every element with a function call.

对于包含许多基本数据类型的大型Java对象（例如整数数组和字符串），此开销是不可接受的。 （考虑用于执行向量和矩阵计算的Native方法。）迭代Java数组并使用函数调用检索每个元素将是非常低效的。

> One solution introduces a notion of “pinning” so that the native method can ask the VM to pin down the contents of an array. The native method then receives a direct pointer to the elements. This approach, however, has two implications:
> - The garbage collector must support pinning.
> - The VM must lay out primitive arrays contiguously in memory. Although this is the most natural implementation for most primitive arrays, boolean arrays can be implemented as packed or unpacked. Therefore, native code that relies on the exact layout of boolean arrays will not be portable.

一种解决方案引入了“固定”的概念，以便本机方法可以要求VM确定数组的内容。 然后，Native方法接收指向元素的直接指针。 然而，这种方法有两个含义：
- 垃圾收集器必须支持固定。
- VM必须在内存中连续布局原始数组。 虽然这是大多数原始数组最自然的实现，但是布尔数组可以实现为打包或解包。 因此，依赖于布尔数组的确切布局的本机代码将不可移植。

> We adopt a compromise that overcomes both of the above problems.

我们采取妥协方案，克服上述两个问题。

> First, we provide a set of functions to copy primitive array elements between a segment of a Java array and a native memory buffer. Use these functions if a native method needs access to only a small number of elements in a large array.

首先，我们提供了一组函数来复制Java数组的一段和本机内存缓冲区之间的原始数组元素。 如果本机方法只需要访问大型数组中的少量元素，请使用这些函数。

> 
Second, programmers can use another set of functions to retrieve a pinned-down version of array elements. Keep in mind that these functions may require the Java VM to perform storage allocation and copying. Whether these functions in fact copy the array depends on the VM implementation, as follows:
> - If the garbage collector supports pinning, and the layout of the array is the same as expected by the native method, then no copying is needed.
> - Otherwise, the array is copied to a nonmovable memory block (for example, in the C heap) and the necessary format conversion is performed. A pointer to the copy is returned.

其次，程序员可以使用另一组函数来检索数组元素的固定版本。 请记住，这些功能可能需要Java VM执行存储分配和复制。 这些函数实际上是否复制数组取决于VM实现，如下所示：
- 如果垃圾收集器支持固定，并且数组的布局与本机方法的预期相同，则不需要复制。
- 否则，将数组复制到不可移动的内存块（例如，在C堆中）并执行必要的格式转换。 返回指向副本的指针。

> Lastly, the interface provides functions to inform the VM that the native code no longer needs to access the array elements. When you call these functions, the system either unpins the array, or it reconciles the original array with its non-movable copy and frees the copy.

最后，该接口提供了通知VM本机代码不再需要访问数组元素的功能。 当您调用这些函数时，系统会取消数组，或者将原始数组与其不可移动的副本进行协调并释放副本。

> Our approach provides flexibility. A garbage collector algorithm can make separate decisions about copying or pinning for each given array. For example, the garbage collector may copy small objects, but pin the larger objects.

我们的方法提供灵活性 垃圾收集器算法可以针对每个给定阵列单独决定复制或固定。 例如，垃圾收集器可以复制小对象，但可以固定较大的对象。

> A JNI implementation must ensure that native methods running in multiple threads can simultaneously access the same array. For example, the JNI may keep an internal counter for each pinned array so that one thread does not unpin an array that is also pinned by another thread. Note that the JNI does not need to lock primitive arrays for exclusive access by a native method. Simultaneously updating a Java array from different threads leads to nondeterministic results.

JNI实现必须确保在多个线程中运行的本机方法可以同时访问同一个数组。 例如，JNI可以为每个固定数组保留一个内部计数器，这样一个线程就不会取消固定另一个线程固定的数组。 请注意，JNI不需要锁定原始数组以供本机方法独占访问。 同时从不同的线程更新Java数组会导致不确定的结果。

### <a name="42">Accessing Fields and Methods</a>

> The JNI allows native code to access the fields and to call the methods of Java objects. The JNI identifies methods and fields by their symbolic names and type signatures. A two-step process factors out the cost of locating the field or method from its name and signature. For example, to call the method f in class cls, the native code first obtains a method ID, as follows:

JNI允许Native代码访问字段并调用Java对象的方法。 JNI通过符号名称和类型签名来标识方法和字段。 两步过程会从字段名称和签名中分析出定位字段或方法的成本。 例如，要在类cls中调用方法f，Native代码首先获取方法ID，如下所示：

	jmethodID mid = env->GetMethodID(cls, “f”, “(ILjava/lang/String;)D”);
	
> The native code can then use the method ID repeatedly without the cost of method lookup, as follows:

然后，Native代码可以重复使用方法ID，而无需查找方法，如下所示：

	jdouble result = env->CallDoubleMethod(obj, mid, 10, str);
	
> A field or method ID does not prevent the VM from unloading the class from which the ID has been derived. After the class is unloaded, the method or field ID becomes invalid. The native code, therefore, must make sure to:
> - keep a live reference to the underlying class, or
> - recompute the method or field ID

字段或方法ID不会阻止VM卸载已从中派生ID的类。 卸载类后，方法或字段ID将变为无效。 因此，本机代码必须确保：
- 保持对基础类的实时引用，或
- 重新计算方法或字段ID

> 
if it intends to use a method or field ID for an extended period of time.

如果它打算长时间使用方法或字段ID。

> The JNI does not impose any restrictions on how field and method IDs are implemented internally.

JNI不对内部如何实现字段和方法ID施加任何限制。

## <a name="5">Reporting Programming Errors</a>

> The JNI does not check for programming errors such as passing in NULL pointers or illegal argument types. Illegal argument types includes such things as using a normal Java object instead of a Java class object. The JNI does not check for these programming errors for the following reasons:
> - Forcing JNI functions to check for all possible error conditions degrades the performance of normal (correct) native methods.
> - In many cases, there is not enough runtime type information to perform such checking.

JNI不检查编程错误，例如传入NULL指针或非法参数类型。 非法参数类型包括使用普通Java对象而不是Java类对象。 由于以下原因，JNI不检查这些编程错误：
- 强制JNI函数检查所有可能的错误条件会降低正常（正确）本机方法的性能。
- 在许多情况下，没有足够的运行时类型信息来执行此类检查。

> Most C library functions do not guard against programming errors. The printf() function, for example, usually causes a runtime error, rather than returning an error code, when it receives an invalid address. Forcing C library functions to check for all possible error conditions would likely result in such checks to be duplicated--once in the user code, and then again in the library.

大多数C库函数都不能防止编程错误。 例如，printf（）函数在收到无效地址时通常会导致运行时错误，而不是返回错误代码。 强制C库函数检查所有可能的错误条件可能会导致重复此类检查 - 一次在用户代码中，然后再次在库中。

> The programmer must not pass illegal pointers or arguments of the wrong type to JNI functions. Doing so could result in arbitrary consequences, including a corrupted system state or VM crash.

程序员不得将非法指针或错误类型的参数传递给JNI函数。 这样做可能会导致任意后果，包括系统状态损坏或VM崩溃。

## <a name="6">Java Exceptions</a>

> The JNI allows native methods to raise arbitrary Java exceptions. The native code may also handle outstanding Java exceptions. The Java exceptions left unhandled are propagated back to the VM.

### <a name="61">Exceptions and Error Codes</a>

> Certain JNI functions use the Java exception mechanism to report error conditions. In most cases, JNI functions report error conditions by returning an error code and throwing a Java exception. The error code is usually a special return value (such as NULL) that is outside of the range of normal return values. Therefore, the programmer can:
> - quickly check the return value of the last JNI call to determine if an error has occurred, and
> - call a function, ExceptionOccurred(), to obtain the exception object that contains a more detailed description of the error condition.

某些JNI函数使用Java异常机制来报告错误情况。 在大多数情况下，JNI函数通过返回错误代码并抛出Java异常来报告错误情况。 错误代码通常是一个特殊的返回值（如NULL），它超出了正常返回值的范围。 因此，程序员可以：
- 快速检查上次JNI调用的返回值，以确定是否发生了错误
- 调用一个函数ExceptionOccurred（），以获取包含错误条件的更详细描述的异常对象。

> There are two cases where the programmer needs to check for exceptions without being able to first check an error code:
> - The JNI functions that invoke a Java method return the result of the Java method. The programmer must call ExceptionOccurred() to check for possible exceptions that occurred during the execution of the Java method.
> - Some of the JNI array access functions do not return an error code, but may throw an *ArrayIndexOutOfBoundsException* or *ArrayStoreException.*

在两种情况下，程序员需要检查异常而无法首先检查错误代码：
- 调用Java方法的JNI函数返回Java方法的结果。 程序员必须调用ExceptionOccurred（）来检查在执行Java方法期间可能发生的异常。
- 某些JNI数组访问函数不返回错误代码，但可能抛出* ArrayIndexOutOfBoundsException *或* ArrayStoreException。*

> In all other cases, a non-error return value guarantees that no exceptions have been thrown.

在所有其他情况下，非错误返回值可确保不会抛出任何异常。

### <a name="62">Asynchronous Exceptions</a>

> In cases of multiple threads, threads other than the current thread may post an asynchronous exception. An asynchronous exception does not immediately affect the execution of the native code in the current thread, until:
> - the native code calls one of the JNI functions that could raise synchronous exceptions
> - the native code uses ExceptionOccurred() to explicitly check for synchronous and asynchronous exceptions.

在多线程的情况下，除当前线程之外的线程可能发布异步异常。 异步异常不会立即影响当前线程中本机代码的执行，直到：
- 本机代码调用可能引发同步异常的JNI函数之一
- 本机代码使用ExceptionOccurred（）显式检查同步和异步异常。

> Note that only those JNI function that could potentially raise synchronous exceptions check for asynchronous exceptions.

请注意，只有那些可能引发同步异常的JNI函数才会检查异步异常。

> Native methods should insert ExceptionOccurred()checks in necessary places (such as in a tight loop without other exception checks) to ensure that the current thread responds to asynchronous exceptions in a reasonable amount of time.

本机方法应在必要的位置插入ExceptionOccurred（）检查（例如在没有其他异常检查的紧密循环中），以确保当前线程在合理的时间内响应异步异常。

### <a name="63">Exception Handling</a>

> There are two ways to handle an exception in native code:
> - The native method can choose to return immediately, causing the exception to be thrown in the Java code that initiated the native method call.
> - The native code can clear the exception by calling ExceptionClear(), and then execute its own exception-handling code.

有两种方法可以处理本机代码中的异常：
- 本机方法可以选择立即返回，从而导致在启动本机方法调用的Java代码中抛出异常。
- 本机代码可以通过调用ExceptionClear（）来清除异常，然后执行自己的异常处理代码。

> After an exception has been raised, the native code must first clear the exception before making other JNI calls. When there is a pending exception, the JNI functions that are safe to call are:

引发异常后，本机代码必须首先清除异常，然后再进行其他JNI调用。 当存在挂起的异常时，可以安全调用的JNI函数是：

	ExceptionOccurred()
	ExceptionDescribe()
	ExceptionClear()
	ExceptionCheck()
	ReleaseStringChars()
	ReleaseStringUTFChars()
	ReleaseStringCritical()
	Release<Type>ArrayElements()
	ReleasePrimitiveArrayCritical()
	DeleteLocalRef()
	DeleteGlobalRef()
	DeleteWeakGlobalRef()
	MonitorExit()
	PushLocalFrame()
	PopLocalFrame()

  [1]: sss
  [2]: ./images/designa.gif
  [3]: https://docs.oracle.com/javase/8/docs/technotes/guides/jni/spec/img_text/designa.html
  [4]: ssss