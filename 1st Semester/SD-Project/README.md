# Washing Machine
Final project for the Digital Systems subject of the Computer Engineering Degree.

## 1. Problem Description
&nbsp;&nbsp;&nbsp;The main goal is to create a washing machine control system. The machine is composed by the following modules:  
* machine control module;  
* water control module;  
* wash control module.  

&nbsp;&nbsp;&nbsp;In addition to the control modules, the machine has the following sensors and buttons:  
* **Start button (BI):** the machine must start working when this button takes the value 1;  
* **Water level sensor (SNA):** this sensor takes the value 1 when the water reaches the level necessary to start washing; when the machine doesn't have water inside, it takes the value 0;  
* **Water temperature sensor (STA):** this sensor takes the value 1 when the water reaches the temperature required to start washing;  

and the following components:  
* **Water inlet valve (VA):** this valve serves to let water enter the machine; when it receives the value 1, the valve is open and lets in water;  
* **Water heating resistor (AQ):** this resistor serves to heat the washing water; when set to 1, the resistance is on and heats the water;  
* **Motor rotates do right (MD):** this motor input serves to rotate the motor to the right; when given the value 1, the motor rotates to the right;
* **Motor rotates do left (ME):** this motor input serves to rotate the motor to the left; when given the value 1, the motor rotates to the left;  
* **Water pump (BA):** the water pump serves to remove water from the machine at the end of the washing cycle; when taking the value 1, the pump is activated and the water is expelled from the machine.  

## 2. Operating Mode
&nbsp;&nbsp;&nbsp;When starting the system, all machine sensores must have the value 0 and all modules must be stopped. When the `BI` takes the value 1, the `VA` must be activated, being deactivated only when the water reaches the necessary level to start the washing process. Then the `RAQ` must be activated, being deactivated only when the water reaches the necessary temperature to start washing the clothes.  
&nbsp;&nbsp;&nbsp;After the water has reached the necessary temperature to start the washing, the motor must rotate to the right (`MD`) for 2 clock cycles, rotate to the left (`ME`) for 2 clock cycles, and then activate the `BA`. The `BA` should continue to work until the `SNA` is no longer active.  
&nbsp;&nbsp;&nbsp;After the water is removed from the machine, the motor must be activated in spin mode during 1 cycle and then stop. During centrifugation the `BA` must continue to work. After the spin stops, the machine should stop and be ready to start another wash cycle.  

## 3. Implementation
&nbsp;&nbsp;&nbsp;The machine controller must be implemented through 3 independent modules, which must be linked together in order to implement the complete system:  
* machine control module;
* water control module;
* wash control module.  

&nbsp;&nbsp;&nbsp;You have the option to implement:  
* Machine control module + Water control module + Wash control module; 
* Water Control Module + Wash Control Module.

&nbsp;&nbsp;&nbsp;Each of the controllers must include the necessary inputs and outputs for its good operation, including those that are necessary so that the 3 modules can be connected between themselves. Don't forget that some outputs from some modules are inputs from other modules.  
&nbsp;&nbsp;&nbsp;Implement the 3 modules separately. For each of the modules, follow the usual procedure for the synthesis of sequential circuits:  
1. Clearly define the inputs and outputs of the circuit.
2. Draw the ASM model; don't forget to include the mnemonics and state coding, the boolean expressions associated with the decision boxes and the value for the following state.
3. Write the state transition and output tables; be consistent with coding presented in the ASM model.
4. Choose the type of flip-flop to use (D, JK or T).
5. Find the input equations for flip-flops and outputs; use the excitation tables of the flip-flops chosen to draw the Karnaugh maps and extract the simplified equations.
6. Design the simplified circuit in the Logisim simulator and test it.

&nbsp;&nbsp;&nbsp;Once the 3 modules are implemented, combine them and test them in Logisim in order to implement the machine control system.
