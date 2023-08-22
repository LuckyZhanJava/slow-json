# slow json：一个简易的json解析器
这是一个json解析器实现，只支持简单的json转换成对象，一些细节尚未处理，在实现json转换成复杂对象之前，这是一个单纯的json解析器，只能把json转换成通用的`Map<String,Object>`类型，这个过程大概经历了三个步骤：
1. 词法分析。
2. 语法分析。
3. 解释执行。

json有自己的语法，它自身的类型很有限，不需要指定具体的java类型即可提供默认的类型与之一一对应。关于json的语法可以参看：[https://www.json.org/json-en.html](https://www.json.org/json-en.html)。总的来说分为以下类型：

1. number：代表数字，可能是整数，也可能浮点数，可能很长，与之对应的java类型可以用`BigDecimal`。
2. string：代表字符串，可能是长字符，也可能只有一个字符，对应的可以转成`String`类型，如果指定目标类型为`char`，那也可以将长度为1的string转为`char`。
3. boolean：只有两个值 true 和 false，对应java的`boolean`。
4. null：空值，对应java的表示也为`null`。
5. object：类似java中的类，key为string，value为object类型，object类型可以是这6项中的任意类型，这里没有具体的类，我们直接把`Map`与之对应。
6. array：代表数组，数组的元素可以是以上任意类型，也可能是array类型，与之对应的java类型是`Object[]`。

关于类型的讨论，只是简单的描述了一下最后的转换方向，关注点还是转移到前边提到的步骤中。

## 1.词法分析
一段json字符串可以理解为一个字符流，词法分析主要是把字符流转换成`Token(标识)流`。Token由字符组成，它们向上又组成了语法。以一段java的类定义为例：`class Demo{}`。这段定义由几个部分组成：
1. `class`标识符（它是一段特殊的字符串）。
2. `Demo`一段普通的字符串代表类名。
3. 然后是类的内容开始符号`{`。
4. 最后是类定义的结束符号`}`。

词法分析器可以把上述定义转换为token流，输出内容为：`ClassDefineToken(text="class")`, `SimpleNameToken(text="Demo")`, `SkipToken(text="{")`, `SkipToken(text="}")`。

也就是说：

1. 每次开始词法分析时，向后逐渐读入的内容最后必定对应一个类型的token，如果没有，那么可能存在词法错误。
2. token和token之间的空格通常是可以忽略掉的，对于一个字符串类型的token`StringToken(text="a b")`，这中间的空格不能省略。

词法分析器的实现可能还需要用到类似`lookAhead(k)`的实现可以向前查看k个字符, 比如对于`class`和`classes`两个内容，必须向前看1位才能决定是否停止读取返回token，以上两段符号对应不同的token类型：`ClassDefineToken(text="class")`和`SimpleNameToken(text="classes")`。

## 2.语法分析
语法分析和词法分析有些类似，语法分析器从词法分析器中读出一个一个的token，然后根据读出的内容把这些token组成语法树的节点`ASTNode`，与词法分析不同的是，语法分析可能会彼此嵌套，比如对于`[{"array":[1]}]`，这样一个数组array，语法分析器可能先读出第一个token`[`，认定这将是一个数组形式的语法节点，但是下一步，读出的token是`{`，它是object语法的开始，而object语法读取的过程中会发现，自己的内部可能也有array形式的语法。这些语法之间相互嵌套，在实现上需要有一个有效的处理办法。

几乎所有的语言都是上下文无关文法，它足够简单，字符所代表的含义与它所处的上下文无关，BNF经常用来表达上下文无关文法。通常可以简单分为以下4种匹配方式：

1. {pat} 模式至少重复0次，可以命名为：repeat。
2. [pat] 模式重复0到1次，可以命名为：option。
3. pat1 | pat2 与pat1或者pat2匹配，可以命名为：or。
4. () 括号内代表一个完整的模式。

于是，我们可以用这种方法来表示json的语法，实际上json的语法在开始的链接中已经提供，我们在这里简单描述如下：

1. string : "{.}"代表一段文本以`"`开头和结尾，中间是任意字符。
2. number : ([-|+]\d{\d}(.\d{\d}|{\d})) 代表可能有正负和小数点的数字，实际的定义要更复杂。
3. boolean : (true | false) 只有`true`和`false`两种情况。
4. null ： null 只有`null·一种情况。
5. array : ('['']' | '[' value, {, value} ']') 以`[`开头以`]`结尾，元素类型为`value`。
6. object : '{''}' | '{' key : value {, key : value } '}' 以`{`开头以`}`结尾，内部为string-value的key-value对。
7. value : (string | number | boolean | null | array | object) 可能为以上任意类型。
8. json : (object | array) 可能为object类型也可能为array类型。

语法树的作用关乎它的实现，基本上语法树有两种作用：

1. 校验语法，不能出现错误的语法。
2. 简单执行，根据语法树内组织的节点，简单执行，以执行为目的可以帮助理解应该如何组织`ASTNode`。

还是以`[{"array":[1]}]`为例，假定我们认为给定的是一个json片段，我们需要用json语法解析器来处理输入，语法分析器已经准备好了自身的工作，接下来该语法分析工作了。

### 2.1 校验语法

1. 由于json是由object和array两种类型组成的，所以json解析器内部存放了两个解析器分别是：object解析器和array解析器。
2. 由于解析过程需要试探到底属于哪个分支，所以又用到了`lookAhead(k)`的方式，解析器只简单的向前看一位`token`即可决定是否可以由自己处理，这种方式和直接解析的方式的差别在于，向前看并不消耗`token`，直接解析如果出错想要退回读取到的`token`并不容易。
3. 通过简单的向前看，这里选定array解析器进行解析。
4. array内部元素由value组成，于是array解析器的内部是一个value解析器。
5. value解析器由多种类型组成，那它的内部解析器的类型更多。
6. 各种解析器相互依赖，层层委派即可完成最简单的语法校验工作。假设上述字符串的开始为`$`字符，那么array和object解析器会发现都不是自己能解析的类型，那么直接抛出异常停止解析即可，这时是出现了语法错误。

### 2.2 简单执行

语法分析的结果通常可以直接转化为具体的结果，还是以上述片段为例：

1. array解析器解析的结果是一个只有一个值组成的数组，这个值由object解析器返回。
2. object解析器返回的值则由其他解析器返回。
3. 这里并不急于执行，所以解析器都只返回一个结构一致带有执行方法的类的对象，在需要时再执行。大致结构如下：
```java
public interface ASTNode {
    Object eval();
}
```
那么对于上述array解析器，它可能返回这样一个`ASTNode` :
```java
public interface ASTNode {
    List<ASTNode> astList = ...;

    ASTNode valueNode = objectParse.parse(...);
    astList.add(valueNode);

    Token next = nextToken();
    while(!isSkip(next, ']')){
      skip(',');
      valueNode = objectParse.parse(...);
      astList.add(valueNode);
      next = nextToken();
    }
    
    Object eval(){
      Object[] values = ...;
      for(int i = 0; i < astList.size(); i++){
        values[i] = astList.get(i).eval();
      }
      return values;
    }
}
```

## 3.json解析器的实现

最后的解析执行会交由非常具体的解析器来执行，根据解析器类型的不同返回的结果可能是`String`，`BigDecimal`，`Boolean`等等。

但是，常用的json解析器都指定了目标类型，字段的类型也非常具体，比如：`int`, `double`等等。

对于可以用字符串直接表示的一些类型来说，比如：数字类型，字符类型，日期类型。必须先执行返回`String`，然后通过某种可以完成类型转换的转换器进行转换。

对于数组或者集合类型来说是个例外，如果不指定类型，那么可能会默认返回一个由`String`，`BigDecimal`，`Boolean`等类型作为元素的`Object[]`，我们需要再把数组转换成目标类型的数组或者集合，比如：`Integer[]`或者`Set<Integer>`等等，这个过程可能会略微耗费性能。所以对于数组类型的节点的处理，我们可以提前取出目标元素类型，在元素解析器解析时直接返回目标类型的对象。

由于泛型，继承，重载带来了诸多复杂度，这里的实现对它们的支持并不完善，只做简单实现。





