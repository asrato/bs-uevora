# Phone Book
Third project for the Data Structures and Algorithms I subject of the Computer Engineering Degree. 

## 1. Objectives - Usage and Binary Seacrh Trees Implementation  
&nbsp;&nbsp;&nbsp;It's intended that you implement **Binary Search Trees**, in an efficient way and provide a program that uses this data structure to create a phone book.  

## 2. The Project  
&nbsp;&nbsp;&nbsp;The project consists of the presentation of a package consisting of three required Java classes. 

#### 2.1. Trees
&nbsp;&nbsp;&nbsp;The implementation of trees should follow what is usual for this type of data structures.  
&nbsp;&nbsp;&nbsp;The implmentations you present must be "stand-alone". It should be possible with your implemntations create ABPs of Integer, String, or any other comparable type. Your trees must be iterable. Te iterator can iterate the trees as you wish (in order, by levels, etc).  
&nbsp;&nbsp;&nbsp;For AVL trees, you can use the "lazy deletion". After the implemenation of the trees, under the conditions listed, you can move towards the implementation of the mobile agenda.  

#### 2.2. The Agenda
&nbsp;&nbsp;&nbsp;The phone book willstore a list of contacts. A `Contacto` is identfied by the name (ID) and by one or more phone numbers. It should be possible to perform the following basic operations on the agenda.  
&nbsp;&nbsp;&nbsp;It's nt allowed to store contacts in an array, queue, stack or linked list. You are also prohibited from using any data structure imported from `java.util`.  
&nbsp;&nbsp;&nbsp;Don't create menus to make your agenda interactive.  

**Functions:**  
* `ADICIONAR`: adds a new contact (ID and phone number);  
* `EDITAR`: allows, using the id or any phone number, to change a contact (you can edit the id, a nmuber or even add a new phone number);  
* `REMOVER`: allows, using the id or any phone number, to remove a contact;  
* `LISTAR`: lists agenda's content, by alphabetical order;  
* `CHAMADOR`: diplays the id associated to the number in `CHAMADOR` (if doesn't exists displays "DESCONHECIDO").
