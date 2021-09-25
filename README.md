# Socket echo server with swing gui (Win/Linux)
## Info ->
Opened windows are connecting via socket.
![image](https://user-images.githubusercontent.com/45079123/95383198-7bade080-08f3-11eb-8e83-0041631295c4.png)

The window with yellow small square at the left-top -- is the *server* window . 

![image](https://user-images.githubusercontent.com/45079123/95383475-e6f7b280-08f3-11eb-9ca5-a403616f2016.png)

When the connection is established, each window now can create objects on the global plane. Throw balls and cubes with '*serfcxdw*' keys and see everybody else on the plane. Each window has its own objects that are colored yellow and can see other objects of other windows colored white.

### Fast Bash deploy 

Find .jar file in main dir, and run 
``` bash  
$ java -jar socket_echo_server_swing.jar 
``` 

### Tested 
* On Windows 10 home 
* On Ubuntu20

### NO JAVA??

To run .jar file, you need to have java or jdk installed (**IntelliJ** jdk is ok).

#### **IntelliJ SDK**
~~~
$ java 
~~~

If there is no java in PATH, you should add it on your own:    

#### Linux
``` bash
 $ export PATH="$HOME/example/.jdks/openjdk-15/bin:$PATH" 
``` 
In my way, the **example** was empty because  .jdks lay in home dir.

#### Windows 
[Link Here](https://www.architectryan.com/2018/03/17/add-to-the-path-on-windows-10/)

 
#### No **IntelliJ SDK**

[Download Java via apt](https://opensource.com/article/19/11/install-java-linux)  
Type to check 
~~~
$ java 
~~~

***

