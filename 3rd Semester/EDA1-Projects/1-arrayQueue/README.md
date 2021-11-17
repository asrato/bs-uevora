# Bus Stop
First project for the Data Structures and Algorithms I subject of the Computer Engineering Degree. 

## 1. Objectives - Usage and Queues Implementation  
&nbsp;&nbsp;&nbsp;It's intended that you implement **Queues**, in an efficient way and provide a program that uses this daa structure to simulate the queue management at a bus stop.  

## 2. The Project  
&nbsp;&nbsp;&nbsp;The project consists of the presentation of a package consisting of three required Java classes and an interface. A group of people arrives at the bust stop at a certain hour. When other groups arrive later, they will go to the end of the line. The number of people os an attribte of the group, but there is no problem with the group having only 1 person.  
&nbsp;&nbsp;&nbsp;There is only one bus stop. The bus has seats available and takes all passengers who are in the queue for the maximum number ir spaces. If necessary, the group of people is split, standing in the beginning of the queue, a smaller group. When the bus takes people, the average waiting time for people on the bus must e calculated.  
&nbsp;&nbsp;&nbsp;The queue class must redefine the `toString()` method in order to allow viewing always as possible the content of the queue. I will also be useful to define a `BusData` class that implements the time and number of people arriving at the stop,and by the way the `toString()` method for this class.  
&nbsp;&nbsp;&nbsp;An usage exmaple is given by the following `main` of the `BusStop` class whose output is also reproduced below:  
**Input:**
```java
public static void main (String[] args) {
  BusStop b23 = new BusTop();
  b23.chega_grupo(14, 14, 3);
  b23.chega_grupo(14, 18, 2);
  System.out.println(b23.fila);
  b23.chegada_bus(14, 29, 4);
  b23.chegada_bus(14, 40, 10);
}
```
**Output:**  
```
Hora 14:14 3 pessoas chegam, ficam 3 na fila
Hora 14:18 2 pessoas chegam, ficam 5 na fila
[14:14 Grupo 3 pessoas; 14:18 Grupo 2 pessoas]
Chega Bus -> 14:29 há 4 vagas no bus
Hora 14:14 chegaram 3 ficam 0
Hora 14:18 chegaram 2 ficam 1
Tempo médio de espera: 13m
Chega Bus -> 14:40 há 10 vagas no bus
Hora 14:18 chegaram 1 ficam 0
Tempo médio de espera: 22m
```  

&nbsp;&nbsp;&nbsp;You can only use classes from `java.util` package that allows you to handle input (if you need it).
