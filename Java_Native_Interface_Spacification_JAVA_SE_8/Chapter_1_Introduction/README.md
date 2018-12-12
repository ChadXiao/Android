# Chapter 1: Introduction


----------
> This chapter introduces the Java Native Interface (JNI). The JNI is a native programming interface. It allows Java code that runs inside a Java Virtual Machine (VM) to interoperate with applications and libraries written in other programming languages, such as C, C++, and assembly.

本章介绍Java Native Interface（JNI）。 JNI是本机编程接口。 它允许在Java虚拟机（VM）内运行的Java代码与使用其他编程语言（如C，C ++和汇编语言）编写的应用程序和库进行互操作。

 > The most important benefit of the JNI is that it imposes no restrictions on the implementation of the underlying Java VM. Therefore, Java VM vendors can add support for the JNI without affecting other parts of the VM. Programmers can write one version of a native application or library and expect it to work with all Java VMs supporting the JNI.


JNI最重要的好处是它对底层Java VM的实现没有任何限制。 因此，Java VM供应商可以添加对JNI的支持，而不会影响VM的其他部分。 程序员可以编写一个版本的本机应用程序或库，并期望它可以与支持JNI的所有Java VM一起使用。

> This chapter covers the following topics:

 - <a href="#1">Java Native Interface Overview(概览)</a>
 - <a href="#2">Historical Background</a>
	 - <a href="#21">JDK 1.0 Native Method Interface</a>
	 -  <a href="#22">Java Runtime Interface</a>
	 -  <a href="#23">Raw Native Interface and Java/COM Interface</a>
 -  <a href="#3">Objectives</a>
 -   <a href="#4">Java Native Interface Approach</a>
 -   <a href="#5">Programming to the JNI</a>
 -   <a href="#6">Changes</a>

## <a name="1">Java Native Interface Overview</a>

> While you can write applications entirely in Java, there are situations where Java alone does not meet the needs of your application. Programmers use the JNI to write Java native methods to handle those situations when an application cannot be written entirely in Java.

虽然您可以完全使用Java编写应用程序，但有些情况下Java本身并不能满足您的应用程序的需求。 当应用程序无法完全用Java编写时，程序员使用JNI编写Java本机方法来处理这些情况。

> The following examples illustrate when you need to use Java native methods:

以下示例说明何时需要使用Java本机方法：



 >  - The standard Java class library does not support the platform-dependent features needed by the application.
 >  -  You already have a library written in another language, and wish to make it accessible to Java code through the JNI.
 >  - You want to implement a small portion of time-critical code in a lower-level language such as assembly.

 - 标准Java类库不支持应用程序所需的与平台相关的功能。
 - 您已经有一个用另一种语言编写的库，并希望通过JNI使其可以访问Java代码。
 - 您希望在较低级别的语言（如汇编语言）中实现一小部分时间关键代码。

> By programming through the JNI, you can use native methods to:

通过JNI编程，您可以使用本机方法：

> - Create, inspect, and update Java objects (including arrays and strings).
> - Call Java methods.
> - Catch and throw exceptions.
> - Load classes and obtain class information.
> - Perform runtime type checking.

 - 创建，检查和更新Java对象（包括数组和字符串）。
 - 调用Java方法。
 - 捕获并抛出异常。
 - 加载类并获取类信息。
 - 执行运行时类型检查。

> You can also use the JNI with the Invocation API to enable an arbitrary native application to embed the Java VM. This allows programmers to easily make their existing applications Java-enabled without having to link with the VM source code.

您还可以将JNI与Invocation API一起使用，以使任意本机应用程序能够嵌入Java VM。 这使程序员可以轻松地使其现有应用程序支持Java，而无需与VM源代码链接。

## <a name="2">Historical Background</a>

>　VMs from different vendors offer different native method interfaces. These different interfaces force programmers to produce, maintain, and distribute multiple versions of native method libraries on a given platform.

来自不同供应商的VM提供不同的本机方法接口。 这些不同的接口迫使程序员在给定平台上生成，维护和分发多个版本的本机方法库。


> We briefly examine some of the native method interfaces, such as:

我们简要介绍一些本机方法接口，例如：

 - <a href="#21">JDK 1.0 Native Method Interface</a>
 -  <a href="#22">Java Runtime Interface</a>
 -  <a href="#23">Raw Native Interface and Java/COM Interface</a>

### <a name="21">JDK 1.0 Native Method Interface</a>

> JDK 1.0 was shipped with a native method interface. Unfortunately, there were two major reasons that this interface was unsuitable for adoption by other Java VMs.

JDK 1.0附带了本机方法接口。 不幸的是，这个接口不适合其他Java VM采用有两个主要原因。

> First, the native code accessed fields in Java objects as members of C structures. However, the Java Language Specification does not define how objects are laid out in memory. If a Java VM lays out objects differently in memory, then the programmer would have to recompile the native method libraries.

首先，本机代码访问Java对象中的字段作为C结构的成员。 但是，Java语言规范没有定义对象在内存中的布局方式。 如果Java VM在内存中以不同方式布局对象，则程序员必须重新编译本机方法库。

> Second, JDK 1.0's native method interface relied on a conservative garbage collector. The unrestricted use of the unhand macro, for example, made it necessary to conservatively scan the native stack.

其次，JDK 1.0的本机方法接口依赖于保守的垃圾收集器。 例如，不受限制地使用unhand宏使得必须保守地扫描本机堆栈。

### <a name="22">Java Runtime Interface</a>

> Netscape had proposed the Java Runtime Interface (JRI), a general interface for services provided in the Java virtual machine. JRI was designed with portability in mind---it makes few assumptions about the implementation details in the underlying Java VM. The JRI addressed a wide range of issues, including native methods, debugging, reflection, embedding (invocation), and so on.

Netscape提出了Java运行时接口（JRI），它是Java虚拟机中提供的服务的通用接口。 JRI的设计考虑了可移植性 - 它对底层Java VM中的实现细节做了很少的假设。 JRI解决了广泛的问题，包括本机方法，调试，反射，嵌入（调用）等。

### <a name="23">Raw Native Interface and Java/COM Interface</a>

> The Microsoft Java VM supports two native method interfaces. At the low level, it provides an efficient Raw Native Interface (RNI). The RNI offers a high degree of source-level backward compatibility with the JDK’s native method interface, although it has one major difference. Instead of relying on conservative garbage collection, the native code must use RNI functions to interact explicitly with the garbage collector.

Microsoft Java VM支持两种本机方法接口。 在低级别，它提供了有效的原始本机接口（RNI）。 RNI提供了与JDK本机方法接口的高度源级向后兼容性，尽管它有一个主要区别。 本机代码必须使用RNI函数与垃圾收集器明确交互，而不是依赖于保守的垃圾收集。

> At a higher level, Microsoft's Java/COM interface offers a language-independent standard binary interface to the Java VM. Java code can use a COM object as if it were a Java object. A Java class can also be exposed to the rest of the system as a COM class.

在更高级别，Microsoft的Java / COM接口为Java VM提供了与语言无关的标准二进制接口。 Java代码可以像使用Java对象一样使用COM对象。 Java类也可以作为COM类公开给系统的其余部分。

## <a name="3">Objectives</a>

>  We believe that a uniform, well-thought-out standard interface offers the following benefits for everyone:

我们相信，统一，经过深思熟虑的标准界面为每个人提供以下好处

> - Each VM vendor can support a larger body of native code.
> - Tool builders will not have to maintain different kinds of native method interfaces.
> - Application programmers will be able to write one version of their native code and this version will run on different VMs.


- 每个VM供应商都可以支持更大的本机代码。
- 工具构建器不必维护不同类型的本机方法接口。
- 应用程序编程人员将能够编写其本机代码的一个版本，该版本将在不同的VM上运行。

> The best way to achieve a standard native method interface is to involve all parties with an interest in Java VMs. Therefore we organized a series of discussions among the Java licensees on the design of a uniform native method interface. It is clear from the discussions that the standard native method interface must satisfy the following requirements:

实现标准本机方法接口的最佳方法是让所有各方都参与Java VM。 因此，我们在Java许可证持有者之间组织了一系列关于统一本机方法接口设计的讨论。 从讨论中可以清楚地看出，标准本机方法接口必须满足以下要求：

> - Binary compatibility - The primary goal is binary compatibility of native method libraries across all Java VM implementations on a given platform. Programmers should maintain only one version of their native method libraries for a given platform.
> -  Efficiency - To support time-critical code, the native method interface must impose little overhead. All known techniques to ensure VM-independence (and thus binary compatibility) carry a certain amount of overhead. We must somehow strike a compromise between efficiency and VM-independence.
> - Functionality - The interface must expose enough Java VM internals to allow native methods to accomplish useful tasks.


- 二进制兼容性 - 主要目标是在给定平台上的所有Java VM实现中对本机方法库进行二进制兼容。 程序员应该只为给定平台维护其本机方法库的一个版本。
- 效率 - 为了支持时间关键代码，本机方法接口必须施加很少的开销。 确保VM独立性（以及二进制兼容性）的所有已知技术都带有一定量的开销。 我们必须以某种方式在效率和VM独立性之间达成妥协。
- 功能 - 接口必须公开足够的Java VM内部，以允许本机方法完成有用的任务。

## <a name="4">Java Native Interface Approach</a>

> We hoped to adopt one of the existing approaches as the standard interface, because this would have imposed the least burden on programmers who had to learn multiple interfaces in different VMs. Unfortunately, no existing solutions are completely satisfactory in achieving our goals.

我们希望采用现有方法之一作为标准接口，因为这会给必须在不同VM中学习多个接口的程序员带来最小的负担。 不幸的是，现有的解决方案在实现我们的目

> Netscape’s JRI is the closest to what we envision as a portable native method interface, and was used as the starting point of our design. Readers familiar with the JRI will notice the similarities in the API naming convention, the use of method and field IDs, the use of local and global references, and so on. Despite our best efforts, however, the JNI is not binary-compatible with the JRI, although a VM can support both the JRI and the JNI.

Netscape的JRI最接近我们想象的便携式本机方法接口，并被用作我们设计的起点。 熟悉JRI的读者会注意到API命名约定，方法和字段ID的使用，本地和全局引用的使用等方面的相似之处。 尽管我们尽最大努力，但JNI与JRI不是二进制兼容的，尽管VM可以同时支持JRI和JNI。

> Microsoft’s RNI was an improvement over JDK 1.0 because it solved the problem of native methods working with a nonconservative garbage collector. The RNI, however, was not suitable as a VM-independent native method interface. Like the JDK, RNI native methods access Java objects as C structures, leading to two problems:

微软的RNI是对JDK 1.0的改进，因为它解决了使用非保守垃圾收集器的本机方法的问题。 但是，RNI不适合作为独立于VM的本机方法接口。 与JDK一样，RNI本机方法将Java对象作为C结构访问，导致两个问题：

> - RNI exposed the layout of internal Java objects to native code.
> - Direct access of Java objects as C structures makes it impossible to efficiently incorporate “write barriers,” which are necessary in advanced garbage collection algorithms.

- RNI将内部Java对象的布局暴露给本机代码。
- 直接访问Java对象作为C结构使得无法有效地合并“写入障碍”，这在高级垃圾收集算法中是必需的。

> As a binary standard, COM ensures complete binary compatibility across different VMs. Invoking a COM method requires only an indirect call, which carries little overhead. In addition, COM objects are a great improvement over dynamic-link libraries in solving versioning problems.

作为二进制标准，COM确保跨不同VM的完全二进制兼容性。 调用COM方法只需要间接调用，这几乎不会产生任何开销。 此外，COM对象在解决版本问题方面比动态链接库有了很大的改进。

> The use of COM as the standard Java native method interface, however, is hampered by a few factors:

但是，使用COM作为标准Java本机方法接口受到以下几个因素的阻碍：

> - First, the Java/COM interface lacks certain desired functions, such as accessing private fields and raising general exceptions.
> - Second, the Java/COM interface automatically provides the standard IUnknown and IDispatch COM interfaces for Java objects, so that native code can access public methods and fields. Unfortunately, the IDispatch interface does not deal with overloaded Java methods and is case-insensitive in matching method names. Furthermore, all Java methods exposed through the IDispatch interface are wrapped to perform dynamic type checking and coercion. This is because the IDispatch interface is designed with weakly-typed languages (such as Basic) in mind.
> - Third, instead of dealing with individual low-level functions, COM is designed to allow software components (including full-fledged applications) to work together. We believe that it is not appropriate to treat all Java classes or low-level native methods as software components.
>  - Fourth, the immediate adoption of COM is hampered by the lack of its support on UNIX platforms.


- 首先，Java / COM接口缺少某些所需的功能，例如访问私有字段和引发一般异常。
- 其次，Java / COM接口自动为Java对象提供标准的IUnknown和IDispatch COM接口，以便本机代码可以访问公共方法和字段。 遗憾的是，IDispatch接口不处理重载的Java方法，并且在匹配方法名称时不区分大小写。 此外，通过IDispatch接口公开的所有Java方法都被包装以执行动态类型检查和强制。 这是因为IDispatch接口在设计时考虑了弱类型语言（例如Basic）。
- 第三，COM不是处理单独的低级功能，而是旨在允许软件组件（包括完整的应用程序）协同工作。 我们认为将所有Java类或低级本机方法视为软件组件是不合适的。
- 第四，由于缺乏对UNIX平台的支持，因此立即采用COM受到阻碍

> Although Java objects are not exposed to the native code as COM objects, the JNI interface itself is binary-compatible with COM. JNI uses the same jump table structure and calling convention that COM does. This means that, as soon as cross-platform support for COM is available, the JNI can become a COM interface to the Java VM.

虽然Java对象不作为COM对象公开给本机代码，但JNI接口本身与COM二进制兼容。 JNI使用与COM相同的跳转表结构和调用约定。 这意味着，只要跨平台支持COM，JNI就可以成为Java VM的COM接口。

> JNI is not believed to be the only native method interface supported by a given Java VM. A standard interface benefits programmers, who would like to load their native code libraries into different Java VMs. In some cases, the programmer may have to use a lower-level, VM-specific interface to achieve top efficiency. In other cases, the programmer might use a higher-level interface to build software components. Indeed, as the Java environment and component software technologies become more mature, native methods will gradually lose their significance.

JNI不被认为是给定Java VM支持的唯一本机方法接口。 标准接口使程序员受益，他们希望将本机代码库加载到不同的Java VM中。 在某些情况下，程序员可能必须使用较低级别的VM特定接口来实现最高效率。 在其他情况下，程序员可能使用更高级别的界面来构建软件组件。 实际上，随着Java环境和组件软件技术的日趋成熟，本机方法将逐渐失去意义。

## <a name="5">Programming to the JNI</a>

> Native method programmers should program to the JNI. Programming to the JNI insulates you from unknowns, such as the vendor’s VM that the end user might be running. By conforming to the JNI standard, you will give a native library the best chance to run in a given Java VM.

本机方法程序员应该编程到JNI。 对JNI进行编程可以使您免于未知，例如最终用户可能正在运行的供应商的VM。 通过遵循JNI标准，您将为本机库提供在给定Java VM中运行的最佳机会。

> If you are implementing a Java VM, you should implement the JNI. JNI has been time tested and ensured to not impose any overhead or restrictions on your VM implementation, including object representation, garbage collection scheme, and so on. Please send us your feedback if you run into any problems we may have overlooked.

如果要实现Java VM，则应实现JNI。 JNI已经过时间测试，并确保不对您的VM实施施加任何开销或限制，包括对象表示，垃圾收集方案等。 如果您遇到我们可能忽略的任何问题，请将您的反馈发送给我们。

## <a name="6">Changes</a>

> As of Java SE 6.0, the deprecated structures JDK1_1InitArgs and JDK1_1AttachArgs have been removed, instead JavaVMInitArgs and JavaVMAttachArgs are to be used.

从Java SE 6.0开始，已删除不推荐使用的结构JDK1_1InitArgs和JDK1_1AttachArgs，而是使用JavaVMInitArgs和JavaVMAttachArgs。