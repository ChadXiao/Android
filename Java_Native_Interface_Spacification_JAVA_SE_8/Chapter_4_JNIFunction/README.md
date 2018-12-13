# Chapter 4: JNI Functions


----------


> This chapter serves as the reference section for the JNI functions. It provides a complete listing of all the JNI functions. It also presents the exact layout of the JNI function table.

本章作为JNI函数的参考部分。 它提供了所有JNI功能的完整列表。 它还提供了JNI函数表的确切布局。

> Note the use of the term “must” to describe restrictions on JNI programmers. For example, when you see that a certain JNI function must receive a non-NULL object, it is your responsibility to ensure that NULL is not passed to that JNI function. As a result, a JNI implementation does not need to perform NULL pointer checks in that JNI function.

请注意使用术语“必须”来描述对JNI程序员的限制。 例如，当您看到某个JNI函数必须接收非NULL对象时，您有责任确保不将NULL传递给该JNI函数。 因此，JNI实现不需要在该JNI函数中执行NULL指针检查。

> A portion of this chapter is adapted from Netscape’s JRI documentation.

本章的一部分改编自Netscape的JRI文档。

The reference material groups functions by their usage. The reference section is organized by the following functional areas:

- <a href="#1">Interface Function Table</a>
- <a href="#2">Version Information</a>
	- <a href="#21">GetVersion</a>
	- <a href="#22">Constants</a>
- <a href="#3">Class Operations</a>
	- <a href="#31">DefineClass</a>
	- <a href="#32">FindClass</a>
	- <a href="#33">GetSuperclass</a>
	- <a href="#34">IsAssignableFrom</a>
- <a href="#4">Exceptions</a>
	- <a href="#41">Throw</a>
	- <a href="#42">ThrowNew</a>
	- <a href="#43">ExceptionOccurred</a>
	- <a href="#44">ExceptionDescribe</a>
	- <a href="#45">ExceptionClear</a>
	- <a href="#46">FatalError</a>
	- <a href="#47">ExceptionCheck</a>
- <a href="#5">Global and Local References</a>
	- <a href="#51">Global References</a>
	- <a href="#52">NewGlobalRef</a>
	- <a href="#53">DeleteGlobalRef</a>
	- <a href="#54">Local References</a>
	- <a href="#55">DeleteLocalRef</a>
	- <a href="#56">EnsureLocalCapacity</a>
	- <a href="#57">PushLocalFrame</a>
	- <a href="#58">PopLocalFrame</a>
	- <a href="#59">NewLocalRef</a>
- <a href="#6">Weak Global References</a>
	- <a href="#61">NewWeakGlobalRef</a>
	- <a href="#62">DeleteWeakGlobalRef</a>
- <a href="#7">Object Operations</a>
	- <a href="#71">AllocObject</a>
	- <a href="#72">NewObject, NewObjectA, NewObjectV</a>
	- <a href="#73">GetObjectClass</a>
	- <a href="#74">GetObjectRefType</a>
	- <a href="#75">IsInstanceOf</a>
	- <a href="#76">IsSameObject</a>
- <a href="#8">Accessing Fields of Objects</a>
	- <a href="#81">GetFieldID</a>
	- <a href="#82">Get\<type>Field Routines</a>
	- <a href="#83">Set\<type>Field Routines</a>
- <a href="#9">Calling Instance Methods</a>
	- <a href="#91">GetMethodID</a>
	- <a href="#92">Call\<type>Method Routines, Call\<type>MethodA Routines, Call\<type>MethodV Routines</a>
	- <a href="#93">CallNonvirtual\<type>Method Routines, CallNonvirtual\<type>MethodA Routines, CallNonvirtual\<type>MethodV Routines</a>
- <a href="#10">Accessing Static Fields</a>
	- <a href="#101">GetStaticFieldID</a>
	- <a href="#102">GetStatic\<type>Field Routines</a>
	- <a href="#103">SetStatic\<type>Field Routines</a>
- <a href="#11">Calling Static Methods</a>
	- <a href="#111">GetStaticMethodID</a>
	- <a href="#112">CallStatic\<type>Method Routines, CallStatic\<type>MethodA Routines, CallStatic\<type>MethodV Routines</a>
- <a href="#12">String Operations</a>
	- <a href="#121">NewString</a>
	- <a href="#122">GetStringLength</a>
	- <a href="#123">GetStringChars</a>
	- <a href="#124">ReleaseStringChars</a>
	- <a href="#125">NewStringUTF</a>
	- <a href="#126">GetStringUTFLength</a>
	- <a href="#127">GetStringUTFChars</a>
	- <a href="#128">ReleaseStringUTFChars</a>
	- <a href="#129">GetStringRegion</a>
	- <a href="#1210">GetStringUTFRegion</a>
	- <a href="#1211">GetStringCritical, ReleaseStringCritical</a>
- <a href="#13">Array Operations</a>
	- <a href="#131">GetArrayLength</a>
	- <a href="#132">NewObjectArray</a>
	- <a href="#133">GetObjectArrayElement</a>
	- <a href="#134">SetObjectArrayElement</a>
	- <a href="#135">New\<PrimitiveType>Array Routines</a>
	- <a href="#136">Get\<PrimitiveType>ArrayElements Routines</a>
	- <a href="#137">Release\<PrimitiveType>ArrayElements Routines</a>
	- <a href="#138">Get\<PrimitiveType>ArrayRegion Routines</a>
	- <a href="#139">Set\<PrimitiveType>ArrayRegion Routines</a>
	- <a href="#1310">GetPrimitiveArrayCritical, ReleasePrimitiveArrayCritical</a>
- <a href="#14">Registering Native Methods</a>
	- <a href="#141">RegisterNatives</a>
	- <a href="#142">UnregisterNatives</a>
- <a href="#15">Monitor Operations</a>
	- <a href="#151">MonitorEnter</a>
	- <a href="#152">MonitorExit</a>
- <a href="#16">NIO Support</a>
	- <a href="#161">NewDirectByteBuffer</a>
	- <a href="#162">GetDirectBufferAddress</a>
	- <a href="#163">GetDirectBufferCapacity</a>
- <a href="#17">Reflection Support</a>
	- <a href="#171">FromReflectedMethod</a>
	- <a href="#172">FromReflectedField</a>
	- <a href="#173">ToReflectedMethod</a>
	- <a href="#174">ToReflectedField</a>
- <a href="#18">Java VM Interface</a>
	- <a href="#181">GetJavaVM</a>
	
## <a name="1">Interface Function Table</a>
> Each function is accessible at a fixed offset through the JNIEnv argument. The JNIEnv type is a pointer to a structure storing all JNI function pointers. It is defined as follows:

可以通过JNIEnv参数以固定偏移量访问每个函数。 JNIEnv类型是指向存储所有JNI函数指针的结构的指针。 它的定义如下：

	typedef const struct JNINativeInterface *JNIEnv;
> The VM initializes the function table, as shown by the following code example. Note that the first three entries are reserved for future compatibility with COM. In addition, we reserve a number of additional NULL entries near the beginning of the function table, so that, for example, a future class-related JNI operation can be added after FindClass, rather than at the end of the table.

VM初始化函数表，如以下代码示例所示。 请注意，前三个条目保留用于将来与COM的兼容性。 此外，我们在函数表的开头附近保留了一些额外的NULL条目，因此，例如，可以在FindClass之后而不是在表的末尾添加与类相关的未来JNI操作。

> Note that the function table can be shared among all JNI interface pointers.

请注意，函数表可以在所有JNI接口指针之间共享。

	const struct JNINativeInterface ... = {

		NULL,
		NULL,
		NULL,
		NULL,
		GetVersion,

		DefineClass,
		FindClass,

		FromReflectedMethod,
		FromReflectedField,
		ToReflectedMethod,

		GetSuperclass,
		IsAssignableFrom,

		ToReflectedField,

		Throw,
		ThrowNew,
		ExceptionOccurred,
		ExceptionDescribe,
		ExceptionClear,
		FatalError,

		PushLocalFrame,
		PopLocalFrame,

		NewGlobalRef,
		DeleteGlobalRef,
		DeleteLocalRef,
		IsSameObject,
		NewLocalRef,
		EnsureLocalCapacity,

		AllocObject,
		NewObject,
		NewObjectV,
		NewObjectA,

		GetObjectClass,
		IsInstanceOf,

		GetMethodID,

		CallObjectMethod,
		CallObjectMethodV,
		CallObjectMethodA,
		CallBooleanMethod,
		CallBooleanMethodV,
		CallBooleanMethodA,
		CallByteMethod,
		CallByteMethodV,
		CallByteMethodA,
		CallCharMethod,
		CallCharMethodV,
		CallCharMethodA,
		CallShortMethod,
		CallShortMethodV,
		CallShortMethodA,
		CallIntMethod,
		CallIntMethodV,
		CallIntMethodA,
		CallLongMethod,
		CallLongMethodV,
		CallLongMethodA,
		CallFloatMethod,
		CallFloatMethodV,
		CallFloatMethodA,
		CallDoubleMethod,
		CallDoubleMethodV,
		CallDoubleMethodA,
		CallVoidMethod,
		CallVoidMethodV,
		CallVoidMethodA,

		CallNonvirtualObjectMethod,
		CallNonvirtualObjectMethodV,
		CallNonvirtualObjectMethodA,
		CallNonvirtualBooleanMethod,
		CallNonvirtualBooleanMethodV,
		CallNonvirtualBooleanMethodA,
		CallNonvirtualByteMethod,
		CallNonvirtualByteMethodV,
		CallNonvirtualByteMethodA,
		CallNonvirtualCharMethod,
		CallNonvirtualCharMethodV,
		CallNonvirtualCharMethodA,
		CallNonvirtualShortMethod,
		CallNonvirtualShortMethodV,
		CallNonvirtualShortMethodA,
		CallNonvirtualIntMethod,
		CallNonvirtualIntMethodV,
		CallNonvirtualIntMethodA,
		CallNonvirtualLongMethod,
		CallNonvirtualLongMethodV,
		CallNonvirtualLongMethodA,
		CallNonvirtualFloatMethod,
		CallNonvirtualFloatMethodV,
		CallNonvirtualFloatMethodA,
		CallNonvirtualDoubleMethod,
		CallNonvirtualDoubleMethodV,
		CallNonvirtualDoubleMethodA,
		CallNonvirtualVoidMethod,
		CallNonvirtualVoidMethodV,
		CallNonvirtualVoidMethodA,

		GetFieldID,

		GetObjectField,
		GetBooleanField,
		GetByteField,
		GetCharField,
		GetShortField,
		GetIntField,
		GetLongField,
		GetFloatField,
		GetDoubleField,
		SetObjectField,
		SetBooleanField,
		SetByteField,
		SetCharField,
		SetShortField,
		SetIntField,
		SetLongField,
		SetFloatField,
		SetDoubleField,

		GetStaticMethodID,

		CallStaticObjectMethod,
		CallStaticObjectMethodV,
		CallStaticObjectMethodA,
		CallStaticBooleanMethod,
		CallStaticBooleanMethodV,
		CallStaticBooleanMethodA,
		CallStaticByteMethod,
		CallStaticByteMethodV,
		CallStaticByteMethodA,
		CallStaticCharMethod,
		CallStaticCharMethodV,
		CallStaticCharMethodA,
		CallStaticShortMethod,
		CallStaticShortMethodV,
		CallStaticShortMethodA,
		CallStaticIntMethod,
		CallStaticIntMethodV,
		CallStaticIntMethodA,
		CallStaticLongMethod,
		CallStaticLongMethodV,
		CallStaticLongMethodA,
		CallStaticFloatMethod,
		CallStaticFloatMethodV,
		CallStaticFloatMethodA,
		CallStaticDoubleMethod,
		CallStaticDoubleMethodV,
		CallStaticDoubleMethodA,
		CallStaticVoidMethod,
		CallStaticVoidMethodV,
		CallStaticVoidMethodA,

		GetStaticFieldID,

		GetStaticObjectField,
		GetStaticBooleanField,
		GetStaticByteField,
		GetStaticCharField,
		GetStaticShortField,
		GetStaticIntField,
		GetStaticLongField,
		GetStaticFloatField,
		GetStaticDoubleField,

		SetStaticObjectField,
		SetStaticBooleanField,
		SetStaticByteField,
		SetStaticCharField,
		SetStaticShortField,
		SetStaticIntField,
		SetStaticLongField,
		SetStaticFloatField,
		SetStaticDoubleField,

		NewString,

		GetStringLength,
		GetStringChars,
		ReleaseStringChars,

		NewStringUTF,
		GetStringUTFLength,
		GetStringUTFChars,
		ReleaseStringUTFChars,

		GetArrayLength,

		NewObjectArray,
		GetObjectArrayElement,
		SetObjectArrayElement,

		NewBooleanArray,
		NewByteArray,
		NewCharArray,
		NewShortArray,
		NewIntArray,
		NewLongArray,
		NewFloatArray,
		NewDoubleArray,

		GetBooleanArrayElements,
		GetByteArrayElements,
		GetCharArrayElements,
		GetShortArrayElements,
		GetIntArrayElements,
		GetLongArrayElements,
		GetFloatArrayElements,
		GetDoubleArrayElements,

		ReleaseBooleanArrayElements,
		ReleaseByteArrayElements,
		ReleaseCharArrayElements,
		ReleaseShortArrayElements,
		ReleaseIntArrayElements,
		ReleaseLongArrayElements,
		ReleaseFloatArrayElements,
		ReleaseDoubleArrayElements,

		GetBooleanArrayRegion,
		GetByteArrayRegion,
		GetCharArrayRegion,
		GetShortArrayRegion,
		GetIntArrayRegion,
		GetLongArrayRegion,
		GetFloatArrayRegion,
		GetDoubleArrayRegion,
		SetBooleanArrayRegion,
		SetByteArrayRegion,
		SetCharArrayRegion,
		SetShortArrayRegion,
		SetIntArrayRegion,
		SetLongArrayRegion,
		SetFloatArrayRegion,
		SetDoubleArrayRegion,

		RegisterNatives,
		UnregisterNatives,

		MonitorEnter,
		MonitorExit,

		GetJavaVM,

		GetStringRegion,
		GetStringUTFRegion,

		GetPrimitiveArrayCritical,
		ReleasePrimitiveArrayCritical,

		GetStringCritical,
		ReleaseStringCritical,

		NewWeakGlobalRef,
		DeleteWeakGlobalRef,

		ExceptionCheck,

		NewDirectByteBuffer,
		GetDirectBufferAddress,
		GetDirectBufferCapacity,

		GetObjectRefType
	  };
	  
## <a name="2">Version Information</a>
### <a name="21">GetVersion</a>

	jint GetVersion(JNIEnv *env);

> Returns the version of the native method interface.

返回本机方法接口的版本。

**LINKAGE:**
> Index 4 in the JNIEnv interface function table.
JNIEnv接口函数表中的索引4。

**PARAMETERS:**
> env: the JNI interface pointer.

env：JNI接口指针

**RETURNS:**
> Returns the major version number in the higher 16 bits and the minor version number in the lower 16 bits.

返回高16位的主版本号和低16位的次版本号。

> In JDK/JRE 1.1, GetVersion() returns 0x00010001.
> 
> In JDK/JRE 1.2, GetVersion() returns 0x00010002.
> 
> In JDK/JRE 1.4, GetVersion() returns 0x00010004.
> 
> In JDK/JRE 1.6, GetVersion() returns 0x00010006.

### <a name="22">Constants</a>
**SINCE JDK/JRE 1.2:**

	#define JNI_VERSION_1_1 0x00010001
	#define JNI_VERSION_1_2 0x00010002

	/* Error codes */
	#define JNI_EDETACHED    (-2)              /* thread detached from the VM */
	#define JNI_EVERSION     (-3)              /* JNI version error 
**SINCE JDK/JRE 1.4:**

    #define JNI_VERSION_1_4 0x00010004
	
**SINCE JDK/JRE 1.6:**

    #define JNI_VERSION_1_6 0x00010006
	
## <a name="3">Class Operations</a>
### <a name="31">DefineClass</a>

	jclass DefineClass(JNIEnv *env, const char *name, jobject loader,
	const jbyte *buf, jsize bufLen);

> Loads a class from a buffer of raw class data. The buffer containing the raw class data is not referenced by the VM after the DefineClass call returns, and it may be discarded if desired.

从原始类数据的缓冲区加载一个类。 在DefineClass调用返回后，VM不会引用包含原始类数据的缓冲区，如果需要，可以将其丢弃。

**LINKAGE:**

> Index 5 in the JNIEnv interface function table.

JNIEnv接口函数表中的索引5。

**PARAMETERS:**

> env: the JNI interface pointer.
> 
>name: the name of the class or interface to be defined. The string is encoded in modified UTF-8.
>
>loader: a class loader assigned to the defined class.
>
>buf: buffer containing the .class file data.
>
>bufLen: buffer length.

env：JNI接口指针。
name：要定义的类或接口的名称。 该字符串以修改后的UTF-8编码。
loader：分配给已定义类的类加载器。
buf：包含.class文件数据的缓冲区。
bufLen：缓冲区长度。


**RETURNS:**

> Returns a Java class object or NULL if an error occurs.

如果发生错误，则返回Java类对象或NULL。

**THROWS:**

> *ClassFormatError*: if the class data does not specify a valid class.
>
> *ClassCircularityError*: if a class or interface would be its own superclass or superinterface.
>
> *OutOfMemoryError*: if the system runs out of memory.
>
> *SecurityException*: if the caller attempts to define a class in the "java" package tree.

*ClassFormatError*：如果类数据未指定有效类。
*ClassCircularityError*：如果类或接口是它自己的超类或超接口。
*OutOfMemoryError*：如果系统内存不足。
*SecurityException*：如果调用者尝试在“java”包树中定义一个类。

## <a name="32"> FindClass</a>

	jclass FindClass(JNIEnv *env, const char *name);

> In JDK release 1.1, this function loads a locally-defined class. It searches the directories and zip files specified by the CLASSPATH environment variable for the class with the specified name.

在JDK 1.1版中，此函数加载本地定义的类。 它搜索CLASSPATH环境变量指定的目录和zip文件，以查找具有指定名称的类。

> Since Java 2 SDK release 1.2, the Java security model allows non-system classes to load and call native methods. FindClass locates the class loader associated with the current native method; that is, the class loader of the class that declared the native method. If the native method belongs to a system class, no class loader will be involved. Otherwise, the proper class loader will be invoked to load and link the named class.

从Java 2 SDK 1.2版开始，Java安全模型允许非系统类加载和调用本机方法。 FindClass定位与当前本机方法关联的类加载器; 也就是说，声明本机方法的类的类加载器。 如果本机方法属于系统类，则不涉及类加载器。 否则，将调用适当的类加载器来加载和链接命名类。

> Since Java 2 SDK release 1.2, when FindClass is called through the Invocation Interface, there is no current native method or its associated class loader. In that case, the result of ClassLoader.getSystemClassLoader is used. This is the class loader the virtual machine creates for applications, and is able to locate classes listed in the java.class.path property.

从Java 2 SDK 1.2版开始，当通过调用接口调用FindClass时，没有当前的本机方法或其关联的类加载器。 在这种情况下，使用ClassLoader.getSystemClassLoader的结果。 这是虚拟机为应用程序创建的类加载器，并且能够定位java.class.path属性中列出的类。

> The name argument is a fully-qualified class name or an array type signature . For example, the fully-qualified class name for the java.lang.String class is:

name参数是完全限定的类名或数组类型签名。 例如，java.lang.String类的完全限定类名是：

	   "java/lang/String"

> The array type signature of the array class java.lang.Object[] is:

数组类java.lang.Object []的数组类型签名是：

	   "[Ljava/lang/Object;"


**LINKAGE:**

> Index 6 in the JNIEnv interface function table.

JNIEnv接口函数表中的索引6。

**PARAMETERS:**
> env: the JNI interface pointer.
> 
> name: a fully-qualified class name (that is, a package name, delimited by “/”, followed by the class name). If the name begins with “[“ (the array signature character), it returns an array class. The string is encoded in modified UTF-8.

env：JNI接口指针。
name：完全限定的类名（即包名，由“/”分隔，后跟类名）。 如果名称以“[”（数组签名字符）开头，则返回数组类。 该字符串以修改后的UTF-8编码。

**RETURNS:**

> Returns a class object from a fully-qualified name, or NULL if the class cannot be found.

从完全限定名称返回类对象，如果找不到类，则返回NULL。

**THROWS:**

> *ClassFormatError*: if the class data does not specify a valid class.
> 
> *ClassCircularityError*: if a class or interface would be its own
> superclass or superinterface.
> 
> *NoClassDefFoundError*: if no definition for a requested class or
> interface can be found.
> 
> *OutOfMemoryError*: if the system runs out of memory.

*ClassFormatError*：如果类数据未指定有效类。
*ClassCircularityError*：如果类或接口是它自己的超类或超接口。
*NoClassDefFoundError*：如果找不到所请求的类或接口的定义。
*OutOfMemoryError*：如果系统内存不足。

### <a name="33">GetSuperclass</a>

	jclass GetSuperclass(JNIEnv *env, jclass clazz);

> If clazz represents any class other than the class Object, then this function returns the object that represents the superclass of the class specified by clazz.

如果clazz表示除Object类之外的任何类，则此函数返回表示clazz指定的类的超类的对象

> If clazz specifies the class Object, or clazz represents an interface, this function returns NULL.

如果clazz指定类Object，或者clazz表示接口，则此函数返回NULL。

**LINKAGE:**
> Index 10 in the JNIEnv interface function table.
> 
JNIEnv接口函数表中的索引10。


**PARAMETERS:**

> env: the JNI interface pointer.
> 
> clazz: a Java class object.

env：JNI接口指针。
clazz：一个Java类对象。

**RETURNS:**

> Returns the superclass of the class represented by clazz, or NULL.

返回clazz表示的类的超类，或NULL。

### <a name="34">IsAssignableFrom</a>

	jboolean IsAssignableFrom(JNIEnv *env, jclass clazz1, jclass clazz2);

> Determines whether an object of clazz1 can be safely cast to clazz2.

确定clazz1的对象是否可以安全地转换为clazz2。

**LINKAGE:**
> Index 11 in the JNIEnv interface function table.

JNIEnv接口函数表中的索引11。

**PARAMETERS:**

> env: the JNI interface pointer.
> 
> clazz1: the first class argument.
> 
> clazz2: the second class argument.

env：JNI接口指针。
clazz1：第一类参数。
clazz2：第二类参数。

**RETURNS:**

> Returns JNI_TRUE if either of the following is true:
> -  The first and second class arguments refer to the same Java class. 
> - The first class is a subclass of the second class. 
> - The first class has the second class as one of its interfaces.

## <a name="4">Exceptions</a>

### <a name="41">Throw</a>

	jint Throw(JNIEnv *env, jthrowable obj);

Causes a java.lang.Throwable object to be thrown.

**LINKAGE:**
Index 13 in the JNIEnv interface function table.

**PARAMETERS:**
env: the JNI interface pointer.

obj: a java.lang.Throwable object.

**RETURNS:**
Returns 0 on success; a negative value on failure.

**THROWS:**
the java.lang.Throwable object obj.

### <a name="42">ThrowNew</a>

	jint ThrowNew(JNIEnv *env, jclass clazz, const char *message);

> Constructs an exception object from the specified class with the message specified by message and causes that exception to be thrown.

使用message指定的消息从指定的类构造一个异常对象，并导致抛出该异常。

**LINKAGE:**
Index 14 in the JNIEnv interface function table.

**PARAMETERS:**
*env*: the JNI interface pointer.

*clazz*: a subclass of java.lang.Throwable.

*message*: the message used to construct the java.lang.Throwable object. The string is encoded in modified UTF-8.

**RETURNS:**

Returns 0 on success; a negative value on failure.

**THROWS:**

the newly constructed java.lang.Throwable object.

### <a name="43">ExceptionOccurred</a>

	jthrowable ExceptionOccurred(JNIEnv *env);

> Determines if an exception is being thrown. The exception stays being thrown until either the native code calls ExceptionClear(), or the Java code handles the exception.

确定是否抛出异常。 在本机代码调用ExceptionClear（）或Java代码处理异常之前，异常会一直抛出。

**LINKAGE:**

Index 15 in the JNIEnv interface function table.

**PARAMETERS:**
env: the JNI interface pointer.

**RETURNS:**

Returns the exception object that is currently in the process of being thrown, or NULL if no exception is currently being thrown.

### <a name="44">ExceptionDescribe</a>

	void ExceptionDescribe(JNIEnv *env);

> Prints an exception and a backtrace of the stack to a system error-reporting channel, such as stderr. This is a convenience routine provided for debugging.

将堆栈的异常和回溯打印到系统错误报告通道，例如stderr。 这是为调试提供的便利例程。

**LINKAGE:**
Index 16 in the JNIEnv interface function table.

**PARAMETERS:**
env: the JNI interface pointer.

### <a name="45">ExceptionClear</a>

	void ExceptionClear(JNIEnv *env);

> Clears any exception that is currently being thrown. If no exception is currently being thrown, this routine has no effect.

清除当前正在抛出的任何异常。 如果当前没有抛出异常，则此例程无效。

**LINKAGE:**
Index 17 in the JNIEnv interface function table.

**PARAMETERS:**
env: the JNI interface pointer.

### <a name="46">FatalError</a>

	void FatalError(JNIEnv *env, const char *msg);

> Raises a fatal error and does not expect the VM to recover. This function does not return.

引发致命错误，并且不希望VM恢复。 此功能不返回。

**LINKAGE:**
Index 18 in the JNIEnv interface function table.

**PARAMETERS:**
*env*: the JNI interface pointer.

*msg*: an error message. The string is encoded in modified UTF-8.

### <a name="47">ExceptionCheck</a>
> We introduce a convenience function to check for pending exceptions without creating a local reference to the exception object.

我们引入了一个便捷函数来检查挂起的异常，而不创建对异常对象的本地引用。

	jboolean ExceptionCheck(JNIEnv *env);

> Returns JNI_TRUE when there is a pending exception; otherwise, returns JNI_FALSE.

存在挂起异常时返回JNI_TRUE; 否则，返回JNI_FALSE。

**LINKAGE:**

Index 228 in the JNIEnv interface function table.

**SINCE:**

JDK/JRE 1.2


## <a name="5">Global and Local References</a>


### <a name="51">Global References</a>
### <a name="52">NewGlobalRef</a>

	jobject NewGlobalRef(JNIEnv *env, jobject obj);

> Creates a new global reference to the object referred to by the obj argument. The obj argument may be a global or local reference. Global references must be explicitly disposed of by calling DeleteGlobalRef().

创建对obj参数引用的对象的新全局引用。 obj参数可以是全局或本地引用。 必须通过调用DeleteGlobalRef（）显式处理全局引用。

**LINKAGE:**
Index 21 in the JNIEnv interface function table.

**PARAMETERS:**
*env*: the JNI interface pointer.

*obj*: a global or local reference.

**RETURNS:**

Returns a global reference, or NULL if the system runs out of memory.

### <a name="53">DeleteGlobalRef</a>

	void DeleteGlobalRef(JNIEnv *env, jobject globalRef);

> Deletes the global reference pointed to by globalRef.

删除globalRef指向的全局引用

**LINKAGE:**
Index 22 in the JNIEnv interface function table.

**PARAMETERS:**
*env*: the JNI interface pointer.

*globalRef*: a global reference.

### <a name="54">Local References</a>

> Local references are valid for the duration of a native method call. They are freed automatically after the native method returns. Each local reference costs some amount of Java Virtual Machine resource. Programmers need to make sure that native methods do not excessively allocate local references. Although local references are automatically freed after the native method returns to Java, excessive allocation of local references may cause the VM to run out of memory during the execution of a native method.

本地引用在本机方法调用的持续时间内有效。 它们在本机方法返回后自动释放。 每个本地引用都会花费一定量的Java虚拟机资源。 程序员需要确保本机方法不会过度分配本地引用。 尽管在本机方法返回到Java之后会自动释放本地引用，但过度分配本地引用可能会导致VM在执行本机方法期间耗尽内存。

### <a name="55">DeleteLocalRef</a>

	void DeleteLocalRef(JNIEnv *env, jobject localRef);

> Deletes the local reference pointed to by localRef.

删除localRef指向的本地引用。

**LINKAGE:**
Index 23 in the JNIEnv interface function table.

**PARAMETERS:**
*env*: the JNI interface pointer.

*localRef*: a local reference.

> **Note**: JDK/JRE 1.1 provides the DeleteLocalRef function above so that programmers can manually delete local references. For example, if native code iterates through a potentially large array of objects and uses one element in each iteration, it is a good practice to delete the local reference to the no-longer-used array element before a new local reference is created in the next iteration.

> As of JDK/JRE 1.2 an additional set of functions are provided for local reference lifetime management. They are the four functions listed below.

从JDK / JRE 1.2开始，为本地参考生命周期管理提供了一组额外的功能。 它们是下面列出的四个功能。

### <a name="56">EnsureLocalCapacity</a>

	jint EnsureLocalCapacity(JNIEnv *env, jint capacity);

> Ensures that at least a given number of local references can be created in the current thread. Returns 0 on success; otherwise returns a negative number and throws an OutOfMemoryError.

确保在当前线程中至少可以创建给定数量的本地引用。 成功时返回0; 否则返回一个负数并抛出一个OutOfMemoryError。

> Before it enters a native method, the VM automatically ensures that at least 16 local references can be created.

在进入本机方法之前，VM会自动确保至少可以创建16个本地引用。

> For backward compatibility, the VM allocates local references beyond the ensured capacity. (As a debugging support, the VM may give the user warnings that too many local references are being created. In the JDK, the programmer can supply the -verbose:jni command line option to turn on these messages.) The VM calls FatalError if no more local references can be created beyond the ensured capacity.

为了向后兼容，VM分配超出保证容量的本地引用。 （作为调试支持，VM可能会向用户发出警告，指出正在创建太多本地引用。在JDK中，程序员可以提供-verbose：jni命令行选项来打开这些消息。）VM调用FatalError if 除了确保的容量之外，不能再创建本地引用。

**LINKAGE:**

Index 26 in the JNIEnv interface function table.

**SINCE:**

JDK/JRE 1.2

### <a name="57">PushLocalFrame</a>

	jint PushLocalFrame(JNIEnv *env, jint capacity);

> Creates a new local reference frame, in which at least a given number of local references can be created. Returns 0 on success, a negative number and a pending OutOfMemoryError on failure.

创建一个新的本地参考框架，其中至少可以创建给定数量的本地参考。 成功时返回0，失败时返回负数和挂起的OutOfMemoryError。

> Note that local references already created in previous local frames are still valid in the current local frame.

请注意，已在先前本地帧中创建的本地引用在当前本地帧中仍然有效。

**LINKAGE:**

Index 19 in the JNIEnv interface function table.

**SINCE:**

JDK/JRE 1.2

### <a name="58">PopLocalFrame</a>

	jobject PopLocalFrame(JNIEnv *env, jobject result);

> Pops off the current local reference frame, frees all the local references, and returns a local reference in the previous local reference frame for the given result object.

弹出当前本地引用框架，释放所有本地引用，并在给定结果对象的先前本地引用框架中返回本地引用。

> Pass NULL as result if you do not need to return a reference to the previous frame.

Pass NULL as result if you do not need to return a reference to the previous frame.

**LINKAGE:**

Index 20 in the JNIEnv interface function table.

**SINCE:**

JDK/JRE 1.2

### <a name="59">NewLocalRef</a>

	jobject NewLocalRef(JNIEnv *env, jobject ref);

> Creates a new local reference that refers to the same object as ref. The given ref may be a global or local reference. Returns NULL if ref refers to null.

创建一个引用与ref相同的对象的新本地引用。 给定的ref可以是全局或本地引用。 如果ref引用null，则返回NULL。

**LINKAGE:**

Index 25 in the JNIEnv interface function table.

**SINCE:**

JDK/JRE 1.2

## <a name="6">Weak Global References</a>

>Weak global references are a special kind of global reference. Unlike normal global references, a weak global reference allows the underlying Java object to be garbage collected. Weak global references may be used in any situation where global or local references are used. When the garbage collector runs, it frees the underlying object if the object is only referred to by weak references. A weak global reference pointing to a freed object is functionally equivalent to NULL. Programmers can detect whether a weak global reference points to a freed object by using IsSameObject to compare the weak reference against NULL.

弱全局引用是一种特殊的全局引用。 与普通的全局引用不同，弱全局引用允许对底层Java对象进行垃圾回收。 在使用全局或本地引用的任何情况下都可以使用弱全局引用。 当垃圾收集器运行时，如果该对象仅由弱引用引用，则它释放底层对象。 指向释放对象的弱全局引用在功能上等效于NULL。 程序员可以通过使用IsSameObject将弱引用与NULL进行比较来检测弱全局引用是否指向释放的对象。

> Weak global references in JNI are a simplified version of the Java Weak References, available as part of the Java 2 Platform API ( java.lang.ref package and its classes).

JNI中的弱全局引用是Java Weak References的简化版本，可作为Java 2 Platform API（java.lang.ref包及其类）的一部分提供。

**Clarification   (added June 2001)**

> Since garbage collection may occur while native methods are running, objects referred to by weak global references can be freed at any time. While weak global references can be used where global references are used, it is generally inappropriate to do so, as they may become functionally equivalent to NULL without notice.

由于在本机方法运行时可能会发生垃圾收集，因此可以随时释放由弱全局引用引用的对象。 虽然可以在使用全局引用的地方使用弱全局引用，但这样做通常是不合适的，因为它们可能在功能上等同于NULL而不另行通知。

> While IsSameObject can be used to determine whether a weak global reference refers to a freed object, it does not prevent the object from being freed immediately thereafter. Consequently, programmers may not rely on this check to determine whether a weak global reference may used (as a non-NULL reference) in any future JNI function call.

虽然IsSameObject可用于确定弱全局引用是否引用已释放的对象，但它不会阻止此对象立即被释放。 因此，程序员可能不依赖此检查来确定在将来的任何JNI函数调用中是否可以使用弱全局引用（作为非NULL引用）。

> To overcome this inherent limitation, it is recommended that a standard (strong) local or global reference to the same object be acquired using the JNI functions NewLocalRef or NewGlobalRef, and that this strong reference be used to access the intended object. These functions will return NULL if the object has been freed, and otherwise will return a strong reference (which will prevent the object from being freed). The new reference should be explicitly deleted when immediate access to the object is no longer required, allowing the object to be freed.

为了克服这种固有的限制，建议使用JNI函数NewLocalRef或NewGlobalRef获取对同一对象的标准（强）本地或全局引用，并使用此强引用来访问目标对象。 如果对象已被释放，这些函数将返回NULL，否则将返回强引用（这将阻止对象被释放）。 当不再需要立即访问对象时，应该显式删除新引用，从而允许释放对象。

> The weak global reference is weaker than other types of weak references (Java objects of the SoftReference or WeakReference classes). A weak global reference to a specific object will not become functionally equivalent to NULL until after SoftReference or WeakReference objects referring to that same specific object have had their references cleared.

弱全局引用弱于其他类型的弱引用（SoftReference或WeakReference类的Java对象）。 在引用同一特定对象的SoftReference或WeakReference对象清除其引用之前，对特定对象的弱全局引用在功能上不会等效于NULL。

> The weak global reference is weaker than Java's internal references to objects requiring finalization. A weak global reference will not become functionally equivalent to NULL until after the completion of the finalizer for the referenced object, if present.

弱全局引用弱于Java对需要完成的对象的内部引用。 在完成引用对象的终结器（如果存在）之后，弱全局引用将不会在功能上等效于NULL。

> Interactions between weak global references and PhantomReferences are undefined. In particular, implementations of a Java VM may (or may not) process weak global references after PhantomReferences, and it may (or may not) be possible to use weak global references to hold on to objects which are also referred to by PhantomReference objects. This undefined use of weak global references should be avoided.

弱全局引用和PhantomReferences之间的交互未定义。 特别地，Java VM的实现可以（或可以不）在PhantomReferences之后处理弱全局引用，并且它可以（或可以不）使用弱全局引用来保持也由PhantomReference对象引用的对象。 应避免使用弱全局引用的未定义。

### <a name="61">NewWeakGlobalRef</a>

	jweak NewWeakGlobalRef(JNIEnv *env, jobject obj);

> Creates a new weak global reference. Returns NULL if obj refers to null, or if the VM runs out of memory. If the VM runs out of memory, an *OutOfMemoryError* will be thrown.

创建一个新的弱全局引用。 如果obj引用null，或者VM内存不足，则返回NULL。 如果VM内存不足，将抛出*OutOfMemoryError*。

**LINKAGE:**

Index 226 in the JNIEnv interface function table.

**SINCE:**

JDK/JRE 1.2

### <a name="62">DeleteWeakGlobalRef</a>

	void DeleteWeakGlobalRef(JNIEnv *env, jweak obj);

> Delete the VM resources needed for the given weak global reference.

删除给定的弱全局引用所需的VM资源。

**LINKAGE:**

Index 227 in the JNIEnv interface function table.

**SINCE:**

JDK/JRE 1.2

## <a name="7">Object Operations</a>

### <a name="71">AllocObject</a>

	jobject AllocObject(JNIEnv *env, jclass clazz);

> Allocates a new Java object without invoking any of the constructors for the object. Returns a reference to the object.

在不调用对象的任何构造函数的情况下分配新的Java对象。 返回对象的引用。

> The clazz argument must not refer to an array class.

clazz参数不能引用数组类。

**LINKAGE:**

Index 27 in the JNIEnv interface function table.

**PARAMETERS:**
*env*: the JNI interface pointer.

*clazz*: a Java class object.

**RETURNS:**
Returns a Java object, or NULL if the object cannot be constructed.

**THROWS:**
*InstantiationException*: if the class is an interface or an abstract class.

*OutOfMemoryError*: if the system runs out of memory.

### <a name="72">NewObject, NewObjectA, NewObjectV</a>

	jobject NewObject(JNIEnv *env, jclass clazz,
	jmethodID methodID, ...);

	jobject NewObjectA(JNIEnv *env, jclass clazz,
	jmethodID methodID, const jvalue *args);

	jobject NewObjectV(JNIEnv *env, jclass clazz,
	jmethodID methodID, va_list args);

> Constructs a new Java object. The method ID indicates which constructor method to invoke. This ID must be obtained by calling GetMethodID() with \<init> as the method name and void (V) as the return type.

构造一个新的Java对象。 方法ID指示要调用的构造方法。 必须通过调用GetMethodID（）并使用\<init>作为方法名称并将void（V）作为返回类型来获取此ID。

> The clazz argument must not refer to an array class.

clazz参数不能引用数组类。

#### NewObject

> Programmers place all arguments that are to be passed to the constructor immediately following the methodID argument. NewObject() accepts these arguments and passes them to the Java method that the programmer wishes to invoke.

程序员将所有要传递给构造函数的参数紧跟在methodID参数之后。 NewObject（）接受这些参数并将它们传递给程序员希望调用的Java方法。

**LINKAGE:**
Index 28 in the JNIEnv interface function table.

####  NewObjectA

>  Programmers place all arguments that are to be passed to the constructor in an args array of jvalues that immediately follows the methodID argument. NewObjectA() accepts the arguments in this array, and, in turn, passes them to the Java method that the programmer wishes to invoke.

程序员将所有要传递给构造函数的参数放在紧跟在methodID参数之后的jvalues的args数组中。 NewObjectA（）接受此数组中的参数，然后将它们传递给程序员希望调用的Java方法。

**LINKAGE:**
Index 30 in the JNIEnv interface function table.

#### NewObjectV

> Programmers place all arguments that are to be passed to the constructor in an args argument of type va_list that immediately follows the methodID argument. NewObjectV() accepts these arguments, and, in turn, passes them to the Java method that the programmer wishes to invoke.

程序员将所有要传递给构造函数的参数放在紧跟在methodID参数之后的类型为va_list的args参数中。 NewObjectV（）接受这些参数，然后将它们传递给程序员希望调用的Java方法。

**LINKAGE:**
Index 29 in the JNIEnv interface function table.

**PARAMETERS:**
*env*: the JNI interface pointer.

*clazz*: a Java class object.

*methodID*: the method ID of the constructor.

 **Additional Parameter for NewObject:**

> arguments to the constructor.

构造函数的参数。

**Additional Parameter for NewObjectA:**

*args*: an array of arguments to the constructor.

**Additional Parameter for NewObjectV:**

*args*: a va_list of arguments to the constructor.

**RETURNS:**
Returns a Java object, or NULL if the object cannot be constructed.

**THROWS:**
*InstantiationException*: if the class is an interface or an abstract class.

*OutOfMemoryError*: if the system runs out of memory.

> Any exceptions thrown by the constructor.

构造函数抛出的任何异常。

### <a name="73">GetObjectClass</a>


	jclass GetObjectClass(JNIEnv *env, jobject obj);

> Returns the class of an object.

返回对象的类。

**LINKAGE:**
Index 31 in the JNIEnv interface function table.

**PARAMETERS:**
*env*: the JNI interface pointer.

*obj*: a Java object (must not be NULL).

**RETURNS:**
Returns a Java class object.

### <a name="74">GetObjectRefType</a>

	jobjectRefType GetObjectRefType(JNIEnv* env, jobject obj);

> Returns the type of the object referred to by the obj argument. The argument obj can either be a local, global or weak global reference.

返回obj参数引用的对象的类型。 参数obj可以是本地，全局或弱全局引用。

**LINKAGE:**
Index 232 in the JNIEnv interface function table.

**PARAMETERS:**
*env*: the JNI interface pointer.

*obj*: a local, global or weak global reference.

*vm*: the virtual machine instance from which the interface will be retrieved.

*env*: pointer to the location where the JNI interface pointer for the current thread will be placed.

*version*: the requested JNI version.

**RETURNS:**

The function GetObjectRefType returns one of the following enumerated values defined as a jobjectRefType:

	JNIInvalidRefType = 0,
	JNILocalRefType = 1,
	JNIGlobalRefType = 2,
	JNIWeakGlobalRefType = 3
	
> If the argument obj is a weak global reference type, the return will be JNIWeakGlobalRefType.

如果参数obj是弱全局引用类型，则返回将为JNIWeakGlobalRefType。

> If the argument obj is a global reference type, the return value will be JNIGlobalRefType.

如果参数obj是全局引用类型，则返回值将为JNIGlobalRefType。

> If the argument obj is a local reference type, the return will be JNILocalRefType.

如果参数obj是本地引用类型，则返回将为JNILocalRefType。

> If the obj argument is not a valid reference, the return value for this function will be JNIInvalidRefType.

如果obj参数不是有效引用，则此函数的返回值将为JNIInvalidRefType。

> An invalid reference is a reference which is not a valid handle. That is, the obj pointer address does not point to a location in memory which has been allocated from one of the Ref creation functions or returned from a JNI function.

无效引用是不是有效句柄的引用。 也就是说，obj指针地址不指向已从Ref创建函数之一分配或从JNI函数返回的存储器中的位置。

> As such, NULL would be an invalid reference and GetObjectRefType(env,NULL) would return JNIInvalidRefType.

因此，NULL将是无效引用，GetObjectRefType（env，NULL）将返回JNIInvalidRefType。

> On the other hand, a null reference, which is a reference that points to a null, would return the type of reference that the null reference was originally created as.

另一方面，null引用（指向null的引用）将返回最初创建null引用的引用类型。

> *GetObjectRefType* cannot be used on deleted references.

GetObjectRefType不能用于已删除的引用。

> Since references are typically implemented as pointers to memory data structures that can potentially be reused by any of the reference allocation services in the VM, once deleted, it is not specified what value the GetObjectRefType will return.

由于引用通常实现为指向内存数据结构的指针，这些内存数据结构可能被VM中的任何引用分配服务重用，因此一旦删除，就不会指定GetObjectRefType将返回什么值。

**SINCE:**
JDK/JRE 1.6

### <a name="75">IsInstanceOf</a>

	jboolean IsInstanceOf(JNIEnv *env, jobject obj, jclass clazz);

> Tests whether an object is an instance of a class.

测试对象是否是类的实例。

**LINKAGE:**
Index 32 in the JNIEnv interface function table.

**PARAMETERS:**
*env*: the JNI interface pointer.

*obj*: a Java object.

*clazz*: a Java class object.

**RETURNS:**
Returns JNI_TRUE if obj can be cast to clazz; otherwise, returns JNI_FALSE. A NULL object can be cast to any class.

### <a name="75">IsSameObject</a>

	jboolean IsSameObject(JNIEnv *env, jobject ref1, jobject ref2);

> Tests whether two references refer to the same Java object.

测试两个引用是否引用相同的Java对象。

**LINKAGE:**
Index 24 in the JNIEnv interface function table.

**PARAMETERS:**
*env*: the JNI interface pointer.

*ref1*: a Java object.

*ref2*: a Java object.

**RETURNS:**

> Returns JNI_TRUE if ref1 and ref2 refer to the same Java object, or are both NULL; otherwise, returns JNI_FALSE.

如果ref1和ref2引用相同的Java对象，则返回JNI_TRUE，或者都是NULL; 否则，返回JNI_FALSE。

## <a name="8">Accessing Fields of Objects</a>

### <a name="81">GetFieldID</a>

	jfieldID GetFieldID(JNIEnv *env, jclass clazz, const char *name, const char *sig);

> Returns the field ID for an instance (nonstatic) field of a class. The field is specified by its name and signature. The Get<type>Field and Set<type>Field families of accessor functions use field IDs to retrieve object fields.

返回类的实例（非静态）字段的字段ID。 该字段由其名称和签名指定。 访问者函数的Get <type> Field和Set <type>字段系列使用字段ID来检索对象字段。

> GetFieldID() causes an uninitialized class to be initialized.

GetFieldID（）导致初始化未初始化的类。

> GetFieldID() cannot be used to obtain the length field of an array. Use GetArrayLength() instead.

GetFieldID（）不能用于获取数组的长度字段。 请改用GetArrayLength（）。

**LINKAGE:**
Index 94 in the JNIEnv interface function table.

**PARAMETERS:**
*env*: the JNI interface pointer.

*clazz*: a Java class object.

*name*: the field name in a 0-terminated modified UTF-8 string.

*sig*: the field signature in a 0-terminated modified UTF-8 string.

**RETURNS:**
Returns a field ID, or NULL if the operation fails.

**THROWS:**

*NoSuchFieldError*: if the specified field cannot be found.
*ExceptionInInitializerError*: if the class initializer fails due to an exception.
*OutOfMemoryError*: if the system runs out of memory.

### <a name="82">Get\<type>Field Routines</a>

	NativeType Get<type>Field(JNIEnv *env, jobject obj, jfieldID fieldID);

> This family of accessor routines returns the value of an instance (nonstatic) field of an object. The field to access is specified by a field ID obtained by calling GetFieldID().

此系列的访问器例程返回对象的实例（非静态）字段的值。 要访问的字段由通过调用GetFieldID（）获得的字段ID指定。

> The following table describes the Get\<type>Field routine name and result type. You should replace type in Get\<type>Field with the Java type of the field, or use one of the actual routine names from the table, and replace NativeType with the corresponding native type for that routine.

下表描述了Get\<type> Field例程名称和结果类型。 您应该使用字段的Java类型替换Get \<type> Field中的type，或者使用表中的一个实际例程名称，并将NativeType替换为该例程的相应本机类型。

**Get\<type>Field Family of Accessor Routines**
|  Get\<type>Field Routine Name   |   	Native Type  |
| --- | --- |
|   GetObjectField()  |  jobject   |
|  GetBooleanField()   |  jboolean   |
| GetByteField()    |  jbyte   |
|  GetCharField()   |   jchar  |
|   GetShortField()  |  jshort   |
|   GetIntField()  |  jint   |
|  GetLongField()   |   jlong  |
|  GetFloatField()   |   jfloat  |
| GetDoubleField()    |   jdouble  |

**LINKAGE:**

> Indices in the JNIEnv interface function table:

JNIEnv接口函数表中的索引：

**Get\<type>Field Family of Accessor Routines**

|   Get\<type>Field Routine Name  |  Index   |
| --- | --- |
|  GetObjectField()   |   95  |
|  GetBooleanField()   |    96 |
|  GetByteField()   |   97  |
|   GetCharField()  |    98 |
| GetShortField()    |    99 |
|   GetIntField()  |    100 |
|  GetLongField()	   |  101   |
|  GetFloatField()	   |   102  |
|   GetDoubleField()  |   103  |

**PARAMETERS:**

*env*: the JNI interface pointer.
*obj*: a Java object (must not be NULL).
*fieldID*: a valid field ID.

**RETURNS:**
Returns the content of the field.

### <a name="83">Set\<type>Field Routines</a>
	void Set<type>Field(JNIEnv *env, jobject obj, jfieldID fieldID, NativeType value);

> This family of accessor routines sets the value of an instance (nonstatic) field of an object. The field to access is specified by a field ID obtained by calling GetFieldID().

此系列的访问器例程设置对象的实例（非静态）字段的值。 要访问的字段由通过调用GetFieldID（）获得的字段ID指定。

> The following table describes the Set\<type>Field routine name and value type. You should replace type in Set\<type>Field with the Java type of the field, or use one of the actual routine names from the table, and replace NativeType with the corresponding native type for that routine.

下表描述了Set \<type> Field例程名称和值类型。 您应该使用字段的Java类型替换Set \<type> Field中的类型，或者使用表中的一个实际例程名称，并将NativeType替换为该例程的相应本机类型。

**Set\<type>Field Family of Accessor Routines**

|  Set\<type>Field Routine   | Native Type    |
| --- | --- |
|   SetObjectField()  |   jobject  |
|  SetBooleanField()   |  jboolean   |
|    SetByteField() |  jbyte   |
|  SetCharField()   |   jchar  |
|  SetShortField()   |  jshort   |
| SetIntField()    |  jint   |
|   SetLongField()  |   jlong  |
|  SetFloatField()   | jfloat    |
|  SetDoubleField()   |   jdouble  |

**LINKAGE:**

> Indices in the JNIEnv interface function table.

JNIEnv接口函数表中的索引。

**Set\<type>Field Family of Accessor Routines**
|  Set<type>Field Routine  | 	Index  |
|  ---  |   ---  |
| SetObjectField()	|104 |
|SetBooleanField()	|105|
|SetByteField()	|106|
|SetCharField()|	107|
|SetShortField()|	108|
|SetIntField()|	109|
|SetLongField()	|110|
|SetFloatField()	|111|
|SetDoubleField()|	112|

**PARAMETERS:**
*env*: the JNI interface pointer.
*obj*: a Java object (must not be NULL).
*fieldID*: a valid field ID.
*value*: the new value of the field.

## <a name="9">Calling Instance Methods</a>
### <a name="91">GetMethodID</a>

	jmethodID GetMethodID(JNIEnv *env, jclass clazz,const char *name, const char *sig);

> Returns the method ID for an instance (nonstatic) method of a class or interface. The method may be defined in one of the clazz’s superclasses and inherited by clazz. The method is determined by its name and signature.

返回类或接口的实例（非静态）方法的方法ID。 该方法可以在clazz的一个超类中定义，并由clazz继承。 该方法由其名称和签名确定。

> GetMethodID() causes an uninitialized class to be initialized.

GetMethodID（）导致初始化未初始化的类。

> To obtain the method ID of a constructor, supply \<init> as the method name and void (V) as the return type.

要获取构造函数的方法ID，请提供\ <init>作为方法名称，并将void（V）作为返回类型。

**LINKAGE:**
Index 33 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*clazz*: a Java class object.
*name*: the method name in a 0-terminated modified UTF-8 string.
*sig*: the method signature in 0-terminated modified UTF-8 string.

**RETURNS:**
Returns a method ID, or NULL if the specified method cannot be found.

**THROWS:**

*NoSuchMethodError*: if the specified method cannot be found.
*ExceptionInInitializerError*: if the class initializer fails due to an exception.
*OutOfMemoryError*: if the system runs out of memory.

### <a name="92">Call\<type>Method Routines, Call\<type>MethodA Routines, Call\<type>MethodV Routines</a>
	NativeType Call<type>Method(JNIEnv *env, jobject obj, jmethodID methodID, ...);

	NativeType Call<type>MethodA(JNIEnv *env, jobject obj, jmethodID methodID, const jvalue *args);

	NativeType Call<type>MethodV(JNIEnv *env, jobject obj, jmethodID methodID, va_list args);

> Methods from these three families of operations are used to call a Java instance method from a native method.They only differ in their mechanism for passing parameters to the methods that they call.

来自这三个操作系列的方法用于从本机方法调用Java实例方法。它们只是将参数传递给它们调用的方法的机制不同。

> These families of operations invoke an instance (nonstatic) method on a Java object, according to the specified method ID. The methodID argument must be obtained by calling GetMethodID().

这些操作系列根据指定的方法ID在Java对象上调用实例（非静态）方法。 必须通过调用GetMethodID（）获取methodID参数。

> When these functions are used to call private methods and constructors, the method ID must be derived from the real class of obj, not from one of its superclasses.

当这些函数用于调用私有方法和构造函数时，方法ID必须从obj的真实类派生，而不是从其超类之一派生。

#### Call\<type>Method Routines
> Programmers place all arguments that are to be passed to the method immediately following the methodID argument. The Call\<type>Method routine accepts these arguments and passes them to the Java method that the programmer wishes to invoke.

程序员将所有要传递给方法的参数紧跟在methodID参数之后。 Call \<type> Method例程接受这些参数并将它们传递给程序员希望调用的Java方法。

#### Call\<type>MethodA Routines
> Programmers place all arguments to the method in an args array of jvalues that immediately follows the methodID argument. The Call\<type>MethodA routine accepts the arguments in this array, and, in turn, passes them to the Java method that the programmer wishes to invoke.

程序员将方法的所有参数放在紧跟在methodID参数之后的jvalues的args数组中。 Call \<type> MethodA例程接受此数组中的参数，然后将它们传递给程序员希望调用的Java方法。

#### Call\ <type>MethodV Routines
> Programmers place all arguments to the method in an args argument of type va_list that immediately follows the methodID argument. The Call<type>MethodV routine accepts the arguments, and, in turn, passes them to the Java method that the programmer wishes to invoke.

程序员将方法的所有参数放在紧跟在methodID参数之后的类型为va_list的args参数中。 Call \<type> MethodV例程接受参数，然后将它们传递给程序员希望调用的Java方法。

> The following table describes each of the method calling routines according to their result type. You should replace type in Call\<type>Method with the Java type of the method you are calling (or use one of the actual method calling routine names from the table) and replace NativeType with the corresponding native type for that routine.

下表根据结果类型描述了每个方法调用例程。 您应该将Call \<type> Method中的类型替换为您正在调用的方法的Java类型（或使用表中实际的方法调用例程名称之一），并将NativeType替换为该例程的相应本机类型。

**Instance Method Calling Routines**


|  Call\<type>Method Routine Name  |  Native Type  |
|  ---  |  ---  |
|CallVoidMethod() CallVoidMethodA() CallVoidMethodV()|	void|
|CallObjectMethod() CallObjectMethodA() CallObjectMethodV()|	jobject|
|CallBooleanMethod() CallBooleanMethodA() CallBooleanMethodV()|	jboolean|
|CallByteMethod() CallByteMethodA() CallByteMethodV()|	jbyte|
|CallCharMethod() CallCharMethodA() CallCharMethodV()|	jchar|
|CallShortMethod() CallShortMethodA() CallShortMethodV()|	jshort|
|CallIntMethod() CallIntMethodA() CallIntMethodV()	|jint|
|CallLongMethod() CallLongMethodA() CallLongMethodV()|	jlong|
|CallFloatMethod() CallFloatMethodA() CallFloatMethodV()	|jfloat|
|CallDoubleMethod() CallDoubleMethodA() CallDoubleMethodV()	|jdouble|

**LINKAGE:**
> Indices in the JNIEnv interface function table:

JNIEnv接口函数表中的索引：

**Instance Method Calling Routines**
|  Call\<type>Method Routine Name  |  Index  |
|  ---  |  ---  |
|CallVoidMethod() CallVoidMethodA() CallVoidMethodV()	 |61 63 62 |
| CallObjectMethod() CallObjectMethodA() CallObjectMethodV()	 |34 36 35 |
|CallBooleanMethod() CallBooleanMethodA() CallBooleanMethodV()	 |37 39 38 |
|CallByteMethod() CallByteMethodA() CallByteMethodV() |	40 42 41 |
|CallCharMethod() CallCharMethodA() CallCharMethodV()	| 43 45 44|
|CallShortMethod() CallShortMethodA() CallShortMethodV()	|46 48 47|
|CallIntMethod() CallIntMethodA() CallIntMethodV()	|49 51 50|
|CallLongMethod() CallLongMethodA() CallLongMethodV()	| 52 54 53|
|CallFloatMethod() CallFloatMethodA() CallFloatMethodV()	| 55 57 56|
|CallDoubleMethod() CallDoubleMethodA() CallDoubleMethodV()	| 58 60 59|

**PARAMETERS:**
*env*: the JNI interface pointer.
*obj*: a Java object.
*methodID*: a method ID.

**Additional Parameter for Call\<type>Method Routines:**
> arguments to the Java method.

Java方法的参数。

**Additional Parameter for Call\<type>MethodA Routines:**
> args: an array of arguments.

args：一组参数。

**Additional Parameter for Call<type>MethodV Routines:**
> args: a va_list of arguments.

args：一个参数的va_list。

**RETURNS:**
Returns the result of calling the Java method.

**THROWS:**
Exceptions raised during the execution of the Java method.

### <a name="93">CallNonvirtual\<type>Method Routines, CallNonvirtual\<type>MethodA Routines, CallNonvirtual\<type>MethodV Routines</a>
	NativeType CallNonvirtual\<type>Method(JNIEnv *env, jobject obj, jclass clazz, jmethodID methodID, ...);

	NativeType CallNonvirtual\<type>MethodA(JNIEnv *env, jobject obj, jclass clazz, jmethodID methodID, const jvalue *args);

	NativeType CallNonvirtual\<type>MethodV(JNIEnv *env, jobject obj, jclass clazz, jmethodID methodID, va_list args);

> These families of operations invoke an instance (nonstatic) method on a Java object, according to the specified class and method ID. The methodID argument must be obtained by calling GetMethodID() on the class clazz.

这些操作系列根据指定的类和方法ID在Java对象上调用实例（非静态）方法。 必须通过在类clazz上调用GetMethodID（）来获取methodID参数。

> The CallNonvirtual\<type>Method families of routines and the Call\<type>Method families of routines are different. Call\<type>Method routines invoke the method based on the class of the object, while CallNonvirtual\<type>Method routines invoke the method based on the class, designated by the clazz parameter, from which the method ID is obtained. The method ID must be obtained from the real class of the object or from one of its superclasses.

CallNonvirtual \<type>方法系列例程和Call \<type>方法系列例程是不同的。 调用\<type>方法例程根据对象的类调用方法，而CallNonvirtual \<type>方法例程根据clazz参数指定的类调用方法，从中获取方法ID。 方法ID必须从对象的真实类或其超类之一获得。

**CallNonvirtual\<type>Method Routines**
> Programmers place all arguments that are to be passed to the method immediately following the methodID argument. The CallNonvirtual<type>Method routine accepts these arguments and passes them to the Java method that the programmer wishes to invoke.

程序员将所有要传递给方法的参数紧跟在methodID参数之后。 CallNonvirtual <type> Method例程接受这些参数并将它们传递给程序员希望调用的Java方法。

**CallNonvirtual\<type>MethodA Routines**
> Programmers place all arguments to the method in an args array of jvalues that immediately follows the methodID argument. The CallNonvirtual\<type>MethodA routine accepts the arguments in this array, and, in turn, passes them to the Java method that the programmer wishes to invoke.

程序员将方法的所有参数放在紧跟在methodID参数之后的jvalues的args数组中。 CallNonvirtual \<type> MethodA例程接受此数组中的参数，然后将它们传递给程序员希望调用的Java方法。

**CallNonvirtual\<type>MethodV Routines**
> Programmers place all arguments to the method in an args argument of type va_list that immediately follows the methodID argument. The CallNonvirtualMethodV routine accepts the arguments, and, in turn, passes them to the Java method that the programmer wishes to invoke.

程序员将方法的所有参数放在紧跟在methodID参数之后的类型为va_list的args参数中。 CallNonvirtualMethodV例程接受参数，然后将它们传递给程序员希望调用的Java方法。

> The following table describes each of the method calling routines according to their result type. You should replace type in CallNonvirtual\<type>Method with the Java type of the method, or use one of the actual method calling routine names from the table, and replace NativeType with the corresponding native type for that routine.

下表根据结果类型描述了每个方法调用例程。 您应该将CallNonvirtual \<type> Method中的type替换为方法的Java类型，或者使用表中实际的方法调用例程名称之一，并将NativeType替换为该例程的相应本机类型。

**CallNonvirtual\<type>Method Routines**
|  CallNonvirtual\<type>Method Routine Name  |  	Native Type  |
|  ---  |  ---  |
|CallNonvirtualVoidMethod() CallNonvirtualVoidMethodA() CallNonvirtualVoidMethodV()	|void|
|CallNonvirtualObjectMethod() CallNonvirtualObjectMethodA() CallNonvirtualObjectMethodV()	|jobject|
|CallNonvirtualBooleanMethod() CallNonvirtualBooleanMethodA() CallNonvirtualBooleanMethodV()|jboolean|
|CallNonvirtualByteMethod() CallNonvirtualByteMethodA() CallNonvirtualByteMethodV()|	jbyte|
|CallNonvirtualCharMethod() CallNonvirtualCharMethodA() CallNonvirtualCharMethodV()|	jchar|
|CallNonvirtualShortMethod() CallNonvirtualShortMethodA() CallNonvirtualShortMethodV()|	jshort|
|CallNonvirtualIntMethod() CallNonvirtualIntMethodA() CallNonvirtualIntMethodV()|	jint|
|CallNonvirtualLongMethod() CallNonvirtualLongMethodA() CallNonvirtualLongMethodV()|	jlong|
|CallNonvirtualFloatMethod() CallNonvirtualFloatMethodA() CallNonvirtualFloatMethodV()|	jfloat|
|CallNonvirtualDoubleMethod() CallNonvirtualDoubleMethodA() CallNonvirtualDoubleMethodV()|	jdouble|

**LINKAGE:**

> Indices in the JNIEnv interface function table.

JNIEnv接口函数表中的索引。

**CallNonvirtual\<type>Method Routines**
|  CallNonvirtual<type>Method Routine Name  |  Index  |
|  ---  |  ---  |
|CallNonvirtualVoidMethod() CallNonvirtualVoidMethodA() CallNonvirtualVoidMethodV()|	91 93 92|
|CallNonvirtualObjectMethod() CallNonvirtualObjectMethodA() CallNonvirtualObjectMethodV()|	64 66 65|
|CallNonvirtualBooleanMethod() CallNonvirtualBooleanMethodA() CallNonvirtualBooleanMethodV()|	67 69 68|
|CallNonvirtualByteMethod() CallNonvirtualByteMethodA() CallNonvirtualByteMethodV()|	70 72 71|
|CallNonvirtualCharMethod() CallNonvirtualCharMethodA() CallNonvirtualCharMethodV()	|73 75 74|
|CallNonvirtualShortMethod() CallNonvirtualShortMethodA() CallNonvirtualShortMethodV()	76 78 77|
|CallNonvirtualIntMethod() CallNonvirtualIntMethodA() CallNonvirtualIntMethodV()|	79 81 80|
|CallNonvirtualLongMethod() CallNonvirtualLongMethodA() CallNonvirtualLongMethodV()|	82 84 83|
|CallNonvirtualFloatMethod() CallNonvirtualFloatMethodA() CallNonvirtualFloatMethodV()|	85 87 86|
|CallNonvirtualDoubleMethod() CallNonvirtualDoubleMethodA() CallNonvirtualDoubleMethodV()	|88 90 89|

**PARAMETERS:**

*env*: the JNI interface pointer.
*clazz*: a Java class.
*obj*: a Java object.
*methodID*: a method ID.

**Additional Parameter for CallNonvirtual\<type>Method Routines:**
arguments to the Java method.

**Additional Parameter for CallNonvirtual\<type>MethodA Routines:**
args: an array of arguments.

**Additional Parameter for CallNonvirtual\<type>MethodV Routines:**
args: a va_list of arguments.

**RETURNS:**
Returns the result of calling the Java method.

**THROWS:**
Exceptions raised during the execution of the Java method.

## <a name="10">Accessing Static Fields</a>
### <a name="101"> GetStaticFieldID</a>
	
	jfieldID GetStaticFieldID(JNIEnv *env, jclass clazz, const char *name, const char *sig);

> Returns the field ID for a static field of a class. The field is specified by its name and signature. The GetStatic\<type>Field and SetStatic\<type>Field families of accessor functions use field IDs to retrieve static fields.

返回类的静态字段的字段ID。 该字段由其名称和签名指定。 GetStatic \<type> Field和SetStatic \<type>字段系列的访问者函数使用字段ID来检索静态字段。

> GetStaticFieldID() causes an uninitialized class to be initialized.

GetStaticFieldID（）导致初始化未初始化的类。

**LINKAGE:**
Index 144 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*clazz*: a Java class object.
*name*: the static field name in a 0-terminated modified UTF-8 string.
*sig*: the field signature in a 0-terminated modified UTF-8 string.

**RETURNS:**
Returns a field ID, or NULL if the specified static field cannot be found.

**THROWS:**

*NoSuchFieldError*: if the specified static field cannot be found.
*ExceptionInInitializerError*: if the class initializer fails due to an exception.
*OutOfMemoryError*: if the system runs out of memory.

### <a name="102">GetStatic<type>Field Routines</a>

	NativeType GetStatic<type>Field(JNIEnv *env, jclass clazz, jfieldID fieldID);

> This family of accessor routines returns the value of a static field of an object. The field to access is specified by a field ID, which is obtained by calling GetStaticFieldID().

这个访问器例程系列返回对象的静态字段的值。 要访问的字段由字段ID指定，该字段ID是通过调用GetStaticFieldID（）获得的。

> The following table describes the family of get routine names and result types. You should replace type in GetStatic\<type>Field with the Java type of the field, or one of the actual static field accessor routine names from the table, and replace NativeType with the corresponding native type for that routine.

下表描述了get例程名称和结果类型的系列。 您应该使用字段的Java类型或表中的一个实际静态字段访问者例程名称替换GetStatic \<type>字段中的类型，并将NativeType替换为该例程的相应本机类型。

**GetStatic\<type>Field Family of Accessor Routines**

|  GetStatic\<type>Field Routine Name  |  Native Type  |
|  ---  |  ---  |
|GetStaticObjectField()|	jobject|
|GetStaticBooleanField()|	jboolean|
|GetStaticByteField()|	jbyte|
|GetStaticCharField()|	jchar|
|GetStaticShortField()	|jshort|
|GetStaticIntField()|	jint|
|GetStaticLongField()|	jlong|
|GetStaticFloatField()|	jfloat|
|GetStaticDoubleField()	|jdouble|

**LINKAGE:**

> Indices in the JNIEnv interface function table.

JNIEnv接口函数表中的索引。


**GetStatic\<type>Field Family of Accessor Routines**

|  GetStatic\<type>Field Routine Name  |  Index  |
|  ---  |  ---  |
|GetStaticObjectField()	|145|
|GetStaticBooleanField()	|146|
|GetStaticByteField()	|147|
|GetStaticCharField()	|148|
|GetStaticShortField()|	149|
|GetStaticIntField()	|150|
|GetStaticLongField()|	151|
|GetStaticFloatField()	|152|
|GetStaticDoubleField()	|153|

**PARAMETERS:**

*env*: the JNI interface pointer.
*clazz*: a Java class object.
*fieldID*: a static field ID.

**RETURNS:**

Returns the content of the static field.

### <a name="103">SetStatic<type>Field Routines</a>
	void SetStatic\<type>Field(JNIEnv *env, jclass clazz, jfieldID fieldID, NativeType value);

> This family of accessor routines sets the value of a static field of an object. The field to access is specified by a field ID, which is obtained by calling GetStaticFieldID().

这个访问器例程系列设置对象的静态字段的值。 要访问的字段由字段ID指定，该字段ID是通过调用GetStaticFieldID（）获得的。

> The following table describes the set routine name and value types. You should replace type in SetStatic\<type>Field with the Java type of the field, or one of the actual set static field routine names from the table, and replace NativeType with the corresponding native type for that routine.

下表描述了设置例程名称和值类型。 您应该将SetStatic \<type> Field中的type替换为该字段的Java类型，或者从表中替换一个实际的set static field routine name，并将NativeType替换为该例程的相应本机类型。

**SetStatic\<type>Field Family of Accessor Routines**
|  SetStatic\<type>Field Routine Name  |  NativeType  |
|  ---  |  ---  |
|SetStaticObjectField()	|jobject|
|SetStaticBooleanField()|	jboolean|
|SetStaticByteField()	|jbyte|
|SetStaticCharField()|	jchar|
|SetStaticShortField()|	jshort|
|SetStaticIntField()	|jint|
|SetStaticLongField()|	jlong|
|SetStaticFloatField()|	jfloat|
|SetStaticDoubleField()|	jdouble|
**LINKAGE:**
Indices in the JNIEnv interface function table.

**SetStatic\<type>Field Family of Accessor Routines**
|  SetStatic\<type>Field Routine Name  |  Index  |
|  ---  |  ---  |
|SetStaticObjectField()|	154|
|SetStaticBooleanField()	|155|
|SetStaticByteField()	|156|
|SetStaticCharField()|	157|
|SetStaticShortField()	|158|
|SetStaticIntField()|	159|
|SetStaticLongField()|	160|
|SetStaticFloatField()	|161|
|SetStaticDoubleField()|	162|

**PARAMETERS:**

*env*: the JNI interface pointer.
*clazz*: a Java class object.
*fieldID*: a static field ID.
*value*: the new value of the field.

## <a name="11"> Calling Static Methods</a>
### <a name="111">GetStaticMethodID</a>

	jmethodID GetStaticMethodID(JNIEnv *env, jclass clazz, const char *name, const char *sig);

> Returns the method ID for a static method of a class. The method is specified by its name and signature.

返回类的静态方法的方法ID。 该方法由其名称和签名指定。

> GetStaticMethodID() causes an uninitialized class to be initialized.

GetStaticMethodID（）导致初始化未初始化的类。

**LINKAGE:**

Index 113 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*clazz*: a Java class object.
*name*: the static method name in a 0-terminated modified UTF-8 string.
*sig*: the method signature in a 0-terminated modified UTF-8 string.

**RETURNS:**
Returns a method ID, or NULL if the operation fails.

**THROWS:**

*NoSuchMethodError*: if the specified static method cannot be found.
*ExceptionInInitializerError*: if the class initializer fails due to an exception.
*OutOfMemoryError*: if the system runs out of memory.



CallStatic\<type>Method Routines, CallStatic\<type>MethodA Routines, CallStatic\<type>MethodV Routines</a>

	NativeType CallStatic\<type>Method(JNIEnv *env, jclass clazz, jmethodID methodID, ...);

	NativeType CallStatic\<type>MethodA(JNIEnv *env, jclass clazz, jmethodID methodID, jvalue *args);

	NativeType CallStatic\<type>MethodV(JNIEnv *env, jclass clazz, jmethodID methodID, va_list args);

> This family of operations invokes a static method on a Java object, according to the specified method ID. The methodID argument must be obtained by calling GetStaticMethodID().

此系列操作根据指定的方法ID在Java对象上调用静态方法。 必须通过调用GetStaticMethodID（）获取methodID参数。

> The method ID must be derived from clazz, not from one of its superclasses.

方法ID必须来自clazz，而不是来自其中一个超类。

**CallStatic<type>Method Routines**
> Programmers should place all arguments that are to be passed to the method immediately following the methodID argument. The CallStatic\<type>Method routine accepts these arguments and passes them to the Java method that the programmer wishes to invoke.

程序员应该将所有要传递给方法的参数紧跟在methodID参数之后。 CallStatic \<type> Method例程接受这些参数并将它们传递给程序员希望调用的Java方法。

**CallStatic<type>MethodA Routines**
> Programmers should place all arguments to the method in an args array of jvalues that immediately follows the methodID argument. The CallStaticMethodA routine accepts the arguments in this array, and, in turn, passes them to the Java method that the programmer wishes to invoke.

程序员应该将方法的所有参数放在紧跟在methodID参数之后的jvalues的args数组中。 CallStaticMethodA例程接受此数组中的参数，然后将它们传递给程序员希望调用的Java方法。

**CallStatic<type>MethodV Routines**
> Programmers should place all arguments to the method in an args argument of type va_list that immediately follows the methodID argument. The CallStaticMethodV routine accepts the arguments, and, in turn, passes them to the Java method that the programmer wishes to invoke.

程序员应该将方法的所有参数放在紧跟在methodID参数之后的类型为va_list的args参数中。 CallStaticMethodV例程接受参数，然后将它们传递给程序员希望调用的Java方法。

> The following table describes each of the method calling routines according to their result types. You should replace type in CallStatic\<type>Method with the Java type of the method, or one of the actual method calling routine names from the table, and replace NativeType with the corresponding native type for that routine.

下表根据结果类型描述了每个方法调用例程。 您应该将CallStatic \ <type> Method中的type替换为方法的Java类型，或者从表中替换实际的方法调用例程名称，并将NativeType替换为该例程的相应本机类型。

**CallStatic\<type>Method Calling Routines**
|  CallStatic\<type>Method Routine Name  |  Native Type  | 
|  ---  |   ---  |
|CallStaticVoidMethod() CallStaticVoidMethodA() CallStaticVoidMethodV()	|void|
|CallStaticObjectMethod() CallStaticObjectMethodA() CallStaticObjectMethodV()|	jobject|
|CallStaticBooleanMethod() CallStaticBooleanMethodA() CallStaticBooleanMethodV()	|jboolean|
|CallStaticByteMethod() CallStaticByteMethodA() CallStaticByteMethodV()	|jbyte|
|CallStaticCharMethod() CallStaticCharMethodA() CallStaticCharMethodV()|	jchar|
|CallStaticShortMethod() CallStaticShortMethodA() CallStaticShortMethodV()|	jshort|
|CallStaticIntMethod() CallStaticIntMethodA() CallStaticIntMethodV()	|jint|
|CallStaticLongMethod() CallStaticLongMethodA() CallStaticLongMethodV()|	jlong|
|CallStaticFloatMethod() CallStaticFloatMethodA() CallStaticFloatMethodV()|	jfloat|
|CallStaticDoubleMethod() CallStaticDoubleMethodA() CallStaticDoubleMethodV()|	jdouble|

**LINKAGE:**

> Indices in the JNIEnv interface function table.

JNIEnv接口函数表中的索引。

**CallStatic\<type>Method Calling Routines**
|  CallStatic\<type>Method Routine Name  |  Index  |
|  ---  |   ---  |
|CallStaticVoidMethod() CallStaticVoidMethodA() CallStaticVoidMethodV()	|141 143 142|
|CallStaticObjectMethod() CallStaticObjectMethodA() CallStaticObjectMethodV()|	114 116 115|
|CallStaticBooleanMethod() CallStaticBooleanMethodA() CallStaticBooleanMethodV()	|117 119 118|
|CallStaticByteMethod() CallStaticByteMethodA() CallStaticByteMethodV()	|120 122 121|
|CallStaticCharMethod() CallStaticCharMethodA() CallStaticCharMethodV()|	123 125 124|
|CallStaticShortMethod() CallStaticShortMethodA() CallStaticShortMethodV()	|126 128 127|
|CallStaticIntMethod() CallStaticIntMethodA() CallStaticIntMethodV()	|129 131 130|
|CallStaticLongMethod() CallStaticLongMethodA() CallStaticLongMethodV()|132 134 133|
|CallStaticFloatMethod() CallStaticFloatMethodA() CallStaticFloatMethodV()|	135 137 136|
|CallStaticDoubleMethod() CallStaticDoubleMethodA() CallStaticDoubleMethodV()	|138 140 139|

**PARAMETERS:**

*env*: the JNI interface pointer.
*clazz*: a Java class object.
*methodID*: a static method ID.

**Additional Parameter for CallStatic\<type>Method Routines:**
arguments to the static method.

**Additional Parameter for CallStatic\<type>MethodA Routines:**
args: an array of arguments.

**Additional Parameter for CallStatic\<type>MethodV Routines:**
args: a va_list of arguments.

**RETURNS:**
Returns the result of calling the static Java method.

**THROWS:**
Exceptions raised during the execution of the Java method.

## <a name="12"> String Operations</a>
### <a name="121">NewString</a>
	jstring NewString(JNIEnv *env, const jchar *unicodeChars, jsize len);

> Constructs a new java.lang.String object from an array of Unicode characters.

从Unicode字符数组构造一个新的java.lang.String对象.

**LINKAGE:**

Index 163 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*unicodeChars*: pointer to a Unicode string.
*len*: length of the Unicode string.

**RETURNS:**
Returns a Java string object, or NULL if the string cannot be constructed.

**THROWS:**
OutOfMemoryError: if the system runs out of memory.

### <a name="122">GetStringLength</a>

	jsize GetStringLength(JNIEnv *env, jstring string);

> Returns the length (the count of Unicode characters) of a Java string.

返回Java字符串的长度（Unicode字符数）。

**LINKAGE:**

Index 164 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*string*: a Java string object.

**RETURNS:**
Returns the length of the Java string.

### <a name="123">GetStringChars</a>
	const jchar * GetStringChars(JNIEnv *env, jstring string, jboolean *isCopy);

> Returns a pointer to the array of Unicode characters of the string. This pointer is valid until ReleaseStringChars() is called.

返回指向字符串的Unicode字符数组的指针。 在调用ReleaseStringChars（）之前，此指针有效。

> If isCopy is not NULL, then *isCopy is set to JNI_TRUE if a copy is made; or it is set to JNI_FALSE if no copy is made.

如果isCopy不为NULL，则如果进行复制，则* isCopy设置为JNI_TRUE; 如果没有复制，则设置为JNI_FALSE。

**LINKAGE:**

Index 165 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*string*: a Java string object.
*isCopy*: a pointer to a boolean.

**RETURNS:**
Returns a pointer to a Unicode string, or NULL if the operation fails.

### <a name="124">ReleaseStringChars</a>

	void ReleaseStringChars(JNIEnv *env, jstring string, const jchar *chars);

> Informs the VM that the native code no longer needs access to chars. The chars argument is a pointer obtained from string using GetStringChars().

通知VM本机代码不再需要访问字符。 chars参数是使用GetStringChars（）从字符串获取的指针。

**LINKAGE:**

Index 166 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*string*: a Java string object.
*chars*: a pointer to a Unicode string.

### <a name="125">NewStringUTF</a>
	jstring NewStringUTF(JNIEnv *env, const char *bytes);

> Constructs a new java.lang.String object from an array of characters in modified UTF-8 encoding.

从修改后的UTF-8编码中的字符数组构造一个新的java.lang.String对象。

**LINKAGE:**

Index 167 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*bytes*: the pointer to a modified UTF-8 string.

**RETURNS:**
Returns a Java string object, or NULL if the string cannot be constructed.

**THROWS:**
OutOfMemoryError: if the system runs out of memory.

### <a name="126">GetStringUTFLength</a>

	jsize GetStringUTFLength(JNIEnv *env, jstring string);

> Returns the length in bytes of the modified UTF-8 representation of a string.

返回字符串的修改后的UTF-8表示的字节长度。

**LINKAGE:**

Index 168 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*string*: a Java string object.

**RETURNS:**
Returns the UTF-8 length of the string.

### <a name="127">GetStringUTFChars</a>

	const char * GetStringUTFChars(JNIEnv *env, jstring string, jboolean *isCopy);

> Returns a pointer to an array of bytes representing the string in modified UTF-8 encoding. This array is valid until it is released by ReleaseStringUTFChars().

返回指向字节数组的指针，该字节数组表示修改后的UTF-8编码中的字符串。 此数组在ReleaseStringUTFChars（）释放之前有效。

> If isCopy is not NULL, then *isCopy is set to JNI_TRUE if a copy is made; or it is set to JNI_FALSE if no copy is made.

如果isCopy不为NULL，则如果进行复制，则* isCopy设置为JNI_TRUE; 如果没有复制，则设置为JNI_FALSE。

**LINKAGE:**

Index 169 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*string*: a Java string object.
*isCopy*: a pointer to a boolean.

**RETURNS:**

Returns a pointer to a modified UTF-8 string, or NULL if the operation fails.

### <a name="128">ReleaseStringUTFChars</a>

	void ReleaseStringUTFChars(JNIEnv *env, jstring string, const char *utf);

> Informs the VM that the native code no longer needs access to utf. The utf argument is a pointer derived from string using GetStringUTFChars().

通知VM本机代码不再需要访问utf。 utf参数是使用GetStringUTFChars（）从字符串派生的指针。

**LINKAGE:**

Index 170 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*string*: a Java string object.
*utf*: a pointer to a modified UTF-8 string.

> **Note**: In JDK/JRE 1.1, programmers can get primitive array elements in a user-supplied buffer. As of JDK/JRE 1.2 additional set of functions are provided allowing native code to obtain characters in Unicode (UTF-16) or modified UTF-8 encoding in a user-supplied buffer. See the functions below.

在JDK / JRE 1.1中，程序员可以在用户提供的缓冲区中获取原始数组元素。 从JDK / JRE 1.2开始，提供了一组额外的功能，允许本机代码在用户提供的缓冲区中获取Unicode（UTF-16）或修改的UTF-8编码的字符。 请参阅以下功能。

### <a name="129">GetStringRegion</a>

	void GetStringRegion(JNIEnv *env, jstring str, jsize start, jsize len, jchar *buf);

> Copies len number of Unicode characters beginning at offset start to the given buffer buf.

将从offset start开始的len个Unicode字符数复制到给定的缓冲区buf。

> Throws StringIndexOutOfBoundsException on index overflow.

索引溢出时抛出StringIndexOutOfBoundsException。

**LINKAGE:**

Index 220 in the JNIEnv interface function table.

**SINCE:**

JDK/JRE 1.2

### <a name="1210">GetStringUTFRegion</a>

	void GetStringUTFRegion(JNIEnv *env, jstring str, jsize start, jsize len, char *buf);

> Translates len number of Unicode characters beginning at offset start into modified UTF-8 encoding and place the result in the given buffer buf.

将从offset start开始的len个Unicode字符转换为修改的UTF-8编码，并将结果放在给定的缓冲区buf中。

> Throws StringIndexOutOfBoundsException on index overflow.

索引溢出时抛出StringIndexOutOfBoundsException。

**LINKAGE:**

Index 221 in the JNIEnv interface function table.

**SINCE:**

JDK/JRE 1.2

### <a name="1211">GetStringCritical, ReleaseStringCritical</a>
	
	const jchar * GetStringCritical(JNIEnv *env, jstring string, jboolean *isCopy);
	void ReleaseStringCritical(JNIEnv *env, jstring string, const jchar *carray);

> The semantics of these two functions are similar to the existing Get/ReleaseStringChars functions. If possible, the VM returns a pointer to string elements; otherwise, a copy is made. However, there are significant restrictions on how these functions can be used. In a code segment enclosed by Get/ReleaseStringCritical calls, the native code must not issue arbitrary JNI calls, or cause the current thread to block.

这两个函数的语义类似于现有的Get / ReleaseStringChars函数。 如果可能，VM返回指向字符串元素的指针; 否则，制作副本。 但是，如何使用这些功能存在重大限制。 在Get / ReleaseStringCritical调用包含的代码段中，本机代码不得发出任意JNI调用，或导致当前线程阻塞。

> The restrictions on Get/ReleaseStringCritical are similar to those on Get/ReleasePrimitiveArrayCritical.

Get / ReleaseStringCritical的限制类似于Get / ReleasePrimitiveArrayCritical上的限制。

**LINKAGE (GetStringCritical):**

Index 224 in the JNIEnv interface function table.

**LINKAGE (ReleaseStingCritical):**

Index 225 in the JNIEnv interface function table.

**SINCE:**

JDK/JRE 1.2

## <a name="13"> Array Operations</a>
### <a name="131"> GetArrayLength</a>

	jsize GetArrayLength(JNIEnv *env, jarray array);

> Returns the number of elements in the array.

返回数组中的元素数。

**LINKAGE:**

Index 171 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*array*: a Java array object.

**RETURNS:**

Returns the length of the array.

### <a name="132">NewObjectArray</a>

	jobjectArray NewObjectArray(JNIEnv *env, jsize length, jclass elementClass, jobject initialElement);

> Constructs a new array holding objects in class elementClass. All elements are initially set to initialElement.

在类elementClass中构造一个包含对象的新数组。 所有元素最初都设置为initialElement。

**LINKAGE:**

Index 172 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*length*: array size.
*elementClass*: array element class.
*initialElement*: initialization value.

**RETURNS:**
Returns a Java array object, or NULL if the array cannot be constructed.

**THROWS:**
OutOfMemoryError: if the system runs out of memory.

### <a name="133">GetObjectArrayElement</a>

	jobject GetObjectArrayElement(JNIEnv *env, jobjectArray array, jsize index);

> Returns an element of an Object array.

返回Object数组的元素。

**LINKAGE:**

Index 173 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*array*: a Java array.
*index*: array index.

**RETURNS:**
Returns a Java object.

**THROWS:**
ArrayIndexOutOfBoundsException: if index does not specify a valid index in the array.

### <a name="134">SetObjectArrayElement</a>

	void SetObjectArrayElement(JNIEnv *env, jobjectArray array, jsize index, jobject value);

> Sets an element of an Object array.

设置Object数组的元素。

**LINKAGE:**

Index 174 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*array*: a Java array.
*index*: array index.
*value*: the new value.

**THROWS:**

ArrayIndexOutOfBoundsException: if index does not specify a valid index in the array.
ArrayStoreException: if the class of value is not a subclass of the element class of the array.

### <a name="135">New\<PrimitiveType>Array Routines</a>

	ArrayType New\<PrimitiveType>Array(JNIEnv *env, jsize length);

> A family of operations used to construct a new primitive array object. The following table describes the specific primitive array constructors. You should replace New\<PrimitiveType>Array with one of the actual primitive array constructor routine names from this table, and replace ArrayType with the corresponding array type for that routine.

用于构造新原始数组对象的一系列操作。 下表描述了特定的基本数组构造函数。 您应该使用此表中的一个实际原始数组构造函数例程名称替换New \<PrimitiveType> Array，并将ArrayType替换为该例程的相应数组类型。

**New\<PrimitiveType>Array Family of Array Constructors**
|  New\<PrimitiveType>Array Routines	Array  |  Type  |
|  ---  |  ---  |
|NewBooleanArray()|	jbooleanArray|
|NewByteArray()|	jbyteArray|
|NewCharArray()|	jcharArray|
|NewShortArray()|	jshortArray|
|NewIntArray()|	jintArray|
|NewLongArray()|	jlongArray|
|NewFloatArray()|	jfloatArray|
|NewDoubleArray()|	jdoubleArray|

**LINKAGE:**

> Indices in the JNIEnv interface function table.

JNIEnv接口函数表中的索引。

**New\<PrimitiveType>Array Family of Array Constructors**
|  New<PrimitiveType>Array Routines  |  Index  |
|  ---  |  ---  |
|NewBooleanArray()	|175|
|NewByteArray()|	176|
|NewCharArray()|	177|
|NewShortArray()|	178|
|NewIntArray()|	179|
|NewLongArray()	|180|
|NewFloatArray()	|181|
|NewDoubleArray()	|182|

**PARAMETERS:**

*env*: the JNI interface pointer.
*length*: the array length.

**RETURNS:**
Returns a Java array, or NULL if the array cannot be constructed.

### <a name="136">Get\<PrimitiveType>ArrayElements Routines</a>

	NativeType *Get<PrimitiveType>ArrayElements(JNIEnv *env, ArrayType array, jboolean *isCopy);

> A family of functions that returns the body of the primitive array. The result is valid until the corresponding Release\<PrimitiveType>ArrayElements() function is called. Since the returned array may be a copy of the Java array, changes made to the returned array will not necessarily be reflected in the original array until Release\<PrimitiveType>ArrayElements() is called.

返回基本数组主体的一系列函数。 结果有效，直到调用相应的Release \<PrimitiveType> ArrayElements（）函数。 由于返回的数组可能是Java数组的副本，因此在调用Release \<PrimitiveType> ArrayElements（）之前，对返回数组所做的更改不一定会反映在原始数组中。

> If isCopy is not NULL, then *isCopy is set to JNI_TRUE if a copy is made; or it is set to JNI_FALSE if no copy is made.

如果isCopy不为NULL，则如果进行复制，则* isCopy设置为JNI_TRUE; 如果没有复制，则设置为JNI_FALSE。

> The following table describes the specific primitive array element accessors. You should make the following substitutions:
> - Replace Get\<PrimitiveType>ArrayElements with one of the actual primitive element accessor routine names from the following table.
> - Replace ArrayType with the corresponding array type.
> - Replace NativeType with the corresponding native type for that routine.

下表描述了特定的原始数组元素访问器。 您应该进行以下替换：
- 将Get \<PrimitiveType> ArrayElements替换为下表中的一个实际原始元素访问器例程名称。
- 将ArrayType替换为相应的数组类型。
- 将NativeType替换为该例程的相应本机类型。


> Regardless of how boolean arrays are represented in the Java VM, GetBooleanArrayElements() always returns a pointer to jbooleans, with each byte denoting an element (the unpacked representation). All arrays of other types are guaranteed to be contiguous in memory.

无论如何在Java VM中表示布尔数组，GetBooleanArrayElements（）始终返回指向jbooleans的指针，每个字节表示一个元素（解包表示）。 其他类型的所有数组都保证在内存中是连续的。

**Get\<PrimitiveType>ArrayElements Family of Accessor Routines**
|  Get\<PrimitiveType>ArrayElements Routines  |  Array Type  |  Native Type  |
|  ---  |  ---  |  ---  |
|GetBooleanArrayElements()|	jbooleanArray|	jboolean|
|GetByteArrayElements()	|jbyteArray|	jbyte|
|GetCharArrayElements()|	jcharArray	|jchar|
|GetShortArrayElements()|	jshortArray|	jshort|
|GetIntArrayElements()|	jintArray	|jint|
|GetLongArrayElements()|	jlongArray|	jlong|
|GetFloatArrayElements()	|jfloatArray|	jfloat|
|GetDoubleArrayElements()	|jdoubleArray|	jdouble|

**LINKAGE:**

Indices in the JNIEnv interface function table.

**Get\<PrimitiveType>ArrayElements Family of Accessor Routines**

|  Get<PrimitiveType>ArrayElements Routines  |  Index  |
|  ---  |  ---  |
|GetBooleanArrayElements()	|183|
|GetByteArrayElements()	|184|
|GetCharArrayElements()	|185|
|GetShortArrayElements()|	186|
|GetIntArrayElements()|	187|
|GetLongArrayElements()	|188|
|GetFloatArrayElements()|	189|
|GetDoubleArrayElements()	|190|

**PARAMETERS:**

*env*: the JNI interface pointer.
*array*: a Java string object.
*isCopy*: a pointer to a boolean.

**RETURNS:**

Returns a pointer to the array elements, or NULL if the operation fails.

### <a name="137">Release<PrimitiveType>ArrayElements Routines</a>
	void Release<PrimitiveType>ArrayElements(JNIEnv *env, ArrayType array, NativeType *elems, jint mode);

> A family of functions that informs the VM that the native code no longer needs access to elems. The elems argument is a pointer derived from array using the corresponding Get\<PrimitiveType>ArrayElements() function. If necessary, this function copies back all changes made to elems to the original array.

一系列函数，通知VM本机代码不再需要访问elems。 elems参数是使用相应的Get \<PrimitiveType> ArrayElements（）函数从数组派生的指针。 如有必要，此函数会将对elems所做的所有更改复制回原始数组。

> The mode argument provides information on how the array buffer should be released. mode has no effect if elems is not a copy of the elements in array. Otherwise, mode has the following impact, as shown in the following table:

mode参数提供有关如何释放数组缓冲区的信息。 如果elems不是数组中元素的副本，则mode无效。 否则，模式会产生以下影响，如下表所示：

**Primitive Array Release Modes**
|  mode  | 	actions  |
|  ---  |  ---  |
|0|	copy back the content and free the elems buffer|
|JNI_COMMIT|	copy back the content but do not free the elems buffer
|JNI_ABORT|	free the buffer without copying back the possible changes|

> In most cases, programmers pass “0” to the mode argument to ensure consistent behavior for both pinned and copied arrays. The other options give the programmer more control over memory management and should be used with extreme care.

在大多数情况下，程序员将“0”传递给mode参数，以确保固定和复制数组的一致行为。 其他选项使程序员可以更好地控制内存管理，并且应该非常谨慎地使用。

> The next table describes the specific routines that comprise the family of primitive array disposers. You should make the following substitutions:
> -  Replace Release\<PrimitiveType>ArrayElements with one of the actual primitive array disposer routine names from the following table.
> -  Replace ArrayType with the corresponding array type.
> -  Replace NativeType with the corresponding native type for that routine.


下表描述了构成原始数组处理器系列的特定例程。 您应该进行以下替换：
- 将Release \<PrimitiveType> ArrayElements替换为下表中的一个实际原始数组处理程序例程名称。
- 将ArrayType替换为相应的数组类型。
- 将NativeType替换为该例程的相应本机类型。

**Release\<PrimitiveType>ArrayElements Family of Array Routines**

|  Release\<PrimitiveType>ArrayElements Routines  |  Array Type  |  Native Type  |
|  ---  |  ---  |  ---  |
|ReleaseBooleanArrayElements()|	jbooleanArray	|jboolean|
|ReleaseByteArrayElements()|	jbyteArray|	jbyte|
|ReleaseCharArrayElements()|	jcharArray|	jchar|
|ReleaseShortArrayElements()|	jshortArray	|jshort|
|ReleaseIntArrayElements()|	jintArray	|jint|
|ReleaseLongArrayElements()|	jlongArray	|jlong|
|ReleaseFloatArrayElements()|	jfloatArray|	jfloat|
|ReleaseDoubleArrayElements()|	jdoubleArray|	jdouble|

**LINKAGE:**

Indices in the JNIEnv interface function table.

**Release\<PrimitiveType>ArrayElements Family of Array Routines**
|  Release<PrimitiveType>ArrayElements Routines  |  Index  |
|  ---  |  ---  |
|ReleaseBooleanArrayElements()|	191|
|ReleaseByteArrayElements()|	192|
|ReleaseCharArrayElements()|	193|
|ReleaseShortArrayElements()|	194|
|ReleaseIntArrayElements()	|195|
|ReleaseLongArrayElements()	|196|
|ReleaseFloatArrayElements()|	197|
|ReleaseDoubleArrayElements()|	198|

**PARAMETERS:**

*env*: the JNI interface pointer.
*array*: a Java array object.
*elems*: a pointer to array elements.
*mode*: the release mode.

### <a name="138">Get\<PrimitiveType>ArrayRegion Routines</a>

	void Get\<PrimitiveType>ArrayRegion(JNIEnv *env, ArrayType array, jsize start, jsize len, NativeType *buf);

> A family of functions that copies a region of a primitive array into a buffer.

一系列函数，用于将基本数组的区域复制到缓冲区中。

> The following table describes the specific primitive array element accessors. You should do the following substitutions:
> - Replace Get\<PrimitiveType>ArrayRegion with one of the actual primitive element accessor routine names from the following table.
> - Replace ArrayType with the corresponding array type.
> - Replace NativeType with the corresponding native type for that routine.


下表描述了特定的原始数组元素访问器。 你应该做以下替换：
- 将Get \<PrimitiveType> ArrayRegion替换为下表中的一个实际原始元素访问器例程名称。
- 将ArrayType替换为相应的数组类型。
- 将NativeType替换为该例程的相应本机类型。



**Get\<PrimitiveType>ArrayRegion Family of Array Accessor Routines**
|  Get\<PrimitiveType>ArrayRegion Routine  |  Array Type  |  Native Type  |
|  ---  |  ---  |  ---  |
|GetBooleanArrayRegion()|	jbooleanArray|	jboolean
|GetByteArrayRegion()|	jbyteArray|	jbyte|
|GetCharArrayRegion()|	jcharArray|	jchar|
|GetShortArrayRegion()|	jshortArray|	jhort|
|GetIntArrayRegion()|	jintArray|	jint|
|GetLongArrayRegion()|	jlongArray|	jlong|
|GetFloatArrayRegion()|	jfloatArray	|jloat|
|GetDoubleArrayRegion()|	jdoubleArray|	jdouble|
**LINKAGE:**
Indices in the JNIEnv interface function table.

**Get\<PrimitiveType>ArrayRegion Family of Array Accessor Routines**
|  Get\<PrimitiveType>ArrayRegion Routine  |  Index  |
|  ---  |  ---  |
|GetBooleanArrayRegion()|	199|
|GetByteArrayRegion()|	200|
|GetCharArrayRegion()|	201|
|GetShortArrayRegion()|	202|
|GetIntArrayRegion()|	203|
|GetLongArrayRegion()|	204|
|GetFloatArrayRegion()|	205|
|GetDoubleArrayRegion()|	206|

**PARAMETERS:**

*env*: the JNI interface pointer.
*array*: a Java array.
*start*: the starting index.
*len*: the number of elements to be copied.
*buf*: the destination buffer.

**THROWS:**
ArrayIndexOutOfBoundsException: if one of the indexes in the region is not valid.

### <a name="139">Set<PrimitiveType>ArrayRegion Routines</a>

	void Set<PrimitiveType>ArrayRegion(JNIEnv *env, ArrayType array, jsize start, jsize len, const NativeType *buf);

> A family of functions that copies back a region of a primitive array from a buffer.

一系列函数，用于从缓冲区复制基本数组的区域。

> The following table describes the specific primitive array element accessors. You should make the following replacements:
> - Replace Set\<PrimitiveType>ArrayRegion with one of the actual primitive element accessor routine names from the following table.
> - Replace ArrayType with the corresponding array type.
> - Replace NativeType with the corresponding native type for that routine.

下表描述了特定的原始数组元素访问器。 您应该进行以下替换：
- 将Set \<PrimitiveType> ArrayRegion替换为下表中的一个实际原始元素访问器例程名称。
- 将ArrayType替换为相应的数组类型。
- 将NativeType替换为该例程的相应本机类型。


**Set\<PrimitiveType>ArrayRegion Family of Array Accessor Routines**
|  Set\<PrimitiveType>ArrayRegion Routine  |  Array Type  |  Native Type  |
|  ---  |  ---  |  ---  |
|SetBooleanArrayRegion()|	jbooleanArray|	jboolean|
|SetByteArrayRegion()|	jbyteArray	|jbyte|
|SetCharArrayRegion()|	jcharArray|	jchar|
|SetShortArrayRegion()|	jshortArray|	jshort|
|SetIntArrayRegion()|	jintArray|	jint|
|SetLongArrayRegion()|	jlongArray|	jlong|
|SetFloatArrayRegion()|	jfloatArray|	jfloat|
|SetDoubleArrayRegion()|	jdoubleArray|	jdouble|

**LINKAGE:**
Indices in the JNIEnv interface function table.

**Set\<PrimitiveType>ArrayRegion Family of Array Accessor Routines**

|  Set\<PrimitiveType>ArrayRegion Routine  |  Index  |
|  ---  |  ---  |
|SetBooleanArrayRegion()|	207|
|SetByteArrayRegion()|	208|
|SetCharArrayRegion()|	209|
|SetShortArrayRegion()	|210||
|SetIntArrayRegion()|	211|
|SetLongArrayRegion()|	212||
|SetFloatArrayRegion()	|213|
|SetDoubleArrayRegion()	|214||

**PARAMETERS:**

*env*: the JNI interface pointer.
*array*: a Java array.
*start*: the starting index.
*len*: the number of elements to be copied.
*buf*: the source buffer.

**THROWS:**

ArrayIndexOutOfBoundsException: if one of the indexes in the region is not valid.

> **Note**: As of JDK/JRE 1.1, programmers can use Get/Release<primitivetype>ArrayElements functions to obtain a pointer to primitive array elements. If the VM supports pinning, the pointer to the original data is returned; otherwise, a copy is made.
> New functions introduced as of JDK/JRE 1.3 allow native code to obtain a direct pointer to array elements even if the VM does not support pinning.

从JDK / JRE 1.1开始，程序员可以使用Get / Release <primitivetype> ArrayElements函数来获取指向原始数组元素的指针。 如果VM支持固定，则返回指向原始数据的指针; 否则，制作副本。
从JDK / JRE 1.3开始引入的新功能允许本机代码获取指向数组元素的直接指针，即使VM不支持固定也是如此。

### <a name="1310">GetPrimitiveArrayCritical, ReleasePrimitiveArrayCritical</a>
	
	void * GetPrimitiveArrayCritical(JNIEnv *env, jarray array, jboolean *isCopy);
	void ReleasePrimitiveArrayCritical(JNIEnv *env, jarray array, void *carray, jint mode);

> The semantics of these two functions are very similar to the existing Get/Release<primitivetype>ArrayElements functions. If possible, the VM returns a pointer to the primitive array; otherwise, a copy is made. However, there are significant restrictions on how these functions can be used.

这两个函数的语义与现有的Get / Release <primitivetype> ArrayElements函数非常相似。 如果可能，VM返回指向基元数组的指针; 否则，制作副本。 但是，如何使用这些功能存在重大限制。

> After calling GetPrimitiveArrayCritical, the native code should not run for an extended period of time before it calls ReleasePrimitiveArrayCritical. We must treat the code inside this pair of functions as running in a "critical region." Inside a critical region, native code must not call other JNI functions, or any system call that may cause the current thread to block and wait for another Java thread. (For example, the current thread must not call read on a stream being written by another Java thread.)

在调用GetPrimitiveArrayCritical之后，本机代码在调用ReleasePrimitiveArrayCritical之前不应该运行很长一段时间。 我们必须将这对函数中的代码视为在“关键区域”中运行。 在关键区域内，本机代码不得调用其他JNI函数或任何可能导致当前线程阻塞并等待另一个Java线程的系统调用。 （例如，当前线程不能对另一个Java线程正在写入的流调用read。）

> These restrictions make it more likely that the native code will obtain an uncopied version of the array, even if the VM does not support pinning. For example, a VM may temporarily disable garbage collection when the native code is holding a pointer to an array obtained via GetPrimitiveArrayCritical.

这些限制使得本机代码更有可能获得阵列的未复制版本，即使VM不支持固定。 例如，当本机代码持有指向通过GetPrimitiveArrayCritical获得的数组的指针时，VM可以临时禁用垃圾收集。

> Multiple pairs of GetPrimtiveArrayCritical and ReleasePrimitiveArrayCritical may be nested. For example:

可以嵌套多对GetPrimtiveArrayCritical和ReleasePrimitiveArrayCritical。 例如：

	  jint len = (*env)->GetArrayLength(env, arr1);
	  jbyte *a1 = (*env)->GetPrimitiveArrayCritical(env, arr1, 0);
	  jbyte *a2 = (*env)->GetPrimitiveArrayCritical(env, arr2, 0);
	  /* We need to check in case the VM tried to make a copy. */
	  if (a1 == NULL || a2 == NULL) {
		... /* out of memory exception thrown */
	  }
	  memcpy(a1, a2, len);
	  (*env)->ReleasePrimitiveArrayCritical(env, arr2, a2, 0);
	  (*env)->ReleasePrimitiveArrayCritical(env, arr1, a1, 0);
	  
> Note that GetPrimitiveArrayCritical might still make a copy of the array if the VM internally represents arrays in a different format. Therefore we need to check its return value against NULL for possible out of memory situations.

请注意，如果VM内部表示不同格式的数组，则GetPrimitiveArrayCritical可能仍会生成该数组的副本。 因此，我们需要针对可能的内存不足情况检查其返回值是否为NULL。

**LINKAGE** (GetPrimitiveArrayCritical):
Linkage Index 222 in the JNIEnv interface function table.

**LINKAGE** (ReleasePrimitiveArrayCritical):
Linkage Index 223 in the JNIEnv interface function table.

**SINCE:**
JDK/JRE 1.2

## <a name="14"> Registering Native Methods</a>
### <a name="141">RegisterNatives</a>
	jint RegisterNatives(JNIEnv *env, jclass clazz, const JNINativeMethod *methods, jint nMethods);

> Registers native methods with the class specified by the clazz argument. The methods parameter specifies an array of JNINativeMethod structures that contain the names, signatures, and function pointers of the native methods. The name and signature fields of the JNINativeMethod structure are pointers to modified UTF-8 strings. The nMethods parameter specifies the number of native methods in the array. The JNINativeMethod structure is defined as follows:

使用clazz参数指定的类注册本机方法。 methods参数指定JNINativeMethod结构的数组，其中包含本机方法的名称，签名和函数指针。 JNINativeMethod结构的名称和签名字段是指向修改后的UTF-8字符串的指针。 nMethods参数指定数组中的本机方法数。 JNINativeMethod结构定义如下：

	typedef struct {

		char *name;

		char *signature;

		void *fnPtr;

	} JNINativeMethod;

> The function pointers nominally must have the following signature:

名义上的函数指针必须具有以下签名：

	ReturnType (*fnPtr)(JNIEnv *env, jobject objectOrClass, ...);

**LINKAGE:**
Index 215 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*clazz*: a Java class object.
*methods*: the native methods in the class.
*nMethods*: the number of native methods in the class.

**RETURNS:**
Returns “0” on success; returns a negative value on failure.

**THROWS:**
NoSuchMethodError: if a specified method cannot be found or if the method is not native.

### <a name="142">UnregisterNatives</a>
	jint UnregisterNatives(JNIEnv *env, jclass clazz);

> Unregisters native methods of a class. The class goes back to the state before it was linked or registered with its native method functions.

取消注册类的本机方法。 该类返回到它与其本机方法函数链接或注册之前的状态。

> This function should not be used in normal native code. Instead, it provides special programs a way to reload and relink native libraries.

此函数不应在普通本机代码中使用。 相反，它为特殊程序提供了重新加载和重新链接本机库的方法。

**LINKAGE:**

Index 216 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*clazz*: a Java class object.

**RETURNS:**
Returns “0” on success; returns a negative value on failure.

## <a name="15">Monitor Operations</a>
### <a name="151"> MonitorEnter</a>

	jint MonitorEnter(JNIEnv *env, jobject obj);

> Enters the monitor associated with the underlying Java object referred to by obj.

输入与obj引用的基础Java对象关联的监视器。

> Enters the monitor associated with the object referred to by obj. The obj reference must not be NULL.

输入与obj引用的对象关联的监视器。 obj引用不能为NULL。

> Each Java object has a monitor associated with it. If the current thread already owns the monitor associated with obj, it increments a counter in the monitor indicating the number of times this thread has entered the monitor. If the monitor associated with obj is not owned by any thread, the current thread becomes the owner of the monitor, setting the entry count of this monitor to 1. If another thread already owns the monitor associated with obj, the current thread waits until the monitor is released, then tries again to gain ownership.

每个Java对象都有一个与之关联的监视器。 如果当前线程已经拥有与obj关联的监视器，它会增加监视器中的计数器，指示此线程进入监视器的次数。 如果与obj关联的监视器不属于任何线程，则当前线程将成为监视器的所有者，将此监视器的条目计数设置为1.如果另一个线程已拥有与obj关联的监视器，则当前线程将等待，直到 监视器被释放，然后再次尝试获得所有权。

> A monitor entered through a MonitorEnter JNI function call cannot be exited using the monitorexit Java virtual machine instruction or a synchronized method return. A MonitorEnter JNI function call and a monitorenter Java virtual machine instruction may race to enter the monitor associated with the same object.

无法使用monitorexit Java虚拟机指令或同步方法返回退出通过MonitorEnter JNI函数调用输入的监视器。 MonitorEnter JNI函数调用和monitorenter Java虚拟机指令可能竞争进入与同一对象关联的监视器。

> To avoid deadlocks, a monitor entered through a MonitorEnter JNI function call must be exited using the MonitorExit JNI call, unless the DetachCurrentThread call is used to implicitly release JNI monitors.

为避免死锁，必须使用MonitorExit JNI调用退出通过MonitorEnter JNI函数调用输入的监视器，除非使用DetachCurrentThread调用来隐式释放JNI监视器。

**LINKAGE:**

Index 217 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*obj*: a normal Java object or class object.

**RETURNS:**
Returns “0” on success; returns a negative value on failure.

### <a name="152">MonitorExit</a>

	jint MonitorExit(JNIEnv *env, jobject obj);

> The current thread must be the owner of the monitor associated with the underlying Java object referred to by obj. The thread decrements the counter indicating the number of times it has entered this monitor. If the value of the counter becomes zero, the current thread releases the monitor.

当前线程必须是与obj引用的基础Java对象关联的监视器的所有者。 线程递减计数器，指示它进入此监视器的次数。 如果计数器的值变为零，则当前线程释放监视器。

> Native code must not use MonitorExit to exit a monitor entered through a synchronized method or a monitorenter Java virtual machine instruction.

本机代码不得使用MonitorExit退出通过synchronized方法或monitorenter Java虚拟机指令输入的监视器。

**LINKAGE:**

Index 218 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*obj*: a normal Java object or class object.

**RETURNS:**
Returns “0” on success; returns a negative value on failure.

**EXCEPTIONS:**
IllegalMonitorStateException: if the current thread does not own the monitor.

## <a name="16">NIO Support</a>

> The NIO-related entry points allow native code to access java.nio direct buffers. The contents of a direct buffer can, potentially, reside in native memory outside of the ordinary garbage-collected heap. For information about direct buffers, please see New I/O APIs and the specification of the java.nio.ByteBuffer class.

与NIO相关的入口点允许本机代码访问java.nio直接缓冲区。 直接缓冲区的内容可能存在于普通垃圾收集堆之外的本机内存中。 有关直接缓冲区的信息，请参阅新I / O API和java.nio.ByteBuffer类的规范。

> Three new functions introduced in JDK/JRE 1.4 allow JNI code to create, examine, and manipulate direct buffers:
> - <a href="161">NewDirectByteBuffer</a>
> - <a href="162">GetDirectBufferAddress</a>
> - <a href="163">GetDirectBufferCapacity</a>

JDK / JRE 1.4中引入的三个新函数允许JNI代码创建，检查和操作直接缓冲区：
- <a href="161">NewDirectByteBuffer</a>
- <a href="162">GetDirectBufferAddress</a>
- <a href="163">GetDirectBufferCapacity</a>



> Every implementation of the Java virtual machine must support these functions, but not every implementation is required to support JNI access to direct buffers. If a JVM does not support such access then the NewDirectByteBuffer and GetDirectBufferAddress functions must always return NULL, and the GetDirectBufferCapacity function must always return -1. If a JVM does support such access then these three functions must be implemented to return the appropriate values.

Java虚拟机的每个实现都必须支持这些功能，但并不是每个实现都需要支持对直接缓冲区的JNI访问。 如果JVM不支持此类访问，则NewDirectByteBuffer和GetDirectBufferAddress函数必须始终返回NULL，并且GetDirectBufferCapacity函数必须始终返回-1。 如果JVM确实支持此类访问，则必须实现这三个函数以返回适当的值。

### <a name="161">NewDirectByteBuffer</a>

	jobject NewDirectByteBuffer(JNIEnv* env, void* address, jlong capacity);

> Allocates and returns a direct java.nio.ByteBuffer referring to the block of memory starting at the memory address address and extending capacity bytes.

分配并返回直接java.nio.ByteBuffer，引用从内存地址开始的内存块并扩展容量字节。

> Native code that calls this function and returns the resulting byte-buffer object to Java-level code should ensure that the buffer refers to a valid region of memory that is accessible for reading and, if appropriate, writing. An attempt to access an invalid memory location from Java code will either return an arbitrary value, have no visible effect, or cause an unspecified exception to be thrown.

调用此函数并将生成的字节缓冲区对象返回到Java级代码的本机代码应确保缓冲区引用可供读取的内存的有效区域，并在适当时写入。 尝试从Java代码访问无效的内存位置将返回任意值，没有可见效果，或导致抛出未指定的异常。

**LINKAGE:**
Index 229 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNIEnv interface pointer
*address*: the starting address of the memory region (must not be NULL)
*capacity*: the size in bytes of the memory region (must be positive)

**RETURNS:**
Returns a local reference to the newly-instantiated java.nio.ByteBuffer object. Returns NULL if an exception occurs, or if JNI access to direct buffers is not supported by this virtual machine.

**EXCEPTIONS:**
OutOfMemoryError: if allocation of the ByteBuffer object fails

**SINCE:**
JDK/JRE 1.4

### <a name="162">GetDirectBufferAddress</a>

	void* GetDirectBufferAddress(JNIEnv* env, jobject buf);

> Fetches and returns the starting address of the memory region referenced by the given direct java.nio.Buffer.

获取并返回给定直接java.nio.Buffer引用的内存区域的起始地址。

> This function allows native code to access the same memory region that is accessible to Java code via the buffer object.

此函数允许本机代码通过缓冲区对象访问Java代码可访问的相同内存区域。

**LINKAGE:**
Index 230 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNIEnv interface pointer
*buf*: a direct java.nio.Buffer object (must not be NULL)

**RETURNS**:

> Returns the starting address of the memory region referenced by the buffer. Returns NULL if the memory region is undefined, if the given object is not a direct java.nio.Buffer, or if JNI access to direct buffers is not supported by this virtual machine.

返回缓冲区引用的内存区域的起始地址。 如果内存区域未定义，如果给定对象不是直接java.nio.Buffer，或者此虚拟机不支持对直接缓冲区的JNI访问，则返回NULL。

**SINCE:**
JDK/JRE 1.4

### <a name="163">GetDirectBufferCapacity</a>
	
	jlong GetDirectBufferCapacity(JNIEnv* env, jobject buf);

> Fetches and returns the capacity of the memory region referenced by the given direct java.nio.Buffer. The capacity is the number of elements that the memory region contains.

获取并返回给定直接java.nio.Buffer引用的内存区域的容量。 容量是内存区域包含的元素数。


**LINKAGE:**
Index 231 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNIEnv interface pointer
*buf*: a direct java.nio.Buffer object (must not be NULL)

**RETURNS:**

> Returns the capacity of the memory region associated with the buffer. Returns -1 if the given object is not a direct java.nio.Buffer, if the object is an unaligned view buffer and the processor architecture does not support unaligned access, or if JNI access to direct buffers is not supported by this virtual machine.

返回与缓冲区关联的内存区域的容量。 如果给定对象不是直接java.nio.Buffer，如果对象是未对齐的视图缓冲区且处理器体系结构不支持未对齐访问，或者此虚拟机不支持对直接缓冲区的JNI访问，则返回-1。

**SINCE:**
JDK/JRE 1.4

## <a name="17">Reflection Support</a>
Programmers can use the JNI to call Java methods or access Java fields if they know the name and type of the methods or fields. The Java Core Reflection API allows programmers to introspect Java classes at runtime. JNI provides a set of conversion functions between field and method IDs used in the JNI to field and method objects used in the Java Core Reflection API.

如果程序员知道方法或字段的名称和类型，则可以使用JNI调用Java方法或访问Java字段。 Java Core Reflection API允许程序员在运行时内省Java类。 JNI在JNI中使用的字段和方法ID之间提供了一组转换函数，用于Java Core Reflection API中使用的字段和方法对象。

### <a name="171"> FromReflectedMethod</a>

	jmethodID FromReflectedMethod(JNIEnv *env, jobject method);

> Converts a java.lang.reflect.Method or java.lang.reflect.Constructor object to a method ID.

将java.lang.reflect.Method或java.lang.reflect.Constructor对象转换为方法ID。

**LINKAGE:**
Index 7 in the JNIEnv interface function table.

**SINCE:**
JDK/JRE 1.2

### <a name="172">FromReflectedField</a>

	jfieldID FromReflectedField(JNIEnv *env, jobject field);

> Converts a java.lang.reflect.Field to a field ID.

将java.lang.reflect.Field转换为字段ID。

**LINKAGE:**
Index 8 in the JNIEnv interface function table.

**SINCE:**
JDK/JRE 1.2

### <a name="173"> ToReflectedMethod</a>
	jobject ToReflectedMethod(JNIEnv *env, jclass cls, jmethodID methodID, jboolean isStatic);

> Converts a method ID derived from cls to a java.lang.reflect.Method or java.lang.reflect.Constructor object. isStatic must be set to JNI_TRUE if the method ID refers to a static field, and JNI_FALSE otherwise.

将从cls派生的方法ID转换为java.lang.reflect.Method或java.lang.reflect.Constructor对象。 如果方法ID引用静态字段，则isStatic必须设置为JNI_TRUE，否则设置为JNI_FALSE。

> Throws OutOfMemoryError and returns 0 if fails.

抛出OutOfMemoryError，如果失败则返回0。

**LINKAGE:**
Index 9 in the JNIEnv interface function table.

**SINCE:**
JDK/JRE 1.2

### <a name="174">ToReflectedField</a>
	jobject ToReflectedField(JNIEnv *env, jclass cls, jfieldID fieldID, jboolean isStatic);

> Converts a field ID derived from cls to a java.lang.reflect.Field object. isStatic must be set to JNI_TRUE if fieldID refers to a static field, and JNI_FALSE otherwise.

将从cls派生的字段ID转换为java.lang.reflect.Field对象。 如果fieldID引用静态字段，则isStatic必须设置为JNI_TRUE，否则设置为JNI_FALSE。

> Throws OutOfMemoryError and returns 0 if fails.

抛出OutOfMemoryError，如果失败则返回0。

**LINKAGE:**
Index 12 in the JNIEnv interface function table.

**SINCE:**
JDK/JRE 1.2

## <a name="18">Java VM Interface<a>
### <a name="181">GetJavaVM</a>
	
	jint GetJavaVM(JNIEnv *env, JavaVM **vm);

> Returns the Java VM interface (used in the Invocation API) associated with the current thread. The result is placed at the location pointed to by the second argument, vm.

返回与当前线程关联的Java VM接口（在Invocation API中使用）。 结果放在第二个参数vm指向的位置。

**LINKAGE:**
Index 219 in the JNIEnv interface function table.

**PARAMETERS:**

*env*: the JNI interface pointer.
*vm*: a pointer to where the result should be placed.

**RETURNS:**
Returns “0” on success; returns a negative value on failure.