<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<html>
<head>
    <title>welcome</title>
    <script type="text/javascript">
        //得到时间并写入div
        function getDate(){
            let date = new Date();
            let date1 = date.toLocaleString();
            let div1 = document.getElementById("times");
            div1.innerHTML = date1;
        }
        setInterval("getDate()",1000);
    </script>
</head>
<body>
<h2>
    欢迎访问在线考试系统服务!!<br>
    Welcome to the online exam system's server!! <br>
    <div id="times"></div>
</h2>
</body>
</html>
