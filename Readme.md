# MUA 程序语言介绍

[MUA language.md](MUA language.md)

# 解释器

### 数据结构

##### 接口

​	Token 四种数据结构的统一接口

​	Comparable 为MWord, MNumber提供比较操作的接口

​	WordOrList 为MWord, MNumber提供字表操作的接口

##### 类

​	MNumber 数字类型

​	MWord 字类型

​	MBool 布尔类型

​	MList 表类型

### 解释流程

​	Lexer 将输入的字符串转换成Token序列

​	Parser 解释并执行Token序列

​	Executable 接口，为MOperation类提供lambda表达式的接口

​	MOperation 枚举类，定义了所有操作的方法体

### 其他功能

​	MSign 定义了几个常用符号，简化了Lexer处理表类型、进行表达式计算的流程

​	ExpressionCalculator 提供表达式计算的功能，使用递归下降进行实现

​	StopException 提供终止函数执行功能的异常类

### 程序入口

​	Main