
function checkToken(status){
    if(status == 401){
        alert("登录失效,请重新登录!!!!");
        sessionStorage.removeItem("token");
        location.href="login.html";
    }else if(status == 400){
      alert("题库中,题目数量不足!!!")
    }
}

function exitSystem(){

    if(window.confirm("确定要退出吗?")){
        sessionStorage.clear();
        location.href='login.html';
    }

}

axios.defaults.headers.get['Authorization'] = sessionStorage.getItem("token");
axios.defaults.headers.post['Authorization'] = sessionStorage.getItem("token");