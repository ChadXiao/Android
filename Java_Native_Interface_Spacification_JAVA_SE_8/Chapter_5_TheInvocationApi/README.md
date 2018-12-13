# Chapter 5: The Invocation API


----------


> The Invocation API allows software vendors to load the Java VM into an arbitrary native application. Vendors can deliver Java-enabled applications without having to link with the Java VM source code.

Invocation API允许软件供应商将Java VM加载到任意本机应用程序中。 供应商可以提供支持Java的应用程序，而无需链接Java VM源代码。

> This chapter begins with an overview of the Invocation API. This is followed by reference pages for all Invocation API functions. It covers the following topics:

本章首先概述了Invocation API。 接下来是所有Invocation API函数的参考页面。 它涵盖以下主题：

- <a href="#1">Overview</a>
	- <a href="#1a">Creating the VM</a>
	- <a href="#1b">Attaching to the VM</a>
	- <a href="#1c">Detaching from the VM</a>
	- <a href="#1d">Unloading the VM</a>
- <a href="#2">Library and Version Management</a>
	- <a href="#2a">JNI_OnLoad</a>
	- <a href="#2b">JNI_OnUnload</a>
- <a href="#3">Invocation API Functions</a>
	- <a href="#3a">JNI_GetDefaultJavaVMInitArgs</a>
	- <a href="#3b">JNI_GetCreatedJavaVMs</a>
	- <a href="#3c">JNI_CreateJavaVM</a>
	- <a href="#3d">DestroyJavaVM</a>
	- <a href="#3e">AttachCurrentThread</a>
	- <a href="#3f">AttachCurrentThreadAsDaemon</a>
	- <a href="#3g">DetachCurrentThread</a>
	- <a href="#3h">GetEnv</a>


## <a name="1">Overview</a>
> The following code example illustrates how to use functions in the Invocation API. In this example, the C++ code creates a Java VM and invokes a static method, called Main.test. For clarity, we omit error checking.

以下代码示例说明了如何在Invocation API中使用函数。 在此示例中，C ++代码创建Java VM并调用静态方法，称为Main.test。 为清楚起见，我们省略了错误检查。

    #include <jni.h>       /* where everything is defined */
    ...
    JavaVM *jvm;       /* denotes a Java VM */
    JNIEnv *env;       /* pointer to native method interface */
    JavaVMInitArgs vm_args; /* JDK/JRE 6 VM initialization arguments */
    JavaVMOption* options = new JavaVMOption[1];
    options[0].optionString = "-Djava.class.path=/usr/lib/java";
    vm_args.version = JNI_VERSION_1_6;
    vm_args.nOptions = 1;
    vm_args.options = options;
    vm_args.ignoreUnrecognized = false;
    /* load and initialize a Java VM, return a JNI interface
     * pointer in env */
    JNI_CreateJavaVM(&jvm, (void**)&env, &vm_args);
    delete options;
    /* invoke the Main.test method using the JNI */
    jclass cls = env->FindClass("Main");
    jmethodID mid = env->GetStaticMethodID(cls, "test", "(I)V");
    env->CallStaticVoidMethod(cls, mid, 100);
    /* We are done. */
    jvm->DestroyJavaVM();

> This example uses three functions in the API. The Invocation API allows a native application to use the JNI interface pointer to access VM features. The design is similar to Netscape’s JRI Embedding Interface.

此示例在API中使用三个函数。 Invocation API允许本机应用程序使用JNI接口指针来访问VM功能。 该设计类似于Netscape的JRI嵌入接口。

### <a name="1a"> Creating the VM</a>

> The JNI_CreateJavaVM() function loads and initializes a Java VM and returns a pointer to the JNI interface pointer. The thread that called JNI_CreateJavaVM() is considered to be the main thread.

JNI_CreateJavaVM（）函数加载并初始化Java VM并返回指向JNI接口指针的指针。 调用JNI_CreateJavaVM（）的线程被认为是主线程。

### <a name="1b">Attaching to the VM</a>

> The JNI interface pointer (JNIEnv) is valid only in the current thread. Should another thread need to access the Java VM, it must first call AttachCurrentThread() to attach itself to the VM and obtain a JNI interface pointer. Once attached to the VM, a native thread works just like an ordinary Java thread running inside a native method. The native thread remains attached to the VM until it calls DetachCurrentThread() to detach itself.

JNI接口指针（JNIEnv）仅在当前线程中有效。 如果另一个线程需要访问Java VM，它必须首先调用AttachCurrentThread（）将其自身附加到VM并获取JNI接口指针。 一旦连接到VM，本机线程就像在本机方法中运行的普通Java线程一样工作。 本机线程保持连接到VM，直到它调用DetachCurrentThread（）来分离自身。

> The attached thread should have enough stack space to perform a reasonable amount of work. The allocation of stack space per thread is operating system-specific. For example, using pthreads, the stack size can be specified in the pthread_attr_t argument to pthread_create.

附加的线程应该有足够的堆栈空间来执行合理的工作量。 每个线程的堆栈空间分配是特定于操作系统的。 例如，使用pthreads，可以在pthread_create的pthread_attr_t参数中指定堆栈大小。

### <a name="1c">Detaching from the VM</a>

> A native thread attached to the VM must call DetachCurrentThread() to detach itself before exiting. A thread cannot detach itself if there are Java methods on the call stack.

附加到VM的本机线程必须在退出之前调用DetachCurrentThread（）以分离自身。 如果调用堆栈上有Java方法，则线程无法分离。

### <a name="1d">Unloading the VM</a>

> The JNI_DestroyJavaVM() function unloads a Java VM.

JNI_DestroyJavaVM（）函数卸载Java VM。

> The VM waits until the current thread is the only non-daemon user thread before it actually unloads. User threads include both Java threads and attached native threads. This restriction exists because a Java thread or attached native thread may be holding system resources, such as locks, windows, and so on. The VM cannot automatically free these resources. By restricting the current thread to be the only running thread when the VM is unloaded, the burden of releasing system resources held by arbitrary threads is on the programmer.

VM等待，直到当前线程是实际卸载之前唯一的非守护程序用户线程。 用户线程包括Java线程和附加的本机线程。 存在此限制是因为Java线程或附加的本机线程可能正在保留系统资源，例如锁，窗口等。 VM无法自动释放这些资源。 通过在卸载VM时将当前线程限制为唯一正在运行的线程，释放由任意线程保持的系统资源的负担在程序员上。

## <a name="2">Library and Version Management</a>

> Once a native library is loaded, it is visible from all class loaders. Therefore two classes in different class loaders may link with the same native method. This leads to two problems:
> -  A class may mistakenly link with native libraries loaded by a class with the same name in a different class loader.
> - Native methods can easily mix classes from different class loaders. This breaks the name space separation offered by class loaders, and leads to type safety problems.

加载本机库后，所有类加载器都可以看到它。 因此，不同类加载器中的两个类可以使用相同的本机方法链接。 这导致两个问题：
 - 类可能会错误地链接到由不同类加载器中具有相同名称的类加载的本机库。
 - 本机方法可以轻松地混合来自不同类加载器的类。 这打破了类加载器提供的名称空间分离，并导致类型安全问题。

> Each class loader manages its own set of native libraries. The same JNI native library cannot be loaded into more than one class loader. Doing so causes UnsatisfiedLinkError to be thrown. For example, System.loadLibrary throws an UnsatisfiedLinkError when used to load a native library into two class loaders. The benefits of the new approach are:
> -  Name space separation based on class loaders is preserved in native libraries. A native library cannot easily mix classes from different class loaders.
> - In addition, native libraries can be unloaded when their corresponding class loaders are garbage collected.

每个类加载器都管理自己的一组本机库。 无法将相同的JNI本机库加载到多个类加载器中。 这样做会导致抛出UnsatisfiedLinkError。 例如，System.loadLibrary在用于将本机库加载到两个类加载器时抛出UnsatisfiedLinkError。 新方法的好处是：
- 基于类加载器的名称空间分隔保留在本机库中。 本机库无法轻松混合来自不同类加载器的类。
- 此外，当对应的类加载器被垃圾收集时，可以卸载本机库。

> To facilitate version control and resource management, JNI libraries optionally export the following two functions:

为了便于版本控制和资源管理，JNI库可选择导出以下两个函数：

### <a name="2a">JNI_OnLoad</a>
	
	jint JNI_OnLoad(JavaVM *vm, void *reserved);

> The VM calls JNI_OnLoad when the native library is loaded (for example, through System.loadLibrary). JNI_OnLoad must return the JNI version needed by the native library.

VM在加载本机库时调用JNI_OnLoad（例如，通过System.loadLibrary）。 JNI_OnLoad必须返回本机库所需的JNI版本。

> In order to use any of the new JNI functions, a native library must export a JNI_OnLoad function that returns JNI_VERSION_1_2. If the native library does not export a JNI_OnLoad function, the VM assumes that the library only requires JNI version JNI_VERSION_1_1. If the VM does not recognize the version number returned by JNI_OnLoad, the VM will unload the library and act as if the library was +never loaded.

为了使用任何新的JNI函数，本机库必须导出返回JNI_VERSION_1_2的JNI_OnLoad函数。 如果本机库未导出JNI_OnLoad函数，则VM假定该库仅需要JNI版本JNI_VERSION_1_1。 如果VM无法识别JNI_OnLoad返回的版本号，则VM将卸载库并将其视为库从未加载。

	JNI_Onload_L(JavaVM *vm, void *reserved);

> If a library L is statically linked, then upon the first invocation of System.loadLibrary("L") or equivalent API, a JNI_OnLoad_L function will be invoked with the same arguments and expected return value as specified for the JNI_OnLoad function. JNI_OnLoad_L must return the JNI version needed by the native library. This version must be JNI_VERSION_1_8 or later. If the VM does not recognize the version number returned by JNI_OnLoad_L, the VM will act as if the library was never loaded.

如果库L是静态链接的，那么在第一次调用System.loadLibrary（“L”）或等效的API时，将使用为JNI_OnLoad函数指定的相同参数和预期返回值调用JNI_OnLoad_L函数。 JNI_OnLoad_L必须返回本机库所需的JNI版本。 此版本必须为JNI_VERSION_1_8或更高版本。 如果VM无法识别JNI_OnLoad_L返回的版本号，则VM将表现为从未加载库。

**LINKAGE:**
Exported from native libraries that contain native method implementation.

### <a name="2b">JNI_OnUnload</a>

	void JNI_OnUnload(JavaVM *vm, void *reserved);

> The VM calls JNI_OnUnload when the class loader containing the native library is garbage collected. This function can be used to perform cleanup operations. Because this function is called in an unknown context (such as from a finalizer), the programmer should be conservative on using Java VM services, and refrain from arbitrary Java call-backs.

当包含本机库的类加载器被垃圾收集时，VM调用JNI_OnUnload。 此功能可用于执行清理操作。 因为在未知的上下文中调用此函数（例如来自终结器），程序员应该保守使用Java VM服务，并避免任意Java回调。

> Note that JNI_OnLoad and JNI_OnUnload are two functions optionally supplied by JNI libraries, not exported from the VM.

请注意，JNI_OnLoad和JNI_OnUnload是JNI库可选提供的两个函数，不是从VM导出的。

	JNI_OnUnload_L(JavaVM *vm, void *reserved);

> When the class loader containing a statically linked native library L is garbage collected, the VM will invoke the JNI_OnUnload_L function of the library if such a function is exported. This function can be used to perform cleanup operations. Because this function is called in an unknown context (such as from a finalizer), the programmer should be conservative on using Java VM services, and refrain from arbitrary Java call-backs.

当包含静态链接的本机库L的类加载器被垃圾收集时，如果导出这样的函数，VM将调用库的JNI_OnUnload_L函数。 此功能可用于执行清理操作。 因为在未知的上下文中调用此函数（例如来自终结器），程序员应该保守使用Java VM服务，并避免任意Java回调。

**Informational Note:**
> The act of loading a native library is the complete process of making the library and its native entry points known and registered to the Java VM and runtime. Note that simply performing operating system level operations to load a native library, such as dlopen on a UNIX(R) system, does not fully accomplish this goal. A native function is normally called from the Java class loader to perform a call to the host operating system that will load the library into memory and return a handle to the native library. This handle will be stored and used in subsequent searches for native library entry points. The Java native class loader will complete the load process once the handle is successfully returned to register the library.

加载Native库的行为是使库及其本机入口点已知并注册到Java VM和运行时的完整过程。 请注意，仅仅执行操作系统级操作来加载Native库（例如UNIX（R）系统上的dlopen）并不能完全实现此目标。 通常从Java类加载器调用Native函数来执行对主机操作系统的调用，该操作系统将库加载到内存中并将句柄返回到Native库。 此句柄将存储并用于后续搜索Native库入口点。 一旦成功返回句柄以注册库，JavaNative类加载器将完成加载过程。

**LINKAGE:**

Exported from native libraries that contain native method implementation.

## <a name="3">Invocation API Functions</a>

> The JavaVM type is a pointer to the Invocation API function table. The following code example shows this function table.

JavaVM类型是指向Invocation API函数表的指针。 以下代码示例显示了此函数表。

	typedef const struct JNIInvokeInterface *JavaVM;

	const struct JNIInvokeInterface ... = {
		NULL,
		NULL,
		NULL,

		DestroyJavaVM,
		AttachCurrentThread,
		DetachCurrentThread,

		GetEnv,

		AttachCurrentThreadAsDaemon
	};
> Note that three Invocation API functions, JNI_GetDefaultJavaVMInitArgs(), JNI_GetCreatedJavaVMs(), and JNI_CreateJavaVM(), are not part of the JavaVM function table. These functions can be used without a preexisting JavaVM structure.

请注意，三个Invocation API函数JNI_GetDefaultJavaVMInitArgs（），JNI_GetCreatedJavaVMs（）和JNI_CreateJavaVM（）不是JavaVM函数表的一部分。 可以在没有预先存在的JavaVM结构的情况下使用这些函数。

### <a name="3a">JNI_GetDefaultJavaVMInitArgs</a>

	jint JNI_GetDefaultJavaVMInitArgs(void *vm_args);

> Returns a default configuration for the Java VM. Before calling this function, native code must set the vm_args->version field to the JNI version it expects the VM to support. After this function returns, vm_args->version will be set to the actual JNI version the VM supports.

返回Java VM的默认配置。 在调用此函数之前，本机代码必须将vm_args-> version字段设置为它期望VM支持的JNI版本。 此函数返回后，vm_args-> version将设置为VM支持的实际JNI版本。

**LINKAGE:**
Exported from the native library that implements the Java virtual machine.

**PARAMETERS:**
vm_args: a pointer to a JavaVMInitArgs structure in to which the default arguments are filled.

**RETURNS:**
Returns JNI_OK if the requested version is supported; returns a JNI error code (a negative number) if the requested version is not supported.

### <a name="3b">JNI_GetCreatedJavaVMs</a>

	jint JNI_GetCreatedJavaVMs(JavaVM **vmBuf, jsize bufLen, jsize *nVMs);

> Returns all Java VMs that have been created. Pointers to VMs are written in the buffer vmBuf in the order they are created. At most bufLen number of entries will be written. The total number of created VMs is returned in *nVMs.

返回已创建的所有Java VM。 指向VM的指针按创建顺序写入缓冲区vmBuf。 最多将写入bufLen条目数。 在* nVM中返回创建的VM的总数。

> Creation of multiple VMs in a single process is not supported.

不支持在单个进程中创建多个VM。

**LINKAGE:**
Exported from the native library that implements the Java virtual machine.

**PARAMETERS:**

*vmBuf*: pointer to the buffer where the VM structures will be placed.
*bufLen*: the length of the buffer.
*nVMs*: a pointer to an integer.

**RETURNS:**
Returns JNI_OK on success; returns a suitable JNI error code (a negative number) on failure.

### <a name="3c">JNI_CreateJavaVM</a>

	jint JNI_CreateJavaVM(JavaVM **p_vm, void **p_env, void *vm_args);

> Loads and initializes a Java VM. The current thread becomes the main thread. Sets the env argument to the JNI interface pointer of the main thread.

加载并初始化Java VM。 当前线程成为主线程。 将env参数设置为主线程的JNI接口指针。

> Creation of multiple VMs in a single process is not supported.

不支持在单个进程中创建多个VM。

> The second argument to JNI_CreateJavaVM is always a pointer to JNIEnv *, while the third argument is a pointer to a JavaVMInitArgs structure which uses option strings to encode arbitrary VM start up options:

JNI_CreateJavaVM的第二个参数始终是指向JNIEnv *的指针，而第三个参数是指向JavaVMInitArgs结构的指针，该结构使用选项字符串来编码任意VM启动选项：

	typedef struct JavaVMInitArgs {
		jint version;

		jint nOptions;
		JavaVMOption *options;
		jboolean ignoreUnrecognized;
	} JavaVMInitArgs;
	
> The options field is an array of the following type:

options字段是以下类型的数组：

	typedef struct JavaVMOption {
		char *optionString;  /* the option as a string in the default platform encoding */
		void *extraInfo;
	} JavaVMOption;


> The size of the array is denoted by the nOptions field in JavaVMInitArgs. If ignoreUnrecognized is JNI_TRUE, JNI_CreateJavaVM ignore all unrecognized option strings that begin with "-X" or "_". If ignoreUnrecognized is JNI_FALSE, JNI_CreateJavaVM returns JNI_ERR as soon as it encounters any unrecognized option strings. All Java VMs must recognize the following set of standard options:

数组的大小由JavaVMInitArgs中的nOptions字段表示。 如果ignoreUnrecognized是JNI_TRUE，则JNI_CreateJavaVM将忽略以“-X”或“_”开头的所有无法识别的选项字符串。 如果ignoreUnrecognized是JNI_FALSE，则JNI_CreateJavaVM会在遇到任何无法识别的选项字符串时立即返回JNI_ERR。 所有Java VM必须识别以下一组标准选项：

**Standard Options**
|  optionString  |  meaning  |
|  ---  |  ---  |
|  -D\<name>=\<value>  |	Set a system property|
|`-verbose[:class|gc|jni]`|	Enable verbose output. The options can be followed by a comma-separated list of names indicating what kind of messages will be printed by the VM. For example, "-verbose:gc,class" instructs the VM to print GC and class loading related messages. Standard names include: gc, class, and jni. All nonstandard (VM-specific) names must begin with "X".|
|vfprintf	|extraInfo is a pointer to the vfprintf hook.|
|exit	|extraInfo is a pointer to the exit hook.|
|abort	|extraInfo is a pointer to the abort hook.|

> In addition, each VM implementation may support its own set of non-standard option strings. Non-standard option names must begin with "-X" or an underscore ("_"). For example, the JDK/JRE supports -Xms and -Xmx options to allow programmers specify the initial and maximum heap size. Options that begin with "-X" are accessible from the "java" command line.

此外，每个VM实现可以支持其自己的一组非标准选项字符串。 非标准选项名称必须以“-X”或下划线（“_”）开头。 例如，JDK / JRE支持-Xms和-Xmx选项，以允许程序员指定初始和最大堆大小。 以“-X”开头的选项可从“java”命令行访问。

> Here is the example code that creates a Java VM in the JDK/JRE:

以下是在JDK / JRE中创建Java VM的示例代码：

	JavaVMInitArgs vm_args;
	JavaVMOption options[4];

	options[0].optionString = "-Djava.compiler=NONE";           /* disable JIT */
	options[1].optionString = "-Djava.class.path=c:\myclasses"; /* user classes */
	options[2].optionString = "-Djava.library.path=c:\mylibs";  /* set native library path */
	options[3].optionString = "-verbose:jni";                   /* print JNI-related messages */

	vm_args.version = JNI_VERSION_1_2;
	vm_args.options = options;
	vm_args.nOptions = 4;
	vm_args.ignoreUnrecognized = TRUE;

	/* Note that in the JDK/JRE, there is no longer any need to call
	 * JNI_GetDefaultJavaVMInitArgs.
	 */
	res = JNI_CreateJavaVM(&vm, (void **)&env, &vm_args);
	if (res < 0) ...
	
**LINKAGE:**
Exported from the native library that implements the Java virtual machine.

**PARAMETERS:**

*p_vm*: pointer to the location where the resulting VM structure will be placed.
*p_env*: pointer to the location where the JNI interface pointer for the main thread will be placed.
*vm_args*: Java VM initialization arguments.

**RETURNS:**
Returns JNI_OK on success; returns a suitable JNI error code (a negative number) on failure.

### <a name="3d">DestroyJavaVM</a>

	jint DestroyJavaVM(JavaVM *vm);

> Unloads a Java VM and reclaims its resources.

卸载Java VM并回收其资源。

> Any thread, whether attached or not, can invoke this function. If the current thread is attached, the VM waits until the current thread is the only non-daemon user-level Java thread. If the current thread is not attached, the VM attaches the current thread and then waits until the current thread is the only non-daemon user-level thread.

任何线程，无论是否附加，都可以调用此函数。 如果附加了当前线程，则VM将等待，直到当前线程是唯一的非守护程序用户级Java线程。 如果未附加当前线程，则VM会附加当前线程，然后等待，直到当前线程是唯一的非守护程序用户级线程。

**LINKAGE:**
Index 3 in the JavaVM interface function table.

**PARAMETERS:**
vm: the Java VM that will be destroyed.

**RETURNS:**
Returns JNI_OK on success; returns a suitable JNI error code (a negative number) on failure.

> Unloading of the VM is not supported.

不支持卸载VM。

### <a name="3e">AttachCurrentThread</a>

	jint AttachCurrentThread(JavaVM *vm, void **p_env, void *thr_args);

> Attaches the current thread to a Java VM. Returns a JNI interface pointer in the JNIEnv argument.

将当前线程附加到Java VM。 返回JNIEnv参数中的JNI接口指针。

> Trying to attach a thread that is already attached is a no-op.

尝试附加已经附加的线程是无操作。

> A native thread cannot be attached simultaneously to two Java VMs.

本机线程无法同时连接到两个Java VM。

> When a thread is attached to the VM, the context class loader is the bootstrap loader.

当线程附加到VM时，上下文类加载器是引导加载程序。

**LINKAGE:**
Index 4 in the JavaVM interface function table.

**PARAMETERS:**

*vm*: the VM to which the current thread will be attached.
*p_env*: pointer to the location where the JNI interface pointer of the current thread will be placed.
*thr_args*: can be NULL or a pointer to a JavaVMAttachArgs structure to specify additional information:

> The second argument to AttachCurrentThread is always a pointer to JNIEnv. The third argument to AttachCurrentThread was reserved, and should be set to NULL.

AttachCurrentThread的第二个参数始终是指向JNIEnv的指针。 AttachCurrentThread的第三个参数是保留的，应该设置为NULL。

> You pass a pointer to the following structure to specify additional information:

您将指针传递给以下结构以指定其他信息：

	typedef struct JavaVMAttachArgs {
		jint version;
		char *name;    /* the name of the thread as a modified UTF-8 string, or NULL */
		jobject group; /* global ref of a ThreadGroup object, or NULL */
	} JavaVMAttachArgs
	RETURNS:
	Returns JNI_OK on success; returns a suitable JNI error code (a negative number) on failure.

### <a name="3e">AttachCurrentThreadAsDaemon</a>

	jint AttachCurrentThreadAsDaemon(JavaVM* vm, void** penv, void* args);

> Same semantics as AttachCurrentThread, but the newly-created java.lang.Thread instance is a daemon.

与AttachCurrentThread相同的语义，但新创建的java.lang.Thread实例是一个守护进程。

> If the thread has already been attached via either AttachCurrentThread or AttachCurrentThreadAsDaemon, this routine simply sets the value pointed to by penv to the JNIEnv of the current thread. In this case neither AttachCurrentThread nor this routine have any effect on the daemon status of the thread.

如果线程已通过AttachCurrentThread或AttachCurrentThreadAsDaemon连接，则此例程只是将penv指向的值设置为当前线程的JNIEnv。 在这种情况下，AttachCurrentThread和此例程都不会对线程的守护程序状态产生任何影响。

**LINKAGE:**
Index 7 in the JavaVM interface function table.

**PARAMETERS:**

*vm*: the virtual machine instance to which the current thread will be attached.
*penv*: a pointer to the location in which the JNIEnv interface pointer for the current thread will be placed.
*args*: a pointer to a JavaVMAttachArgs structure.

**RETURNS**
Returns JNI_OK on success; returns a suitable JNI error code (a negative number) on failure.

**EXCEPTIONS**
None.

### <a name="3f">DetachCurrentThread</a>

	jint DetachCurrentThread(JavaVM *vm);

> Detaches the current thread from a Java VM. All Java monitors held by this thread are released. All Java threads waiting for this thread to die are notified.

从Java VM分离当前线程。 该线程持有的所有Java监视器都将被释放。 等待此线程死亡的所有Java线程都会收到通知。

> The main thread can be detached from the VM.

主线程可以从VM分离。
**LINKAGE:**
Index 5 in the JavaVM interface function table.

**PARAMETERS:**
vm: the VM from which the current thread will be detached.

**RETURNS:**
Returns JNI_OK on success; returns a suitable JNI error code (a negative number) on failure.

### <a name="3g">GetEnv</a>

	jint GetEnv(JavaVM *vm, void **env, jint version);

**LINKAGE:**
Index 6 in the JavaVM interface function table.

**PARAMETERS:**

*vm*: The virtual machine instance from which the interface will be retrieved.
*env*: pointer to the location where the JNI interface pointer for the current thread will be placed.
*version*: The requested JNI version.

**RETURNS:**
> If the current thread is not attached to the VM, sets *env to NULL, and returns JNI_EDETACHED. If the specified version is not supported, sets *env to NULL, and returns JNI_EVERSION. Otherwise, sets *env to the appropriate interface, and returns JNI_OK.

如果当前线程未附加到VM，则将* env设置为NULL，并返回JNI_EDETACHED。 如果不支持指定的版本，请将* env设置为NULL，并返回JNI_EVERSION。 否则，将* env设置为适当的接口，并返回JNI_OK。