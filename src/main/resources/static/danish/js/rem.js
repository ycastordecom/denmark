;
(function(doc, win, uiSize, prem) {
    // 1.html元素
    let html = doc.documentElement

    // 2.声明事件名称
    let resizeEvent = 'orientationchange' in win ? 'orientationchange' : 'resize'

    // 3.声明计算html的font-size的函数
    function recalculate() {
        let clientWidth = html.clientWidth
        if (!clientWidth) {
            return
        }
        let rate = clientWidth / uiSize
        html.style.fontSize = rate * prem + 'px'
    }
    // 4.监听窗口缩放事件
    win.addEventListener(resizeEvent, recalculate, false)
        // 5.监听文档加载结束事件
    doc.addEventListener('DOMContentLoaded', recalculate, false)
      
})(document, window, 1040, 100)