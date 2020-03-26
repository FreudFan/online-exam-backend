
function checkToken(status){
    if(status == 401){
        alert("登录失效,请重新登录!!!!");
        location.href="login.html";
    }
}