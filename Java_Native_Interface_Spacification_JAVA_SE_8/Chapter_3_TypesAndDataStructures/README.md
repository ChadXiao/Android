# Chapter 3: JNI Types and Data Structures
> This chapter discusses how the JNI maps Java types to native C types.

本章讨论JNI如何将Java类型映射到本机C类型。

This chapter covers the following topics:

- <a href="#1"> Primitive Types</a>
- <a href="#2">Reference Types</a>
- <a href="#3">Field and Method IDs</a>
- <a href="#4">The Value Type</a>
- <a href="#5">Type Signatures</a>
- <a href="#6">Modified UTF-8 Strings</a>

## <a name="1">Primitive Types</a>
> The following table describes Java primitive types and their machine-dependent native equivalents.

下表描述了Java基元类型及其与机器相关的本机等效项。

**Primitive Types and Native Equivalents**

|   Java Type  |  Native Type   |  Description   |
| --- | --- | --- |
|  boolean   |  jboolean   |  unsigned 8 bits   |
|  byte   |  jbyte   |  signed 8 bits   |
|  char   | jchar    |   signed 16 bits  |
|   short  |   jshort  |   signed 16 bits  |
|  int   |  jint   |   signed 32 bits  |
|  long   |   jfloat  |  32 bits   |
|  double   | jdouble    | 	64 bits    |
|  void  |   void  |  not applicable   |

> The following definition is provided for convenience.

提供以下定义是为了方便起见。

	#define JNI_FALSE  0
	#define JNI_TRUE   1
> The jsize integer type is used to describe cardinal indices and sizes:

jsize整数类型用于描述基数索引和大小：

	typedef jint jsize;
## <a name="2">Reference Types</a>
> The JNI includes a number of reference types that correspond to different kinds of Java objects. JNI reference types are organized in the following hierarchy:

JNI包含许多与不同类型的Java对象相对应的引用类型。 JNI引用类型按以下层次结构组织：

- jobject
	- jclass (java.lang.Class objects)
	- jstring (java.lang.String objects)
	- jarray (arrays)
		- jobjectArray (object arrays)
		- jbooleanArray (boolean arrays)
		- jbyteArray (byte arrays)
		- jcharArray (char arrays)
		- jshortArray (short arrays)
		- jintArray (int arrays)
		- jlongArray (long arrays)
		- jfloatArray (float arrays)
		- jdoubleArray (double arrays)
	- jthrowable (java.lang.Throwable objects)
	
> In C, all other JNI reference types are defined to be the same as jobject. For example:

在C中，所有其他JNI引用类型都定义为与jobject相同。 例如：

	typedef jobject jclass;	
> In C++, JNI introduces a set of dummy classes to enforce the subtyping relationship. For example:

在C ++中，JNI引入了一组虚拟类来强制执行子类型关系。 例如：

	class _jobject {};
	class _jclass : public _jobject {};
	// ...
	typedef _jobject *jobject;
	typedef _jclass *jclass;
## <a name="3">Field and Method IDs</a>

> Method and field IDs are regular C pointer types:

方法和字段ID是常规C指针类型：

	struct _jfieldID;              /* opaque structure */
	typedef struct _jfieldID *jfieldID;   /* field IDs */

	struct _jmethodID;              /* opaque structure */
	typedef struct _jmethodID *jmethodID; /* method IDs */
## <a name="4">The Value Type</a>
> The jvalue union type is used as the element type in argument arrays. It is declared as follows:

jvalue union类型用作参数数组中的元素类型。 声明如下：

	typedef union jvalue {
		jboolean z;
		jbyte    b;
		jchar    c;
		jshort   s;
		jint     i;
		jlong    j;
		jfloat   f;
		jdouble  d;
		jobject  l;
	} jvalue;
## <a name="5">Type Signatures</a>

> The JNI uses the Java VM’s representation of type signatures. The following table shows these type signatures.

JNI使用Java VM的类型签名表示。 下表显示了这些类型签名。

**Java VM Type Signatures**

|  Type Signature   |   Type Signature  |
| --- | --- |
|  Z   |  	boolean   |
|   B  |  byte   |
|  C   |  	char   |
|   S  |  short   |
|   I  |  	int   |
|   J  | 	long    |
|   F  | 	float    |
|  D   |  	double   |
|   L fully-qualified-class ;  |  fully-qualified-class   |
|  [ type   |   	type[]  |
|  ( arg-types ) ret-type  |  method type  |

> For example, the Java method:
例如，Java方法：

	long f (int n, String s, int[] arr);
	
> has the following type signature:

具有以下类型签名：

	(ILjava/lang/String;[I)J
## <a name="6">Modified UTF-8 Strings</a>

> The JNI uses modified UTF-8 strings to represent various string types. Modified UTF-8 strings are the same as those used by the Java VM. Modified UTF-8 strings are encoded so that character sequences that contain only non-null ASCII characters can be represented using only one byte per character, but all Unicode characters can be represented.

JNI使用修改的UTF-8字符串来表示各种字符串类型。 修改后的UTF-8字符串与Java VM使用的字符串相同。 对已修改的UTF-8字符串进行编码，以便每个字符仅使用一个字节表示仅包含非空ASCII字符的字符序列，但可以表示所有Unicode字符。

> All characters in the range \u0001 to \u007F are represented by a single byte, as follows:

范围\ u0001到\ u007F中的所有字符都由单个字节表示，如下所示：

> -  0xxxxxxx

> The seven bits of data in the byte give the value of the character represented.

字节中的七位数据给出了所表示字符的值。

> The null character ('\u0000') and characters in the range '\u0080' to '\u07FF' are represented by a pair of bytes x and y:

空字符（'\ u0000'）和'\ u0080'到'\ u07FF'范围内的字符由一对字节x和y表示：

> - x: 110xxxxx
> - y: 10yyyyyy

> The bytes represent the character with the value ((x & 0x1f) << 6) + (y & 0x3f).

字节表示具有值（（x＆0x1f）<< 6）+（y和0x3f）的字符。

> Characters in the range '\u0800' to '\uFFFF' are represented by 3 bytes x, y, and z:

'\ u0800'到'\ uFFFF'范围内的字符由3个字节x，y和z表示：

> - x: 1110xxxx
> - y: 10yyyyyy
> - z: 10zzzzzz

> The character with the value ((x & 0xf) << 12) + ((y & 0x3f) << 6) + (z & 0x3f) is represented by the bytes.

具有值（（x＆0xf）<< 12）+（（y＆0x3f）<< 6）+（z＆0x3f）的字符由字节表示。

> Characters with code points above U+FFFF (so-called supplementary characters) are represented by separately encoding the two surrogate code units of their UTF-16 representation. Each of the surrogate code units is represented by three bytes. This means, supplementary characters are represented by six bytes, u, v, w, x, y, and z:

代码点高于U + FFFF的字符（所谓的补充字符）通过分别编码其UTF-16表示的两个代理代码单元来表示。 每个代理代码单元由三个字节表示。 这意味着，补充字符由六个字节u，v，w，x，y和z表示：

> - u: 11101101
> - v: 1010vvvv
> - w: 10wwwwww
> - x: 11101101
> - y: 1011yyyy
> - z: 10zzzzzz


> The character with the value 0x10000+((v&0x0f)<<16)+((w&0x3f)<<10)+(y&0x0f)<<6)+(z&0x3f) is represented by the six bytes.

值为0x10000 +（（v＆0x0f）<< 16）+（（w＆0x3f）<< 10）+（y＆0x0f）<< 6）+（z＆0x3f）的字符由六个字节表示。

> The bytes of multibyte characters are stored in the class file in big-endian (high byte first) order.

多字节字符的字节以big-endian（高字节优先）顺序存储在类文件中。

> There are two differences between this format and the standard UTF-8 format. First, the null character (char)0 is encoded using the two-byte format rather than the one-byte format. This means that modified UTF-8 strings never have embedded nulls. Second, only the one-byte, two-byte, and three-byte formats of standard UTF-8 are used. The Java VM does not recognize the four-byte format of standard UTF-8; it uses its own two-times-three-byte format instead.

此格式与标准UTF-8格式有两点不同。 首先，使用双字节格式而不是单字节格式对空字符（char）0进行编码。 这意味着修改后的UTF-8字符串永远不会嵌入空值。 其次，仅使用标准UTF-8的单字节，双字节和三字节格式。 Java VM无法识别标准UTF-8的四字节格式; 它使用自己的两倍三字节格式代替。

> For more information regarding the standard UTF-8 format, see section 3.9 Unicode Encoding Forms of The Unicode Standard, Version 4.0.

有关标准UTF-8格式的更多信息，请参见3.9 Unicode标准版本4.0的Unicode编码格式。