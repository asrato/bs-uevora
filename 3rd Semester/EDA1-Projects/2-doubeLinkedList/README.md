# Big Integer
Second project for the Data Structures and Algorithms I subject of the Computer Engineering Degree. 

## 1. Objectives - Usage and Linked Lists Implementation  
&nbsp;&nbsp;&nbsp;It's intended that you implement **Linked Lists**, in an efficient way and provide a program that uses this data structure to represent integer numbers that exceed the order of greatness of the Java integers (Integer and Long). 

## 2. The Project  
&nbsp;&nbsp;&nbsp;The project consists of the presentation of a package consisting of two required Java classes and two interfaces. Don't forget that lists are iterable and that your list implementation should provide this functionality. For simplicity the interfaces of both classes are provided.  
&nbsp;&nbsp;&nbsp;Your `BigInt` classe must create a constructor that takes a string of digits and converts it to a `BigInt` "5675300255355316716367366371" will be a posible `BigInt` although "4" too!  
&nbsp;&nbsp;&nbsp;Then List and AstroInt interfaces are presented only as indicators of the functionality that must povide the classes.   
**Lista.java interface:**
```java
public interface Lista <T> {
  void add(T x);
  void add(int i, T x);
  void set(int i, T x);
  T remove(int i);
  void remove(int i);
  void clear();
  T get(int i);
  int size();
  public String toString();
}
```
**AstroInt.java interface:**  
```java
public interface AstroInt {
  AstroInt add(AstroInt x);
  AstroInt sub(AstroInt x);
  AstroInt mult(AstroInt x);
  AstroInt div(AstroInt x);
  AstroInt mod(AstroInt x);
  public String toString();
}
```  

&nbsp;&nbsp;&nbsp;You can only use classes from `java.util` package that allows you to handle input (if you need it).
