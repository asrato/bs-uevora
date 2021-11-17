# EDA2-Problem.SpreadingTheNews
Graph related problem for pratical classes of Structures and Data Algorithms subject.

## Problem: Spreading The News
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;In a large organization, everyone knows a lot of colleagues.  However, friendship relations are kept with only a few of them, to whom news are told. <br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Suppose  that  whenever  an  employee  knows  of  a  piece  of  news,  he  tells  it  to  all  his friends on the following day.  So, on the first day, the source of the information tells it to his friends; on the second day, the source’s friends tell it to their friends; on the third day,the friends of the source’s friends’ tell it to their friends; and so on.<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The goal is to determine:
* *the maximum daily boom size*, which is the largest number of employees that, on a single day, hear the piece of news for the first time;
* *the first boom day*, which is the first day on which the maximum daily boom size occurs.

## Task
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Write a program that, given the friendship relations between the employees and the source of a piece of news,  computes the maximum daily boom size and the first boom day of that information spreading process.

## Input
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The first line of the input contains the number *E*  of employees (1 ≤ *E* ≤ 2500). Employees are numbered from 0 to *E*−1.<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Each of the following *E* lines specifies the set of friends of an employee’s (from employee 0  to  employee *E*−1).   A  set  of  friends  contains  the  number  of  friends *N* (0 ≤ *N* ≤ 15), followed by *N* distinct integers representing the employee’s friends.  All integers are separated by a single space.<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The next line contains an integer *T* (1 ≤ *T* < 60), which is the number of test cases.<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Each  of  the  following *T* lines  contains  an  employee,  which  represents  the  (unique) source of the piece of news in the test case.

## Output
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The output consists of *T* lines, one for each test case.<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;If no employee (but the source) hears the piece of news, the output line contains the integer 0.<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Otherwise,  the  output  line  contains  two  integers, *M* and *D*,  separated  by  a  single space, where *M* is the maximum daily boom size and *D* is the first boom day.

## Sample Input
```
6
2 1 2
2 3 4
3 0 4 5
1 4
0
2 0 2
3
0
4
5
```

## Sample Output
```
3 2
0
2 1
```
