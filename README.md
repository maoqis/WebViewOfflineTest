

两种webview离线包方案比较<br>
====

方案一：html中的css，js等资源链接为相对路径，相对于hmtl目录的路径。<br>
===

离线资源包结构<br>
==
```
testAbsolute
  css
    example.css
  js
  example.js
  ExampleApp.html
```

  
  
ExampleApp.html文件中引用链接相对路径，css/example.css<br>
==
```
<link rel="stylesheet" href="css/example.css">
```
  
  




方案二：html中的资源连接为https等协议开头的url绝对路径。
===
eg：https://xxx.xxx.xxx/testAbsolute/js/example.js

离线资源包结构
==
testRelative<br>
  css<br>
    example.css<br>
  js<br>
    example.js<br>
  ExampleApp.html<br>





