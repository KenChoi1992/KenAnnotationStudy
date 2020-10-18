## AspectJ 用法

### JointPoint                      PointCut 表达式
    Method Call ->                  call(methodPattern)
    Method execution ->             execution(methodPattern)
    Constructor call ->             call(ConstructorPattern)
    Constructor execution           execution(ConstructorPattern)
    Static initialization           staticInitialization(TypePattern)
    Field get                       get(FieldPattern)
    Field set                       set(FieldPattern)
    Handler                         handler(TypePattern)

基本用法：
```
MethodPattern:
[!][@Annotation][public/protected/private][static/final]返回值类型[类名.]方法名(参数类型列表)[throws 异常类型]

ConstructorPattern:
[!][@Annotation][public/protected/private][static/final][类名.]new(参数类型列表)[throws 异常类型]

FieldPattern:
[!][@Annotation][public/protected/private][static/final]属性类型[类名.]属性名

TypePattern:
其他 Pattern 涉及到的类型规则也是一样，可以使用 '!'、''、'..'、'+'，'!' 表示取反，
'' 匹配除 . 外的所有字符串，'*' 单独使用事表示匹配任意类型，'..' 匹配任意字符串，
'..' 单独使用时表示匹配任意长度任意类型，'+' 匹配其自身及子类，还有一个 '...'表示不定个数


@PointCut(PointCut 表达式)
```

### 切入点执行时机




MethodPattern 示例：
```
@Pointcut("execution(@com.cyg.permissionlib.annotation.PermissionRequest * *(..))&& @annotation(permissionRequest)")
public void getPermission(PermissionRequest permissionRequest) {}

@Around("getPermission(permissionRequest)")
```
切入点为方法执行，方法必须带上 @com.cyg.permissionlib.annotation.PermissionRequest 注解，方法的返回类型和方法名、方法参数未限定
最后通过 @annotation(注解参数名) 后续可以在切入点中得到该方法修饰的注解

@Around 表示替换方法执行，注解中的注解参数名必须和 PointCut 中的一致，此例中注解参数名为 permissionRequest


